package org.apache.fulcrum.security.authenticator;

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
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.util.DataBackendException;

/**
 * This class authenticates by doing a plain text match of the user's passwords.
 * Very insecure!
 * 
 * avalon.component name="textmatch-authenticator" avalon.service
 * type="org.apache.fulcrum.security.authenticator.Authenticator"
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 * 
 */
public class TextMatchAuthenticator extends AbstractLogEnabled implements Authenticator {
	/**
	 * Authenticate an username with the specified password. Returns true if the
	 * user password plain text matches the passed in password.
	 *
	 * @param user     object
	 * @param password the user supplied password.
	 * @exception DataBackendException if there is a problem accessing the storage.
	 */
	@Override
	public boolean authenticate(User user, String password) throws DataBackendException {
		if (user == null) {
			return false;
		}

		String referenced = user.getPassword() == null ? "" : user.getPassword().trim();
		String tested = password == null ? "" : password.trim();
		return referenced.equals(tested);
	}
}
