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

import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.turbine.TurbineUserManager;
import org.apache.fulcrum.security.torque.om.TorqueTurbineUserPeer;
import org.apache.fulcrum.security.torque.peer.TorqueTurbinePeer;
import org.apache.fulcrum.security.torque.peer.managers.PeerUserManager;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.apache.torque.NoRowsException;
import org.apache.torque.TooManyRowsException;
import org.apache.torque.TorqueException;
import org.apache.torque.criteria.Criteria;
/**
 * This implementation persists to a database via Torque.
 * 
 *
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id$
 */
public class TorqueTurbineUserManagerImpl extends PeerUserManager implements TurbineUserManager
{

    private static final String ANON = "anon";

    /**
     * Default implementation.
     */
    @Override
    public <T extends User> T getAnonymousUser()
        throws UnknownEntityException
    {
        try
        {
            T anonUser =  getUser( ANON );
            // add more, if needed
            return anonUser;
        }
        catch ( DataBackendException e )
        {
            throw new UnknownEntityException( "Failed to load anonymous user",e);
        } 
    }

    /**
     * Default implementation.
     */
    @Override
    public boolean isAnonymousUser( User u )
    {
        try
        {
            User anon = getAnonymousUser();
            if (u.equals( anon )) 
                {
                 return true;
                }
        }
        catch ( Exception e )
        {
            getLogger().error( "Failed to check user:" + e.getMessage(),e);
        }
        return false;
    }
    
    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractUserManager#doSelectAllUsers(java.sql.Connection)
     */
   
    @Override
	@SuppressWarnings("unchecked")
	protected <T extends User> List<T> doSelectAllUsers(Connection con) throws TorqueException
    {
        Criteria criteria = new Criteria();
        
        if ( (getCustomPeer())) {
            try
            {
            	TorqueTurbinePeer<T> peerInstance = (TorqueTurbinePeer<T>)getPeerInstance();
                return peerInstance.doSelect( criteria, con );
            }
            catch ( DataBackendException e )
            {
                throw new TorqueException( e );
            }
        } else {
            return (List<T>) TorqueTurbineUserPeer.doSelect(criteria, con);
        }
    }

    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractUserManager#doSelectById(java.lang.Integer, java.sql.Connection)
     */
    @Override
	@SuppressWarnings("unchecked")
	protected <T extends User> T doSelectById(Integer id, Connection con) throws NoRowsException, TooManyRowsException, TorqueException
    {
        if ( (getCustomPeer())) {
            try
            {
            	TorqueTurbinePeer<T> peerInstance = (TorqueTurbinePeer<T>)getPeerInstance();
                return peerInstance.retrieveByPK( id, con );
            }
            catch ( DataBackendException e )
            {
                throw new TorqueException( e );
            }
        } else {
            return  (T)  TorqueTurbineUserPeer.retrieveByPK(id, con);
        }
    }

    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractUserManager#doSelectByName(java.lang.String, java.sql.Connection)
     */
    @Override
	@SuppressWarnings("unchecked")
	protected <T extends User> T doSelectByName(String name, Connection con) throws NoRowsException, TooManyRowsException, TorqueException
    {
        Criteria criteria = new Criteria();
        
        criteria.setIgnoreCase(true);
        criteria.setSingleRecord(true);
        
        List<T> users = null;
        if ( (getCustomPeer())) {
            try
            {
            	TorqueTurbinePeer<T> peerInstance = (TorqueTurbinePeer<T>)getPeerInstance();
            	criteria.where(peerInstance.getTableMap().getColumn(getColumnName() ), name);
                users = peerInstance.doSelect( criteria, con );
            }
            catch ( DataBackendException e )
            {
                throw new TorqueException( e );
            }
        } else {
        	criteria.where(TorqueTurbineUserPeer.LOGIN_NAME, name);
        	users = (List<T>) TorqueTurbineUserPeer.doSelect(criteria, con);
        }


        if (users.isEmpty())
        {
            throw new NoRowsException(name);
        }
        
        return users.get(0);
    }

}
