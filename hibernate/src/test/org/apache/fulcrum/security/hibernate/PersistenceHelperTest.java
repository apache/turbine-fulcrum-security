package org.apache.fulcrum.security.hibernate;
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

import net.sf.hibernate.avalon.HibernateService;
import net.sf.hibernate.avalon.HibernateServiceImpl;

import org.apache.fulcrum.security.SecurityService;

import org.apache.fulcrum.testcontainer.BaseUnitTest;
/**
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class PersistenceHelperTest extends BaseUnitTest
{

    public void tearDown()
    {

    }
    /**
    	   * Constructor for HibernatePermissionManagerTest.
    	   * @param arg0
    	   */
    public PersistenceHelperTest(String arg0)
    {
        super(arg0);
    }

    public void testPassingInExternalHibernateService() throws Exception
    {
        this.setRoleFileName("src/test/DynamicHibernateRoleConfig.xml");
        this.setConfigurationFileName("src/test/DynamicHibernateComponentConfig.xml");
        HibernateService hibernateService =
            (HibernateService) lookup(HibernateService.ROLE);
        HibernateHelper.exportSchema(hibernateService.getConfiguration());

        HibernateService hibernateService2 = new HibernateServiceImpl();

        assertNotSame(hibernateService,hibernateService2);

		SecurityService securityService = (SecurityService) lookup(SecurityService.ROLE);
		HibernateGroupManagerImpl groupManager = (HibernateGroupManagerImpl)securityService.getGroupManager();
        PersistenceHelper persistenceHelper = groupManager.getPersistenceHelper();
        assertTrue(persistenceHelper instanceof PersistenceHelperHibernateServiceImpl);
        PersistenceHelperHibernateServiceImpl persistenceHelperFromGroupManager = (PersistenceHelperHibernateServiceImpl)persistenceHelper;
		assertSame(hibernateService,persistenceHelperFromGroupManager.getHibernateService());
        persistenceHelperFromGroupManager.setHibernateService(hibernateService2);
		assertSame(hibernateService2,persistenceHelperFromGroupManager.getHibernateService());

		HibernateRoleManagerImpl roleManager = (HibernateRoleManagerImpl)securityService.getRoleManager();
        PersistenceHelperHibernateServiceImpl persistenceHelperFromRoleManager = (PersistenceHelperHibernateServiceImpl)roleManager.getPersistenceHelper();
		assertSame(hibernateService2,persistenceHelperFromRoleManager.getHibernateService());
		assertNotSame(hibernateService,persistenceHelperFromRoleManager.getHibernateService());
		persistenceHelperFromRoleManager.setHibernateService(hibernateService);
		assertSame(persistenceHelperFromRoleManager.getHibernateService(),persistenceHelperFromGroupManager.getHibernateService());

		roleManager = (HibernateRoleManagerImpl)securityService.getRoleManager();
        PersistenceHelperHibernateServiceImpl persistenceHelperFromRoleManager2 = (PersistenceHelperHibernateServiceImpl)roleManager.getPersistenceHelper();
        assertSame(persistenceHelperFromRoleManager2.getHibernateService(),persistenceHelperFromRoleManager.getHibernateService());
        assertSame(persistenceHelperFromRoleManager2.getHibernateService(),persistenceHelperFromGroupManager.getHibernateService());
    }


}
