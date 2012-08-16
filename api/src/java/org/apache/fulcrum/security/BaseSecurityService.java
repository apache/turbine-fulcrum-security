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

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.thread.ThreadSafe;
/**
 * This a basis implementation of the Fulcrum security service.
 *
 * Provided functionality includes:
 * <ul>
 * <li>methods for retrieving different types of managers.
 * <li>avalon lifecyle managers.
 * </ul>
 *
 * @author <a href="mailto:epugh@upstate.com">Eric PUgh</a>
 * @version $Id$
 */
public class BaseSecurityService
    extends AbstractLogEnabled
    implements SecurityService, Initializable, Serviceable, ThreadSafe
{
    private ServiceManager manager = null;
    // management of Groups/Role/Permissions

    // temporary storage of the classnames prior to initialization
    String userClassName;
    String groupClassName;
    String permissionClassName;
    String roleClassName;
    String aclClassName;
    /** The instance of UserManager the SecurityService uses */
    protected UserManager userManager = null;
    /** The instance of RoleManager the SecurityService uses */
    protected RoleManager roleManager = null;
    /** The instance of GroupManager the SecurityService uses */
    protected GroupManager groupManager = null;
    /** The instance of PermissionManager the SecurityService uses */
    protected PermissionManager permissionManager = null;
    /** The instance of ModelManager the SecurityService uses */
    protected ModelManager modelManager = null;

    /**
	 * Returns the configured UserManager.
	 *
	 * @return An UserManager object
	 */
    public UserManager getUserManager()
    {
        if (userManager == null)
        {
            try
            {
                userManager = (UserManager) manager.lookup(UserManager.ROLE);
            }
            catch (ServiceException ce)
            {
                throw new RuntimeException(ce.getMessage(), ce);
            }
        }
        return userManager;
    }
    /**
	 * Returns the configured GroupManager.
	 *
	 * @return An UserManager object
	 */
    public GroupManager getGroupManager()
    {
        if (groupManager == null)
        {
            try
            {
                groupManager = (GroupManager) manager.lookup(GroupManager.ROLE);
            }
            catch (ServiceException ce)
            {
                throw new RuntimeException(ce.getMessage(), ce);
            }
        }
        return groupManager;
    }
    /**
	 * Returns the configured RoleManager.
	 *
	 * @return An RoleManager object
	 */
    public RoleManager getRoleManager()
    {
        if (roleManager == null)
        {
            try
            {
                roleManager = (RoleManager) manager.lookup(RoleManager.ROLE);
            }
            catch (ServiceException ce)
            {
                throw new RuntimeException(ce.getMessage(), ce);
            }
        }
        return roleManager;
    }

    /**
	 * Returns the configured PermissionManager.
	 *
	 * @return An PermissionManager object
	 */
    public PermissionManager getPermissionManager()
    {
        if (permissionManager == null)
        {
            try
            {
                permissionManager = (PermissionManager) manager.lookup(PermissionManager.ROLE);
            }
            catch (ServiceException ce)
            {
                throw new RuntimeException(ce.getMessage(), ce);
            }
        }
        return permissionManager;
    }

    /**
	 * Returns the configured ModelManager.
	 *
	 * @return An ModelManager object
	 */
    public ModelManager getModelManager()
    {
        if (modelManager == null)
        {
            try
            {
                modelManager = (ModelManager) manager.lookup(ModelManager.ROLE);
            }
            catch (ServiceException ce)
            {
                throw new RuntimeException(ce.getMessage(), ce);
            }
        }
        return modelManager;
    }

    /**
	 * Configure a new role Manager.
	 *
	 * @param permissionManager An PermissionManager object
	 */
    // void setPermissionManager(PermissionManager permissionManager);

    /**
	 * Avalon Service lifecycle method
	 */
    public void service(ServiceManager manager) throws ServiceException
    {
        this.manager = manager;
    }

    /**
	 * Avalon Service lifecycle method Initializes the SecurityService, locating the appropriate
	 * UserManager
	 *
	 * @throws Exception A Problem occurred while initializing the User Manager.
	 */
    public void initialize() throws Exception
    {
        userClassName = null;
        groupClassName = null;
        permissionClassName = null;
        roleClassName = null;
        aclClassName = null;
    }

    /**
	 * Avalon Service lifecycle method
	 */
    public void dispose()
    {
        manager.release(userManager);
        manager.release(roleManager);
        manager.release(groupManager);
        manager.release(permissionManager);
        manager.release(modelManager);
        manager = null;
    }
}
