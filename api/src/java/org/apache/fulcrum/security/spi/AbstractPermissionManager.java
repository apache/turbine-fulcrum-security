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
import org.apache.commons.lang.StringUtils;
import org.apache.fulcrum.security.PermissionManager;
import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.UnknownEntityException;

/**
 * This implementation keeps all objects in memory. This is mostly meant to help
 * with testing and prototyping of ideas.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id: AbstractPermissionManager.java 1372918 2012-08-14 15:19:40Z tv
 *          $
 */
public abstract class AbstractPermissionManager extends AbstractEntityManager implements PermissionManager
{
    protected abstract <T extends Permission> T persistNewPermission(T permission) throws DataBackendException;

    /**
     * Construct a blank Permission object.
     *
     * This method calls getPermissionClass, and then creates a new object using
     * the default constructor.
     *
     * @return an object implementing Permission interface.
     * @throws UnknownEntityException
     *             if the object could not be instantiated.
     */
    public <T extends Permission> T getPermissionInstance() throws UnknownEntityException
    {
        try
        {
            @SuppressWarnings("unchecked")
			T permission = (T) Class.forName(getClassName()).newInstance();
            return permission;
        }
        catch (Exception e)
        {
            throw new UnknownEntityException("Failed to instantiate a Permission implementation object", e);
        }
    }

    /**
     * Construct a blank Permission object.
     *
     * This method calls getPermissionClass, and then creates a new object using
     * the default constructor.
     *
     * @param permName
     *            The name of the permission.
     *
     * @return an object implementing Permission interface.
     * @throws UnknownEntityException
     *             if the object could not be instantiated.
     */
    public <T extends Permission> T getPermissionInstance(String permName) throws UnknownEntityException
    {
        T perm = getPermissionInstance();
        perm.setName(permName);
        return perm;
    }

    /**
     * Retrieve a Permission object with specified name.
     *
     * @param name
     *            the name of the Permission.
     * @return an object representing the Permission with specified name.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the permission does not exist.
     */
    public <T extends Permission> T getPermissionByName(String name) throws DataBackendException, UnknownEntityException
    {
        @SuppressWarnings("unchecked")
		T permission = (T) getAllPermissions().getByName(name);
        if (permission == null)
        {
            throw new UnknownEntityException("The specified permission does not exist");
        }
        return permission;
    }

    /**
     * Retrieve a Permission object with specified Id.
     *
     * @param name
     *            the name of the Permission.
     *
     * @return an object representing the Permission with specified name.
     *
     * @throws UnknownEntityException
     *             if the permission does not exist in the database.
     * @throws DataBackendException
     *             if there is a problem accessing the storage.
     */
    public <T extends Permission> T getPermissionById(Object id) throws DataBackendException, UnknownEntityException
    {
        @SuppressWarnings("unchecked")
		T permission = (T) getAllPermissions().getById(id);
        if (permission == null)
        {
            throw new UnknownEntityException("The specified permission does not exist");
        }
        return permission;
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
    public synchronized <T extends Permission> T addPermission(T permission) throws DataBackendException, EntityExistsException
    {
        boolean permissionExists = false;
        if (StringUtils.isEmpty(permission.getName()))
        {
            throw new DataBackendException("Could not create a permission with empty name!");
        }
        if (permission.getId() != null)
        {
            throw new DataBackendException("Could not create a permission with an id!");
        }
        try
        {
            permissionExists = checkExists(permission);
            if (!permissionExists)
            {
                return persistNewPermission(permission);
            }
        }
        catch (Exception e)
        {
            throw new DataBackendException("addPermission(Permission) failed", e);
        }
        // the only way we could get here without return/throw tirggered
        // is that the permissionExists was true.
        throw new EntityExistsException("Permission '" + permission + "' already exists");
    }

    /**
     * Check whether a specified permission exists.
     *
     * The name is used for looking up the permission
     *
     * @param role
     *            The permission to be checked.
     * @return true if the specified permission exists
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     */
    public boolean checkExists(Permission permission) throws DataBackendException
    {
        return checkExists(permission.getName());
    }

}
