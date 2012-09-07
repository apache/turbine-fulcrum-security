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


import java.util.Map;

import org.apache.fulcrum.security.GroupManager;
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.util.FulcrumSecurityException;
import org.apache.fulcrum.security.util.GroupSet;
import org.apache.fulcrum.security.util.PermissionSet;
import org.apache.fulcrum.security.util.RoleSet;

/**
 * This is a control class that makes it easy to find out if a
 * particular User has a given Permission.  It also determines if a
 * User has a a particular Role.
 *
 * @author <a href="mailto:jmcnally@collab.net">John D. McNally</a>
 * @author <a href="mailto:bmclaugh@algx.net">Brett McLaughlin</a>
 * @author <a href="mailto:greg@shwoop.com">Greg Ritter</a>
 * @author <a href="mailto:Rafal.Krzewski@e-point.pl">Rafal Krzewski</a>
 * @author <a href="mailto:hps@intermeta.de">Henning P. Schmiedehausen</a>
 * @author <a href="mailto:marco@intermeta.de">Marco Kn&uuml;ttel</a>
 * @version $Id: TurbineAccessControlList.java 1096130 2011-04-23 10:37:19Z ludwig $
 */
public class TurbineAccessControlListImpl
        implements TurbineAccessControlList
{
    /** Serial version */
	private static final long serialVersionUID = 2678947159949477950L;

    /** The sets of roles that the user has in different groups */
    private Map<? extends Group, ? extends RoleSet> roleSets;
    /** The sets of permissions that the user has in different groups */
    private Map<? extends Role, ? extends PermissionSet> permissionSets;
    /** The global group */
    private Group globalGroup;
    /** The group manager */
    private GroupManager groupManager;
    /** The distinct list of groups that this user is part of */
    private GroupSet groupSet = new GroupSet();
    /** The distinct list of roles that this user is part of */
    private RoleSet roleSet = new RoleSet();
    /** the distinct list of permissions that this user has */
    private PermissionSet permissionSet = new PermissionSet();

    /**
     * Constructs a new AccessControlList.
     *
     * This class follows 'immutable' pattern - it's objects can't be modified
     * once they are created. This means that the permissions the users have are
     * in effect form the moment they log in to the moment they log out, and
     * changes made to the security settings in that time are not reflected
     * in the state of this object. If you need to reset an user's permissions
     * you need to invalidate his session. <br>
     * The objects that constructs an AccessControlList must supply hashtables
     * of role/permission sets keyed with group objects. <br>
     *
     * @param roleSets a hashtable containing RoleSet objects keyed with Group objects
     * @param permissionSets a hashtable containing PermissionSet objects keyed with Group objects
     * @param groupManager the Group manager
     * @throws FulcrumSecurityException if the global group cannot be retrieved
     */
    public TurbineAccessControlListImpl(
    		Map<? extends Group, ? extends RoleSet> roleSets,
            Map<? extends Role, ? extends PermissionSet> permissionSets,
    		GroupManager groupManager) throws FulcrumSecurityException
    {
        this.roleSets = roleSets;
        this.permissionSets = permissionSets;
        this.groupManager = groupManager;
        this.globalGroup = groupManager.getGroupByName(TurbineModelManager.GLOBAL_GROUP_NAME);
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
    public RoleSet getRoles(Group group)
    {
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
    public RoleSet getRoles()
    {
        return getRoles(globalGroup);
    }

    /**
     * Retrieves a set of Permissions an user is assigned in a Group.
     *
     * @param group the Group
     * @return the set of Permissions this user has within the Group.
     */
    public PermissionSet getPermissions(Group group)
    {
        if (group == null)
        {
            return null;
        }
        return permissionSets.get(group);
    }

    /**
     * Retrieves a set of Permissions an user is assigned in the global Group.
     *
     * @return the set of Permissions this user has within the global Group.
     */
    public PermissionSet getPermissions()
    {
        return getPermissions(globalGroup);
    }

    /**
     * Checks if the user is assigned a specific Role in the Group.
     *
     * @param role the Role
     * @param group the Group
     * @return <code>true</code> if the user is assigned the Role in the Group.
     */
    public boolean hasRole(Role role, Group group)
    {
        RoleSet set = getRoles(group);
        if (set == null || role == null)
        {
            return false;
        }
        return set.contains(role);
    }

    /**
     * Checks if the user is assigned a specific Role in any of the given
     * Groups
     *
     * @param role the Role
     * @param groupset a Groupset
     * @return <code>true</code> if the user is assigned the Role in any of
     *         the given Groups.
     */
    public boolean hasRole(Role role, GroupSet groupset)
    {
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
     * @param role the Role
     * @param group the Group
     * @return <code>true</code> if the user is assigned the Role in the Group.
     */
    public boolean hasRole(String role, String group)
    {
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
     * Checks if the user is assigned a specifie Role in any of the given
     * Groups
     *
     * @param rolename the name of the Role
     * @param groupset a Groupset
     * @return <code>true</code> if the user is assigned the Role in any of
     *         the given Groups.
     */
    public boolean hasRole(String rolename, GroupSet groupset)
    {
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
     * Checks if the user is assigned a specific Role in the global Group.
     *
     * @param role the Role
     * @return <code>true</code> if the user is assigned the Role in the global Group.
     */
    public boolean hasRole(Role role)
    {
        return hasRole(role, globalGroup);
    }

    /**
     * Checks if the user is assigned a specific Role in the global Group.
     *
     * @param role the Role
     * @return <code>true</code> if the user is assigned the Role in the global Group.
     */
    public boolean hasRole(String role)
    {
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
     * @param group the Group
     * @return <code>true</code> if the user is assigned the Permission in the Group.
     */
    public boolean hasPermission(Permission permission, Group group)
    {
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
     * @param groupset a Groupset
     * @return <code>true</code> if the user is assigned the Permission in any
     *         of the given Groups.
     */
    public boolean hasPermission(Permission permission, GroupSet groupset)
    {
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
     * @param group the Group
     * @return <code>true</code> if the user is assigned the Permission in the Group.
     */
    public boolean hasPermission(String permission, String group)
    {
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
     * @param group the Group
     * @return <code>true</code> if the user is assigned the Permission in the Group.
     */
    public boolean hasPermission(String permission, Group group)
    {
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
     * @param groupset a Groupset
     * @return <code>true</code> if the user is assigned the Permission in any
     *         of the given Groups.
     */
    public boolean hasPermission(String permissionName, GroupSet groupset)
    {
        Permission permission;
        try
        {
            permission = permissionSet.getByName(permissionName);
        }
        catch (Exception e)
        {
            return false;
        }
        if (permission == null)
        {
            return false;
        }
        for (Group group : groupset)
        {
            PermissionSet permissions = getPermissions(group);
            if (permissions != null)
            {
                if (permissions.contains(permission))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the user is assigned a specific Permission in the global Group.
     *
     * @param permission the Permission
     * @return <code>true</code> if the user is assigned the Permission in the global Group.
     */
    public boolean hasPermission(Permission permission)
    {
        return hasPermission(permission, globalGroup);
    }

    /**
     * Checks if the user is assigned a specific Permission in the global Group.
     *
     * @param permission the Permission
     * @return <code>true</code> if the user is assigned the Permission in the global Group.
     */
    public boolean hasPermission(String permission)
    {
        try
        {
            return permissionSet.containsName(permission);
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Returns all groups defined in the system.
     *
     * This is useful for debugging, when you want to display all roles
     * and permissions an user is assigned. This method is needed
     * because you can't call static methods of TurbineSecurity class
     * from within WebMacro/Velocity template
     *
     * @return A Group [] of all groups in the system.
     */
    public Group[] getAllGroups()
    {
        try
        {
            return groupManager.getAllGroups().toArray(new Group[0]);
        }
        catch (FulcrumSecurityException e)
        {
            return new Group[0];
        }
    }
}
