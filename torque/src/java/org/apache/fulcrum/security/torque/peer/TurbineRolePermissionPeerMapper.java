package org.apache.fulcrum.security.torque.peer;
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

import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.model.turbine.entity.TurbinePermission;
import org.apache.torque.TorqueException;

/**
 * This extension to the marker interface {@linkplain Peer} is to allow for swappable Peer implementations 
 * in Turbine Torque Manager Implementations.  
 * 
 * 
 * @author <a href="mailto:gk@apache.org">Georg Kallidis</a>
 * @version $Id$
 */
public interface TurbineRolePermissionPeerMapper

{
	
    /**
     * Returns the associated TurbineRole object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TurbineRole object
     * @throws TorqueException  if any database error occurs when reading from the database fails.
     */
    Role getTurbineRole()
        throws TorqueException;

    /**
     * Return the associated TurbineRole object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TurbineRole object
     * @throws TorqueException  if any database error occurs
     */
    Role getTurbineRole( Connection connection )
        throws TorqueException;

    /**
     * Returns the associated TurbinePermission object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TurbinePermission object
     * @throws TorqueException when reading from the database fails.
     */
    Permission getTurbinePermission() throws TorqueException;
    
    /**
     * Return the associated TurbinePermission object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TurbinePermission object
     * @throws TorqueException If a problem occurs with the get[$filedType] method.
     */
    public TurbinePermission getTurbinePermission(Connection connection)
        throws TorqueException;

}
