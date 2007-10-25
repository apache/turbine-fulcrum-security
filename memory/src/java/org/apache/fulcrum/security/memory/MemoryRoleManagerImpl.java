package org.apache.fulcrum.security.memory;
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
import org.apache.fulcrum.security.spi.AbstractRoleManager;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.RoleSet;
import org.apache.fulcrum.security.util.UnknownEntityException;

/**
 *
 * This implementation keeps all objects in memory.  This is mostly meant to help
 * with testing and prototyping of ideas.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class MemoryRoleManagerImpl extends AbstractRoleManager
{
    /** List to store all our roles in */
    private static List roles = new ArrayList();

    /** Our Unique ID counter */
    // private static int uniqueId = 0;

    /**
    	* Renames an existing Role.
    	*
    	* @param role The object describing the role to be renamed.
    	* @param name the new name for the role.
    	* @throws DataBackendException if there was an error accessing the data
    	*         backend.
    	* @throws UnknownEntityException if the role does not exist.
    	*/
    public synchronized void renameRole(Role role, String name)
        throws DataBackendException, UnknownEntityException
    {
        boolean roleExists = false;
        try
        {
            roleExists = checkExists(role);
            if (roleExists)
            {
                roles.remove(role);
                role.setName(name);
                roles.add(role);
                return;
            }
        }
        catch (Exception e)
        {
            throw new DataBackendException("renameRole(Role,String)", e);
        }
        finally
        {
        }
        throw new UnknownEntityException("Unknown role '" + role + "'");
    }

    /**
     * Determines if the <code>Role</code> exists in the security system.
     *
     * @param permission a <code>String</code> value
     * @return true if the role exists in the system, false otherwise
     * @throws DataBackendException when more than one Role with the same name exists.
     * @throws Exception A generic exception.
     */
    public boolean checkExists(String roleName)
    {
        return MemoryHelper.checkExists(roles,roleName);
    }
    /**
    		 * Retrieves all roles defined in the system.
    		 *
    		 * @return the names of all roles defined in the system.
    		 * @throws DataBackendException if there was an error accessing the
    		 *         data backend.
    		 */
    public RoleSet getAllRoles() throws DataBackendException
    {
        return new RoleSet(roles);
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
    protected synchronized Role persistNewRole(Role role)
        throws DataBackendException
    {

        role.setId(MemoryHelper.getUniqueId());
        roles.add(role);
        // add the role to system-wide cache
        getAllRoles().add(role);
        // return the object with correct id
        return role;

    }

    /**
    * Removes a Role from the system.
    *
    * @param role The object describing the role to be removed.
    * @throws DataBackendException if there was an error accessing the data
    *         backend.
    * @throws UnknownEntityException if the role does not exist.
    */
    public synchronized void removeRole(Role role)
        throws DataBackendException, UnknownEntityException
    {
        boolean roleExists = false;
        try
        {
            roleExists = checkExists(role);
            if (roleExists)
            {
                roles.remove(role);
                getAllRoles().remove(role);
                return;
            }
        }
        catch (Exception e)
        {
            throw new DataBackendException("removeRole(Role)", e);
        }
        finally
        {
        }
        throw new UnknownEntityException("Unknown role '" + role + "'");
    }

}
