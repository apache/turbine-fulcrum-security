package org.apache.fulcrum.security.torque.peer;

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
