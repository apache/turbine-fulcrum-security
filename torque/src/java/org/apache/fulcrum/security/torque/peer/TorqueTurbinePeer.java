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
import java.util.List;

import org.apache.torque.NoRowsException;
import org.apache.torque.TooManyRowsException;
import org.apache.torque.TorqueException;
import org.apache.torque.criteria.Criteria;
import org.apache.torque.map.TableMap;

/**
 * This extension to the marker interface {@linkplain Peer} is to allow for swappable Peer implementations 
 * in Turbine Torque Manager Implementations.  
 * 
 * @param <T>  The data object type used by the Torque PeerImpl class.
 * 
 * @author <a href="mailto:gk@apache.org">Georg Kallidis</a>
 * @version $Id$
 */
public interface TorqueTurbinePeer<T>
    extends Peer
{
	
	 TableMap getTableMap() throws TorqueException;
	
     List<T> doSelect( Criteria criteria,
                         Connection connection) throws TorqueException;
     
     T retrieveByPK(Integer pk, Connection con)
                     throws TorqueException, NoRowsException, TooManyRowsException;

}
