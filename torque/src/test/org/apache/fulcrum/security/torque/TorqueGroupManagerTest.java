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

import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.model.test.AbstractGroupManagerTest;

/**
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id:$
 */
public class TorqueGroupManagerTest extends AbstractGroupManagerTest
{
    protected static HsqlDB hsqlDB = null;

    public void setUp()
    {
        try
        {
            hsqlDB = new HsqlDB("jdbc:hsqldb:.", "src/test/fulcrum-basic-schema.sql");
            hsqlDB.addSQL("src/test/fulcrum-dynamic-schema.sql");
            hsqlDB.addSQL("src/test/fulcrum-turbine-schema.sql");
            hsqlDB.addSQL("src/test/id-table-schema.sql");
            hsqlDB.addSQL("src/test/fulcrum-basic-schema-idtable-init.sql");
            hsqlDB.addSQL("src/test/fulcrum-dynamic-schema-idtable-init.sql");
            hsqlDB.addSQL("src/test/fulcrum-turbine-schema-idtable-init.sql");

            this.setRoleFileName("src/test/DynamicTorqueRoleConfig.xml");
            this.setConfigurationFileName("src/test/DynamicTorqueComponentConfig.xml");
            securityService = (SecurityService) lookup(SecurityService.ROLE);
            groupManager = securityService.getGroupManager();
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
    }

    public void tearDown()
    {
/*        // cleanup tables
        try
        {
            Criteria criteria = new Criteria();
            criteria.add(TorqueDynamicUserGroupPeer.USER_ID, 0, Criteria.GREATER_THAN);
            TorqueDynamicUserGroupPeer.doDelete(criteria);

            criteria.clear();
            criteria.add(TorqueDynamicGroupRolePeer.GROUP_ID, 0, Criteria.GREATER_THAN);
            TorqueDynamicGroupRolePeer.doDelete(criteria);

            criteria.clear();
            criteria.add(TorqueDynamicRolePermissionPeer.ROLE_ID, 0, Criteria.GREATER_THAN);
            TorqueDynamicRolePermissionPeer.doDelete(criteria);

            criteria.clear();
            criteria.add(TorqueDynamicUserDelegatesPeer.DELEGATEE_USER_ID, 0, Criteria.GREATER_THAN);
            TorqueDynamicUserDelegatesPeer.doDelete(criteria);

            criteria.clear();
            criteria.add(TorqueUserPeer.USER_ID, 0, Criteria.GREATER_THAN);
            TorqueUserPeer.doDelete(criteria);

            criteria.clear();
            criteria.add(TorqueGroupPeer.GROUP_ID, 0, Criteria.GREATER_THAN);
            TorqueGroupPeer.doDelete(criteria);

            criteria.clear();
            criteria.add(TorqueRolePeer.ROLE_ID, 0, Criteria.GREATER_THAN);
            TorqueRolePeer.doDelete(criteria);

            criteria.clear();
            criteria.add(TorquePermissionPeer.PERMISSION_ID, 0, Criteria.GREATER_THAN);
            TorquePermissionPeer.doDelete(criteria);
        }
        catch (TorqueException e)
        {
            fail(e.toString());
        }
*/
        group = null;
        groupManager = null;
        securityService = null;
    }

    /**
     * Constructor for TorqueGroupManagerTest.
     * @param arg0
     */
    public TorqueGroupManagerTest(String arg0)
    {
        super(arg0);
    }
}
