package org.apache.fulcrum.security.torque.peer;

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

    @Override
    public <P extends Peer> P getPeerInstance(String peerClassName) throws DataBackendException
    {
        return getPeerInstance( peerClassName, Peer.class, null);
    }

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
