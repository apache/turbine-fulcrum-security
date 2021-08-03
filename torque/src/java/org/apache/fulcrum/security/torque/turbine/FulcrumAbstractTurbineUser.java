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

import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.turbine.entity.TurbineUser;
import org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRole;
import org.apache.fulcrum.security.torque.om.TorqueTurbineUserGroupRole;
import org.apache.fulcrum.security.torque.om.TorqueTurbineUserGroupRolePeer;
import org.apache.fulcrum.security.torque.om.TorqueTurbineUserPeer;
import org.apache.fulcrum.security.torque.security.turbine.TorqueAbstractTurbineTurbineSecurityEntity;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.torque.TorqueException;
import org.apache.torque.criteria.Criteria;
import org.apache.torque.om.SimpleKey;
/**
 * This abstract class provides the SecurityInterface to the managers.
 * 
 * An implementing class is required to implement {@link User} at least. Most probably the OM classes allow to implement {@link TurbineUser}, which includes User.
 *
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id:$
 */
public abstract class FulcrumAbstractTurbineUser extends TorqueAbstractTurbineTurbineSecurityEntity
{
    /** Serial version */
	private static final long serialVersionUID = -7255623655281852566L;

    /**
     * Forward reference to generated code
     *
     * Get a list of association objects, pre-populated with their TorqueTurbineRole
     * objects.
     *
     * @param criteria Criteria to define the selection of records
     * @param con a database connection
     * @throws TorqueException  if any database error occurs
     *
     * @return a list of User/Group/Role relations
     */
    protected List<TorqueTurbineUserGroupRole> getTorqueTurbineUserGroupRolesJoinTorqueTurbineGroup(Criteria criteria, Connection con)
        throws TorqueException
    {
        criteria.and(TorqueTurbineUserGroupRolePeer.USER_ID, getEntityId() );
        return TorqueTurbineUserGroupRolePeer.doSelectJoinTorqueTurbineGroup(criteria, con);
    }
    
    /* (non-Javadoc)
     * @see org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity#retrieveAttachedObjects(java.sql.Connection)
     */
    @Override
    public void retrieveAttachedObjects( Connection con )
        throws DataBackendException
    {
        retrieveAttachedObjects( con, false );
    }

    /* (non-Javadoc)
     * @see org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity#retrieveAttachedObjects(java.sql.Connection, java.lang.Boolean)
     */
    @Override
    public void retrieveAttachedObjects(Connection con, Boolean lazy) throws DataBackendException
    {
        try {
            if (!lazy) {
                Set<TurbineUserGroupRole> userGroupRoleSet = new HashSet<TurbineUserGroupRole>();
                List<TorqueTurbineUserGroupRole> ugrs = getTorqueTurbineUserGroupRolesJoinTorqueTurbineGroup(new Criteria(), con);
        
                for (TorqueTurbineUserGroupRole ttugr : ugrs)
                {
                    TurbineUserGroupRole ugr = new TurbineUserGroupRole();
                    ugr.setUser((User) this);
                    ugr.setRole(ttugr.getTorqueTurbineRole());
                    ugr.setGroup(ttugr.getTorqueTurbineGroup(con));
                    userGroupRoleSet.add(ugr);
                }
                setUserGroupRoleSet(userGroupRoleSet);
            }
       } catch (TorqueException e ) {
            throw new DataBackendException( e.getMessage(),e );
       }
    }

    /* (non-Javadoc)
     * @see org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity#update(java.sql.Connection)
     */
    @Override
	public void update(Connection con) throws TorqueException
    {
        try
        {
        	Set<TurbineUserGroupRole> userGroupRoleSet = getUserGroupRoleSet();
            if (userGroupRoleSet != null)
            {
                Criteria criteria = new Criteria();
    
                /* remove old entries */
                criteria.where(TorqueTurbineUserGroupRolePeer.USER_ID, getEntityId());
                TorqueTurbineUserGroupRolePeer.doDelete(criteria, con);
    
                for (TurbineUserGroupRole ugr : userGroupRoleSet)
                {
                    TorqueTurbineUserGroupRole ttugr = new TorqueTurbineUserGroupRole();
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

    
    /* (non-Javadoc)
     * @see org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity#delete()
     */
    @Override
	public void delete() throws TorqueException
    {
        TorqueTurbineUserPeer.doDelete(SimpleKey.keyFor(getEntityId()));
    }
}
