package org.apache.fulcrum.security.model.dynamic.entity.impl;

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

import java.util.HashSet;
import java.util.Set;

import org.apache.fulcrum.security.model.basic.entity.impl.BasicUserImpl;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicUser;

/**
 * Represents the "simple" model where permissions are related to roles, roles
 * are related to groups and groups are related to users, all in many to many
 * relationships.
 *
 * Users have a set of delegates and delegatee's. If user A has B in their
 * delegates - B assumes A's groups,roles and permissions If user C has D in
 * their delegatees - C assumes D's groups,roles and permissions
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id: DynamicUser.java 437451 2006-08-27 20:20:44Z tv $
 */
public class DynamicUserImpl extends BasicUserImpl implements DynamicUser
{
    /**
     * Serial number
     */
    private static final long serialVersionUID = 2841311062371647853L;

    private Set delegators = new HashSet();

    private Set delegatees = new HashSet();

    /**
     * @return Returns the delegatees.
     */
    public Set getDelegatees()
    {
        return delegatees;
    }

    /**
     * @param delegatees
     *            The delegatees to set.
     */
    public void setDelegatees(Set delegatees)
    {
        this.delegatees = delegatees;
    }

    /**
     * @return Returns the delegators.
     */
    public Set getDelegators()
    {
        return delegators;
    }

    /**
     * @param delegates
     *            The delegators to set.
     */
    public void setDelegators(Set delegates)
    {
        this.delegators = delegates;
    }
}
