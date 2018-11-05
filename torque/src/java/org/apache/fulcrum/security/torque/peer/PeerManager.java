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
import java.io.Serializable;

import org.apache.fulcrum.security.spi.AbstractEntityManager;
import org.apache.fulcrum.security.util.DataBackendException;


/**
 * Use this manager in role-list, if you want to swap default peer classes.
 * It gets automatically registered in {@literal TorqueTurbine<Type>ManagerImpl classes. <Types> are: <User>, <Group>, <Permission>, <Role>}.
 * You have to register your {@literal <Type>PeerImpl classes} by adding the {@link TorqueTurbinePeer} interface.
 * 
 * If generating your ORM-classes from a Torque schema, you may have to delete or provide your own baseClasses in the schema.
 * 
 * @see <a href="http://db.apache.org/torque/torque-4.0/documentation/orm-reference/database-4-0.xsd" target=_blank>Torque 4.0 Schema</a>
 * @see org.apache.fulcrum.security.torque.turbine TorqueTurbine&lt;Type&gt;ManagerImpl referencing manager classes.
 *  
 * @author <a href="mailto:gk@apache.org">Georg Kallidis</a>
 * @version $Id$
 */
public interface PeerManager extends Serializable
{

    String ROLE = PeerManager.class.getName(); 

    /**
     * Expects the class name of a Torque Peer class, which could be instantiated. 
     * {@link org.apache.fulcrum.security.spi.AbstractEntityManager#getClassName()}
     *  
     * @param peerClassName the peerClassName
     * @return a cached peer class instance
     * @throws DataBackendException data backend exception
     */
    public abstract <P extends Peer> P getPeerInstance(String peerClassName)
        throws DataBackendException;

    /**
     * This method is provided to get more helpful exception messages.
     * 
     * @param peerClassName the peerClassName
     * @param class1 expected class the peers should implement 
     * @param className target class, i.e. the data object class type of the Peer object. The data object for which the peer is provided. 
     * @return peer instance
     * @throws DataBackendException data backend exception
     */
    public abstract <P extends Peer> P getPeerInstance( String peerClassName, Class<? extends Peer> class1, String className ) throws DataBackendException;


}
