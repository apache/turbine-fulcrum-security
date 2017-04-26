package org.apache.fulcrum.security.torque.peer;

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
     *  Expects the class name of a Torque Peer class, which could be instantiated. 
     *  @see AbstractEntityManager#getPeerClassName()
     *  
     * @param peerClassName the peerClassName
     * 
     * @return a (cashed) peer class instance
     */
    public abstract <P extends Peer> P getPeerInstance(String peerClassName)
        throws DataBackendException;

    /**
     * This method is provided to get more helpful exception messages.
     * 
     * @param peerClassName
     * @param class1 expected class the peers should implement 
     * @param className target class, i.e. the data object class type of the Peer object. The data object for which the peer is provided. 
     * @return
     * @throws DataBackendException
     */
    public abstract <P extends Peer> P getPeerInstance( String peerClassName, Class<? extends Peer> class1, String className ) throws DataBackendException;


}
