package org.apache.fulcrum.security.torque.turbine.model;
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

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.acl.AccessControlList;
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.model.ACLFactory;
import org.apache.fulcrum.security.model.ACLFactoryTest;
import org.apache.fulcrum.security.model.turbine.TurbineAccessControlList;
import org.apache.fulcrum.security.model.turbine.TurbineModelManager;
import org.apache.fulcrum.security.model.turbine.entity.TurbineGroup;
import org.apache.fulcrum.security.model.turbine.entity.TurbinePermission;
import org.apache.fulcrum.security.model.turbine.entity.TurbineRole;
import org.apache.fulcrum.security.model.turbine.entity.TurbineUser;
import org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRole;
import org.apache.fulcrum.security.torque.HsqlDB;
import org.apache.fulcrum.testcontainer.BaseUnit5Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test user with attached object (user-role-goup relationship)
 * 
 * Compare difference to {@link ACLFactoryTest}.
 * 
 * @author <a href="mailto:gk@apache.org">Georg Kallidis</a>
 * @version $Id$
 */
public class TurbineACLFactoryTest extends BaseUnit5Test

{
    protected static HsqlDB hsqlDB = null;
    
    protected SecurityService securityService;
    
    @BeforeEach
    public void setup() throws Exception {
        
        hsqlDB = new HsqlDB( "src/test/fulcrum-turbine-default-schema.sql" );
        
        this.setRoleFileName( "src/test/TurbineTorqueRoleConfig.xml" );
        this.setConfigurationFileName( "src/test/TurbineDefaultWithPeersComponentConfig.xml" );
        
        securityService = (SecurityService) lookup( SecurityService.ROLE );
        
    }
    
    @Test
    public void testCreatingTurbineACLandModel() throws Exception
    {

        ACLFactory factory = (ACLFactory) lookup(ACLFactory.ROLE);
        
        TurbineModelManager modelManager  = (TurbineModelManager) lookup(TurbineModelManager.ROLE);
        assertTrue(modelManager.getGlobalGroupName().equals("global"));
        
        Group global = securityService.getGroupManager().getGroupInstance("global");
        global.setId(new Integer(1));
        securityService.getGroupManager().addGroup(global);
        
        TurbineUser user = securityService.getUserManager().getUserInstance("Bob");
        user.setId(new Integer(1));
        securityService.getUserManager().addUser(user, "mypw");
        
        TurbineGroup group = securityService.getGroupManager().getGroupInstance("group1");
        group.setId(new Integer(2));
        securityService.getGroupManager().addGroup(group);
        
        TurbineRole role = securityService.getRoleManager().getRoleInstance();
        role.setName("role1");
        role.setId(new Integer(1));
        securityService.getRoleManager().addRole( role );
        
        TurbinePermission permission = securityService.getPermissionManager().getPermissionInstance();
        permission.setName("permission1");
        permission.setId(new Integer(1)); 
        securityService.getPermissionManager().addPermission(permission);

        role.addPermission(permission);
        
        // need to save it as TurbineAccessControlListImpl refreshes role permission set! 
        securityService.<TurbineModelManager> getModelManager().grant(role, permission);
        
        TurbineUserGroupRole ugr = new TurbineUserGroupRole();
        ugr.setGroup(group);
        ugr.setRole(role);
        ugr.setUser(user);
        user.addUserGroupRole(ugr);
        
        securityService.<TurbineModelManager> getModelManager().grant( user, group, role );
        
        //securityService.getUserManager().saveUser(user);
        
        AccessControlList acl = factory.getAccessControlList(user);
        
        assertTrue(acl instanceof TurbineAccessControlList);
        
        TurbineAccessControlList tacl = (TurbineAccessControlList) acl;
        
        assertTrue(tacl.hasPermission(permission, group));
    }

}
