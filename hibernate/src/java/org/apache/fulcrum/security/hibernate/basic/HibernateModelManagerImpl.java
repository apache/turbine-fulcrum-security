package org.apache.fulcrum.security.hibernate.basic;

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
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.hibernate.PersistenceHelper;
import org.apache.fulcrum.security.model.basic.BasicModelManager;
import org.apache.fulcrum.security.model.basic.entity.BasicGroup;
import org.apache.fulcrum.security.model.basic.entity.BasicUser;
import org.apache.fulcrum.security.spi.AbstractManager;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * This implementation persists to a database via Hibernate.
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id: HibernateModelManagerImpl.java 1374014 2012-08-16 19:47:27Z tv
 *          $
 */
public class HibernateModelManagerImpl extends AbstractManager implements BasicModelManager
{
    private PersistenceHelper persistenceHelper;

    /**
     * Puts a user in a group.
     * 
     * This method is used when adding a user to a group
     * 
     * @param user
     *            the User.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the account is not present.
     */
    public synchronized void grant(User user, Group group) throws DataBackendException, UnknownEntityException
    {
        boolean groupExists = false;
        boolean userExists = false;
        Transaction transaction = null;

        try
        {
            groupExists = getGroupManager().checkExists(group);
            userExists = getUserManager().checkExists(user);
            if (groupExists && userExists)
            {
                Session session = getPersistenceHelper().retrieveSession();
                transaction = session.beginTransaction();
                ((BasicUser) user).addGroup(group);
                ((BasicGroup) group).addUser(user);
                session.update(user);
                session.update(group);
                transaction.commit();
                transaction = null;
            }
        }
        catch (Exception e)
        {
            throw new DataBackendException("grant(Role,Permission) failed", e);
        }
        finally
        {
            if (transaction != null)
            {
                transaction.rollback();
            }
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
     * @param user
     *            the User.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the user or group is not present.
     */
    public synchronized void revoke(User user, Group group) throws DataBackendException, UnknownEntityException
    {
        boolean groupExists = false;
        boolean userExists = false;
        Transaction transaction = null;

        try
        {
            groupExists = getGroupManager().checkExists(group);
            userExists = getUserManager().checkExists(user);
            if (groupExists && userExists)
            {
                Session session = getPersistenceHelper().retrieveSession();
                transaction = session.beginTransaction();
                ((BasicUser) user).removeGroup(group);
                ((BasicGroup) group).removeUser(user);
                session.update(user);
                session.update(group);
                transaction.commit();
                transaction = null;
            }
        }
        catch (Exception e)
        {
            throw new DataBackendException("grant(Role,Permission) failed", e);
        }
        finally
        {
            if (transaction != null)
            {
                transaction.rollback();
            }
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
     * @param user
     *            the User.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the account is not present.
     */
    public synchronized void revokeAll(User user) throws DataBackendException, UnknownEntityException
    {
        boolean userExists = false;
        userExists = getUserManager().checkExists(user);
        if (userExists)
        {
            Object groups[] = ((BasicUser) user).getGroups().toArray();

            for (Object group2 : groups)
            {
                Group group = (Group) group2;
                revoke(user, group);
            }

            return;
        }
        else
        {
            throw new UnknownEntityException("Unknown user '" + user.getName() + "'");
        }
    }

    /**
     * @return Returns the persistenceHelper.
     */
    public PersistenceHelper getPersistenceHelper()
    {
        if (persistenceHelper == null)
        {
            persistenceHelper = (PersistenceHelper) resolve(PersistenceHelper.ROLE);
        }
        return persistenceHelper;
    }

}
