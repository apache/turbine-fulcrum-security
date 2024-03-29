package org.apache.fulcrum.security.torque.turbine;

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

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.UserManager;
import org.apache.fulcrum.security.acl.AccessControlList;
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.turbine.TurbineAccessControlList;
import org.apache.fulcrum.security.model.turbine.TurbineModelManager;
import org.apache.fulcrum.security.model.turbine.entity.TurbineUser;
import org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRole;
import org.apache.fulcrum.security.torque.HsqlDB;
import org.apache.fulcrum.security.torque.om.TorqueTurbineGroupPeer;
import org.apache.fulcrum.security.torque.om.TorqueTurbinePermissionPeer;
import org.apache.fulcrum.security.torque.om.TorqueTurbineRolePeer;
import org.apache.fulcrum.security.torque.om.TorqueTurbineRolePermissionPeer;
import org.apache.fulcrum.security.torque.om.TorqueTurbineUserGroupRolePeer;
import org.apache.fulcrum.security.torque.om.TorqueTurbineUserPeer;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.PasswordMismatchException;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.apache.fulcrum.security.util.UserSet;
import org.apache.fulcrum.testcontainer.BaseUnit5Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.torque.TorqueException;
import org.apache.torque.criteria.Criteria;
import org.apache.torque.util.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test user with attached object (user-role-goup relationship)
 * 
 * @author <a href="mailto:gk@apache.org">Georg Kallidis</a>
 * @version $Id$
 */
public class TurbineUserManagerTest extends BaseUnit5Test

{

    private User user;

    private UserManager userManager;

    private SecurityService securityService;

    private static final String TEST_GROUP = "TEST_GROUP";

    private static final String TEST_ROLE = "TEST_Role";

    private Group group;

    private Role role;

    private Logger logger = LogManager.getLogger();

    private static HsqlDB hsqlDB = null;

    @BeforeEach
    public void setUp()
    {
        try
        {
            hsqlDB = new HsqlDB( "src/test/fulcrum-turbine-schema.sql" );
            hsqlDB.addSQL( "src/test/id-table-schema.sql" );
            hsqlDB.addSQL( "src/test/fulcrum-turbine-schema-idtable-init.sql" );

            this.setRoleFileName( "src/test/TurbineTorqueRoleConfig.xml" );
            // we have to use declared peers
            this.setConfigurationFileName( "src/test/TurbineTorqueComponentConfig.xml" );
            securityService = (SecurityService) lookup( SecurityService.ROLE );
            userManager = securityService.getUserManager();

            group = securityService.getGroupManager().getGroupInstance();
            group.setName( TEST_GROUP );
            securityService.getGroupManager().addGroup( group );
            role = securityService.getRoleManager().getRoleInstance();
            role.setName( TEST_ROLE );
            securityService.getRoleManager().addRole( role );

        } catch (Exception e)
        {
            fail( e.toString() );
        }
    }

    @AfterEach
    public void tearDown()
    {

        // cleanup tables
        Connection con = null;
        try
        {
            con = Transaction.begin();// "default"

            Criteria criteria = new Criteria();
            criteria.where( TorqueTurbineUserGroupRolePeer.USER_ID, -1, Criteria.GREATER_THAN );

            TorqueTurbineUserGroupRolePeer.doDelete( criteria, con );

            criteria = new Criteria();
            criteria.where( TorqueTurbineRolePermissionPeer.ROLE_ID, 0, Criteria.GREATER_THAN );
            TorqueTurbineRolePermissionPeer.doDelete( criteria, con );

            criteria = new Criteria();
            criteria.where( TorqueTurbineUserPeer.USER_ID, 0, Criteria.GREATER_THAN );
            TorqueTurbineUserPeer.doDelete( criteria, con );

            criteria = new Criteria();
            criteria.where( TorqueTurbineGroupPeer.GROUP_ID, 0, Criteria.GREATER_THAN );
            TorqueTurbineGroupPeer.doDelete( criteria, con );

            criteria = new Criteria();
            criteria.where( TorqueTurbineRolePeer.ROLE_ID, 0, Criteria.GREATER_THAN );
            TorqueTurbineRolePeer.doDelete( criteria, con );

            criteria = new Criteria();
            criteria.where( TorqueTurbinePermissionPeer.PERMISSION_ID, 0, Criteria.GREATER_THAN );
            TorqueTurbinePermissionPeer.doDelete( criteria, con );

            con.commit();
            con = null;
        } catch (TorqueException e)
        {
            fail( e.toString() );
        } catch (SQLException e)
        {
            if (con != null)
            {
                Transaction.safeRollback( con );
            }
            fail( e.toString() );
        }

        user = null;
        userManager = null;
        securityService = null;
    }

    // requires default user in setup
    @Test
    public void testCheckExists() throws Exception
    {
        user = userManager.getUserInstance( "Philip" );
        userManager.addUser( user, "bobo" );
        addDefaultGrantUserGroupRole( user );

        assertTrue( userManager.checkExists( "philip" ) );
        assertTrue( userManager.checkExists( user ) );
        assertFalse( userManager.checkExists( "ImaginaryFriend" ) );
        user = userManager.getUserInstance( "ImaginaryFriend" );
        assertFalse( userManager.checkExists( user ) );
    }

    @Test
    public void testCheckExistsWithString() throws Exception
    {
        user = userManager.getUserInstance( "Philip2" );
        userManager.addUser( user, "bobo" );
        addDefaultGrantUserGroupRole( user );

        assertTrue( userManager.checkExists( "philip2" ) );
        assertTrue( userManager.checkExists( user.getName() ) );
        assertFalse( userManager.checkExists( "ImaginaryFriend2" ) );
        user = userManager.getUserInstance( "ImaginaryFriend2" );
        assertFalse( userManager.checkExists( user.getName() ) );
    }

    /*
     * Class to test for User retrieve(String)
     */
    @Test
    public void testGetUserString() throws Exception
    {
        user = userManager.getUserInstance( "QuietMike" );
        userManager.addUser( user, "bobo" );
        addDefaultGrantUserGroupRole( user );

        user = userManager.getUser( "QuietMike" );
        assertNotNull( user );
    }

    @Test
    public void testGetUserById() throws Exception
    {
        user = userManager.getUserInstance( "QuietMike2" );
        userManager.addUser( user, "bobo" );
        User user2 = userManager.getUserById( user.getId() );
        assertEquals( user.getName(), user2.getName() );
        assertEquals( user.getId(), user2.getId() );
    }

    /*
     * Class to test for User retrieve(String, String)
     */
    @Test
    public void testGetUserStringString() throws Exception
    {
        user = userManager.getUserInstance( "Richard" );
        userManager.addUser( user, "va" );

        addDefaultGrantUserGroupRole( user );

        user = userManager.getUser( "Richard", "va" );
        assertNotNull( user );
        user = userManager.getUser( "richard", "va" );
        assertNotNull( user );
        try
        {
            user = userManager.getUser( "richard", "VA" );
            fail( "should have thrown PasswordMismatchException" );
        } catch (PasswordMismatchException pme)
        {
            // good
        }
    }

    @Test
    public void testGetAllUsers() throws Exception
    {
        int size = userManager.getAllUsers().size();
        user = userManager.getUserInstance( "Bob" );
        userManager.addUser( user, "" );
        addDefaultGrantUserGroupRole( user );

        UserSet<User> userSet = userManager.getAllUsers();
        assertEquals( size + 1, userSet.size() );
    }

    @Test
    public void testAuthenticate() throws Exception
    {
        user = userManager.getUserInstance( "Kay" );
        userManager.addUser( user, "jc" );
        addDefaultGrantUserGroupRole( user );
        userManager.authenticate( user, "jc" );
        try
        {
            userManager.authenticate( user, "JC" );
            fail( "should have thrown PasswordMismatchException" );
        } catch (PasswordMismatchException pme)
        {
            // good
        }
    }

    @Test
    public void testChangePassword() throws Exception
    {
        user = userManager.getUserInstance( "Jonathan" );
        userManager.addUser( user, "jc" );
        addDefaultGrantUserGroupRole( user );
        try
        {
            userManager.changePassword( user, "WrongPWD", "JC" );
            fail( "should have thrown PasswordMismatchException" );
        } catch (PasswordMismatchException pme)
        {
            // good
        }
        userManager.changePassword( user, "jc", "JC" );
        userManager.authenticate( user, "JC" );
    }

    @Test
    public void testForcePassword() throws Exception
    {
        user = userManager.getUserInstance( "Connor" );
        userManager.addUser( user, "jc_subset" );
        addDefaultGrantUserGroupRole( user );

        userManager.forcePassword( user, "JC_SUBSET" );
        userManager.authenticate( user, "JC_SUBSET" );
    }

    /*
     * Class to test for User getUserInstance()
     */
    @Test
    public void testGetUserInstance() throws Exception
    {
        user = userManager.getUserInstance();
        assertNotNull( user );
        assertTrue( user.getName() == null );
    }

    /*
     * Class to test for User getUserInstance(String)
     */
    @Test
    public void testGetUserInstanceString() throws Exception
    {
        user = userManager.getUserInstance( "Philip" );
        assertEquals( "philip", user.getName() );
    }

    /**
     * Need to figure out if save is something we want.. right now it just bloes up if you
     * actually change anything.
     * 
     * @todo figur out what to do here...
     * @throws Exception
     */
    @Test
    public void testSaveUser() throws Exception
    {
        user = userManager.getUserInstance( "Kate" );
        userManager.addUser( user, "katiedid" );

        addDefaultGrantUserGroupRole( user );

        user = userManager.getUser( user.getName() );
        // user.setName("Katherine");
        userManager.saveUser( user );
        assertEquals( "kate", userManager.getUser( user.getName() ).getName() );
    }

    @Test
    public void testGetACL() throws Exception
    {
        user = userManager.getUserInstance( "Tony" );
        userManager.addUser( user, "california" );
        addDefaultGrantUserGroupRole( user );

        AccessControlList acl = userManager.getACL( user );

        assertNotNull( acl );

        Role testRole = securityService.getRoleManager().getRoleByName( TEST_ROLE );
        Group testGroup = securityService.getGroupManager().getGroupByName( TEST_GROUP );
        assertTrue( ((TurbineAccessControlList) acl).hasRole( testRole, testGroup ) );

        Group globalGroup = securityService.<TurbineModelManager>getModelManager().getGlobalGroup();
        securityService.<TurbineModelManager>getModelManager().grant( user, globalGroup, testRole );
        // immutable
        acl = userManager.getACL( user );

        assertTrue( ((TurbineAccessControlList) acl).hasRole( testRole ) );
    }

    @Test
    public void testRemoveUser() throws Exception
    {
        user = userManager.getUserInstance( "Rick" );
        userManager.addUser( user, "nb" );
        addDefaultGrantUserGroupRole( user );
        // required
        revokeDefaultGrantUserGroupRole( user );
        userManager.removeUser( user );
        try
        {
            User user2 = userManager.getUser( user.getName() );
            fail( "Should have thrown UEE" );
        } catch (UnknownEntityException uee)
        {
            // good
        }
    }

    @Test
    public void testAddUser() throws Exception
    {
        user = userManager.getUserInstance( "Joe1" );
        assertNull( user.getId() );
        userManager.addUser( user, "mc" );
        addDefaultGrantUserGroupRole( user );
        user = userManager.getUserInstance( "Joe2" );
        assertNull( user.getId() );
        userManager.addUser( user, "mc" );
        assertNotNull( user.getId() );
        assertNotNull( userManager.getUser( user.getName() ) );
    }

    /*
     * Class to test for boolean checkExists(string)
     */
    public void testAddUserTwiceFails() throws Exception
    {
        user = userManager.getUserInstance( "EATLUNCH" );
        userManager.addUser( user, "bob" );
        addDefaultGrantUserGroupRole( user );
        assertTrue( userManager.checkExists( user.getName() ) );
        User user2 = userManager.getUserInstance( "EATLUNCH" );
        try
        {
            userManager.addUser( user2, "bob" );
        } catch (EntityExistsException uee)
        {
            // good
        }
        try
        {
            userManager.addUser( user2, "differentpassword" );
        } catch (EntityExistsException uee)
        {
            // good
        }
    }

    @Test
    public void testCheckUserCaseSensitiveExists() throws Exception
    {
        user = userManager.getUserInstance( "borrisJohnson" );
        userManager.addUser( user, "bob" );

        assertTrue( userManager.checkExists( "borrisJohnson" ) );
    }

    @Test
    public void testRetrieveUserList() throws Exception
    {
        int size = userManager.getAllUsers().size();
        assertEquals( 0, size );
        user = userManager.getUserInstance( "Bob" );
        userManager.addUser( user, "" );
        user = userManager.getUserInstance( "Claire" );
        userManager.addUser( user, "" );
        user = userManager.getUserInstance( "Clairanne" );
        userManager.addUser( user, "" );

        UserSet allUserSet = userManager.getAllUsers();
        assertEquals( 3, allUserSet.size() );

        Criteria nameCriteria1 = new Criteria();
        nameCriteria1.where( TorqueTurbineUserPeer.LOGIN_NAME, "claire" );
        UserSet userSet1 = userManager.retrieveUserList( nameCriteria1 );
        assertEquals( 1, userSet1.size() );
        Criteria nameCriteria = new Criteria();
        nameCriteria.where( TorqueTurbineUserPeer.LOGIN_NAME, "clair%", Criteria.LIKE );
        UserSet userSet = userManager.retrieveUserList( nameCriteria );
        assertEquals( 2, userSet.size() );
    }

    private void addDefaultGrantUserGroupRole(User user) throws Exception
    {
        securityService.<TurbineModelManager>getModelManager().grant( user, group, role );
        boolean ugrFound = false;
        TurbineUserGroupRole ugrTest = null;
        for (TurbineUserGroupRole ugr : ((TurbineUser) user).getUserGroupRoleSet())
        {
            if (ugr.getUser().equals( user ) && ugr.getGroup().equals( group ) && ugr.getRole().equals( role ))
            {
                ugrFound = true;
                ugrTest = ugr;
                break;
            }
        }
        assertTrue( ugrFound );
        assertTrue( ugrTest.getGroup().equals( group ) );
        assertTrue( ugrTest.getUser().equals( user ) );
    }

    private void revokeDefaultGrantUserGroupRole(User user) throws Exception
    {
        securityService.<TurbineModelManager>getModelManager().revoke( user, group, role );
        boolean ugrFound = false;
        for (TurbineUserGroupRole ugr : ((TurbineUser) user).getUserGroupRoleSet())
        {
            if (ugr.getUser().equals( user ) && ugr.getGroup().equals( group ) && ugr.getRole().equals( role ))
            {
                ugrFound = true;
                break;
            }
        }
        ;
        assertFalse( ugrFound );
    }

}
