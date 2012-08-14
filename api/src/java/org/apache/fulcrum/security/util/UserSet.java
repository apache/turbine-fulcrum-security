package org.apache.fulcrum.security.util;

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

import java.util.Collection;

import org.apache.fulcrum.security.entity.User;

/**
 * This class represents a set of Users.  It is based on UserSet.
 * Hibernate doesn't return the right kind of set, so this is used to
 * force the type of set.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class UserSet
        extends SecuritySet<User>
{
    /**
     * Serial number
     */
    private static final long serialVersionUID = 4415634631270197073L;

    /**
     * Constructs an empty UserSet
     */
    public UserSet()
    {
        super();
    }

    /**
     * Constructs a new UserSet with specified contents.
     *
     * If the given collection contains multiple objects that are
     * identical WRT equals() method, some objects will be overwritten.
     *
     * @param users A collection of users to be contained in the set.
     */
    public UserSet(Collection<? extends User> users)
    {
        this();
        addAll(users);
    }

    /**
     * Returns a User with the given name, if it is contained in
     * this UserSet.
     *
     * @param userName Name of User.
     * @return User if argument matched a User in this
     * UserSet; null if no match.
     * @deprecated use getByName()
     */
    public User getUserByName(String userName)
    {
        return getByName(userName);
    }

    /**
     * Returns a User with the given id, if it is contained in this
     * UserSet.
     *
     * @param userId id of the User.
     * @return User if argument matched a User in this UserSet; null
     * if no match.
     * @deprecated use getById()
     */
    public User getUserById(Object userId)
    {
    	return getById(userId);
    }

    /**
     * Print out a UserSet as a String
     *
     * @returns The User Set as String
     *
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("UserSet: ");
        sb.append(super.toString());

        return sb.toString();
    }
}
