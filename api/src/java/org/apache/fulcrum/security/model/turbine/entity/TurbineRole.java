package org.apache.fulcrum.security.model.turbine.entity;

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

import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.util.PermissionSet;

/**
 * Represents the "turbine" model where permissions are in a many to many
 * relationship to roles, roles are related to groups are related to users, all
 * in many to many relationships.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh </a>
 * @version $Id$
 */
public interface TurbineRole extends Role
{
    /**
     * Get the permission that are part of this role
     *
     * @return a set of permissions
     */
    public PermissionSet getPermissions();

    /**
     * Get the permission that are part of this role as Set
     *
     * @return a set of permissions
     */
    public Set getPermissionsAsSet();

    /**
     * Set the permission that are part of this role
     *
     * @param permissionSet a set of permissions
     */
    public void setPermissions(PermissionSet permissionSet);

    /**
     * Set the permission that are part of this role as Set
     *
     * @param permissions a set of permissions
     */
    public void setPermissionsAsSet(Set permissions);

    /**
    * This method should only be used by a RoleManager.  Not directly.
    * @param permission
    */
    public void addPermission(Permission permission);

    /**
     * This method should only be used by a RoleManager.  Not directly.
     * @param permission
     */
    public void removePermission(Permission permission);

    /**
     * Get the User/Group/Role set associated with this role
     *
     * @return a set of User/GRoup/Role relations
     */
    public Set getUserGroupRoleSet();

    /**
     * Get the User/Group/Role set associated with this group
     *
     * @param userGroupRoleSet a set of User/GRoup/Role relations
     */
    public void setUserGroupRoleSet(Set userGroupRoleSet);

    /**
     * Add a User/Group/Role relation to this role
     *
     * @param userGroupRole a User/GRoup/Role relation to add
     */
    public void addUserGroupRole(TurbineUserGroupRole userGroupRole);

    /**
     * Remove a User/Group/Role relation from this role
     *
     * @param userGroupRole a User/GRoup/Role relation to remove
     */
    public void removeUserGroupRole(TurbineUserGroupRole userGroupRole);
}
