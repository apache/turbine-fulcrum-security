package org.apache.fulcrum.security.adapter.osuser;
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
import java.util.Collection;

import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.model.dynamic.DynamicAccessControlList;
import org.apache.fulcrum.security.model.dynamic.DynamicModelManager;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicUser;

import org.apache.fulcrum.testcontainer.BaseUnitTest;

import com.opensymphony.user.User;
import com.opensymphony.user.UserManager;
import com.opensymphony.user.provider.AccessProvider;
import com.opensymphony.user.provider.CredentialsProvider;

/**
 * Test that we can load up OSUser backed by Fulcrum Security. The fulcrum Security service is just
 * running in memory.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */

public class OSUserAdapterTest extends BaseUnitTest
{

    protected UserManager osUserManager;

    protected SecurityService securityService;
    private org.apache.fulcrum.security.entity.User fulcrumUser;
    private User osUser;

    public OSUserAdapterTest(String name) throws Exception
    {
        super(name);
    }
    public void setUp()
    {
        try
        {
            this.setRoleFileName("src/test/DynamicMemoryRoleConfig.xml");
            this.setConfigurationFileName("src/test/DynamicMemoryComponentConfig.xml");
            securityService = (SecurityService) lookup(SecurityService.ROLE);
            BaseFulcrumProvider.setSecurityService(securityService);

            osUserManager = new UserManager("osuser.xml");
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
    }
    public void tearDown()
    {

        osUserManager = null;

        securityService = null;
    }
    public void testUsingAvalonComponents() throws Exception
    {
        Group fulcrumGroup = securityService.getGroupManager().getGroupInstance("TEST_REVOKEALL");
        securityService.getGroupManager().addGroup(fulcrumGroup);
        Group fulcrumGroup2 = securityService.getGroupManager().getGroupInstance("TEST_REVOKEALL2");
        securityService.getGroupManager().addGroup(fulcrumGroup2);
        Role fulcrumRole = securityService.getRoleManager().getRoleInstance("role1");
        Role fulcrumRole2 = securityService.getRoleManager().getRoleInstance("role2");
        securityService.getRoleManager().addRole(fulcrumRole);
        securityService.getRoleManager().addRole(fulcrumRole2);
        Permission fulcrumPermission =
            securityService.getPermissionManager().getPermissionInstance("perm1");
        Permission fulcrumPermission2 =
            securityService.getPermissionManager().getPermissionInstance("perm2");
        Permission fulcrumPermission3 =
            securityService.getPermissionManager().getPermissionInstance("perm3");
        securityService.getPermissionManager().addPermission(fulcrumPermission);
        securityService.getPermissionManager().addPermission(fulcrumPermission2);
        securityService.getPermissionManager().addPermission(fulcrumPermission3);
        DynamicModelManager modelManager = (DynamicModelManager) securityService.getModelManager();
        modelManager.grant(fulcrumRole, fulcrumPermission);
        modelManager.grant(fulcrumRole2, fulcrumPermission2);
        modelManager.grant(fulcrumRole2, fulcrumPermission3);
        modelManager.grant(fulcrumGroup, fulcrumRole);
        modelManager.grant(fulcrumGroup, fulcrumRole2);
        modelManager.grant(fulcrumGroup2, fulcrumRole2);
        fulcrumUser = securityService.getUserManager().getUserInstance("Jeannie");
        securityService.getUserManager().addUser(fulcrumUser, "wyatt");
        modelManager.grant(fulcrumUser, fulcrumGroup);
        modelManager.grant(fulcrumUser, fulcrumGroup2);
        assertEquals(2, ((DynamicUser) fulcrumUser).getGroups().size());

        Collection accessProviders = osUserManager.getAccessProviders();
        assertEquals(1, accessProviders.size());
        AccessProvider accessProvider = (AccessProvider) accessProviders.toArray()[0];
        assertTrue(accessProvider.handles("Jeannie"));
        assertTrue(securityService.getUserManager().checkExists("Jeannie"));

        assertEquals(
            "Both should not handle user Bob",
            accessProvider.handles("Bob"),
            securityService.getUserManager().checkExists("Bob"));

        fulcrumUser = securityService.getUserManager().getUser("Jeannie");
        DynamicAccessControlList acl = (DynamicAccessControlList)securityService.getUserManager().getACL(fulcrumUser);
        assertEquals(
            "Both should have role1",
            acl.hasRole("role1"),
            accessProvider.inGroup("Jeannie", "role1"));

        System.out.println("hi");

        assertEquals(
            "Neither should have role3",
            acl.hasRole("role3"),
            accessProvider.inGroup("Jeannie", "role3"));

        Collection credentialProviders = osUserManager.getCredentialsProviders();
        assertEquals(1, credentialProviders.size());
        CredentialsProvider credentialProvider =
            (CredentialsProvider) credentialProviders.toArray()[0];

        assertTrue(credentialProvider.authenticate("Jeannie", "wyatt"));

    }

}
