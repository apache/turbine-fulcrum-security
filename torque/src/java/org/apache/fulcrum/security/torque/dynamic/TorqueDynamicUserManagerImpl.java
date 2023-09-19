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

import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.torque.TorqueAbstractUserManager;
import org.apache.fulcrum.security.torque.om.TorqueDynamicUser;
import org.apache.fulcrum.security.torque.om.TorqueDynamicUserPeer;
import org.apache.fulcrum.security.util.DataBackendException;
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
public class TorqueDynamicUserManagerImpl extends TorqueAbstractUserManager
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /* (non-Javadoc)
     * @see org.apache.fulcrum.security.torque.TorqueAbstractUserManager#doSelectAllUsers(java.sql.Connection)
     */
    @SuppressWarnings("unchecked")
    @Override
	protected <T extends User> List<T> doSelectAllUsers(Connection con) throws TorqueException
    {
        Criteria criteria = new Criteria(TorqueDynamicUserPeer.DATABASE_NAME);

        return (List<T>)TorqueDynamicUserPeer.doSelect(criteria, con);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected <T extends User> List<T> doSelectUsers(Connection con, Criteria criteria) throws TorqueException
    {
        return (List<T>)TorqueDynamicUserPeer.doSelect(criteria, con);
    }

    /* (non-Javadoc)
     * @see org.apache.fulcrum.security.torque.TorqueAbstractUserManager#doSelectById(java.lang.Integer, java.sql.Connection)
     */
    @SuppressWarnings("unchecked")
	protected <T extends User> T doSelectById(Integer id, Connection con) throws NoRowsException, TooManyRowsException, TorqueException
    {
        return (T) TorqueDynamicUserPeer.retrieveByPK(id, con);
    }

    /* (non-Javadoc)
     * @see org.apache.fulcrum.security.torque.TorqueAbstractUserManager#doSelectByName(java.lang.String, java.sql.Connection)
     */
    @SuppressWarnings("unchecked")
	protected <T extends User> T doSelectByName(String name, Connection con) throws NoRowsException, TooManyRowsException, TorqueException
    {
        Criteria criteria = new Criteria(TorqueDynamicUserPeer.DATABASE_NAME);
        criteria.where(TorqueDynamicUserPeer.LOGIN_NAME, name);
        criteria.setIgnoreCase(true);
        criteria.setSingleRecord(true);

        List<TorqueDynamicUser> users = TorqueDynamicUserPeer.doSelect(criteria, con);

        if (users.isEmpty())
        {
            throw new NoRowsException(name);
        }

        return (T) users.get(0);
    }

}
