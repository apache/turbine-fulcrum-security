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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.avalon.framework.activity.Disposable;
import org.apache.fulcrum.security.spi.AbstractManager;
import org.apache.fulcrum.security.util.DataBackendException;

/**
 * Use this class, if you want to replace the default Torque Peer classes with your own. 
 *  
 * To use it, the PeerImpl classes (usually generated) must implement 
 * at least the {@linkplain Peer} marker interface or some extended interface.
 * 
 * @see PeerManager
 * 
 * @author <a href="mailto:gk@apache.org">Georg Kallidis</a>
 * @version $Id$
 * 
 */
public class PeerManagerDefaultImpl extends AbstractManager
    implements PeerManager, Disposable
{
      
	 /** Serial version */
	private static final long serialVersionUID = -3891813089694207441L;
	private Map<String,Peer> peers = new ConcurrentHashMap<String,Peer>(4,0.75f,4);

    /* (non-Javadoc)
     * @see org.apache.fulcrum.security.torque.peer.PeerManager#getPeerInstance(java.lang.String)
     */
    @Override
    public <P extends Peer> P getPeerInstance(String peerClassName) throws DataBackendException
    {
        return getPeerInstance( peerClassName, Peer.class, null);
    }

    /* (non-Javadoc)
     * @see org.apache.fulcrum.security.torque.peer.PeerManager#getPeerInstance(java.lang.String, java.lang.Class, java.lang.String)
     */
    @SuppressWarnings( "unchecked" )
    @Override
    public <P extends Peer> P getPeerInstance( String peerClassName, Class<? extends Peer> peerInterface , String className) throws DataBackendException
    {
        if (peers.containsKey(peerClassName )) {
            getLogger().debug( " get cached PeerInstance():" +  peers.get( peerClassName ));
            return (P) peers.get( peerClassName );
        }
        try
        {
            P peer = (P) Class.forName(peerClassName).getConstructor().newInstance();
            getLogger().debug( " getPeerInstance():" +  peer);
            peers.put( peerClassName, peer );
            return peer;
        }
        catch (ClassCastException e) {
            throw new DataBackendException( e.getMessage()+ ".\nThe peer class " + peerClassName + " should implement "+ peerInterface + "\n of generic type <"+className +">.",e );
        } 
        catch (Throwable e)
        {
            throw new DataBackendException("Problem creating instance of class " + peerClassName, e);
        }             
    }


}
