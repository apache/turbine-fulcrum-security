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

import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicGroup;
import org.apache.fulcrum.security.torque.om.TorqueDynamicGroupPeer;
import org.apache.fulcrum.security.torque.om.TorqueDynamicGroupRole;
import org.apache.fulcrum.security.torque.om.TorqueDynamicGroupRolePeer;
import org.apache.fulcrum.security.torque.om.TorqueDynamicUserGroup;
import org.apache.fulcrum.security.torque.om.TorqueDynamicUserGroupPeer;
import org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity;
import org.apache.fulcrum.security.util.RoleSet;
import org.apache.fulcrum.security.util.UserSet;
import org.apache.torque.TorqueException;
import org.apache.torque.criteria.Criteria;
import org.apache.torque.om.SimpleKey;
/**
 * This abstract class provides the SecurityInterface to the managers.
 *
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id:$
 */
public abstract class TorqueAbstractDynamicGroup extends TorqueAbstractSecurityEntity
    implements DynamicGroup
{
    /** Serial version */
	private static final long serialVersionUID = -122088742532595477L;

	/** a cache of user objects */
    private Set<User> userSet = null;

    /** a cache of role objects */
    private Set<Role> roleSet = null;

    /**
     * Forward reference to generated code
     *
     * Get a list of association objects, pre-populated with their TorqueDynamicUser
     * objects.
     *
     * @param criteria Criteria to define the selection of records
     * @param con a database connection
     * @throws TorqueException
     *
     * @return a list of User/Group relations
     */
    protected List<TorqueDynamicUserGroup> getTorqueDynamicUserGroupsJoinTorqueDynamicUser(Criteria criteria, Connection con)
        throws TorqueException
    {
        criteria.and(TorqueDynamicUserGroupPeer.GROUP_ID, getEntityId() );
        return TorqueDynamicUserGroupPeer.doSelectJoinTorqueDynamicUser(criteria, con);
    }

    /**
     * Forward reference to generated code
     *
     * Get a list of association objects, pre-populated with their TorqueDynamicRole
     * objects.
     *
     * @param criteria Criteria to define the selection of records
     * @param con a database connection
     * @throws TorqueException
     *
     * @return a list of Role/Group relations
     */
    protected List<TorqueDynamicGroupRole> getTorqueDynamicGroupRolesJoinTorqueDynamicRole(Criteria criteria, Connection con)
        throws TorqueException
    {
        criteria.and(TorqueDynamicGroupRolePeer.GROUP_ID, getEntityId() );
        return TorqueDynamicGroupRolePeer.doSelectJoinTorqueDynamicRole(criteria, con);
    }

    /**
     * @see org.apache.fulcrum.security.model.basic.entity.BasicGroup#addUser(org.apache.fulcrum.security.entity.User)
     */
    public void addUser(User user)
    {
        getUsers().add(user);
    }

    /**
     * @see org.apache.fulcrum.security.model.basic.entity.BasicGroup#getUsers()
     */
    public UserSet getUsers()
    {
        if (userSet == null)
        {
            userSet = new UserSet<User>();
        }
        else if(!(userSet instanceof UserSet))
        {
            userSet = new UserSet<User>(userSet);
        }

        return (UserSet)userSet;
    }

    /**
     * @see org.apache.fulcrum.security.model.basic.entity.BasicGroup#getUsersAsSet()
     */
    @SuppressWarnings("unchecked")
	public <T extends User> Set<T> getUsersAsSet()
    {
        return (Set<T>)userSet;
    }

    /**
     * @see org.apache.fulcrum.security.model.basic.entity.BasicGroup#removeUser(org.apache.fulcrum.security.entity.User)
     */
    public void removeUser(User user)
    {
        getUsers().remove(user);
    }

    /**
     * @see org.apache.fulcrum.security.model.basic.entity.BasicGroup#setUsers(org.apache.fulcrum.security.util.UserSet)
     */
    public void setUsers(UserSet userSet)
    {
        if(userSet != null)
        {
            this.userSet = (UserSet<User>) userSet;
        }
        else
        {
            this.userSet = new UserSet<User>();
        }
    }

    /**
     * @see org.apache.fulcrum.security.model.basic.entity.BasicGroup#setUsersAsSet(java.util.Set)
     */
    public <T extends User> void setUsersAsSet(Set<T> users)
    {
        setUsers(new UserSet<User>(users));
    }

    /**
     * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicGroup#addRole(org.apache.fulcrum.security.entity.Role)
     */
    public void addRole(Role role)
    {
        getRoles().add(role);
    }

    /**
     * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicGroup#getRoles()
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
     * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicGroup#getRolesAsSet()
     */
    @SuppressWarnings("unchecked")
	public <T extends Role> Set<T> getRolesAsSet()
    {
        return (Set<T>)roleSet;
    }

    /**
     * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicGroup#removeRole(org.apache.fulcrum.security.entity.Role)
     */
    public void removeRole(Role role)
    {
        getRoles().remove(role);
    }

    /**
     * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicGroup#setRoles(org.apache.fulcrum.security.util.RoleSet)
     */
    public void setRoles(RoleSet roleSet)
    {
        if(roleSet != null)
        {
            this.roleSet = roleSet;
        }
        else
        {
            this.roleSet = new RoleSet();
        }
    }

    /**
     * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicGroup#setRolesAsSet(java.util.Set)
     */
    public <T extends Role> void setRolesAsSet(Set<T> roles)
    {
        setRoles(new RoleSet(roles));
    }

    /**
     * @see org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity#getDatabaseName()
     */
    public String getDatabaseName()
    {
        return TorqueDynamicGroupPeer.DATABASE_NAME;
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
        this.userSet = new UserSet<User>();

        List<TorqueDynamicUserGroup> usergroups = getTorqueDynamicUserGroupsJoinTorqueDynamicUser(new Criteria(), con);

        for (TorqueDynamicUserGroup tdug : usergroups)
        {
            userSet.add(tdug.getTorqueDynamicUser());
        }

        this.roleSet = new RoleSet();

        List<TorqueDynamicGroupRole> grouproles = getTorqueDynamicGroupRolesJoinTorqueDynamicRole(new Criteria(), con);

        for (TorqueDynamicGroupRole tdgr : grouproles)
        {
            roleSet.add(tdgr.getTorqueDynamicRole());
        }
    }

    /**
     * @see org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity#update(java.sql.Connection)
     */
    public void update(Connection con) throws TorqueException
    {
        if (userSet != null)
        {
            Criteria criteria = new Criteria();

            /* remove old entries */
            criteria.where(TorqueDynamicUserGroupPeer.GROUP_ID, getEntityId());
            TorqueDynamicUserGroupPeer.doDelete(criteria, con);

            for (User u : userSet)
            {
                TorqueDynamicUserGroup ug = new TorqueDynamicUserGroup();
                ug.setUserId((Integer)u.getId());
                ug.setGroupId(getEntityId());
                ug.save(con);
            }
        }

        if (roleSet != null)
        {
            Criteria criteria = new Criteria();

            /* remove old entries */
            criteria.where(TorqueDynamicGroupRolePeer.GROUP_ID, getEntityId());
            TorqueDynamicGroupRolePeer.doDelete(criteria, con);

            for (Role r : roleSet)
            {
                TorqueDynamicGroupRole gr = new TorqueDynamicGroupRole();
                gr.setRoleId((Integer)r.getId());
                gr.setGroupId(getEntityId());
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
        TorqueDynamicGroupPeer.doDelete(SimpleKey.keyFor(getEntityId()));
    }
}
