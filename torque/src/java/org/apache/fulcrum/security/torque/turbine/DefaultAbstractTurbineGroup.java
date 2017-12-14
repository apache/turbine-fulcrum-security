package org.apache.fulcrum.security.torque.turbine;
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
import java.sql.Connection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.fulcrum.security.model.turbine.entity.TurbineGroup;
import org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRole;
import org.apache.fulcrum.security.torque.om.TurbineGroupPeer;
import org.apache.fulcrum.security.torque.om.TurbineUserGroupRolePeer;
import org.apache.fulcrum.security.torque.security.turbine.TorqueAbstractTurbineTurbineSecurityEntity;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.torque.TorqueException;
import org.apache.torque.criteria.Criteria;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.util.Transaction;
/**
 * This abstract class provides the SecurityInterface to the managers.
 *
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id:$
 */
public abstract class DefaultAbstractTurbineGroup extends TorqueAbstractTurbineTurbineSecurityEntity
    implements TurbineGroup
{
    /** Serial version */
	private static final long serialVersionUID = -6230312046016785990L;

    /**
     * Forward reference to generated code
     *
     * Get a list of association objects, pre-populated with their TurbineRole
     * objects.
     *
     * @param criteria Criteria to define the selection of records
     * @param con a database connection
     * @throws TorqueException
     *
     * @return a list of User/Group/Role relations
     */
    protected List<org.apache.fulcrum.security.torque.om.TurbineUserGroupRole> getTurbineUserGroupRolesJoinTurbineRole(Criteria criteria, Connection con)
        throws TorqueException
    {
        criteria.and(TurbineUserGroupRolePeer.GROUP_ID, getEntityId() );
        return TurbineUserGroupRolePeer.doSelectJoinTurbineRole(criteria, con);
    }
    
    
    /**
     * @see TorqueAbstractTurbineTurbineSecurityEntity#retrieveAttachedObjects(Connection, boolean)
     */
    @Override
    public void retrieveAttachedObjects( Connection con, Boolean lazy ) throws TorqueException
    {
        if (!lazy) {
            Set<TurbineUserGroupRole> userGroupRoleSet = new HashSet<TurbineUserGroupRole>();
            
            List<org.apache.fulcrum.security.torque.om.TurbineUserGroupRole> ugrs = getTurbineUserGroupRolesJoinTurbineRole(new Criteria(), con);
    
            for (org.apache.fulcrum.security.torque.om.TurbineUserGroupRole ttugr : ugrs)
            {
                TurbineUserGroupRole ugr = new TurbineUserGroupRole();
                ugr.setGroup(this);
                ugr.setRole(ttugr.getTurbineRole());
                ugr.setUser(ttugr.getTurbineUser(con));
                userGroupRoleSet.add(ugr);
            }
    
            setUserGroupRoleSet(userGroupRoleSet);
        }
    }
    
   
    @Override
    public <T extends TurbineUserGroupRole> Set<T> getUserGroupRoleSet() throws DataBackendException
    {
        if (super.getUserGroupRoleSet() == null || super.getUserGroupRoleSet().isEmpty()) {
            Connection con = null;
            try
            {
                con = Transaction.begin();
               
                retrieveAttachedObjects( con, false ); // not configurable, we set it
    
                Transaction.commit(con);
                con = null;
            }
            catch (TorqueException e)
            {
                throw new DataBackendException("Error retrieving group information", e);
            }
            finally
            {
                if (con != null)
                {
                    Transaction.safeRollback(con);
                }
            }
        }
        
        return super.getUserGroupRoleSet();
        
    }

    /**
     * @see org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity#update(java.sql.Connection)
     */
    @Override
    public void update(Connection con) throws TorqueException
    {
        try
        {
            Set<TurbineUserGroupRole> userGroupRoleSet = getUserGroupRoleSet();
            if (userGroupRoleSet != null && !userGroupRoleSet.isEmpty())
            {
                Criteria criteria = new Criteria();
    
                /* remove old entries */
                criteria.where(TurbineUserGroupRolePeer.GROUP_ID, getEntityId());
                TurbineUserGroupRolePeer.doDelete(criteria, con);
    
                for (TurbineUserGroupRole ugr : userGroupRoleSet)
                {
                    org.apache.fulcrum.security.torque.om.TurbineUserGroupRole ttugr = new org.apache.fulcrum.security.torque.om.TurbineUserGroupRole();
                    ttugr.setGroupId((Integer)ugr.getGroup().getId());
                    ttugr.setUserId((Integer)ugr.getUser().getId());
                    ttugr.setRoleId((Integer)ugr.getRole().getId());
                    ttugr.save(con);
                }
            }
            save(con);
        }
        catch (Exception e)
        {
            throw new TorqueException(e);
        }
    }

    /**
     * @see org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity#delete()
     */
    @Override
    public void delete() throws TorqueException
    {
        TurbineGroupPeer.doDelete(SimpleKey.keyFor(getEntityId()));
    }
}
