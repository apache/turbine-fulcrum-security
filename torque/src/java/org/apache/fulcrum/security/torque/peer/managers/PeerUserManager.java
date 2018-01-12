package org.apache.fulcrum.security.torque.peer.managers;
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
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.fulcrum.security.torque.TorqueAbstractUserManager;
import org.apache.fulcrum.security.torque.peer.Peer;
import org.apache.fulcrum.security.torque.peer.PeerManagable;
import org.apache.fulcrum.security.torque.peer.PeerManager;
import org.apache.fulcrum.security.torque.peer.TorqueTurbinePeer;
import org.apache.fulcrum.security.torque.peer.TorqueTurbineUserGroupRolePeer;
import org.apache.fulcrum.security.util.DataBackendException;
/**
 * This implementation persists to a database via Torque.
 *
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id:$
 */
public abstract class PeerUserManager extends TorqueAbstractUserManager implements PeerManagable
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * if set, the generic implementation is used (by setting @link {@link #PEER_CLASS_NAME_KEY} in configuration file.
	 */
    private Boolean customPeer = false;  //  used for torque which uses per object peer classes
    
    private String peerClassName;
    private static final String PEER_CLASS_NAME_KEY = "peerClassName";
    transient PeerManager peerManager;
    
	private String columnName = "LOGIN_NAME";
	
	private String columnName4UserGroupRole = "USER_ID";

    private String userGroupRolePeerClassName;
    
    /**
     * Avalon Service lifecycle method
     */
    @Override
	public void configure(Configuration conf) throws ConfigurationException
    {
       super.configure( conf );
       
//        peerClassName = conf.getChild( PEER_CLASS_NAME_KEY).getValue( "org.apache.fulcrum.security.torque.om.TorqueTurbineUserPeer" );
        peerClassName = conf.getChild( PEER_CLASS_NAME_KEY).getValue( null );
        if (peerClassName != null) {
            setPeerClassName( peerClassName );
            setCustomPeer(true);
        } 
        
        Configuration[]  userGroupRoleConf = conf.getChild( "userGroupRoleManager").getChildren();
        for ( int i = 0; i < userGroupRoleConf.length; i++ )
        {
            Configuration configuration = userGroupRoleConf[i];
            if ( configuration.getName().equals(PEER_CLASS_NAME_KEY) ) {
                userGroupRolePeerClassName = configuration.getValue( null );
                if (userGroupRolePeerClassName != null) {
                    setUserGroupRolePeerClassName( userGroupRolePeerClassName );
                    setCustomPeer(true);
                } 
            }  
        }
        if (peerClassName != null && userGroupRolePeerClassName == null) {
            //peerClassName.replace( "TurbineUserPeerImpl", "TurbineUserPeerImpl )
            throw new ConfigurationException("If using peers in component configuration (xml) the element userManager expects to have a userGroupRoleManager child element with peerClassName child element.");
        }

    }
  
    
    @Override
	public Peer getPeerInstance() throws DataBackendException {
        return getPeerManager().getPeerInstance(getPeerClassName(), TorqueTurbinePeer.class, getClassName());
    }
    
    public Peer getUserGroupRolePeerInstance() throws DataBackendException {
        return getPeerManager().getPeerInstance(getUserGroupRolePeerClassName(), TorqueTurbineUserGroupRolePeer.class, getClassName());
    }
    
    /**
     * @return Returns the persistenceHelper.
     */
    @Override
    public PeerManager getPeerManager()
    {
        if (peerManager == null)
        {
            peerManager = (PeerManager) resolve(PeerManager.ROLE);
        }
        return peerManager;
    }

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
    
    /**
     * 
     * @return if <code>true</code>, the generic implementation is used.
     */
    @Override
	public Boolean getCustomPeer()
    {
        return customPeer;
    }

    @Override
	public void setCustomPeer( Boolean customPeer )
    {
        this.customPeer = customPeer;
    }

    @Override
	public String getPeerClassName()
    {
        return peerClassName;
    }

    @Override
	public void setPeerClassName( String peerClassName )
    {
        this.peerClassName = peerClassName;
    }


    public String getUserGroupRolePeerClassName()
    {
        return userGroupRolePeerClassName;
    }


    public void setUserGroupRolePeerClassName( String userGroupRolePeerClassName )
    {
        this.userGroupRolePeerClassName = userGroupRolePeerClassName;
    }


    public String getColumnName4UserGroupRole()
    {
        return columnName4UserGroupRole;
    }


    public void setColumnName4UserGroupRole( String columnName4UserGroupRole )
    {
        this.columnName4UserGroupRole = columnName4UserGroupRole;
    }
   
    
}
