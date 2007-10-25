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

import org.apache.fulcrum.security.entity.SecurityEntity;
import org.apache.turbine.om.security.Permission;
import org.apache.turbine.om.security.Role;
import org.apache.turbine.util.security.PermissionSet;
import org.apache.turbine.util.security.TurbineSecurityException;
/**
 * Adapter around Fulcrum Role.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class RoleAdapter extends BaseAdapter implements Role
{

   public RoleAdapter(org.apache.fulcrum.security.entity.Role role){
   	super((SecurityEntity)role);
   }

    public void setPermissions(PermissionSet arg0)
    {
		throw new RuntimeException("Unsupported operation");
    }
    public PermissionSet getPermissions()
    {
		throw new RuntimeException("Unsupported operation");
    }
    /* Not implemented.
     * @see org.apache.turbine.om.security.Role#create(java.lang.String)
     */
    public Role create(String arg0) throws TurbineSecurityException
    {
		throw new RuntimeException("Unsupported operation");
    }
    /* Not implemented.
     * @see org.apache.turbine.om.security.Role#grant(org.apache.turbine.om.security.Permission)
     */
    public void grant(Permission arg0) throws TurbineSecurityException
    {
		throw new RuntimeException("Unsupported operation");
    }
    /* Not implemented.
     * @see org.apache.turbine.om.security.Role#grant(org.apache.turbine.util.security.PermissionSet)
     */
    public void grant(PermissionSet arg0) throws TurbineSecurityException
    {
		throw new RuntimeException("Unsupported operation");
    }
    /* Not implemented.
     * @see org.apache.turbine.om.security.Role#revoke(org.apache.turbine.om.security.Permission)
     */
    public void revoke(Permission arg0) throws TurbineSecurityException
    {
		throw new RuntimeException("Unsupported operation");
    }
    /* Not implemented.
     * @see org.apache.turbine.om.security.Role#revoke(org.apache.turbine.util.security.PermissionSet)
     */
    public void revoke(PermissionSet arg0) throws TurbineSecurityException
    {
		throw new RuntimeException("Unsupported operation");
    }
}
