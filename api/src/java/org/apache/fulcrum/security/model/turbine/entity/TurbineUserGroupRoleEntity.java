package org.apache.fulcrum.security.model.turbine.entity;

import java.io.Serializable;

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

import java.util.Set;

import org.apache.fulcrum.security.util.DataBackendException;

/**
 * Represents the "turbine" model where permissions are in a many to many
 * relationship to roles, roles are related to groups are related to users, all
 * in many to many relationships.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh </a>
 * @version $Id: TurbineRole.java 535465 2007-05-05 06:58:06Z tv $
 */
public interface TurbineUserGroupRoleEntity extends Serializable {

	/**
	 * Get the User/Group/Role set associated with this entity
	 * 
	 * @param <T> TurbineUserGroupRole
	 * @return a set of User/Group/Role relations
	 * @throws DataBackendException generic exception
	 */
	public <T extends TurbineUserGroupRole> Set<T> getUserGroupRoleSet() throws DataBackendException;

	/**
	 * Set the User/Group/Role set associated with this entity
	 * 
	 * @param userGroupRoleSet a set of User/Group/Role relations
	 * @param <T> TurbineUserGroupRole
	 */
	public <T extends TurbineUserGroupRole> void setUserGroupRoleSet(Set<T> userGroupRoleSet);

	/**
	 * Add a User/Group/Role relation to this entity
	 *
	 * @param userGroupRole a User/Group/Role relation to add
	 * @throws DataBackendException generic exception
	 */
	public void addUserGroupRole(TurbineUserGroupRole userGroupRole) throws DataBackendException;

	/**
	 * Remove a User/Group/Role relation from this entity
	 *
	 * @param userGroupRole a User/Group/Role relation to remove
	 * @throws DataBackendException generic exception
	 */
	public void removeUserGroupRole(TurbineUserGroupRole userGroupRole) throws DataBackendException;
}
