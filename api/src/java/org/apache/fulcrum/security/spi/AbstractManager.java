package org.apache.fulcrum.security.spi;
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
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.fulcrum.security.GroupManager;
import org.apache.fulcrum.security.PermissionManager;
import org.apache.fulcrum.security.RoleManager;
import org.apache.fulcrum.security.UserManager;
import org.apache.fulcrum.security.util.DataBackendException;

/**
 *
 * This abstract implementation provides most of the functionality that
 * a manager will need.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public abstract class AbstractManager
    extends AbstractLogEnabled
    implements Serviceable, Disposable, ThreadSafe
{
    boolean composed = false;

    private ServiceManager manager = null;
    private PermissionManager permissionManager;
    private RoleManager roleManager;
    private GroupManager groupManager;
    private UserManager userManager;

    /**
     * @return
     */
    protected ServiceManager getServiceManager()
    {
        return manager;
    }

    /**
     * @return
     */
    protected UserManager getUserManager() throws DataBackendException
    {
        if (userManager == null)
        {
            try
            {
                userManager = (UserManager) manager.lookup(UserManager.ROLE);

            }
            catch (ServiceException ce)
            {
                throw new DataBackendException(ce.getMessage(), ce);
            }
        }
        return userManager;
    }

    /**
     * @return
     */
    protected PermissionManager getPermissionManager()
        throws DataBackendException
    {
        if (permissionManager == null)
        {
            try
            {
                permissionManager =
                    (PermissionManager) manager.lookup(PermissionManager.ROLE);

            }
            catch (ServiceException ce)
            {
                throw new DataBackendException(ce.getMessage(), ce);
            }
        }
        return permissionManager;
    }

    /**
     * @return
     */
    protected RoleManager getRoleManager() throws DataBackendException
    {
        if (roleManager == null)
        {
            try
            {
                roleManager = (RoleManager) manager.lookup(RoleManager.ROLE);

            }
            catch (ServiceException ce)
            {
                throw new DataBackendException(ce.getMessage(), ce);
            }
        }
        return roleManager;
    }

    /**
     * @return
     */
    protected GroupManager getGroupManager() throws DataBackendException
    {
        if (groupManager == null)
        {
            try
            {
                groupManager = (GroupManager) manager.lookup(GroupManager.ROLE);

            }
            catch (ServiceException ce)
            {
                throw new DataBackendException(ce.getMessage(), ce);
            }
        }
        return groupManager;
    }

    /**
    * Avalon Service lifecycle method
    */
    public void service(ServiceManager manager) throws ServiceException
    {
        this.manager = manager;

    }

    public void dispose()
    {
		release(roleManager);
		release(permissionManager);
		release(groupManager);
		release(userManager);
        manager = null;
    }

    protected void release(Object obj)
    {
        if(obj!=null){
            manager.release(obj);
            obj = null;
        }
    }


    /**
     * @return A resolved object
     * @throws DataBackendException if the backend failed for some reason.
     */
    protected Object resolve(String lookup)
    {
        Object component = null;
        {
            try
            {
                component = manager.lookup(lookup);
            }
            catch (ServiceException ce)
            {
                throw new RuntimeException(ce.getMessage(), ce);
            }
        }
        return component;
    }

}
