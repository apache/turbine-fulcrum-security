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
import org.apache.fulcrum.security.model.dynamic.DynamicAccessControlList;
import org.apache.fulcrum.security.model.dynamic.DynamicAccessControlListImpl;
import org.apache.fulcrum.security.model.dynamic.DynamicModelManager;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicGroup;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicRole;
import org.apache.fulcrum.security.util.GroupSet;
import org.apache.fulcrum.security.util.PermissionSet;
import org.apache.fulcrum.security.util.RoleSet;
import org.apache.fulcrum.testcontainer.BaseUnit5Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Test that we can generate AccessControlLists from the Factory
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class AccessControlListTest extends BaseUnit5Test
{

    private UserManager userManager;
    private GroupManager groupManager;
    private RoleManager roleManager;
    private DynamicModelManager modelManager;
    private PermissionManager permissionManager;
    private DynamicAccessControlList acl;
    private static int counter = 1;
    private User user;

    @BeforeEach
    public void setUp() throws Exception
    {

        this.setRoleFileName("src/test/DynamicMemoryRoleConfig.xml");
        this.setConfigurationFileName("src/test/DynamicMemoryComponentConfig.xml");

        SecurityService securityService = (SecurityService) lookup(SecurityService.ROLE);
        userManager = securityService.getUserManager();
        groupManager = securityService.getGroupManager();
        roleManager = securityService.getRoleManager();
        permissionManager = securityService.getPermissionManager();
        modelManager = (DynamicModelManager) securityService.getModelManager();

    }

    @Test
    public void testCreatingDefaultAccessControlListViaFactory() throws Exception
    {
        Group group = getGroup();
        Role role = getRole();
        Permission permission = getPermission();
        user = userManager.getUserInstance("User 1");
        userManager.addUser(user, "secretpassword");
        modelManager.grant(user, group);
        modelManager.grant(group, role);
        modelManager.grant(role, permission);
        RoleSet roleSet = new RoleSet();
        PermissionSet permissionSet = new PermissionSet();
        roleSet.add(role);
        permissionSet.add(permission);
        Map<Group, RoleSet> roleSets = new HashMap<Group, RoleSet>();
        Map<Role, PermissionSet> permissionSets = new HashMap<Role, PermissionSet>();
        roleSets.put(group, roleSet);
        permissionSets.put(role, permissionSet);
        AccessControlList acl = userManager.getACL(user);
        assertTrue(acl instanceof DynamicAccessControlList);
        DynamicAccessControlList dacl = (DynamicAccessControlList) acl;
        assertTrue(dacl.hasRole(role));
        assertTrue(dacl.hasPermission(permission));
        
        Serializable serDeSer = SerializationUtils.roundtrip(acl);
        assertTrue( ((DynamicAccessControlList)serDeSer).getRoles().toString().equals(dacl.getRoles().toString()));

    }
    @Test
    public void testGetRolesGroup() throws Exception
    {
        Group group = getGroup();
        Role role = getRole();
        Role role2 = getRole();
        Role role3 = getRole();
        modelManager.grant(group, role);
        modelManager.grant(group, role2);
        Map<Group, RoleSet> roleSets = new HashMap<Group, RoleSet>();
        Map<Role, PermissionSet> permissionSets = new HashMap<Role, PermissionSet>();
        roleSets.put(group, ((DynamicGroup) group).getRoles());
        acl = new DynamicAccessControlListImpl(roleSets, permissionSets);
        RoleSet resultRoleSet = acl.getRoles(group);
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
        modelManager.grant(group, role);
        modelManager.grant(group, role2);
        modelManager.grant(group2, role2);
        modelManager.grant(group2, role3);
        Map<Group, RoleSet> roleSets = new HashMap<Group, RoleSet>();
        Map<Role, PermissionSet> permissionSets = new HashMap<Role, PermissionSet>();
        roleSets.put(group, ((DynamicGroup) group).getRoles());
        roleSets.put(group2, ((DynamicGroup) group2).getRoles());
        acl = new DynamicAccessControlListImpl(roleSets, permissionSets);
        RoleSet resultRoleSet = acl.getRoles();
        assertTrue(resultRoleSet.contains(role));
        assertTrue(resultRoleSet.contains(role2));
        assertTrue(resultRoleSet.contains(role3));
        assertEquals(3, resultRoleSet.size());
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
        modelManager.grant(group, role);
        modelManager.grant(group, role2);
        modelManager.grant(group2, role3);
        modelManager.grant(role, permission);
        modelManager.grant(role, permission2);
        modelManager.grant(role, permission3);
        modelManager.grant(role2, permission2);
        modelManager.grant(role2, permission3);
        Map<Group, RoleSet> roleSets = new HashMap<Group, RoleSet>();
        Map<Role, PermissionSet> permissionSets = new HashMap<Role, PermissionSet>();
        roleSets.put(group, ((DynamicGroup) group).getRoles());
        permissionSets.put(role, ((DynamicRole) role).getPermissions());
        acl = new DynamicAccessControlListImpl(roleSets, permissionSets);
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
        modelManager.grant(group, role);
        modelManager.grant(group, role2);
        modelManager.grant(group2, role3);
        modelManager.grant(role, permission);
        modelManager.grant(role, permission2);
        modelManager.grant(role, permission3);
        modelManager.grant(role2, permission2);
        modelManager.grant(role2, permission3);
        Map<Group, RoleSet> roleSets = new HashMap<Group, RoleSet>();
        Map<Role, PermissionSet> permissionSets = new HashMap<Role, PermissionSet>();
        roleSets.put(group, ((DynamicGroup) group).getRoles());
        roleSets.put(group2, ((DynamicGroup) group2).getRoles());
        permissionSets.put(role, ((DynamicRole) role).getPermissions());
        permissionSets.put(role2, ((DynamicRole) role2).getPermissions());
        permissionSets.put(role3, ((DynamicRole) role3).getPermissions());
        acl = new DynamicAccessControlListImpl(roleSets, permissionSets);
        PermissionSet resultPermissionSet = acl.getPermissions();
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
        modelManager.grant(group, role);
        modelManager.grant(group, role2);
        modelManager.grant(group2, role);
        modelManager.grant(group2, role3);
        Map<Group, RoleSet> roleSets = new HashMap<Group, RoleSet>();
        Map<Role, PermissionSet> permissionSets = new HashMap<Role, PermissionSet>();
        roleSets.put(group, ((DynamicGroup) group).getRoles());
        roleSets.put(group2, ((DynamicGroup) group2).getRoles());
        acl = new DynamicAccessControlListImpl(roleSets, permissionSets);
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
        modelManager.grant(group, role);
        modelManager.grant(group, role2);
        modelManager.grant(group2, role);
        modelManager.grant(group2, role3);
        Map<Group, RoleSet> roleSets = new HashMap<Group, RoleSet>();
        Map<Role, PermissionSet> permissionSets = new HashMap<Role, PermissionSet>();
        roleSets.put(group, ((DynamicGroup) group).getRoles());
        roleSets.put(group2, ((DynamicGroup) group2).getRoles());
        acl = new DynamicAccessControlListImpl(roleSets, permissionSets);
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
        Group group = getGroup();
        Group group2 = getGroup();
        Role role = getRole();
        Role role2 = getRole();
        Role role3 = getRole();
        modelManager.grant(group, role);
        modelManager.grant(group, role2);
        modelManager.grant(group2, role);
        modelManager.grant(group2, role3);
        Map<Group, RoleSet> roleSets = new HashMap<Group, RoleSet>();
        Map<Role, PermissionSet> permissionSets = new HashMap<Role, PermissionSet>();
        roleSets.put(group, ((DynamicGroup) group).getRoles());
        roleSets.put(group2, ((DynamicGroup) group2).getRoles());
        acl = new DynamicAccessControlListImpl(roleSets, permissionSets);
        assertTrue(acl.hasRole(role.getName(), group.getName()));
        assertTrue(acl.hasRole(role.getName(), group2.getName()));
        assertTrue(acl.hasRole(role2.getName(), group.getName()));
        assertFalse(acl.hasRole(role2.getName(), group2.getName()));
        assertTrue(acl.hasRole(role.getName(), group2.getName()));
        assertFalse(acl.hasRole(role2.getName(), group2.getName()));
        assertTrue(acl.hasRole(role3.getName(), group2.getName()));
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
        modelManager.grant(group, role);
        modelManager.grant(group, role2);
        modelManager.grant(group2, role3);
        modelManager.grant(role, permission);
        modelManager.grant(role, permission2);
        modelManager.grant(role, permission3);
        modelManager.grant(role2, permission2);
        modelManager.grant(role2, permission3);
        modelManager.grant(role3, permission4);
        Map<Group, RoleSet> roleSets = new HashMap<Group, RoleSet>();
        Map<Role, PermissionSet> permissionSets = new HashMap<Role, PermissionSet>();
        roleSets.put(group, ((DynamicGroup) group).getRoles());
        roleSets.put(group2, ((DynamicGroup) group2).getRoles());
        permissionSets.put(role, ((DynamicRole) role).getPermissions());
        permissionSets.put(role2, ((DynamicRole) role2).getPermissions());
        permissionSets.put(role3, ((DynamicRole) role3).getPermissions());
        acl = new DynamicAccessControlListImpl(roleSets, permissionSets);
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
        modelManager.grant(group, role);
        modelManager.grant(group, role2);
        modelManager.grant(group2, role3);
        modelManager.grant(group3, role4);
        modelManager.grant(role, permission);
        modelManager.grant(role, permission2);
        modelManager.grant(role, permission3);
        modelManager.grant(role2, permission2);
        modelManager.grant(role2, permission3);
        modelManager.grant(role3, permission4);
        modelManager.grant(role4, permission5);
        Map<Group, RoleSet> roleSets = new HashMap<Group, RoleSet>();
        Map<Role, PermissionSet> permissionSets = new HashMap<Role, PermissionSet>();
        roleSets.put(group, ((DynamicGroup) group).getRoles());
        roleSets.put(group2, ((DynamicGroup) group2).getRoles());
        roleSets.put(group3, ((DynamicGroup) group3).getRoles());
        permissionSets.put(role, ((DynamicRole) role).getPermissions());
        permissionSets.put(role2, ((DynamicRole) role2).getPermissions());
        permissionSets.put(role3, ((DynamicRole) role3).getPermissions());
        permissionSets.put(role4, ((DynamicRole) role4).getPermissions());
        acl = new DynamicAccessControlListImpl(roleSets, permissionSets);
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
        modelManager.grant(group, role);
        modelManager.grant(group, role2);
        modelManager.grant(group2, role3);
        modelManager.grant(group3, role4);
        modelManager.grant(role, permission);
        modelManager.grant(role, permission2);
        modelManager.grant(role, permission3);
        modelManager.grant(role2, permission2);
        modelManager.grant(role2, permission3);
        modelManager.grant(role3, permission4);
        modelManager.grant(role4, permission5);
        Map<Group, RoleSet> roleSets = new HashMap<Group, RoleSet>();
        Map<Role, PermissionSet> permissionSets = new HashMap<Role, PermissionSet>();
        roleSets.put(group, ((DynamicGroup) group).getRoles());
        roleSets.put(group2, ((DynamicGroup) group2).getRoles());
        roleSets.put(group3, ((DynamicGroup) group3).getRoles());
        permissionSets.put(role, ((DynamicRole) role).getPermissions());
        permissionSets.put(role2, ((DynamicRole) role2).getPermissions());
        permissionSets.put(role3, ((DynamicRole) role3).getPermissions());
        permissionSets.put(role4, ((DynamicRole) role4).getPermissions());
        acl = new DynamicAccessControlListImpl(roleSets, permissionSets);
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
