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

import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.entity.impl.SecurityEntityImpl;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicPermission;
import org.apache.fulcrum.security.util.RoleSet;

/**
 * Represents the "simple" model where permissions are related to roles,
 * roles are related to groups and groups are related to users,
 * all in many to many relationships.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id: DynamicPermission.java 223059 2004-07-07 16:49:09Z epugh $
 */
public class DynamicPermissionImpl extends SecurityEntityImpl implements DynamicPermission
{
    private Set<? extends Role> roleSet = new RoleSet();

    /**
     * Get the roles that this permission belongs to
     *
     * @return a set of roles
     */
    public RoleSet getRoles()
    {
        if (roleSet instanceof RoleSet) {
			return (RoleSet) roleSet;
		} else {
            roleSet = new RoleSet(roleSet);
            return (RoleSet) roleSet;
        }
    }

    /**
     * Set the roles that this permission belongs to
     *
     * @param roleSet a set of roles
     */
    public void setRoles(RoleSet roleSet)
    {
        if (roleSet != null) {
			this.roleSet = roleSet;
		} else {
			this.roleSet = new RoleSet();
		}
    }

    /**
     * Add a role to this permission
     *
     * @param role the role to add
     */
    public void addRole(Role role)
    {
        getRoles().add(role);
    }

    /**
     * Remove a role from this permission
     *
     * @param role the role to remove
     */
    public void removeRole(Role role)
    {
        getRoles().remove(role);
    }

    /**
     * Set the roles that this permission belongs to as Set
     *
     * @param roles a set of roles
     */
    public <T extends Role> void setRolesAsSet(Set<T> roles)
    {
        this.roleSet = roles;
    }

    /**
     * Get the roles that this permission belongs to as Set
     *
     * @return a set of roles
     */
    @SuppressWarnings("unchecked")
	public <T extends Role> Set<T> getRolesAsSet()
    {
        return (Set<T>)roleSet;
    }
}
