package org.apache.fulcrum.security.model.basic.test;

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

import static org.junit.Assert.*;

import org.apache.fulcrum.security.GroupManager;
import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.UserManager;
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.basic.BasicModelManager;
import org.apache.fulcrum.security.model.basic.entity.BasicGroup;
import org.apache.fulcrum.security.model.basic.entity.BasicUser;
import org.apache.fulcrum.testcontainer.BaseUnit4Test;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Eric Pugh
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractModelManagerTest extends BaseUnit4Test
{
    protected Role role;
    protected BasicModelManager modelManager;
    protected GroupManager groupManager;
    protected UserManager userManager;
    protected SecurityService securityService;

    @Before
    public void setUp() throws Exception
    {
        userManager = securityService.getUserManager();
        groupManager = securityService.getGroupManager();
        modelManager = (BasicModelManager) securityService.getModelManager();
    }

    @Test
    public void testRevokeAllUser() throws Exception
    {
        Group group = securityService.getGroupManager().getGroupInstance();
        group.setName("TEST_REVOKEALL");
        securityService.getGroupManager().addGroup(group);
        Group group2 = securityService.getGroupManager().getGroupInstance();
        group2.setName("TEST_REVOKEALL2");
        securityService.getGroupManager().addGroup(group2);
        User user = userManager.getUserInstance("Clint2");
        userManager.addUser(user, "clint");
        modelManager.grant(user, group);
        modelManager.grant(user, group2);

        modelManager.revokeAll(user);
        assertEquals(0, ((BasicUser) user).getGroups().size());
        group = securityService.getGroupManager().getGroupByName("TEST_REVOKEALL");
        group2 = securityService.getGroupManager().getGroupByName("TEST_REVOKEALL2");
        assertFalse(((BasicGroup) group).getUsersAsSet().contains(user));
        assertFalse(((BasicGroup) group2).getUsers().contains(user));
    }
    @Test
    public void testGrantUserGroup() throws Exception
    {
        Group group = securityService.getGroupManager().getGroupInstance();
        group.setName("TEST_GROUP");
        securityService.getGroupManager().addGroup(group);
        User user = userManager.getUserInstance("Clint");
        userManager.addUser(user, "clint");
        modelManager.grant(user, group);
        assertTrue(((BasicUser) user).getGroups().contains(group));
        assertTrue(((BasicGroup) group).getUsers().contains(user));
    }
    @Test
    public void testRevokeUserGroup() throws Exception
    {
        Group group = securityService.getGroupManager().getGroupInstance();
        group.setName("TEST_REVOKE");
        securityService.getGroupManager().addGroup(group);
        User user = userManager.getUserInstance("Lima");
        userManager.addUser(user, "pet");
        modelManager.revoke(user, group);
        assertFalse(((BasicUser) user).getGroups().contains(group));
        assertFalse(((BasicGroup) group).getUsers().contains(user));
        user = userManager.getUser("Lima");
        assertFalse(((BasicUser) user).getGroups().contains(group));
    }
}
