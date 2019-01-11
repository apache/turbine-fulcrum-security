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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.UserManager;
import org.apache.fulcrum.security.acl.AccessControlList;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.PasswordMismatchException;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.apache.fulcrum.security.util.UserSet;
import org.apache.fulcrum.testcontainer.BaseUnit5Test;
import org.junit.jupiter.api.Test;


/**
 * @author Eric Pugh
 */
public abstract class AbstractUserManagerTest extends BaseUnit5Test
{
    protected User user;
    protected UserManager userManager;
    protected SecurityService securityService;

    @Test
    public void testCheckExists() throws Exception
    {
        user = userManager.getUserInstance("Philip");
        userManager.addUser(user, "bobo");
        assertTrue(userManager.checkExists("philip"));
        assertTrue(userManager.checkExists(user));
        assertFalse(userManager.checkExists("ImaginaryFriend"));
        user = userManager.getUserInstance("ImaginaryFriend");
        assertFalse(userManager.checkExists(user));
    }
    @Test
    public void testCheckExistsWithString() throws Exception
    {
        user = userManager.getUserInstance("Philip2");
        userManager.addUser(user, "bobo");
        assertTrue(userManager.checkExists("philip2"));
        assertTrue(userManager.checkExists(user.getName()));
        assertFalse(userManager.checkExists("ImaginaryFriend2"));
        user = userManager.getUserInstance("ImaginaryFriend2");
        assertFalse(userManager.checkExists(user.getName()));
    }

    /*
     * Class to test for User retrieve(String)
     */
    @Test
    public void testGetUserString() throws Exception
    {
        user = userManager.getUserInstance("QuietMike");
        userManager.addUser(user, "bobo");
        user = userManager.getUser("QuietMike");
        assertNotNull(user);
    }
    @Test
    public void testGetUserById() throws Exception
    {
        user = userManager.getUserInstance("QuietMike2");
        userManager.addUser(user, "bobo");
        User user2 = userManager.getUserById(user.getId());
        assertEquals(user.getName(), user2.getName());
        assertEquals(user.getId(), user2.getId());
    }

    /*
     * Class to test for User retrieve(String, String)
     */
    @Test
    public void testGetUserStringString() throws Exception
    {
        user = userManager.getUserInstance("Richard");
        userManager.addUser(user, "va");
        user = userManager.getUser("Richard", "va");
        assertNotNull(user);
        user = userManager.getUser("richard", "va");
        assertNotNull(user);
        try
        {
            user = userManager.getUser("richard", "VA");
            fail("should have thrown PasswordMismatchException");
        }
        catch (PasswordMismatchException pme)
        {
            // good
        }
    }
    @Test
    public void testGetAllUsers() throws Exception
    {
        int size = userManager.getAllUsers().size();
        user = userManager.getUserInstance("Bob");
        userManager.addUser(user, "");
        UserSet userSet = userManager.getAllUsers();
        assertEquals(size + 1, userSet.size());
    }
    @Test
    public void testAuthenticate() throws Exception
    {
        user = userManager.getUserInstance("Kay");
        userManager.addUser(user, "jc");
        userManager.authenticate(user, "jc");
        try
        {
            userManager.authenticate(user, "JC");
            fail("should have thrown PasswordMismatchException");
        }
        catch (PasswordMismatchException pme)
        {
            // good
        }
    }
    @Test
    public void testChangePassword() throws Exception
    {
        user = userManager.getUserInstance("Jonathan");
        userManager.addUser(user, "jc");
        try
        {
            userManager.changePassword(user, "WrongPWD", "JC");
            fail("should have thrown PasswordMismatchException");
        }
        catch (PasswordMismatchException pme)
        {
            // good
        }
        userManager.changePassword(user, "jc", "JC");
        userManager.authenticate(user, "JC");
    }
    @Test
    public void testForcePassword() throws Exception
    {
        user = userManager.getUserInstance("Connor");
        userManager.addUser(user, "jc_subset");
        userManager.forcePassword(user, "JC_SUBSET");
        userManager.authenticate(user, "JC_SUBSET");
    }

    /**
     * Class to test for User getUserInstance()
     * @throws Exception generic exception
     */
    @Test
    public void testGetUserInstance() throws Exception
    {
        user = userManager.getUserInstance();
        assertNotNull(user);
        assertTrue(user.getName() == null);
    }

    /**
     * Class to test for User getUserInstance(String)
     * 
     * @throws Exception generic exception
     */
    @Test
    public void testGetUserInstanceString() throws Exception
    {
        user = userManager.getUserInstance("Philip");
        assertEquals("philip", user.getName());
    }

    /**
     * Need to figure out if save is something we want.. right now it just bloes
     * up if you actually cahnge anything.
     * 
     * @throws Exception generic exception
     */
    @Test
    public void testSaveUser() throws Exception
    {
    	// TODO figure out what to do here
        user = userManager.getUserInstance("Kate");
        userManager.addUser(user, "katiedid");
        user = userManager.getUser(user.getName());
        // user.setName("Katherine");
        userManager.saveUser(user);
        assertEquals("kate", userManager.getUser(user.getName()).getName());
    }

    public void testGetACL() throws Exception
    {
        user = userManager.getUserInstance("Tony");
        userManager.addUser(user, "california");
        AccessControlList acl = userManager.getACL(user);
        assertNotNull(acl);
    }

    public void testRemoveUser() throws Exception
    {
        user = userManager.getUserInstance("Rick");
        userManager.addUser(user, "nb");
        userManager.removeUser(user);
        try
        {
            User user2 = userManager.getUser(user.getName());
            fail("Should have thrown UEE");
        }
        catch (UnknownEntityException uee)
        {
            // good
        }
    }
    @Test
    public void testAddUser() throws Exception
    {
        user = userManager.getUserInstance("Joe1");
        assertNull(user.getId());
        userManager.addUser(user, "mc");
        user = userManager.getUserInstance("Joe2");
        assertNull(user.getId());
        userManager.addUser(user, "mc");
        assertNotNull(user.getId());
        assertNotNull(userManager.getUser(user.getName()));
    }

    /*
     * Class to test for boolean checkExists(string)
     */
    public void testAddUserTwiceFails() throws Exception
    {
        user = userManager.getUserInstance("EATLUNCH");
        userManager.addUser(user, "bob");
        assertTrue(userManager.checkExists(user.getName()));
        User user2 = userManager.getUserInstance("EATLUNCH");
        try
        {
            userManager.addUser(user2, "bob");
        }
        catch (EntityExistsException uee)
        {
            // good
        }
        try
        {
            userManager.addUser(user2, "differentpassword");
        }
        catch (EntityExistsException uee)
        {
            // good
        }
    }
    @Test
    public void testCheckUserCaseSensitiveExists() throws Exception
    {
        user = userManager.getUserInstance("borrisJohnson");
        userManager.addUser(user, "bob");

        assertTrue(userManager.checkExists("borrisJohnson"));
    }

}
