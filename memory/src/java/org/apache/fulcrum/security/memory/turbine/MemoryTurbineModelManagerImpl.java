package org.apache.fulcrum.security.memory.turbine;

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
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.turbine.AbstractTurbineModelManager;
import org.apache.fulcrum.security.model.turbine.TurbineModelManager;
import org.apache.fulcrum.security.model.turbine.entity.TurbineGroup;
import org.apache.fulcrum.security.model.turbine.entity.TurbinePermission;
import org.apache.fulcrum.security.model.turbine.entity.TurbineRole;
import org.apache.fulcrum.security.model.turbine.entity.TurbineUser;
import org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRole;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.UnknownEntityException;

/**
 * This implementation keeps all objects in memory. This is mostly meant to help
 * with testing and prototyping of ideas.
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh </a>
 * @version $Id: MemoryTurbineModelManagerImpl.java,v 1.2 2004/07/07 16:51:27
 *          epugh Exp $
 */
public class MemoryTurbineModelManagerImpl extends AbstractTurbineModelManager implements TurbineModelManager
{
    /**
     * Grants a Role a Permission
     * 
     * @param role
     *            the Role.
     * @param permission
     *            the Permission.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if role or permission is not present.
     */
    public synchronized void grant(Role role, Permission permission) throws DataBackendException, UnknownEntityException
    {
        boolean roleExists = false;
        boolean permissionExists = false;
        try
        {
            roleExists = getRoleManager().checkExists(role);
            permissionExists = getPermissionManager().checkExists(permission);
            if (roleExists && permissionExists)
            {
                ((TurbineRole) role).addPermission(permission);
                ((TurbinePermission) permission).addRole(role);
                return;
            }
        }
        catch (Exception e)
        {
            throw new DataBackendException("grant(Role,Permission) failed", e);
        }

        if (!roleExists)
        {
            throw new UnknownEntityException("Unknown role '" + role.getName() + "'");
        }
        if (!permissionExists)
        {
            throw new UnknownEntityException("Unknown permission '" + permission.getName() + "'");
        }
    }

    /**
     * Revokes a Permission from a Role.
     * 
     * @param role
     *            the Role.
     * @param permission
     *            the Permission.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if role or permission is not present.
     */
    public synchronized void revoke(Role role, Permission permission) throws DataBackendException, UnknownEntityException
    {
        boolean roleExists = false;
        boolean permissionExists = false;
        try
        {
            roleExists = getRoleManager().checkExists(role);
            permissionExists = getPermissionManager().checkExists(permission);
            if (roleExists && permissionExists)
            {
                ((TurbineRole) role).removePermission(permission);
                ((TurbinePermission) permission).removeRole(role);
                return;
            }
        }
        catch (Exception e)
        {
            throw new DataBackendException("revoke(Role,Permission) failed", e);
        }

        if (!roleExists)
        {
            throw new UnknownEntityException("Unknown role '" + role.getName() + "'");
        }
        if (!permissionExists)
        {
            throw new UnknownEntityException("Unknown permission '" + permission.getName() + "'");
        }
    }

    public void grant(User user, Group group, Role role) throws DataBackendException, UnknownEntityException
    {
        boolean roleExists = false;
        boolean userExists = false;
        boolean groupExists = false;
        try
        {
            roleExists = getRoleManager().checkExists(role);
            userExists = getUserManager().checkExists(user);
            groupExists = getGroupManager().checkExists(group);
            if (roleExists && groupExists && userExists)
            {
                TurbineUserGroupRole ugr = new TurbineUserGroupRole();
                ugr.setGroup(group);
                ugr.setRole(role);
                ugr.setUser(user);
                ((TurbineUser) user).addUserGroupRole(ugr);
                ((TurbineGroup) group).addUserGroupRole(ugr);
                ((TurbineRole) role).addUserGroupRole(ugr);
                return;
            }
        }
        catch (Exception e)
        {
            throw new DataBackendException("grant(Role,Permission) failed", e);
        }

        if (!roleExists)
        {
            throw new UnknownEntityException("Unknown role '" + role.getName() + "'");
        }
        if (!groupExists)
        {
            throw new UnknownEntityException("Unknown group '" + group.getName() + "'");
        }
        if (!userExists)
        {
            throw new UnknownEntityException("Unknown user '" + user.getName() + "'");
        }

    }

    public void revoke(User user, Group group, Role role) throws DataBackendException, UnknownEntityException
    {
        boolean roleExists = false;
        boolean userExists = false;
        boolean groupExists = false;
        try
        {
            roleExists = getRoleManager().checkExists(role);
            userExists = getUserManager().checkExists(user);
            groupExists = getGroupManager().checkExists(group);
            if (roleExists && groupExists && userExists)
            {
                boolean ugrFound = false;
                for (TurbineUserGroupRole ugr : ((TurbineUser) user).getUserGroupRoleSet())
                {
                    if (ugr.getUser().equals(user) && ugr.getGroup().equals(group) && ugr.getRole().equals(role))
                    {
                        ugrFound = true;

                        ((TurbineUser) user).removeUserGroupRole(ugr);
                        ((TurbineGroup) group).removeUserGroupRole(ugr);
                        ((TurbineRole) role).removeUserGroupRole(ugr);

                        break;
                    }
                }
                if (!ugrFound)
                {
                    throw new UnknownEntityException("Could not find User/Group/Role");
                }

                return;
            }
        }
        catch (Exception e)
        {
            throw new DataBackendException("grant(Role,Permission) failed", e);
        }

        if (!roleExists)
        {
            throw new UnknownEntityException("Unknown role '" + role.getName() + "'");
        }
        if (!groupExists)
        {
            throw new UnknownEntityException("Unknown group '" + group.getName() + "'");
        }
        if (!userExists)
        {
            throw new UnknownEntityException("Unknown user '" + user.getName() + "'");
        }

    }

    @Override
    public void replace( User user, Role oldRole, Role newRole )
        throws DataBackendException, UnknownEntityException
    {
        Group group = getGlobalGroup();
        revoke( user, group, oldRole );
        grant( user, group, newRole );
    }
}
