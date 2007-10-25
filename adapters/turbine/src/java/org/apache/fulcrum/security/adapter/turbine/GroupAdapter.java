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
import org.apache.turbine.om.security.Group;
import org.apache.turbine.om.security.Role;
import org.apache.turbine.om.security.User;
import org.apache.turbine.util.security.RoleSet;
import org.apache.turbine.util.security.TurbineSecurityException;
/**
 * Adapter around Fulcrum Groups.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class GroupAdapter extends BaseAdapter implements Group
{

	public GroupAdapter(org.apache.fulcrum.security.entity.Group group){
		super((SecurityEntity)group);
	   }
    /* Not implemented.
     * @see org.apache.turbine.om.security.Group#grant(org.apache.turbine.om.security.User, org.apache.turbine.om.security.Role)
     */
    public void grant(User arg0, Role arg1) throws TurbineSecurityException
    {
        throw new RuntimeException("Unsupported operation");
    }
    /* Not implemented.
     * @see org.apache.turbine.om.security.Group#grant(org.apache.turbine.om.security.User, org.apache.turbine.util.security.RoleSet)
     */
    public void grant(User arg0, RoleSet arg1) throws TurbineSecurityException
    {
        throw new RuntimeException("Unsupported operation");
    }
    /* Not implemented.
     * @see org.apache.turbine.om.security.Group#revoke(org.apache.turbine.om.security.User, org.apache.turbine.om.security.Role)
     */
    public void revoke(User arg0, Role arg1) throws TurbineSecurityException
    {
        throw new RuntimeException("Unsupported operation");
    }
    /* Not implemented.
     * @see org.apache.turbine.om.security.Group#revoke(org.apache.turbine.om.security.User, org.apache.turbine.util.security.RoleSet)
     */
    public void revoke(User arg0, RoleSet arg1) throws TurbineSecurityException
    {
        throw new RuntimeException("Unsupported operation");
    }
}
