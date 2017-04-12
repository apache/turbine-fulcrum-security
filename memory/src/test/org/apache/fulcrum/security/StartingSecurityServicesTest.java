package org.apache.fulcrum.security;

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
import static org.junit.Assert.fail;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.fulcrum.security.memory.MemoryGroupManagerImpl;
import org.apache.fulcrum.security.memory.MemoryPermissionManagerImpl;
import org.apache.fulcrum.security.memory.MemoryRoleManagerImpl;
import org.apache.fulcrum.security.memory.MemoryUserManagerImpl;
import org.apache.fulcrum.security.model.basic.BasicModelManager;
import org.apache.fulcrum.security.model.dynamic.DynamicModelManager;
import org.apache.fulcrum.security.model.turbine.TurbineModelManager;
import org.apache.fulcrum.testcontainer.BaseUnit4Test;
import org.junit.Test;

/**
 * @author <a href="mailto:marco@intermeta.de">Marco Kn&uuml;ttel</a>
 * @version $Id: StartingSecurityServicesTest.java 535465 2007-05-05 06:58:06Z
 *          tv $
 */

public class StartingSecurityServicesTest extends BaseUnit4Test
{
    private SecurityService securityService = null;



    @Test
    public void testStartingDynamicModel() throws Exception
    {
        this.setRoleFileName("src/test/DynamicMemoryRoleConfig.xml");
        this.setConfigurationFileName("src/test/DynamicMemoryComponentConfig.xml");
        securityService = (SecurityService) lookup(SecurityService.ROLE);
        assertTrue(securityService.getClass().getName(), securityService.getUserManager() instanceof MemoryUserManagerImpl);
        assertTrue(securityService.getRoleManager() instanceof MemoryRoleManagerImpl);
        assertTrue(securityService.getPermissionManager() instanceof MemoryPermissionManagerImpl);
        assertTrue(securityService.getGroupManager() instanceof MemoryGroupManagerImpl);
        assertTrue(securityService.getModelManager() instanceof DynamicModelManager);
    }
    @Test
    public void testStartingTurbineModel() throws Exception
    {
        this.setRoleFileName("src/test/TurbineMemoryRoleConfig.xml");
        this.setConfigurationFileName("src/test/TurbineMemoryComponentConfig.xml");

        securityService = (SecurityService) lookup(SecurityService.ROLE);
        assertTrue(securityService.getUserManager() instanceof org.apache.fulcrum.security.memory.turbine.MemoryTurbineUserManagerImpl);
        assertTrue(securityService.getRoleManager() instanceof MemoryRoleManagerImpl);
        assertTrue(securityService.getPermissionManager() instanceof MemoryPermissionManagerImpl);
        assertTrue(securityService.getGroupManager() instanceof MemoryGroupManagerImpl);
        assertTrue(securityService.getModelManager() instanceof org.apache.fulcrum.security.memory.turbine.MemoryTurbineModelManagerImpl);
        assertTrue(securityService.getModelManager() instanceof TurbineModelManager);
    }
    @Test
    public void testStartingBasicModel() throws Exception
    {

        this.setRoleFileName("src/test/BasicMemoryRoleConfig.xml");
        this.setConfigurationFileName("src/test/BasicMemoryComponentConfig.xml");
        securityService = (SecurityService) lookup(SecurityService.ROLE);
        assertTrue(securityService.getUserManager() instanceof MemoryUserManagerImpl);
        assertTrue(securityService.getGroupManager() instanceof MemoryGroupManagerImpl);
        assertTrue(securityService.getModelManager() instanceof BasicModelManager);
    }
    @Test
    public void testLazyLoadingOfServices() throws Exception
    {
        this.setRoleFileName("src/test/LazyLoadRoleConfig.xml");
        this.setConfigurationFileName("src/test/LazyLoadComponentConfig.xml");
        securityService = (SecurityService) lookup(SecurityService.ROLE);
        assertTrue(securityService.getUserManager() instanceof MemoryUserManagerImpl);
        try
        {
            securityService.getModelManager();
            fail("Should have throw runtime error");
        }
        catch (RuntimeException re)
        {
            assertTrue("Type was " + re.getCause().getClass().getName(), re.getCause() instanceof ServiceException);
        }
    }

}
