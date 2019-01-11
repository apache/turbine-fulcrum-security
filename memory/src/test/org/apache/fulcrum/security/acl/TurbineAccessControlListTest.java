package org.apache.fulcrum.security.acl;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.fulcrum.security.GroupManager;
import org.apache.fulcrum.security.PermissionManager;
import org.apache.fulcrum.security.RoleManager;
import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.UserManager;
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.turbine.TurbineAccessControlList;
import org.apache.fulcrum.security.model.turbine.TurbineModelManager;
import org.apache.fulcrum.security.util.GroupSet;
import org.apache.fulcrum.security.util.PermissionSet;
import org.apache.fulcrum.security.util.RoleSet;
import org.apache.fulcrum.testcontainer.BaseUnit5Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Test that we can generate AccessControlLists from the Factory
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id: AccessControlListTest.java 1791100 2017-04-12 09:48:44Z gk $
 */
public class TurbineAccessControlListTest extends BaseUnit5Test
{

    private UserManager userManager;
    private GroupManager groupManager;
    private RoleManager roleManager;
    private TurbineModelManager modelManager;
    private PermissionManager permissionManager;
    private TurbineAccessControlList acl;
    private static int counter = 1;
    private User user;

    @BeforeEach
    public void setUp() throws Exception
    {

    	this.setRoleFileName("src/test/TurbineMemoryRoleConfig.xml");
        this.setConfigurationFileName("src/test/TurbineMemoryComponentConfig.xml");

        SecurityService securityService = (SecurityService) lookup(SecurityService.ROLE);
        userManager = securityService.getUserManager();
        groupManager = securityService.getGroupManager();
        roleManager = securityService.getRoleManager();
        permissionManager = securityService.getPermissionManager();
        modelManager = (TurbineModelManager) securityService.getModelManager();
        
        user = userManager.getUserInstance("User 1");
        if (!userManager.checkExists(user)) {
        	userManager.addUser(user, "secretpassword");
        }
        
    }
    
    @Override
    @AfterEach
    public void tearDown()
    {
        userManager = null;
        groupManager = null;
        roleManager = null;
        modelManager = null;
        permissionManager = null;
        user = null;
    }

    @Test
    public void testCreatingDefaultAccessControlListViaFactory() throws Exception
    {
        Group group = getGroup();
        Role role = getRole();
        Role globalRole = getRole();
        Permission permission = getPermission();
        modelManager.grant(user, modelManager.getGlobalGroup(), globalRole);
        modelManager.grant(user, group, role);
        modelManager.grant(role, permission);
        RoleSet roleSet = new RoleSet();
        PermissionSet permissionSet = new PermissionSet();
        roleSet.add(role);
        permissionSet.add(permission);
        Map<Group, RoleSet> roleSets = new HashMap<Group, RoleSet>();
        Map<Group, PermissionSet> permissionSets = new HashMap<Group, PermissionSet>();
        roleSets.put(group, roleSet);
        permissionSets.put(group, permissionSet);
        AccessControlList acl = userManager.getACL(user);
        assertTrue(acl instanceof TurbineAccessControlList);
        TurbineAccessControlList dacl = (TurbineAccessControlList) acl;
        assertTrue(dacl.hasRole(role,group));
        assertTrue(dacl.hasRole(globalRole));
        assertTrue(dacl.hasPermission(permission,group));     
        assertEquals(dacl.getPermissions(group).toString(),permissionSets.get(group).toString());
        Serializable serDeSer = SerializationUtils.roundtrip(acl);
        assertTrue( ((TurbineAccessControlList)serDeSer).getRoles().toString().equals(dacl.getRoles().toString()),
                "Expected RoleSet: [role 4 -> 4]");

    }
    @Test
    public void testGetRolesGroup() throws Exception
    {
        Role role = getRole();
        Role role2 = getRole();
        Role role3 = getRole();
        modelManager.grant(user, modelManager.getGlobalGroup(), role);
        modelManager.grant(user, modelManager.getGlobalGroup(), role2);
        acl = userManager.getACL(user);
        RoleSet resultRoleSet = acl.getRoles();
        assertTrue(resultRoleSet.contains(role));
        assertTrue(resultRoleSet.contains(role2));
        assertFalse(resultRoleSet.contains(role3));
    }

    /*
     * Class to test for RoleSet getRoles()
     */
    @Test
    public void testGetRoles() throws Exception
    {
        Group group = getGroup();
        Group group2 = getGroup();
        Role role = getRole();
        Role role2 = getRole();
        Role role3 = getRole();
        modelManager.grant(user,group, role);
        modelManager.grant(user,group, role2);
        modelManager.grant(user,group2, role2);
        modelManager.grant(user,group2, role3);
        acl = userManager.getACL(user);
        RoleSet resultRoleSet = acl.getRoles(group);
        assertTrue(resultRoleSet.contains(role));
        assertTrue(resultRoleSet.contains(role2));
        assertTrue(!resultRoleSet.contains(role3));
        assertEquals(2, resultRoleSet.size());
    }

    /*
     * Class to test for PermissionSet getPermissions(Group)
     */
    @Test
    public void testGetPermissionsGroup() throws Exception
    {
        Group group = getGroup();
        Group group2 = getGroup();
        Role role = getRole();
        Role role2 = getRole();
        Role role3 = getRole();
        Permission permission = getPermission();
        Permission permission2 = getPermission();
        Permission permission3 = getPermission();
        modelManager.grant(user,group, role);
        modelManager.grant(user,group, role2);
        modelManager.grant(user,group2, role3);
        modelManager.grant(role, permission);
        modelManager.grant(role, permission2);
        modelManager.grant(role, permission3);
        modelManager.grant(role2, permission2);
        modelManager.grant(role2, permission3);
        acl = userManager.getACL(user);
        PermissionSet resultPermissionSet = acl.getPermissions(group);
        assertEquals(3, resultPermissionSet.size());
        assertTrue(resultPermissionSet.contains(permission));
        assertTrue(resultPermissionSet.contains(permission2));
        assertTrue(resultPermissionSet.contains(permission3));
        resultPermissionSet = acl.getPermissions(group2);
        assertEquals(0, resultPermissionSet.size());
    }

    /*
     * Class to test for PermissionSet getPermissions()
     */
    @Test
    public void testGetPermissions() throws Exception
    {
        Group group = getGroup();
        Group group2 = getGroup();
        Role role = getRole();
        Role role2 = getRole();
        Role role3 = getRole();
        Permission permission = getPermission();
        Permission permission2 = getPermission();
        Permission permission3 = getPermission();
        modelManager.grant(user,group, role);
        modelManager.grant(user,group, role2);
        modelManager.grant(user,group2, role3);
        modelManager.grant(role, permission);
        modelManager.grant(role, permission2);
        modelManager.grant(role, permission3);
        modelManager.grant(role2, permission2);
        modelManager.grant(role2, permission3);
        acl = userManager.getACL(user);
        PermissionSet resultPermissionSet = acl.getPermissions(group);
        assertEquals(3, resultPermissionSet.size());
    }

    /*
     * Class to test for boolean hasRole(Role, Group)
     */
    @Test
    public void testHasRoleRoleGroup() throws Exception
    {
        Group group = getGroup();
        Group group2 = getGroup();
        Role role = getRole();
        Role role2 = getRole();
        Role role3 = getRole();
        modelManager.grant(user,group, role);
        modelManager.grant(user,group, role2);
        modelManager.grant(user,group2, role);
        modelManager.grant(user,group2, role3);
        acl = userManager.getACL(user);
        assertTrue(acl.hasRole(role, group));
        assertTrue(acl.hasRole(role, group2));
        assertTrue(acl.hasRole(role2, group));
        assertFalse(acl.hasRole(role2, group2));
        assertTrue(acl.hasRole(role, group2));
        assertFalse(acl.hasRole(role2, group2));
        assertTrue(acl.hasRole(role3, group2));
    }

    /*
     * Class to test for boolean hasRole(Role, GroupSet)
     */
    @Test
    public void testHasRoleRoleGroupSet() throws Exception
    {
        Group group = getGroup();
        Group group2 = getGroup();
        Role role = getRole();
        Role role2 = getRole();
        Role role3 = getRole();
        modelManager.grant(user,group, role);
        modelManager.grant(user,group, role2);
        modelManager.grant(user,group2, role);
        modelManager.grant(user,group2, role3);
        acl = userManager.getACL(user);
        GroupSet groupSet = new GroupSet();
        groupSet.add(group);
        assertTrue(acl.hasRole(role, groupSet));
        assertTrue(acl.hasRole(role2, groupSet));
        assertFalse(acl.hasRole(role3, groupSet));
        groupSet.add(group2);
        assertTrue(acl.hasRole(role, groupSet));
        assertTrue(acl.hasRole(role2, groupSet));
        assertTrue(acl.hasRole(role3, groupSet));
        groupSet.add(group2);
    }

    /*
     * Class to test for boolean hasRole(String, String)
     */
    @Test
    public void testHasRoleStringString() throws Exception
    {
        try {
            Group group = getGroup();
            Group group2 = getGroup();
            Role role = getRole();
            Role role2 = getRole();
            Role role3 = getRole();
            modelManager.grant(user, group, role);
            modelManager.grant(user, group, role2);
            modelManager.grant(user, group2, role);
            modelManager.grant(user, group2, role3);
            acl = userManager.getACL(user);
            assertTrue(acl.hasRole(role.getName(), group.getName()));
            assertTrue(acl.hasRole(role.getName(), group2.getName()));
            assertTrue(acl.hasRole(role2.getName(), group.getName()));
            assertFalse(acl.hasRole(role2.getName(), group2.getName()));
            assertTrue(acl.hasRole(role.getName(), group2.getName()));
            assertFalse(acl.hasRole(role2.getName(), group2.getName()));
            assertTrue(acl.hasRole(role3.getName(), group2.getName()));
        } catch (Exception e) {
            fail("failed with " + e.getMessage());
        }
        
    }

    /*
     * Class to test for boolean hasPermission(Permission, Group)
     */
    @Test
    public void testHasPermissionPermissionGroup() throws Exception
    {
        Group group = getGroup();
        Group group2 = getGroup();
        Role role = getRole();
        Role role2 = getRole();
        Role role3 = getRole();
        Permission permission = getPermission();
        Permission permission2 = getPermission();
        Permission permission3 = getPermission();
        Permission permission4 = getPermission();
        modelManager.grant(user, group, role);
        modelManager.grant(user, group, role2);
        modelManager.grant(user, group2, role3);
        modelManager.grant(role, permission);
        modelManager.grant(role, permission2);
        modelManager.grant(role, permission3);
        modelManager.grant(role2, permission2);
        modelManager.grant(role2, permission3);
        modelManager.grant(role3, permission4);
        acl = userManager.getACL(user);
        assertTrue(acl.hasPermission(permission, group));
        assertTrue(acl.hasPermission(permission2, group));
        assertTrue(acl.hasPermission(permission3, group));
        assertFalse(acl.hasPermission(permission4, group));
        assertTrue(acl.hasPermission(permission4, group2));
        assertFalse(acl.hasPermission(permission, group2));
    }

    /*
     * Class to test for boolean hasPermission(Permission, GroupSet)
     */
    @Test
    public void testHasPermissionPermissionGroupSet() throws Exception
    {
        Group group = getGroup();
        Group group2 = getGroup();
        Group group3 = getGroup();
        Role role = getRole();
        Role role2 = getRole();
        Role role3 = getRole();
        Role role4 = getRole();
        Permission permission = getPermission();
        Permission permission2 = getPermission();
        Permission permission3 = getPermission();
        Permission permission4 = getPermission();
        Permission permission5 = getPermission();
        modelManager.grant(user,group, role);
        modelManager.grant(user,group, role2);
        modelManager.grant(user,group2, role3);
        modelManager.grant(user,group3, role4);
        modelManager.grant(role, permission);
        modelManager.grant(role, permission2);
        modelManager.grant(role, permission3);
        modelManager.grant(role2, permission2);
        modelManager.grant(role2, permission3);
        modelManager.grant(role3, permission4);
        modelManager.grant(role4, permission5);
        acl = userManager.getACL(user);
        GroupSet groupSet = new GroupSet();
        groupSet.add(group);
        groupSet.add(group2);
        assertTrue(acl.hasPermission(permission, groupSet));
        assertFalse(acl.hasPermission(permission5, groupSet));
        groupSet.add(group3);
        assertTrue(acl.hasPermission(permission5, groupSet));
    }

    /*
     * Class to test for boolean hasPermission(Permission)
     */
    @Test
    public void testHasPermissionPermission() throws Exception
    {
        Role role = getRole();
        Role role2 = getRole();
        Role role3 = getRole();
        Role role4 = getRole();
        Permission permission = getPermission();
        Permission permission2 = getPermission();
        Permission permission3 = getPermission();
        Permission permission4 = getPermission();
        Permission permission5 = getPermission();
        modelManager.grant(user,modelManager.getGlobalGroup(), role);
        modelManager.grant(user,modelManager.getGlobalGroup(), role2);
        modelManager.grant(user,modelManager.getGlobalGroup(), role3);
        modelManager.grant(user,modelManager.getGlobalGroup(), role4);
        modelManager.grant(role, permission);
        modelManager.grant(role, permission2);
        modelManager.grant(role, permission3);
        modelManager.grant(role2, permission2);
        modelManager.grant(role2, permission3);
        modelManager.grant(role3, permission4);
        modelManager.grant(role4, permission5);
        acl = userManager.getACL(user);
        assertTrue(acl.hasPermission(permission));
        assertTrue(acl.hasPermission(permission2));
        assertTrue(acl.hasPermission(permission3));
        assertTrue(acl.hasPermission(permission4));
        assertTrue(acl.hasPermission(permission5));
    }

    private int getId()
    {
        return ++counter;
    }

    private Role getRole() throws Exception
    {
        Role role = roleManager.getRoleInstance("Role " + getId());
        roleManager.addRole(role);
        return role;
    }

    private Group getGroup() throws Exception
    {
        Group group = groupManager.getGroupInstance("Group " + getId());
        groupManager.addGroup(group);
        return group;
    }

    private Permission getPermission() throws Exception
    {
        Permission permission = permissionManager.getPermissionInstance("Permission " + getId());
        permissionManager.addPermission(permission);
        return permission;
    }
}
