package org.apache.fulcrum.security.model.basic;

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

import org.apache.fulcrum.security.acl.AccessControlList;
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.util.GroupSet;

/**
 * This interface describes a control class that makes it easy to find out if a
 * particular User has a given Permission. It also determines if a User has a a
 * particular Role.
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public interface BasicAccessControlList extends Serializable, AccessControlList
{
    /**
     * Retrieves all groups for a user
     * 
     * @return the set of Groups this user has
     */
    GroupSet getGroups();

    /**
     * Checks if the user is assigned a specific Group
     * 
     * @param role
     *            the Group
     * @return <code>true</code> if the user is assigned to the Group
     */
    boolean hasGroup(Group group);

    /**
     * Checks if the user is assigned a specific Group
     * 
     * @param role
     *            the group name
     * @return <code>true</code>if the user is assigned the Group.
     */
    boolean hasGroup(String group);

}
