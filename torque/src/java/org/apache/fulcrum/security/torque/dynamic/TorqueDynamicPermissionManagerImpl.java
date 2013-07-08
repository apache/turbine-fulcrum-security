package org.apache.fulcrum.security.torque.dynamic;
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

import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.torque.TorqueAbstractPermissionManager;
import org.apache.fulcrum.security.torque.om.TorqueDynamicPermission;
import org.apache.fulcrum.security.torque.om.TorqueDynamicPermissionPeer;
import org.apache.torque.NoRowsException;
import org.apache.torque.TooManyRowsException;
import org.apache.torque.TorqueException;
import org.apache.torque.criteria.Criteria;
/**
 * This implementation persists to a database via Torque.
 *
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id:$
 */
public class TorqueDynamicPermissionManagerImpl extends TorqueAbstractPermissionManager
{
    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractPermissionManager#doSelectAllPermissions(java.sql.Connection)
     */
    @SuppressWarnings("unchecked")
	protected <T extends Permission> List<T> doSelectAllPermissions(Connection con) throws TorqueException
    {
        Criteria criteria = new Criteria(TorqueDynamicPermissionPeer.DATABASE_NAME);

        return (List<T>)TorqueDynamicPermissionPeer.doSelect(criteria, con);
    }

    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractPermissionManager#doSelectById(java.lang.Integer, java.sql.Connection)
     */
    @SuppressWarnings("unchecked")
	protected <T extends Permission> T doSelectById(Integer id, Connection con) throws NoRowsException, TooManyRowsException, TorqueException
    {
        return (T) TorqueDynamicPermissionPeer.retrieveByPK(id, con);
    }

    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractPermissionManager#doSelectByName(java.lang.String, java.sql.Connection)
     */
    @SuppressWarnings("unchecked")
	protected <T extends Permission> T doSelectByName(String name, Connection con) throws NoRowsException, TooManyRowsException, TorqueException
    {
        Criteria criteria = new Criteria(TorqueDynamicPermissionPeer.DATABASE_NAME);
        criteria.where(TorqueDynamicPermissionPeer.PERMISSION_NAME, name);
        criteria.setIgnoreCase(true);
        criteria.setSingleRecord(true);

        List<TorqueDynamicPermission> permissions = TorqueDynamicPermissionPeer.doSelect(criteria, con);

        if (permissions.isEmpty())
        {
            throw new NoRowsException(name);
        }

        return (T) permissions.get(0);
    }
}
