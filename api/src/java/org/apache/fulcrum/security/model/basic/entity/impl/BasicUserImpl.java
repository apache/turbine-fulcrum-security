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

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.entity.impl.SecurityEntityImpl;
import org.apache.fulcrum.security.model.basic.entity.BasicUser;
import org.apache.fulcrum.security.util.GroupSet;

/**
 * Represents the "basic" model where users can be part of multiple groups
 * directly, with no roles or permissions.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id: BasicUser.java 437451 2006-08-27 20:20:44Z tv $
 */
public class BasicUserImpl extends SecurityEntityImpl implements BasicUser
{
    /**
     * Serial number
     */
    private static final long serialVersionUID = 7911631916604987203L;

    /** The password */
    private String password;

    /** Set of related groups */
    private Set<? extends Group> groupSet = new GroupSet();

    /**
     * Returns the user's password. This method should not be used by the
     * application directly, because it's meaning depends upon the
     * implementation of UserManager that manages this particular user object.
     * Some implementations will use this attribute for storing a password
     * encrypted in some way, other will not use it at all, when user entered
     * password is presented to some external authority (like NT domain
     * controller) to validate it. See also
     * {@link org.apache.fulcrum.security.UserManager#authenticate(User,String)}
     * .
     *
     * @return A String with the password for the user.
     */
    @Override
    public String getPassword()
    {
        return password;
    }

    /**
     * Set password. Application should not use this method directly, see
     * {@link #getPassword()}. See also
     * {@link org.apache.fulcrum.security.UserManager#changePassword(User,String,String)}
     * .
     *
     * @param password
     *            The new password.
     */
    @Override
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Get the groups this user is part of
     *
     * @return a set of groups
     */
    @Override
    public GroupSet getGroups()
    {
        if (groupSet instanceof GroupSet)
        {
            return (GroupSet) groupSet;
        }
        else
        {
            groupSet = new GroupSet(groupSet);
            return (GroupSet) groupSet;
        }
    }

    /**
     * Set the groups this user is part of
     *
     * @param groups
     *            the set of groups
     */
    @Override
    public void setGroups(GroupSet groups)
    {
        if (groups != null)
        {
            this.groupSet = groups;
        }
        else
        {
            this.groupSet = new GroupSet();
        }
    }

    /**
     * Remove the group from the list of groups
     *
     * @param group
     *            the group to remove
     */
    @Override
    public void removeGroup(Group group)
    {
        getGroups().remove(group);
    }

    /**
     * Add the group to the list of groups
     *
     * @param group
     *            the group to add
     */
    @Override
    public void addGroup(Group group)
    {
        getGroups().add(group);
    }

    /**
     * Set the groups this user is part of as a Set
     *
     * @param groups
     *            the set of groups
     */
    @Override
    public <T extends Group> void setGroupsAsSet(Set<T> groups)
    {
        this.groupSet = groups;
    }

    /**
     * Get the groups this user is part of as a Set
     *
     * @return a set of groups
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Group> Set<T> getGroupsAsSet()
    {
        return (Set<T>) groupSet;
    }

    /**
     * Calculate a hash code for this object
     *
     * @see org.apache.fulcrum.security.entity.impl.SecurityEntityImpl#hashCode()
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(43, 19).append(getPassword()).appendSuper(super.hashCode()).toHashCode();
    }
}
