package org.apache.fulcrum.security.nt.dynamic;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.UserManager;
import org.apache.fulcrum.security.acl.AccessControlList;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.basic.BasicAccessControlList;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.apache.fulcrum.testcontainer.BaseUnitTest;

/**
 * 
 * Test the NT implementation of the user manager. This test traps some
 * exceptions that can be thrown if there is NO nt dll.
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class NTBasicACLTest extends BaseUnitTest implements TestConstants
{
    private static Log log = LogFactory.getLog(NTBasicACLTest.class);
    private static final String DOMAIN = "IQUITOS";
    private static final String GUESTUSER = DOMAIN + "/" + "Guest";
    private SecurityService securityService;
    private UserManager userManager;
    private User user;

    @Override
    public void setUp() throws Exception
    {
        this.setRoleFileName("src/test/BasicNTRoleConfig.xml");
        this.setConfigurationFileName("src/test/BasicNTComponentConfig.xml");
        securityService = (SecurityService) lookup(SecurityService.ROLE);
        userManager = securityService.getUserManager();
    }

    @Override
    public void tearDown()
    {
        user = null;
        userManager = null;
        securityService = null;
    }

    /**
     * Constructor for NTBasicACLTest.
     * 
     * @param arg0
     */
    public NTBasicACLTest(String arg0)
    {
        super(arg0);
    }

    public void testLoadingUpGroupsForBasicModelACL() throws Exception
    {
        try
        {
            user = userManager.getUser(GUESTUSER, "");
            user.setPassword("");
            AccessControlList acl = userManager.getACL(user);
            assertTrue(acl instanceof BasicAccessControlList);
            BasicAccessControlList bacl = (BasicAccessControlList) acl;
            assertEquals(4, bacl.getGroups().size());
            assertTrue(bacl.hasGroup("Guests"));
            assertTrue(bacl.hasGroup("gUEsts"));
        }
        catch (DataBackendException dbe)
        {
            assertTrue(dbe.getMessage().indexOf(SCB_INVALID) > -1);
        }
        catch (UnknownEntityException re)
        {
            assertTrue(re.getMessage().indexOf("Unknown user") > -1);
        }
        catch (UnsatisfiedLinkError ule)
        {
            log.info("Unit test not being run due to missing NT DLL");
        }
    }
}
