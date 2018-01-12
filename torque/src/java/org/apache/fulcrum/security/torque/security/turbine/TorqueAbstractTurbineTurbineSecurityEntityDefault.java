package org.apache.fulcrum.security.torque.security.turbine;
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
import java.sql.Connection;
import java.util.List;

import org.apache.fulcrum.security.model.ACLFactory;
import org.apache.fulcrum.security.model.turbine.entity.TurbineUserGroupRoleEntity;
import org.apache.fulcrum.security.torque.peer.TurbineUserGroupRoleModelPeerMapper;
import org.apache.torque.TorqueException;

/**
 * This abstract class extends the user manager to allow for custom OM attached objects (user group role relationships) for ACL.
 * 
 * Additional Torque Custom Peer contract
 *
 * @author <a href="mailto:gk@apache.org">Georg Kallidis</a>
 * @version $Id:$
 */
public abstract class TorqueAbstractTurbineTurbineSecurityEntityDefault extends TorqueAbstractTurbineTurbineSecurityEntity
    implements TurbineUserGroupRoleEntity // not in group and role but already in TurbineUser interface 
{
    
    private static final long serialVersionUID = 1L;
   
    /**
     * Retrieve attached objects for user: user-group-role relationship(s)
     * 
     * This method is required if custom peers is activated for (user) manager, to allow the custom group/role in {@link ACLFactory#getAccessControlList(org.apache.fulcrum.security.entity.User)}
     * 
     * @param con A database connection
     * @param lazy if <code>true</code>, fetches objects lazily
     * @param ugrs The custom TurbineUserGroupRole set
     */
    public abstract <T extends TurbineUserGroupRoleModelPeerMapper> void retrieveAttachedObjects( Connection con, Boolean lazy, List<T> ugrs ) throws TorqueException;
   
}
