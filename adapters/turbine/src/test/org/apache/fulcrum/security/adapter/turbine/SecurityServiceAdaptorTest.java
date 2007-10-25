package org.apache.fulcrum.security.adapter.turbine;
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
import org.apache.fulcrum.testcontainer.BaseUnitTest;
import org.apache.turbine.om.security.User;
import org.apache.turbine.services.ServiceManager;
import org.apache.turbine.services.TurbineServices;
import org.apache.turbine.services.security.SecurityService;
import org.apache.turbine.services.security.TurbineSecurity;
import org.apache.turbine.util.TurbineConfig;

/**
 * Test that the SecurityServiceAdapter works properly.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */

public class SecurityServiceAdaptorTest extends BaseUnitTest
{
    private static final String PREFIX =
        "services." + SecurityService.SERVICE_NAME + '.';
    public SecurityServiceAdaptorTest(String name) throws Exception
    {
        super(name);
    }

    public void setUp() throws Exception
    {
        super.setUp();
        TurbineConfig tc =
            new TurbineConfig(
                ".",
                "/src/test/AdapterTestTurbineResources.properties");
        tc.initialize();
    }
    public void testAccountExists() throws Exception
    {

        User user = new org.apache.turbine.om.security.TurbineUser();
        user.setAccessCounter(5);
        assertFalse(TurbineSecurity.getService().accountExists(user));

    }

	public void testCreateUser() throws Exception
	{

		User user = new org.apache.turbine.om.security.TurbineUser();
		user.setAccessCounter(5);
		user.setName("ringo");
		TurbineSecurity.getService().addUser(user,"fakepasswrod");
		assertTrue(TurbineSecurity.getService().accountExists(user));

	}

    public void tearDown()
    {
        ServiceManager serviceManager = TurbineServices.getInstance();
        serviceManager.shutdownService(SecurityService.SERVICE_NAME);
        serviceManager.shutdownServices();
    }


}
