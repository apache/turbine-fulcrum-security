package org.apache.fulcrum.security.model.turbine.test;

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

import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.turbine.AbstractTurbineModelManager;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.UnknownEntityException;

public class ModelManagerDummyImpl extends AbstractTurbineModelManager  
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void grant(Role role, Permission permission)
			throws DataBackendException, UnknownEntityException {
		throw new DataBackendException("not yet implemented, just a test ");
	}

	@Override
	public void revoke(Role role, Permission permission)
			throws DataBackendException, UnknownEntityException {
		throw new DataBackendException("not yet implemented, just a test ");		
	}

	@Override
	public void grant(User user, Group group, Role role)
			throws DataBackendException, UnknownEntityException {
		throw new DataBackendException("not yet implemented, just a test ");	
	}

	@Override
	public void revoke(User user, Group group, Role role)
			throws DataBackendException, UnknownEntityException {
		throw new DataBackendException("not yet implemented, just a test ");
	}

    @Override
    public void replace( User user, Role oldRole, Role newRole ) throws DataBackendException
    {
        throw new DataBackendException("not yet implemented, just a test ");
    }

}
