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

import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.RoleSet;
import org.apache.fulcrum.security.util.UnknownEntityException;

/**
 * A RoleManager performs {@link org.apache.fulcrum.security.entity.Role}
 * objects related tasks on behalf of the
 * {@link org.apache.fulcrum.security.BaseSecurityService}.
 *
 * The responsibilities of this class include loading data of a role from the
 * storage and putting them into the
 * {@link org.apache.fulcrum.security.entity.Role} objects, saving those data
 * to the permanent storage.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public interface RoleManager extends Serializable
{
    /** Avalon role - used to id the component within the manager */
    String ROLE = RoleManager.class.getName();

    /**
     * Construct a blank Role object
     *
     * This method calls getRoleClass, and then creates a new object using the
     * default constructor.
     * 
     * @return an object implementing Role interface.
     * @throws DataBackendException if the object could not be instantiated
     */
    <T extends Role> T getRoleInstance() throws DataBackendException;

    /**
     * Construct a blank Role object.
     *
     * This method calls getRoleClass, and then creates a new object using the
     * default constructor.
     *
     * @param roleName
     *            The name of the Role
     *
     * @return an object implementing Role interface.
     * @throws DataBackendException
     *             if the object could not be instantiated.
     */
    <T extends Role> T getRoleInstance(String roleName) throws DataBackendException;

    /**
     * Retrieve a Role object with specified name.
     *
     * @param name
     *            the name of the Role.
     * @return an object representing the Role with specified name.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the role does not exist.
     */
    <T extends Role> T getRoleByName(String name) throws DataBackendException, UnknownEntityException;

    /**
     * Retrieve a Role object with specified Id.
     *
     * @param id
     *            the Id of the Role.
     *
     * @return an object representing the Role with specified name.
     *
     * @exception UnknownEntityException
     *                if the permission does not exist in the database.
     * @exception DataBackendException
     *                if there is a problem accessing the storage.
     */
    <T extends Role> T getRoleById(Object id) throws DataBackendException, UnknownEntityException;

    /**
     * Retrieves all roles defined in the system.
     *
     * @return the names of all roles defined in the system.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     */
    RoleSet getAllRoles() throws DataBackendException;

    /**
     * Creates a new role with specified attributes.
     *
     * @param role
     *            The object describing the role to be created.
     * @return the new Role object.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws EntityExistsException
     *             if the role already exists.
     */
    <T extends Role> T addRole(T role) throws DataBackendException, EntityExistsException;

    /**
     * Removes a Role from the system.
     *
     * @param role
     *            The object describing the role to be removed.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the role does not exist.
     */
    void removeRole(Role role) throws DataBackendException, UnknownEntityException;

    /**
     * Renames an existing Role.
     *
     * @param role
     *            The object describing the role to be renamed.
     * @param name
     *            the new name for the role.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the role does not exist.
     */
    void renameRole(Role role, String name) throws DataBackendException, UnknownEntityException;

    /**
     * Determines if the <code>Role</code> exists in the security system.
     *
     * @param role
     *            a <code>Role</code> value
     * @return true if the role exists in the system, false otherwise
     * @throws DataBackendException
     *             when more than one Role with the same name exists.
     */
    boolean checkExists(Role role) throws DataBackendException;

    /**
     * Determines if a <code>Role</code> exists in the security system with the
     * specified role name.
     *
     * @param roleName
     *            the name of a <code>Role</code> to check.
     * @return true if the role exists in the system, false otherwise
     * @throws DataBackendException
     *             when more than one Role with the same name exists.
     */
    boolean checkExists(String roleName) throws DataBackendException;
}
