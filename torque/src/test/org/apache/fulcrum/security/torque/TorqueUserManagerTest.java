package org.apache.fulcrum.security.torque;
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
import static org.junit.jupiter.api.Assertions.fail;

import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.model.test.AbstractUserManagerTest;
import org.apache.fulcrum.security.torque.om.BaseTorqueTurbineUserPeer;
import org.apache.fulcrum.security.util.UserSet;
import org.apache.torque.ColumnImpl;
import org.apache.torque.criteria.Criteria;
import org.apache.torque.criteria.SqlEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @author <a href="jh@byteaction.de">J&#252;rgen Hoffmann</a>
 *
 * @version $Id:$
 */
public class TorqueUserManagerTest extends AbstractUserManagerTest
{
    protected static HsqlDB hsqlDB = null;

    @BeforeEach
    public void setUp()
    {
        try
        {
            hsqlDB = new HsqlDB( "src/test/fulcrum-basic-schema.sql" );
            hsqlDB.addSQL( "src/test/fulcrum-dynamic-schema.sql" );
            hsqlDB.addSQL( "src/test/fulcrum-turbine-schema.sql" );
            hsqlDB.addSQL( "src/test/id-table-schema.sql" );
            hsqlDB.addSQL( "src/test/fulcrum-basic-schema-idtable-init.sql" );
            hsqlDB.addSQL( "src/test/fulcrum-dynamic-schema-idtable-init.sql" );
            hsqlDB.addSQL( "src/test/fulcrum-turbine-schema-idtable-init.sql" );

            this.setRoleFileName( "src/test/DynamicTorqueRoleConfig.xml" );
            this.setConfigurationFileName( "src/test/DynamicTorqueComponentConfig.xml" );
            securityService = (SecurityService) lookup( SecurityService.ROLE );
            userManager = securityService.getUserManager();
        } catch (Exception e)
        {
            fail( e.toString() );
        }
    }

    @Test
    @Override
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

        Criteria nameCriteria1 = new Criteria().where( new ColumnImpl( "FULCRUM_DYNAMIC_USER", "LOGIN_NAME" ),
                "claire" );
        UserSet userSet1 = userManager.retrieveUserList( nameCriteria1 );
        assertEquals( 1, userSet1.size() );
        Criteria nameCriteria2 = new Criteria().where( new ColumnImpl( "FULCRUM_DYNAMIC_USER", "LOGIN_NAME" ), "clair%",
                Criteria.LIKE );
        UserSet userSet = userManager.retrieveUserList( nameCriteria2 );
        assertEquals( 2, userSet.size() );
    }

    @Override
    public void tearDown()
    {
        /*
         * // cleanup tables try { Criteria criteria = new Criteria();
         * criteria.where(TorqueDynamicUserGroupPeer.USER_ID, 0, Criteria.GREATER_THAN);
         * TorqueDynamicUserGroupPeer.doDelete(criteria);
         *
         * criteria = new Criteria(); criteria.where(TorqueDynamicGroupRolePeer.GROUP_ID, 0,
         * Criteria.GREATER_THAN); TorqueDynamicGroupRolePeer.doDelete(criteria);
         *
         * criteria = new Criteria(); criteria.where(TorqueDynamicRolePermissionPeer.ROLE_ID, 0,
         * Criteria.GREATER_THAN); TorqueDynamicRolePermissionPeer.doDelete(criteria);
         *
         * criteria = new Criteria();
         * criteria.where(TorqueDynamicUserDelegatesPeer.DELEGATEE_USER_ID, 0,
         * Criteria.GREATER_THAN); TorqueDynamicUserDelegatesPeer.doDelete(criteria);
         *
         * criteria = new Criteria(); criteria.where(TorqueUserPeer.USER_ID, 0,
         * Criteria.GREATER_THAN); TorqueUserPeer.doDelete(criteria);
         *
         * criteria = new Criteria(); criteria.where(TorqueGroupPeer.GROUP_ID, 0,
         * Criteria.GREATER_THAN); TorqueGroupPeer.doDelete(criteria);
         *
         * criteria = new Criteria(); criteria.where(TorqueRolePeer.ROLE_ID, 0,
         * Criteria.GREATER_THAN); TorqueRolePeer.doDelete(criteria);
         *
         * criteria = new Criteria(); criteria.where(TorquePermissionPeer.PERMISSION_ID, 0,
         * Criteria.GREATER_THAN); TorquePermissionPeer.doDelete(criteria); } catch
         * (TorqueException e) { fail(e.toString()); }
         */
        user = null;
        userManager = null;
        securityService = null;
    }

}
