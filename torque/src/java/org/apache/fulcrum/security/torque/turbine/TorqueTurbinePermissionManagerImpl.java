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

import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.torque.TorqueAbstractPermissionManager;
import org.apache.fulcrum.security.torque.om.TorqueTurbinePermissionPeer;
import org.apache.fulcrum.security.torque.peer.Peer;
import org.apache.fulcrum.security.torque.peer.PeerManagable;
import org.apache.fulcrum.security.torque.peer.PeerManager;
import org.apache.fulcrum.security.torque.peer.TorqueTurbinePeer;
import org.apache.fulcrum.security.torque.peer.managers.PeerPermissionManager;
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
public class TorqueTurbinePermissionManagerImpl extends PeerPermissionManager 
{
    
    
    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractPermissionManager#doSelectAllPermissions(java.sql.Connection)
     */
    @Override
	@SuppressWarnings("unchecked")
	protected <T extends Permission> List<T> doSelectAllPermissions(Connection con) throws TorqueException
    {
        Criteria criteria = new Criteria();
        
        if ( (getCustomPeer())) {
            try
            {
                return ((TorqueTurbinePeer<T>) getPeerInstance()).doSelect( criteria, con );
            }
            catch ( DataBackendException e )
            {
                throw new TorqueException( e );
            }
        } else {
            return (List<T>)TorqueTurbinePermissionPeer.doSelect(criteria, con);
        }

       
    }

    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractPermissionManager#doSelectById(java.lang.Integer, java.sql.Connection)
     */
    @Override
	@SuppressWarnings("unchecked")
	protected <T extends Permission> T doSelectById(Integer id, Connection con) throws NoRowsException, TooManyRowsException, TorqueException
    {
        if ( (getCustomPeer())) {
            try
            {
                return ((TorqueTurbinePeer<T>) getPeerInstance()).retrieveByPK(id, con);
            }
            catch ( DataBackendException e )
            {
                throw new TorqueException( e );
            }
        } else {
            return (T) TorqueTurbinePermissionPeer.retrieveByPK(id, con);
        } 
    }

    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractPermissionManager#doSelectByName(java.lang.String, java.sql.Connection)
     */
    @Override
	@SuppressWarnings("unchecked")
	protected <T extends Permission> T doSelectByName(String name, Connection con) throws NoRowsException, TooManyRowsException, TorqueException
    {
        Criteria criteria = new Criteria();
       
        criteria.setIgnoreCase(true);
        criteria.setSingleRecord(true);
        
        List<T> permissions = null;
        if ( (getCustomPeer())) {
            try
            {
            	TorqueTurbinePeer<T> peerInstance = (TorqueTurbinePeer<T>)getPeerInstance();
            	criteria.where(peerInstance.getTableMap().getColumn(getColumnName()), name);
            	permissions = peerInstance.doSelect( criteria, con );
                
            }
            catch ( DataBackendException e )
            {
                throw new TorqueException( e );
            }
        } else {
        	 criteria.where(TorqueTurbinePermissionPeer.PERMISSION_NAME, name);
            permissions = (List<T>) TorqueTurbinePermissionPeer.doSelect(criteria, con);
        } 

        if (permissions.isEmpty())
        {
            throw new NoRowsException(name);
        }

        return permissions.get(0);
    }
    
  
}
