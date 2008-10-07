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

import javax.naming.NamingException;

import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.fulcrum.security.UserManager;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.ldap.LDAPUserManagerImpl;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.UnknownEntityException;

/**
 * This class authenticates a user against LDAP.
 *
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id: LDAPAuthenticator.java 535465 2007-05-05 06:58:06Z tv $
 */
public class LDAPAuthenticator extends AbstractLogEnabled implements Authenticator, Serviceable
{
    /** Access to the associated UserManager */
    private LDAPUserManagerImpl userManager;
    
    /**
     * Authenticate an user with the specified password. If authentication
     * is successful the method returns true. If it fails, it returns false
     * If there are any problems, an exception is thrown.
     *
     *
     * @param user a LDAPUser object.
     * @param password the user supplied password.
     * @exception UnknownEntityException if the user's account does not
     *            exist in the database.
     * @exception DataBackendException if there is a problem accessing the
     *            storage.
     */
    public boolean authenticate(User user, String password)
        throws  DataBackendException
    {
        try
        {
            userManager.bind(userManager.getDN(user), password);
        }
        catch (NamingException ex)
        {
            throw new DataBackendException(
                    "NamingException caught:", ex);
        }
        
        return true;
    }

    /**
     * Avalon Lifecycle method
     * 
     * @see org.apache.avalon.framework.service.Serviceable#service(org.apache.avalon.framework.service.ServiceManager)
     */
    public void service(ServiceManager manager) throws ServiceException
    {
        try
        {
            this.userManager = (LDAPUserManagerImpl)manager.lookup(UserManager.ROLE);
        }
        catch (ClassCastException e)
        {
            throw new ServiceException(Authenticator.ROLE, "LDAPAuthenticator needs a configured LDAPUserManagerImpl.", e);
        }
    }

}
