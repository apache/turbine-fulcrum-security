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
import java.util.Set;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.fulcrum.security.GroupManager;
import org.apache.fulcrum.security.ModelManager;
import org.apache.fulcrum.security.RoleManager;
import org.apache.fulcrum.security.acl.AccessControlList;
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.ACLFactory;
import org.apache.fulcrum.security.model.turbine.entity.TurbineUser;
import org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRole;
import org.apache.fulcrum.security.spi.AbstractManager;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.FulcrumSecurityException;
import org.apache.fulcrum.security.util.UnknownEntityException;

/**
 *
 * This factory creates instance of the TurbineAccessControlList
 *
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id: DynamicACLFactory.java 1374616 2012-08-18 17:26:07Z tv $
 */
public class TurbineACLFactory extends AbstractManager implements ACLFactory
{
	
    private TurbineModelManager modelManager;
    
    /**
     * @return
     */
    protected TurbineModelManager getTurbineModelManager() throws DataBackendException
    {
        if (modelManager == null)
        {
            try
            {
            	modelManager = (TurbineModelManager) manager.lookup(ModelManager.ROLE);

            }
            catch (ServiceException ce)
            {
                throw new DataBackendException(ce.getMessage(), ce);
            }
        }
        return modelManager;
    }
	
    /**
     * @see org.apache.fulcrum.security.model.ACLFactory#getAccessControlList(org.apache.fulcrum.security.entity.User)
     */
    @Override
    public <T extends AccessControlList> T getAccessControlList(User user)
    {
    	TurbineUser tu = (TurbineUser)user;
    	Set<TurbineUserGroupRole> tugr = tu.getUserGroupRoleSet();

        try
        {
            @SuppressWarnings("unchecked")
			T aclInstance = (T) getAclInstance(tugr);
			return aclInstance;
        }
        catch (UnknownEntityException uue)
        {
            throw new RuntimeException(uue.getMessage(), uue);
        }
    }

    /**
     * Construct a new ACL object.
     *
     * This constructs a new ACL object from the configured class and
     * initializes it with the supplied roles and permissions.
     *
     * @param turbineUserGroupRoleSet
     *            The set of user/group/role relations that this acl is built from
     *
     * @return an object implementing ACL interface.
     * @throws UnknownEntityException
     *             if the object could not be instantiated.
     */
    private TurbineAccessControlList getAclInstance(Set<? extends TurbineUserGroupRole> turbineUserGroupRoleSet) throws UnknownEntityException
    {
    	GroupManager groupManager = null;
        RoleManager roleManager = null;
        TurbineModelManager modelManager = null;

    	try
    	{
    	    roleManager = getRoleManager();
			groupManager = getGroupManager();
			modelManager = getTurbineModelManager();

	        // make sure the global group exists
	        if (groupManager != null)
	        {
	            Group g = null;
	            try
	            {
	                g = groupManager.getGroupByName(modelManager.getGlobalGroupName());
	            }
	            catch (UnknownEntityException uee)
	            {
	                g = groupManager.getGroupInstance(modelManager.getGlobalGroupName());
	                try
	                {
	                    groupManager.addGroup(g);
	                }
	                catch (EntityExistsException eee)
	                {
	                    throw new DataBackendException(eee.getMessage(), eee);
	                }
	            }
	        }
    	}
    	catch (DataBackendException e)
    	{
    		// ignore
		}

    	TurbineAccessControlList accessControlList;
        try
        {
            accessControlList =
                new TurbineAccessControlListImpl(turbineUserGroupRoleSet,
                        groupManager, roleManager, modelManager);
        }
        catch (FulcrumSecurityException e)
        {
            throw new UnknownEntityException("Failed to instantiate an ACL implementation object", e);
        }
        return accessControlList;
    }
    
}
