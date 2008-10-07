package org.apache.fulcrum.security.ldap.basic;
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
import org.apache.fulcrum.security.model.basic.test.AbstractModelManagerTest;
import org.apache.fulcrum.security.util.UserSet;

/**
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id:LDAPBasicModelManagerTest.java 535465 2007-05-05 06:58:06Z tv $
 */
public class LDAPBasicModelManagerTest extends AbstractModelManagerTest
{
	/**
     * @param arg0
     */
    public LDAPBasicModelManagerTest(String arg0)
    {
        super(arg0);
    }
    public void setUp()
	 {
		 try
		 {
             this.setRoleFileName("src/test/BasicLDAPRoleConfig.xml");
                this.setConfigurationFileName("src/test/BasicLDAPComponentConfig.xml");
			 securityService = (SecurityService) lookup(SecurityService.ROLE);
			 super.setUp();

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
	     groupManager=null;
		 securityService = null;
	 }
}
