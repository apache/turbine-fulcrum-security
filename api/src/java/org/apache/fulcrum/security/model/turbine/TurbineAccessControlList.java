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


import java.io.Serializable;

import org.apache.fulcrum.security.acl.AccessControlList;
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.util.GroupSet;
import org.apache.fulcrum.security.util.PermissionSet;
import org.apache.fulcrum.security.util.RoleSet;

/**
 * This interface describes a control class that makes it
 * easy to find out if a particular User has a given Permission.
 * It also determines if a User has a a particular Role.
 *
 * @author <a href="mailto:john.mcnally@clearink.com">John D. McNally</a>
 * @author <a href="mailto:bmclaugh@algx.net">Brett McLaughlin</a>
 * @author <a href="mailto:greg@shwoop.com">Greg Ritter</a>
 * @author <a href="mailto:Rafal.Krzewski@e-point.pl">Rafal Krzewski</a>
 * @author <a href="mailto:marco@intermeta.de">Marco Kn&uuml;ttel</a>
 * @author <a href="mailto:hps@intermeta.de">Henning P. Schmiedehausen</a>
 * @version $Id: AccessControlList.java 615328 2008-01-25 20:25:05Z tv $
 */
public interface TurbineAccessControlList<T extends TurbineAccessControlList<T>> extends Serializable, AccessControlList 
{
    /**
     * Retrieves a set of Roles an user is assigned in a Group.
     *
     * @param group the Group
     * @return the set of Roles this user has within the Group.
     */
    RoleSet getRoles(Group group);

    /**
     * Retrieves a set of Roles an user is assigned in the global Group.
     *
     * @return the set of Roles this user has within the global Group or null.
     */
    RoleSet getRoles();

    /**
     * Retrieves a set of Permissions an user is assigned in a Group.
     *
     * @param group the Group
     * @return the set of Permissions this user has within the Group.
     */
    PermissionSet getPermissions(Group group);

    /**
     * Retrieves a set of Permissions an user is assigned in the global Group.
     *
     * @return the set of Permissions this user has within the global Group.
     */
    PermissionSet getPermissions();

    /**
     * Checks if the user is assigned a specific Role in the Group.
     *
     * @param role the Role
     * @param group the Group
     * @return <code>true</code> if the user is assigned the Role in the Group.
     */
    boolean hasRole(Role role, Group group);

    /**
     * Checks if the user is assigned a specific Role in any of the given
     * Groups
     *
     * @param role the Role
     * @param groupset a Groupset
     * @return <code>true</code> if the user is assigned the Role in any of
     *         the given Groups.
     */
    boolean hasRole(Role role, GroupSet groupset);

    /**
     * Checks if the user is assigned a specific Role in the Group.
     *
     * @param role the Role
     * @param group the Group
     * @return <code>true</code> if the user is assigned the Role in the Group.
     */
    boolean hasRole(String role, String group);

    /**
     * Checks if the user is assigned a specifie Role in any of the given
     * Groups
     *
     * @param rolename the name of the Role
     * @param groupset a Groupset
     * @return <code>true</code> if the user is assigned the Role in any of
     *         the given Groups.
     */
    boolean hasRole(String rolename, GroupSet groupset);

    /**
     * Checks if the user is assigned a specific Role in the global Group.
     *
     * @param role the Role
     * @return <code>true</code> if the user is assigned the Role in the global Group.
     */
    boolean hasRole(Role role);

    /**
     * Checks if the user is assigned a specific Role in the global Group.
     *
     * @param role the Role
     * @return <code>true</code> if the user is assigned the Role in the global Group.
     */
    boolean hasRole(String role);

    /**
     * Checks if the user is assigned a specific Permission in the Group.
     *
     * @param permission the Permission
     * @param group the Group
     * @return <code>true</code> if the user is assigned the Permission in the Group.
     */
    boolean hasPermission(Permission permission, Group group);

    /**
     * Checks if the user is assigned a specific Permission in any of the given
     * Groups
     *
     * @param permission the Permission
     * @param groupset a Groupset
     * @return <code>true</code> if the user is assigned the Permission in any
     *         of the given Groups.
     */
    boolean hasPermission(Permission permission, GroupSet groupset);

    /**
     * Checks if the user is assigned a specific Permission in the Group.
     *
     * @param permission the Permission
     * @param group the Group
     * @return <code>true</code> if the user is assigned the Permission in the Group.
     */
    boolean hasPermission(String permission, String group);

    /**
     * Checks if the user is assigned a specific Permission in the Group.
     *
     * @param permission the Permission
     * @param group the Group
     * @return <code>true</code> if the user is assigned the Permission in the Group.
     */
    boolean hasPermission(String permission, Group group);

    /**
     * Checks if the user is assigned a specifie Permission in any of the given
     * Groups
     *
     * @param permissionName the name of the Permission
     * @param groupset a Groupset
     * @return <code>true</code> if the user is assigned the Permission in any
     *         of the given Groups.
     */
    boolean hasPermission(String permissionName, GroupSet groupset);

    /**
     * Checks if the user is assigned a specific Permission in the global Group.
     *
     * @param permission the Permission
     * @return <code>true</code> if the user is assigned the Permission in the global Group.
     */
    boolean hasPermission(Permission permission);

    /**
     * Checks if the user is assigned a specific Permission in the global Group.
     *
     * @param permission the Permission
     * @return <code>true</code> if the user is assigned the Permission in the global Group.
     */
    boolean hasPermission(String permission);

    /**
     * Returns all groups defined in the system.
     *
     * @return An Array of all defined Groups
     *
     * This is useful for debugging, when you want to display all roles
     * and permissions an user is assigned. This method is needed
     * because you can't call static methods of TurbineSecurity class
     * from within WebMacro/Velocity template
     */
    Group[] getAllGroups();

    /**
     * Retrieves a set of Groups an user is assigned to.
     *
     * @return the set of Groups this user is assigned to.
     */
    GroupSet getGroupSet();
}
