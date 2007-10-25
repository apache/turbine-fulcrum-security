package org.apache.fulcrum.security.adapter.turbine;
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

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fulcrum.security.model.basic.BasicAccessControlList;
import org.apache.fulcrum.security.model.dynamic.DynamicAccessControlList;
import org.apache.turbine.om.security.Group;
import org.apache.turbine.om.security.Permission;
import org.apache.turbine.om.security.Role;
import org.apache.turbine.util.security.AccessControlList;
import org.apache.turbine.util.security.GroupSet;
import org.apache.turbine.util.security.PermissionSet;
import org.apache.turbine.util.security.RoleSet;
/**
 * This class adaptes the Turbine AccessControlList to the Fulcrum
 * Security service AccessControlList.  All calls back and forth are
 * proxied through this class.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class AccessControlListAdapter implements AccessControlList
{
    private static Log log = LogFactory.getLog(AccessControlListAdapter.class);
    private org.apache.fulcrum.security.acl.AccessControlList acl;
    /**
     *
     */
    public AccessControlListAdapter()
    {
        super();
    }
    public AccessControlListAdapter(org.apache.fulcrum.security.acl.AccessControlList acl)
    {
        super();
        this.acl = acl;
    }
    public AccessControlListAdapter(Map rolesMap, Map permissionsMap)
    {
        log.info("AccessControlListAdapter is eating call to constructor(Map,Map).");
    }
    /* (non-Javadoc)
     * @see org.apache.turbine.util.security.AccessControlList#getRoles(org.apache.turbine.om.security.Group)
     */
    public RoleSet getRoles(Group arg0)
    {
		throw new RuntimeException("Unsupported operation");
    }
    /* (non-Javadoc)
     * @see org.apache.turbine.util.security.AccessControlList#getRoles()
     */
    public RoleSet getRoles()
    {
		throw new RuntimeException("Unsupported operation");
    }
    /* (non-Javadoc)
     * @see org.apache.turbine.util.security.AccessControlList#getPermissions(org.apache.turbine.om.security.Group)
     */
    public PermissionSet getPermissions(Group arg0)
    {
		throw new RuntimeException("Unsupported operation");
    }
    /* (non-Javadoc)
     * @see org.apache.turbine.util.security.AccessControlList#getPermissions()
     */
    public PermissionSet getPermissions()
    {
        if (!(acl instanceof DynamicAccessControlList)){
			throw new RuntimeException("ACL doesn't support this opperation");
        }
        PermissionSet turbinePS = new PermissionSet();
        org.apache.fulcrum.security.util.PermissionSet fulcrumPS = ((DynamicAccessControlList)acl).getPermissions();
        for (Iterator i = fulcrumPS.iterator(); i.hasNext();)
        {
            org.apache.fulcrum.security.entity.Permission fulcrumPermission =
                (org.apache.fulcrum.security.entity.Permission) i.next();
            Permission turbinePermission = new PermissionAdapter(fulcrumPermission);
            turbinePS.add(turbinePermission);
        }
        return turbinePS;
    }
    /* (non-Javadoc)
     * @see org.apache.turbine.util.security.AccessControlList#hasRole(org.apache.turbine.om.security.Role, org.apache.turbine.om.security.Group)
     */
    public boolean hasRole(Role arg0, Group arg1)
    {
		throw new RuntimeException("Unsupported operation");
    }
    /* (non-Javadoc)
     * @see org.apache.turbine.util.security.AccessControlList#hasRole(org.apache.turbine.om.security.Role, org.apache.turbine.util.security.GroupSet)
     */
    public boolean hasRole(Role arg0, GroupSet arg1)
    {
		throw new RuntimeException("Unsupported operation");
    }
    /* (non-Javadoc)
     * @see org.apache.turbine.util.security.AccessControlList#hasRole(java.lang.String, java.lang.String)
     */
    public boolean hasRole(String arg0, String arg1)
    {
		throw new RuntimeException("Unsupported operation");
    }
    /* (non-Javadoc)
     * @see org.apache.turbine.util.security.AccessControlList#hasRole(java.lang.String, org.apache.turbine.util.security.GroupSet)
     */
    public boolean hasRole(String arg0, GroupSet arg1)
    {
		throw new RuntimeException("Unsupported operation");
    }
    /* (non-Javadoc)
     * @see org.apache.turbine.util.security.AccessControlList#hasRole(org.apache.turbine.om.security.Role)
     */
    public boolean hasRole(Role arg0)
    {
		throw new RuntimeException("Unsupported operation");
    }
    /* For a DynamicACL, checks the role.  But, for a BasicACL, it maps
     * roles onto BasicGroup's.
     * @see org.apache.turbine.util.security.AccessControlList#hasRole(java.lang.String)
     */
    public boolean hasRole(String roleName)
    {
		if (acl instanceof DynamicAccessControlList){
			return ((DynamicAccessControlList)acl).hasRole(roleName);
		}
		else if (acl instanceof BasicAccessControlList){
		    return ((BasicAccessControlList)acl).hasGroup(roleName);
		}
		throw new RuntimeException("Unsupported operation");
    }
    /* (non-Javadoc)
     * @see org.apache.turbine.util.security.AccessControlList#hasPermission(org.apache.turbine.om.security.Permission, org.apache.turbine.om.security.Group)
     */
    public boolean hasPermission(Permission arg0, Group arg1)
    {
		throw new RuntimeException("Unsupported operation");
    }
    /* (non-Javadoc)
     * @see org.apache.turbine.util.security.AccessControlList#hasPermission(org.apache.turbine.om.security.Permission, org.apache.turbine.util.security.GroupSet)
     */
    public boolean hasPermission(Permission arg0, GroupSet arg1)
    {
		throw new RuntimeException("Unsupported operation");
    }
    /* (non-Javadoc)
     * @see org.apache.turbine.util.security.AccessControlList#hasPermission(java.lang.String, java.lang.String)
     */
    public boolean hasPermission(String arg0, String arg1)
    {
		throw new RuntimeException("Unsupported operation");
    }
    /* (non-Javadoc)
     * @see org.apache.turbine.util.security.AccessControlList#hasPermission(java.lang.String, org.apache.turbine.om.security.Group)
     */
    public boolean hasPermission(String arg0, Group arg1)
    {
		throw new RuntimeException("Unsupported operation");
    }
    /* (non-Javadoc)
     * @see org.apache.turbine.util.security.AccessControlList#hasPermission(java.lang.String, org.apache.turbine.util.security.GroupSet)
     */
    public boolean hasPermission(String arg0, GroupSet arg1)
    {
		throw new RuntimeException("Unsupported operation");
    }
    /* (non-Javadoc)
     * @see org.apache.turbine.util.security.AccessControlList#hasPermission(org.apache.turbine.om.security.Permission)
     */
    public boolean hasPermission(Permission arg0)
    {
		throw new RuntimeException("Unsupported operation");
    }
    /* (non-Javadoc)
     * @see org.apache.turbine.util.security.AccessControlList#hasPermission(java.lang.String)
     */
    public boolean hasPermission(String arg0)
    {
		throw new RuntimeException("Unsupported operation");
    }
    /* (non-Javadoc)
     * @see org.apache.turbine.util.security.AccessControlList#getAllGroups()
     */
    public Group[] getAllGroups()
    {
		throw new RuntimeException("Unsupported operation");
    }
}
