package org.apache.fulcrum.security.model.turbine;

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

import org.apache.fulcrum.security.ModelManager;
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.UnknownEntityException;

/**
 * Describes all the relationships between entities in the "Turbine" model.
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public interface TurbineModelManager extends ModelManager
{


	/**
	 * attribute where global group name could be set 
	 */
    String GLOBAL_GROUP_ATTR_NAME = "globalGroup";
    
    /**
     * The name of the <a href="#global">global group</a>, if no global group name is set in model manager
     */
    public String GLOBAL_GROUP_NAME = "global";

    /**
     * Provides a reference to the Group object that represents the <a
     * href="#global">global group</a>.
     * 
     * @return A Group object that represents the global group.
     */
    Group getGlobalGroup() throws DataBackendException;
    
    /**
     * 
     * @return the configured global group name, by default {@link #GLOBAL_GROUP_ATTR_NAME}
     */
    public String getGlobalGroupName();

    /**
     * Puts a permission in a role
     * 
     * This method is used when adding a permission to a role
     * 
     * @param user
     *            the User.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the account is not present.
     */
    void grant(Role role, Permission permission) throws DataBackendException, UnknownEntityException;

    /**
     * Removes a permission from a role
     * 
     * @param role
     *            the Role.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the user or group is not present.
     */
    void revoke(Role role, Permission permission) throws DataBackendException, UnknownEntityException;

    /**
     * Revokes all roles from an User.
     * 
     * This method is typically used when deleting an account.
     * 
     * @param user
     *            the User.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the account is not present.
     */
    void revokeAll(User user) throws DataBackendException, UnknownEntityException;

    /**
     * Revokes all permissions from a Role.
     * 
     * This method is typically used when deleting a Role.
     * 
     * @param role
     *            the Role
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the Role is not present.
     */
    void revokeAll(Role role) throws DataBackendException, UnknownEntityException;

    /**
     * Grant an User a Role in a Group.
     * 
     * @param user
     *            the user.
     * @param group
     *            the group.
     * @param role
     *            the role.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if user account, group or role is not present.
     */
    void grant(User user, Group group, Role role) throws DataBackendException, UnknownEntityException;

    /**
     * Revoke a Role in a Group from an User.
     * 
     * @param user
     *            the user.
     * @param group
     *            the group.
     * @param role
     *            the role.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if user account, group or role is not present.
     */
    void revoke(User user, Group group, Role role) throws DataBackendException, UnknownEntityException;
}
