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

import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.model.basic.entity.BasicUser;
import org.apache.fulcrum.security.torque.TorqueAbstractSecurityEntity;
import org.apache.fulcrum.security.torque.om.TorqueBasicUserGroup;
import org.apache.fulcrum.security.torque.om.TorqueBasicUserGroupPeer;
import org.apache.fulcrum.security.torque.om.TorqueBasicUserPeer;
import org.apache.fulcrum.security.util.GroupSet;
import org.apache.torque.TorqueException;
import org.apache.torque.criteria.Criteria;
import org.apache.torque.om.SimpleKey;

/**
 * This abstract class provides the SecurityInterface to the managers.
 *
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id:$
 */
public abstract class TorqueAbstractBasicUser extends TorqueAbstractSecurityEntity
    implements BasicUser
{
    /** Serial version */
	private static final long serialVersionUID = 7669398253522416329L;
	/** a cache of group objects */
    private Set<Group> groupSet = null;

    /**
     * Forward reference to generated code
     *
     * Get a list of association objects, pre-populated with their TorqueBasicGroup
     * objects.
     *
     * @param criteria Criteria to define the selection of records
     * @param con a database connection
     * @throws TorqueException
     *
     * @return a list of User/Group relations
     */
    protected List<TorqueBasicUserGroup> getTorqueBasicUserGroupsJoinTorqueBasicGroup(Criteria criteria, Connection con)
        throws TorqueException
    {
        criteria.and(TorqueBasicUserGroupPeer.USER_ID, getEntityId() );
        return TorqueBasicUserGroupPeer.doSelectJoinTorqueBasicGroup(criteria, con);
    }

    /**
     * @see org.apache.fulcrum.security.model.basic.entity.BasicUser#addGroup(org.apache.fulcrum.security.entity.Group)
     */
    public void addGroup(Group group)
    {
        getGroups().add(group);
    }

    /**
     * @see org.apache.fulcrum.security.model.basic.entity.BasicUser#getGroups()
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
     * @see org.apache.fulcrum.security.model.basic.entity.BasicUser#getGroupsAsSet()
     */
    @SuppressWarnings("unchecked")
	public <T extends Group> Set<T> getGroupsAsSet()
    {
        return (Set<T>)groupSet;
    }

    /**
     * @see org.apache.fulcrum.security.model.basic.entity.BasicUser#removeGroup(org.apache.fulcrum.security.entity.Group)
     */
    public void removeGroup(Group group)
    {
        getGroups().remove(group);
    }

    /**
     * @see org.apache.fulcrum.security.model.basic.entity.BasicUser#setGroups(org.apache.fulcrum.security.util.GroupSet)
     */
    public void setGroups(GroupSet groups)
    {
        if(groups != null)
        {
            this.groupSet = groups;
        }
        else
        {
            this.groupSet = new GroupSet();
        }
    }

    /**
     * @see org.apache.fulcrum.security.model.basic.entity.BasicUser#setGroupsAsSet(java.util.Set)
     */
    public <T extends Group> void setGroupsAsSet(Set<T> groups)
    {
        setGroups(new GroupSet(groups));
    }

    /**
     * Retrieve attached objects such as users, permissions,....
     */
    public void retrieveAttachedObjects(Connection con) throws TorqueException
    {
        this.groupSet = new GroupSet();

        List<TorqueBasicUserGroup> usergroups = getTorqueBasicUserGroupsJoinTorqueBasicGroup(new Criteria(), con);

        for (TorqueBasicUserGroup tbug : usergroups)
        {
            groupSet.add(tbug.getTorqueBasicGroup());
        }
    }

    /**
     * Update this instance to the database with all dependent objects
     *
     * @param con A database connection
     */
    public void update(Connection con) throws TorqueException
    {
        if (groupSet != null)
        {
            Criteria criteria = new Criteria();

            /* remove old entries */
            criteria.where(TorqueBasicUserGroupPeer.USER_ID, getEntityId());
            TorqueBasicUserGroupPeer.doDelete(criteria, con);

            for (Group g : groupSet)
            {
                TorqueBasicUserGroup ug = new TorqueBasicUserGroup();
                ug.setUserId(getEntityId());
                ug.setGroupId((Integer)g.getId());
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
     * Get the name of the connnection pool associated to this object
     *
     * @return the logical Torque database name
     */
    public String getDatabaseName()
    {
        return TorqueBasicUserPeer.DATABASE_NAME;
    }

    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractSecurityEntity#delete()
     */
    public void delete() throws TorqueException
    {
        TorqueBasicUserPeer.doDelete(SimpleKey.keyFor(getEntityId()));
    }
}
