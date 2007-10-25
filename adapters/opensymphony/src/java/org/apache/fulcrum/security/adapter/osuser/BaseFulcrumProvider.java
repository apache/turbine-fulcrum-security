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
import java.util.Properties;

import org.apache.avalon.framework.component.ComponentException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.util.DataBackendException;

import com.opensymphony.user.Entity.Accessor;
import com.opensymphony.user.provider.UserProvider;

/**
 * Base implementation of the Fulcrum provider for OSUser. This is meant to
 * provide access from OSUser to the Fulcrum Security implementation.
 * Currently, to change things you should use the Fulcrum Security system
 * directly, this is a very mimimal implementation.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public abstract class BaseFulcrumProvider implements UserProvider
{

	/** Logging */
	private static Log log = LogFactory.getLog(BaseFulcrumProvider.class);
	/** Our Fulcrum Security Service to use */
	private static SecurityService securityService;

	/*
	 * Does nothing for now.
	 *
	 * @see com.opensymphony.user.provider.UserProvider#create(java.lang.String)
	 */
	public boolean create(String arg0)
	{
		return true;
	}

	/*
	 * Does nothign for now.
	 *
	 * @see com.opensymphony.user.provider.UserProvider#flushCaches()
	 */
	public void flushCaches()
	{

	}

	/*
	 * Doesn't do anything. Init isn't required as the Fulcrum Security is
	 * assumed to be running in an Avalon container.
	 *
	 * @see com.opensymphony.user.provider.UserProvider#init(java.util.Properties)
	 */
	public boolean init(Properties arg0)
	{
		return true;
	}

	/*
	 * Sets the accessor to be mutable, and returns true.
	 *
	 * @see com.opensymphony.user.provider.UserProvider#load(java.lang.String,
	 *      com.opensymphony.user.Entity.Accessor)
	 */
	public boolean load(String name, Accessor accessor)
	{
		accessor.setMutable(true);

		return true;
	}

	/*
	 * Returns false, this doesn't do anything.
	 *
	 * @see com.opensymphony.user.provider.UserProvider#remove(java.lang.String)
	 */
	public boolean remove(String arg0)
	{
		return false;
	}

	/*
	 * Returns false, this doesn't do anything right now.
	 *
	 * @see com.opensymphony.user.provider.UserProvider#store(java.lang.String,
	 *      com.opensymphony.user.Entity.Accessor)
	 */
	public boolean store(String arg0, Accessor arg1)
	{
		return false;
	}

	/**
	  * Lazy loads the SecurityService.
	  *
	  * @return
	  */
	 public SecurityService getSecurityService() throws DataBackendException
	 {
		 if (securityService == null)
		 {
			 try
			 {
			    throw new ComponentException(SecurityService.ROLE,"SecurityService not initialized!");
	//			securityService = (SecurityService) manager.lookup(SecurityService.ROLE);
			 }
			 catch (ComponentException ce)
			 {
				 throw new DataBackendException(ce.getMessage(), ce);
			 }
		 }
		 return securityService;
	 }

	/**
	 * The Fulcrum Security Service that will back the Fulcrum
	 * providers.
	 *
	 * @param securityService
	 *            The securityService to set.
	 */
	public static void setSecurityService(SecurityService asecurityService)
	{
		securityService = asecurityService;
	}


}
