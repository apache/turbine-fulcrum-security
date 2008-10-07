package org.apache.fulcrum.security.ldap;
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
import org.apache.avalon.framework.service.ServiceException;
import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.ldap.LDAPUserManagerImpl;
import org.apache.fulcrum.security.model.basic.BasicModelManager;
import org.apache.fulcrum.security.model.dynamic.DynamicModelManager;
import org.apache.fulcrum.security.model.turbine.TurbineModelManager;
import org.apache.fulcrum.testcontainer.BaseUnitTest;

/**
 * @author <a href="mailto:marco@intermeta.de">Marco Kn&uuml;ttel</a>
 * @version $Id:StartingSecurityServicesTest.java 535465 2007-05-05 06:58:06Z tv $
 */

public class StartingSecurityServicesTest extends BaseUnitTest
{
    private SecurityService securityService = null;
    public StartingSecurityServicesTest(String name)
    {
        super(name);
    }
    public void testStartingDynamicModel() throws Exception
    {
        this.setRoleFileName("src/test/DynamicLDAPRoleConfig.xml");
        this.setConfigurationFileName("src/test/DynamicLDAPComponentConfig.xml");
        securityService = (SecurityService) lookup(SecurityService.ROLE);
        assertTrue(securityService.getClass().getName(),
            securityService.getUserManager() instanceof LDAPUserManagerImpl);
        assertTrue(
            securityService.getModelManager() instanceof DynamicModelManager);
    }

    public void testStartingTurbineModel() throws Exception
    {
        this.setRoleFileName("src/test/TurbineLDAPRoleConfig.xml");
        this.setConfigurationFileName("src/test/TurbineLDAPComponentConfig.xml");

        securityService = (SecurityService) lookup(SecurityService.ROLE);
        assertTrue(
            securityService.getUserManager()
                instanceof org.apache.fulcrum.security.ldap.turbine
                    .LDAPTurbineUserManagerImpl);
        assertTrue(
            securityService.getModelManager() instanceof TurbineModelManager);
    }

    public void testStartingBasicModel() throws Exception
    {
        this.setRoleFileName("src/test/BasicLDAPRoleConfig.xml");
        this.setConfigurationFileName("src/test/BasicLDAPComponentConfig.xml");
        securityService = (SecurityService) lookup(SecurityService.ROLE);
        assertTrue(
            securityService.getUserManager() instanceof LDAPUserManagerImpl);
        assertTrue(
            securityService.getModelManager() instanceof BasicModelManager);
    }

    public void testLazyLoadingOfServices() throws Exception
    {
        this.setRoleFileName("src/test/LazyLoadRoleConfig.xml");
        this.setConfigurationFileName("src/test/LazyLoadComponentConfig.xml");
        securityService = (SecurityService) lookup(SecurityService.ROLE);
        assertTrue(
            securityService.getUserManager() instanceof LDAPUserManagerImpl);
        try
        {
            securityService.getModelManager();
            fail("Should have throw runtime error");
        }
        catch (RuntimeException re)
        {
            assertTrue(
                "Type was " + re.getCause().getClass().getName(),
                re.getCause() instanceof ServiceException);
        }
    }
}
