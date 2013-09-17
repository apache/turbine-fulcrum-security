package org.apache.fulcrum.security.ldap.basic;

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

import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.ldap.AbstractLDAPUserImpl;
import org.apache.fulcrum.security.model.basic.entity.BasicUser;
import org.apache.fulcrum.security.util.GroupSet;

/**
 * LDAPBasicUser implements User and provides access to a user who accesses the
 * system via LDAP.
 *
 * @author <a href="mailto:cberry@gluecode.com">Craig D. Berry</a>
 * @author <a href="mailto:tadewunmi@gluecode.com">Tracy M. Adewunmi</a>
 * @author <a href="mailto:lflournoy@gluecode.com">Leonard J. Flournoy </a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @author <a href="mailto:hhernandez@itweb.com.mx">Humberto Hernandez</a>
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id: LDAPBasicUser.java 534527 2007-05-02 16:10:59Z tv $
 */
public class LDAPBasicUser extends AbstractLDAPUserImpl
    implements BasicUser
{
    /** Serial Version UID */
    private static final long serialVersionUID = 3953123276619326752L;

	/**
	 * @see org.apache.fulcrum.security.model.basic.entity.BasicUser#getGroups()
	 */
	public GroupSet getGroups()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.apache.fulcrum.security.model.basic.entity.BasicUser#setGroups(org.apache.fulcrum.security.util.GroupSet)
	 */
	public void setGroups(GroupSet groups)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.apache.fulcrum.security.model.basic.entity.BasicUser#removeGroup(org.apache.fulcrum.security.entity.Group)
	 */
	public void removeGroup(Group group)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.apache.fulcrum.security.model.basic.entity.BasicUser#addGroup(org.apache.fulcrum.security.entity.Group)
	 */
	public void addGroup(Group group)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.apache.fulcrum.security.model.basic.entity.BasicUser#setGroupsAsSet(java.util.Set)
	 */
	public <T extends Group> void setGroupsAsSet(Set<T> groups)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.apache.fulcrum.security.model.basic.entity.BasicUser#getGroupsAsSet()
	 */
	public <T extends Group> Set<T> getGroupsAsSet()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
