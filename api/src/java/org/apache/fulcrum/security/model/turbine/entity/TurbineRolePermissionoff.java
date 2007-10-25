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

import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.entity.Role;

/**
 * Represents the "turbine" model where permissions are in a many to many
 * relationship to roles, roles are related to groups are related to users, all
 * in many to many relationships.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh </a>
 * @version $Id$
 */
public class TurbineRolePermissionoff {
    private Role role;

    private Permission permission;

    /**
     * @return Returns the permission.
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     * @return Returns the role.
     */
    public Role getRole() {
        return role;
    }

    /**
     * @param permission
     *            The permission to set.
     */
    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    /**
     * @param role
     *            The role to set.
     */
    public void setRole(Role role) {
        this.role = role;
    }
}
