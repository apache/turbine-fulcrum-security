package org.apache.fulcrum.security.model.test;

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

import org.apache.fulcrum.security.PermissionManager;
import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.model.dynamic.DynamicModelManager;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicRole;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.PermissionSet;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.apache.fulcrum.testcontainer.BaseUnitTest;

/**
 * @author Eric Pugh
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractPermissionManagerTest extends BaseUnitTest
{
    protected Permission permission;
    protected PermissionManager permissionManager;
    protected SecurityService securityService;

    /**
     * Constructor for PermissionManagerTest.
     * 
     * @param arg0
     */
    public AbstractPermissionManagerTest(String arg0)

    {
        super(arg0);
    }

    /*
     * Class to test for Permission getPermissionInstance()
     */
    public void testGetPermissionInstance() throws Exception
    {
        permission = permissionManager.getPermissionInstance();
        assertNotNull(permission);
        assertTrue(permission.getName() == null);
    }

    /*
     * Class to test for Permission getPermissionInstance(String)
     */
    public void testGetPermissionInstanceString() throws Exception
    {
        permission = permissionManager.getPermissionInstance("CAN_TREAT_ANIMALS");
        assertEquals("can_treat_animals", permission.getName());
    }

    public void testGetPermissionByName() throws Exception
    {
        permission = permissionManager.getPermissionInstance("CLEAN_KENNEL");
        permissionManager.addPermission(permission);
        Permission permission2 = permissionManager.getPermissionByName("CLEAN_KENNEL");
        assertEquals(permission.getName(), permission2.getName());
    }

    public void testGetPermissionById() throws Exception
    {
        permission = permissionManager.getPermissionInstance("ADMINSTER_DRUGS");
        permissionManager.addPermission(permission);
        Permission permission2 = permissionManager.getPermissionById(permission.getId());
        assertEquals(permission.getName(), permission2.getName());
    }

    public void testGetAllPermissions() throws Exception
    {
        int size = permissionManager.getAllPermissions().size();
        permission = permissionManager.getPermissionInstance("WALK_DOGS");
        permissionManager.addPermission(permission);
        PermissionSet permissionSet = permissionManager.getAllPermissions();
        assertEquals(size + 1, permissionSet.size());
    }

    public void testRenamePermission() throws Exception
    {
        permission = permissionManager.getPermissionInstance("CLEAN_FRONT_OFFICE");
        permissionManager.addPermission(permission);
        int size = permissionManager.getAllPermissions().size();
        permissionManager.renamePermission(permission, "CLEAN_GROOMING_ROOM");
        Permission permission2 = permissionManager.getPermissionById(permission.getId());
        assertEquals("CLEAN_GROOMING_ROOM".toLowerCase(), permission2.getName());
        assertEquals(size, permissionManager.getAllPermissions().size());
    }

    public void testRemovePermission() throws Exception
    {
        permission = permissionManager.getPermissionInstance("CLEAN_CAT_HOUSE");
        permissionManager.addPermission(permission);
        permissionManager.removePermission(permission);
        try
        {
            permission = permissionManager.getPermissionById(permission.getId());
            fail("Should have thrown UnknownEntityException");
        }
        catch (UnknownEntityException uee)
        {
            // good
        }
    }

    public void testAddPermission() throws Exception
    {
        permission = permissionManager.getPermissionInstance("CLEAN_BIG_KENNEL");
        assertNull(permission.getId());
        permissionManager.addPermission(permission);
        assertNotNull(permission.getId());
        permission = permissionManager.getPermissionById(permission.getId());
        assertNotNull(permission);
    }

    /*
     * Class to test for PermissionSet getPermissions(Role)
     */
    public void testGetPermissionsRole() throws Exception
    {
        permission = permissionManager.getPermissionInstance("GREET_PEOPLE");
        permissionManager.addPermission(permission);
        Permission permission2 = permissionManager.getPermissionInstance("ADMINISTER_DRUGS");
        permissionManager.addPermission(permission2);
        Role role = securityService.getRoleManager().getRoleInstance("VET_TECH");
        securityService.getRoleManager().addRole(role);
        ((DynamicModelManager) securityService.getModelManager()).grant(role, permission);
        PermissionSet permissions = ((DynamicRole) role).getPermissions();
        assertEquals(1, permissions.size());
        assertTrue(permissions.contains(permission));
        assertFalse(permissions.contains(permission2));
    }

    /*
     * Class to test for boolean checkExists(permission)
     */
    public void testCheckExistsPermission() throws Exception
    {
        permission = permissionManager.getPermissionInstance("OPEN_OFFICE");
        permissionManager.addPermission(permission);
        assertTrue(permissionManager.checkExists(permission));
        Permission permission2 = permissionManager.getPermissionInstance("CLOSE_OFFICE");
        assertFalse(permissionManager.checkExists(permission2));
    }

    /*
     * Class to test for boolean checkExists(string)
     */
    public void testCheckExistsPermissionWithString() throws Exception
    {
        permission = permissionManager.getPermissionInstance("OPEN_OFFICE2");
        permissionManager.addPermission(permission);
        assertTrue(permissionManager.checkExists(permission.getName()));
        Permission permission2 = permissionManager.getPermissionInstance("CLOSE_OFFICE2");
        assertFalse(permissionManager.checkExists(permission2.getName()));
    }

    /*
     * Class to test for boolean checkExists(string)
     */
    public void testAddPermissionTwiceFails() throws Exception
    {
        permission = permissionManager.getPermissionInstance("EATLUNCH");
        permissionManager.addPermission(permission);
        assertTrue(permissionManager.checkExists(permission.getName()));
        Permission permission2 = permissionManager.getPermissionInstance("EATLUNCH");
        try
        {
            permissionManager.addPermission(permission2);
        }
        catch (EntityExistsException uee)
        {
            // good
        }
    }
}
