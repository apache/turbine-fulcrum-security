package org.apache.fulcrum.security.nt;

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
import javax.security.auth.login.LoginException;

import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.basic.entity.BasicUser;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicUser;
import org.apache.fulcrum.security.spi.AbstractUserManager;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.PasswordMismatchException;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.apache.fulcrum.security.util.UserSet;

import com.tagish.auth.win32.NTSystem;

/**
 * This implementation attempts to manager users against NT.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class NTUserManagerImpl extends AbstractUserManager
{
    @Override
    protected <T extends User> T persistNewUser(T user) throws DataBackendException
    {
        throw new RuntimeException("This method is not supported.");
    }

    /**
     * Retrieve a user from persistent storage using username as the key, and
     * authenticate the user. The implementation may chose to authenticate to
     * the server as the user whose data is being retrieved.
     *
     * @param userName
     *            the name of the user.
     * @param password
     *            the user supplied password.
     * @return an User object.
     * @exception PasswordMismatchException
     *                if the supplied password was incorrect.
     * @exception UnknownEntityException
     *                if the user's account does not exist in the database.
     * @exception DataBackendException
     *                if there is a problem accessing the storage.
     */
    @Override
    public <T extends User> T getUser(String userName, String password) throws PasswordMismatchException, UnknownEntityException, DataBackendException
    {
        T user = getUserInstance(userName);
        authenticate(user, password);
        return user;
    }

    /**
     * Check whether a specified user's account exists.
     *
     * The login name is used for looking up the account.
     *
     * @param user
     *            The user to be checked.
     * @return true if the specified account exists
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     */
    @Override
    public boolean checkExists(User user) throws DataBackendException
    {
        boolean exists = false;
        try
        {
            authenticate(user, user.getPassword());
            exists = true;
        }
        catch (PasswordMismatchException pme)
        {
            exists = false;
        }
        catch (UnknownEntityException uee)
        {
            exists = false;
        }
        return exists;
    }

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
        throw new RuntimeException("This method is not supported.");
    }

    /**
     * Retrieve a user from persistent storage using username as the key. Not
     * supported currently.
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
    public <T extends User> T getUser(String userName) throws UnknownEntityException, DataBackendException
    {
        throw new RuntimeException("Not supported by NT User Manager");
    }

    /**
     * Authenticate an User with the specified password. If authentication is
     * successful the method returns nothing. If there are any problems,
     * exception was thrown. Additionally, if the User object is of type
     * BasicUser or DynamicUser, then it will populate all the group information
     * as well!
     *
     * @param user
     *            an User object to authenticate.
     * @param password
     *            the user supplied password.
     * @exception PasswordMismatchException
     *                if the supplied password was incorrect.
     * @exception UnknownEntityException
     *                if the user's account does not exist in the database.
     * @exception DataBackendException
     *                if there is a problem accessing the storage.
     */
    @Override
    public void authenticate(User user, String password) throws PasswordMismatchException, UnknownEntityException, DataBackendException
    {
        NTSystem ntSystem = new NTSystem();
        char passwordArray[] = password.toCharArray();
        try
        {
            String username = ParseUtils.parseForUsername(user.getName());
            String domain = ParseUtils.parseForDomain(user.getName());
            ntSystem.logon(username, passwordArray, domain);
            if (!ntSystem.getName().equalsIgnoreCase(username))
            {
                throw new PasswordMismatchException("Could not authenticate user " + username + " against domain " + domain);
            }
            String groups[] = ntSystem.getGroupNames(false);
            for (String group2 : groups)
            {
                // Note how it populates groups? This
                // should maybe delegate a call to the
                // group manager to look for groups it
                // knows about instead.
                Group group = getGroupManager().getGroupInstance();
                group.setName(group2);
                group.setId(group2);
                if (user instanceof DynamicUser)
                {
                    ((DynamicUser) user).addGroup(group);
                }
                else if (user instanceof BasicUser)
                {
                    ((BasicUser) user).addGroup(group);
                }
            }
            ntSystem.logoff();
        }
        catch (LoginException le)
        {
            ntSystem.logoff();
            throw new DataBackendException(le.getMessage(), le);
        }
    }

    /**
     * Removes an user account from the system. Not supported currently.
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
        throw new RuntimeException("Not supported by NT User Manager");
    }

    /**
     * Creates new user account with specified attributes. Not supported
     * currently.
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
    public <T extends User> T addUser(T user, String password) throws DataBackendException, EntityExistsException
    {
        throw new RuntimeException("Not supported by NT User Manager");
    }

    /**
     * Stores User attributes. The User is required to exist in the system. Not
     * supported currently.
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
        throw new RuntimeException("Not supported by NT User Manager");
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
        throw new RuntimeException("Not supported by NT User Manager");
    }

}
