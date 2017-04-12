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

import static org.junit.Assert.assertTrue;

import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.model.dynamic.DynamicModelManager;
import org.apache.fulcrum.security.torque.dynamic.TorqueDynamicGroupManagerImpl;
import org.apache.fulcrum.security.torque.dynamic.TorqueDynamicPermissionManagerImpl;
import org.apache.fulcrum.security.torque.dynamic.TorqueDynamicRoleManagerImpl;
import org.apache.fulcrum.security.torque.dynamic.TorqueDynamicUserManagerImpl;
import org.apache.fulcrum.testcontainer.BaseUnit4Test;
import org.junit.Test;

/**
 * @author <a href="mailto:marco@intermeta.de">Marco Kn&uuml;ttel</a>
 * @version $Id: StartingSecurityServicesTest.java 223140 2004-11-01 13:29:25Z epugh $
 */

public class StartingSecurityServicesTest extends BaseUnit4Test
{
    private SecurityService securityService = null;

    @Test
    public void testStartingTorqueSecurity() throws Exception
    {
        this.setRoleFileName("src/test/DynamicTorqueRoleConfig.xml");
        this.setConfigurationFileName("src/test/DynamicTorqueComponentConfig.xml");
        securityService = (SecurityService) lookup(SecurityService.ROLE);
        assertTrue(securityService.getUserManager() instanceof TorqueDynamicUserManagerImpl);
        assertTrue(securityService.getRoleManager() instanceof TorqueDynamicRoleManagerImpl);
        assertTrue(
            securityService.getPermissionManager() instanceof TorqueDynamicPermissionManagerImpl);
        assertTrue(securityService.getGroupManager() instanceof TorqueDynamicGroupManagerImpl);
        assertTrue(securityService.getModelManager() instanceof DynamicModelManager);
    }



}
