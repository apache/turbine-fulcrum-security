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
import java.security.NoSuchAlgorithmException;

import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.fulcrum.crypto.CryptoAlgorithm;
import org.apache.fulcrum.crypto.CryptoService;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.UnknownEntityException;
/**
 * This class authenticates using the Fulcrum Crypto service a user and
 * their password
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 * @avalon.component name="crypto-authenticator"
 * @avalon.service type="org.apache.fulcrum.security.authenticator.Authenticator"
 */
public class CryptoAuthenticator extends AbstractLogEnabled
    implements Authenticator, Serviceable, Disposable, Configurable
{
    boolean composed = false;
    protected CryptoService cryptoService = null;
    private String algorithm;
    private String cipher;

    /**
     * Authenticate a user with the specified password. If authentication
     * is successful the method returns true. If it fails, it returns false
     * If there are any problems, an exception is thrown.
     *
     * @param user a User object.
     * @param password the user supplied password.
     * @exception UnknownEntityException if the user's account does not
     *            exist in the database.
     * @exception DataBackendException if there is a problem accessing the
     *            storage.
     */
    public boolean authenticate(User user, String password) throws  DataBackendException
    {
        try
        {
            CryptoAlgorithm ca = cryptoService.getCryptoAlgorithm(algorithm);
            ca.setCipher(cipher);
            String output = ca.encrypt(password);
            return output.equals(user.getPassword());
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new DataBackendException(e.getMessage(), e);
        }
        catch (Exception ex)
        {
            throw new DataBackendException(ex.getMessage(), ex);
        }
    }

	// ---------------- Avalon Lifecycle Methods ---------------------
    /**
 	 * Avalon component lifecycle method
	 */
    public void configure(Configuration conf) throws ConfigurationException
    {
    	algorithm = conf.getChild("algorithm").getValue();
    	cipher = conf.getChild("cipher").getValue();
    }

    /**
     * Avalon component lifecycle method
     */
    public void service(ServiceManager manager) throws ServiceException
    {
        this.cryptoService = (CryptoService)manager.lookup(CryptoService.ROLE);
    }

    /**
     * Avalon component lifecycle method
     */
    public void dispose()
    {
        cryptoService = null;
    }
}
