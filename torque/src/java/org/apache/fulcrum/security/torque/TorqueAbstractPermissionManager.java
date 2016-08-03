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
import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.spi.AbstractPermissionManager;
import org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.PermissionSet;
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
public abstract class TorqueAbstractPermissionManager extends AbstractPermissionManager
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
     * Get all specialized Permissions
     *
     * @param con a database connection
     *
     * @return a List of Permission instances
     *
     * @throws TorqueException if any database error occurs
     */
    protected abstract <T extends Permission> List<T> doSelectAllPermissions(Connection con)
        throws TorqueException;

    /**
     * Get a specialized Permission by name
     *
     * @param name the name of the group
     * @param con a database connection
     *
     * @return a Permission instance
     *
     * @throws NoRowsException if no such group exists
     * @throws TooManyRowsException if multiple groups with the given name exist
     * @throws TorqueException if any other database error occurs
     */
    protected abstract <T extends Permission> T doSelectByName(String name, Connection con)
        throws NoRowsException, TooManyRowsException, TorqueException;

    /**
     * Get a specialized Permission by id
     *
     * @param id the id of the group
     * @param con a database connection
     *
     * @return a Permission instance
     *
     * @throws NoRowsException if no such group exists
     * @throws TooManyRowsException if multiple groups with the given id exist
     * @throws TorqueException if any other database error occurs
     */
    protected abstract <T extends Permission> T doSelectById(Integer id, Connection con)
        throws NoRowsException, TooManyRowsException, TorqueException;

    /**
     * Renames an existing Permission.
     *
     * @param permission
     *            The object describing the permission to be renamed.
     * @param name
     *            the new name for the permission.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the permission does not exist.
     */
    @Override
	public synchronized void renamePermission(Permission permission, String name) throws DataBackendException, UnknownEntityException
    {
        if (checkExists(permission))
        {
            permission.setName(name);

            try
            {
                TorqueAbstractSecurityEntity p = (TorqueAbstractSecurityEntity)permission;
                p.setNew(false);
                p.save();
            }
            catch (Exception e)
            {
                throw new DataBackendException("Renaming Permission '" + permission.getName() + "' failed", e);
            }
        }
        else
        {
            throw new UnknownEntityException("Unknown permission '" + permission.getName() + "'");
        }
    }

    /**
     * Removes a Permission from the system.
     *
     * @param permission
     *            The object describing the permission to be removed.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the permission does not exist.
     */
    @Override
	public synchronized void removePermission(Permission permission) throws DataBackendException, UnknownEntityException
    {
        if (checkExists(permission))
        {
            try
            {

                ((TorqueAbstractSecurityEntity)permission).delete();
            }
            catch (TorqueException e)
            {
                throw new DataBackendException("Removing Permission '" + permission.getName() + "' failed", e);
            }
        }
        else
        {
            throw new UnknownEntityException("Unknown permission '" + permission.getName() + "'");
        }
    }

    /**
     * Creates a new permission with specified attributes.
     *
     * @param permission
     *            the object describing the permission to be created.
     * @return a new Permission object that has id set up properly.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws EntityExistsException
     *             if the permission already exists.
     */
    @Override
	protected synchronized <T extends Permission> T persistNewPermission(T permission) throws DataBackendException
    {
        try
        {
            ((TorqueAbstractSecurityEntity)permission).save();
        }
        catch (Exception e)
        {
            throw new DataBackendException("Adding Permission '" + permission.getName() + "' failed", e);
        }

        return permission;
    }

    /**
     * Retrieves all permissions defined in the system.
     *
     * @return the names of all roles defined in the system.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     */
    @Override
	public PermissionSet getAllPermissions() throws DataBackendException
    {
        PermissionSet permissionSet = new PermissionSet();
        Connection con = null;

        try
        {
            con = Transaction.begin();

            List<Permission> permissions = doSelectAllPermissions(con);

            for (Permission p : permissions)
            {
                // Add attached objects if they exist
                ((TorqueAbstractSecurityEntity)p).retrieveAttachedObjects(con);
                permissionSet.add(p);
            }

            Transaction.commit(con);
            con = null;
        }
        catch (TorqueException e)
        {
            throw new DataBackendException("Error retrieving permission information", e);
        }
        finally
        {
            if (con != null)
            {
                Transaction.safeRollback(con);
            }
        }

        return permissionSet;
    }

    /**
     * Determines if the <code>Permission</code> exists in the security
     * system.
     *
     * @param permissionName
     *            a <code>Permission</code> value
     * @return true if the permission name exists in the system, false otherwise
     * @throws DataBackendException
     *             when more than one Permission with the same name exists.
     */
    @Override
	public boolean checkExists(String permissionName) throws DataBackendException
    {
        boolean exists = false;

        Connection con = null;

        try
        {
            con = Transaction.begin();

            doSelectByName(permissionName, con);

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
            throw new DataBackendException("Multiple permissions with same name '" + permissionName + "'");
        }
        catch (TorqueException e)
        {
            throw new DataBackendException("Error retrieving permission information", e);
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
     * Retrieve a Permission object with specified id.
     *
     * @param id
     *            the id of the Permission.
     * @return an object representing the Permission with specified id.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the permission does not exist.
     */
    @Override
	public <T extends Permission> T getPermissionById(Object id) throws DataBackendException, UnknownEntityException
    {
        T permission;

        if (id != null && id instanceof Integer)
        {
            Connection con = null;

            try
            {
                con = Transaction.begin();

                permission = doSelectById((Integer)id, con);

                // Add attached objects if they exist
                ((TorqueAbstractSecurityEntity)permission).retrieveAttachedObjects(con);

                Transaction.commit(con);
                con = null;
            }
            catch (NoRowsException e)
            {
                throw new UnknownEntityException("Permission with id '" + id + "' does not exist.", e);
            }
            catch (TorqueException e)
            {
                throw new DataBackendException("Error retrieving permission information", e);
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
            throw new UnknownEntityException("Invalid permission id '" + id + "'");
        }

        return permission;
    }

    /**
     * Retrieve a Permission object with specified name.
     *
     * @param name the name of the Group.
     * @return an object representing the Group with specified name.
     * @throws DataBackendException if there was an error accessing the
     *         data backend.
     * @throws UnknownEntityException if the group does not exist.
     */
    @Override
	public <T extends Permission> T getPermissionByName(String name) throws DataBackendException, UnknownEntityException
    {
        T permission = null;
        Connection con = null;

        try
        {
            con = Transaction.begin();

            permission = doSelectByName(name, con);

            // Add attached objects if they exist
            ((TorqueAbstractSecurityEntity)permission).retrieveAttachedObjects(con);

            Transaction.commit(con);
            con = null;
        }
        catch (NoRowsException e)
        {
            throw new UnknownEntityException("Could not find permission " + name);
        }
        catch (TooManyRowsException e)
        {
            throw new DataBackendException("Multiple Permissions with same name '" + name + "'");
        }
        catch (TorqueException e)
        {
            throw new DataBackendException("Error retrieving permission information", e);
        }
        finally
        {
            if (con != null)
            {
                Transaction.safeRollback(con);
            }
        }

        return permission;
    }
}
