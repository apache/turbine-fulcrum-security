package org.apache.fulcrum.security;

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
import java.io.Serializable;

import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.PermissionSet;
import org.apache.fulcrum.security.util.UnknownEntityException;

/**
 * A PermissionManager performs {@link org.apache.fulcrum.security.entity.Permission}
 * objects related tasks on behalf of the
 * {@link org.apache.fulcrum.security.BaseSecurityService}.
 *
 * The responsibilities of this class include loading data of an permission from the
 * storage and putting them into the
 * {@link org.apache.fulcrum.security.entity.Permission} objects, saving those data
 * to the permanent storage.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public interface PermissionManager extends Serializable
{

    /** Avalon role - used to id the component within the manager */
    String ROLE = PermissionManager.class.getName();

    /**
     * Construct a blank Permission object.
     *
     * This method calls getPermissionClass, and then creates a new object using
     * the default constructor.
     *
     * @param <T>
     * @return an object implementing Permission interface.
     * @throws DataBackendException
     * 				if there was an error accessing the data backend. 
     */
    <T extends Permission> T getPermissionInstance() throws DataBackendException;

    /**
     * Construct a blank Permission object.
     *
     * This method calls getPermissionClass, and then creates a new object using
     * the default constructor.
     * 
     * @param <T>
     * @param permName
     *            The name of the Permission
     *
     * @return an object implementing Permission interface.
     * @throws DataBackendException
     * 				if there was an error accessing the data backend. 
     */
    <T extends Permission> T getPermissionInstance(String permName) throws DataBackendException;

    /**
     * Retrieve a Permission object with specified name.
     * 
     * @param <T>
     * @param name
     *            the name of the Permission.
     * @return an object representing the Permission with specified name.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the permission does not exist.
     */
    <T extends Permission> T getPermissionByName(String name) throws DataBackendException, UnknownEntityException;

    /**
     * Retrieve a Permission object with specified Id.
     * 
     * @param <T>
     *
     * @param id
     *            the Id of the Permission.
     *
     * @return an object representing the Permission with specified name.
     *
     * @exception UnknownEntityException
     *                if the permission does not exist in the database.
     * @exception DataBackendException
     *                if there is a problem accessing the storage.
     */
    <T extends Permission> T getPermissionById(Object id) throws DataBackendException, UnknownEntityException;

    /**
     * Retrieves all permissions defined in the system.
     *
     * @return the names of all permissions defined in the system.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     */
    PermissionSet getAllPermissions() throws DataBackendException;

    /**
     * Creates a new permission with specified attributes.
     *
     * @param permission
     *            The object describing the permission to be created.
     * @return the new Permission object.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws EntityExistsException
     *             if the permission already exists.
     */
    <T extends Permission> T addPermission(T permission) throws DataBackendException, EntityExistsException;

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
    void removePermission(Permission permission) throws DataBackendException, UnknownEntityException;

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
    void renamePermission(Permission permission, String name) throws DataBackendException, UnknownEntityException;

    /**
     * Determines if the <code>Permission</code> exists in the security system.
     *
     * @param permission
     *            a <code>Permission</code> value
     * @return true if the permission exists in the system, false otherwise
     * @throws DataBackendException
     *             when more than one Permission with the same name exists.
     */
    boolean checkExists(Permission permission) throws DataBackendException;

    /**
     * Determines if a <code>Permission</code> exists in the security system
     * with the specified name.
     *
     * @param permissionName
     *            the name of a <code>Permission</code> to check
     * @return true if the permission exists in the system, false otherwise
     * @throws DataBackendException
     *             when more than one Permission with the same name exists.
     */
    boolean checkExists(String permissionName) throws DataBackendException;
}
