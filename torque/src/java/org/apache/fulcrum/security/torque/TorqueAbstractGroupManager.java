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
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.spi.AbstractGroupManager;
import org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.GroupSet;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.apache.torque.NoRowsException;
import org.apache.torque.TooManyRowsException;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Transaction;

/**
 * This implementation persists to a database via Torque.
 *
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id:$
 */
public abstract class TorqueAbstractGroupManager extends AbstractGroupManager implements LazyLoadable 
{
    
	/** Serial version */
	private static final long serialVersionUID = -3735730556110100621L;
	
    private static final String LAZY_LOADING = "lazy";
    protected Boolean lazyLoading = false;
	
    /**
     * Avalon Service lifecycle method
     */
    @Override
    public void configure(Configuration conf) throws ConfigurationException
    {
       super.configure( conf );
       lazyLoading = conf.getAttributeAsBoolean( LAZY_LOADING, false);
       getLogger().debug("setting lazyLoading: " + lazyLoading);
    }
    
    /**
     * Get all specialized Groups
     *
     * @param con a database connection
     *
     * @return a List of Group instances
     *
     * @throws TorqueException if any database error occurs
     */
    protected abstract <T extends Group> List<T> doSelectAllGroups(Connection con)
        throws TorqueException;

    /**
     * Get a specialized Group by name
     *
     * @param name the name of the group
     * @param con a database connection
     *
     * @return a Group instance
     *
     * @throws NoRowsException if no such group exists
     * @throws TooManyRowsException if multiple groups with the given name exist
     * @throws TorqueException if any other database error occurs
     */
    protected abstract <T extends Group> T doSelectByName(String name, Connection con)
        throws NoRowsException, TooManyRowsException, TorqueException;

    /**
     * Get a specialized Group by id
     *
     * @param id the id of the group
     * @param con a database connection
     *
     * @return a Group instance
     *
     * @throws NoRowsException if no such group exists
     * @throws TooManyRowsException if multiple groups with the given id exist
     * @throws TorqueException if any other database error occurs
     */
    protected abstract <T extends Group> T doSelectById(Integer id, Connection con)
        throws NoRowsException, TooManyRowsException, TorqueException;

    /**
    * Creates a new group with specified attributes.
    *
    * @param group the object describing the group to be created.
    * @return a new Group object that has id set up properly.
    * @throws DataBackendException if there was an error accessing the data
    *         backend.
    * @throws EntityExistsException if the group already exists.
    */
    @Override
	protected synchronized <T extends Group> T persistNewGroup(T group) throws DataBackendException
    {
        try
        {
        	
            ((TorqueAbstractSecurityEntity)group).save();
        }
        catch (Exception e)
        {
            throw new DataBackendException("Adding Group '" + group.getName() + "' failed", e);
        }

        return group;
    }

    /**
    * Renames an existing Group.
    *
    * @param group The object describing the group to be renamed.
    * @param name the new name for the group.
    * @throws DataBackendException if there was an error accessing the data
    *         backend.
    * @throws UnknownEntityException if the group does not exist.
    */
    @Override
	public synchronized void renameGroup(Group group, String name) throws DataBackendException, UnknownEntityException
    {
        if (checkExists(group))
        {
            group.setName(name);

            try
            {
                TorqueAbstractSecurityEntity g = (TorqueAbstractSecurityEntity)group;
                g.setNew(false);
                g.save();
            }
            catch (Exception e)
            {
                throw new DataBackendException("Renaming Group '" + group.getName() + "' failed", e);
            }
        }
        else
        {
            throw new UnknownEntityException("Unknown group '" + group.getName() + "'");
        }
    }

    /**
    * Removes a Group from the system.
    *
    * @param group The object describing the group to be removed.
    * @throws DataBackendException if there was an error accessing the data
    *         backend.
    * @throws UnknownEntityException if the group does not exist.
    */
    @Override
	public synchronized void removeGroup(Group group) throws DataBackendException, UnknownEntityException
    {
        try
        {
            ((TorqueAbstractSecurityEntity)group).delete();
        }
        catch (TorqueException e)
        {
            throw new DataBackendException("Removing Group '" + group.getName() + "' failed", e);
        }
    }

    /**
     * Retrieve a Group object with specified name.
     *
     * @param name the name of the Group.
     * @return an object representing the Group with specified name.
     * @throws DataBackendException if there was an error accessing the
     *         data backend.
     * @throws UnknownEntityException if the group does not exist.
     */
    @Override
	public <T extends Group> T getGroupByName(String name) throws DataBackendException, UnknownEntityException
    {
        T group = null;
        Connection con = null;

        try
        {
            con = Transaction.begin();

            group = doSelectByName(name, con);

            // Add dependent objects if they exist
            ((TorqueAbstractSecurityEntity)group).retrieveAttachedObjects(con, getLazyLoading());

            Transaction.commit(con);
            con = null;
        }
        catch (NoRowsException e)
        {
            throw new UnknownEntityException("Could not find group " + name);
        }
        catch (TooManyRowsException e)
        {
            throw new DataBackendException("Multiple Groups with same name '" + name + "'");
        }
        catch (TorqueException e)
        {
            throw new DataBackendException("Error retrieving group information", e);
        }
        finally
        {
            if (con != null)
            {
                Transaction.safeRollback(con);
            }
        }

        return group;
    }

    /**
     * Retrieves all groups defined in the system.
     *
     * @return the names of all groups defined in the system.
     * @throws DataBackendException if there was an error accessing the
     *         data backend.
     */
    @Override
	public GroupSet getAllGroups() throws DataBackendException
    {
        GroupSet groupSet = new GroupSet();
        Connection con = null;

        try
        {
        
            con = Transaction.begin();

            List<Group> groups = doSelectAllGroups(con);

            for (Group group : groups)
            {
                // Add dependent objects if they exist
                ((TorqueAbstractSecurityEntity)group).retrieveAttachedObjects(con, getLazyLoading());

                groupSet.add(group);
            }

            Transaction.commit(con);
            con = null;
        }
        catch (TorqueException e)
        {
            throw new DataBackendException("Error retrieving group information", e);
        }
        finally
        {
            if (con != null)
            {
                Transaction.safeRollback(con);
            }
        }

        return groupSet;
    }

    /**
     * Determines if the <code>Group</code> exists in the security system.
     *
     * @param groupName a <code>Group</code> value
     * @return true if the group name exists in the system, false otherwise
     * @throws DataBackendException when more than one Group with
     *         the same name exists.
     */
    @Override
	public boolean checkExists(String groupName) throws DataBackendException
    {
        boolean exists = false;

        Connection con = null;

        try
        {
            con = Transaction.begin();

            doSelectByName(groupName, con);

            Transaction.commit(con);
            con = null;

            exists = true;
        }
        catch (NoRowsException e)
        {
            exists = false;
        }
        catch (TooManyRowsException e)
        {
            throw new DataBackendException(
                    "Multiple groups with same name '" + groupName + "'");
        }
        catch (TorqueException e)
        {
            throw new DataBackendException("Error retrieving group information", e);
        }
        finally
        {
            if (con != null)
            {
                Transaction.safeRollback(con);
            }
        }

        return exists;
    }

    /**
     * Retrieve a Group object with specified id.
     *
     * @param id
     *            the id of the Group.
     * @return an object representing the Group with specified id.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the group does not exist.
     */
    @Override
	public <T extends Group> T getGroupById(Object id) throws DataBackendException, UnknownEntityException
    {
        T group;

        if (id != null && id instanceof Integer)
        {
            Connection con = null;

            try
            {
                con = Transaction.begin();

                group = doSelectById((Integer)id, con);

                // Add dependent objects if they exist
                ((TorqueAbstractSecurityEntity)group).retrieveAttachedObjects(con, getLazyLoading());

                Transaction.commit(con);
                con = null;
            }
            catch (NoRowsException e)
            {
                throw new UnknownEntityException("Group with id '" + id + "' does not exist.", e);
            }
            catch (TorqueException e)
            {
                throw new DataBackendException("Error retrieving group information", e);
            }
            finally
            {
                if (con != null)
                {
                    Transaction.safeRollback(con);
                }
            }
        }
        else
        {
            throw new UnknownEntityException("Invalid group id '" + id + "'");
        }

        return group;
    }

    public Boolean getLazyLoading()
    {
        return lazyLoading;
    }

    public void setLazyLoading( Boolean lazyLoading )
    {
        this.lazyLoading = lazyLoading;
    }
}
