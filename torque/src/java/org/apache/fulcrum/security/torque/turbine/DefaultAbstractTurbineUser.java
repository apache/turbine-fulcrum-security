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
import org.apache.fulcrum.security.torque.om.TurbineUserGroupRolePeer;
import org.apache.fulcrum.security.torque.om.TurbineUserPeer;
import org.apache.fulcrum.security.torque.peer.TurbineUserGroupRoleModelPeerMapper;
import org.apache.fulcrum.security.torque.security.turbine.TorqueAbstractTurbineTurbineSecurityEntityDefault;
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
public abstract class DefaultAbstractTurbineUser extends TorqueAbstractTurbineTurbineSecurityEntityDefault
{
    /** Serial version */
	private static final long serialVersionUID = -7255623655281852566L;
    
    /**
     * Forward reference to module generated code
     *
     * Get a list of association objects, pre-populated with their TurbineGroup
     * objects.
     * 
     * Does intentionally not initialize the cache collTurbineUserGroupRoles for referenced objects. 
     * 
     * Be careful not to call any of the generated getTurbineUserGroupRoles methods in derived classes,
     * the link {@link #save()} method otherwise might not update the right relationships.
     *
     * @param criteria Criteria to define the selection of records
     * @param con a database connection
     * @throws DataBackendException  
     *
     * @return a list of User/Group/Role relations
     * @throws TorqueException if any database error occurs
     */
    protected <T extends TurbineUserGroupRoleModelPeerMapper> List<T> getTurbineUserGroupRolesJoinTurbineGroup(Criteria criteria, Connection con)
        throws TorqueException, DataBackendException
    {
        criteria.and(TurbineUserGroupRolePeer.USER_ID, getEntityId() );
        try {
            return (List<T>) TurbineUserGroupRolePeer.doSelectJoinTurbineGroup(criteria, con);
        } catch (  TorqueException e) {
            throw new DataBackendException( e.getMessage(), e );
        }
    }
    
    /** 
     * @see org.apache.fulcrum.security.torque.security.turbine.TorqueAbstractTurbineTurbineSecurityEntityDefault#retrieveAttachedObjects(java.sql.Connection, java.lang.Boolean, java.util.List)
     */
    @Override
    public <T extends TurbineUserGroupRoleModelPeerMapper> void retrieveAttachedObjects( Connection con, Boolean lazy, List<T> ugrs ) throws DataBackendException, TorqueException
    {
        if (!lazy ) { 
            Set<TurbineUserGroupRole> userGroupRoleSet = new HashSet<TurbineUserGroupRole>();
    
            if (ugrs == null) { // default 
                // the groups are the keys in roleset, roles are not expected to be used as keys
                ugrs = getTurbineUserGroupRolesJoinTurbineGroup(new Criteria(), con);
            } 
    
            maptoModel( con, userGroupRoleSet, ugrs );
            
            setUserGroupRoleSet(userGroupRoleSet);
        }
    }
    
    /* (non-Javadoc)
     * @see org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity#retrieveAttachedObjects(java.sql.Connection, java.lang.Boolean)
     */
    @Override
    public void retrieveAttachedObjects( Connection con, Boolean lazy ) throws TorqueException, DataBackendException
    {
        if (!lazy) {
            Set<TurbineUserGroupRole> userGroupRoleSet = new HashSet<TurbineUserGroupRole>();
            
            List<TurbineUserGroupRoleModelPeerMapper> ugrs = getTurbineUserGroupRolesJoinTurbineGroup(new Criteria(), con);
    
            // TODO: Need to call this too to fetch right role object? getTurbineUserGroupRolesJoinTurbineRole
            // org.apache.fulcrum.security.torque.om.TurbineUserGroupRole
            maptoModel( con, userGroupRoleSet, ugrs );
    
            setUserGroupRoleSet(userGroupRoleSet);
        }
    }

    /* (non-Javadoc)
     * @see org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity#retrieveAttachedObjects(java.sql.Connection)
     */
    @Override
    public void retrieveAttachedObjects( Connection con )
        throws DataBackendException, TorqueException
    {
        retrieveAttachedObjects( con, false );
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
            if ( userGroupRoleSet != null ) //&& !userGroupRoleSet.isEmpty() commented allow delete/empty 
            {
                Criteria criteria = new Criteria();

                /* remove old entries */
                criteria.where(TurbineUserGroupRolePeer.USER_ID, getEntityId());
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

    /* (non-Javadoc)
     * @see org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity#delete()
     */
    @Override
	public void delete() throws TorqueException
    {
        TurbineUserPeer.doDelete(SimpleKey.keyFor(getEntityId()));
    }
    
    /**
     * @param con data connection
     * @param userGroupRoleSet U/G/R to be set / target object
     * @param ugrs list of all ugrs
     * @throws TorqueException if data connection could not be found
     */
    private <T extends TurbineUserGroupRoleModelPeerMapper> void maptoModel( Connection con, 
            Set<TurbineUserGroupRole> userGroupRoleSet,
                             List<T> ugrs )
        throws DataBackendException
    {
        try {
            for (TurbineUserGroupRoleModelPeerMapper ttugr : ugrs)
            {
                TurbineUserGroupRole ugr = new TurbineUserGroupRole();
                ugr.setUser((User) this);
                ugr.setRole(ttugr.getTurbineRole(con));
                
                // org.apache.fulcrum.security.torque.om.TurbineGroup implements 
                // org.apache.fulcrum.security.model.turbine.entity.TurbineGroup
                // but may be hides it? 
                ugr.setGroup(ttugr.getTurbineGroup(con));
                userGroupRoleSet.add(ugr);
            }
        } catch (TorqueException e ) {
            throw new DataBackendException( e.getMessage(),e );
        }
    }
}
