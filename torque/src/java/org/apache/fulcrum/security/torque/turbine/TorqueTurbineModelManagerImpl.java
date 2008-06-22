package org.apache.fulcrum.security.torque.turbine;
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
import java.sql.Connection;
import java.util.Iterator;

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
import org.apache.fulcrum.security.torque.TorqueAbstractSecurityEntity;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Transaction;
/**
 * This implementation persists to a database via Torque.
 *
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id:$
 */
public class TorqueTurbineModelManagerImpl extends AbstractTurbineModelManager implements TurbineModelManager
{
    /**
     * Grants a Role a Permission
     *
     * @param role the Role.
     * @param permission the Permission.
     * @throws DataBackendException if there was an error accessing the data backend.
     * @throws UnknownEntityException if role or permission is not present.
     */
    public synchronized void grant(Role role, Permission permission)
        throws DataBackendException, UnknownEntityException
    {
        boolean roleExists = getRoleManager().checkExists(role);
        boolean permissionExists = getPermissionManager().checkExists(permission);

        if (roleExists && permissionExists)
        {
            ((TurbineRole)role).addPermission(permission);
            ((TurbinePermission)permission).addRole(role);

            Connection con = null;

            try
            {
                con = Transaction.begin(((TorqueAbstractSecurityEntity)role).getDatabaseName());

                ((TorqueAbstractSecurityEntity)role).update(con);
                ((TorqueAbstractSecurityEntity)permission).update(con);

                Transaction.commit(con);
                con = null;
            }
            catch (TorqueException e)
            {
                throw new DataBackendException("grant('" + role.getName() + "', '" + permission.getName() + "') failed", e);
            }
            finally
            {
                if (con != null)
                {
                    Transaction.safeRollback(con);
                }
            }

            return;
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
     * @param role the Role.
     * @param permission the Permission.
     * @throws DataBackendException if there was an error accessing the data backend.
     * @throws UnknownEntityException if role or permission is not present.
     */
    public synchronized void revoke(Role role, Permission permission)
        throws DataBackendException, UnknownEntityException
    {
        boolean roleExists = getRoleManager().checkExists(role);
        boolean permissionExists = getPermissionManager().checkExists(permission);

        if (roleExists && permissionExists)
        {
            ((TurbineRole)role).removePermission(permission);
            ((TurbinePermission)permission).removeRole(role);

            Connection con = null;

            try
            {
                con = Transaction.begin(((TorqueAbstractSecurityEntity)role).getDatabaseName());

                ((TorqueAbstractSecurityEntity)role).update(con);
                ((TorqueAbstractSecurityEntity)permission).update(con);

                Transaction.commit(con);
                con = null;
            }
            catch (TorqueException e)
            {
                throw new DataBackendException("revoke('" + role.getName() + "', '" + permission.getName() + "') failed", e);
            }
            finally
            {
                if (con != null)
                {
                    Transaction.safeRollback(con);
                }
            }

            return;
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

    public synchronized void grant(User user, Group group, Role role) throws DataBackendException, UnknownEntityException
    {
        boolean roleExists = getRoleManager().checkExists(role);
        boolean userExists = getUserManager().checkExists(user);
        boolean groupExists = getGroupManager().checkExists(group);

        if (roleExists && groupExists && userExists)
        {
            TurbineUserGroupRole user_group_role = new TurbineUserGroupRole();
            user_group_role.setUser(user);
            user_group_role.setGroup(group);
            user_group_role.setRole(role);
            ((TurbineUser) user).addUserGroupRole(user_group_role);
            ((TurbineGroup) group).addUserGroupRole(user_group_role);
            ((TurbineRole) role).addUserGroupRole(user_group_role);

            Connection con = null;

            try
            {
                con = Transaction.begin(((TorqueAbstractSecurityEntity)user).getDatabaseName());

                ((TorqueAbstractSecurityEntity)user).update(con);
                ((TorqueAbstractSecurityEntity)group).update(con);
                ((TorqueAbstractSecurityEntity)role).update(con);

                Transaction.commit(con);
                con = null;
            }
            catch (TorqueException e)
            {
                throw new DataBackendException("grant('"
                        + user.getName() + "', '"
                        + group.getName() + "', '"
                        + role.getName() + "') failed", e);
            }
            finally
            {
                if (con != null)
                {
                    Transaction.safeRollback(con);
                }
            }

            return;
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

    public synchronized void revoke(User user, Group group, Role role)
        throws DataBackendException, UnknownEntityException
    {
        boolean roleExists = getRoleManager().checkExists(role);
        boolean userExists = getUserManager().checkExists(user);
        boolean groupExists = getGroupManager().checkExists(group);

        if (roleExists && groupExists && userExists)
        {
            boolean ugrFound = false;
            TurbineUserGroupRole user_group_role = null;

            for (Iterator i = ((TurbineUser) user).getUserGroupRoleSet()
                    .iterator(); i.hasNext();)
            {
                user_group_role = (TurbineUserGroupRole) i.next();
                if (user_group_role.getUser().equals(user)
                    && user_group_role.getGroup().equals(group)
                    && user_group_role.getRole().equals(role))
                {
                    ugrFound = true;
                    break;
                }
            }

            if (!ugrFound)
            {
                throw new UnknownEntityException("Could not find User/Group/Role");
            }

            ((TurbineUser)user).removeUserGroupRole(user_group_role);
            ((TurbineGroup)group).removeUserGroupRole(user_group_role);
            ((TurbineRole)role).removeUserGroupRole(user_group_role);

            Connection con = null;

            try
            {
                con = Transaction.begin(((TorqueAbstractSecurityEntity)user).getDatabaseName());

                ((TorqueAbstractSecurityEntity)user).update(con);
                ((TorqueAbstractSecurityEntity)group).update(con);
                ((TorqueAbstractSecurityEntity)role).update(con);

                Transaction.commit(con);
                con = null;
            }
            catch (TorqueException e)
            {
                throw new DataBackendException("revoke('"
                        + user.getName() + "', '"
                        + group.getName() + "', '"
                        + role.getName() + "') failed", e);
            }
            finally
            {
                if (con != null)
                {
                    Transaction.safeRollback(con);
                }
            }

            return;
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
}
