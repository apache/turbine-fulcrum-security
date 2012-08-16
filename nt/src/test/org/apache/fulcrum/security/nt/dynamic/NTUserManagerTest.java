package org.apache.fulcrum.security.nt.dynamic;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.acl.AccessControlList;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicUser;
import org.apache.fulcrum.security.model.dynamic.entity.impl.DynamicUserImpl;
import org.apache.fulcrum.security.model.test.AbstractUserManagerTest;
import org.apache.fulcrum.security.util.DataBackendException;

import com.tagish.auth.win32.NTSystem;
/**
 *
 * Test the NT implementation of the user manager. This test traps some exceptions that can be
 * thrown if there is NO nt dll.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class NTUserManagerTest extends AbstractUserManagerTest implements TestConstants
{
    private static Log log = LogFactory.getLog(NTUserManagerTest.class);
    private static final String ERROR_MSG = "Not supported by NT User Manager";
    private static final String USERNAME = "Eric Pugh";
    private static final String DOMAIN = "IQUITOS";
    private static final String PASSWORD = "";
    private static final String GUESTUSER = DOMAIN + "/" + "Guest";

    public void setUp() throws Exception
    {
        this.setRoleFileName("src/test/DynamicNTRoleConfig.xml");
        this.setConfigurationFileName("src/test/DynamicNTComponentConfig.xml");
        securityService = (SecurityService) lookup(SecurityService.ROLE);
        userManager = securityService.getUserManager();
    }

    public void tearDown()
    {
        user = null;
        userManager = null;
        securityService = null;
    }

    /**
     * Constructor for NTUserManagerTest.
     *
     * @param arg0
     */
    public NTUserManagerTest(String arg0)
    {
        super(arg0);
    }

    public void testCheckExists() throws Exception
    {
        try
        {
            user = userManager.getUser(GUESTUSER, "");
            user.setPassword("");
            assertTrue(userManager.checkExists(user));
        }
        catch(DataBackendException dbe){
            assertTrue(dbe.getMessage().indexOf(SCB_INVALID)>-1);
        }
        catch (UnsatisfiedLinkError ule)
        {
            log.info("Unit test not being run due to missing NT DLL");
        }
    }

    public void testCheckExistsFails() throws Exception
    {
        try
        {
            user = new DynamicUserImpl();
            user.setName("MCD\\Ronald Mcdonaled");
            ((DynamicUser) user).setPassword("");
            assertFalse(userManager.checkExists(user));
        }
        catch (NoClassDefFoundError ule)
        {
            log.info("Unit test not being run due to missing NT DLL");
        }
    }

    /**
     * tests getting an NT username
     */
    public void testNTGetName() throws Exception
    {
        try
        {
            NTSystem ntSystem = new NTSystem();
            assertTrue(
                "Name is eric:" + ntSystem.getName(),
                USERNAME.equals(ntSystem.getName()));
        }
        catch (NoClassDefFoundError ule)
        {
            log.info("Unit test not being run due to missing NT DLL");
        }
    }

    /**
     * tests getting an NT Domain
     */
    public void testGetDomain() throws Exception
    {
        try
        {
            NTSystem ntSystem = new NTSystem();
            assertTrue(
                "Domain is:" + ntSystem.getDomain(),
                DOMAIN.equals(ntSystem.getDomain()));
        }
        catch (NoClassDefFoundError ule)
        {
            log.info("Unit test not being run due to missing NT DLL");
        }
    }

    /**
     * tests logging on a different user
     */
    public void OFFtestLoginAsUser() throws Exception
    {
        try
        {
            NTSystem ntSystem = new NTSystem();
            char password[] = "editor!".toCharArray();
            ntSystem.logon(USERNAME, password, DOMAIN);
            String groups[] = ntSystem.getGroupNames(false);
            for (int i = 0; i < groups.length; i++)
            {
                System.out.println("Groups :" + groups[i]);
            }
            ntSystem.logoff();
            assertTrue(
                "User is:" + ntSystem.getName(),
                USERNAME.equals(ntSystem.getName()));
            assertTrue(
                "Domain is:" + ntSystem.getName(),
                "IQUITOS".equals(ntSystem.getDomain()));
            assertTrue(
                "Primary Group is:" + ntSystem.getPrimaryGroupName(),
                "None".equals(ntSystem.getPrimaryGroupName()));
        }
        catch (NoClassDefFoundError ule)
        {
            log.info("Unit test not being run due to missing NT DLL");
        }
    }

    /*
     * test for User retrieve(String, String)
     */
    public void testGetUserStringString() throws Exception
    {
        try
        {
            user = userManager.getUser(GUESTUSER, PASSWORD);
            assertNotNull(user);
            assertTrue(((DynamicUser) user).getGroups().size() > 0);
        }
        catch (NoClassDefFoundError ule)
        {
            log.info("Unit test not being run due to missing NT DLL");
        }
    }

    public void testAuthenticate() throws Exception
    {
        try
        {
            user = userManager.getUserInstance(GUESTUSER);
            userManager.authenticate(user, PASSWORD);
        }
        catch (NoClassDefFoundError ule)
        {
            log.info("Unit test not being run due to missing NT DLL");
        }
    }

    public void testGetACL() throws Exception
    {
        try
        {
            user = userManager.getUserInstance(GUESTUSER);
            userManager.authenticate(user, PASSWORD);
            AccessControlList acl = userManager.getACL(user);
            assertNotNull(acl);
        }
        catch (NoClassDefFoundError ule)
        {
            log.info("Unit test not being run due to missing NT DLL");
        }
    }

    /** ******* ALL BELOW HERE THROW RUNTIME EXCEPTIONS ******** */
    /*
     * test for User retrieve(String, String)
     */
    public void testGetAllUsers() throws Exception
    {
        try
       {
           userManager.getAllUsers();
           fail("Should throw runtime exception");
       }
       catch (RuntimeException re)
       {
           assertTrue(re.getMessage().equals(ERROR_MSG));
       }
    }

    /*
     * Class to test for User retrieve(String)
     */
    public void testGetUserString() throws Exception
    {
        try
        {
            user = userManager.getUser("QuietMike");
            fail("Should throw runtime exception");
        }
        catch (RuntimeException re)
        {
            assertTrue(re.getMessage().equals(ERROR_MSG));
        }
    }

    public void testGetUserById() throws Exception
    {
        try
        {
            userManager.getUserById(null);
            fail("Should throw runtime exception");
        }
        catch (RuntimeException re)
        {
            assertTrue(re.getMessage().equals(ERROR_MSG));
        }
    }

    public void testChangePassword() throws Exception
    {
        try
        {
            user = userManager.getUser(GUESTUSER, "");
            user.setPassword("");

            userManager.changePassword(user, "", "newPassword");
            fail("Should throw runtime exception");
        }
        catch (NoClassDefFoundError ule)
        {
            log.info("Unit test not being run due to missing NT DLL");
        }
        catch (RuntimeException re)
        {
            assertTrue(re.getMessage().equals(ERROR_MSG));
        }
    }

    public void testForcePassword() throws Exception
    {
        try
        {
            user = userManager.getUser(GUESTUSER, "");
            user.setPassword("");

            userManager.forcePassword(user, "JC_SUBSET");
            fail("Should throw runtime exception");
        }
        catch (NoClassDefFoundError ule)
        {
            log.info("Unit test not being run due to missing NT DLL");
        }
        catch (RuntimeException re)
        {
            assertTrue(re.getMessage().equals(ERROR_MSG));
        }
    }

    public void testSaveUser() throws Exception
    {
        try
        {
            userManager.saveUser(user);
            fail("Should throw runtime exception");
        }
        catch (RuntimeException re)
        {
            assertTrue(re.getMessage().equals(ERROR_MSG));
        }
    }

    public void testRemoveUser() throws Exception
    {
        try
        {
            userManager.removeUser(user);
            fail("Should throw runtime exception");
        }
        catch (RuntimeException re)
        {
            assertTrue(re.getMessage().equals(ERROR_MSG));
        }
    }

    public void testAddUser() throws Exception
    {
        try
        {
            user = userManager.getUserInstance("Joe1");
            userManager.addUser(user, "mc");
        }
        catch (RuntimeException re)
        {
            assertTrue(re.getMessage().equals(ERROR_MSG));
        }
    }

    public void testRetrieveingUsersByGroup() throws Exception
    {
        try
        {
            user = userManager.getUserInstance("Joe1");
            userManager.addUser(user, "mc");
        }
        catch (RuntimeException re)
        {
            assertTrue(re.getMessage().equals(ERROR_MSG));
        }
    }

    /*
     * Override parent class, doesn't make sense..
     */
    public void testCheckExistsWithString() throws Exception
    {
        // empty
    }

    /*
     * Override parent class, doesn't make sense..
     */
    public void testAddUserTwiceFails() throws Exception
    {
        // empty
    }

    /*
     * Override parent class, doesn't make sense..
     */
    public void testCheckUserCaseSensitiveExists() throws Exception
    {
        // empty
    }
}
