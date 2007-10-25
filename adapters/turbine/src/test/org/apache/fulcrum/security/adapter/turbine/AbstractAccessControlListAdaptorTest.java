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
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.fulcrum.testcontainer.BaseUnitTest;
import org.apache.turbine.modules.actions.sessionvalidator.DefaultSessionValidator;
import org.apache.turbine.modules.actions.sessionvalidator.SessionValidator;
import org.apache.turbine.om.security.User;
import org.apache.turbine.services.ServiceManager;
import org.apache.turbine.services.TurbineServices;
import org.apache.turbine.services.avaloncomponent.AvalonComponentService;
import org.apache.turbine.services.rundata.RunDataService;
import org.apache.turbine.services.security.SecurityService;
import org.apache.turbine.util.RunData;
import org.apache.turbine.util.TurbineConfig;

import com.mockobjects.servlet.MockHttpServletResponse;
import com.mockobjects.servlet.MockHttpSession;
import com.mockobjects.servlet.MockServletConfig;

/**
 * Test that we can load up a fulcrum ACL in Turbine, without Turbine
 * knowing that anything has changed.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */

public abstract class AbstractAccessControlListAdaptorTest extends BaseUnitTest
{
    protected static final String PREFIX =
        "services." + SecurityService.SERVICE_NAME + '.';
	protected TurbineConfig tc;
	protected MockHttpSession session;
	protected AvalonComponentService acs;

	public abstract String getTRProps();
    public AbstractAccessControlListAdaptorTest(String name) throws Exception
    {
        super(name);
    }

	public void setUp() throws Exception
	 {
	    super.setUp();
		 tc =
			 new TurbineConfig(
				 ".",
				 "/src/test/"+getTRProps());
		 tc.initialize();
		 session = new MockHttpSession();
		 acs =
			 (AvalonComponentService) TurbineServices.getInstance().getService(
				 AvalonComponentService.SERVICE_NAME);
	 }

	protected User getUserFromRunData(HttpSession session) throws Exception
    {
        RunDataService rds =
            (RunDataService) TurbineServices.getInstance().getService(
                RunDataService.SERVICE_NAME);
        BetterMockHttpServletRequest request =
            new BetterMockHttpServletRequest();
        request.setupServerName("bob");
        request.setupGetProtocol("http");
        request.setupScheme("scheme");
        request.setupPathInfo("damn");
        request.setupGetServletPath("damn2");
        request.setupGetContextPath("wow");
        request.setupGetContentType("html/text");
        request.setupAddHeader("Content-type", "html/text");
        Vector v = new Vector();
        request.setupGetParameterNames(v.elements());
        request.setSession(session);
        HttpServletResponse response = new MockHttpServletResponse();
        ServletConfig config = new MockServletConfig();
        RunData rd = rds.getRunData(request, response, config);
        SessionValidator sessionValidator = new DefaultSessionValidator();
        sessionValidator.doPerform(rd);
        User turbineUser = rd.getUser();
        assertNotNull(turbineUser);
        return turbineUser;
    }


    public void tearDown()
    {
        super.tearDown();
        ServiceManager serviceManager = TurbineServices.getInstance();
        serviceManager.shutdownService(SecurityService.SERVICE_NAME);
        serviceManager.shutdownServices();
    }
}
