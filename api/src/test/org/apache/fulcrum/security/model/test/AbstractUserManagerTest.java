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

import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.UserManager;
import org.apache.fulcrum.security.acl.AccessControlList;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.PasswordMismatchException;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.apache.fulcrum.security.util.UserSet;
import org.apache.fulcrum.testcontainer.BaseUnitTest;

/**
 * @author Eric Pugh
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractUserManagerTest extends BaseUnitTest
{
    protected User user;
    protected UserManager userManager;
    protected SecurityService securityService;

    /**
     * Constructor for AbstractUserManagerTest.
     * 
     * @param arg0
     */
    public AbstractUserManagerTest(String arg0)
    {
        super(arg0);
    }

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
    public void testGetUserString() throws Exception
    {
        user = userManager.getUserInstance("QuietMike");
        userManager.addUser(user, "bobo");
        user = userManager.getUser("QuietMike");
        assertNotNull(user);
    }

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

    public void testGetAllUsers() throws Exception
    {
        int size = userManager.getAllUsers().size();
        user = userManager.getUserInstance("Bob");
        userManager.addUser(user, "");
        UserSet userSet = userManager.getAllUsers();
        assertEquals(size + 1, userSet.size());
    }

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

    public void testForcePassword() throws Exception
    {
        user = userManager.getUserInstance("Connor");
        userManager.addUser(user, "jc_subset");
        userManager.forcePassword(user, "JC_SUBSET");
        userManager.authenticate(user, "JC_SUBSET");
    }

    /*
     * Class to test for User getUserInstance()
     */
    public void testGetUserInstance() throws Exception
    {
        user = userManager.getUserInstance();
        assertNotNull(user);
        assertTrue(user.getName() == null);
    }

    /*
     * Class to test for User getUserInstance(String)
     */
    public void testGetUserInstanceString() throws Exception
    {
        user = userManager.getUserInstance("Philip");
        assertEquals("philip", user.getName());
    }

    /**
     * Need to figure out if save is something we want.. right now it just bloes
     * up if you actually cahnge anything.
     * 
     * @todo figur out what to do here...
     * @throws Exception
     */
    public void testSaveUser() throws Exception
    {
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

    public void testCheckUserCaseSensitiveExists() throws Exception
    {
        user = userManager.getUserInstance("borrisJohnson");
        userManager.addUser(user, "bob");

        assertTrue(userManager.checkExists("borrisJohnson"));
    }

}
