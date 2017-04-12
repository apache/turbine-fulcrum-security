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

import static org.junit.Assert.assertTrue;

import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.testcontainer.BaseUnit4Test;
import org.hibernate.Session;
import org.junit.Test;

/**
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id: PersistenceHelperDefaultImplTest.java 1374014 2012-08-16
 *          19:47:27Z tv $
 */
public class PersistenceHelperDefaultImplTest extends BaseUnit4Test
{

    @SuppressWarnings("unused")
    @Test
    public void testPassingInExternalHibernateService() throws Exception
    {
        this.setRoleFileName("src/test/PersistenceHelperDefaultImplRoleConfig.xml");
        this.setConfigurationFileName("src/test/PersistenceHelperDefaultImplComponentConfig.xml");

        SecurityService securityService = (SecurityService) lookup(SecurityService.ROLE);
        HibernateGroupManagerImpl groupManager = (HibernateGroupManagerImpl) securityService.getGroupManager();
        PersistenceHelper persistenceHelper = groupManager.getPersistenceHelper();
        assertTrue(persistenceHelper instanceof PersistenceHelperDefaultImpl);
        PersistenceHelperDefaultImpl persistenceHelperFromGroupManager = (PersistenceHelperDefaultImpl) persistenceHelper;
        Session s = persistenceHelper.retrieveSession();
    }
}
