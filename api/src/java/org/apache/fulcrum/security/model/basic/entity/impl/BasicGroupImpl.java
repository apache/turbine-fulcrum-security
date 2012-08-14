package org.apache.fulcrum.security.model.basic.entity.impl;
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

import java.util.Set;

import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.entity.impl.SecurityEntityImpl;
import org.apache.fulcrum.security.model.basic.entity.BasicGroup;
import org.apache.fulcrum.security.util.UserSet;

/**
 * Represents the "basic" model where users are part of groups, but nothing
 * else.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id: BasicGroup.java 223057 2004-07-05 19:28:23Z epugh $
 */
public class BasicGroupImpl extends SecurityEntityImpl implements BasicGroup
{
	private Set<? extends User> userSet = new UserSet();

    /**
     * Get the users that are part of this group
     *
     * @return a set of users
     */
	public UserSet getUsers()
	{
	    if( userSet instanceof UserSet )
	    {
	        return (UserSet) userSet;
	    }
	    else
	    {
	        userSet = new UserSet(userSet);
	        return (UserSet)userSet;
	    }
	}

    /**
     * Set the users that are part of this group
     *
	 * @param userSet a set of users
	 */
	public void setUsers(UserSet userSet)
	{
	    if( userSet != null )
	    {
	        this.userSet = userSet;
	    }
	    else
	    {
	        this.userSet = new UserSet();
	    }
	}

    /**
     * Get the users that are part of this group as a Set
     *
     * @return a set of users
     */
	@SuppressWarnings("unchecked")
	public <T extends User> Set<T> getUsersAsSet()
	{
	    return (Set<T>)userSet;
	}

    /**
     * Set the users that are part of this group as a Set
     *
     * @param userSet a set of users
     */
	public <T extends User> void setUsersAsSet(Set<T> users)
	{
	    this.userSet = users;
	}

    /**
     * Add a user to this group
     *
     * @param user the user to add
     */
	public void addUser(User user)
	{
		getUsers().add(user);
	}

    /**
     * Remove a user from this group
     *
     * @param user the user to remove
     */
	public void removeUser(User user)
	{
		getUsers().remove(user);
	}
}
