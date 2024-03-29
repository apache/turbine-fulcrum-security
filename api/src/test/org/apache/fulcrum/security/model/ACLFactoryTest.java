package org.apache.fulcrum.security.model;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

import org.apache.fulcrum.security.ModelManager;
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
import org.apache.fulcrum.security.model.turbine.TurbineAccessControlList;
import org.apache.fulcrum.security.model.turbine.TurbineModelManager;
import org.apache.fulcrum.security.model.turbine.entity.TurbineGroup;
import org.apache.fulcrum.security.model.turbine.entity.TurbinePermission;
import org.apache.fulcrum.security.model.turbine.entity.TurbineRole;
import org.apache.fulcrum.security.model.turbine.entity.TurbineUser;
import org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRole;
import org.apache.fulcrum.security.model.turbine.entity.impl.TurbineGroupImpl;
import org.apache.fulcrum.security.model.turbine.entity.impl.TurbinePermissionImpl;
import org.apache.fulcrum.security.model.turbine.entity.impl.TurbineRoleImpl;
import org.apache.fulcrum.security.model.turbine.entity.impl.TurbineUserImpl;
import org.apache.fulcrum.security.util.RoleSet;
import org.apache.fulcrum.testcontainer.BaseUnit5Test;
import org.junit.jupiter.api.Test;

/**
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class ACLFactoryTest extends BaseUnit5Test
{

    public ACLFactoryTest()
    {
    }

    @Test
    public void testCreatingTurbineACLandModel() throws Exception
    {
        this.setRoleFileName("src/test/TurbineACLRoleModelConfig.xml");
        this.setConfigurationFileName("src/test/ACLModelComponentConfig.xml");

        ACLFactory factory = (ACLFactory) lookup(ACLFactory.ROLE);
        
        TurbineModelManager modelManager  = (TurbineModelManager) lookup(TurbineModelManager.ROLE);
        assertTrue(modelManager.getGlobalGroupName().equals("dumb"));
        
        //factory.ge
        TurbineUser user = new TurbineUserImpl();
        user.setName("bob");
        user.setId( Integer.valueOf( 1 ));
        TurbineGroup group = new TurbineGroupImpl();
        group.setName("group1");
        group.setId( Integer.valueOf(1));
        TurbineRole role = new TurbineRoleImpl();
        role.setName("role1");
        role.setId( Integer.valueOf(1));
        TurbinePermission permission = new TurbinePermissionImpl();
        permission.setName("permission1");
        permission.setId( Integer.valueOf(1));
        role.addPermission(permission);
        TurbineUserGroupRole ugr = new TurbineUserGroupRole();
        ugr.setGroup(group);
        ugr.setRole(role);
        ugr.setUser(user);
        user.addUserGroupRole(ugr);
        AccessControlList acl = factory.getAccessControlList(user);
        assertTrue(acl instanceof TurbineAccessControlList);
        TurbineAccessControlList tacl = (TurbineAccessControlList) acl;
        assertTrue(tacl.hasPermission(permission, group));
        
        RoleSet roleSet = tacl.getRoles( group );
        assertTrue(roleSet.contains( role ), "expect group "+ group +  " has role "+ role);
        
        assertNull( tacl.getRoles(), "expect no role in global group");
//        assertTrue(tacl.getAllGroups().length > 0, 
//                "expect length for all group set:" + tacl.getAllGroups());
        assertTrue(tacl.getGroupSet().size() > 0, 
                "expect length for all group set:" + tacl.getAllGroups());
    }

    @Test
    public void testCreatingDynamicACL() throws Exception
    {
        this.setRoleFileName("src/test/DynamicACLRoleConfig.xml");
        this.setConfigurationFileName("src/test/ACLModelComponentConfig.xml");
        
        ModelManager modelManager  = (ModelManager) lookup(ModelManager.ROLE);
        assertTrue(modelManager != null);
        
        ACLFactory factory = (ACLFactory) lookup(ACLFactory.ROLE);
        
        DynamicUser user = new DynamicUserImpl();
        user.setName("bob");
        user.setId( Integer.valueOf(1));
        DynamicGroup group = new DynamicGroupImpl();
        group.setName("group1");
        group.setId(  Integer.valueOf(1));
        DynamicRole role = new DynamicRoleImpl();
        role.setName("role1");
        role.setId( Integer.valueOf(1));
        DynamicPermission permission = new DynamicPermissionImpl();
        permission.setName("permission1");
        permission.setId(Integer.valueOf(1));
        role.addPermission(permission);
        group.addRole(role);
        user.addGroup(group);
        AccessControlList acl = factory.getAccessControlList(user);
        assertTrue(acl instanceof DynamicAccessControlList);
        DynamicAccessControlList dacl = (DynamicAccessControlList) acl;
        assertTrue(dacl.hasPermission(permission));
    }

    @Test
    public void testCreatingBasicACL() throws Exception
    {
        this.setRoleFileName("src/test/BasicACLRoleConfig.xml");
        this.setConfigurationFileName("src/test/ACLModelComponentConfig.xml");

        ACLFactory factory = (ACLFactory) lookup(ACLFactory.ROLE);
        BasicUser user = new BasicUserImpl();
        user.setName("bob");
        user.setId( Integer.valueOf(1));
        BasicGroup group = new BasicGroupImpl();
        group.setName("group1");
        group.setId( Integer.valueOf(1));
        user.addGroup(group);
        AccessControlList acl = factory.getAccessControlList(user);
        assertTrue(acl instanceof BasicAccessControlList);
        BasicAccessControlList bacl = (BasicAccessControlList) acl;
        assertTrue(bacl.hasGroup(group));
    }
}
