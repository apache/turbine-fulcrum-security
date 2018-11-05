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
 * This class authenticates by returning TRUE always. Regardless of the user and
 * password data.
 * 
 * 
 * avalon.component name="noop-authenticator" avalon.service
 * type="org.apache.fulcrum.security.authenticator.Authenticator"
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 * 
 */
public class NoOpAuthenticator extends AbstractLogEnabled implements Authenticator {

	/* (non-Javadoc)
	 * @see org.apache.fulcrum.security.authenticator.Authenticator#authenticate(org.apache.fulcrum.security.entity.User, java.lang.String)
	 */
	@Override
	public boolean authenticate(User user, String password) throws DataBackendException {
		return true;
	}
}
