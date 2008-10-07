package org.apache.fulcrum.security.ldap.turbine;
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

import java.util.Iterator;

import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.entity.User;
import org
    .apache
    .fulcrum
    .security
    .model
    .turbine
    .test
    .AbstractTurbineModelManagerTest;
import org.apache.fulcrum.security.util.UserSet;

/**
 * @author Eric Pugh
 *
 * Test the memory implementation of the turbine model..
 */
public class LDAPTurbineModelManagerTest
    extends AbstractTurbineModelManagerTest
{

    public void setUp() throws Exception
    {

        this.setRoleFileName("src/test/TurbineLDAPRoleConfig.xml");
        this.setConfigurationFileName("src/test/TurbineLDAPComponentConfig.xml");
        securityService = (SecurityService) lookup(SecurityService.ROLE);
        super.setUp();

    }
    public void tearDown()
    {
        try
        {
            UserSet users = userManager.getAllUsers();
            
            for (Iterator i = users.iterator(); i.hasNext();)
            {
                User user = (User)i.next();
                userManager.removeUser(user);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.toString());
        }

        super.tearDown();
		modelManager = null;
        securityService = null;
    }
    /**
    	* Constructor for LDAPPermissionManagerTest.
    	* @param arg0
    	*/
    public LDAPTurbineModelManagerTest(String arg0)
    {
        super(arg0);
    }
}
