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

import java.util.Iterator;
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
import org.apache.fulcrum.testcontainer.BaseUnitTest;

/**
 * @author Eric Pugh
 *
 */
public abstract class AbstractTurbineModelManagerTest extends BaseUnitTest
{
    protected Role role;

    protected TurbineModelManager modelManager;

    protected RoleManager roleManager;

    protected GroupManager groupManager;

    protected PermissionManager permissionManager;

    protected UserManager userManager;

    protected SecurityService securityService;

    public void setUp() throws Exception
    {
        super.setUp();
        roleManager = securityService.getRoleManager();
        userManager = securityService.getUserManager();
        groupManager = securityService.getGroupManager();
        permissionManager = securityService.getPermissionManager();
        modelManager = (TurbineModelManager) securityService.getModelManager();
    }

    /**
     * Constructor for AbstractTurbineModelManagerTest.
     *
     * @param arg0
     */
    public AbstractTurbineModelManagerTest(String arg0)
    {
        super(arg0);
    }

    public void testGetGlobalGroup() throws Exception
    {
        Group global = modelManager.getGlobalGroup();
        assertNotNull(global);
        assertEquals(global.getName(), TurbineModelManager.GLOBAL_GROUP_NAME);
    }

    public void testGrantRolePermission() throws Exception
    {
        Permission permission = permissionManager.getPermissionInstance();
        permission.setName("ANSWER_PHONE");
        permissionManager.addPermission(permission);
        role = roleManager.getRoleInstance("RECEPTIONIST");
        roleManager.addRole(role);
        modelManager.grant(role, permission);
        role = roleManager.getRoleById(role.getId());
        PermissionSet permissions = ((TurbineRole) role).getPermissions();
        assertEquals(1, permissions.size());
        assertTrue(((TurbineRole) role).getPermissions().contains(permission));
    }

    public void testRevokeRolePermission() throws Exception
    {
        Permission permission = securityService.getPermissionManager()
                .getPermissionInstance();
        permission.setName("ANSWER_FAX");
        securityService.getPermissionManager().addPermission(permission);
        role = roleManager.getRoleInstance("SECRETARY");
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

    public void testRevokeAllRole() throws Exception
    {
        Permission permission = securityService.getPermissionManager()
                .getPermissionInstance();
        Permission permission2 = securityService.getPermissionManager()
                .getPermissionInstance();
        permission.setName("SEND_SPAM");
        permission2.setName("ANSWER_EMAIL");
        securityService.getPermissionManager().addPermission(permission);
        securityService.getPermissionManager().addPermission(permission2);
        role = roleManager.getRoleInstance("HELPER");
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

    public void testRevokeAllUser() throws Exception
    {
        Group group = securityService.getGroupManager().getGroupInstance();
        group.setName("TEST_REVOKEALLUSER_GROUP");
        securityService.getGroupManager().addGroup(group);
        Role role = securityService.getRoleManager().getRoleInstance();
        role.setName("TEST_REVOKEALLUSER_ROLE");
        securityService.getRoleManager().addRole(role);

        User user = userManager.getUserInstance("calvin");
        userManager.addUser(user, "calvin");
        modelManager.grant(user, group, role);

        group = groupManager.getGroupById(group.getId());
        Set userGroupRoleSet = ((TurbineGroup) group).getUserGroupRoleSet();
        assertEquals(1, userGroupRoleSet.size());
        Set userGroupRoleSet2 = ((TurbineGroup) group).getUserGroupRoleSet();
        assertEquals(1, userGroupRoleSet2.size());

        modelManager.revokeAll(user);
        group = groupManager.getGroupById(group.getId());
        assertEquals(0, ((TurbineGroup) group).getUserGroupRoleSet().size());
        role = securityService.getRoleManager().getRoleByName(
                "TEST_REVOKEALLUSER_ROLE");

        // assertFalse(((TurbineRole) role).getGroups().contains(group));

    }

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
        TurbineUserGroupRole ugr = null;
        for (Iterator i = ((TurbineUser) user).getUserGroupRoleSet().iterator(); i
                .hasNext();)
        {
            ugr = (TurbineUserGroupRole) i.next();
            if (ugr.getUser().equals(user) && ugr.getGroup().equals(group)
                    && ugr.getRole().equals(role))
            {
                ugrFound = true;
                break;
            }
        }
        assertTrue(ugrFound);
        assertTrue(ugr.getGroup().equals(group));
        assertTrue(ugr.getUser().equals(user));

    }

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
        boolean ugrFound = false;
        TurbineUserGroupRole ugr = null;
        for (Iterator i = ((TurbineUser) user).getUserGroupRoleSet().iterator(); i
                .hasNext();)
        {
            ugr = (TurbineUserGroupRole) i.next();
            if (ugr.getUser().equals(user) && ugr.getGroup().equals(group)
                    && ugr.getRole().equals(role))
            {
                ugrFound = true;
                break;
            }
        }
        assertFalse(ugrFound);
    }
}
