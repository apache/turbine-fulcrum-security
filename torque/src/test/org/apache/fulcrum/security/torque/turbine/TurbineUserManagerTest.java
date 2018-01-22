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
import org.apache.fulcrum.security.torque.HsqlDB;
import org.apache.fulcrum.security.torque.om.TorqueTurbineGroupPeer;
import org.apache.fulcrum.security.torque.om.TorqueTurbinePermissionPeer;
import org.apache.fulcrum.security.torque.om.TorqueTurbineRolePeer;
import org.apache.fulcrum.security.torque.om.TorqueTurbineRolePermissionPeer;
import org.apache.fulcrum.security.torque.om.TorqueTurbineUserGroupRolePeer;
import org.apache.fulcrum.security.torque.om.TorqueTurbineUserPeer;
import org.apache.torque.TorqueException;
import org.apache.torque.criteria.Criteria;
import org.apache.torque.util.Transaction;
import org.junit.Before;

/**
 * Test user with attached object (user-role-goup relationship)
 * 
 * @author <a href="mailto:gk@apache.org">Georg Kallidis</a>
 *
 * @version $Id$
 */
public class TurbineUserManagerTest extends AbstractTurbineUserManagerTest
{
    protected static HsqlDB hsqlDB = null;

    @Before
    public void setUp()
    {
        try
        {
            hsqlDB = new HsqlDB("jdbc:hsqldb:.", "src/test/fulcrum-turbine-schema.sql");
            hsqlDB.addSQL("src/test/id-table-schema.sql");
            hsqlDB.addSQL("src/test/fulcrum-turbine-schema-idtable-init.sql");
            
            this.setRoleFileName("src/test/TurbineTorqueRoleConfig.xml");
            // we have to use declared peers 
            this.setConfigurationFileName("src/test/TurbineTorqueComponentConfig.xml");
            securityService = (SecurityService) lookup(SecurityService.ROLE);
            userManager = securityService.getUserManager();
            
            group = securityService.getGroupManager().getGroupInstance();
            group.setName(TEST_GROUP);
            securityService.getGroupManager().addGroup(group);
            role = securityService.getRoleManager().getRoleInstance();
            role.setName(TEST_ROLE);
            securityService.getRoleManager().addRole(role);
            
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
        Connection con = null;
        try
        {
            con = Transaction.begin();// "default"

            Criteria criteria = new Criteria();
            criteria.where(TorqueTurbineUserGroupRolePeer.USER_ID, -1, Criteria.GREATER_THAN);
            
            TorqueTurbineUserGroupRolePeer.doDelete(criteria,con);

            criteria = new Criteria();
            criteria.where(TorqueTurbineRolePermissionPeer.ROLE_ID, 0, Criteria.GREATER_THAN);
            TorqueTurbineRolePermissionPeer.doDelete(criteria,con);

            criteria = new Criteria();
            criteria.where(TorqueTurbineUserPeer.USER_ID, 0, Criteria.GREATER_THAN);
            TorqueTurbineUserPeer.doDelete(criteria,con);
 
            criteria = new Criteria();
            criteria.where(TorqueTurbineGroupPeer.GROUP_ID, 0, Criteria.GREATER_THAN);
            TorqueTurbineGroupPeer.doDelete(criteria,con);

            criteria = new Criteria();
            criteria.where(TorqueTurbineRolePeer.ROLE_ID, 0, Criteria.GREATER_THAN);
            TorqueTurbineRolePeer.doDelete(criteria,con);

            criteria = new Criteria();
            criteria.where(TorqueTurbinePermissionPeer.PERMISSION_ID, 0, Criteria.GREATER_THAN);
            TorqueTurbinePermissionPeer.doDelete(criteria,con);
            
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
        
        user = null;
        userManager = null;
        securityService = null;
    }


}
