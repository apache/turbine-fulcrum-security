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
import org.apache.fulcrum.security.torque.om.TorqueTurbineRolePeer;
import org.apache.fulcrum.security.torque.om.TorqueTurbineRolePermission;
import org.apache.fulcrum.security.torque.om.TorqueTurbineRolePermissionPeer;
import org.apache.fulcrum.security.torque.om.TorqueTurbineUserGroupRole;
import org.apache.fulcrum.security.torque.om.TorqueTurbineUserGroupRolePeer;
import org.apache.fulcrum.security.util.PermissionSet;
import org.apache.torque.TorqueException;
import org.apache.torque.criteria.Criteria;
import org.apache.torque.om.SimpleKey;
/**
 * This abstract class provides the SecurityInterface to the managers.
 *
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id:$
 */
public abstract class TorqueAbstractTurbineRole extends TorqueAbstractTurbineTurbineSecurityEntity
    implements TurbineRole
{
    /** Serial version */
	private static final long serialVersionUID = -1782236723198646728L;

	/** a cache of permission objects */
    private Set<Permission> permissionSet = null;

    /**
     * Forward reference to generated code
     *
     * Get a list of association objects, pre-populated with their TorqueTurbinePermission
     * objects.
     *
     * @param criteria Criteria to define the selection of records
     * @param con a database connection
     * @throws TorqueException
     *
     * @return a list of Role/Permission relations
     */
    protected abstract List<TorqueTurbineRolePermission> getTorqueTurbineRolePermissionsJoinTorqueTurbinePermission(Criteria criteria, Connection con)
        throws TorqueException;

    /**
     * Forward reference to generated code
     *
     * Get a list of association objects, pre-populated with their TorqueTurbineGroup
     * objects.
     *
     * @param criteria Criteria to define the selection of records
     * @param con a database connection
     * @throws TorqueException
     *
     * @return a list of User/Group/Role relations
     */
    protected abstract List<TorqueTurbineUserGroupRole> getTorqueTurbineUserGroupRolesJoinTorqueTurbineGroup(Criteria criteria, Connection con)
        throws TorqueException;

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbineRole#addPermission(org.apache.fulcrum.security.entity.Permission)
     */
    public void addPermission(Permission permission)
    {
        getPermissions().add(permission);
    }

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbineRole#getPermissions()
     */
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
    @SuppressWarnings("unchecked")
	public <T extends Permission> Set<T> getPermissionsAsSet()
    {
        return (Set<T>)permissionSet;
    }

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbineRole#removePermission(org.apache.fulcrum.security.entity.Permission)
     */
    public void removePermission(Permission permission)
    {
        getPermissions().remove(permission);
    }

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbineRole#setPermissions(org.apache.fulcrum.security.util.PermissionSet)
     */
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
    public <T extends Permission> void setPermissionsAsSet(Set<T> permissions)
    {
        setPermissions(new PermissionSet(permissions));
    }

    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractSecurityEntity#getDatabaseName()
     */
    public String getDatabaseName()
    {
        return TorqueTurbineRolePeer.DATABASE_NAME;
    }

    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractSecurityEntity#retrieveAttachedObjects(java.sql.Connection)
     */
    public void retrieveAttachedObjects(Connection con) throws TorqueException
    {
        this.permissionSet = new PermissionSet();

        List<TorqueTurbineRolePermission> rolepermissions = getTorqueTurbineRolePermissionsJoinTorqueTurbinePermission(new Criteria(), con);

        for (TorqueTurbineRolePermission ttrp : rolepermissions)
        {
            permissionSet.add(ttrp.getTorqueTurbinePermission());
        }

        Set<TurbineUserGroupRole> userGroupRoleSet = new HashSet<TurbineUserGroupRole>();

        List<TorqueTurbineUserGroupRole> ugrs = getTorqueTurbineUserGroupRolesJoinTorqueTurbineGroup(new Criteria(), con);

        for (TorqueTurbineUserGroupRole ttugr : ugrs)
        {
            TurbineUserGroupRole ugr = new TurbineUserGroupRole();
            ugr.setRole(this);
            ugr.setGroup(ttugr.getTorqueTurbineGroup());
            ugr.setUser(ttugr.getTorqueTurbineUser(con));
            userGroupRoleSet.add(ugr);
        }

        setUserGroupRoleSet(userGroupRoleSet);
    }

    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractSecurityEntity#update(java.sql.Connection)
     */
    public void update(Connection con) throws TorqueException
    {
        if (permissionSet != null)
        {
            Criteria criteria = new Criteria();

            /* remove old entries */
            criteria.where(TorqueTurbineRolePermissionPeer.ROLE_ID, getEntityId());
            TorqueTurbineRolePermissionPeer.doDelete(criteria, con);

            for (Permission p : permissionSet)
            {
                TorqueTurbineRolePermission rp = new TorqueTurbineRolePermission();
                rp.setPermissionId((Integer)p.getId());
                rp.setRoleId(getEntityId());
                rp.save(con);
            }
        }

    	Set<TurbineUserGroupRole> userGroupRoleSet = getUserGroupRoleSet();
        if (userGroupRoleSet != null)
        {
            Criteria criteria = new Criteria();

            /* remove old entries */
            criteria.where(TorqueTurbineUserGroupRolePeer.ROLE_ID, getEntityId());
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

        try
        {
            save(con);
        }
        catch (Exception e)
        {
            throw new TorqueException(e);
        }
    }

    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractSecurityEntity#delete()
     */
    public void delete() throws TorqueException
    {
        TorqueTurbineRolePeer.doDelete(SimpleKey.keyFor(getEntityId()));
    }
}
