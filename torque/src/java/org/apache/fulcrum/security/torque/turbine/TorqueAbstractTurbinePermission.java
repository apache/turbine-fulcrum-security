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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.model.turbine.entity.TurbinePermission;
import org.apache.fulcrum.security.torque.TorqueAbstractSecurityEntity;
import org.apache.fulcrum.security.torque.om.TorqueTurbinePermissionPeer;
import org.apache.fulcrum.security.torque.om.TorqueTurbineRole;
import org.apache.fulcrum.security.torque.om.TorqueTurbineRolePermission;
import org.apache.fulcrum.security.torque.om.TorqueTurbineRolePermissionPeer;
import org.apache.fulcrum.security.util.RoleSet;
import org.apache.torque.TorqueException;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.util.Criteria;
/**
 * This abstract class provides the SecurityInterface to the managers.
 *
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id:$
 */
public abstract class TorqueAbstractTurbinePermission extends TorqueAbstractSecurityEntity
    implements TurbinePermission
{
    /** a cache of role objects */
    private Set roleSet = null;

    /**
     * Forward reference to generated code
     *
     * Get a list of association objects, pre-populated with their TorqueTurbineRole
     * objects.
     *
     * @param criteria Criteria to define the selection of records
     * @throws TorqueException
     *
     * @return a list of Role/Permission relations
     */
    protected abstract List getTorqueTurbineRolePermissionsJoinTorqueTurbineRole(Criteria criteria)
        throws TorqueException;

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbinePermission#addRole(org.apache.fulcrum.security.entity.Role)
     */
    public void addRole(Role role)
    {
        getRoles().add(role);
    }

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbinePermission#getRoles()
     */
    public RoleSet getRoles()
    {
        if (roleSet == null)
        {
            roleSet = new RoleSet();
        }
        else if(!(roleSet instanceof RoleSet))
        {
            roleSet = new RoleSet(roleSet);
        }

        return (RoleSet)roleSet;
    }

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbinePermission#getRolesAsSet()
     */
    public Set getRolesAsSet()
    {
        return roleSet;
    }

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbinePermission#removeRole(org.apache.fulcrum.security.entity.Role)
     */
    public void removeRole(Role role)
    {
        getRoles().remove(role);
    }

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbinePermission#setRoles(org.apache.fulcrum.security.util.RoleSet)
     */
    public void setRoles(RoleSet roleSet)
    {
        if (roleSet != null)
        {
            this.roleSet = roleSet;
        }
        else
        {
            this.roleSet = new RoleSet();
        }
    }

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbinePermission#setRolesAsSet(java.util.Set)
     */
    public void setRolesAsSet(Set roles)
    {
        setRoles(new RoleSet(roles));
    }

    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractSecurityEntity#getDatabaseName()
     */
    public String getDatabaseName()
    {
        return TorqueTurbinePermissionPeer.DATABASE_NAME;
    }

    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractSecurityEntity#retrieveAttachedObjects(java.sql.Connection)
     */
    public void retrieveAttachedObjects(Connection con) throws TorqueException
    {
        this.roleSet = new RoleSet();

        // the generated method that allows a Connection parameter is missing
        List rolepermissions = getTorqueTurbineRolePermissionsJoinTorqueTurbineRole(new Criteria());

        for (Iterator i = rolepermissions.iterator(); i.hasNext();)
        {
            TorqueTurbineRolePermission ttrp = (TorqueTurbineRolePermission)i.next();
            roleSet.add(ttrp.getTorqueTurbineRole());
        }
    }

    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractSecurityEntity#update(java.sql.Connection)
     */
    public void update(Connection con) throws TorqueException
    {
        if (roleSet != null)
        {
            Criteria criteria = new Criteria();

            /* remove old entries */
            criteria.add(TorqueTurbineRolePermissionPeer.PERMISSION_ID, getEntityId());
            TorqueTurbineRolePermissionPeer.doDelete(criteria, con);

            for (Iterator i = roleSet.iterator(); i.hasNext();)
            {
                TorqueTurbineRole role = (TorqueTurbineRole)i.next();

                TorqueTurbineRolePermission rp = new TorqueTurbineRolePermission();
                rp.setRoleId(role.getEntityId());
                rp.setPermissionId(getEntityId());
                rp.save(con);
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
        TorqueTurbinePermissionPeer.doDelete(SimpleKey.keyFor(getEntityId()));
    }
}
