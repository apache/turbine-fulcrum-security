package org.apache.fulcrum.security.adapter.osuser;
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

import java.util.List;

import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.PasswordMismatchException;
import org.apache.fulcrum.security.util.UnknownEntityException;

import com.opensymphony.user.Entity.Accessor;
import com.opensymphony.user.provider.CredentialsProvider;

/**
 * Fulcrum provider for OSUser. Primarily provides support for authenticating a
 * user. This delegates to whatever authenticator is configured in the
 * getSecurityService().
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id: FulcrumCredentialsProvider.java,v 1.2 2003/10/21 10:16:29
 *          epugh Exp $
 */
public class FulcrumCredentialsProvider
    extends BaseFulcrumProvider
    implements CredentialsProvider
{

    /*
     * Authenticate a user with their password.
     *
     * @see com.opensymphony.user.provider.CredentialsProvider#authenticate(java.lang.String,
     *      java.lang.String)
     */
    public boolean authenticate(String name, String password)
    {
        try
        {
            User user = getSecurityService().getUserManager().getUser(name);
            getSecurityService().getUserManager().authenticate(user, password);
            return true;
        }
        catch (PasswordMismatchException pme)
        {
            return false;
        }
        catch (UnknownEntityException uee)
        {
            return false;
        }
        catch (DataBackendException dbe)
        {
            throw new RuntimeException(dbe);
        }

    }

    /*
     * Not implemented.
     *
     * @see com.opensymphony.user.provider.CredentialsProvider#changePassword(java.lang.String,
     *      java.lang.String)
     */
    public boolean changePassword(String arg0, String arg1)
    {
		throw new RuntimeException("Not implemented");
    }

    /*
     * Not implemented.
     *
     * @see com.opensymphony.user.provider.UserProvider#create(java.lang.String)
     */
    public boolean create(String name)
    {

        throw new RuntimeException("Not implemented");

    }

    /*
     * Does nothing.
     *
     * @see com.opensymphony.user.provider.UserProvider#flushCaches()
     */
    public void flushCaches()
    {

    }

    /*
     * Returns whether a user exists or not.
     *
     * @see com.opensymphony.user.provider.UserProvider#handles(java.lang.String)
     */
    public boolean handles(String name)
    {
        try
        {
            User user = getSecurityService().getUserManager().getUser(name);
            return true;
        }
        catch (UnknownEntityException uee)
        {
            return false;
        }
        catch (DataBackendException dbe)
        {
            throw new RuntimeException(dbe);
        }
    }

    /*
     * Not implemented.
     *
     * @see com.opensymphony.user.provider.UserProvider#list()
     */
    public List list()
    {
        return null;
    }

    /*
     * Not implemented.
     *
     * @see com.opensymphony.user.provider.UserProvider#remove(java.lang.String)
     */
    public boolean remove(String arg0)
    {
		throw new RuntimeException("Not implemented");
    }

    /*
     * Not implemented.
     *
     * @see com.opensymphony.user.provider.UserProvider#store(java.lang.String,
     *      com.opensymphony.user.Entity.Accessor)
     */
    public boolean store(String arg0, Accessor arg1)
    {
		throw new RuntimeException("Not implemented");
    }

}
