package org.apache.fulcrum.security.model.basic;
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

import org.apache.fulcrum.security.ModelManager;
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.UnknownEntityException;

/**
 * Describes all the relationships between entities in the "Basic" model.
 * This model pretty much allows a user to be in multiple groups.  There
 * is no concept of Permissions, Roles, Groups in Groups etc..
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public interface BasicModelManager extends ModelManager
{
     /**
	 * Puts a user in a group.
	 *
	 * This method is used when adding a user to a group
	 *
	 * @param user the User.
	 * @throws DataBackendException if there was an error accessing the data backend.
	 * @throws UnknownEntityException if the account is not present.
	 */
    void grant(User user, Group group) throws DataBackendException, UnknownEntityException;
    /**
	 * Removes a user from a group
	 *
	 * @param user the User.
	 * @throws DataBackendException if there was an error accessing the data backend.
	 * @throws UnknownEntityException if the user or group is not present.
	 */
    void revoke(User user, Group group) throws DataBackendException, UnknownEntityException;
    /**
	 * Revokes all groups from an User.
	 *
	 * This method is used when deleting an account.
	 *
	 * @param user the User.
	 * @throws DataBackendException if there was an error accessing the data backend.
	 * @throws UnknownEntityException if the account is not present.
	 */
    void revokeAll(User user) throws DataBackendException, UnknownEntityException;
}
