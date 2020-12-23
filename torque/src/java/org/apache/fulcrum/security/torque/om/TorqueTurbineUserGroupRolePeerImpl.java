package org.apache.fulcrum.security.torque.om;

import java.sql.Connection;
import java.util.List;

import org.apache.torque.TorqueException;
import org.apache.torque.criteria.Criteria;

/**
 * The skeleton for this class was autogenerated by Torque on:
 *
 * [Mon Dec 21 11:27:28 CET 2020]
 *
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */

public class TorqueTurbineUserGroupRolePeerImpl
    extends org.apache.fulcrum.security.torque.om.BaseTorqueTurbineUserGroupRolePeerImpl
    implements org.apache.fulcrum.security.torque.peer.TorqueTurbineUserGroupRolePeer
{
    /** Serial version */
    private static final long serialVersionUID = 1608546448609L;

	@Override
	public List doSelectJoinTurbineRole(Criteria criteria, Connection con) throws TorqueException {
		return doSelectJoinTorqueTurbineRole(criteria, con);
	}



}