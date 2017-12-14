package org.apache.fulcrum.security.torque.security.turbine;
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
import java.util.HashSet;
import java.util.Set;

import org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRole;
import org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRoleEntity;
import org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity;
import org.apache.fulcrum.security.util.DataBackendException;
/**
 * This abstract class provides the SecurityInterface to the managers.
 * 
 * Additional Torque contract
 *
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id:$
 */
public abstract class TorqueAbstractTurbineTurbineSecurityEntity extends TorqueAbstractSecurityEntity
    implements TurbineUserGroupRoleEntity // not in group and role but already in TurbineUser interface 
{
    /** Serial version */
	private static final long serialVersionUID = -6230312046016785990L;

	/** a cache of user_group_role objects */
    private Set<? extends TurbineUserGroupRole> userGroupRoleSet = null;

    /**
     * @throws DataBackendException 
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbineGroup#addUserGroupRole(org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRole)
     */
    public void addUserGroupRole(TurbineUserGroupRole userGroupRole) throws DataBackendException
    {
        getUserGroupRoleSet().add(userGroupRole);
    }

    /**
     * @throws DataBackendException if loaded lazily
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbineGroup#getUserGroupRoleSet()
     */
    @SuppressWarnings("unchecked")
	public <T extends TurbineUserGroupRole> Set<T> getUserGroupRoleSet() throws DataBackendException
    {
        if (userGroupRoleSet == null)
        {
            userGroupRoleSet = new HashSet<TurbineUserGroupRole>();
        }

        return (Set<T>)userGroupRoleSet;
    }

    /**
     * @throws DataBackendException 
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbineGroup#removeUserGroupRole(org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRole)
     */
    public void removeUserGroupRole(TurbineUserGroupRole userGroupRole) throws DataBackendException
    {
        getUserGroupRoleSet().remove(userGroupRole);
    }

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbineGroup#setUserGroupRoleSet(java.util.Set)
     */
    public <T extends TurbineUserGroupRole> void setUserGroupRoleSet(Set<T> userGroupRoleSet)
    {
        if (userGroupRoleSet != null)
        {
            this.userGroupRoleSet = userGroupRoleSet;
        }
        else
        {
            this.userGroupRoleSet = new HashSet<TurbineUserGroupRole>();
        }
    }
    
//    /**
//     * Retrieve attached objects 
//     *
//     * @param con A database connection
//     * @param lazy if <code>true</code>, does not retrieve user group role relationships
//     */
//    public abstract void retrieveAttachedObjects(Connection con, boolean lazy) throws TorqueException;
}
