package org.apache.fulcrum.security.memory;
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
import java.util.ArrayList;
import java.util.List;

import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.spi.AbstractUserManager;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.apache.fulcrum.security.util.UserSet;

/**
 * This implementation keeps all objects in memory.  This is mostly meant to help
 * with testing and prototyping of ideas.
 *
 * @todo Need to load up Crypto component and actually encrypt passwords!
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class MemoryUserManagerImpl
    extends AbstractUserManager
{
    private static List users = new ArrayList();

    /** Our Unique ID counter */
    // private static int uniqueId = 0;

    /**
     * Check whether a specified user's account exists.
     *
     * The login name is used for looking up the account.
     *
     * @param userName The name of the user to be checked.
     * @return true if the specified account exists
     * @throws DataBackendException if there was an error accessing
     *         the data backend.
     */
    public boolean checkExists(String userName) throws DataBackendException
    {
        return MemoryHelper.checkExists(users,userName);

    }

	/**
	 * Retrieves all users defined in the system.
	 *
	 * @return the names of all users defined in the system.
	 * @throws DataBackendException if there was an error accessing the data backend.
	 */
	public UserSet getAllUsers() throws DataBackendException
	{
		return new UserSet(users);
	}
    /**
    	* Removes an user account from the system.
    	*
    	* @param user the object describing the account to be removed.
    	* @throws DataBackendException if there was an error accessing the data
    	*         backend.
    	* @throws UnknownEntityException if the user account is not present.
    	*/
    public void removeUser(User user)
        throws DataBackendException, UnknownEntityException
    {
        users.remove(user);
    }
    /**
       * Creates new user account with specified attributes.
       *
       * @param user the object describing account to be created.
       * @param password The password to use for the account.
       *
       * @throws DataBackendException if there was an error accessing the
       *         data backend.
       * @throws EntityExistsException if the user account already exists.
       */
    protected User persistNewUser(User user)
        throws DataBackendException
    {

            users.remove(user);
            user.setId(MemoryHelper.getUniqueId());
            users.add(user);
            return user;

    }
    /**
       * Stores User attributes. The User is required to exist in the system.
       *
       * @param role The User to be stored.
       * @throws DataBackendException if there was an error accessing the data
       *         backend.
       * @throws UnknownEntityException if the role does not exist.
       */
    public void saveUser(User user)
        throws DataBackendException, UnknownEntityException
    {
        boolean userExists = false;
        userExists = checkExists(user);
        if (userExists)
        {
            users.remove(user);
            users.add(user);
        }
        else
        {
            throw new UnknownEntityException("Unknown user '" + user + "'");
        }
    }
}
