package org.apache.fulcrum.security;
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
import org.apache.avalon.framework.component.Component;
import org.apache.fulcrum.security.acl.AccessControlList;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.PasswordMismatchException;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.apache.fulcrum.security.util.UserSet;
/**
 * An UserManager performs {@link org.apache.fulcrum.security.entity.User} objects
 * related tasks on behalf of the {@link org.apache.fulcrum.security.BaseSecurityService}.
 *
 * The responsibilities of this class include loading data of an user from the
 * storage and putting them into the {@link org.apache.fulcrum.security.entity.User} objects,
 * saving those data to the permanent storage, and authenticating users.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @author <a href="mailto:Rafal.Krzewski@e-point.pl">Rafal Krzewski</a>
 * @version $Id$
 */
public interface UserManager extends Component
{
    /** Avalon role - used to id the component within the manager */
    String ROLE = UserManager.class.getName();
    /**
     * Construct a blank User object.
     *
     * This method calls getUserClass, and then creates a new object using
     * the default constructor.
     *
     * @return an object implementing User interface.
     * @throws DataBackendException if the object could not be instantiated.
     */
    User getUserInstance() throws DataBackendException;
    /**
     * Construct a blank User object.
     *
     * This method calls getUserClass, and then creates a new object using
     * the default constructor.
     *
     * @param userName The name of the user.
     *
     * @return an object implementing User interface.
     * @throws DataBackendException if the object could not be instantiated.
     */
    User getUserInstance(String userName) throws DataBackendException;

	/**
   * Determines if the <code>User</code> exists in the security system.
   *
   * @param role a <code>User</code> value
   * @return true if the user exists in the system, false otherwise
   * @throws DataBackendException when more than one user with
   *         the same name exists.
   * @throws Exception A generic exception.
   */
	boolean checkExists(User user) throws DataBackendException;
    /**
     * Check whether a specified user's account exists.
     *
     * The login name is used for looking up the account.
     *
     * @param userName The name of the user to be checked.
     * @return true if the specified account exists
     * @throws DataBackendException if there was an error accessing the data backend.
     */
    boolean checkExists(String userName) throws DataBackendException;
    /**
      * Retrieve a user from persistent storage using username as the
      * key.
      *
      * @param username the name of the user.
      * @return an User object.
      * @exception UnknownEntityException if the user's record does not
      *            exist in the database.
      * @exception DataBackendException if there is a problem accessing the
      *            storage.
      */
    User getUser(String username) throws UnknownEntityException, DataBackendException;

    /**
     * Retrieve a user from persistent storage using the id as the
     * key.
     *
     * @param id the id of the user.
     * @return an User object.
     * @exception UnknownEntityException if the user's record does not
     *            exist in the database.
     * @exception DataBackendException if there is a problem accessing the
     *            storage.
     */
   User getUserById(Object id) throws UnknownEntityException, DataBackendException;
    /**
     * Retrieve a user from persistent storage using username as the
     * key, and authenticate the user. The implementation may chose
     * to authenticate to the server as the user whose data is being
     * retrieved.
     *
     * @param username the name of the user.
     * @param password the user supplied password.
     * @return an User object.
     * @exception PasswordMismatchException if the supplied password was
     *            incorrect.
     * @exception UnknownEntityException if the user's record does not
     *            exist in the database.
     * @exception DataBackendException if there is a problem accessing the
     *            storage.
     */
    User getUser(String username, String password)
        throws PasswordMismatchException, UnknownEntityException, DataBackendException;

	/**
	   * Retrieves all users defined in the system.
	   *
	   * @return the names of all users defined in the system.
	   * @throws DataBackendException if there was an error accessing the data
	   *         backend.
	   */
	UserSet getAllUsers() throws DataBackendException;
    /**
    * Saves User's data in the permanent storage. The user account is required
    * to exist in the storage.
    *
    * @param user the user object to save
    * @throws UnknownEntityException if the user's account does not
    *         exist in the database.
    * @throws DataBackendException if there is a problem accessing the storage.
    */
    void saveUser(User user) throws UnknownEntityException, DataBackendException;
    /**
     * Authenticate an User with the specified password. If authentication
     * is successful the method returns nothing. If there are any problems,
     * exception was thrown.
     *
     * @param user an User object to authenticate.
     * @param password the user supplied password.
     * @exception PasswordMismatchException if the supplied password was
     *            incorrect.
     * @exception UnknownEntityException if the user's record does not
     *            exist in the database.
     * @exception DataBackendException if there is a problem accessing the
     *            storage.
     */
    void authenticate(User user, String password)
        throws PasswordMismatchException, UnknownEntityException, DataBackendException;
    /**
     * Creates new user account with specified attributes.
     *
     * @param user the object describing account to be created.
     * @param password The password to use for the object creation
     *
     * @throws DataBackendException if there was an error accessing the data backend.
     * @throws EntityExistsException if the user account already exists.
     */
    public User addUser(User user, String password) throws EntityExistsException, DataBackendException;
    /**
     * Removes an user account from the system.
     *
     * @param user the object describing the account to be removed.
     * @throws DataBackendException if there was an error accessing the data backend.
     * @throws UnknownEntityException if the user account is not present.
     */
    void removeUser(User user) throws UnknownEntityException, DataBackendException;
    /**
     * Change the password for an User.
     *
     * @param user an User to change password for.
     * @param oldPassword the current password suplied by the user.
     * @param newPassword the current password requested by the user.
     * @exception PasswordMismatchException if the supplied password was
     *            incorrect.
     * @exception UnknownEntityException if the user's record does not
     *            exist in the database.
     * @exception DataBackendException if there is a problem accessing the
     *            storage.
     */
    void changePassword(User user, String oldPassword, String newPassword)
        throws PasswordMismatchException, UnknownEntityException, DataBackendException;
    /**
     * Forcibly sets new password for an User.
     *
     * This is supposed by the administrator to change the forgotten or
     * compromised passwords. Certain implementatations of this feature
     * would require administrative level access to the authenticating
     * server / program.
     *
     * @param user an User to change password for.
     * @param password the new password.
     * @exception UnknownEntityException if the user's record does not
     *            exist in the database.
     * @exception DataBackendException if there is a problem accessing the
     *            storage.
     */
    void forcePassword(User user, String password) throws UnknownEntityException, DataBackendException;

    /**
       * Return a Class object representing the system's chosen implementation of
       * of ACL interface.
       *
       * @return systems's chosen implementation of ACL interface.
       * @throws UnknownEntityException if the implementation of ACL interface
       *         could not be determined, or does not exist.
       */
    public AccessControlList getACL(User user) throws UnknownEntityException;





}
