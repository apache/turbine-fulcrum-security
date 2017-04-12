package org.apache.fulcrum.security.model.turbine.test;

import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.turbine.AbstractTurbineModelManager;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.UnknownEntityException;

public class ModelManagerDummyImpl extends AbstractTurbineModelManager  
{

	@Override
	public void grant(Role role, Permission permission)
			throws DataBackendException, UnknownEntityException {
		throw new DataBackendException("not yet implemented, just a test ");
	}

	@Override
	public void revoke(Role role, Permission permission)
			throws DataBackendException, UnknownEntityException {
		throw new DataBackendException("not yet implemented, just a test ");		
	}

	@Override
	public void grant(User user, Group group, Role role)
			throws DataBackendException, UnknownEntityException {
		throw new DataBackendException("not yet implemented, just a test ");	
	}

	@Override
	public void revoke(User user, Group group, Role role)
			throws DataBackendException, UnknownEntityException {
		throw new DataBackendException("not yet implemented, just a test ");
	}

}
