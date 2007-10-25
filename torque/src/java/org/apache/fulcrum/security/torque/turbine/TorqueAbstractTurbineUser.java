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

import org.apache.fulcrum.security.model.turbine.entity.TurbineUser;
import org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRole;
import org.apache.fulcrum.security.torque.TorqueAbstractSecurityEntity;
import org.apache.fulcrum.security.torque.om.TorqueTurbineUserGroupRole;
import org.apache.fulcrum.security.torque.om.TorqueTurbineUserGroupRolePeer;
import org.apache.fulcrum.security.torque.om.TorqueTurbineUserPeer;
import org.apache.torque.TorqueException;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.util.Criteria;
/**
 * This abstract class provides the SecurityInterface to the managers.
 *
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id:$
 */
public abstract class TorqueAbstractTurbineUser extends TorqueAbstractSecurityEntity
    implements TurbineUser
{
    /** a cache of user_group_role objects */
    private Set userGroupRoleSet = null;

    /**
     * Forward reference to generated code
     *
     * Get a list of association objects, pre-populated with their TorqueTurbineRole
     * objects.
     *
     * @param criteria Criteria to define the selection of records
     * @throws TorqueException
     *
     * @return a list of User/Group/Role relations
     */
    protected abstract List getTorqueTurbineUserGroupRolesJoinTorqueTurbineRole(Criteria criteria)
        throws TorqueException;

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbineUser#addUserGroupRole(org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRole)
     */
    public void addUserGroupRole(TurbineUserGroupRole userGroupRole)
    {
        getUserGroupRoleSet().add(userGroupRole);
    }

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbineUser#getUserGroupRoleSet()
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
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbineUser#removeUserGroupRole(org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRole)
     */
    public void removeUserGroupRole(TurbineUserGroupRole userGroupRole)
    {
        getUserGroupRoleSet().remove(userGroupRole);
    }

    /**
     * @see org.apache.fulcrum.security.model.turbine.entity.TurbineUser#setUserGroupRoleSet(java.util.Set)
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
        return TorqueTurbineUserPeer.DATABASE_NAME;
    }

    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractSecurityEntity#retrieveAttachedObjects(java.sql.Connection)
     */
    public void retrieveAttachedObjects(Connection con) throws TorqueException
    {
        this.userGroupRoleSet = new HashSet();

        // the generated method that allows a Connection parameter is missing
        List ugrs = getTorqueTurbineUserGroupRolesJoinTorqueTurbineRole(new Criteria());

        for (Iterator i = ugrs.iterator(); i.hasNext();)
        {
            TorqueTurbineUserGroupRole ttugr = (TorqueTurbineUserGroupRole)i.next();

            TurbineUserGroupRole ugr = new TurbineUserGroupRole();
            ugr.setUser(this);
            ugr.setRole(ttugr.getTorqueTurbineRole());
            ugr.setGroup(ttugr.getTorqueTurbineGroup(con));
            userGroupRoleSet.add(ugr);
        }
    }

    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractSecurityEntity#update(java.sql.Connection)
     */
    public void update(Connection con) throws TorqueException
    {
        if (userGroupRoleSet != null)
        {
            Criteria criteria = new Criteria();

            /* remove old entries */
            criteria.add(TorqueTurbineUserGroupRolePeer.USER_ID, getEntityId());
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
        TorqueTurbineUserPeer.doDelete(SimpleKey.keyFor(getEntityId()));
    }
}
