package org.apache.fulcrum.security.model.basic.entity;
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

import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.util.UserSet;

/**
 * Represents the "basic" model where users are part of groups, but nothing
 * else.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public interface BasicGroup extends Group
{
    /**
     * Get the users that are part of this group
     *
     * @return a set of users
     */
	public UserSet getUsers();

    /**
     * Set the users that are part of this group
     *
	 * @param userSet a set of users
	 */
	public void setUsers(UserSet userSet);

    /**
     * Get the users that are part of this group as a Set
     *
     * @return a set of users
     */
	public Set getUsersAsSet();

    /**
     * Set the users that are part of this group as a Set
     *
     * @param userSet a set of users
     */
	public void setUsersAsSet(Set users);

    /**
     * Add a user to this group
     *
     * @param user the user to add
     */
	public void addUser(User user);

    /**
     * Remove a user from this group
     *
     * @param user the user to remove
     */
	public void removeUser(User user);
}
