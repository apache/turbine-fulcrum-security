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
import org.apache.fulcrum.security.util.GroupSet;

/**
 * Represents the "basic" model where users can be part of multiple groups
 * directly, with no roles or permissions.
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public interface BasicUser extends User
{
    /**
     * Get the groups this user is part of
     * 
     * @return a set of groups
     */
    public GroupSet getGroups();

    /**
     * Set the groups this user is part of
     * 
     * @param groups
     *            the set of groups
     */
    public void setGroups(GroupSet groups);

    /**
     * Remove the group from the list of groups
     * 
     * @param group
     *            the group to remove
     */
    public void removeGroup(Group group);

    /**
     * Add the group to the list of groups
     * 
     * @param group
     *            the group to add
     */
    public void addGroup(Group group);

    /**
     * Set the groups this user is part of as a Set
     * 
     * @param groups
     *            the set of groups
     */
    public <T extends Group> void setGroupsAsSet(Set<T> groups);

    /**
     * Get the groups this user is part of as a Set
     * 
     * @return a set of groups
     */
    public <T extends Group> Set<T> getGroupsAsSet();
}
