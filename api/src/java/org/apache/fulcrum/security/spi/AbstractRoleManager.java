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
import org.apache.commons.lang.StringUtils;
import org.apache.fulcrum.security.RoleManager;
import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.UnknownEntityException;

/**
 *
 * This implementation keeps all objects in memory.  This is mostly meant to help
 * with testing and prototyping of ideas.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public abstract class AbstractRoleManager
    extends AbstractEntityManager
    implements RoleManager
{
	protected abstract Role persistNewRole(Role role) throws DataBackendException;

    /**
    	* Construct a blank Role object.
    	*
    	* This method calls getRoleClass, and then creates a new object using
    	* the default constructor.
    	*
    	* @return an object implementing Role interface.
    	* @throws DataBackendException if the object could not be instantiated.
    	*/
    public Role getRoleInstance() throws DataBackendException
    {
        Role role;
        try
        {
            role = (Role) Class.forName(getClassName()).newInstance();
        }
        catch (Exception e)
        {
            throw new DataBackendException(
                "Problem creating instance of class " + getClassName(),
                e);
        }

        return role;
    }

    /**
	* Construct a blank Role object.
	*
	* This method calls getRoleClass, and then creates a new object using
	* the default constructor.
	*
	* @param roleName The name of the role.
	*
	* @return an object implementing Role interface.
	*
	* @throws DataBackendException if the object could not be instantiated.
	*/
    public Role getRoleInstance(String roleName) throws DataBackendException
    {
        Role role = getRoleInstance();
        role.setName(roleName);
        return role;
    }

    /**
	* Retrieve a Role object with specified name.
	*
	* @param name the name of the Role.
	* @return an object representing the Role with specified name.
	* @throws DataBackendException if there was an error accessing the
	*         data backend.
	* @throws UnknownEntityException if the role does not exist.
	*/
    public Role getRoleByName(String name)
        throws DataBackendException, UnknownEntityException
    {
        Role role = getAllRoles().getRoleByName(name);
        if (role == null)
        {
            throw new UnknownEntityException("The specified role does not exist");
        }
        return role;
    }

    /**
	* Retrieve a Role object with specified Id.
	*
	* @param name the name of the Role.
	*
	* @return an object representing the Role with specified name.
	*
	* @throws UnknownEntityException if the permission does not
	*            exist in the database.
	* @throws DataBackendException if there is a problem accessing the
	*            storage.
	*/
    public Role getRoleById(Object id)
        throws DataBackendException, UnknownEntityException
    {
        Role role = getAllRoles().getRoleById(id);
        if (role == null)
        {
            throw new UnknownEntityException("The specified role does not exist");
        }
        return role;
    }

    /**
	* Creates a new role with specified attributes.
	*
	* @param role the object describing the role to be created.
	* @return a new Role object that has id set up properly.
	* @throws DataBackendException if there was an error accessing the data
	*         backend.
	* @throws EntityExistsException if the role already exists.
	*/
    public synchronized Role addRole(Role role)
        throws DataBackendException, EntityExistsException
    {
        boolean roleExists = false;
        if (StringUtils.isEmpty(role.getName()))
        {
            throw new DataBackendException("Could not create a role with empty name!");
        }
        if (role.getId() != null)
        {
            throw new DataBackendException("Could not create a role with an id!");
        }
        try
        {
            roleExists = checkExists(role);
            if (!roleExists)
            {
                return persistNewRole(role);
            }
        }
        catch (Exception e)
        {
            throw new DataBackendException("addRole(Role) failed", e);
        }

        // the only way we could get here without return/throw tirggered
        // is that the roleExists was true.
        throw new EntityExistsException("Role '" + role + "' already exists");
    }

    /**
	* Check whether a specified role exists.
	*
	* The name is used for looking up the role
	*
	* @param role The role to be checked.
	* @return true if the specified role exists
	* @throws DataBackendException if there was an error accessing
	*         the data backend.
	*/
	public boolean checkExists(Role role) throws DataBackendException
	{
	    return checkExists(role.getName());
	}


}
