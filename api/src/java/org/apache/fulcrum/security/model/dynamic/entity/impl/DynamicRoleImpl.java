package org.apache.fulcrum.security.model.dynamic.entity.impl;

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

import java.util.Set;

import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.entity.impl.SecurityEntityImpl;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicRole;
import org.apache.fulcrum.security.util.GroupSet;
import org.apache.fulcrum.security.util.PermissionSet;

/**
 * Represents the "simple" model where permissions are related to roles, roles
 * are related to groups and groups are related to users, all in many to many
 * relationships.
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id: DynamicRole.java 437451 2006-08-27 20:20:44Z tv $
 */
public class DynamicRoleImpl extends SecurityEntityImpl implements DynamicRole
{
    private Set<? extends Permission> permissionSet = new PermissionSet();

    private Set<? extends Group> groupSet = new GroupSet();

    /**
     * Get the permission that are part of this role
     * 
     * @return a set of permissions
     */
    public PermissionSet getPermissions()
    {
        if (permissionSet instanceof PermissionSet)
        {
            return (PermissionSet) permissionSet;
        }
        else
        {
            permissionSet = new PermissionSet(permissionSet);
            return (PermissionSet) permissionSet;
        }
    }

    /**
     * Get the permission that are part of this role as Set
     * 
     * @return a set of permissions
     */
    @SuppressWarnings("unchecked")
    public <T extends Permission> Set<T> getPermissionsAsSet()
    {
        return (Set<T>) permissionSet;
    }

    /**
     * Set the permission that are part of this role
     * 
     * @param permissionSet
     *            a set of permissions
     */
    public void setPermissions(PermissionSet permissionSet)
    {
        if (permissionSet != null)
        {
            this.permissionSet = permissionSet;
        }
        else
        {
            this.permissionSet = new PermissionSet();
        }
    }

    /**
     * Set the permission that are part of this role as Set
     * 
     * @param permissions
     *            a set of permissions
     */
    public <T extends Permission> void setPermissionsAsSet(Set<T> permissions)
    {
        this.permissionSet = permissions;
    }

    /**
     * This method should only be used by a RoleManager. Not directly.
     * 
     * @param permission
     */
    public void addPermission(Permission permission)
    {
        getPermissions().add(permission);
    }

    /**
     * This method should only be used by a RoleManager. Not directly.
     * 
     * @param permission
     */
    public void removePermission(Permission permission)
    {
        getPermissions().remove(permission);
    }

    /**
     * Get the groups this role belongs to
     * 
     * @return a set of groups
     */
    public GroupSet getGroups()
    {
        if (groupSet instanceof GroupSet)
        {
            return (GroupSet) groupSet;
        }
        else
        {
            groupSet = new GroupSet(groupSet);
            return (GroupSet) groupSet;
        }
    }

    /**
     * Set the groups this role belongs to
     * 
     * @param groups
     *            the set of groups
     */
    public void setGroups(GroupSet groupSet)
    {
        if (groupSet != null)
        {
            this.groupSet = groupSet;
        }
        else
        {
            this.groupSet = new GroupSet();
        }
    }

    /**
     * This method should only be used by a RoleManager. Not directly.
     * 
     * @param group
     */
    public void removeGroup(Group group)
    {
        getGroups().remove(group);
    }

    /**
     * This method should only be used by a RoleManager. Not directly.
     * 
     * @param group
     */
    public void addGroup(Group group)
    {
        getGroups().add(group);
    }

    /**
     * Set the groups this role belongs to as a Set
     * 
     * @param groups
     *            the set of groups
     */
    public <T extends Group> void setGroupsAsSet(Set<T> groups)
    {
        this.groupSet = groups;
    }

    /**
     * Get the groups this role belongs to as a Set
     * 
     * @return a set of groups
     */
    @SuppressWarnings("unchecked")
    public <T extends Group> Set<T> getGroupsAsSet()
    {
        return (Set<T>) groupSet;
    }
}
