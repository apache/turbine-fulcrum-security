package org.apache.fulcrum.security.ldap.dynamic;

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

import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.ldap.basic.LDAPBasicUser;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicUser;

/**
 * LDAPDynamicUser implements User and provides access to a user who accesses the
 * system via LDAP.
 *
 * @author <a href="mailto:cberry@gluecode.com">Craig D. Berry</a>
 * @author <a href="mailto:tadewunmi@gluecode.com">Tracy M. Adewunmi</a>
 * @author <a href="mailto:lflournoy@gluecode.com">Leonard J. Flournoy </a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @author <a href="mailto:hhernandez@itweb.com.mx">Humberto Hernandez</a>
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id: LDAPDynamicUser.java 534527 2007-05-02 16:10:59Z tv $
 */
public class LDAPDynamicUser extends LDAPBasicUser
    implements DynamicUser
{
    /** Serial Version UID */
    private static final long serialVersionUID = 3953123276619326752L;

	/**
	 * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicUser#getDelegatees()
	 */
	public <T extends User> Set<T> getDelegatees()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicUser#setDelegatees(java.util.Set)
	 */
	public <T extends User> void setDelegatees(Set<T> delegatees)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicUser#getDelegators()
	 */
	public <T extends User> Set<T> getDelegators()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.apache.fulcrum.security.model.dynamic.entity.DynamicUser#setDelegators(java.util.Set)
	 */
	public <T extends User> void setDelegators(Set<T> delegators)
	{
		// TODO Auto-generated method stub

	}
}
