package org.apache.fulcrum.security.torque.dynamic;
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
import java.util.List;
import java.util.Set;

import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicRole;
import org.apache.fulcrum.security.torque.om.TorqueDynamicGroupRole;
import org.apache.fulcrum.security.torque.om.TorqueDynamicGroupRolePeer;
import org.apache.fulcrum.security.torque.om.TorqueDynamicPermissionPeer;
import org.apache.fulcrum.security.torque.om.TorqueDynamicRolePeer;
import org.apache.fulcrum.security.torque.om.TorqueDynamicRolePermission;
import org.apache.fulcrum.security.torque.om.TorqueDynamicRolePermissionPeer;
import org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity;
import org.apache.fulcrum.security.util.GroupSet;
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
public abstract class TorqueAbstractDynamicRole extends TorqueAbstractSecurityEntity
    implements DynamicRole
{
    /** Serial version */
	private static final long serialVersionUID = 2163956770966574224L;

	/** a cache of group objects */
    private Set<Group> groupSet = null;

    /** a cache of permission objects */
    private Set<Permission> permissionSet = null;

    /**
     * Forward reference to generated code
     *
     * Get a list of association objects, pre-populated with their TorqueDynamicPermission
     * objects.
     *
     * @param criteria Criteria to define the selection of records
     * @param con a database connection
     * @throws TorqueException  if any database error occurs
     *
     * @return a list of Role/Permission relations
     */
    protected List<TorqueDynamicRolePermission> getTorqueDynamicRolePermissionsJoinTorqueDynamicPermission(Criteria criteria, Connection con)
        throws TorqueException
    {
        criteria.and(TorqueDynamicRolePermissionPeer.ROLE_ID, getEntityId() );
        return TorqueDynamicRolePermissionPeer.doSelectJoinTorqueDynamicPermission(criteria, con);
    }

    /**
     * Forward reference to generated code
     *
     * Get a list of association objects, pre-populated with their TorqueDynamicGroup
     * objects.
     *
     * @param criteria Criteria to define the selection of records
     * @param con a database connection
     * @throws TorqueException  if any database error occurs
     *
     * @return a list of Group/Role relations
     */
    protected List<TorqueDynamicGroupRole> getTorqueDynamicGroupRolesJoinTorqueDynamicGroup(Criteria criteria, Connection con)
        throws TorqueException
    {
        criteria.and(TorqueDynamicGroupRolePeer.ROLE_ID, getEntityId() );
        return TorqueDynamicGroupRolePeer.doSelectJoinTorqueDynamicGroup(criteria, con);
    }

    /**
     * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicRole#addGroup(org.apache.fulcrum.security.entity.Group)
     */
    public void addGroup(Group group)
    {
        getGroups().add(group);
    }

    /**
     * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicRole#addPermission(org.apache.fulcrum.security.entity.Permission)
     */
    public void addPermission(Permission permission)
    {
        getPermissions().add(permission);
    }

    /**
     * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicRole#getGroups()
     */
    public GroupSet getGroups()
    {
        if (groupSet == null)
        {
            groupSet = new GroupSet();
        }
        else if(!(groupSet instanceof GroupSet))
        {
            groupSet = new GroupSet(groupSet);
        }

        return (GroupSet)groupSet;
    }

    /**
     * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicRole#getGroupsAsSet()
     */
    @SuppressWarnings("unchecked")
	public <T extends Group> Set<T> getGroupsAsSet()
    {
        return (Set<T>)groupSet;
    }

    /**
     * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicRole#getPermissions()
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
     * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicRole#getPermissionsAsSet()
     */
    @SuppressWarnings("unchecked")
	public <T extends Permission> Set<T> getPermissionsAsSet()
    {
        return (Set<T>)permissionSet;
    }

    /**
     * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicRole#removeGroup(org.apache.fulcrum.security.entity.Group)
     */
    public void removeGroup(Group group)
    {
        getGroups().remove(group);
    }

    /**
     * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicRole#removePermission(org.apache.fulcrum.security.entity.Permission)
     */
    public void removePermission(Permission permission)
    {
        getPermissions().remove(permission);
    }

    /**
     * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicRole#setGroups(org.apache.fulcrum.security.util.GroupSet)
     */
    public void setGroups(GroupSet groups)
    {
        if (groups != null)
        {
            this.groupSet = groups;
        }
        else
        {
            this.groupSet = new GroupSet();
        }
    }

    /**
     * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicRole#setGroupsAsSet(java.util.Set)
     */
    public <T extends Group> void setGroupsAsSet(Set<T> groups)
    {
        setGroups(new GroupSet(groups));
    }

    /**
     * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicRole#setPermissions(org.apache.fulcrum.security.util.PermissionSet)
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
     * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicRole#setPermissionsAsSet(java.util.Set)
     */
    public <T extends Permission> void setPermissionsAsSet(Set<T> permissions)
    {
        setPermissions(new PermissionSet(permissions));
    }

    /**
     * @return the database name
     */
    public String getDatabaseName()
    {
        return TorqueDynamicPermissionPeer.DATABASE_NAME;
    }
    
    @Override
    public void retrieveAttachedObjects( Connection con )
        throws TorqueException
    {
        retrieveAttachedObjects( con, false );
    }

    /**
     * @see org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity#retrieveAttachedObjects(Connection, Boolean)
     */
    @Override
    public void retrieveAttachedObjects( Connection con, Boolean lazy )
        throws TorqueException
    {
        this.permissionSet = new PermissionSet();

        List<TorqueDynamicRolePermission> rolepermissions = getTorqueDynamicRolePermissionsJoinTorqueDynamicPermission(new Criteria(), con);

        for (TorqueDynamicRolePermission tdrp : rolepermissions)
        {
            permissionSet.add(tdrp.getTorqueDynamicPermission());
        }

        this.groupSet = new GroupSet();

        List<TorqueDynamicGroupRole> grouproles = getTorqueDynamicGroupRolesJoinTorqueDynamicGroup(new Criteria(), con);

        for (TorqueDynamicGroupRole tdgr : grouproles)
        {
            groupSet.add(tdgr.getTorqueDynamicGroup());
        }
    }

    /**
     * @see org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity#update(java.sql.Connection)
     */
    public void update(Connection con) throws TorqueException
    {
        if (permissionSet != null)
        {
            Criteria criteria = new Criteria();

            /* remove old entries */
            criteria.where(TorqueDynamicRolePermissionPeer.ROLE_ID, getEntityId());
            TorqueDynamicRolePermissionPeer.doDelete(criteria, con);

            for (Permission p : permissionSet)
            {
                TorqueDynamicRolePermission rp = new TorqueDynamicRolePermission();
                rp.setPermissionId((Integer)p.getId());
                rp.setRoleId(getEntityId());
                rp.save(con);
            }
        }

        if (groupSet != null)
        {
            Criteria criteria = new Criteria();

            /* remove old entries */
            criteria.where(TorqueDynamicGroupRolePeer.ROLE_ID, getEntityId());
            TorqueDynamicGroupRolePeer.doDelete(criteria, con);

            for (Group g : groupSet)
            {
                TorqueDynamicGroupRole gr = new TorqueDynamicGroupRole();
                gr.setGroupId((Integer)g.getId());
                gr.setRoleId(getEntityId());
                gr.save(con);
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
     * @see org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity#delete()
     */
    public void delete() throws TorqueException
    {
        TorqueDynamicRolePeer.doDelete(SimpleKey.keyFor(getEntityId()));
    }
}
