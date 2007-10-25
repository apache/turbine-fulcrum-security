package org.apache.fulcrum.security.adapter.turbine;
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
import java.util.Iterator;
import java.util.List;

import org.apache.avalon.framework.component.ComponentException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.acl.AccessControlList;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.PasswordMismatchException;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.apache.torque.util.Criteria;
import org.apache.turbine.om.security.Group;
import org.apache.turbine.om.security.Permission;
import org.apache.turbine.om.security.Role;
import org.apache.turbine.om.security.User;
import org.apache.turbine.services.InitializationException;
import org.apache.turbine.services.TurbineServices;
import org.apache.turbine.services.avaloncomponent.AvalonComponentService;
import org.apache.turbine.services.security.BaseSecurityService;
import org.apache.turbine.services.security.UserManager;
import org.apache.turbine.util.security.EntityExistsException;
import org.apache.turbine.util.security.GroupSet;
import org.apache.turbine.util.security.PermissionSet;
import org.apache.turbine.util.security.RoleSet;

/**
 * An implementation of SecurityService that adapts it to work with
 * Fulcrum Security.  This adapter currently uses the "Simple" model of
 * security.  However, it should actually use the "Turbine" model of
 * security.  I didn't do that because I don't quite understand some of
 * the reasons behind the "Turbine" model.
 *
 * All the methods that currently throw a not implemented RuntimeException
 * could easily delegate their calls to the Fulcrum SecurityService.
 *
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class SecurityServiceAdapter extends BaseSecurityService
{
    /** Logging */
    private static Log log = LogFactory.getLog(SecurityServiceAdapter.class);
    /** Our Fulcrum Security Service to use */
    SecurityService securityService;
    /**
     * Hook into the init() calls so our adapter can load up from Avalon
     * the securityManager it needs.
     */
    public void init() throws InitializationException
    {
        super.init();
        AvalonComponentService acs =
            (AvalonComponentService) TurbineServices.getInstance().getService(
                AvalonComponentService.SERVICE_NAME);
        try
        {
            securityService =
                (SecurityService) acs.lookup(SecurityService.ROLE);
        }
        catch (ComponentException ce)
        {
            throw new InitializationException(
                "Could not retrieve Avalon Security Service:" + ce.getMessage(),
                ce);
        }
    }
    /*
     * -----------------------------------------------------------------------
     *  C R E A T I O N  O F  A C C E S S  C O N T R O L  L I S T
     * -----------------------------------------------------------------------
     */
    /**
     * Constructs an AccessControlList for a specific user.
     *
     * This method creates a snapshot of the state of security information
     * concerning this user, at the moment of invocation and stores it
     * into an AccessControlList object.
     *
     * @param user the user for whom the AccessControlList are to be retrieved
     * @throws org.apache.turbine.util.security.DataBackendException if there was an error accessing the backend.
     * @throws UnknownEntityException if user account is not present.
     * @return an AccessControlList for a specific user.
     */
    public org.apache.turbine.util.security.AccessControlList getACL(User user)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        try
        {
            org.apache.fulcrum.security.entity.User fulcrumUser =
                securityService.getUserManager().getUser(user.getName());
			 AccessControlList acl =
                 securityService
                    .getUserManager()
                    .getACL(
                    fulcrumUser);

            AccessControlListAdapter aclAdaptor =
                new AccessControlListAdapter(acl);
            return aclAdaptor;
        }
        catch (DataBackendException dbe)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                dbe.getMessage(),
                dbe);
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.UnknownEntityException(
                uee.getMessage(),
                uee);
        }
    }
    /*
     * -----------------------------------------------------------------------
     * S E C U R I T Y  M A N A G E M E N T
     * -----------------------------------------------------------------------
     */
    /**
     * Grant an User a Role in a Group.
     *
     * @param user the user.
     * @param group the group.
     * @param role the role.
     * @throws org.apache.turbine.util.security.DataBackendException if there was an error accessing the backend.
     * @throws UnknownEntityException if user account, group or role
     *         is not present.
     */
    public synchronized void grant(User user, Group group, Role role)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        try
        {
            throw new UnknownEntityException("Not implemented");
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.UnknownEntityException(
                uee.getMessage(),
                uee);
        }
    }
    /**
     * Revoke a Role in a Group from an User.
     *
     * @param user the user.
     * @param group the group.
     * @param role the role.
     * @throws org.apache.turbine.util.security.DataBackendException if there was an error accessing the backend.
     * @throws UnknownEntityException if user account, group or role is
     *         not present.
     */
    public synchronized void revoke(User user, Group group, Role role)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        try
        {
            throw new UnknownEntityException("Not implemented");
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.UnknownEntityException(
                uee.getMessage(),
                uee);
        }
    }
    /**
     * Grants a Role a Permission
     *
     * @param role the Role.
     * @param permission the Permission.
     * @throws org.apache.turbine.util.security.DataBackendException if there was an error accessing the backend.
     * @throws UnknownEntityException if role or permission is not present.
     */
    public synchronized void grant(Role role, Permission permission)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        try
        {
            throw new UnknownEntityException("Not implemented");
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.UnknownEntityException(
                uee.getMessage(),
                uee);
        }
    }
    /**
     * Revokes a Permission from a Role.
     *
     * @param role the Role.
     * @param permission the Permission.
     * @throws org.apache.turbine.util.security.DataBackendException if there was an error accessing the backend.
     * @throws UnknownEntityException if role or permission is not present.
     */
    public synchronized void revoke(Role role, Permission permission)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        try
        {
            throw new UnknownEntityException("Not implemented");
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.UnknownEntityException(
                uee.getMessage(),
                uee);
        }
    }
    /*
     * -----------------------------------------------------------------------
     * G R O U P / R O L E / P E R M I S S I O N  M A N A G E M E N T
     * -----------------------------------------------------------------------
     */
    /**
     * Retrieve a set of Groups that meet the specified Criteria.  However,
     * in this adapter, we just return EVERYTHING!
     *
     * @param criteria Criteria of Group selection.
     * @return a set of Groups that meet the specified Criteria.
     * @throws org.apache.turbine.util.security.DataBackendException if there is problem with the Backend.
     */
    public GroupSet getGroups(Criteria criteria)
        throws org.apache.turbine.util.security.DataBackendException
    {
        return getAllGroups();
    }
    /** Get the Roles that a user belongs in a specific group.
     * @param user The user.
     * @param group The group
     * @throws org.apache.turbine.util.security.DataBackendException if there is a problem with
     *     the LDAP service.
     * @return a RoleSet.
     */
    private RoleSet getRoles(User user, Group group)
        throws org.apache.turbine.util.security.DataBackendException
    {
        try
        {
            throw new UnknownEntityException("Not implemented");
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                uee.getMessage(),
                uee);
        }
    }
    /**
     * Retrieve a set of Roles that meet the specified Criteria.
     *
     * @param criteria Criteria of Roles selection.
     * @return a set of Roles that meet the specified Criteria.
     * @throws org.apache.turbine.util.security.DataBackendException if there is a problem with the Backend.
     */
    public RoleSet getRoles(Criteria criteria)
        throws org.apache.turbine.util.security.DataBackendException
    {
        try
        {
            throw new UnknownEntityException("Not implemented");
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                uee.getMessage(),
                uee);
        }
    }
    /**
     * Retrieve a set of Permissions that meet the specified Criteria.
     *
     * @param criteria Criteria of Permissions selection.
     * @return a set of Permissions that meet the specified Criteria.
     * @throws org.apache.turbine.util.security.DataBackendException if there is a problem with the Backend.
     */
    public PermissionSet getPermissions(Criteria criteria)
        throws org.apache.turbine.util.security.DataBackendException
    {
        try
        {
            throw new UnknownEntityException("Not implemented");
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                uee.getMessage(),
                uee);
        }
    }
    /**
     * Retrieves all permissions associated with a role.
     *
     * @param role the role name, for which the permissions are to be retrieved.
     * @throws org.apache.turbine.util.security.DataBackendException if there was an error accessing the backend.
     * @throws UnknownEntityException if the role is not present.
     * @return a PermissionSet.
     */
    public PermissionSet getPermissions(Role role)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        try
        {
            throw new UnknownEntityException("Not implemented");
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                uee.getMessage(),
                uee);
        }
    }
    /**
     * Stores Group's attributes. The Groups is required to exist in the system.
     *
     * @param group The Group to be stored.
     * @throws org.apache.turbine.util.security.DataBackendException if there was an error accessing the backend.
     * @throws UnknownEntityException if the group does not exist.
     */
    public void saveGroup(Group group)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        try
        {
            throw new UnknownEntityException("Not implemented");
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                uee.getMessage(),
                uee);
        }
    }
    /**
     * Stores Role's attributes. The Roles is required to exist in the system.
     *
     * @param role The Role to be stored.
     * @throws org.apache.turbine.util.security.DataBackendException if there was an error accessing the backend.
     * @throws UnknownEntityException if the role does not exist.
     */
    public void saveRole(Role role)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        try
        {
            throw new UnknownEntityException("Not implemented");
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                uee.getMessage(),
                uee);
        }
    }
    /**
     * Stores Permission's attributes. The Permissions is required to exist in
     * the system.
     *
     * @param permission The Permission to be stored.
     * @throws org.apache.turbine.util.security.DataBackendException if there was an error accessing the backend.
     * @throws UnknownEntityException if the permission does not exist.
     */
    public void savePermission(Permission permission)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        try
        {
            throw new UnknownEntityException("Not implemented");
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                uee.getMessage(),
                uee);
        }
    }
    /**
     * Creates a new group with specified attributes.
     * <strong>Not implemented</strong>
     *
     * @param group the object describing the group to be created.
     * @return a new Group object that has id set up properly.
     * @throws org.apache.turbine.util.security.DataBackendException if there was an error accessing the backend.
     * @throws EntityExistsException if the group already exists.
     */
    public synchronized Group addGroup(Group group)
        throws
            org.apache.turbine.util.security.DataBackendException,
            EntityExistsException
    {
        try
        {
            throw new UnknownEntityException("Not implemented");
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                uee.getMessage(),
                uee);
        }
    }
    /**
     * Creates a new role with specified attributes.
     *
     * @param role the object describing the role to be created.
     * @return a new Role object that has id set up properly.
     * @throws org.apache.turbine.util.security.DataBackendException if there was an error accessing the backend.
     * @throws EntityExistsException if the role already exists.
     */
    public synchronized Role addRole(Role role)
        throws
            org.apache.turbine.util.security.DataBackendException,
            EntityExistsException
    {
        try
        {
            throw new UnknownEntityException("Not implemented");
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                uee.getMessage(),
                uee);
        }
    }
    /**
     * Creates a new permission with specified attributes.
     * <strong>Not implemented</strong>
     *
     * @param permission the object describing the permission to be created.
     * @return a new Permission object that has id set up properly.
     * @throws org.apache.turbine.util.security.DataBackendException if there was an error accessing the backend.
     * @throws EntityExistsException if the permission already exists.
     */
    public synchronized Permission addPermission(Permission permission)
        throws
            org.apache.turbine.util.security.DataBackendException,
            EntityExistsException
    {
        try
        {
            throw new UnknownEntityException("Not implemented");
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                uee.getMessage(),
                uee);
        }
    }
    /**
     * Removes a Group from the system.
     *
     * @param group object describing group to be removed.
     * @throws org.apache.turbine.util.security.DataBackendException if there was an error accessing the backend.
     * @throws UnknownEntityException if the group does not exist.
     */
    public synchronized void removeGroup(Group group)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        try
        {
            throw new UnknownEntityException("Not implemented");
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                uee.getMessage(),
                uee);
        }
    }
    /**
     * Removes a Role from the system.
     *
     * @param role object describing role to be removed.
     * @throws org.apache.turbine.util.security.DataBackendException if there was an error accessing the backend.
     * @throws UnknownEntityException if the role does not exist.
     */
    public synchronized void removeRole(Role role)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        try
        {
            throw new UnknownEntityException("Not implemented");
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                uee.getMessage(),
                uee);
        }
    }
    /**
     * Removes a Permission from the system.
     *
     * @param permission object describing permission to be removed.
     * @throws org.apache.turbine.util.security.DataBackendException if there was an error accessing the backend.
     * @throws UnknownEntityException if the permission does not exist.
     */
    public synchronized void removePermission(Permission permission)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        try
        {
            throw new UnknownEntityException("Not implemented");
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                uee.getMessage(),
                uee);
        }
    }
    /**
     * Renames an existing Group.
     *
     * @param group object describing the group to be renamed.
     * @param name the new name for the group.
     * @throws org.apache.turbine.util.security.DataBackendException if there was an error accessing the backend.
     * @throws UnknownEntityException if the group does not exist.
     */
    public synchronized void renameGroup(Group group, String name)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        try
        {
            throw new UnknownEntityException("Not implemented");
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                uee.getMessage(),
                uee);
        }
    }
    /**
     * Renames an existing Role.
     *
     * @param role object describing the role to be renamed.
     * @param name the new name for the role.
     * @throws org.apache.turbine.util.security.DataBackendException if there was an error accessing the backend.
     * @throws UnknownEntityException if the role does not exist.
     */
    public synchronized void renameRole(Role role, String name)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        try
        {
            throw new UnknownEntityException("Not implemented");
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                uee.getMessage(),
                uee);
        }
    }
    /**
     * Renames an existing Permission.
     *
     * @param permission object describing the permission to be renamed.
     * @param name the new name for the permission.
     * @throws org.apache.turbine.util.security.DataBackendException if there was an error accessing the backend.
     * @throws UnknownEntityException if the permission does not exist.
     */
    public synchronized void renamePermission(
        Permission permission,
        String name)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        try
        {
            throw new UnknownEntityException("Not implemented");
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                uee.getMessage(),
                uee);
        }
    }
    /**
     * Revoke all the roles to a user
     * @param user the user.
     * @throws org.apache.turbine.util.security.DataBackendException if there is an error with the data backend.
     * @throws UnkownEntityException if the role or a permission is not found.
     */
    public void revokeAll(User user)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        Iterator groupsIterator = getAllGroups().iterator();
        while (groupsIterator.hasNext())
        {
            Group group = (Group) groupsIterator.next();
            Iterator rolesIterator = getRoles(user, group).iterator();
            while (rolesIterator.hasNext())
            {
                Role role = (Role) rolesIterator.next();
                revoke(user, group, role);
            }
        }
    }
    /**
     * Revoke all the permissions to a role.
     * @param role the role.
     * @throws org.apache.turbine.util.security.DataBackendException if there is an error with the data backend.
     * @throws UnkownEntityException if the role or a permission is not found.
     */
    public void revokeAll(Role role)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        PermissionSet permissions = getPermissions(role);
        Iterator permIterator = permissions.iterator();
        while (permIterator.hasNext())
        {
            Permission perm = (Permission) permIterator.next();
            revoke(role, perm);
        }
    }
    /**
     * Revoke all the roles to a group.
     * @param group the group.
     * @throws org.apache.turbine.util.security.DataBackendException if there is an error with the data backend.
     * @throws UnkownEntityException if the role or a permission is not found.
     */
    public void revokeAll(Group group)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        for (Iterator it = getUserList(new Criteria()).iterator();
            it.hasNext();
            )
        {
            User user = (User) it.next();
            for (Iterator rolesIterator = getRoles(user, group).iterator();
                rolesIterator.hasNext();
                )
            {
                Role role = (Role) rolesIterator.next();
                revoke(user, group, role);
            }
        }
    }
    /**
     * Determines if the <code>Role</code> exists in the security system.
     *
     * @param role a <code>Role</code> value
     * @return true if the role exists in the system, false otherwise
     * @throws org.apache.turbine.util.security.DataBackendException if there is an error with LDAP
     */
    public boolean checkExists(Role role)
        throws org.apache.turbine.util.security.DataBackendException
    {
        RoleSet roleSet = getRoles(new Criteria());
        return roleSet.contains(role);
    }
    /**
     * Determines if the <code>Group</code> exists in the security system.
     *
     * @param group a <code>Group</code> value
     * @return true if the group exists in the system, false otherwise
     * @throws org.apache.turbine.util.security.DataBackendException if there is an error with LDAP
     */
    public boolean checkExists(Group group)
        throws org.apache.turbine.util.security.DataBackendException
    {
        GroupSet groupSet = getGroups(new Criteria());
        return groupSet.contains(group);
    }
    /**
     * Determines if the <code>Permission</code> exists in the security system.
     *
     * @param permission a <code>Permission</code> value
     * @return true if the permission exists in the system, false otherwise
     * @throws org.apache.turbine.util.security.DataBackendException if there is an error with LDAP
     */
    public boolean checkExists(Permission permission)
        throws org.apache.turbine.util.security.DataBackendException
    {
        PermissionSet permissionSet = getPermissions(new Criteria());
        return permissionSet.contains(permission);
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#getUserManager()
     */
    public UserManager getUserManager()
    {
        throw new RuntimeException("Not implemented");
    }
    /* This doesn't do anything.  You don't need to call this anymore
     * as the SecurityService loads it's UserManager via Avalon.
     * @see org.apache.turbine.services.security.SecurityService#setUserManager(org.apache.turbine.services.security.UserManager)
     */
    public void setUserManager(UserManager arg0)
    {
        //throw new RuntimeException("Not implemented");
        log.info("setUserManager() call being eaten by SecurityServiceAdapter");
    }
    /* Returns whther an account exists for this username
     * @see org.apache.turbine.services.security.SecurityService#accountExists(java.lang.String)
     */
    public boolean accountExists(String name)
        throws org.apache.turbine.util.security.DataBackendException
    {
        try
        {
            return securityService.getUserManager().checkExists(name);
        }
        catch (DataBackendException dbe)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                dbe.getMessage(),
                dbe);
        }
    }
    /*
     * @see org.apache.turbine.services.security.SecurityService#accountExists(org.apache.turbine.om.security.User)
     */
    public boolean accountExists(User arg0)
        throws org.apache.turbine.util.security.DataBackendException
    {
        return accountExists(arg0.getName());
    }
    /*
     * @see org.apache.turbine.services.security.SecurityService#getAuthenticatedUser(java.lang.String, java.lang.String)
     */
    public User getAuthenticatedUser(String arg0, String arg1)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException,
            org.apache.turbine.util.security.PasswordMismatchException
    {
        try
        {
            return new UserAdapter(
                securityService.getUserManager().getUser(arg0, arg1));
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.UnknownEntityException(
                uee.getMessage(),
                uee);
        }
        catch (PasswordMismatchException pwe)
        {
            throw new org
                .apache
                .turbine
                .util
                .security
                .PasswordMismatchException(
                pwe.getMessage());
        }
        catch (DataBackendException dbe)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                dbe.getMessage(),
                dbe);
        }
    }
    /*
     * @see org.apache.turbine.services.security.SecurityService#getUser(java.lang.String)
     */
    public User getUser(String arg0)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        try
        {
            return new UserAdapter(
                securityService.getUserManager().getUser(arg0));
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.UnknownEntityException(
                uee.getMessage(),
                uee);
        }
        catch (DataBackendException dbe)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                dbe.getMessage(),
                dbe);
        }
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#getUsers(org.apache.torque.util.Criteria)
     */
    public User[] getUsers(Criteria arg0)
        throws org.apache.turbine.util.security.DataBackendException
    {
        try
        {
            throw new UnknownEntityException("Not implemented");
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                uee.getMessage(),
                uee);
        }
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#getUserList(org.apache.torque.util.Criteria)
     */
    public List getUserList(Criteria arg0)
        throws org.apache.turbine.util.security.DataBackendException
    {
        try
        {
            throw new UnknownEntityException("Not implemented");
        }
        catch (UnknownEntityException uee)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                uee.getMessage(),
                uee);
        }
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#saveUser(org.apache.turbine.om.security.User)
     */
    public void saveUser(User arg0)
        throws
            org.apache.turbine.util.security.UnknownEntityException,
            org.apache.turbine.util.security.DataBackendException
    {
        throw new org.apache.turbine.util.security.UnknownEntityException(
            "Not implemented");
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#saveOnSessionUnbind(org.apache.turbine.om.security.User)
     */
    public void saveOnSessionUnbind(User arg0)
        throws
            org.apache.turbine.util.security.UnknownEntityException,
            org.apache.turbine.util.security.DataBackendException
    {
        throw new org.apache.turbine.util.security.DataBackendException(
            "Not implemented");
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#addUser(org.apache.turbine.om.security.User, java.lang.String)
     */
    public void addUser(User user, String password)
        throws org.apache.turbine.util.security.DataBackendException
    {
        try
        {
            org.apache.fulcrum.security.entity.User fulcrumUser =
                securityService.getUserManager().getUserInstance(
                    user.getName());
            securityService.getUserManager().addUser(fulcrumUser, password);
        }
        catch (org.apache.fulcrum.security.util.EntityExistsException eee)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                "User already exists",
                eee);
        }
        catch (DataBackendException dbe)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                "Problem adding user",
                dbe);
        }
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#removeUser(org.apache.turbine.om.security.User)
     */
    public void removeUser(User arg0)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        throw new org.apache.turbine.util.security.DataBackendException(
            "Not implemented");
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#encryptPassword(java.lang.String)
     */
    public String encryptPassword(String arg0)
    {
        throw new RuntimeException("Not implemented");
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#encryptPassword(java.lang.String, java.lang.String)
     */
    public String encryptPassword(String arg0, String arg1)
    {
        throw new RuntimeException("Not implemented");
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#checkPassword(java.lang.String, java.lang.String)
     */
    public boolean checkPassword(String arg0, String arg1)
    {
        throw new RuntimeException("Not implemented");
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#changePassword(org.apache.turbine.om.security.User, java.lang.String, java.lang.String)
     */
    public void changePassword(User arg0, String arg1, String arg2)
        throws
            org.apache.turbine.util.security.PasswordMismatchException,
            org.apache.turbine.util.security.UnknownEntityException,
            org.apache.turbine.util.security.DataBackendException
    {
        throw new RuntimeException("Not implemented");
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#forcePassword(org.apache.turbine.om.security.User, java.lang.String)
     */
    public void forcePassword(User arg0, String arg1)
        throws
            org.apache.turbine.util.security.UnknownEntityException,
            org.apache.turbine.util.security.DataBackendException
    {
        throw new RuntimeException("Not implemented");
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#getGlobalGroup()
     */
    public Group getGlobalGroup()
    {
        throw new RuntimeException("Not implemented");
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#getNewGroup(java.lang.String)
     */
    public Group getNewGroup(String arg0)
    {
        throw new RuntimeException("Not implemented");
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#getNewRole(java.lang.String)
     */
    public Role getNewRole(String arg0)
    {
        throw new RuntimeException("Not implemented");
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#getNewPermission(java.lang.String)
     */
    public Permission getNewPermission(String arg0)
    {
        throw new RuntimeException("Not implemented");
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#getGroup(java.lang.String)
     */
    public Group getGroup(String arg0)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        throw new RuntimeException("Not implemented");
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#getGroupByName(java.lang.String)
     */
    public Group getGroupByName(String arg0)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        throw new RuntimeException("Not implemented");
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#getGroupById(int)
     */
    public Group getGroupById(int arg0)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        throw new RuntimeException("Not implemented");
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#getRole(java.lang.String)
     */
    public Role getRole(String arg0)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        throw new RuntimeException("Not implemented");
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#getRoleByName(java.lang.String)
     */
    public Role getRoleByName(String arg0)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        throw new RuntimeException("Not implemented");
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#getRoleById(int)
     */
    public Role getRoleById(int arg0)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        throw new RuntimeException("Not implemented");
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#getPermission(java.lang.String)
     */
    public Permission getPermission(String arg0)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        throw new RuntimeException("Not implemented");
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#getPermissionByName(java.lang.String)
     */
    public Permission getPermissionByName(String arg0)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        throw new RuntimeException("Not implemented");
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#getPermissionById(int)
     */
    public Permission getPermissionById(int arg0)
        throws
            org.apache.turbine.util.security.DataBackendException,
            org.apache.turbine.util.security.UnknownEntityException
    {
        throw new RuntimeException("Not implemented");
    }
    /* Not implemented.
     * @see org.apache.turbine.services.security.SecurityService#getAllGroups()
     */
    public GroupSet getAllGroups()
        throws org.apache.turbine.util.security.DataBackendException
    {
        try
        {
            GroupSet turbineGS = new GroupSet();
            org.apache.fulcrum.security.util.GroupSet fulcrumGS =
                securityService.getGroupManager().getAllGroups();
            for (Iterator i = fulcrumGS.iterator(); i.hasNext();)
            {
                org.apache.fulcrum.security.entity.Group fulcrumGroup =
                    (org.apache.fulcrum.security.entity.Group) i.next();
                Group turbineGroup = new GroupAdapter(fulcrumGroup);
                turbineGS.add(turbineGroup);
            }
            return turbineGS;
        }
        catch (DataBackendException uee)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                uee.getMessage(),
                uee);
        }
    }
    /* Return all the roles from the Fulcrum Security Service.
     * @see org.apache.turbine.services.security.SecurityService#getAllRoles()
     */
    public RoleSet getAllRoles()
        throws org.apache.turbine.util.security.DataBackendException
    {
        try
        {
            RoleSet turbineRS = new RoleSet();
            org.apache.fulcrum.security.util.RoleSet fulcrumRS =
                securityService.getRoleManager().getAllRoles();
            for (Iterator i = fulcrumRS.iterator(); i.hasNext();)
            {
                org.apache.fulcrum.security.entity.Role fulcrumRole =
                    (org.apache.fulcrum.security.entity.Role) i.next();
                Role turbineRole = new RoleAdapter(fulcrumRole);
                turbineRS.add(turbineRole);
            }
            return turbineRS;
        }
        catch (DataBackendException uee)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                uee.getMessage(),
                uee);
        }
    }
    /* Return all the permissions from the Fulcrum SecurityService.
     * @see org.apache.turbine.services.security.SecurityService#getAllPermissions()
     */
    public PermissionSet getAllPermissions()
        throws org.apache.turbine.util.security.DataBackendException
    {
        try
        {
            PermissionSet turbinePS = new PermissionSet();
            org.apache.fulcrum.security.util.PermissionSet fulcrumPS =
                securityService.getPermissionManager().getAllPermissions();
            for (Iterator i = fulcrumPS.iterator(); i.hasNext();)
            {
                org.apache.fulcrum.security.entity.Permission fulcrumPermission =
                    (org.apache.fulcrum.security.entity.Permission) i.next();
                Permission turbinePermission =
                    new PermissionAdapter(fulcrumPermission);
                turbinePS.add(turbinePermission);
            }
            return turbinePS;
        }
        catch (DataBackendException uee)
        {
            throw new org.apache.turbine.util.security.DataBackendException(
                uee.getMessage(),
                uee);
        }
    }
}
