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
import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.spi.AbstractRoleManager;
import org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.RoleSet;
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
public abstract class TorqueAbstractRoleManager extends AbstractRoleManager 
{
    
    /**
     * Avalon Service lifecycle method
     */
    @Override
	public void configure(Configuration conf) throws ConfigurationException
    {
       super.configure( conf );
 
    }
    
    /**
     * Get all specialized Roles
     *
     * @param con a database connection
     *
     * @return a List of Role instances
     *
     * @throws TorqueException if any database error occurs
     */
    protected abstract <T extends Role> List<T> doSelectAllRoles(Connection con)
        throws TorqueException;

    /**
     * Get a specialized Role by name
     *
     * @param name the name of the group
     * @param con a database connection
     *
     * @return a Role instance
     *
     * @throws NoRowsException if no such group exists
     * @throws TooManyRowsException if multiple groups with the given name exist
     * @throws TorqueException if any other database error occurs
     */
    protected abstract <T extends Role> T doSelectByName(String name, Connection con)
        throws NoRowsException, TooManyRowsException, TorqueException;

    /**
     * Get a specialized Role by id
     *
     * @param id the id of the group
     * @param con a database connection
     *
     * @return a Role instance
     *
     * @throws NoRowsException if no such group exists
     * @throws TooManyRowsException if multiple groups with the given id exist
     * @throws TorqueException if any other database error occurs
     */
    protected abstract <T extends Role> T doSelectById(Integer id, Connection con)
        throws NoRowsException, TooManyRowsException, TorqueException;


    /**
    * Renames an existing Role.
    *
    * @param role The object describing the role to be renamed.
    * @param name the new name for the role.
    * @throws DataBackendException if there was an error accessing the data
    *         backend.
    * @throws UnknownEntityException if the role does not exist.
    */
    @Override
	public synchronized void renameRole(Role role, String name) throws DataBackendException, UnknownEntityException
    {
        if (checkExists(role))
        {
            role.setName(name);

            try
            {
                TorqueAbstractSecurityEntity r = (TorqueAbstractSecurityEntity)role;
                r.setNew(false);
                r.save();
            }
            catch (Exception e)
            {
                throw new DataBackendException("Renaming Role '" + role.getName() + "' failed", e);
            }
        }
        else
        {
            throw new UnknownEntityException("Unknown Role '" + role.getName() + "'");
        }
    }

    /**
    * Creates a new role with specified attributes.
    *
    * @param role the object describing the role to be created.
    * @return a new Role object that has id set up properly.
    * @throws DataBackendException if there was an error accessing the data
    *         backend.
    * @throws EntityExistsException if the role already exists.
    */
    @Override
	protected synchronized <T extends Role> T persistNewRole(T role) throws DataBackendException
    {
        try
        {
            ((TorqueAbstractSecurityEntity)role).save();
        }
        catch (Exception e)
        {
            throw new DataBackendException("Adding Role '" + role.getName() + "' failed", e);
        }

        return role;
    }

    /**
    * Removes a Role from the system.
    *
    * @param role The object describing the role to be removed.
    * @throws DataBackendException if there was an error accessing the data
    *         backend.
    * @throws UnknownEntityException if the role does not exist.
    */
    @Override
	public synchronized void removeRole(Role role) throws DataBackendException, UnknownEntityException
    {
        if (checkExists(role))
        {
            try
            {
                ((TorqueAbstractSecurityEntity)role).delete();
            }
            catch (TorqueException e)
            {
                throw new DataBackendException("Removing Role '" + role.getName() + "' failed", e);
            }
        }
        else
        {
            throw new UnknownEntityException("Unknown role '" + role.getName() + "'");
        }
    }

    /**
      * Determines if the <code>Role</code> exists in the security system.
      *
      * @param roleName a <code>Role</code> value
      * @return true if the role name exists in the system, false otherwise
      * @throws DataBackendException when more than one Role with
      *         the same name exists.
      */
    @Override
	public boolean checkExists(String roleName) throws DataBackendException
    {
        boolean exists = false;

        Connection con = null;

        try
        {
            con = Transaction.begin();

            doSelectByName(roleName, con);

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
            throw new DataBackendException("Multiple roles with same name '" + roleName + "'");
        }
        catch (TorqueException e)
        {
            throw new DataBackendException("Error retrieving role information", e);
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
     * Retrieves all roles defined in the system.
     *
     * @return the names of all roles defined in the system.
     * @throws DataBackendException if there was an error accessing the
     *         data backend.
     */
    @Override
	public RoleSet getAllRoles() throws DataBackendException
    {
        RoleSet roleSet = new RoleSet();
        Connection con = null;

        try
        {
            con = Transaction.begin();

            List<Role> roles = doSelectAllRoles(con);

            for (Role role : roles)
            {
                // Add attached objects if they exist
                ((TorqueAbstractSecurityEntity)role).retrieveAttachedObjects(con);

                roleSet.add(role);
            }

            Transaction.commit(con);
            con = null;
        }
        catch (TorqueException e)
        {
            throw new DataBackendException("Error retrieving role information", e);
        }
        finally
        {
            if (con != null)
            {
                Transaction.safeRollback(con);
            }
        }

        return roleSet;
    }

    /**
     * Retrieve a Role object with specified id.
     *
     * @param id
     *            the id of the Role.
     * @return an object representing the Role with specified id.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the role does not exist.
     */
    @Override
	public <T extends Role> T getRoleById(Object id) throws DataBackendException, UnknownEntityException
    {
        T role;

        if (id != null && id instanceof Integer)
        {
            Connection con = null;

            try
            {
                con = Transaction.begin();

                role = doSelectById((Integer)id, con);

                // Add attached objects if they exist
                ((TorqueAbstractSecurityEntity)role).retrieveAttachedObjects(con);

                Transaction.commit(con);
                con = null;
            }
            catch (NoRowsException e)
            {
                throw new UnknownEntityException("Role with id '" + id + "' does not exist.", e);
            }
            catch (TorqueException e)
            {
                throw new DataBackendException("Error retrieving role information", e);
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
            throw new UnknownEntityException("Invalid role id '" + id + "'");
        }

        return role;
    }

    /**
     * Retrieve a Role object with specified name.
     *
     * @param name the name of the Role.
     * @return an object representing the Role with specified name.
     * @throws DataBackendException if there was an error accessing the
     *         data backend.
     * @throws UnknownEntityException if the role does not exist.
     */
    @Override
	public <T extends Role> T getRoleByName(String name) throws DataBackendException, UnknownEntityException
    {
        T role = null;
        Connection con = null;

        try
        {
            con = Transaction.begin();

            role = doSelectByName(name, con);

            // Add attached objects if they exist
            ((TorqueAbstractSecurityEntity)role).retrieveAttachedObjects(con);

            Transaction.commit(con);
            con = null;
        }
        catch (NoRowsException e)
        {
            throw new UnknownEntityException("Could not find role" + name);
        }
        catch (TooManyRowsException e)
        {
            throw new DataBackendException("Multiple Roles with same name '" + name + "'");
        }
        catch (TorqueException e)
        {
            throw new DataBackendException("Error retrieving role information", e);
        }
        finally
        {
            if (con != null)
            {
                Transaction.safeRollback(con);
            }
        }

        return role;
    }
}
