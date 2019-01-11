package org.apache.fulcrum.security.model.dynamic.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

import java.util.HashSet;
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
import org.apache.fulcrum.security.model.dynamic.DynamicAccessControlList;
import org.apache.fulcrum.security.model.dynamic.DynamicModelManager;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicGroup;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicPermission;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicRole;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicUser;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.PermissionSet;
import org.apache.fulcrum.security.util.RoleSet;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.apache.fulcrum.security.util.UserSet;
import org.apache.fulcrum.testcontainer.BaseUnit5Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Eric Pugh
 * @author <a href="mailto:ben@gidley.co.uk">Ben Gidley </a>
 *
 */
public abstract class AbstractDynamicModelManagerTest extends BaseUnit5Test
{
    private static final String ONLY_BORRIS_PERMISSION = "ONLY_BORRIS_PERMISSION";

    private static final String ONLY_BORRIS_GROUP = "ONLY_BORRIS_GROUP";

    private static final String ONLY_BORRIS_ROLE = "ONLY BORRIS ROLE";

    private static final String USERNAME_SAM = "sam1";

    private static final String USERNAME_BORRIS = "borris1";

    protected Role role;

    protected DynamicModelManager modelManager;

    protected RoleManager roleManager;

    protected GroupManager groupManager;

    protected PermissionManager permissionManager;

    protected UserManager userManager;

    protected SecurityService securityService;

    @BeforeEach
    public void setUp() throws Exception
    {
        roleManager = securityService.getRoleManager();
        userManager = securityService.getUserManager();
        groupManager = securityService.getGroupManager();
        permissionManager = securityService.getPermissionManager();
        modelManager = (DynamicModelManager) securityService.getModelManager();
    }


	@Override
	@AfterEach
    public void tearDown()
    {
        this.release(roleManager);
        this.release(userManager);
        this.release(groupManager);
        this.release(permissionManager);
        this.release(modelManager);
    }


	@Test
    public void testGrantRolePermission() throws Exception
    {
        Permission permission = permissionManager.getPermissionInstance();
        permission.setName("ANSWER_PHONE");
        permissionManager.addPermission(permission);
        role = roleManager.getRoleInstance("RECEPTIONIST");
        roleManager.addRole(role);
        modelManager.grant(role, permission);
        role = roleManager.getRoleById(role.getId());
        PermissionSet permissions = ((DynamicRole) role).getPermissions();
        assertEquals(1, permissions.size());
        assertTrue(((DynamicRole) role).getPermissions().contains(permission));
    }
	@Test
    public void testRevokeRolePermission() throws Exception
    {
        Permission permission = securityService.getPermissionManager().getPermissionInstance();
        permission.setName("ANSWER_FAX");
        securityService.getPermissionManager().addPermission(permission);
        role = roleManager.getRoleInstance("SECRETARY");
        roleManager.addRole(role);
        modelManager.grant(role, permission);
        role = roleManager.getRoleById(role.getId());
        PermissionSet permissions = ((DynamicRole) role).getPermissions();
        assertEquals(1, permissions.size());
        modelManager.revoke(role, permission);
        role = roleManager.getRoleById(role.getId());
        permissions = ((DynamicRole) role).getPermissions();
        assertEquals(0, permissions.size());
        assertFalse(((DynamicRole) role).getPermissions().contains(permission));
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
        roleManager.addRole(role);
        modelManager.grant(role, permission);
        modelManager.grant(role, permission2);
        role = roleManager.getRoleById(role.getId());
        PermissionSet permissions = ((DynamicRole) role).getPermissions();
        assertEquals(2, permissions.size());
        modelManager.revokeAll(role);
        role = roleManager.getRoleById(role.getId());
        permissions = ((DynamicRole) role).getPermissions();
        assertEquals(0, permissions.size());
    }
	@Test
    public void testRevokeAllGroup() throws Exception
    {
        Permission permission = securityService.getPermissionManager().getPermissionInstance();
        Permission permission2 = securityService.getPermissionManager().getPermissionInstance();
        permission.setName("SEND_SPAM2");
        permission2.setName("ANSWER_EMAIL2");
        securityService.getPermissionManager().addPermission(permission);
        securityService.getPermissionManager().addPermission(permission2);
        role = roleManager.getRoleInstance("HELPER2");
        roleManager.addRole(role);
        modelManager.grant(role, permission);
        modelManager.grant(role, permission2);
        role = roleManager.getRoleById(role.getId());
        PermissionSet permissions = ((DynamicRole) role).getPermissions();
        assertEquals(2, permissions.size());
        modelManager.revokeAll(role);
        role = roleManager.getRoleById(role.getId());
        permissions = ((DynamicRole) role).getPermissions();
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
        securityService.getRoleManager().addRole(role);
        User user = userManager.getUserInstance("calvin");
        userManager.addUser(user, "calvin");
        modelManager.grant(user, group);
        modelManager.grant(group, role);
        group = groupManager.getGroupById(group.getId());
        RoleSet roles = ((DynamicGroup) group).getRoles();
        assertEquals(1, roles.size());
        UserSet users = ((DynamicGroup) group).getUsers();
        assertEquals(1, users.size());

        modelManager.revokeAll(group);
        assertEquals(0, ((DynamicGroup) group).getUsers().size());
        role = securityService.getRoleManager().getRoleByName("TEST_REVOKEALLUSER_ROLE");

        assertFalse(((DynamicRole) role).getGroups().contains(group));

    }
	@Test
    public void testRevokeAllPermission() throws Exception
    {
        Role role = securityService.getRoleManager().getRoleInstance();
        Role role2 = securityService.getRoleManager().getRoleInstance();
        role.setName("SEND_SPAM");
        role2.setName("ANSWER_EMAIL");
        securityService.getRoleManager().addRole(role);
        securityService.getRoleManager().addRole(role2);
        Permission permission = permissionManager.getPermissionInstance("HELPER");
        permissionManager.addPermission(permission);
        modelManager.grant(role, permission);
        modelManager.grant(role2, permission);
        permission = permissionManager.getPermissionById(permission.getId());
        RoleSet roles = ((DynamicPermission) permission).getRoles();
        assertEquals(2, roles.size());
        modelManager.revokeAll(permission);
        permission = permissionManager.getPermissionById(permission.getId());
        roles = ((DynamicPermission) permission).getRoles();
        assertEquals(0, roles.size());
    }
	@Test
    public void testGrantUserGroup() throws Exception
    {
        Group group = securityService.getGroupManager().getGroupInstance();
        group.setName("TEST_GROUP");
        securityService.getGroupManager().addGroup(group);
        User user = userManager.getUserInstance("Clint");
        userManager.addUser(user, "clint");
        modelManager.grant(user, group);
        assertTrue(((DynamicUser) user).getGroups().contains(group));
        assertTrue(((DynamicGroup) group).getUsers().contains(user));
    }
	@Test
    public void testRevokeUserGroup() throws Exception
    {
        Group group = securityService.getGroupManager().getGroupInstance();
        group.setName("TEST_REVOKE");
        securityService.getGroupManager().addGroup(group);
        User user = userManager.getUserInstance("Lima");
        userManager.addUser(user, "pet");
        modelManager.revoke(user, group);
        assertFalse(((DynamicUser) user).getGroups().contains(group));
        assertFalse(((DynamicGroup) group).getUsers().contains(user));
        user = userManager.getUser("Lima");
        assertFalse(((DynamicUser) user).getGroups().contains(group));
    }
	@Test
    public void testGrantGroupRole() throws Exception
    {
        Role role = securityService.getRoleManager().getRoleInstance();
        role.setName("TEST_PERMISSION");
        securityService.getRoleManager().addRole(role);
        Group group = groupManager.getGroupInstance("TEST_GROUP2");
        groupManager.addGroup(group);
        modelManager.grant(group, role);
        group = groupManager.getGroupByName("TEST_GROUP2");
        assertTrue(((DynamicGroup) group).getRoles().contains(role));
        assertTrue(((DynamicRole) role).getGroups().contains(group));

    }
    @Test
    public void testRevokeGroupRole() throws Exception
    {
        Role role = securityService.getRoleManager().getRoleInstance();
        role.setName("TEST_PERMISSION2");
        securityService.getRoleManager().addRole(role);
        Group group = groupManager.getGroupInstance("Lima2");
        groupManager.addGroup(group);
        modelManager.grant(group, role);
        modelManager.revoke(group, role);
        group = groupManager.getGroupByName("Lima2");
        assertFalse(((DynamicGroup) group).getRoles().contains(role));
        assertFalse(((DynamicRole) role).getGroups().contains(group));
    }
    @Test
    public void testRetrieveingUsersByGroup() throws Exception
    {
        User user = userManager.getUserInstance("Joe3");
        userManager.addUser(user, "mc");
        String GROUP_NAME = "oddbug2";
        Group group = null;
        GroupManager groupManager = securityService.getGroupManager();
        try
        {
            group = groupManager.getGroupByName("");
        }
        catch (UnknownEntityException uue)
        {
            group = groupManager.getGroupInstance(GROUP_NAME);
            groupManager.addGroup(group);
        }
        assertNotNull(group);
        user = null;
        UserManager userManager = securityService.getUserManager();
        user = userManager.getUser("joe3");
        ((DynamicModelManager) securityService.getModelManager()).grant(user, group);
        assertTrue(((DynamicGroup) group).getUsers().contains(user));
        group = groupManager.getGroupByName(GROUP_NAME);
        Set<User> users = ((DynamicGroup) group).getUsers();
        int size = users.size();
        assertEquals(1, size);
        // assertTrue("Check class:" + users.getClass().getName(),users
        // instanceof UserSet);
        boolean found = false;
        Set<User> newSet = new HashSet<User>();
        for (User u : users)
        {
            if (u.equals(user))
            {
                found = true;
                newSet.add(u);
            }
        }
        assertTrue(found);
        assertTrue(users.contains(user));
    }
    @Test
    public void testAddRemoveDelegate() throws Exception
    {
        DynamicUser borris = (DynamicUser) userManager.getUserInstance(USERNAME_BORRIS);
        userManager.addUser(borris, "mc");
        DynamicUser sam = (DynamicUser) userManager.getUserInstance(USERNAME_SAM);
        userManager.addUser(sam, "mc");
        modelManager.addDelegate(borris, sam);
        assertTrue(borris.getDelegatees().contains(sam));
        assertTrue(sam.getDelegators().contains(borris));

        DynamicUser borrisLoaded = (DynamicUser) userManager.getUser(USERNAME_BORRIS);
        DynamicUser samLoaded = (DynamicUser) userManager.getUser(USERNAME_SAM);
        assertTrue(borrisLoaded.getDelegatees().contains(samLoaded));
        assertTrue(samLoaded.getDelegators().contains(borrisLoaded));

        // Now grant borris some permissions and check sam has them
        Group group = groupManager.getGroupInstance();
        group.setName(ONLY_BORRIS_GROUP);
        groupManager.addGroup(group);
        Role role = roleManager.getRoleInstance();
        role.setName(ONLY_BORRIS_ROLE);
        roleManager.addRole(role);
        Permission permission = permissionManager.getPermissionInstance();
        permission.setName(ONLY_BORRIS_PERMISSION);
        permissionManager.addPermission(permission);

        modelManager.grant(role, permission);
        modelManager.grant(group, role);
        modelManager.grant(borris, group);

        DynamicAccessControlList acl = (DynamicAccessControlList) userManager.getACL(sam);
        assertTrue(acl.hasPermission(permission));
        assertTrue(acl.hasRole(role));

        // Now just to be silly make it recursive and check permissions work
        modelManager.addDelegate(sam, borris);
        acl = (DynamicAccessControlList) userManager.getACL(sam);
        assertTrue(acl.hasPermission(permission));
        assertTrue(acl.hasRole(role));

        modelManager.removeDelegate(borris, sam);
        assertFalse(borris.getDelegatees().contains(sam));
        assertFalse(sam.getDelegators().contains(borris));

        borrisLoaded = (DynamicUser) userManager.getUser(USERNAME_BORRIS);
        samLoaded = (DynamicUser) userManager.getUser(USERNAME_SAM);
        assertFalse(borrisLoaded.getDelegatees().contains(samLoaded));
        assertFalse(samLoaded.getDelegators().contains(borrisLoaded));

        boolean thrown = false;
        try
        {
            modelManager.removeDelegate(borris, sam);
        }
        catch (DataBackendException e)
        {
            throw e;
        }
        catch (UnknownEntityException e)
        {
            thrown = true;
        }
        assertTrue(thrown);
    }
}
