package org.apache.fulcrum.security.torque.peer;

import org.apache.fulcrum.security.util.DataBackendException;

public interface PeerManagable
{

    public abstract PeerManager getPeerManager();
    
    public Peer getPeerInstance() throws DataBackendException;
    
    public Boolean getCustomPeer();
    public void setCustomPeer( Boolean customPeer );

    public String getPeerClassName();

    public void setPeerClassName( String peerClassName );
}
