package org.apache.fulcrum.security.hibernate;
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
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;

import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.spi.AbstractPermissionManager;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.PermissionSet;
import org.apache.fulcrum.security.util.UnknownEntityException;
/**
 * This implementation persists to a database via Hibernate.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class HibernatePermissionManagerImpl extends AbstractPermissionManager
{
	private PersistenceHelper persistenceHelper;

    /**
    * Retrieves all permissions defined in the system.
    *
    * @return the names of all roles defined in the system.
    * @throws DataBackendException if there was an error accessing the
    *         data backend.
    */
    public PermissionSet getAllPermissions() throws DataBackendException
    {
        PermissionSet permissionSet = new PermissionSet();
        try
        {

            List permissions = getPersistenceHelper().retrieveSession().find("from " + Permission.class.getName() + "");
            permissionSet.add(permissions);

        }
        catch (HibernateException e)
        {
            throw new DataBackendException("Error retriving permission information", e);
        }
        return permissionSet;
    }
    /**
    * Renames an existing Permission.
    *
    * @param permission The object describing the permission to be renamed.
    * @param name the new name for the permission.
    * @throws DataBackendException if there was an error accessing the data
    *         backend.
    * @throws UnknownEntityException if the permission does not exist.
    */
    public synchronized void renamePermission(Permission permission, String name)
        throws DataBackendException, UnknownEntityException
    {
        boolean permissionExists = false;
        permissionExists = checkExists(permission);
        if (permissionExists)
        {
            permission.setName(name);
			getPersistenceHelper().updateEntity(permission);
            return;
        }
        else
        {
            throw new UnknownEntityException("Unknown permission '" + permission + "'");
        }
    }
    /**
    * Determines if the <code>Permission</code> exists in the security system.
    *
    * @param permissionName a <code>Permission</code> value
    * @return true if the permission name exists in the system, false otherwise
    * @throws DataBackendException when more than one Permission with
    *         the same name exists.
    */
    public boolean checkExists(String permissionName) throws DataBackendException
    {
        List permissions;
        try
        {

            permissions =
			getPersistenceHelper().retrieveSession().find("from " + Permission.class.getName() + " sp where sp.name=?", permissionName, Hibernate.STRING);

        }
        catch (HibernateException e)
        {
            throw new DataBackendException("Error retriving permission information", e);
        }
        if (permissions.size() > 1)
        {
            throw new DataBackendException("Multiple permissions with same name '" + permissionName + "'");
        }
        return (permissions.size() == 1);
    }
    /**
     * Removes a Permission from the system.
     *
     * @param permission The object describing the permission to be removed.
     * @throws DataBackendException if there was an error accessing the data
     *         backend.
     * @throws UnknownEntityException if the permission does not exist.
     */
    public synchronized void removePermission(Permission permission)
        throws DataBackendException, UnknownEntityException
    {
        boolean permissionExists = false;
        permissionExists = checkExists(permission);
        if (permissionExists)
        {
			getPersistenceHelper().removeEntity(permission);
        }
        else
        {
            throw new UnknownEntityException("Unknown permission '" + permission + "'");
        }
    }
    /**
    * Creates a new permission with specified attributes.
    *
    * @param permission the object describing the permission to be created.
    * @return a new Permission object that has id set up properly.
    * @throws DataBackendException if there was an error accessing the data
    *         backend.
    * @throws EntityExistsException if the permission already exists.
    */
    protected synchronized Permission persistNewPermission(Permission permission)
        throws DataBackendException
    {

		getPersistenceHelper().addEntity(permission);
        return permission;
    }

	/**
	 * @return Returns the persistenceHelper.
	 */
	public PersistenceHelper getPersistenceHelper() throws DataBackendException
	{
		if (persistenceHelper == null)
		{
			persistenceHelper = (PersistenceHelper)resolve(PersistenceHelper.ROLE);
		}
		return persistenceHelper;
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
	public Permission getPermissionById(Object id)
	throws DataBackendException, UnknownEntityException {

		Permission permission = null;

		if (id != null)
			try {
				List permissions =
					getPersistenceHelper().retrieveSession().find(
							"from " + Permission.class.getName() + " sp where sp.id=?",
							id,
							Hibernate.LONG);
				if (permissions.size() == 0) {
					throw new UnknownEntityException(
							"Could not find permission by id " + id);
				}
				permission = (Permission) permissions.get(0);

			} catch (HibernateException e) {
				throw new DataBackendException(
						"Error retriving permission information",
						e);
			}

		return permission;
	}
}
