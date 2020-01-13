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

import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.model.turbine.entity.TurbineRole;
import org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRole;
import org.apache.fulcrum.security.torque.om.TurbineRolePeer;
import org.apache.fulcrum.security.torque.om.TurbineRolePermission;
import org.apache.fulcrum.security.torque.om.TurbineRolePermissionPeer;
import org.apache.fulcrum.security.torque.om.TurbineUserGroupRolePeer;
import org.apache.fulcrum.security.torque.peer.TurbineRolePermissionPeerMapper;
import org.apache.fulcrum.security.torque.peer.TurbineUserGroupRoleModelPeerMapper;
import org.apache.fulcrum.security.torque.security.turbine.TorqueAbstractTurbineTurbineSecurityEntity;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.PermissionSet;
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
public abstract class DefaultAbstractTurbineRole extends TorqueAbstractTurbineTurbineSecurityEntity
    implements TurbineRole
{
    /** Serial version */
	private static final long serialVersionUID = -1782236723198646728L;

	/** a cache of permission objects */
    private Set<Permission> permissionSet = null;

    /**
     * Forward reference to generated code
     *
     * Get a list of association objects, pre-populated with their TurbinePermission
     * objects.
     *
     * @param criteria Criteria to define the selection of records
     * @param con a database connection
     * @throws TorqueException  if any database error occurs
     *
     * @return a list of Role/Permission relations
     */
    protected <T extends TurbineRolePermissionPeerMapper> List<T> getTurbineRolePermissionsJoinTurbinePermission(Criteria criteria, Connection con)
        throws TorqueException
    {
        criteria.and(TurbineRolePermissionPeer.ROLE_ID, getEntityId() );
        return (List<T>) TurbineRolePermissionPeer.doSelectJoinTurbinePermission(criteria, con);
    }

    /**
     * Forward reference to generated code
     *
     * Get a list of association objects, pre-populated with their TurbineGroup
     * objects.
     *
     * @param criteria Criteria to define the selection of records
     * @param con a database connection
     * @throws TorqueException  if any database error occurs
     *
     * @return a list of User/Group/Role relations
     */
    protected <T extends TurbineUserGroupRoleModelPeerMapper> List<T> getTurbineUserGroupRolesJoinTurbineGroup(Criteria criteria, Connection con)
            throws TorqueException, DataBackendException
    {
        criteria.and(TurbineUserGroupRolePeer.ROLE_ID, getEntityId() );
        return (List<T>) TurbineUserGroupRolePeer.doSelectJoinTurbineGroup(criteria, con);
    }

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbineRole#addPermission(org.apache.fulcrum.security.entity.Permission)
     */
    @Override
	public void addPermission(Permission permission)
    {
        getPermissions().add(permission);
    }

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbineRole#getPermissions()
     */
    @Override
	public PermissionSet getPermissions()
    {
        if (permissionSet == null)
        {
            permissionSet = new PermissionSet();
        }
        else if(!(permissionSet instanceof PermissionSet))
        {
            permissionSet = new PermissionSet(permissionSet);
        }

        return (PermissionSet)permissionSet;
    }

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbineRole#getPermissionsAsSet()
     */
    @Override
	@SuppressWarnings("unchecked")
	public <T extends Permission> Set<T> getPermissionsAsSet()
    {
        return (Set<T>)permissionSet;
    }

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbineRole#removePermission(org.apache.fulcrum.security.entity.Permission)
     */
    @Override
	public void removePermission(Permission permission)
    {
        getPermissions().remove(permission);
    }

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbineRole#setPermissions(org.apache.fulcrum.security.util.PermissionSet)
     */
    @Override
	public void setPermissions(PermissionSet permissionSet)
    {
        if (permissionSet != null)
        {
            this.permissionSet = permissionSet;
        }
        else
        {
            this.permissionSet = new PermissionSet();
        }
    }

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbineRole#setPermissionsAsSet(java.util.Set)
     */
    @Override
	public <T extends Permission> void setPermissionsAsSet(Set<T> permissions)
    {
        setPermissions(new PermissionSet(permissions));
    }
    
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
    public void retrieveAttachedObjects( Connection con, Boolean lazy )
        throws DataBackendException
    {
        this.permissionSet = new PermissionSet();

        try {
            List<TurbineRolePermissionPeerMapper> rolepermissions = getTurbineRolePermissionsJoinTurbinePermission(new Criteria(), con);
    
            for (TurbineRolePermissionPeerMapper ttrp : rolepermissions)
            {
                permissionSet.add(ttrp.getTurbinePermission());
            }
    
            if (!lazy) {
                Set<TurbineUserGroupRole> userGroupRoleSet = new HashSet<TurbineUserGroupRole>();
        
                List<TurbineUserGroupRoleModelPeerMapper> ugrs = getTurbineUserGroupRolesJoinTurbineGroup(new Criteria(), con);
        
                for (TurbineUserGroupRoleModelPeerMapper ttugr : ugrs)
                {
                    TurbineUserGroupRole ugr = new TurbineUserGroupRole();
                    ugr.setRole(this);
                    ugr.setGroup(ttugr.getTurbineGroup());
                    ugr.setUser(ttugr.getTurbineUser(con));
                    userGroupRoleSet.add(ugr);
                }
        
                setUserGroupRoleSet(userGroupRoleSet);
            
            }
        } catch (TorqueException e ) {
            throw new DataBackendException( e.getMessage(),e );
        }
    }
    
    /* (non-Javadoc)
     * @see org.apache.fulcrum.security.torque.security.turbine.TorqueAbstractTurbineTurbineSecurityEntity#getUserGroupRoleSet()
     */
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
    
    
    /* (non-Javadoc)
     * @see org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity#update(java.sql.Connection)
     */
    @Override
	public void update(Connection con) throws TorqueException
    {
        if (permissionSet != null && !permissionSet.isEmpty())
        {
            Criteria criteria = new Criteria();

            /* remove old entries */
            criteria.where(TurbineRolePermissionPeer.ROLE_ID, getEntityId());
            TurbineRolePermissionPeer.doDelete(criteria, con);

            for (Permission p : permissionSet)
            {
                TurbineRolePermission rp = new TurbineRolePermission();
                rp.setPermissionId((Integer)p.getId());
                rp.setRoleId(getEntityId());
                rp.save(con);
            }
        }

        // role permission change does not require reassignment of user-group-roles? commented for now   
//        try
//        {
//            Set<TurbineUserGroupRole> userGroupRoleSet = getUserGroupRoleSet();
//            if (userGroupRoleSet != null && !userGroupRoleSet.isEmpty())
//            {
//                Criteria criteria = new Criteria();
//
//                criteria.where(TurbineUserGroupRolePeer.ROLE_ID, getEntityId());
//                TurbineUserGroupRolePeer.doDelete(criteria, con);
//
//                for (TurbineUserGroupRole ugr : userGroupRoleSet)
//                {
//                    org.apache.fulcrum.security.torque.om.TurbineUserGroupRole ttugr = new org.apache.fulcrum.security.torque.om.TurbineUserGroupRole();
//                    ttugr.setGroupId((Integer)ugr.getGroup().getId());
//                    ttugr.setUserId((Integer)ugr.getUser().getId());
//                    ttugr.setRoleId((Integer)ugr.getRole().getId());
//                    ttugr.save(con);
//                }
//            }
//            save(con);
//        }
//        catch (Exception e)
//        {
//            throw new TorqueException(e);
//        }
    }

    /**
     * @see org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity#delete()
     */
    @Override
	public void delete() throws TorqueException
    {
        TurbineRolePeer.doDelete(SimpleKey.keyFor(getEntityId()));
    }
}
