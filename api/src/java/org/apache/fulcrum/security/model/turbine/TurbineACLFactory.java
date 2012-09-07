package org.apache.fulcrum.security.model.turbine;

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
import java.util.HashMap;
import java.util.Map;

import org.apache.fulcrum.security.GroupManager;
import org.apache.fulcrum.security.acl.AccessControlList;
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.ACLFactory;
import org.apache.fulcrum.security.spi.AbstractManager;
import org.apache.fulcrum.security.util.PermissionSet;
import org.apache.fulcrum.security.util.RoleSet;
import org.apache.fulcrum.security.util.UnknownEntityException;

/**
 *
 * This factory creates instance of the TurbineAccessControlList
 *
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id: DynamicACLFactory.java 1374616 2012-08-18 17:26:07Z tv $
 */
public class TurbineACLFactory extends AbstractManager implements ACLFactory
{
    /**
     * @see org.apache.fulcrum.security.model.ACLFactory#getAccessControlList(org.apache.fulcrum.security.entity.User)
     */
    public <T extends AccessControlList> T getAccessControlList(User user)
    {
        Map<Group, RoleSet> roleSets = new HashMap<Group, RoleSet>();
        Map<Role, PermissionSet> permissionSets = new HashMap<Role, PermissionSet>();

        try
        {
            @SuppressWarnings("unchecked")
			T aclInstance = (T) getAclInstance(roleSets, permissionSets);
			return aclInstance;
        }
        catch (UnknownEntityException uue)
        {
            throw new RuntimeException(uue.getMessage(), uue);
        }
    }

    /**
     * Construct a new ACL object.
     *
     * This constructs a new ACL object from the configured class and
     * initializes it with the supplied roles and permissions.
     *
     * @param roles
     *            The roles that this ACL should contain
     * @param permissions
     *            The permissions for this ACL
     *
     * @return an object implementing ACL interface.
     * @throws UnknownEntityException
     *             if the object could not be instantiated.
     */
    private TurbineAccessControlList getAclInstance(Map<? extends Group, ? extends RoleSet> roles,
            Map<? extends Role, ? extends PermissionSet> permissions) throws UnknownEntityException
    {
    	TurbineAccessControlList accessControlList;
        try
        {
        	GroupManager groupManager = (GroupManager) resolve(GroupManager.ROLE);
            accessControlList = new TurbineAccessControlListImpl(roles, permissions, groupManager);
        }
        catch (Exception e)
        {
            throw new UnknownEntityException("Failed to instantiate an ACL implementation object", e);
        }
        return accessControlList;
    }
}
