package org.apache.fulcrum.security.torque;

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
import java.util.List;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.spi.AbstractUserManager;
import org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.apache.fulcrum.security.util.UserSet;
import org.apache.torque.NoRowsException;
import org.apache.torque.TooManyRowsException;
import org.apache.torque.TorqueException;
import org.apache.torque.criteria.Criteria;
import org.apache.torque.util.Transaction;

/**
 * This implementation persists to a database via Torque.
 *
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id:$
 */
public abstract class TorqueAbstractUserManager extends AbstractUserManager {

	/** Serial version */
	private static final long serialVersionUID = 2050218990148719292L;

	/**
	 * Avalon Service lifecycle method
	 */
	@Override
	public void configure(Configuration conf) throws ConfigurationException {
		super.configure(conf);
	}

	/**
	 * Get all specialized Users
	 *
	 * @param con a database connection
	 *
	 * @return a List of User instances
	 *
	 * @throws TorqueException if any database error occurs
	 */
	protected abstract <T extends User> List<T> doSelectAllUsers(Connection con) throws TorqueException;
	
	/**
     * Get filtered specialized Users
     *
     * @param con a database connection
     * 
     * @param criteria the criteria filter
     *
     * @return a List of User instances
     *
     * @throws TorqueException if any database error occurs
     */
	protected abstract <T extends User> List<T> doSelectUsers(Connection con, Criteria criteria) throws TorqueException;

	/**
	 * Get a specialized User by name
	 *
	 * @param name the name of the group
	 * @param con  a database connection
	 *
	 * @return a User instance
	 *
	 * @throws NoRowsException      if no such group exists
	 * @throws TooManyRowsException if multiple groups with the given name exist
	 * @throws TorqueException      if any database error occurs if any other
	 *                              database error occurs
	 */
	protected abstract <T extends User> T doSelectByName(String name, Connection con)
			throws NoRowsException, TooManyRowsException, TorqueException;

	/**
	 * Get a specialized User by id
	 *
	 * @param id  the id of the group
	 * @param con a database connection
	 *
	 * @return a User instance
	 *
	 * @throws NoRowsException      if no such group exists
	 * @throws TooManyRowsException if multiple groups with the given id exist
	 * @throws TorqueException      if any database error occurs if any other
	 *                              database error occurs
	 */
	protected abstract <T extends User> T doSelectById(Integer id, Connection con)
			throws NoRowsException, TooManyRowsException, TorqueException;

	/**
	 * Removes an user account from the system.
	 *
	 * @param user the object describing the account to be removed.
	 * @throws DataBackendException   if there was an error accessing the data
	 *                                backend.
	 * @throws UnknownEntityException if the user account is not present.
	 */
	@Override
	public synchronized void removeUser(User user) throws DataBackendException, UnknownEntityException {
		try {
			((TorqueAbstractSecurityEntity) user).delete();
		} catch (TorqueException e) {
			throw new DataBackendException("Removing User '" + user.getName() + "' failed", e);
		}
	}

	/**
	 * Creates new user account with specified attributes.
	 *
	 * @param user the object describing account to be created.
	 *
	 * @throws DataBackendException if there was an error accessing the data
	 *                              backend.
	 */
	@Override
	protected synchronized <T extends User> T persistNewUser(T user) throws DataBackendException {
		try {
			TorqueAbstractSecurityEntity u = (TorqueAbstractSecurityEntity) user;
			u.save();
		} catch (Exception e) {
			throw new DataBackendException("Adding User '" + user.getName() + "' failed", e);
		}

		return user;
	}

	/**
	 * Stores User attributes. The User is required to exist in the system.
	 *
	 * @param user The User to be stored.
	 * @throws DataBackendException   if there was an error accessing the data
	 *                                backend.
	 * @throws UnknownEntityException if the role does not exist.
	 */
	@Override
	public synchronized void saveUser(User user) throws DataBackendException, UnknownEntityException {
		if (checkExists(user)) {
			try {
				TorqueAbstractSecurityEntity u = (TorqueAbstractSecurityEntity) user;
				u.setNew(false);
				u.save();
			} catch (Exception e) {
				throw new DataBackendException("Saving User '" + user.getName() + "' failed", e);
			}
		} else {
			throw new UnknownEntityException("Unknown user '" + user + "'");
		}
	}

	/**
	 * Check whether a specified user's account exists.
	 *
	 * The login name is used for looking up the account.
	 *
	 * @param userName The name of the user to be checked.
	 * @return true if the specified account exists
	 * @throws DataBackendException if there was an error accessing the data
	 *                              backend.
	 */
	@Override
	public boolean checkExists(String userName) throws DataBackendException {
		boolean exists = false;

		Connection con = null;

		try {
			con = Transaction.begin();

			doSelectByName(userName, con);

			Transaction.commit(con);
			con = null;

			exists = true;
		} catch (NoRowsException e) {
			exists = false;
		} catch (TooManyRowsException e) {
			throw new DataBackendException("Multiple Users with same username '" + userName + "'");
		} catch (TorqueException e) {
			throw new DataBackendException("Error retrieving user information", e);
		} finally {
			if (con != null) {
				Transaction.safeRollback(con);
			}
		}

		return exists;
	}

	/**
	 * Retrieve a user from persistent storage using username as the key.
	 * 
	 * Additionally retrieves all attached objects from {@link TorqueAbstractSecurityEntity#retrieveAttachedObjects(Connection, Boolean)}
	 *
	 * @param userName the name of the user.
	 * @return an User object.
	 * @exception UnknownEntityException if the user's account does not exist in the
	 *                                   database.
	 * @exception DataBackendException   if there is a problem accessing the
	 *                                   storage.
	 */
	@Override
	public <T extends User> T getUser(String userName) throws UnknownEntityException, DataBackendException {
		T user = null;
		Connection con = null;

		try {
			con = Transaction.begin();

			user = doSelectByName(userName.toLowerCase(), con);

			// Add attached objects if they exist
			((TorqueAbstractSecurityEntity) user).retrieveAttachedObjects(con, false);

			Transaction.commit(con);
			con = null;
		} catch (NoRowsException e) {
			throw new UnknownEntityException("Unknown user '" + userName + "'");
		} catch (TooManyRowsException e) {
			throw new DataBackendException("Multiple Users with same username '" + userName + "'");
		} catch (TorqueException e) {
			throw new DataBackendException("Error retrieving user information", e);
		} finally {
			if (con != null) {
				Transaction.safeRollback(con);
			}
		}

		return user;
	}

	/**
	 * Retrieves all users defined in the system.
	 *
	 * @return the names of all users defined in the system.
	 * @throws DataBackendException if there was an error accessing the data
	 *                              backend.
	 */
	@Override
	public <T extends User> UserSet<T> getAllUsers() throws DataBackendException {
		UserSet<T> userSet = new UserSet<T>();
		Connection con = null;

		try {
			con = Transaction.begin();

			List<User> users = doSelectAllUsers(con);

			for (User user : users) {
				// Add attached objects if they exist
				((TorqueAbstractSecurityEntity) user).retrieveAttachedObjects(con, false);

				userSet.add(user);
			}

			Transaction.commit(con);
			con = null;
		} catch (TorqueException e) {
			throw new DataBackendException("Error retrieving all users", e);
		} finally {
			if (con != null) {
				Transaction.safeRollback(con);
			}
		}

		return userSet;
	}
	
    @Override
    public <T extends User> UserSet<T> retrieveUserList(Object criteriaO) throws DataBackendException
    {
        if (! (criteriaO instanceof Criteria)) {
            throw new DataBackendException("Query object has to be of type " + Criteria.class.getName());
        }
        Criteria criteria = (Criteria) criteriaO;
        UserSet<T> userSet = new UserSet<T>();
        Connection con = null;

        try {
            con = Transaction.begin();

            List<User> users = doSelectUsers(con, criteria);

            for (User user : users) {
                // Add attached objects if they exist
                ((TorqueAbstractSecurityEntity) user).retrieveAttachedObjects(con, false);

                userSet.add(user);
            }

            Transaction.commit(con);
            con = null;
        } catch (TorqueException e) {
            throw new DataBackendException("Error retrieving filtered user list.", e);
        } finally {
            if (con != null) {
                Transaction.safeRollback(con);
            }
        }

        return userSet;
    }

	/**
	 * Retrieve a User object with specified id.
	 *
	 * @param id the id of the User.
	 * @return an object representing the User with specified id.
	 * @throws DataBackendException   if there was an error accessing the data
	 *                                backend.
	 * @throws UnknownEntityException if the user does not exist.
	 */
	@Override
	public <T extends User> T getUserById(Object id) throws DataBackendException, UnknownEntityException {
		T user;

		if (id != null && id instanceof Integer) {
			Connection con = null;

			try {
				con = Transaction.begin();

				user = doSelectById((Integer) id, con);

				// Add attached objects if they exist
				((TorqueAbstractSecurityEntity) user).retrieveAttachedObjects(con, false); //

				Transaction.commit(con);
				con = null;
			} catch (NoRowsException e) {
				throw new UnknownEntityException("User with id '" + id + "' does not exist.", e);
			} catch (TorqueException e) {
				throw new DataBackendException("Error retrieving user information", e);
			} finally {
				if (con != null) {
					Transaction.safeRollback(con);
				}
			}
		} else {
			throw new UnknownEntityException("Invalid user id '" + id + "'");
		}

		return user;
	}

}
