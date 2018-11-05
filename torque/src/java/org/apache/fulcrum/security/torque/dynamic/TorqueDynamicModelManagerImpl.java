package org.apache.fulcrum.security.torque.dynamic;

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
import org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Transaction;

/**
 * This implementation persists to a database via Torque.
 *
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id:$
 */
public class TorqueDynamicModelManagerImpl extends AbstractDynamicModelManager implements DynamicModelManager {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -4102444603078107928L;

	/**
	 * Revokes a Role from a Group.
	 *
	 * @param group the Group.
	 * @param role  the Role.
	 * @throws DataBackendException   if there was an error accessing the data
	 *                                backend.
	 * @throws UnknownEntityException if group or role is not present.
	 */
	@Override
	public synchronized void revoke(Group group, Role role) throws DataBackendException, UnknownEntityException {
		boolean groupExists = getGroupManager().checkExists(group);
		boolean roleExists = getRoleManager().checkExists(role);

		if (groupExists && roleExists) {
			((DynamicGroup) group).removeRole(role);
			((DynamicRole) role).removeGroup(group);

			Connection con = null;

			try {
				con = Transaction.begin();

				((TorqueAbstractSecurityEntity) role).update(con);
				((TorqueAbstractSecurityEntity) group).update(con);

				Transaction.commit(con);
				con = null;
			} catch (TorqueException e) {
				throw new DataBackendException("revoke('" + group.getName() + "', '" + role.getName() + "') failed", e);
			} finally {
				if (con != null) {
					Transaction.safeRollback(con);
				}
			}

			return;
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
	@Override
	public synchronized void grant(Role role, Permission permission)
			throws DataBackendException, UnknownEntityException {
		boolean roleExists = getRoleManager().checkExists(role);
		boolean permissionExists = getPermissionManager().checkExists(permission);

		if (roleExists && permissionExists) {
			((DynamicRole) role).addPermission(permission);
			((DynamicPermission) permission).addRole(role);

			Connection con = null;

			try {
				con = Transaction.begin();

				((TorqueAbstractSecurityEntity) role).update(con);
				((TorqueAbstractSecurityEntity) permission).update(con);

				Transaction.commit(con);
				con = null;
			} catch (TorqueException e) {
				throw new DataBackendException("grant('" + role.getName() + "', '" + permission.getName() + "') failed",
						e);
			} finally {
				if (con != null) {
					Transaction.safeRollback(con);
				}
			}

			return;
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
	@Override
	public synchronized void revoke(Role role, Permission permission)
			throws DataBackendException, UnknownEntityException {
		boolean roleExists = getRoleManager().checkExists(role);
		boolean permissionExists = getPermissionManager().checkExists(permission);

		if (roleExists && permissionExists) {
			((DynamicRole) role).removePermission(permission);
			((DynamicPermission) permission).removeRole(role);

			Connection con = null;

			try {
				con = Transaction.begin();
				;

				((TorqueAbstractSecurityEntity) role).update(con);
				((TorqueAbstractSecurityEntity) permission).update(con);

				Transaction.commit(con);
				con = null;
			} catch (TorqueException e) {
				throw new DataBackendException(
						"revoke('" + role.getName() + "', '" + permission.getName() + "') failed", e);
			} finally {
				if (con != null) {
					Transaction.safeRollback(con);
				}
			}

			return;
		}

		if (!roleExists) {
			throw new UnknownEntityException("Unknown role '" + role.getName() + "'");
		}

		if (!permissionExists) {
			throw new UnknownEntityException("Unknown permission '" + permission.getName() + "'");
		}
	}

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
	@Override
	public synchronized void grant(User user, Group group) throws DataBackendException, UnknownEntityException {
		boolean groupExists = getGroupManager().checkExists(group);
		boolean userExists = getUserManager().checkExists(user);

		if (groupExists && userExists) {
			((DynamicUser) user).addGroup(group);
			((DynamicGroup) group).addUser(user);

			Connection con = null;

			try {
				con = Transaction.begin();

				((TorqueAbstractSecurityEntity) user).update(con);
				((TorqueAbstractSecurityEntity) group).update(con);

				Transaction.commit(con);
				con = null;
			} catch (TorqueException e) {
				throw new DataBackendException("grant('" + user.getName() + "', '" + group.getName() + "') failed", e);
			} finally {
				if (con != null) {
					Transaction.safeRollback(con);
				}
			}

			return;
		}

		if (!groupExists) {
			throw new UnknownEntityException("Unknown group '" + group.getName() + "'");
		}

		if (!userExists) {
			throw new UnknownEntityException("Unknown user '" + user.getName() + "'");
		}
	}

	/**
	 * Removes a user in a group.
	 *
	 * This method is used when removing a user to a group
	 *
	 * @param user  the User.
	 * @param group the Group
	 * @throws DataBackendException   if there was an error accessing the data
	 *                                backend.
	 * @throws UnknownEntityException if the user or group is not present.
	 */
	@Override
	public synchronized void revoke(User user, Group group) throws DataBackendException, UnknownEntityException {
		boolean groupExists = getGroupManager().checkExists(group);
		boolean userExists = getUserManager().checkExists(user);

		if (groupExists && userExists) {
			((DynamicUser) user).removeGroup(group);
			((DynamicGroup) group).removeUser(user);

			Connection con = null;

			try {
				con = Transaction.begin();

				((TorqueAbstractSecurityEntity) user).update(con);
				((TorqueAbstractSecurityEntity) group).update(con);

				Transaction.commit(con);
				con = null;
			} catch (TorqueException e) {
				throw new DataBackendException("revoke('" + user.getName() + "', '" + group.getName() + "') failed", e);
			} finally {
				if (con != null) {
					Transaction.safeRollback(con);
				}
			}

			return;
		}

		if (!groupExists) {
			throw new UnknownEntityException("Unknown group '" + group.getName() + "'");
		}

		if (!userExists) {
			throw new UnknownEntityException("Unknown user '" + user.getName() + "'");
		}
	}

	/**
	 * Grants a Group a Role
	 *
	 * @param group the Group.
	 * @param role  the Role.
	 * @throws DataBackendException   if there was an error accessing the data
	 *                                backend.
	 * @throws UnknownEntityException if group or role is not present.
	 */
	@Override
	public synchronized void grant(Group group, Role role) throws DataBackendException, UnknownEntityException {
		boolean groupExists = getGroupManager().checkExists(group);
		boolean roleExists = getRoleManager().checkExists(role);

		if (groupExists && roleExists) {
			((DynamicGroup) group).addRole(role);
			((DynamicRole) role).addGroup(group);

			Connection con = null;

			try {
				con = Transaction.begin();

				((TorqueAbstractSecurityEntity) role).update(con);
				((TorqueAbstractSecurityEntity) group).update(con);

				Transaction.commit(con);
				con = null;
			} catch (TorqueException e) {
				throw new DataBackendException("grant('" + group.getName() + "', '" + role.getName() + "') failed", e);
			} finally {
				if (con != null) {
					Transaction.safeRollback(con);
				}
			}

			return;
		}

		if (!groupExists) {
			throw new UnknownEntityException("Unknown group '" + group.getName() + "'");
		}

		if (!roleExists) {
			throw new UnknownEntityException("Unknown role '" + role.getName() + "'");
		}
	}

	/**
	 * Allow B to assumes A's roles, groups and permissions
	 * 
	 * @param delegator A
	 * @param delegatee B
	 * @throws DataBackendException   if there was an error accessing the data
	 *                                backend.
	 * @throws UnknownEntityException if delegator or delagatee is not present.
	 */
	@Override
	public synchronized void addDelegate(User delegator, User delegatee)
			throws DataBackendException, UnknownEntityException {
		boolean delegatorExists = getUserManager().checkExists(delegator);
		boolean delegateeExists = getUserManager().checkExists(delegatee);

		if (delegatorExists && delegateeExists) {
			super.addDelegate(delegator, delegatee);

			Connection con = null;

			try {
				con = Transaction.begin();

				((TorqueAbstractSecurityEntity) delegator).update(con);
				((TorqueAbstractSecurityEntity) delegatee).update(con);

				Transaction.commit(con);
				con = null;
			} catch (TorqueException e) {
				throw new DataBackendException(
						"addDelegate('" + delegator.getName() + "', '" + delegatee.getName() + "') failed", e);
			} finally {
				if (con != null) {
					Transaction.safeRollback(con);
				}
			}

			return;
		}

		if (!delegatorExists) {
			throw new UnknownEntityException("Unknown user '" + delegator.getName() + "'");
		}

		if (!delegateeExists) {
			throw new UnknownEntityException("Unknown user '" + delegatee.getName() + "'");
		}
	}

	/**
	 * Stop A having B's roles, groups and permissions
	 * 
	 * @param delegator  A
	 * @param delegatee  B
	 * 
	 * @throws DataBackendException   if there was an error accessing the data
	 *                                backend.
	 * @throws UnknownEntityException if delegator or delagatee is not present.
	 */
	@Override
	public synchronized void removeDelegate(User delegator, User delegatee)
			throws DataBackendException, UnknownEntityException {
		boolean delegatorExists = getUserManager().checkExists(delegator);
		boolean delegateeExists = getUserManager().checkExists(delegatee);

		if (delegatorExists && delegateeExists) {
			super.removeDelegate(delegator, delegatee);

			Connection con = null;

			try {
				con = Transaction.begin();

				((TorqueAbstractSecurityEntity) delegator).update(con);
				((TorqueAbstractSecurityEntity) delegatee).update(con);

				Transaction.commit(con);
				con = null;
			} catch (TorqueException e) {
				throw new DataBackendException(
						"removeDelegate('" + delegator.getName() + "', '" + delegatee.getName() + "') failed", e);
			} finally {
				if (con != null) {
					Transaction.safeRollback(con);
				}
			}

			return;
		}

		if (!delegatorExists) {
			throw new UnknownEntityException("Unknown user '" + delegator.getName() + "'");
		}

		if (!delegateeExists) {
			throw new UnknownEntityException("Unknown user '" + delegatee.getName() + "'");
		}
	}
}
