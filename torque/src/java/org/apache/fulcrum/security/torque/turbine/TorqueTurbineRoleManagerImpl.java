package org.apache.fulcrum.security.torque.turbine;
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

import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.torque.TorqueAbstractRoleManager;
import org.apache.fulcrum.security.torque.om.TorqueTurbineRolePeer;
import org.apache.torque.NoRowsException;
import org.apache.torque.TooManyRowsException;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;
/**
 * This implementation persists to a database via Torque.
 *
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id:$
 */
public class TorqueTurbineRoleManagerImpl extends TorqueAbstractRoleManager
{
    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractRoleManager#doSelectAllRoles(java.sql.Connection)
     */
    protected List doSelectAllRoles(Connection con) throws TorqueException
    {
        Criteria criteria = new Criteria(TorqueTurbineRolePeer.DATABASE_NAME);

        return TorqueTurbineRolePeer.doSelect(criteria, con);
    }

    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractRoleManager#doSelectById(java.lang.Integer, java.sql.Connection)
     */
    protected Role doSelectById(Integer id, Connection con) throws NoRowsException, TooManyRowsException, TorqueException
    {
        return TorqueTurbineRolePeer.retrieveByPK(id, con);
    }

    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractRoleManager#doSelectByName(java.lang.String, java.sql.Connection)
     */
    protected Role doSelectByName(String name, Connection con) throws NoRowsException, TooManyRowsException, TorqueException
    {
        Criteria criteria = new Criteria(TorqueTurbineRolePeer.DATABASE_NAME);
        criteria.add(TorqueTurbineRolePeer.ROLE_NAME, name);
        criteria.setIgnoreCase(true);
        criteria.setSingleRecord(true);

        List roles = TorqueTurbineRolePeer.doSelect(criteria, con);

        if (roles.isEmpty())
        {
            throw new NoRowsException(name);
        }

        return (Role)roles.get(0);
    }
}
