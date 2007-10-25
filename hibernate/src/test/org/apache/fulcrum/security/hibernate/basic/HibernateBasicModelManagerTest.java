package org.apache.fulcrum.security.hibernate.basic;
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

import net.sf.hibernate.Transaction;
import net.sf.hibernate.avalon.HibernateService;

import org.apache.fulcrum.security.SecurityService;

import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.hibernate.HibernateHelper;
import org.apache.fulcrum.security.hibernate.HibernateUserManagerImpl;
import org.apache.fulcrum.security.model.basic.entity.BasicUser;
import org.apache.fulcrum.security.model.basic.test.AbstractModelManagerTest;
/**
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class HibernateBasicModelManagerTest extends AbstractModelManagerTest
{
    public void setUp() throws Exception
    {

        this.setRoleFileName("src/test/BasicHibernateRoleConfig.xml");
        this.setConfigurationFileName("src/test/BasicHibernateComponentConfig.xml");
        HibernateService hibernateService =
            (HibernateService) lookup(HibernateService.ROLE);
        HibernateHelper.exportSchema(hibernateService.getConfiguration());
        securityService = (SecurityService) lookup(SecurityService.ROLE);
        super.setUp();

    }

	public void testRevokeAllUser() throws Exception{
	    super.testRevokeAllUser();
	    ((HibernateUserManagerImpl)userManager).getPersistenceHelper().retrieveSession().close();
	    Transaction tx = ((HibernateUserManagerImpl)userManager).getPersistenceHelper().retrieveSession().beginTransaction();
		User user = userManager.getUserInstance("Clint2");
		assertEquals(0, ((BasicUser) user).getGroups().size());
		tx.commit();

	}

    public void tearDown()
    {
        try
        {
            //((BaseHibernateManager) permissionManager).getHibernateSession().close();
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }

        securityService = null;
    }
    /**
    * Constructor for HibernatePermissionManagerTest.
    *
    * @param arg0
    */
    public HibernateBasicModelManagerTest(String arg0)
    {
        super(arg0);
    }
}
