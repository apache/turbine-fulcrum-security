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

import org.apache.fulcrum.security.entity.Group;

/**
 * This class represents a set of Groups. It's useful for building
 * administration UI.  It enforces that only
 * Group objects are allowed in the set and only relevant methods
 * are available.
 *
 * @author <a href="mailto:john.mcnally@clearink.com">John D. McNally</a>
 * @author <a href="mailto:bmclaugh@algx.net">Brett McLaughlin</a>
 * @author <a href="mailto:marco@intermeta.de">Marco Kn&uuml;ttel</a>
 * @author <a href="mailto:hps@intermeta.de">Henning P. Schmiedehausen</a>
 * @version $Id$
 */
public class GroupSet
        extends SecuritySet<Group>
{
    /**
     * Serial number
     */
    private static final long serialVersionUID = 6882240173053961011L;

    /**
     * Constructs an empty GroupSet
     */
    public GroupSet()
    {
        super();
    }

    /**
     * Constructs a new GroupSet with specified contents.
     *
     * If the given collection contains multiple objects that are
     * identical WRT equals() method, some objects will be overwritten.
     *
     * @param groups A collection of groups to be contained in the set.
     */
    public GroupSet(Collection<? extends Group> groups)
    {
        this();
        addAll(groups);
    }

    /**
     * Returns a Group with the given name, if it is contained in
     * this GroupSet.
     *
     * @param groupName Name of Group.
     * @return Group if argument matched a Group in this
     * GroupSet; null if no match.
     * @deprecated Use getByName()
     */
    public Group getGroupByName(String groupName)
    {
		return getByName(groupName);
    }

    /**
     * Returns a Group with the given id, if it is contained in
     * this GroupSet.
     *
     * @param groupId Id of the group
     * @return Group if argument matched a Group in this
     * GroupSet; null if no match.
     * @deprecated Use getById()
     */
    public Group getGroupById(Object groupId)
    {
    	return getById(groupId);
    }

    /**
     * Print out a GroupSet as a String
     *
     * @returns The Group Set as String
     *
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("GroupSet: ");
        sb.append(super.toString());

        return sb.toString();
    }
}
