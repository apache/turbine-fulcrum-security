package org.apache.fulcrum.security.torque.basic;
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
import java.util.ArrayList;
import java.util.List;

import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.basic.BasicModelManager;
import org.apache.fulcrum.security.model.basic.entity.BasicGroup;
import org.apache.fulcrum.security.model.basic.entity.BasicUser;
import org.apache.fulcrum.security.spi.AbstractManager;
import org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity;
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
public class TorqueBasicModelManagerImpl extends AbstractManager implements BasicModelManager
{
    /**
     * Puts a user in a group.
     *
     * This method is used when adding a user to a group
     *
     * @param user the User.
     * @throws DataBackendException if there was an error accessing the data backend.
     * @throws UnknownEntityException if the account is not present.
     */
    public synchronized void grant(User user, Group group) throws DataBackendException, UnknownEntityException
    {
        boolean groupExists = getGroupManager().checkExists(group);
        boolean userExists = getUserManager().checkExists(user);

        if (groupExists && userExists)
        {
            ((BasicUser) user).addGroup(group);
            ((BasicGroup) group).addUser(user);

            Connection con = null;

            try
            {
                con = Transaction.begin(((TorqueAbstractSecurityEntity)user).getDatabaseName());

                ((TorqueAbstractSecurityEntity)user).update(con);
                ((TorqueAbstractSecurityEntity)group).update(con);

                Transaction.commit(con);
                con = null;
            }
            catch (TorqueException e)
            {
                throw new DataBackendException("grant('" + user.getName() + user.getId() + "', '" + group.getName() + group.getId() + "') failed", e);
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

        if (!groupExists)
        {
            throw new UnknownEntityException("Unknown group '" + group.getName() + "'");
        }

        if (!userExists)
        {
            throw new UnknownEntityException("Unknown user '" + user.getName() + "'");
        }
    }

    /**
     * Removes a user in a group.
     *
     * This method is used when removing a user to a group
     *
     * @param user the User.
     * @throws DataBackendException if there was an error accessing the data backend.
     * @throws UnknownEntityException if the user or group is not present.
     */
    public synchronized void revoke(User user, Group group) throws DataBackendException, UnknownEntityException
    {
        boolean groupExists = getGroupManager().checkExists(group);
        boolean userExists = getUserManager().checkExists(user);

        if (groupExists && userExists)
        {
            ((BasicUser) user).removeGroup(group);
            ((BasicGroup) group).removeUser(user);

            Connection con = null;

            try
            {
                con = Transaction.begin(((TorqueAbstractSecurityEntity)user).getDatabaseName());

                ((TorqueAbstractSecurityEntity)user).update(con);
                ((TorqueAbstractSecurityEntity)group).update(con);

                Transaction.commit(con);
                con = null;
            }
            catch (TorqueException e)
            {
                throw new DataBackendException("grant('" + user.getName() + user.getId() + "', '" + group.getName() + group.getId() + "') failed", e);
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

        if (!groupExists)
        {
            throw new UnknownEntityException("Unknown group '" + group.getName() + "'");
        }

        if (!userExists)
        {
            throw new UnknownEntityException("Unknown user '" + user.getName() + "'");
        }
    }

    /**
     * Revokes all groups from a user
     *
     * This method is used when deleting an account.
     *
     * @param user the User.
     * @throws DataBackendException if there was an error accessing the data backend.
     * @throws UnknownEntityException if the account is not present.
     */
    public synchronized void revokeAll(User user)
        throws DataBackendException, UnknownEntityException
    {
        boolean userExists = getUserManager().checkExists(user);

        if (userExists)
        {
            BasicUser u = (BasicUser) user;

            // copy to avoid ConcurrentModificationException
            List<Group> groups = new ArrayList<Group>(u.getGroups());

            for (Group group : groups)
            {
                u.removeGroup(group);
            }

            Connection con = null;

            try
            {
                con = Transaction.begin(((TorqueAbstractSecurityEntity)user).getDatabaseName());

                ((TorqueAbstractSecurityEntity)user).update(con);

                Transaction.commit(con);
                con = null;
            }
            catch (TorqueException e)
            {
                throw new DataBackendException("revokeAll('" + user.getName() + user.getId() + "') failed", e);
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
        else
        {
            throw new UnknownEntityException("Unknown user '" + user.getName() + "'");
        }
    }
}
