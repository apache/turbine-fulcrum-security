package org.apache.fulcrum.security.memory.turbine;

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

import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.memory.MemoryUserManagerImpl;
import org.apache.fulcrum.security.model.turbine.TurbineUserManager;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.apache.commons.lang3.StringUtils;

/**
 * This implementation keeps all objects in memory. This is mostly meant to help
 * with testing and prototyping of ideas.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id: MemoryTurbineUserManagerImpl.java 535465 2007-05-05 06:58:06Z
 *          tv $
 */
public class MemoryTurbineUserManagerImpl extends MemoryUserManagerImpl implements TurbineUserManager {
	
	// TODO Need to load up Crypto component and actually encrypt passwords!

	/**
	 * Constructs an User object to represent an anonymous user of the application.
	 *
	 * @return An anonymous Turbine User.
	 * @throws UnknownEntityException if the implementation of User interface could
	 *                                not be determined, or does not exist.
	 */
	@Override
	public <T extends User> T getAnonymousUser() throws UnknownEntityException {
		T user;
		try {
			user = getUserInstance();
		} catch (DataBackendException dbe) {
			throw new UnknownEntityException("Could not create an anonymous user.", dbe);
		}

		user.setName("");
		return user;
	}

	/**
	 * Checks whether a passed user object matches the anonymous user pattern
	 * according to the configured user manager
	 *
	 * @param user An user object
	 * @return <code>true</code> if this is an anonymous user
	 *
	 */
	@Override
	public boolean isAnonymousUser(User user) {
		// Test if we have an anonymous user obj
		boolean isAnon = false;
		if ( user == null ) {
			isAnon = true;
		} else {
			if ( StringUtils.isEmpty( user.getName() ) )
				isAnon = true;
			else
			{
				// Turbine 4.0 and great allows for a user stored in table called "anon"
				if ( user.getName().equals("anon") )
					isAnon = true;
			}
		}
		return isAnon;
	}

}
