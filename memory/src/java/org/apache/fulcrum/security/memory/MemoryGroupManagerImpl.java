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

import org.apache.fulcrum.security.GroupManager;
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.spi.AbstractGroupManager;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.GroupSet;
import org.apache.fulcrum.security.util.UnknownEntityException;

/**
 * This implementation keeps all objects in memory. This is mostly meant to help
 * with testing and prototyping of ideas.
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class MemoryGroupManagerImpl extends AbstractGroupManager implements GroupManager
{
    private static List<Group> groups = new ArrayList<Group>();

    /** Our Unique ID counter */
    // private static int uniqueId = 0;

    /**
     * Retrieves all groups defined in the system.
     * 
     * @return the names of all groups defined in the system.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     */
    public GroupSet getAllGroups() throws DataBackendException
    {
        return new GroupSet(groups);
    }

    /**
     * Removes a Group from the system.
     * 
     * @param group
     *            The object describing the group to be removed.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the group does not exist.
     */
    public synchronized void removeGroup(Group group) throws DataBackendException, UnknownEntityException
    {
        boolean groupExists = false;
        try
        {
            groupExists = checkExists(group);
            if (groupExists)
            {
                groups.remove(group);
                return;
            }
            else
            {
                throw new UnknownEntityException("Unknown group '" + group + "'");
            }
        }
        catch (Exception e)
        {
            getLogger().error("Failed to delete a Group", e);
            throw new DataBackendException("removeGroup(Group) failed", e);
        }
    }

    /**
     * Renames an existing Group.
     * 
     * @param group
     *            The object describing the group to be renamed.
     * @param name
     *            the new name for the group.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the group does not exist.
     */
    public synchronized void renameGroup(Group group, String name) throws DataBackendException, UnknownEntityException
    {
        boolean groupExists = false;
        try
        {
            groupExists = checkExists(group);
            if (groupExists)
            {
                groups.remove(group);
                group.setName(name);
                groups.add(group);
            }
            else
            {
                throw new UnknownEntityException("Unknown group '" + group + "'");
            }
        }
        catch (Exception e)
        {
            throw new DataBackendException("renameGroup(Group,String)", e);
        }
    }

    /**
     * Determines if the <code>Group</code> exists in the security system.
     * 
     * @param group
     *            a <code>Group</code> value
     * @return true if the group exists in the system, false otherwise
     * @throws DataBackendException
     *             when more than one Group with the same name exists.
     * @throws Exception
     *             A generic exception.
     */
    public boolean checkExists(String groupName) throws DataBackendException
    {
        return MemoryHelper.checkExists(groups, groupName);
    }

    /**
     * Creates a new group with specified attributes.
     * 
     * @param group
     *            the object describing the group to be created.
     * @return a new Group object that has id set up properly.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws EntityExistsException
     *             if the group already exists.
     */
    @Override
    public synchronized Group persistNewGroup(Group group) throws DataBackendException
    {

        group.setId(MemoryHelper.getUniqueId());
        groups.add(group);
        // return the object with correct id
        return group;

    }

}
