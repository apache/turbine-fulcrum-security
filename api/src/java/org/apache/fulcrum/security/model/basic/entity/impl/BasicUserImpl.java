package org.apache.fulcrum.security.model.basic.entity.impl;
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

import java.util.Set;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.impl.SecurityEntityImpl;
import org.apache.fulcrum.security.model.basic.entity.BasicUser;
import org.apache.fulcrum.security.util.GroupSet;

/**
 * Represents the "basic" model where users can be part of multiple groups
 * directly, with no roles or permissions.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id: BasicUser.java 437451 2006-08-27 20:20:44Z tv $
 */
public class BasicUserImpl extends SecurityEntityImpl implements BasicUser
{
    /**
     * Serial number
     */
    private static final long serialVersionUID = 7911631916604987203L;

    private String password;
    private Set groupSet = new GroupSet();

    /**
     * @return
     */
    public String getPassword()
    {
        return password;
    }
    /**
     * @param password
     */
    public void setPassword(String password)
    {
        this.password = password;
    }
    /**
    * @return
    */
    public GroupSet getGroups()
    {
        if( groupSet instanceof GroupSet )
            return (GroupSet) groupSet;
        else {
            groupSet = new GroupSet(groupSet);
            return (GroupSet)groupSet;
        }
    }
    /**
     * @param groups
     */
    public void setGroups(GroupSet groups)
    {
        if( groups != null )
            this.groupSet = groups;
        else
            this.groupSet = new GroupSet();
    }
    public void removeGroup(Group group)
    {
        getGroups().remove(group);
    }
    public void addGroup(Group group)
    {
        getGroups().add(group);
    }
    public void setGroupsAsSet(Set groups)
    {
        this.groupSet = groups;
    }
    public Set getGroupsAsSet()
    {
        return groupSet;
    }

    /**
     * Calculate a hash code for this object
     *
     * @see org.apache.fulcrum.security.entity.impl.SecurityEntityImpl#hashCode()
     */
    public int hashCode()
    {
        return new HashCodeBuilder(43, 19)
                    .append(getPassword())
                    .appendSuper(super.hashCode())
                    .toHashCode();
    }
}
