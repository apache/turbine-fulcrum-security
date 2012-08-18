package org.apache.fulcrum.security.hibernate;

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
import java.util.List;

import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.spi.AbstractUserManager;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.apache.fulcrum.security.util.UserSet;
import org.hibernate.HibernateException;

/**
 * This implementation persists to a database via Hibernate.
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
@SuppressWarnings("unchecked")
public class HibernateUserManagerImpl extends AbstractUserManager
{
    private PersistenceHelper persistenceHelper;

    /**
     * Check whether a specified user's account exists.
     * 
     * The login name is used for looking up the account.
     * 
     * @param userName
     *            The name of the user to be checked.
     * @return true if the specified account exists
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     */
    public boolean checkExists(String userName) throws DataBackendException
    {
        List<User> users = null;
        try
        {
            users = getPersistenceHelper().retrieveSession().createQuery("from " + User.class.getName() + " su where su.name=:name")
                    .setString("name", userName.toLowerCase()).list();
        }
        catch (HibernateException e)
        {
            throw new DataBackendException("Error retrieving user information", e);
        }
        if (users.size() > 1)
        {
            throw new DataBackendException("Multiple Users with same username '" + userName + "'");
        }
        return (users.size() == 1);
    }

    /**
     * Retrieve a user from persistent storage using username as the key.
     * 
     * @param userName
     *            the name of the user.
     * @return an User object.
     * @exception UnknownEntityException
     *                if the user's account does not exist in the database.
     * @exception DataBackendException
     *                if there is a problem accessing the storage.
     */
    @Override
    public User getUser(String userName) throws UnknownEntityException, DataBackendException
    {
        List<User> users = null;
        try
        {
            users = getPersistenceHelper().retrieveSession().createQuery("from " + User.class.getName() + " su where su.name=:name")
                    .setString("name", userName.toLowerCase()).list();
        }
        catch (HibernateException e)
        {
            throw new DataBackendException("Error retrieving user information", e);
        }
        if (users.size() > 1)
        {
            throw new DataBackendException("Multiple Users with same username '" + userName + "'");
        }
        if (users.size() == 1)
        {
            return users.get(0);
        }
        throw new UnknownEntityException("Unknown user '" + userName + "'");
    }

    /**
     * Retrieves all users defined in the system.
     * 
     * @return the names of all users defined in the system.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     */
    public UserSet getAllUsers() throws DataBackendException
    {
        UserSet userSet = new UserSet();
        try
        {
            List<User> users = getPersistenceHelper().retrieveSession().createQuery("from " + User.class.getName()).list();
            userSet.add(users);
        }
        catch (HibernateException e)
        {
            throw new DataBackendException("Error retrieving all users", e);
        }
        return userSet;
    }

    /**
     * Removes an user account from the system.
     * 
     * @param user
     *            the object describing the account to be removed.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the user account is not present.
     */
    public void removeUser(User user) throws DataBackendException, UnknownEntityException
    {
        getPersistenceHelper().removeEntity(user);
    }

    /**
     * Creates new user account with specified attributes.
     * 
     * @param user
     *            the object describing account to be created.
     * @param password
     *            The password to use for the account.
     * 
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws EntityExistsException
     *             if the user account already exists.
     */
    @Override
    public User persistNewUser(User user) throws DataBackendException
    {
        getPersistenceHelper().addEntity(user);
        return user;
    }

    /**
     * Stores User attributes. The User is required to exist in the system.
     * 
     * @param role
     *            The User to be stored.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the role does not exist.
     */
    public void saveUser(User user) throws DataBackendException, UnknownEntityException
    {
        boolean userExists = false;
        userExists = checkExists(user);
        if (userExists)
        {
            getPersistenceHelper().updateEntity(user);
        }
        else
        {
            throw new UnknownEntityException("Unknown user '" + user + "'");
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

    /**
     * Retrieve a User object with specified id.
     * 
     * @param id
     *            the id of the User.
     * @return an object representing the User with specified id.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the user does not exist.
     */
    @Override
    public User getUserById(Object id) throws DataBackendException, UnknownEntityException
    {
        User user = null;

        if (id != null)
        {
            try
            {
                List<User> users = getPersistenceHelper().retrieveSession()
                        .createQuery("from " + User.class.getName() + " su where su.id=:id").setLong("id", ((Long) id).longValue()).list();
                if (users.size() == 0)
                {
                    throw new UnknownEntityException("Could not find user by id " + id);
                }
                user = users.get(0);
                // session.close();
            }
            catch (HibernateException e)
            {
                throw new DataBackendException("Error retrieving user information", e);
            }
        }

        return user;
    }
}
