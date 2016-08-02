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

import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.model.turbine.test.AbstractTurbineModelManagerTest;
import org.apache.fulcrum.security.torque.HsqlDB;
import org.apache.fulcrum.security.torque.om.TurbineGroupPeer;
import org.apache.fulcrum.security.torque.om.TurbinePermissionPeer;
import org.apache.fulcrum.security.torque.om.TurbineRolePeer;
import org.apache.fulcrum.security.torque.om.TurbineRolePermissionPeer;
import org.apache.fulcrum.security.torque.om.TurbineUserGroupRolePeer;
import org.apache.fulcrum.security.torque.om.TurbineUserPeer;
import org.apache.torque.TorqueException;
import org.apache.torque.criteria.Criteria;

/**
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @author <a href="jh@byteaction.de">J&#252;rgen Hoffmann</a>
 * @version $Id:$
 */
public class TurbineDefaultModelManagerTest
    extends AbstractTurbineModelManagerTest
{
    protected static HsqlDB hsqlDB = null;

    @Override
	public void setUp() throws Exception
    {

        try
        {
            hsqlDB = new HsqlDB("jdbc:hsqldb:.", "src/test/fulcrum-turbine-default-schema.sql");
            // we do not need id-broker,set native in schema and added identity in hsql
            // same for both flavors
            this.setRoleFileName("src/test/TurbineTorqueRoleConfig.xml");
            // we have to use declared peers 
            this.setConfigurationFileName("src/test/TurbineDefaultWithPeersComponentConfig.xml");
            securityService = (SecurityService) lookup(SecurityService.ROLE);
            super.setUp();
        }
        catch (Exception e)
        {
            fail(e.toString());
        }

    }
   

    @Override
	public void tearDown()
    {
        // cleanup tables
        try
        {
            Criteria criteria = new Criteria();
            criteria.where(TurbineUserGroupRolePeer.USER_ID, 0, Criteria.GREATER_THAN);
            TurbineUserGroupRolePeer.doDelete(criteria);

            criteria = new Criteria();
            criteria.where(TurbineRolePermissionPeer.ROLE_ID, 0, Criteria.GREATER_THAN);
            TurbineRolePermissionPeer.doDelete(criteria);

            criteria = new Criteria();
            criteria.where(TurbineUserPeer.USER_ID, 0, Criteria.GREATER_THAN);
            TurbineUserPeer.doDelete(criteria);

            criteria = new Criteria();
            criteria.where(TurbineGroupPeer.GROUP_ID, 0, Criteria.GREATER_THAN);
            TurbineGroupPeer.doDelete(criteria);

            criteria = new Criteria();
            criteria.where(TurbineRolePeer.ROLE_ID, 0, Criteria.GREATER_THAN);
            TurbineRolePeer.doDelete(criteria);

            criteria = new Criteria();
            criteria.where(TurbinePermissionPeer.PERMISSION_ID, 0, Criteria.GREATER_THAN);
            TurbinePermissionPeer.doDelete(criteria);
        }
        catch (TorqueException e)
        {
            fail(e.toString());
        }

        modelManager = null;
        securityService = null;
    }

    /**
 	 * Constructor for TurbineDefaultModelManagerTest.
	 * @param arg0
	 */
    public TurbineDefaultModelManagerTest(String arg0)
    {
        super(arg0);
    }
}