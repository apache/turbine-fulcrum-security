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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicUser;
import org.apache.fulcrum.security.torque.TorqueAbstractSecurityEntity;
import org.apache.fulcrum.security.torque.om.TorqueDynamicGroup;
import org.apache.fulcrum.security.torque.om.TorqueDynamicUser;
import org.apache.fulcrum.security.torque.om.TorqueDynamicUserDelegates;
import org.apache.fulcrum.security.torque.om.TorqueDynamicUserDelegatesPeer;
import org.apache.fulcrum.security.torque.om.TorqueDynamicUserGroup;
import org.apache.fulcrum.security.torque.om.TorqueDynamicUserGroupPeer;
import org.apache.fulcrum.security.torque.om.TorqueDynamicUserPeer;
import org.apache.fulcrum.security.util.GroupSet;
import org.apache.torque.TorqueException;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.util.Criteria;
/**
 * This abstract class provides the SecurityInterface to the managers.
 *
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id:$
 */
public abstract class TorqueAbstractDynamicUser extends TorqueAbstractSecurityEntity
    implements DynamicUser
{
    /** a cache of group objects */
    private Set groupSet = null;

    /** a cache of delegator (user) objects */
    private Set delegators = null;

    /** a cache of delegatee(user) objects */
    private Set delegatees = null;

    /**
     * Forward reference to generated code
     *
     * Get a list of association objects, pre-populated with their TorqueDynamicGroup
     * objects.
     *
     * @param criteria Criteria to define the selection of records
     * @throws TorqueException
     *
     * @return a list of User/Group relations
     */
    protected abstract List getTorqueDynamicUserGroupsJoinTorqueDynamicGroup(Criteria criteria)
        throws TorqueException;

    /**
     * Forward reference to generated code
     *
     * Get a list of delegator association objects, pre-populated with their
     * TorqueDynamicUserDelegates objects.
     *
     * @param criteria Criteria to define the selection of records
     * @throws TorqueException
     *
     * @return a list of User/Delegator relations
     */
    protected abstract List getTorqueDynamicUserDelegatessRelatedByDelegateeUserIdJoinTorqueDynamicUserRelatedByDelegatorUserId(Criteria criteria)
        throws TorqueException;

    /**
     * Forward reference to generated code
     *
     * Get a list of delegatee association objects, pre-populated with their
     * TorqueDynamicUserDelegates objects.
     *
     * @param criteria Criteria to define the selection of records
     * @throws TorqueException
     *
     * @return a list of User/Delegator relations
     */
    protected abstract List getTorqueDynamicUserDelegatessRelatedByDelegatorUserIdJoinTorqueDynamicUserRelatedByDelegateeUserId(Criteria criteria)
        throws TorqueException;

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
    public Set getGroupsAsSet()
    {
        return groupSet;
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
     * @see org.apache.fulcrum.security.model.basic.entity.BasicUser#setGroupsAsSet(java.util.Set)
     */
    public void setGroupsAsSet(Set groups)
    {
        setGroups(new GroupSet(groups));
    }

    /**
     * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicUser#getDelegatees()
     */
    public Set getDelegatees()
    {
        if (delegatees == null)
        {
            delegatees = new HashSet();
        }

        return delegatees;
    }

    /**
     * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicUser#getDelegators()
     */
    public Set getDelegators()
    {
        if (delegators == null)
        {
            delegators = new HashSet();
        }

        return delegators;
    }

    /**
     * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicUser#setDelegatees(java.util.Set)
     */
    public void setDelegatees(Set delegatees)
    {
        if (delegatees != null)
        {
            this.delegatees = delegatees;
        }
        else
        {
            this.delegatees = new HashSet();
        }
    }

    /**
     * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicUser#setDelegators(java.util.Set)
     */
    public void setDelegators(Set delegates)
    {
        if (delegators != null)
        {
            this.delegators = delegates;
        }
        else
        {
            this.delegators = new HashSet();
        }
    }

    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractSecurityEntity#getDatabaseName()
     */
    public String getDatabaseName()
    {
        return TorqueDynamicUserPeer.DATABASE_NAME;
    }

    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractSecurityEntity#retrieveAttachedObjects(java.sql.Connection)
     */
    public void retrieveAttachedObjects(Connection con) throws TorqueException
    {
        this.groupSet = new GroupSet();

        List usergroups = getTorqueDynamicUserGroupsJoinTorqueDynamicGroup(new Criteria());

        for (Iterator i = usergroups.iterator(); i.hasNext();)
        {
            TorqueDynamicUserGroup tdug = (TorqueDynamicUserGroup)i.next();
            groupSet.add(tdug.getTorqueDynamicGroup());
        }

        this.delegators = new HashSet();

        List delegatorlist = getTorqueDynamicUserDelegatessRelatedByDelegateeUserIdJoinTorqueDynamicUserRelatedByDelegatorUserId(new Criteria());

        for (Iterator i = delegatorlist.iterator(); i.hasNext();)
        {
            TorqueDynamicUserDelegates tdud = (TorqueDynamicUserDelegates)i.next();
            delegators.add(tdud.getTorqueDynamicUserRelatedByDelegatorUserId());
        }

        this.delegatees = new HashSet();

        List delegateelist = getTorqueDynamicUserDelegatessRelatedByDelegatorUserIdJoinTorqueDynamicUserRelatedByDelegateeUserId(new Criteria());

        for (Iterator i = delegateelist.iterator(); i.hasNext();)
        {
            TorqueDynamicUserDelegates tdud = (TorqueDynamicUserDelegates)i.next();
            delegatees.add(tdud.getTorqueDynamicUserRelatedByDelegateeUserId());
        }
    }

    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractSecurityEntity#update(java.sql.Connection)
     */
    public void update(Connection con) throws TorqueException
    {
        if (groupSet != null)
        {
            Criteria criteria = new Criteria();

            /* remove old entries */
            criteria.add(TorqueDynamicUserGroupPeer.USER_ID, getEntityId());
            TorqueDynamicUserGroupPeer.doDelete(criteria, con);

            for (Iterator i = groupSet.iterator(); i.hasNext();)
            {
                TorqueDynamicGroup group = (TorqueDynamicGroup)i.next();

                TorqueDynamicUserGroup ug = new TorqueDynamicUserGroup();
                ug.setUserId(getEntityId());
                ug.setGroupId(group.getEntityId());
                ug.save(con);
            }
        }

        if (delegators != null)
        {
            Criteria criteria = new Criteria();

            /* remove old entries */
            criteria.add(TorqueDynamicUserDelegatesPeer.DELEGATEE_USER_ID, getEntityId());
            TorqueDynamicUserDelegatesPeer.doDelete(criteria, con);

            for (Iterator i = delegators.iterator(); i.hasNext();)
            {
                TorqueDynamicUser user = (TorqueDynamicUser)i.next();

                TorqueDynamicUserDelegates ud = new TorqueDynamicUserDelegates();
                ud.setDelegateeUserId(getEntityId());
                ud.setDelegatorUserId(user.getEntityId());
                ud.save(con);
            }
        }

        if (delegatees != null)
        {
            Criteria criteria = new Criteria();

            /* remove old entries */
            criteria.add(TorqueDynamicUserDelegatesPeer.DELEGATOR_USER_ID, getEntityId());
            TorqueDynamicUserDelegatesPeer.doDelete(criteria, con);

            for (Iterator i = delegatees.iterator(); i.hasNext();)
            {
                TorqueDynamicUser user = (TorqueDynamicUser)i.next();

                TorqueDynamicUserDelegates ud = new TorqueDynamicUserDelegates();
                ud.setDelegatorUserId(getEntityId());
                ud.setDelegateeUserId(user.getEntityId());
                ud.save(con);
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
        TorqueDynamicUserPeer.doDelete(SimpleKey.keyFor(getEntityId()));
    }
}
