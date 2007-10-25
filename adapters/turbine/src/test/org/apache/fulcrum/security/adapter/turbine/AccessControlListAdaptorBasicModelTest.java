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
import org.apache.fulcrum.security.BaseSecurityService;
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.model.basic.BasicModelManager;
import org.apache.turbine.om.security.User;
import org.apache.turbine.services.security.TurbineSecurity;
import org.apache.turbine.util.security.AccessControlList;

import com.mockobjects.servlet.MockHttpSession;

/**
 * Test that we can load up a fulcrum ACL in Turbine, without Turbine
 * knowing that anything has changed.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */

public class AccessControlListAdaptorBasicModelTest
    extends AbstractAccessControlListAdaptorTest
{
	public String getTRProps(){
		return "AdapterTestTurbineResourcesBasicModel.properties";
	}

    public AccessControlListAdaptorBasicModelTest(String name) throws Exception
    {
        super(name);
    }

    public void testHasRoleBasic() throws Exception
    {

        BaseSecurityService securityService =
            (BaseSecurityService) acs.lookup(BaseSecurityService.ROLE);
        Group fulcrumGroup =
            securityService.getGroupManager().getGroupInstance(
                "TEST_GROUP_BASIC_MODEL");

        org.apache.fulcrum.security.entity.User fulcrumUser =
            securityService.getUserManager().getUserInstance("MikeFitz");

        securityService.getGroupManager().addGroup(fulcrumGroup);

        fulcrumUser =
            securityService.getUserManager().addUser(fulcrumUser, "relana");
        System.out.println(
            securityService.getModelManager().getClass().getName());
        BasicModelManager modelManager =
            (BasicModelManager) securityService.getModelManager();

        modelManager.grant(fulcrumUser, fulcrumGroup);

        User turbineUser = TurbineSecurity.getService().getUser("MikeFitz");

        MockHttpSession session = new MockHttpSession();
        session.setupGetAttribute(User.SESSION_KEY, turbineUser);
        turbineUser = getUserFromRunData(session);
        assertNotNull(turbineUser);
        assertFalse(TurbineSecurity.getService().isAnonymousUser(turbineUser));
        AccessControlList acl =
            TurbineSecurity.getService().getACL(turbineUser);
        assertTrue(acl.hasRole("TEST_GROUP_BASIC_MODEL"));

    }

}
