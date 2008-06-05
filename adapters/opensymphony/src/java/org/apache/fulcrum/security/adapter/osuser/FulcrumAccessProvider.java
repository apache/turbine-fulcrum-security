package org.apache.fulcrum.security.adapter.osuser;
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

import java.util.ArrayList;
import java.util.List;

import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.dynamic.DynamicAccessControlList;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.UnknownEntityException;

import com.opensymphony.user.Entity.Accessor;
import com.opensymphony.user.provider.AccessProvider;

/**
 * Fulcrum provider for OSUser.  Primarily provides support for requesting
 * whether a user exists in a role.  In OSUser, there are no roles, just groups,
 * so this maps Fulcrum roles on OSUser groups.  This means some the the method
 * names refer to groups, but interact with Fulcrum roles.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class FulcrumAccessProvider
	extends BaseFulcrumProvider
	implements AccessProvider
{
	/*
	 * Not implemented.   Should use SecurityService directly.
	 *
	 * @see com.opensymphony.user.provider.AccessProvider#addToGroup(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean addToGroup(String username, String groupname)
	{
		return false;
	}

	/*
	 * Returns whether a user in part of a what OSUser calls a group. <strong>
	 * However, since Fulcrum Security has the concept of roles being
	 * assignable to groups, then what this method really checks is that the
	 * user has a specific role. </strong> This is because the mapping between
	 * OSUser and Fulcurm Security is not a 1 to 1 mapping.
	 *
	 * @see com.opensymphony.user.provider.AccessProvider#inGroup(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean inGroup(String username, String groupname)
	{
		try
		{
			User user = getSecurityService().getUserManager().getUser(username);
			DynamicAccessControlList acl =
			(DynamicAccessControlList)getSecurityService().getUserManager().getACL(user);
			Role role = acl.getRoles().getRoleByName(groupname);
			boolean result =acl.hasRole(role);
			return result;
		}
		catch (UnknownEntityException uee)
		{
			return false;
		}
		catch (DataBackendException dbe)
		{
			throw new RuntimeException(dbe);
		}

	}

	/*
	 * This returns all the ROLES that a user has. This is similar to the
	 * problems with the inGroup() method of this provider.
	 *
	 * @see com.opensymphony.user.provider.AccessProvider#listGroupsContainingUser(java.lang.String)
	 * @see org.apache.fulcrum.security.adapter.osuser.FulcrumAccessProvider#inGroup(java.lang.String,java.lang.String)
	 */
	public List listGroupsContainingUser(String username)
	{
		List roles = new ArrayList();
		try
		{
			User user = getSecurityService().getUserManager().getUser(username);
			DynamicAccessControlList acl =
				(DynamicAccessControlList)getSecurityService().getUserManager().getACL(user);
			roles.addAll(acl.getRoles().getNames());

		}
		catch (UnknownEntityException uee)
		{
			throw new RuntimeException(uee);
		}
		catch (DataBackendException dbe)
		{
			throw new RuntimeException(dbe);
		}
		return roles;

	}

	/*
	 * Not implemented yet.
	 *
	 * @see com.opensymphony.user.provider.AccessProvider#listUsersInGroup(java.lang.String)
	 */
	public List listUsersInGroup(String groupname)
	{
		return null;
	}

	/*
	 * Not implemented.  Should probably use SecurityService directly.
	 *
	 * @see com.opensymphony.user.provider.AccessProvider#removeFromGroup(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean removeFromGroup(String username, String groupname)
	{
		return false;
	}

	/*
	 * Not implemented.  Should use SecurityService directly.
	 *
	 * @see com.opensymphony.user.provider.UserProvider#create(java.lang.String)
	 */
	public boolean create(String name)
	{
		return false;
	}

	/*
	 * Doesn't do anything.
	 *
	 * @see com.opensymphony.user.provider.UserProvider#flushCaches()
	 */
	public void flushCaches()
	{

	}

	/*
	 * Returns true if the user exists, otherwise returns false.
	 *
	 * @see com.opensymphony.user.provider.UserProvider#handles(java.lang.String)
	 */
	public boolean handles(String name)
	{
		try
		{
			User user = getSecurityService().getUserManager().getUser(name);
			return true;
		}
		catch (UnknownEntityException uee)
		{
			return false;
		}
		catch (DataBackendException dbe)
		{
			throw new RuntimeException(dbe);
		}
	}



	/*
	 * not implemented.
	 *
	 * @see com.opensymphony.user.provider.UserProvider#list()
	 */
	public List list()
	{
		return null;
	}



	/*
	 * Not implemented.   Should use SecurityService directly.
	 *
	 * @see com.opensymphony.user.provider.UserProvider#remove(java.lang.String)
	 */
	public boolean remove(String name)
	{
		return false;
	}

	/*
	 * Not implemented.   Should use SecurityService directly.
	 *
	 * @see com.opensymphony.user.provider.UserProvider#store(java.lang.String,
	 *      com.opensymphony.user.Entity.Accessor)
	 */
	public boolean store(String arg0, Accessor arg1)
	{
		return false;
	}

}
