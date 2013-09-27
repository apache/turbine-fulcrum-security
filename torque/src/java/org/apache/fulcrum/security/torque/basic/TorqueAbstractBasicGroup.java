package org.apache.fulcrum.security.torque.basic;
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

import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.basic.entity.BasicGroup;
import org.apache.fulcrum.security.torque.TorqueAbstractSecurityEntity;
import org.apache.fulcrum.security.torque.om.TorqueBasicGroupPeer;
import org.apache.fulcrum.security.torque.om.TorqueBasicUserGroup;
import org.apache.fulcrum.security.torque.om.TorqueBasicUserGroupPeer;
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
public abstract class TorqueAbstractBasicGroup extends TorqueAbstractSecurityEntity
    implements BasicGroup
{
    /** Serial version */
	private static final long serialVersionUID = -3639383219058996135L;

	/** a cache of user objects */
    private Set<User> userSet = null;

    /**
     * Forward reference to generated code
     *
     * Get a list of association objects, pre-populated with their TorqueBasicUser
     * objects.
     *
     * @param criteria Criteria to define the selection of records
     * @param con a database connection
     * @throws TorqueException
     *
     * @return a list of User/Group relations
     */
    protected List<TorqueBasicUserGroup> getTorqueBasicUserGroupsJoinTorqueBasicUser(Criteria criteria, Connection con)
        throws TorqueException
    {
        criteria.and(TorqueBasicUserGroupPeer.GROUP_ID, getEntityId() );
        return TorqueBasicUserGroupPeer.doSelectJoinTorqueBasicUser(criteria, con);
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
            userSet = new UserSet();
        }
        else if(!(userSet instanceof UserSet))
        {
            userSet = new UserSet(userSet);
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
            this.userSet = userSet;
        }
        else
        {
            this.userSet = new UserSet();
        }
    }

    /**
     * @see org.apache.fulcrum.security.model.basic.entity.BasicGroup#setUsersAsSet(java.util.Set)
     */
    public <T extends User> void setUsersAsSet(Set<T> users)
    {
        setUsers(new UserSet(users));
    }

    /**
     * Retrieve attached objects such as users, permissions,....
     */
    public void retrieveAttachedObjects(Connection con) throws TorqueException
    {
        this.userSet = new UserSet();

        List<TorqueBasicUserGroup> usergroups =
        	getTorqueBasicUserGroupsJoinTorqueBasicUser(new Criteria(), con);

        for (TorqueBasicUserGroup tbug : usergroups)
        {
            userSet.add(tbug.getTorqueBasicUser());
        }
    }

    /**
     * Update this instance to the database with all dependent objects
     *
     * @param con A database connection
     */
    public void update(Connection con) throws TorqueException
    {
        if (userSet != null)
        {
            Criteria criteria = new Criteria();

            /* remove old entries */
            criteria.where(TorqueBasicUserGroupPeer.GROUP_ID, getEntityId());
            TorqueBasicUserGroupPeer.doDelete(criteria, con);

            for (User u : userSet)
            {
                TorqueBasicUserGroup ug = new TorqueBasicUserGroup();
                ug.setUserId((Integer)u.getId());
                ug.setGroupId(getEntityId());
                ug.save(con);
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
     * Get the name of the connection pool associated to this object
     *
     * @return the logical Torque database name
     */
    public String getDatabaseName()
    {
        return TorqueBasicGroupPeer.DATABASE_NAME;
    }

    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractSecurityEntity#delete()
     */
    public void delete() throws TorqueException
    {
        TorqueBasicGroupPeer.doDelete(SimpleKey.keyFor(getEntityId()));
    }
}
