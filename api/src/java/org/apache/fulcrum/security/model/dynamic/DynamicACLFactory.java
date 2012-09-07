package org.apache.fulcrum.security.model.dynamic;

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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.fulcrum.security.acl.AccessControlList;
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.ACLFactory;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicGroup;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicRole;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicUser;
import org.apache.fulcrum.security.spi.AbstractManager;
import org.apache.fulcrum.security.util.PermissionSet;
import org.apache.fulcrum.security.util.RoleSet;
import org.apache.fulcrum.security.util.UnknownEntityException;

/**
 *
 * This factory creates instance of the DynamicAccessControlList
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh </a>
 * @author <a href="mailto:ben@gidley.co.uk">Ben Gidley </a>
 * @version $Id$
 */
public class DynamicACLFactory extends AbstractManager implements ACLFactory
{
    /**
     * @see org.apache.fulcrum.security.model.ACLFactory#getAccessControlList(org.apache.fulcrum.security.entity.User)
     */
    public <T extends AccessControlList> T getAccessControlList(User user)
    {
        Map<Group, RoleSet> roleSets = new HashMap<Group, RoleSet>();
        Map<Role, PermissionSet> permissionSets = new HashMap<Role, PermissionSet>();

        Set<DynamicUser> users = new HashSet<DynamicUser>();

        // add the root user
        users.add((DynamicUser) user);
        addDelegators((DynamicUser) user, users);

        for (DynamicUser aUser : users)
        {
            addRolesAndPermissions(aUser, roleSets, permissionSets);
        }

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
    private DynamicAccessControlList getAclInstance(Map<? extends Group, ? extends RoleSet> roles,
            Map<? extends Role, ? extends PermissionSet> permissions) throws UnknownEntityException
    {
    	DynamicAccessControlList accessControlList;
        try
        {
            accessControlList = new DynamicAccessControlListImpl(roles, permissions);
        }
        catch (Exception e)
        {
            throw new UnknownEntityException("Failed to instantiate an ACL implementation object", e);
        }
        return accessControlList;
    }

    /**
     * Add delegators to the user list
     *
     * @param user
     *            the user to add to
     * @param users
     *            the set of delegators
     */
    public <T extends DynamicUser> void addDelegators(DynamicUser user, Set<T> users)
    {
        for (User u : user.getDelegators())
        {
            @SuppressWarnings("unchecked")
            T delegatorUser = (T) u;

            if (users.add(delegatorUser))
            {
                // Only come here if user NOT in users
                addDelegators(delegatorUser, users);
            }
        }
    }

    /**
     * Adds the passed users roles and permissions to the sets As maps overwrite
     * duplicates we just put it in an let it overwrite it is probably quicker
     * than checking for duplicates
     *
     * @param user
     * @param roleSets
     * @param permissionSets
     */
    private void addRolesAndPermissions(User user, Map<Group, RoleSet> roleSets, Map<Role, PermissionSet> permissionSets)
    {
        for (Group group : ((DynamicUser) user).getGroups())
        {
            RoleSet roleSet = ((DynamicGroup) group).getRoles();
            roleSets.put(group, roleSet);
            for (Role r : roleSet)
            {
                DynamicRole role = (DynamicRole) r;
                permissionSets.put(role, role.getPermissions());
            }
        }
    }
}
