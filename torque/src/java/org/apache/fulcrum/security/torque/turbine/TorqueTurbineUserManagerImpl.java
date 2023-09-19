package org.apache.fulcrum.security.torque.turbine;
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

import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.turbine.TurbineUserManager;
import org.apache.fulcrum.security.torque.om.TorqueTurbineUserPeer;
import org.apache.fulcrum.security.torque.peer.TorqueTurbinePeer;
import org.apache.fulcrum.security.torque.peer.TorqueTurbineUserGroupRolePeer;
import org.apache.fulcrum.security.torque.peer.TurbineUserGroupRoleModelPeerMapper;
import org.apache.fulcrum.security.torque.peer.managers.PeerUserManager;
import org.apache.fulcrum.security.torque.security.TorqueAbstractSecurityEntity;
import org.apache.fulcrum.security.torque.security.turbine.TorqueAbstractTurbineTurbineSecurityEntityDefault;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.apache.fulcrum.security.util.UserSet;
import org.apache.torque.NoRowsException;
import org.apache.torque.TooManyRowsException;
import org.apache.torque.TorqueException;
import org.apache.torque.criteria.Criteria;
import org.apache.torque.util.Transaction;
/**
 * This implementation persists to a database via Torque.
 * 
 *
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id$
 */
public class TorqueTurbineUserManagerImpl extends PeerUserManager implements TurbineUserManager
{

	/** Serial version */
	private static final long serialVersionUID = 1L;
	private static final String ANON = "anon";

    /**
     * Default implementation.
     */
    @Override
    public <T extends User> T getAnonymousUser()
        throws UnknownEntityException
    {
        try
        {
            T anonUser =  getUser( ANON );
            // add more, if needed
            return anonUser;
        }
        catch ( DataBackendException e )
        {
            throw new UnknownEntityException( "Failed to load anonymous user",e);
        } 
    }

    /**
     * Default implementation.
     */
    @Override
    public boolean isAnonymousUser( User u )
    {
        try
        {
            User anon = getAnonymousUser();
            if (u.equals( anon )) 
                {
                 return true;
                }
        }
        catch ( Exception e )
        {
            getLogger().error( "Failed to check user:" + e.getMessage(),e);
        }
        return false;
    }
    
    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractUserManager#doSelectAllUsers(java.sql.Connection)
     */
   
    @Override
	@SuppressWarnings("unchecked")
	protected <T extends User> List<T> doSelectAllUsers(Connection con) throws TorqueException
    {
        Criteria criteria = new Criteria();
        
        if ( (getCustomPeer())) {
            try
            {
            	TorqueTurbinePeer<T> peerInstance = (TorqueTurbinePeer<T>)getPeerInstance();
                return peerInstance.doSelect( criteria, con );
            }
            catch ( DataBackendException e )
            {
                throw new TorqueException( e );
            }
        } else {
            return (List<T>) TorqueTurbineUserPeer.doSelect(criteria, con);
        }
    }

    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractUserManager#doSelectById(java.lang.Integer, java.sql.Connection)
     */
    @Override
	@SuppressWarnings("unchecked")
	protected <T extends User> T doSelectById(Integer id, Connection con) throws NoRowsException, TooManyRowsException, TorqueException
    {
        if ( (getCustomPeer())) {
            try
            {
            	TorqueTurbinePeer<T> peerInstance = (TorqueTurbinePeer<T>)getPeerInstance();
                return peerInstance.retrieveByPK( id, con );
            }
            catch ( DataBackendException e )
            {
                throw new TorqueException( e );
            }
        } else {
            return  (T)  TorqueTurbineUserPeer.retrieveByPK(id, con);
        }
    }

    /**
     * @see org.apache.fulcrum.security.torque.TorqueAbstractUserManager#doSelectByName(java.lang.String, java.sql.Connection)
     */
    @Override
	@SuppressWarnings("unchecked")
	protected <T extends User> T doSelectByName(String name, Connection con) throws NoRowsException, TooManyRowsException, TorqueException
    {
        Criteria criteria = new Criteria();
        
        criteria.setIgnoreCase(true);
        criteria.setSingleRecord(true);
        
        List<T> users = null;
        if ( (getCustomPeer())) {
            try
            {
            	TorqueTurbinePeer<T> peerInstance = (TorqueTurbinePeer<T>)getPeerInstance();
            	criteria.where(peerInstance.getTableMap().getColumn(getColumnName() ), name);
                users = peerInstance.doSelect( criteria, con );
            }
            catch ( DataBackendException e )
            {
                throw new TorqueException( e );
            }
        } else {
        	criteria.where(TorqueTurbineUserPeer.LOGIN_NAME, name);
        	users = (List<T>) TorqueTurbineUserPeer.doSelect(criteria, con);
        }


        if (users.isEmpty())
        {
            throw new NoRowsException(name);
        }
        
        return users.get(0);
    }
    
    /**
     * Retrieve a user from persistent storage using username as the
     * key. Also retrieves all attached objects (user group role relationships).
     *
     * @param userName the name of the user.
     * @return an User object.
     * @exception UnknownEntityException if the user's account does not
     *            exist in the database.
     * @exception DataBackendException if there is a problem accessing the
     *            storage.
     */
    @Override
    public <T extends User> T getUser(String userName) throws UnknownEntityException, DataBackendException
    {
        T user = null;
        Connection con = null;

        try
        {
            con = Transaction.begin();

            user = doSelectByName(userName.toLowerCase(), con);
            
            // Add attached objects if they exist
            attachRelatedObjects( user, con ); 

            Transaction.commit(con);
            con = null;
        }
        catch (NoRowsException e)
        {
            throw new UnknownEntityException("Unknown user '" + userName + "'");
        }
        catch (TooManyRowsException e)
        {
            throw new DataBackendException("Multiple Users with same username '" + userName + "'");
        }
        catch (TorqueException e)
        {
            throw new DataBackendException("Error retrieving user information", e);
        }
        finally
        {
            if (con != null)
            {
                Transaction.safeRollback(con);
            }
        }

        return user;
    }
  
  
  /**
   * Retrieves a filtered user list with attached related objects (user group role relationships) defined in the system.
   *
   * @return the names of all users defined in the system.
   * @throws DataBackendException if there was an error accessing the data
   *         backend.
   */
   @Override
   public <T extends User> UserSet<T> retrieveUserList(Object criteriaObject) throws DataBackendException
    {
       
        Criteria criteria = (Criteria) criteriaObject;
        UserSet<T> userSet = new UserSet<T>();
        Connection con = null;
    
        try
        {
            con = Transaction.begin();
    
            List<User> users = doSelectUsers(con, criteria);
    
            for (User user : users)
            {
                // Add attached objects if they exist
                attachRelatedObjects( user, con ); 
    
                userSet.add(user);
            }
    
            Transaction.commit(con);
            con = null;
        }
        catch (TorqueException e)
        {
            throw new DataBackendException("Error retrieving all users", e);
        }
        finally
        {
            if (con != null)
            {
                Transaction.safeRollback(con);
            }
        }
    
        return userSet;
    }

    protected <T extends User> List<T> doSelectUsers(Connection con, Criteria criteria) throws TorqueException
    {
        
        if ( (getCustomPeer())) {
            try
            {
                TorqueTurbinePeer<T> peerInstance = (TorqueTurbinePeer<T>)getPeerInstance();
                return peerInstance.doSelect( criteria, con );
            }
            catch ( DataBackendException e )
            {
                throw new TorqueException( e );
            }
        } else {
            return (List<T>) TorqueTurbineUserPeer.doSelect(criteria, con);
        }
    }


  /**
   * Retrieve a User object with specified id and all attached objects (user group role relationships).
   *
   * @param id
   *            the id of the User.
   * @return an object representing the User with specified id.
   * @throws DataBackendException
   *             if there was an error accessing the data backend.
   * @throws UnknownEntityException
   *             if the user does not exist.
   */
  @Override
  public <T extends User> T getUserById(Object id) throws DataBackendException, UnknownEntityException
  {
      T user;

      if (id != null && id instanceof Integer)
      {
          Connection con = null;

          try
          {
              con = Transaction.begin();

              user = doSelectById((Integer)id, con);

              // Add attached objects if they exist
              attachRelatedObjects( user, con ); 

              Transaction.commit(con);
              con = null;
          }
          catch (NoRowsException e)
          {
              throw new UnknownEntityException("User with id '" + id + "' does not exist.", e);
          }
          catch (TorqueException e)
          {
              throw new DataBackendException("Error retrieving user information", e);
          }
          finally
          {
              if (con != null)
              {
                  Transaction.safeRollback(con);
              }
          }
      }
      else
      {
          throw new UnknownEntityException("Invalid user id '" + id + "'");
      }

      return user;
  }
  
  /**
   * Retrieves all related objects (user group roles). If the objects not exists {@link DataBackendException} is wrapped in a new TorqueException.
   * 
   * @param user
   * @param con
   * @throws TorqueException
   */
  private <T extends User> void attachRelatedObjects( T user, Connection con ) throws TorqueException
  {
      if (user instanceof TorqueAbstractSecurityEntity) {
          if (getCustomPeer()) {
              try
              {
                  TorqueTurbineUserGroupRolePeer<TurbineUserGroupRoleModelPeerMapper> peerInstance = 
                                  (TorqueTurbineUserGroupRolePeer<TurbineUserGroupRoleModelPeerMapper>) getUserGroupRolePeerInstance();
                  Criteria criteria = new Criteria();
                  // expecting the same name in any custom implementation
                  criteria.where(peerInstance.getTableMap().getColumn(getColumnName4UserGroupRole() ), ( (TorqueAbstractSecurityEntity) user ).getEntityId() );                        
                  List<TurbineUserGroupRoleModelPeerMapper> ugrs = peerInstance.doSelectJoinTurbineGroup( criteria, con );
                  
                  if (user instanceof TorqueAbstractTurbineTurbineSecurityEntityDefault) {
                      ((TorqueAbstractTurbineTurbineSecurityEntityDefault)user).retrieveAttachedObjects(con, false, ugrs);
                  }
              }
              catch ( DataBackendException e )
              {
                  throw new TorqueException( e );
              }
          } else {
              try
              {
                ((TorqueAbstractSecurityEntity)user).retrieveAttachedObjects(con);
              }
              catch ( DataBackendException e )
              {
                  throw new TorqueException( e );
              }
          }
      }
  }

}
