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

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;

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
import org.apache.torque.util.Transaction;
import org.junit.After;
import org.junit.Before;

/**
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @author <a href="jh@byteaction.de">J&#252;rgen Hoffmann</a>
 * @version $Id:$
 */
public class TurbineDefaultModelManagerLazyTest
    extends AbstractTurbineModelManagerTest
{
    protected static HsqlDB hsqlDB = null;

    @Override
	@Before
	public void setUp() throws Exception
    {

        try
        {
            hsqlDB = new HsqlDB("jdbc:hsqldb:.", "src/test/fulcrum-turbine-default-schema.sql");
            // we do not need id-broker,set native in schema and added identity in hsql
            // same for both flavors
            this.setRoleFileName("src/test/TurbineTorqueRoleConfig.xml");
            // we have to use declared peers 
            this.setConfigurationFileName("src/test/TurbineDefaultLazyWithPeersComponentConfig.xml");
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
    	Connection con = null;
        try
        {
        	con = Transaction.begin();// "default"

        	Criteria criteria = new Criteria();
            criteria.where(TurbineUserGroupRolePeer.USER_ID, 0, Criteria.GREATER_THAN);
            
            TurbineUserGroupRolePeer.doDelete(criteria,con);

            criteria = new Criteria();
            criteria.where(TurbineRolePermissionPeer.ROLE_ID, 0, Criteria.GREATER_THAN);
            TurbineRolePermissionPeer.doDelete(criteria,con);

            criteria = new Criteria();
            criteria.where(TurbineUserPeer.USER_ID, 0, Criteria.GREATER_THAN);
            TurbineUserPeer.doDelete(criteria,con);
 
            criteria = new Criteria();
            criteria.where(TurbineGroupPeer.GROUP_ID, 0, Criteria.GREATER_THAN);
            TurbineGroupPeer.doDelete(criteria,con);

            criteria = new Criteria();
            criteria.where(TurbineRolePeer.ROLE_ID, 0, Criteria.GREATER_THAN);
            TurbineRolePeer.doDelete(criteria,con);

            criteria = new Criteria();
            criteria.where(TurbinePermissionPeer.PERMISSION_ID, 0, Criteria.GREATER_THAN);
            TurbinePermissionPeer.doDelete(criteria,con);
            
            con.commit();
            con = null;
        }
        catch (TorqueException e)
        {
        	fail(e.toString());
        } catch (SQLException e) {
        	 if (con != null)
             {
                 Transaction.safeRollback(con);
             }
        	 fail(e.toString());
		}

        modelManager = null;
        securityService = null;
    }

}
