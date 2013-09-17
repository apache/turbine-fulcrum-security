package org.apache.fulcrum.security.ldap.turbine;

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

import org.apache.fulcrum.security.ldap.AbstractLDAPUserImpl;
import org.apache.fulcrum.security.model.turbine.entity.TurbineUser;
import org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRole;

/**
 * LDAPTurbineUser implements User and provides access to a user who accesses the
 * system via LDAP.
 *
 * @author <a href="mailto:cberry@gluecode.com">Craig D. Berry</a>
 * @author <a href="mailto:tadewunmi@gluecode.com">Tracy M. Adewunmi</a>
 * @author <a href="mailto:lflournoy@gluecode.com">Leonard J. Flournoy </a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @author <a href="mailto:hhernandez@itweb.com.mx">Humberto Hernandez</a>
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id: LDAPTurbineUser.java 534527 2007-05-02 16:10:59Z tv $
 */
public class LDAPTurbineUser extends AbstractLDAPUserImpl
    implements TurbineUser
{
    /** Serial Version UID */
    private static final long serialVersionUID = 3953123276619326752L;

	/**
	 * @see org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRoleEntity#getUserGroupRoleSet()
	 */
	public <T extends TurbineUserGroupRole> Set<T> getUserGroupRoleSet()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRoleEntity#setUserGroupRoleSet(java.util.Set)
	 */
	public <T extends TurbineUserGroupRole> void setUserGroupRoleSet(
			Set<T> userGroupRoleSet)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRoleEntity#addUserGroupRole(org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRole)
	 */
	public void addUserGroupRole(TurbineUserGroupRole userGroupRole)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRoleEntity#removeUserGroupRole(org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRole)
	 */
	public void removeUserGroupRole(TurbineUserGroupRole userGroupRole)
	{
		// TODO Auto-generated method stub

	}

    public byte[] getObjectdata()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void setObjectdata(byte[] objectdata)
    {
        // TODO Auto-generated method stub

    }
}
