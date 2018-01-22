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

import static org.junit.Assert.*;

import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.model.turbine.test.AbstractTurbineModelManagerTest;
import org.apache.fulcrum.security.torque.HsqlDB;
import org.apache.fulcrum.security.torque.TorqueAbstractGroupManager;
import org.apache.fulcrum.security.torque.TorqueAbstractPermissionManager;
import org.apache.fulcrum.security.torque.TorqueAbstractRoleManager;
import org.apache.fulcrum.security.torque.TorqueAbstractUserManager;
import org.apache.fulcrum.security.torque.om.TorqueTurbineGroupPeer;
import org.apache.fulcrum.security.torque.om.TorqueTurbinePermissionPeer;
import org.apache.fulcrum.security.torque.om.TorqueTurbineRolePeer;
import org.apache.fulcrum.security.torque.om.TorqueTurbineRolePermissionPeer;
import org.apache.fulcrum.security.torque.om.TorqueTurbineUserGroupRolePeer;
import org.apache.fulcrum.security.torque.om.TorqueTurbineUserPeer;
import org.apache.fulcrum.security.torque.peer.PeerManagable;
import org.apache.fulcrum.security.torque.peer.managers.PeerGroupManager;
import org.apache.fulcrum.security.torque.peer.managers.PeerPermissionManager;
import org.apache.fulcrum.security.torque.peer.managers.PeerRoleManager;
import org.apache.fulcrum.security.torque.peer.managers.PeerUserManager;
import org.apache.torque.TorqueException;
import org.apache.torque.criteria.Criteria;

/**
 * Test with @link {@link #customPeers} requires at least Torque version 4.1.
 * 
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @author <a href="jh@byteaction.de">J&#252;rgen Hoffmann</a>
 * @version $Id$
 */
public class TorquePeerTurbineModelManagerTest
    extends AbstractTurbineModelManagerTest
{
    protected static HsqlDB hsqlDB = null;
    
    public static boolean customPeers = false;

    @Override
	public void setUp() throws Exception
    {
        try
        {
            hsqlDB = new HsqlDB("jdbc:hsqldb:.", "src/test/fulcrum-turbine-schema.sql");
            hsqlDB.addSQL("src/test/id-table-schema.sql");
            hsqlDB.addSQL("src/test/fulcrum-turbine-schema-idtable-init.sql");

            this.setRoleFileName("src/test/TurbineTorqueRoleConfig.xml");            
            
            if (customPeers)
                this.setConfigurationFileName("src/test/TurbineTorqueWithPeersComponentConfig.xml");
            else
                this.setConfigurationFileName("src/test/TurbineTorqueComponentConfig.xml");
            
            // The successful Test requires that the PeerImpl classes (in configuration file) implement the interface TorqueTurbinePeer,
            // cft. ClassCastException messages.
            // (interfaces could not yet automatically generated into Peers with Torque, cft JIRA Issue TORQUE-309).
            
            securityService = (SecurityService) lookup(SecurityService.ROLE);
            super.setUp();
        }
        catch (Exception e)
        {
        	fail(e.toString());
        }

    }
    
    public void testCustomPeerSet() {
        if (roleManager instanceof TorqueAbstractRoleManager) {
            assertTrue( "If a custom Peer for RoleManager should be tested, a peerClassName element should be set in the configuration file for roleManager.", ((PeerRoleManager)roleManager).getCustomPeer() == customPeers);
        }
        if (roleManager instanceof PeerManagable) {
            assertNotNull(((PeerManagable)roleManager).getPeerManager());
        }
        if (userManager instanceof TorqueAbstractUserManager) {
            assertTrue( "If a custom Peer for UserManager should be tested, a peerClassName element should be set in the configuration file for userManager.", ((PeerUserManager)userManager).getCustomPeer() == customPeers);
        }
        if (userManager instanceof PeerManagable) {
            assertNotNull(((PeerManagable)userManager).getPeerManager());
        }
        if (groupManager instanceof TorqueAbstractGroupManager) {
            assertTrue( "If a custom Peer for GroupManager should be tested, a peerClassName element should be set in the configuration file for groupManager.", ((PeerGroupManager)groupManager).getCustomPeer() == customPeers);
        }
        if (groupManager instanceof PeerManagable) {
            assertNotNull(((PeerManagable)groupManager).getPeerManager());
        }
        if (permissionManager instanceof TorqueAbstractPermissionManager) {
            assertTrue( "If a custom Peer for PermissionManager should be tested, a peerClassName element should be set in the configuration file for permissionManager.", ((PeerPermissionManager)permissionManager).getCustomPeer() == customPeers);
        }
        if (permissionManager instanceof PeerManagable) {
            assertNotNull(((PeerManagable)permissionManager).getPeerManager());
        }
    }

    @Override
	public void tearDown()
    {
        // cleanup tables
        try
        {
            Criteria criteria = new Criteria();
            criteria.where(TorqueTurbineUserGroupRolePeer.USER_ID, -1, Criteria.GREATER_THAN);
            TorqueTurbineUserGroupRolePeer.doDelete(criteria);

            criteria = new Criteria();
            criteria.where(TorqueTurbineRolePermissionPeer.ROLE_ID, 0, Criteria.GREATER_THAN);
            TorqueTurbineRolePermissionPeer.doDelete(criteria);

            criteria = new Criteria();
            criteria.where(TorqueTurbineUserPeer.USER_ID, 0, Criteria.GREATER_THAN);
            TorqueTurbineUserPeer.doDelete(criteria);

            criteria = new Criteria();
            criteria.where(TorqueTurbineGroupPeer.GROUP_ID, 0, Criteria.GREATER_THAN);
            TorqueTurbineGroupPeer.doDelete(criteria);

            criteria = new Criteria();
            criteria.where(TorqueTurbineRolePeer.ROLE_ID, 0, Criteria.GREATER_THAN);
            TorqueTurbineRolePeer.doDelete(criteria);

            criteria = new Criteria();
            criteria.where(TorqueTurbinePermissionPeer.PERMISSION_ID, 0, Criteria.GREATER_THAN);
            TorqueTurbinePermissionPeer.doDelete(criteria);
        }
        catch (TorqueException e)
        {
        	fail(e.toString());
        }

        modelManager = null;
        securityService = null;
    }

}
