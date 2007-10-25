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

import org.apache.fulcrum.security.RoleManager;
import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.model.test.AbstractRoleManagerTest;
/**
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class HibernateRoleManagerTest extends AbstractRoleManagerTest
{
    public void setUp()
    {
        try
        {
            this.setRoleFileName("src/test/DynamicHibernateRoleConfig.xml");
            this.setConfigurationFileName("src/test/DynamicHibernateComponentConfig.xml");
            HibernateService hibernateService = (HibernateService) lookup(HibernateService.ROLE);
            HibernateHelper.exportSchema(hibernateService.getConfiguration());
            securityService = (SecurityService) lookup(SecurityService.ROLE);
            roleManager = (RoleManager) securityService.getRoleManager();
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
    }
    public void tearDown()
    {
        try
        {
           // ((BaseHibernateManager) roleManager).getHibernateSession().close();
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
        role = null;
        roleManager = null;
        securityService = null;
    }
    /**
    	   * Constructor for HibernatePermissionManagerTest.
    	   * @param arg0
    	   */
    public HibernateRoleManagerTest(String arg0)
    {
        super(arg0);
    }
}
