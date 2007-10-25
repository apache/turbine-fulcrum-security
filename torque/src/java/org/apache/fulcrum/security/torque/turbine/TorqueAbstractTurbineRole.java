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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.model.turbine.entity.TurbineRole;
import org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRole;
import org.apache.fulcrum.security.torque.TorqueAbstractSecurityEntity;
import org.apache.fulcrum.security.torque.om.TorqueTurbinePermission;
import org.apache.fulcrum.security.torque.om.TorqueTurbineRolePeer;
import org.apache.fulcrum.security.torque.om.TorqueTurbineRolePermission;
import org.apache.fulcrum.security.torque.om.TorqueTurbineRolePermissionPeer;
import org.apache.fulcrum.security.torque.om.TorqueTurbineUserGroupRole;
import org.apache.fulcrum.security.torque.om.TorqueTurbineUserGroupRolePeer;
import org.apache.fulcrum.security.util.PermissionSet;
import org.apache.torque.TorqueException;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.util.Criteria;
/**
 * This abstract class provides the SecurityInterface to the managers.
 *
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id:$
 */
public abstract class TorqueAbstractTurbineRole extends TorqueAbstractSecurityEntity
    implements TurbineRole
{
    /** a cache of permission objects */
    private Set permissionSet = null;

    /** a cache of user_group_role objects */
    private Set userGroupRoleSet = null;

    /**
     * Forward reference to generated code
     *
     * Get a list of association objects, pre-populated with their TorqueTurbinePermission
     * objects.
     *
     * @param criteria Criteria to define the selection of records
     * @throws TorqueException
     *
     * @return a list of Role/Permission relations
     */
    protected abstract List getTorqueTurbineRolePermissionsJoinTorqueTurbinePermission(Criteria criteria)
        throws TorqueException;

    /**
     * Forward reference to generated code
     *
     * Get a list of association objects, pre-populated with their TorqueTurbineGroup
     * objects.
     *
     * @param criteria Criteria to define the selection of records
     * @throws TorqueException
     *
     * @return a list of User/Group/Role relations
     */
    protected abstract List getTorqueTurbineUserGroupRolesJoinTorqueTurbineGroup(Criteria criteria)
        throws TorqueException;

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbineRole#addPermission(org.apache.fulcrum.security.entity.Permission)
     */
    public void addPermission(Permission permission)
    {
        getPermissions().add(permission);
    }

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbineRole#addUserGroupRole(org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRole)
     */
    public void addUserGroupRole(TurbineUserGroupRole userGroupRole)
    {
        getUserGroupRoleSet().add(userGroupRole);
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
    public Set getPermissionsAsSet()
    {
        return permissionSet;
    }

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbineRole#getUserGroupRoleSet()
     */
    public Set getUserGroupRoleSet()
    {
        if (userGroupRoleSet == null)
        {
            userGroupRoleSet = new HashSet();
        }

        return userGroupRoleSet;
    }

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbineRole#removePermission(org.apache.fulcrum.security.entity.Permission)
     */
    public void removePermission(Permission permission)
    {
        getPermissions().remove(permission);
    }

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbineRole#removeUserGroupRole(org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRole)
     */
    public void removeUserGroupRole(TurbineUserGroupRole userGroupRole)
    {
        getUserGroupRoleSet().remove(userGroupRole);
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
    public void setPermissionsAsSet(Set permissions)
    {
        setPermissions(new PermissionSet(permissions));
    }

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbineRole#setUserGroupRoleSet(java.util.Set)
     */
    public void setUserGroupRoleSet(Set userGroupRoleSet)
    {
        if (userGroupRoleSet != null)
        {
            this.userGroupRoleSet = userGroupRoleSet;
        }
        else
        {
            this.userGroupRoleSet = new HashSet();
        }
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

        // the generated method that allows a Connection parameter is missing
        List rolepermissions = getTorqueTurbineRolePermissionsJoinTorqueTurbinePermission(new Criteria());

        for (Iterator i = rolepermissions.iterator(); i.hasNext();)
        {
            TorqueTurbineRolePermission ttrp = (TorqueTurbineRolePermission)i.next();
            permissionSet.add(ttrp.getTorqueTurbinePermission());
        }

        this.userGroupRoleSet = new HashSet();

        // the generated method that allows a Connection parameter is missing
        List ugrs = getTorqueTurbineUserGroupRolesJoinTorqueTurbineGroup(new Criteria());

        for (Iterator i = ugrs.iterator(); i.hasNext();)
        {
            TorqueTurbineUserGroupRole ttugr = (TorqueTurbineUserGroupRole)i.next();

            TurbineUserGroupRole ugr = new TurbineUserGroupRole();
            ugr.setRole(this);
            ugr.setGroup(ttugr.getTorqueTurbineGroup());
            ugr.setUser(ttugr.getTorqueTurbineUser(con));
            userGroupRoleSet.add(ugr);
        }
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
            criteria.add(TorqueTurbineRolePermissionPeer.ROLE_ID, getEntityId());
            TorqueTurbineRolePermissionPeer.doDelete(criteria, con);

            for (Iterator i = permissionSet.iterator(); i.hasNext();)
            {
                TorqueTurbinePermission permission = (TorqueTurbinePermission)i.next();

                TorqueTurbineRolePermission rp = new TorqueTurbineRolePermission();
                rp.setPermissionId(permission.getEntityId());
                rp.setRoleId(getEntityId());
                rp.save(con);
            }
        }

        if (userGroupRoleSet != null)
        {
            Criteria criteria = new Criteria();

            /* remove old entries */
            criteria.add(TorqueTurbineUserGroupRolePeer.ROLE_ID, getEntityId());
            TorqueTurbineUserGroupRolePeer.doDelete(criteria, con);

            for (Iterator i = userGroupRoleSet.iterator(); i.hasNext();)
            {
                TurbineUserGroupRole ugr = (TurbineUserGroupRole)i.next();

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
