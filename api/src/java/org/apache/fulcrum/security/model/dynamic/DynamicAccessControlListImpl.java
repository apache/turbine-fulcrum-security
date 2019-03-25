package org.apache.fulcrum.security.model.dynamic;

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

import java.util.Map;

import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.util.GroupSet;
import org.apache.fulcrum.security.util.PermissionSet;
import org.apache.fulcrum.security.util.RoleSet;

/**
 * This is a control class that makes it easy to find out if a particular User
 * has a given Permission. It also determines if a User has a a particular Role.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class DynamicAccessControlListImpl implements DynamicAccessControlList 
{
	// TODO Need to rethink the two maps.. Why not just a single list of groups?
	// That would then cascade down to all the other roles and so on..

	/**
	 * Serial number
	 */
	private static final long serialVersionUID = -5180551537096244085L;

	/** The sets of roles that the user has in different groups */
	private Map<? extends Group, ? extends RoleSet> roleSets;
	
	/** The sets of permissions that the user has in different groups */
	private Map<? extends Role, ? extends PermissionSet> permissionSets;
	
	/** The distinct list of groups that this user is part of */
	private GroupSet groupSet = new GroupSet();
	
	/** The distinct list of roles that this user is part of */
	private RoleSet roleSet = new RoleSet();
	
	/** the distinct list of permissions that this user has */
	private PermissionSet permissionSet = new PermissionSet();

	/**
	 * Constructs a new AccessControlList.
	 *
	 * This class follows 'immutable' pattern - it's objects can't be modified once
	 * they are created. This means that the permissions the users have are in
	 * effect form the moment they log in to the moment they log out, and changes
	 * made to the security settings in that time are not reflected in the state of
	 * this object. If you need to reset an user's permissions you need to
	 * invalidate his session. <br>
	 * The objects that constructs an AccessControlList must supply hashtables of
	 * role/permission sets keyed with group objects. <br>
	 *
	 * @param roleSets       a hashtable containing RoleSet objects keyed with Group
	 *                       objects
	 * @param permissionSets a hashtable containing PermissionSet objects keyed with
	 *                       Roles objects
	 */
	public DynamicAccessControlListImpl(Map<? extends Group, ? extends RoleSet> roleSets,
			Map<? extends Role, ? extends PermissionSet> permissionSets) {
		this.roleSets = roleSets;
		this.permissionSets = permissionSets;
		for (Map.Entry<? extends Group, ? extends RoleSet> entry : roleSets.entrySet()) 
		{
			Group group = entry.getKey();
			groupSet.add(group);
			RoleSet rs = entry.getValue();
			roleSet.add(rs);
		}
		
		for (Map.Entry<? extends Role, ? extends PermissionSet> entry : permissionSets.entrySet()) 
		{
			Role role = entry.getKey();
			roleSet.add(role);
			PermissionSet ps = entry.getValue();
			permissionSet.add(ps);
		}
	}

	/**
	 * Retrieves a set of Roles an user is assigned in a Group.
	 *
	 * @param group the Group
	 * @return the set of Roles this user has within the Group.
	 */
	public RoleSet getRoles(Group group) {
		if (group == null) 
		{
			return null;
		}

		return roleSets.get(group);
	}

	/**
	 * Retrieves a set of Roles an user is assigned in the global Group.
	 *
	 * @return the set of Roles this user has within the global Group.
	 */
	public RoleSet getRoles() {
		return roleSet;
	}

	/**
	 * Retrieves a set of Permissions an user is assigned in a Group.
	 *
	 * @param group the Group
	 * @return the set of Permissions this user has within the Group.
	 */
	public PermissionSet getPermissions(Group group) {
		PermissionSet permissionSet = new PermissionSet();
		if (roleSets.containsKey(group)) 
		{
			for (Role role : roleSets.get(group)) 
			{
				if (permissionSets.containsKey(role)) 
				{
					permissionSet.add(permissionSets.get(role));
				}
			}
		}
		return permissionSet;
	}

	/**
	 * Retrieves a set of Permissions an user is assigned in the global Group.
	 *
	 * @return the set of Permissions this user has within the global Group.
	 */
	public PermissionSet getPermissions() {
		return permissionSet;
	}

	/**
	 * Checks if the user is assigned a specific Role in the Group.
	 *
	 * @param role  the Role
	 * @param group the Group
	 * @return <code>true</code> if the user is assigned the Role in the Group.
	 */
	public boolean hasRole(Role role, Group group) {
		RoleSet set = getRoles(group);
		if (set == null || role == null) 
		{
			return false;
		}
		return set.contains(role);
	}

	/**
	 * Checks if the user is assigned a specific Role in any of the given Groups
	 *
	 * @param role     the Role
	 * @param groupset a Groupset
	 * @return <code>true</code> if the user is assigned the Role in any of the
	 *         given Groups.
	 */
	public boolean hasRole(Role role, GroupSet groupset) {
		if (role == null) 
		{
			return false;
		}

		for (Group group : groupset) 
		{
			RoleSet roles = getRoles(group);
			if (roles != null && roles.contains(role)) 
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the user is assigned a specific Role in the Group.
	 *
	 * @param role  the Role
	 * @param group the Group
	 * @return <code>true</code> if the user is assigned the Role in the Group.
	 */
	public boolean hasRole(String role, String group) {
		boolean roleFound = false;
		try 
		{
			for (Map.Entry<? extends Group, ? extends RoleSet> entry : roleSets.entrySet()) 
			{
				Group g = entry.getKey();
				if (g.getName().equalsIgnoreCase(group)) 
				{
					RoleSet rs = entry.getValue();
					roleFound = rs.containsName(role);
				}
			}
		} 
		catch (Exception e) 
		{
			roleFound = false;
		}
		return roleFound;
	}

	/**
	 * Checks if the user is assigned a specific Role in any of the given Groups
	 *
	 * @param rolename the name of the Role
	 * @param groupset a Groupset
	 * @return <code>true</code> if the user is assigned the Role in any of the
	 *         given Groups.
	 */
	public boolean hasRole(String rolename, GroupSet groupset) {
		Role role;
		try 
		{
			role = roleSet.getByName(rolename);
		} 
		catch (Exception e) 
		{
			return false;
		}
		
		if (role == null) 
		{
			return false;
		}
		
		for (Group group : groupset) 
		{
			RoleSet roles = getRoles(group);
			if (roles != null && roles.contains(role)) 
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the user is assigned a specific Role
	 *
	 * @param role the Role
	 * @return <code>true</code> if the user is assigned the Role in the global
	 *         Group.
	 */
	public boolean hasRole(Role role) {
		return roleSet.contains(role);
	}

	/**
	 * Checks if the user is assigned a specific Role .
	 *
	 * @param role the Role
	 * @return <code>true</code> if the user is assigned the Role .
	 */
	public boolean hasRole(String role) {
		try 
		{
			return roleSet.containsName(role);
		} 
		catch (Exception e) 
		{
			return false;
		}
	}

	/**
	 * Checks if the user is assigned a specific Permission in the Group.
	 *
	 * @param permission the Permission
	 * @param group      the Group
	 * @return <code>true</code> if the user is assigned the Permission in the
	 *         Group.
	 */
	public boolean hasPermission(Permission permission, Group group) {
		PermissionSet set = getPermissions(group);
		if (set == null || permission == null) 
		{
			return false;
		}
		return set.contains(permission);
	}

	/**
	 * Checks if the user is assigned a specific Permission in any of the given
	 * Groups
	 *
	 * @param permission the Permission
	 * @param groupset   a Groupset
	 * @return <code>true</code> if the user is assigned the Permission in any of
	 *         the given Groups.
	 */
	public boolean hasPermission(Permission permission, GroupSet groupset) {
		if (permission == null) 
		{
			return false;
		}
		
		for (Group group : groupset) 
		{
			PermissionSet permissions = getPermissions(group);
			if (permissions != null && permissions.contains(permission)) 
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the user is assigned a specific Permission in the Group.
	 *
	 * @param permission the Permission
	 * @param group      the Group
	 * @return <code>true</code> if the user is assigned the Permission in the
	 *         Group.
	 */
	public boolean hasPermission(String permission, String group) {
		try 
		{
			return hasPermission(permissionSet.getByName(permission), groupSet.getByName(group));
		} 
		catch (Exception e) 
		{
			return false;
		}
	}

	/**
	 * Checks if the user is assigned a specific Permission in the Group.
	 *
	 * @param permission the Permission
	 * @param group      the Group
	 * @return <code>true</code> if the user is assigned the Permission in the
	 *         Group.
	 */
	public boolean hasPermission(String permission, Group group) {
		try 
		{
			return hasPermission(permissionSet.getByName(permission), group);
		} 
		catch (Exception e) 
		{
			return false;
		}
	}

	/**
	 * Checks if the user is assigned a specific Permission in any of the given
	 * Groups
	 *
	 * @param permissionName the name of the Permission
	 * @param groupset       a Groupset
	 * @return <code>true</code> if the user is assigned the Permission in any of
	 *         the given Groups.
	 */
	public boolean hasPermission(String permissionName, GroupSet groupset) {
		Permission permission;
		try 
		{
			permission = permissionSet.getByName(permissionName);
		} catch (Exception e) {
			return false;
		}
		
		if (permission == null) 
		{
			return false;
		}
		
		for (Group group : groupset) 
		{
			PermissionSet permissions = getPermissions(group);
			if (permissions != null && permissions.contains(permission)) 
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the user is assigned a specific Permission.
	 *
	 * @param permission the Permission
	 * @return <code>true</code> if the user is assigned the Permission .
	 */
	public boolean hasPermission(Permission permission) {
		return permissionSet.contains(permission);
	}

	/**
	 * Checks if the user is assigned a specific Permission in the global Group.
	 *
	 * @param permission the Permission
	 * @return <code>true</code> if the user is assigned the Permission in the
	 *         global Group.
	 */
	public boolean hasPermission(String permission) {
		try 
		{
			return permissionSet.containsName(permission);
		} 
		catch (Exception e) 
		{
			return false;
		}
	}
}
