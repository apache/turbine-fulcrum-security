package org.apache.fulcrum.security.spi;

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
import org.apache.commons.lang3.StringUtils;
import org.apache.fulcrum.security.GroupManager;
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.UnknownEntityException;

/**
 * This implementation keeps all objects in memory. This is mostly meant to help
 * with testing and prototyping of ideas.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public abstract class AbstractGroupManager extends AbstractEntityManager implements GroupManager
{
    /**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	protected abstract <T extends Group> T persistNewGroup(T group) throws DataBackendException;

    /**
     * Construct a blank Group object.
     *
     * This method calls getGroupClass, and then creates a new object using the
     * default constructor.
     *
     * @return an object implementing Group interface.
     * @throws DataBackendException
     *             if the object could not be instantiated.
     */
    @Override
	public <T extends Group> T getGroupInstance() throws DataBackendException
    {
        try
        {
            @SuppressWarnings("unchecked")
			T group = (T) Class.forName(getClassName()).getConstructor( null ).newInstance();
            return group;
        }
        catch (Exception e)
        {
            throw new DataBackendException("Problem creating instance of class " + getClassName(), e);
        }
    }

    /**
     * Construct a blank Group object.
     *
     * This method calls getGroupClass, and then creates a new object using the
     * default constructor.
     *
     * @param groupName
     *            The name of the Group
     *
     * @return an object implementing Group interface.
     *
     * @throws DataBackendException
     *             if the object could not be instantiated.
     */
    @Override
	public <T extends Group> T getGroupInstance(String groupName) throws DataBackendException
    {
        T group = getGroupInstance();
        group.setName(groupName);
        return group;
    }

    /**
     * Retrieve a Group object with specified name.
     *
     * @param name
     *            the name of the Group.
     * @return an object representing the Group with specified name.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the group does not exist.
     */
    @Override
	public <T extends Group> T getGroupByName(String name) throws DataBackendException, UnknownEntityException
    {
        @SuppressWarnings("unchecked")
		T group = (T) getAllGroups().getByName(name);
        if (group == null)
        {
            throw new UnknownEntityException("The specified group does not exist");
        }
        return group;
    }

    /**
     * Retrieve a Group object with specified Id.
     *
     * @param id
     *            the ID of the Group.
     *
     * @return an object representing the Group with specified name.
     *
     * @throws UnknownEntityException
     *             if the permission does not exist in the database.
     * @throws DataBackendException
     *             if there is a problem accessing the storage.
     */
    @Override
	public <T extends Group> T getGroupById(Object id) throws DataBackendException, UnknownEntityException
    {
        @SuppressWarnings("unchecked")
		T group = (T) getAllGroups().getById(id);
        if (group == null)
        {
            throw new UnknownEntityException("The specified group does not exist");
        }
        return group;
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
	public synchronized <T extends Group> T addGroup(T group) throws DataBackendException, EntityExistsException
    {
        boolean groupExists = false;
        if (StringUtils.isEmpty(group.getName()))
        {
            throw new DataBackendException("Could not create a group with empty name!");
        }
//        if (group.getId() == null)
//        {
//            throw new DataBackendException("Could not create a group with an id of null!");
//        }
        groupExists = checkExists(group);
        if (!groupExists)
        {

            // return the object with correct id
            return persistNewGroup(group);
        }
        else
        {
            throw new EntityExistsException("Group '" + group + "' already exists");
        }
    }

    /**
     * Check whether a specified group exists.
     *
     * The name is used for looking up the group
     *
     * @param group
     *            The group to be checked.
     * @return true if the specified group exists
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     */
    @Override
	public boolean checkExists(Group group) throws DataBackendException
    {
        return checkExists(group.getName());
    }

}
