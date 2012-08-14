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

import org.apache.fulcrum.security.entity.Permission;

/**
 * This class represents a set of Permissions.  It makes it easy to
 * build a UI that would allow someone to add a group of Permissions
 * to a Role.  It enforces that only
 * Permission objects are allowed in the set and only relevant methods
 * are available.
 *
 * @author <a href="mailto:john.mcnally@clearink.com">John D. McNally</a>
 * @author <a href="mailto:bmclaugh@algx.net">Brett McLaughlin</a>
 * @author <a href="mailto:marco@intermeta.de">Marco Kn&uuml;ttel</a>
 * @author <a href="mailto:hps@intermeta.de">Henning P. Schmiedehausen</a>
 * @version $Id$
 */
public class PermissionSet
    extends SecuritySet<Permission>
{
    /**
     * Serial number
     */
    private static final long serialVersionUID = 4532868341893924965L;

    /**
     * Constructs an empty PermissionSet
     */
    public PermissionSet()
    {
        super();
    }

    /**
     * Constructs a new PermissionSet with specified contents.
     *
     * If the given collection contains multiple objects that are
     * identical WRT equals() method, some objects will be overwritten.
     *
     * @param permissions A collection of permissions to be contained in the set.
     */
    public PermissionSet(Collection<? extends Permission> permissions)
    {
        this();
        addAll(permissions);
    }

    /**
     * Returns a Permission with the given name, if it is contained in
     * this PermissionSet.
     *
     * @param permissionName Name of Permission.
     * @return Permission if argument matched a Permission in this
     * PermissionSet; null if no match.
     * @deprecated use getByName()
     */
    public Permission getPermissionByName(String permissionName)
    {
		return getByName(permissionName);
    }

    /**
     * Returns a Permission with the given id, if it is contained in
     * this PermissionSet.
     *
     * @param permissionId Id of the Permission.
     * @return Permission if argument matched a Permission in this
     * PermissionSet; null if no match.
     * @deprecated Use getById()
     */
    public Permission getPermissionById(Object permissionId)
    {
    	return getById(permissionId);
    }

    /**
     * Print out a PermissionSet as a String
     *
     * @returns The Permission Set as String
     *
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("PermissionSet: ");
        sb.append(super.toString());

        return sb.toString();
    }
}
