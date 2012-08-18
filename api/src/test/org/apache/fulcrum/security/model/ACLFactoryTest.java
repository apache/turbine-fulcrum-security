package org.apache.fulcrum.security.model;

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

import org.apache.fulcrum.security.acl.AccessControlList;
import org.apache.fulcrum.security.model.basic.BasicAccessControlList;
import org.apache.fulcrum.security.model.basic.entity.BasicGroup;
import org.apache.fulcrum.security.model.basic.entity.BasicUser;
import org.apache.fulcrum.security.model.basic.entity.impl.BasicGroupImpl;
import org.apache.fulcrum.security.model.basic.entity.impl.BasicUserImpl;
import org.apache.fulcrum.security.model.dynamic.DynamicAccessControlList;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicGroup;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicPermission;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicRole;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicUser;
import org.apache.fulcrum.security.model.dynamic.entity.impl.DynamicGroupImpl;
import org.apache.fulcrum.security.model.dynamic.entity.impl.DynamicPermissionImpl;
import org.apache.fulcrum.security.model.dynamic.entity.impl.DynamicRoleImpl;
import org.apache.fulcrum.security.model.dynamic.entity.impl.DynamicUserImpl;
import org.apache.fulcrum.testcontainer.BaseUnitTest;

/**
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class ACLFactoryTest extends BaseUnitTest
{

    public ACLFactoryTest(String arg0)
    {
        super(arg0);
    }

    public void testCreatingDynamicACL() throws Exception
    {
        this.setRoleFileName("src/test/DynamicACLRoleConfig.xml");
        this.setConfigurationFileName("src/test/ACLComponentConfig.xml");

        ACLFactory factory = (ACLFactory) lookup(ACLFactory.ROLE);
        DynamicUser user = new DynamicUserImpl();
        user.setName("bob");
        user.setId(new Integer(1));
        DynamicGroup group = new DynamicGroupImpl();
        group.setName("group1");
        group.setId(new Integer(1));
        DynamicRole role = new DynamicRoleImpl();
        role.setName("role1");
        role.setId(new Integer(1));
        DynamicPermission permission = new DynamicPermissionImpl();
        permission.setName("permission1");
        permission.setId(new Integer(1));
        role.addPermission(permission);
        group.addRole(role);
        user.addGroup(group);
        AccessControlList acl = factory.getAccessControlList(user);
        assertTrue(acl instanceof DynamicAccessControlList);
        DynamicAccessControlList dacl = (DynamicAccessControlList) acl;
        assertTrue(dacl.hasPermission(permission));

    }

    public void testCreatingBasicACL() throws Exception
    {
        this.setRoleFileName("src/test/BasicACLRoleConfig.xml");
        this.setConfigurationFileName("src/test/ACLComponentConfig.xml");

        ACLFactory factory = (ACLFactory) lookup(ACLFactory.ROLE);
        BasicUser user = new BasicUserImpl();
        user.setName("bob");
        user.setId(new Integer(1));
        BasicGroup group = new BasicGroupImpl();
        group.setName("group1");
        group.setId(new Integer(1));
        user.addGroup(group);
        AccessControlList acl = factory.getAccessControlList(user);
        assertTrue(acl instanceof BasicAccessControlList);
        BasicAccessControlList bacl = (BasicAccessControlList) acl;
        assertTrue(bacl.hasGroup(group));

    }

}
