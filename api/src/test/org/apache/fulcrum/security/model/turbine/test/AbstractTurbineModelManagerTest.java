package org.apache.fulcrum.security.model.turbine.test;

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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.apache.fulcrum.security.GroupManager;
import org.apache.fulcrum.security.PermissionManager;
import org.apache.fulcrum.security.RoleManager;
import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.UserManager;
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.turbine.TurbineModelManager;
import org.apache.fulcrum.security.model.turbine.entity.TurbineGroup;
import org.apache.fulcrum.security.model.turbine.entity.TurbineRole;
import org.apache.fulcrum.security.model.turbine.entity.TurbineUser;
import org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRole;
import org.apache.fulcrum.security.util.PermissionSet;
import org.apache.fulcrum.security.util.UserSet;
import org.apache.fulcrum.testcontainer.BaseUnit5Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Initialization of services in implementing tests
 * @author Eric Pugh
 * 
 */
public abstract class AbstractTurbineModelManagerTest extends BaseUnit5Test
{
    protected Role role;

    protected TurbineModelManager modelManager;

    protected RoleManager roleManager;

    protected GroupManager groupManager;

    protected PermissionManager permissionManager;

    protected UserManager userManager;

    protected SecurityService securityService;

    @BeforeEach
    public void setUp() throws Exception
    {
    	securityService  = (SecurityService) lookup(SecurityService.ROLE);
        roleManager = securityService.getRoleManager();
        userManager = securityService.getUserManager();
        groupManager = securityService.getGroupManager();
        permissionManager = securityService.getPermissionManager();
        modelManager = (TurbineModelManager) securityService.getModelManager();
    }


    @Test
    public void testGetGlobalGroup() throws Exception
    {
        Group global = modelManager.getGlobalGroup();
        assertNotNull(global);
        assertEquals(global.getName(), modelManager.getGlobalGroupName());
    }
    @Test
    public void testGrantRolePermission() throws Exception
    {
        Permission permission = permissionManager.getPermissionInstance();
        permission.setName("ANSWER_PHONE");
        permissionManager.addPermission(permission);
        role = roleManager.getRoleInstance("RECEPTIONIST");
        //role.setId(new Integer(1));
        roleManager.addRole(role);
        modelManager.grant(role, permission);
        role = roleManager.getRoleById(role.getId());
        PermissionSet permissions = ((TurbineRole) role).getPermissions();
        assertEquals(1, permissions.size());
        assertTrue(((TurbineRole) role).getPermissions().contains(permission));
    }
    @Test
    public void testRevokeRolePermission() throws Exception
    {
        Permission permission = securityService.getPermissionManager().getPermissionInstance();
        permission.setName("ANSWER_FAX");
        securityService.getPermissionManager().addPermission(permission);
        role = roleManager.getRoleInstance("SECRETARY"); 
        //role.setId(new Integer(1));
        roleManager.addRole(role);
        modelManager.grant(role, permission);
        role = roleManager.getRoleById(role.getId());
        PermissionSet permissions = ((TurbineRole) role).getPermissions();
        assertEquals(1, permissions.size());
        modelManager.revoke(role, permission);
        role = roleManager.getRoleById(role.getId());
        permissions = ((TurbineRole) role).getPermissions();
        assertEquals(0, permissions.size());
        assertFalse(((TurbineRole) role).getPermissions().contains(permission));
    }
    @Test
    public void testRevokeRolePermissionOneOfTwo() throws Exception
    {
        Permission permission = securityService.getPermissionManager().getPermissionInstance();
        Permission permission2 = securityService.getPermissionManager().getPermissionInstance();
        permission.setName("ANOTHER_SEND_SPAM"); // otherwise memory complains "does already exist
        permission2.setName("ANOTHER_ANSWER_EMAIL");
        // assign new db entities
        permission = securityService.getPermissionManager().addPermission(permission);
        permission2 =  securityService.getPermissionManager().addPermission(permission2);
        role = roleManager.getRoleInstance("ANOTHERSECRETARY");
        //role.setId(new Integer(1));
        role = roleManager.addRole(role);
        modelManager.grant(role, permission);
        modelManager.grant(role, permission2);
        role = roleManager.getRoleById(role.getId());
        PermissionSet permissions = ((TurbineRole) role).getPermissions();
        assertEquals(2, permissions.size());
        modelManager.revoke(role, permission);
        role = roleManager.getRoleById(role.getId());
        permissions = ((TurbineRole) role).getPermissions();
        assertEquals(1, permissions.size());
        assertFalse(((TurbineRole) role).getPermissions().contains(permission));
        assertTrue(((TurbineRole) role).getPermissions().contains(permission2));
        // to cleanup
        modelManager.revoke(role, permission2);
        assertFalse(((TurbineRole) role).getPermissions().contains(permission2));
    }
    @Test
    public void testRevokeAllRole() throws Exception
    {
        Permission permission = securityService.getPermissionManager().getPermissionInstance();
        Permission permission2 = securityService.getPermissionManager().getPermissionInstance();
        permission.setName("SEND_SPAM");
        permission2.setName("ANSWER_EMAIL");
        securityService.getPermissionManager().addPermission(permission);
        securityService.getPermissionManager().addPermission(permission2);
        role = roleManager.getRoleInstance("HELPER");
        //role.setId(new Integer(1));
        roleManager.addRole(role);
        modelManager.grant(role, permission);
        modelManager.grant(role, permission2);
        role = roleManager.getRoleById(role.getId());
        PermissionSet permissions = ((TurbineRole) role).getPermissions();
        assertEquals(2, permissions.size());
        modelManager.revokeAll(role);
        role = roleManager.getRoleById(role.getId());
        permissions = ((TurbineRole) role).getPermissions();
        assertEquals(0, permissions.size());
    }
    @Test
    public void testRevokeAllUser() throws Exception
    {
        Group group = securityService.getGroupManager().getGroupInstance();
        group.setName("TEST_REVOKEALLUSER_GROUP");
        securityService.getGroupManager().addGroup(group);
        Role role = securityService.getRoleManager().getRoleInstance();
        role.setName("TEST_REVOKEALLUSER_ROLE");
        //role.setId(new Integer(1));
        role = securityService.getRoleManager().addRole(role);
        
        Group group2 = securityService.getGroupManager().getGroupInstance();
        group2.setName("TEST_REVOKEALLUSER_GROUP2");
        //group2.setId(new Integer(1));
        securityService.getGroupManager().addGroup(group2);
        Role role2 = securityService.getRoleManager().getRoleInstance();
        role2.setName("TEST_REVOKEALLUSER_ROLE2");
        //role2.setId(new Integer(2));
        role2 = securityService.getRoleManager().addRole(role2);
        
        String username = "calvin";
        User user = userManager.getUserInstance(username);
        user = userManager.addUser(user, username);
        modelManager.grant(user, group, role);
        modelManager.grant(user, group2, role2);
        // original objects have relationship attached
        Set<TurbineUserGroupRole> userGroupRoleSet =  ((TurbineUser)user).getUserGroupRoleSet();
        assertEquals(2, userGroupRoleSet.size());
        Set<TurbineUserGroupRole> userGroupRoleSet1 = ((TurbineRole) role).getUserGroupRoleSet();
        assertEquals(1, userGroupRoleSet1.size());
        Set<TurbineUserGroupRole> userGroupRoleSet2 = ((TurbineGroup) group).getUserGroupRoleSet();
        assertEquals(1, userGroupRoleSet2.size());
        // retrieve objects again, which have as lazily loaded no usergroupset yet
        group = groupManager.getGroupById(group.getId());
        user = userManager.getUser( username );
        role = roleManager.getRoleById( role.getId() );
        
        UserSet<User> userSet = userManager.getAllUsers();
        User user2 = userManager.getUserById( user.getId() );
        assertEquals( user, user2 );
        
        // retrieve usergroupset now
        userGroupRoleSet =  ((TurbineUser)user).getUserGroupRoleSet();
        assertEquals(2, userGroupRoleSet.size());
        userGroupRoleSet1 = ((TurbineRole) role).getUserGroupRoleSet();
        assertEquals(1, userGroupRoleSet1.size());
        userGroupRoleSet2 = ((TurbineGroup) group).getUserGroupRoleSet();
        assertEquals(1, userGroupRoleSet2.size());

        modelManager.revokeAll(user);
        group = groupManager.getGroupById(group.getId());
        assertEquals(0, ((TurbineGroup) group).getUserGroupRoleSet().size());
        role = securityService.getRoleManager().getRoleByName("TEST_REVOKEALLUSER_ROLE");
        assertEquals(0,((TurbineRole) role).getUserGroupRoleSet().size());
        assertTrue(((TurbineRole) role).getUserGroupRoleSet().isEmpty());
        
        modelManager.grant(user, group, role);
        assertEquals(1,((TurbineRole) role).getUserGroupRoleSet().size());
        assertTrue(((TurbineRole) role).getUserGroupRoleSet().iterator().next().getGroup().equals( group ));

        
    }
    @Test
    public void testGrantUserGroupRole() throws Exception
    {
        Group group = securityService.getGroupManager().getGroupInstance();
        group.setName("TEST_GROUP");
        securityService.getGroupManager().addGroup(group);
        Role role = roleManager.getRoleInstance();
        role.setName("TEST_Role");
        roleManager.addRole(role);
        User user = userManager.getUserInstance("Clint");
        userManager.addUser(user, "clint");
        modelManager.grant(user, group, role);
        boolean ugrFound = false;
        TurbineUserGroupRole ugrTest = null;
        for (TurbineUserGroupRole ugr : ((TurbineUser) user).getUserGroupRoleSet())
        {
            if (ugr.getUser().equals(user) && ugr.getGroup().equals(group) && ugr.getRole().equals(role))
            {
                ugrFound = true;
                ugrTest = ugr;
                break;
            }
        }
        assertTrue(ugrFound);
        assertTrue(ugrTest.getGroup().equals(group));
        assertTrue(ugrTest.getUser().equals(user));
    }
    @Test
    public void testRevokeUserGroupRole() throws Exception
    {
        Group group = securityService.getGroupManager().getGroupInstance();
        group.setName("TEST_REVOKE");
        securityService.getGroupManager().addGroup(group);
        User user = userManager.getUserInstance("Lima");
        userManager.addUser(user, "pet");
        Role role = roleManager.getRoleInstance();
        role.setName("TEST_REVOKE_ROLE");
        roleManager.addRole(role);
        modelManager.grant(user, group, role);
        modelManager.revoke(user, group, role);
        boolean ugrFound = ((TurbineUser) user)
                .getUserGroupRoleSet().stream()
                .anyMatch(ugr -> ugr.getUser().equals(user)
                        && ugr.getGroup().equals(group)
                        && ugr.getRole().equals(role));

        assertFalse(ugrFound);
    }
    
    @Test
    public void testUserGroupGrantRolePermission() throws Exception
    {
        Permission permission = permissionManager.getPermissionInstance();
        permission.setName("ANSWER_PHONE__");
        //permission.setId(new Integer (1));
        permissionManager.addPermission(permission);
        
        Permission permission2 = permissionManager.getPermissionInstance();
        permission2.setName("ANSWER_PHONE__2");
        // TurbineHibernate.hbm.xml has <generator class="native" for id, throws org.hibernate.HibernateException: 
        // identifier of an instance of org.apache.fulcrum.security.model.turbine.entity.impl.TurbinePermissionImpl was altered from 1 to 2
        //permission.setId(new Integer (2));
        permissionManager.addPermission(permission2);
        
        role = roleManager.getRoleInstance("RECEPTIONIST__");
        roleManager.addRole(role);
        
        Group group = securityService.getGroupManager().getGroupInstance();
        group.setName("TEST_GROUP__");
        //group.setId(new Integer(1));
        securityService.getGroupManager().addGroup(group);
        Role role = roleManager.getRoleInstance();
        
        role.setName("TEST_Role__");
        //role.setId(new Integer(1));
        roleManager.addRole(role);
        User user = userManager.getUserInstance("Clint__");
        userManager.addUser(user, "clint");
        // this is required
        modelManager.grant(user, group, role);
        
        modelManager.grant(role, permission);
        modelManager.grant(role, permission2);
        role = roleManager.getRoleById(role.getId());
        PermissionSet permissions = ((TurbineRole) role).getPermissions();
        assertEquals(2, permissions.size());
        assertTrue(((TurbineRole) role).getPermissions().contains(permission));
        assertTrue(((TurbineRole) role).getPermissions().contains(permission2));
        
        modelManager.revoke( role, permission2 );
        permissions = ((TurbineRole) role).getPermissions();
        assertEquals(1, permissions.size());
        assertTrue(((TurbineRole) role).getPermissions().contains(permission));
        
        modelManager.revoke(role, permission);
        permissions = ((TurbineRole) role).getPermissions();
        assertEquals(0, permissions.size());
        assertFalse(((TurbineRole) role).getPermissions().contains(permission));
 
    }
    
    
    @Test
    public void testReplaceUserGroupRole() throws Exception
    {
        Group global = modelManager.getGlobalGroup();
        
        Role role = roleManager.getRoleInstance();
        role.setName("TEST_REPLACE_ROLE");
        if (!roleManager.checkExists( role ) ) {
            roleManager.addRole(role);
        }

        
        
        Role newRole = roleManager.getRoleInstance();
        newRole.setName("TEST_NEW_ROLE");
        roleManager.addRole(newRole);
        User user = userManager.getUserInstance("Dave");
        userManager.addUser(user, "dave");
        
        modelManager.grant(user, global, role);
        
        modelManager.replace(user, role, newRole);
        

        
        boolean ugrFound = false;
        boolean ugrNotFound = true;
        TurbineUserGroupRole ugrTest = null;
        for (TurbineUserGroupRole ugr : ((TurbineUser) user).getUserGroupRoleSet())
        {
            if (ugr.getUser().equals(user) && ugr.getGroup().equals(global) && ugr.getRole().equals(newRole))
            {
                ugrFound = true;
                ugrTest = ugr;
            }
            if (ugr.getUser().equals(user) && ugr.getGroup().equals(global) && ugr.getRole().equals(role))
            {
                ugrNotFound = false;
                ugrTest = ugr;
            }
        }
        assertTrue(ugrFound);
        assertTrue(ugrNotFound);
        assertTrue(ugrTest.getGroup().equals(global));
        assertTrue(ugrTest.getUser().equals(user));

    }
}
