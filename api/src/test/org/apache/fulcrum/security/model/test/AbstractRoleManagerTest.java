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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.fulcrum.security.RoleManager;
import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.RoleSet;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.apache.fulcrum.testcontainer.BaseUnit4Test;
import org.junit.Test;

/**
 * @author Eric Pugh
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractRoleManagerTest extends BaseUnit4Test
{
    protected Role role;
    protected RoleManager roleManager;
    protected SecurityService securityService;

    /*
     * Class to test for Role getRoleInstance()
     */
    @Test
    public void testGetRoleInstance() throws Exception
    {
        role = roleManager.getRoleInstance();
        assertNotNull(role);
        assertTrue(role.getName() == null);
    }

    /*
     * Class to test for Role getRoleInstance(String)
     */
    @Test
    public void testGetRoleInstanceString() throws Exception
    {
        role = roleManager.getRoleInstance("DOG_CATCHER");
        assertEquals("dog_catcher", role.getName());
    }
    @Test
    public void testGetRoleByName() throws Exception
    {
        role = roleManager.getRoleInstance("DOG_CATCHERd");
        roleManager.addRole(role);
        Role role2 = roleManager.getRoleByName("DOG_CATCHERd");
        assertEquals(role.getName(), role2.getName());
    }
    @Test
    public void testGetRoleById() throws Exception
    {
        role = roleManager.getRoleInstance("CLEAN_KENNEL_A");
        roleManager.addRole(role);
        Role role2 = roleManager.getRoleById(role.getId());
        assertEquals(role.getName(), role2.getName());
    }
    @Test
    public void testRenameRole() throws Exception
    {
        role = roleManager.getRoleInstance("CLEAN_KENNEL_X");
        roleManager.addRole(role);
        int size = roleManager.getAllRoles().size();
        roleManager.renameRole(role, "CLEAN_GROOMING_ROOM");
        Role role2 = roleManager.getRoleById(role.getId());
        assertEquals("clean_grooming_room", role2.getName());
        assertEquals(size, roleManager.getAllRoles().size());
    }
    @Test
    public void testGetAllRoles() throws Exception
    {
        int size = roleManager.getAllRoles().size();
        role = roleManager.getRoleInstance("CLEAN_KENNEL_J");
        roleManager.addRole(role);
        RoleSet roleSet = roleManager.getAllRoles();
        assertEquals(size + 1, roleSet.size());
    }
    @Test
    public void testAddRole() throws Exception
    {
        role = roleManager.getRoleInstance("DOG_NAPPER");
        assertNull(role.getId());
        roleManager.addRole(role);
        assertNotNull(role.getId());
        assertNotNull(roleManager.getRoleById(role.getId()));
    }
    @Test
    public void testRemoveRole() throws Exception
    {
        role = roleManager.getRoleInstance("CLEAN_KENNEL_K");
        roleManager.addRole(role);
        int size = roleManager.getAllRoles().size();
        roleManager.removeRole(role);
        try
        {
            Role role2 = roleManager.getRoleById(role.getId());
            fail("Should have thrown UEE");
        }
        catch (UnknownEntityException uee)
        {
            // good
        }
        assertEquals(size - 1, roleManager.getAllRoles().size());
    }
    @Test
    public void testCheckExists() throws Exception
    {
        role = roleManager.getRoleInstance("GREET_PEOPLE");
        roleManager.addRole(role);
        assertTrue(roleManager.checkExists(role));
        Role role2 = roleManager.getRoleInstance("WALK_DOGS");
        assertFalse(roleManager.checkExists(role2));
    }
    @Test
    public void testCheckExistsWithString() throws Exception
    {
        role = roleManager.getRoleInstance("GREET_PEOPLE2");
        roleManager.addRole(role);
        assertTrue(roleManager.checkExists(role.getName()));
        Role role2 = roleManager.getRoleInstance("WALK_DOGS2");
        assertFalse(roleManager.checkExists(role2.getName()));
    }

    /*
     * Class to test for boolean checkExists(string)
     */
    @Test
    public void testAddRoleTwiceFails() throws Exception
    {
        role = roleManager.getRoleInstance("EATLUNCH");
        roleManager.addRole(role);
        assertTrue(roleManager.checkExists(role.getName()));
        Role role2 = roleManager.getRoleInstance("EATLUNCH");
        try
        {
            roleManager.addRole(role2);
        }
        catch (EntityExistsException uee)
        {
            // good
        }
    }
}
