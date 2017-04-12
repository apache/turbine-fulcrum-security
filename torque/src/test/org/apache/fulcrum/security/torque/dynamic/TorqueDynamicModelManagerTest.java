package org.apache.fulcrum.security.torque.dynamic;

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
import org.apache.fulcrum.security.model.dynamic.test.AbstractDynamicModelManagerTest;
import org.apache.fulcrum.security.torque.HsqlDB;
import org.apache.fulcrum.security.torque.om.TorqueDynamicGroupPeer;
import org.apache.fulcrum.security.torque.om.TorqueDynamicGroupRolePeer;
import org.apache.fulcrum.security.torque.om.TorqueDynamicPermissionPeer;
import org.apache.fulcrum.security.torque.om.TorqueDynamicRolePeer;
import org.apache.fulcrum.security.torque.om.TorqueDynamicRolePermissionPeer;
import org.apache.fulcrum.security.torque.om.TorqueDynamicUserDelegatesPeer;
import org.apache.fulcrum.security.torque.om.TorqueDynamicUserGroupPeer;
import org.apache.fulcrum.security.torque.om.TorqueDynamicUserPeer;
import org.apache.torque.TorqueException;
import org.apache.torque.criteria.Criteria;
import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;


/**
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @author <a href="jh@byteaction.de">J&#252;rgen Hoffmann</a>
 * @version $Id:$
 */
public class TorqueDynamicModelManagerTest extends AbstractDynamicModelManagerTest
{
    protected static HsqlDB hsqlDB = null;

    @Override
	@Before
    public void setUp()
    {
        try
        {
            hsqlDB = new HsqlDB("jdbc:hsqldb:.", "src/test/fulcrum-dynamic-schema.sql");
            hsqlDB.addSQL("src/test/id-table-schema.sql");
            hsqlDB.addSQL("src/test/fulcrum-dynamic-schema-idtable-init.sql");

            this.setRoleFileName("src/test/DynamicTorqueRoleConfig.xml");
            this.setConfigurationFileName("src/test/DynamicTorqueComponentConfig.xml");
            securityService = (SecurityService) lookup(SecurityService.ROLE);
            super.setUp();
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
    }

    @Override
    @After
	public void tearDown()
    {
        // cleanup tables
        try
        {
            Criteria criteria = new Criteria();
            criteria.where(TorqueDynamicUserGroupPeer.USER_ID, 0, Criteria.GREATER_THAN);
            TorqueDynamicUserGroupPeer.doDelete(criteria);

            criteria = new Criteria();
            criteria.where(TorqueDynamicGroupRolePeer.GROUP_ID, 0, Criteria.GREATER_THAN);
            TorqueDynamicGroupRolePeer.doDelete(criteria);

            criteria = new Criteria();
            criteria.where(TorqueDynamicRolePermissionPeer.ROLE_ID, 0, Criteria.GREATER_THAN);
            TorqueDynamicRolePermissionPeer.doDelete(criteria);

            criteria = new Criteria();
            criteria.where(TorqueDynamicUserDelegatesPeer.DELEGATEE_USER_ID, 0, Criteria.GREATER_THAN);
            TorqueDynamicUserDelegatesPeer.doDelete(criteria);

            criteria = new Criteria();
            criteria.where(TorqueDynamicUserPeer.USER_ID, 0, Criteria.GREATER_THAN);
            TorqueDynamicUserPeer.doDelete(criteria);

            criteria = new Criteria();
            criteria.where(TorqueDynamicGroupPeer.GROUP_ID, 0, Criteria.GREATER_THAN);
            TorqueDynamicGroupPeer.doDelete(criteria);

            criteria = new Criteria();
            criteria.where(TorqueDynamicRolePeer.ROLE_ID, 0, Criteria.GREATER_THAN);
            TorqueDynamicRolePeer.doDelete(criteria);

            criteria = new Criteria();
            criteria.where(TorqueDynamicPermissionPeer.PERMISSION_ID, 0, Criteria.GREATER_THAN);
            TorqueDynamicPermissionPeer.doDelete(criteria);
        }
        catch (TorqueException e)
        {
            fail(e.toString());
        }

        securityService = null;
    }

}
