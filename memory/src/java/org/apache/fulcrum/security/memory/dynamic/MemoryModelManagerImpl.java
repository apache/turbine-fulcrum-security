package org.apache.fulcrum.security.memory.dynamic;

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
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.dynamic.AbstractDynamicModelManager;
import org.apache.fulcrum.security.model.dynamic.DynamicModelManager;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicGroup;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicPermission;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicRole;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicUser;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.UnknownEntityException;

/**
 * This implementation keeps all objects in memory. This is mostly meant to help
 * with testing and prototyping of ideas.
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class MemoryModelManagerImpl extends AbstractDynamicModelManager implements DynamicModelManager {
	/**
	 * Puts a user in a group.
	 * 
	 * This method is used when adding a user to a group
	 * 
	 * @param user  the User.
	 * @param group the Group
	 * @throws DataBackendException   if there was an error accessing the data
	 *                                backend.
	 * @throws UnknownEntityException if the account is not present.
	 */
	public void grant(User user, Group group) throws DataBackendException, UnknownEntityException {
		boolean groupExists = false;
		boolean userExists = false;
		try {
			groupExists = getGroupManager().checkExists(group);
			userExists = getUserManager().checkExists(user);
			if (groupExists && userExists) {
				((DynamicUser) user).addGroup(group);
				((DynamicGroup) group).addUser(user);
				return;
			}
		} catch (Exception e) {
			throw new DataBackendException("grant(Role,Permission) failed", e);
		}

		if (!groupExists) {
			throw new UnknownEntityException("Unknown group '" + group.getName() + "'");
		}
		if (!userExists) {
			throw new UnknownEntityException("Unknown user '" + user.getName() + "'");
		}
	}

	/**
	 * Revokes a user from a group
	 * 
	 * @param user  the User.
	 * @param group the Group
	 * @throws DataBackendException   if there was an error accessing the data
	 *                                backend
	 * @throws UnknownEntityException if the user or group is not present.
	 */
	public void revoke(User user, Group group) throws DataBackendException, UnknownEntityException {
		boolean groupExists = false;
		boolean userExists = false;
		try {
			groupExists = getGroupManager().checkExists(group);
			userExists = getUserManager().checkExists(user);
			if (groupExists && userExists) {
				((DynamicUser) user).removeGroup(group);
				((DynamicGroup) group).removeUser(user);
				return;
			}
		} catch (Exception e) {
			throw new DataBackendException("grant(Role,Permission) failed", e);
		}

		if (!groupExists) {
			throw new UnknownEntityException("Unknown group '" + group.getName() + "'");
		}
		if (!userExists) {
			throw new UnknownEntityException("Unknown user '" + user.getName() + "'");
		}
	}

	/**
	 * Grants a Role to a Group
	 * 
	 * @param group the Group.
	 * @param role  the Role.
	 * @throws DataBackendException   if there was an error accessing the data
	 *                                backend.
	 * @throws UnknownEntityException if group or role is not present.
	 */
	public synchronized void grant(Group group, Role role) throws DataBackendException, UnknownEntityException {
		boolean groupExists = false;
		boolean roleExists = false;
		try {
			groupExists = getGroupManager().checkExists(group);
			roleExists = getRoleManager().checkExists(role);
			if (groupExists && roleExists) {
				((DynamicGroup) group).addRole(role);
				((DynamicRole) role).addGroup(group);
				return;
			}
		} catch (Exception e) {
			throw new DataBackendException("grant(Group,Role) failed", e);
		}
		if (!groupExists) {
			throw new UnknownEntityException("Unknown group '" + group.getName() + "'");
		}
		if (!roleExists) {
			throw new UnknownEntityException("Unknown role '" + role.getName() + "'");
		}
	}

	/**
	 * Revokes a Role from a Group.
	 * 
	 * @param group the Group.
	 * @param role  the Role.
	 * @throws DataBackendException   if there was an error accessing the data
	 *                                backend.
	 * @throws UnknownEntityException if group or role is not present.
	 */
	public synchronized void revoke(Group group, Role role) throws DataBackendException, UnknownEntityException {
		boolean groupExists = false;
		boolean roleExists = false;
		try {
			groupExists = getGroupManager().checkExists(group);
			roleExists = getRoleManager().checkExists(role);
			if (groupExists && roleExists) {
				((DynamicGroup) group).removeRole(role);
				((DynamicRole) role).removeGroup(group);
				return;
			}
		} catch (Exception e) {
			throw new DataBackendException("revoke(Group,Role) failed", e);
		}

		if (!groupExists) {
			throw new UnknownEntityException("Unknown group '" + group.getName() + "'");
		}
		if (!roleExists) {
			throw new UnknownEntityException("Unknown role '" + role.getName() + "'");
		}
	}

	/**
	 * Grants a Role a Permission
	 * 
	 * @param role       the Role.
	 * @param permission the Permission.
	 * @throws DataBackendException   if there was an error accessing the data
	 *                                backend.
	 * @throws UnknownEntityException if role or permission is not present.
	 */
	public synchronized void grant(Role role, Permission permission)
			throws DataBackendException, UnknownEntityException {
		boolean roleExists = false;
		boolean permissionExists = false;
		try {
			roleExists = getRoleManager().checkExists(role);
			permissionExists = getPermissionManager().checkExists(permission);
			if (roleExists && permissionExists) {
				((DynamicRole) role).addPermission(permission);
				((DynamicPermission) permission).addRole(role);
				return;
			}
		} catch (Exception e) {
			throw new DataBackendException("grant(Role,Permission) failed", e);
		}

		if (!roleExists) {
			throw new UnknownEntityException("Unknown role '" + role.getName() + "'");
		}
		if (!permissionExists) {
			throw new UnknownEntityException("Unknown permission '" + permission.getName() + "'");
		}
	}

	/**
	 * Revokes a Permission from a Role.
	 * 
	 * @param role       the Role.
	 * @param permission the Permission.
	 * @throws DataBackendException   if there was an error accessing the data
	 *                                backend.
	 * @throws UnknownEntityException if role or permission is not present.
	 */
	public synchronized void revoke(Role role, Permission permission)
			throws DataBackendException, UnknownEntityException {
		boolean roleExists = false;
		boolean permissionExists = false;
		try {
			roleExists = getRoleManager().checkExists(role);
			permissionExists = getPermissionManager().checkExists(permission);
			if (roleExists && permissionExists) {
				((DynamicRole) role).removePermission(permission);
				((DynamicPermission) permission).removeRole(role);
				return;
			}
		} catch (Exception e) {
			throw new DataBackendException("revoke(Role,Permission) failed", e);
		}

		if (!roleExists) {
			throw new UnknownEntityException("Unknown role '" + role.getName() + "'");
		}
		if (!permissionExists) {
			throw new UnknownEntityException("Unknown permission '" + permission.getName() + "'");
		}
	}

}
