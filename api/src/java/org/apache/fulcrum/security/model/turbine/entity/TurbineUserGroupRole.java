package org.apache.fulcrum.security.model.turbine.entity;

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

import java.io.Serializable;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.entity.User;

/**
 * Represents the "turbine" model where permissions are in a many to many
 * relationship to roles, roles are related to groups are related to users, all
 * in many to many relationships.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh </a>
 * @version $Id$
 */
public class TurbineUserGroupRole
    implements Serializable
{
    /**
     * Serial number
     */
    private static final long serialVersionUID = 265670888102016829L;

    private User user;

    private Group group;

    private Role role;

    /**
     * Get the group
     *
     * @return Returns the group.
     */
    public Group getGroup()
    {
        return group;
    }

    /**
     * Get the role
     *
     * @return Returns the role.
     */
    public Role getRole()
    {
        return role;
    }

    /**
     * Get the user
     *
     * @return Returns the user.
     */
    public User getUser()
    {
        return user;
    }

    /**
     * Set the group
     *
     * @param group
     *            The group to set.
     */
    public void setGroup(Group group)
    {
        this.group = group;
    }

    /**
     * Set the role
     *
     * @param role
     *            The role to set.
     */
    public void setRole(Role role)
    {
        this.role = role;
    }

    /**
     * Set the user
     *
     * @param user
     *            The user to set.
     */
    public void setUser(User user)
    {
        this.user = user;
    }

    public boolean equals(Object obj)
    {
        if (null == obj)
        {
            return false;
        }
        if (!(obj instanceof TurbineUserGroupRole))
        {
            return false;
        }
        else
        {
            TurbineUserGroupRole mObj = (TurbineUserGroupRole) obj;
            if (null != this.getRole() && null != mObj.getRole())
            {
                if (!this.getRole().equals(mObj.getRole()))
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
            if (null != this.getUser() && null != mObj.getUser())
            {
                if (!this.getUser().equals(mObj.getUser()))
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
            if (null != this.getGroup() && null != mObj.getGroup())
            {
                if (!this.getGroup().equals(mObj.getGroup()))
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
            return true;
        }
    }

    public int hashCode()
    {
        HashCodeBuilder hcBuilder = new HashCodeBuilder(39, 17);

        Role role = getRole();
        if (null != role)
        {
            hcBuilder.append(role);
        }

        User user = getUser();
        if (null != user)
        {
            hcBuilder.append(user);
        }

        Group group = getGroup();
        if (null != group)
        {
            hcBuilder.append(group);
        }

        return hcBuilder.toHashCode();
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();

        sb.append(null != getUser() ? getUser().toString() : "null");
        sb.append('\n');
        sb.append(null != getGroup() ? getGroup().toString() : "null");
        sb.append('\n');
        sb.append(null != getRole() ? getRole().toString() : "null");
        sb.append('\n');

        return sb.toString();
    }
}
