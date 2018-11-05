package org.apache.fulcrum.security.util;

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

import java.util.Collection;

import org.apache.fulcrum.security.entity.Role;

/**
 * This class represents a set of Roles. It makes it easy to build a UI that
 * would allow someone to add a group of Roles to a User. It enforces that only
 * Role objects are allowed in the set and only relevant methods are available.
 * 
 * @author <a href="mailto:john.mcnally@clearink.com">John D. McNally</a>
 * @author <a href="mailto:bmclaugh@algx.net">Brett McLaughlin</a>
 * @author <a href="mailto:marco@intermeta.de">Marco Kn&uuml;ttel</a>
 * @author <a href="mailto:hps@intermeta.de">Henning P. Schmiedehausen</a>
 * @version $Id$
 */
public class RoleSet extends SecuritySet<Role>
{
    /**
     * Serial number
     */
    private static final long serialVersionUID = -7878336668034575930L;

    /**
     * Constructs an empty RoleSet
     */
    public RoleSet()
    {
        super();
    }

    /**
     * Constructs a new RoleSet with specified contents.
     * 
     * If the given collection contains multiple objects that are identical WRT
     * equals() method, some objects will be overwritten.
     * 
     * @param roles
     *            A collection of roles to be contained in the set.
     */
    public RoleSet(Collection<? extends Role> roles)
    {
        this();
        addAll(roles);
    }

    /**
     * Returns a Role with the given name, if it is contained in this RoleSet.
     * 
     * @param roleName
     *            Name of Role.
     * @return Role if argument matched a Role in this RoleSet; null if no
     *         match.
     * @deprecated use getByName()
     */
    @Deprecated
    public Role getRoleByName(String roleName)
    {
        return getByName(roleName);
    }

    /**
     * Returns a Role with the given id, if it is contained in this RoleSet.
     * 
     * @param roleId
     *            id of the Role.
     * @return Role if argument matched a Role in this RoleSet; null if no
     *         match.
     * @deprecated Use getById()
     */
    @Deprecated
    public Role getRoleById(Object roleId)
    {
        return getById(roleId);
    }

    /**
     * Print out a RoleSet as a String
     * 
     * @return The Role Set as String
     * 
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("RoleSet: ");
        sb.append(super.toString());

        return sb.toString();
    }
}
