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

import javax.security.auth.login.LoginException;

import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.nt.ParseUtils;
import org.apache.fulcrum.security.util.DataBackendException;

import com.tagish.auth.win32.NTSystem;
/**
 * This class authenticates a user against NT.  Requires some
 * extra libraries.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class NTAuthenticator extends AbstractLogEnabled implements Authenticator
{
    /**
     * Authenticate an username with the specified password. If authentication
     * is successful the method returns true. If it fails, it returns false
     * If there are any problems, an exception is thrown.
     *
     *
     * @param usernameAndDomain an string in the format [domain]/[username].
     * @param password the user supplied password.
     * @exception UnknownEntityException if the user's account does not
     *            exist in the database.
     * @exception DataBackendException if there is a problem accessing the
     *            storage.
     */
    public boolean authenticate(User user, String password)
        throws  DataBackendException
    {
        // check NT...
        boolean authenticated = false;
        NTSystem ntSystem = new NTSystem();
        char passwordArray[] = password.toCharArray();
        try
        {
            String username = ParseUtils.parseForUsername(user.getName());
            String domain = ParseUtils.parseForDomain(user.getName());
            ntSystem.logon(username, passwordArray, domain);
            if (ntSystem.getName().equalsIgnoreCase(username))
            {
                authenticated = true;
            }
            ntSystem.logoff();
        }
        catch (LoginException le)
        {
            ntSystem.logoff();
            throw new DataBackendException(le.getMessage(), le);
        }
        return authenticated;
    }

}
