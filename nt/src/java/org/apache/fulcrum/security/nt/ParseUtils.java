package org.apache.fulcrum.security.nt;
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

/**
 * Utility class for parsing username and domain out of a single string.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class ParseUtils
{
    public static String parseForUsername(String usernameAndDomain) throws LoginException
    {
        // fix up angles in case user puts in wrong one!
        String _usernameAndDomain = usernameAndDomain.replace('/', '\\');
        // parse the domain and username values out of the username
        int separator = _usernameAndDomain.indexOf("\\");
        if (separator == -1)
        {
            throw new LoginException("Error: no separator (\\) found in the username pased in to distinguish between domain and username");
        }
        return _usernameAndDomain.substring(separator + 1);
    }

    public static String parseForDomain(String usernameAndDomain) throws LoginException
    {
        // fix up angles in case user puts in wrong one!
        String _usernameAndDomain = usernameAndDomain.replace('/', '\\');
        // parse the domain and username values out of the username
        int separator = _usernameAndDomain.indexOf("\\");
        if (separator == -1)
        {
            throw new LoginException("Error: no separator (\\) found in the username pased in to distinguish between domain and username");
        }
        return _usernameAndDomain.substring(0, separator);
    }
}
