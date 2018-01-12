package org.apache.fulcrum.security.spi;

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
import org.apache.commons.lang.StringUtils;
import org.apache.fulcrum.security.UserManager;
import org.apache.fulcrum.security.acl.AccessControlList;
import org.apache.fulcrum.security.authenticator.Authenticator;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.ACLFactory;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.PasswordMismatchException;
import org.apache.fulcrum.security.util.UnknownEntityException;

/**
 * This implementation keeps all objects in memory. This is mostly meant to help
 * with testing and prototyping of ideas.
 *
 * @todo Need to load up Crypto component and actually encrypt passwords!
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public abstract class AbstractUserManager extends AbstractEntityManager implements UserManager
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected abstract <T extends User> T persistNewUser(T user) throws DataBackendException;

    private ACLFactory aclFactory;
    private Authenticator authenticator;

    @Override
	public <T extends AccessControlList> T getACL(User user) throws UnknownEntityException
    {
        return getACLFactory().getAccessControlList(user);
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
        return checkExists(user.getName());
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
        T user = getUser(userName);
        authenticate(user, password);
        return user;
    }

    @Override
	public <T extends User> T getUser(String name) throws DataBackendException, UnknownEntityException
    {
        @SuppressWarnings("unchecked")
		T user = (T)getAllUsers().getByName(name);
        if (user == null)
        {
            throw new UnknownEntityException("The specified user does not exist");
        }
        return user;
    }

    /**
     * Retrieve a User object with specified Id.
     *
     * @param id
     *            the id of the User.
     *
     * @return an object representing the User with specified id.
     *
     * @throws UnknownEntityException
     *             if the user does not exist in the database.
     * @throws DataBackendException
     *             if there is a problem accessing the storage.
     */
    @Override
	public <T extends User> T getUserById(Object id) throws DataBackendException, UnknownEntityException
    {
        @SuppressWarnings("unchecked")
		T user = (T)getAllUsers().getById(id);
        if (user == null)
        {
            throw new UnknownEntityException("The specified user does not exist");
        }
        return user;
    }

    /**
     * Authenticate an User with the specified password. If authentication is
     * successful the method returns nothing. If there are any problems,
     * exception was thrown.
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
        if (authenticator == null)
        {
            authenticator = (Authenticator) resolve(Authenticator.ROLE);

        }
        if (!authenticator.authenticate(user, password))
        {
            throw new PasswordMismatchException("Can not authenticate user.");
        }
    }

    /**
     * Change the password for an User. The user must have supplied the old
     * password to allow the change.
     *
     * @param user
     *            an User to change password for.
     * @param oldPassword
     *            The old password to verify
     * @param newPassword
     *            The new password to set
     * @exception PasswordMismatchException
     *                if the supplied password was incorrect.
     * @exception UnknownEntityException
     *                if the user's account does not exist in the database.
     * @exception DataBackendException
     *                if there is a problem accessing the storage.
     */
    @Override
	public void changePassword(User user, String oldPassword, String newPassword) throws PasswordMismatchException, UnknownEntityException,
            DataBackendException
    {
        if (!checkExists(user))
        {
            throw new UnknownEntityException("The account '" + user.getName() + "' does not exist");
        }
        if (!oldPassword.equals(user.getPassword()))
        {
            throw new PasswordMismatchException("The supplied old password for '" + user.getName() + "' was incorrect");
        }
        user.setPassword(newPassword);
        // save the changes in the database immediately, to prevent the password
        // being 'reverted' to the old value if the user data is lost somehow
        // before it is saved at session's expiry.
        saveUser(user);
    }

    /**
     * Forcibly sets new password for an User.
     *
     * This is supposed by the administrator to change the forgotten or
     * compromised passwords. Certain implementatations of this feature would
     * require administrative level access to the authenticating server /
     * program.
     *
     * @param user
     *            an User to change password for.
     * @param password
     *            the new password.
     * @exception UnknownEntityException
     *                if the user's record does not exist in the database.
     * @exception DataBackendException
     *                if there is a problem accessing the storage.
     */
    @Override
	public void forcePassword(User user, String password) throws UnknownEntityException, DataBackendException
    {
        if (!checkExists(user))
        {
            throw new UnknownEntityException("The account '" + user.getName() + "' does not exist");
        }
        user.setPassword(password);
        // save the changes in the database immediately, to prevent the
        // password being 'reverted' to the old value if the user data
        // is lost somehow before it is saved at session's expiry.
        saveUser(user);
    }

    /**
     * Construct a blank User object.
     *
     * This method calls getUserClass, and then creates a new object using the
     * default constructor.
     *
     * @return an object implementing User interface.
     * @throws DataBackendException
     *             if the object could not be instantiated.
     */
    @Override
	public <T extends User> T getUserInstance() throws DataBackendException
    {
        try
        {
            @SuppressWarnings("unchecked")
			T user = (T) Class.forName(getClassName()).newInstance();
            return user;
        }
        catch (Exception e)
        {
            throw new DataBackendException("Problem creating instance of class " + getClassName(), e);
        }
    }

    /**
     * Construct a blank User object.
     *
     * This method calls getUserClass, and then creates a new object using the
     * default constructor.
     *
     * @param userName
     *            The name of the user.
     *
     * @return an object implementing User interface.
     *
     * @throws DataBackendException
     *             if the object could not be instantiated.
     */
    @Override
	public <T extends User> T getUserInstance(String userName) throws DataBackendException
    {
        T user = getUserInstance();
        user.setName(userName);
        return user;
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
	public <T extends User> T addUser(T user, String password) throws DataBackendException, EntityExistsException
    {
        if (StringUtils.isEmpty(user.getName()))
        {
            throw new DataBackendException("Could not create " + "an user with empty name!");
        }
        if (checkExists(user))
        {
            throw new EntityExistsException("The account '" + user.getName() + "' already exists");
        }
        user.setPassword(password);
        try
        {
            return persistNewUser(user);
        }
        catch (Exception e)
        {
            throw new DataBackendException("Failed to create account '" + user.getName() + "'", e);
        }
    }

    /**
     * @return Returns the ACLFactory.
     */
    public ACLFactory getACLFactory()
    {
        if (aclFactory == null)
        {
            aclFactory = (ACLFactory) resolve(ACLFactory.ROLE);
        }
        return aclFactory;
    }

}
