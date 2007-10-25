package org.apache.fulcrum.security.adapter.turbine;
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
import java.util.Date;
import java.util.Hashtable;

import javax.servlet.http.HttpSessionBindingEvent;

import org.apache.fulcrum.security.entity.SecurityEntity;
import org.apache.turbine.om.security.TurbineUser;
import org.apache.turbine.om.security.User;

/**
 * Adapter around Fulcrum User.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class UserAdapter extends BaseAdapter implements User
{

  /*
   *  turbineUser object to delegate extra methods that the fulcrum user
   * doesn't support
   */
  private TurbineUser turbineUser;


  public UserAdapter(org.apache.fulcrum.security.entity.User user)
     {
       super((SecurityEntity)user);
       turbineUser = new TurbineUser();
     }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#getAccessCounter()
     */
    public int getAccessCounter()
    {
        return 0;
    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#getAccessCounterForSession()
     */
    public int getAccessCounterForSession()
    {
        return 0;
    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#getLastAccessDate()
     */
    public Date getLastAccessDate()
    {
        return null;
    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#getCreateDate()
     */
    public Date getCreateDate()
    {
        return null;
    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#getLastLogin()
     */
    public Date getLastLogin()
    {
        return null;
    }
    /*
     * @see org.apache.turbine.om.security.User#getPassword()
     */
    public String getPassword()
    {
        return ((org.apache.fulcrum.security.entity.User)entity).getPassword();
    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#getPerm(java.lang.String)
     */
    public Object getPerm(String arg0)
    {

        return null;
    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#getPerm(java.lang.String, java.lang.Object)
     */
    public Object getPerm(String arg0, Object arg1)
    {

        return null;
    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#getPermStorage()
     */
    public Hashtable getPermStorage()
    {

        return null;
    }
    /*
     * @see org.apache.turbine.om.security.User#getTempStorage()
     */
    public Hashtable getTempStorage()
    {

        return turbineUser.getTempStorage();
    }
    /*
     * @see org.apache.turbine.om.security.User#getTemp(java.lang.String)
     */
    public Object getTemp(String arg0)
    {

		return turbineUser.getTemp(arg0);
    }
    /*
     * @see org.apache.turbine.om.security.User#getTemp(java.lang.String, java.lang.Object)
     */
    public Object getTemp(String arg0, Object arg1)
    {

        return turbineUser.getTemp(arg0,arg1);
    }
    /* Adapter from getUserName to getName!
     * @see org.apache.turbine.om.security.User#getUserName()
     */
    public String getUserName()
    {
        return getName();
    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#getFirstName()
     */
    public String getFirstName()
    {

        return null;
    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#getLastName()
     */
    public String getLastName()
    {

        return null;
    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#getEmail()
     */
    public String getEmail()
    {

        return null;
    }
    /*
     * @see org.apache.turbine.om.security.User#setHasLoggedIn(java.lang.Boolean)
     */
    public void setHasLoggedIn(Boolean arg0)
    {
		turbineUser.setHasLoggedIn(arg0);
    }
    /*
     * @see org.apache.turbine.om.security.User#hasLoggedIn()
     */
    public boolean hasLoggedIn()
    {
		return turbineUser.hasLoggedIn();
    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#incrementAccessCounter()
     */
    public void incrementAccessCounter()
    {

    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#incrementAccessCounterForSession()
     */
    public void incrementAccessCounterForSession()
    {

    }
    /*
     * @see org.apache.turbine.om.security.User#removeTemp(java.lang.String)
     */
    public Object removeTemp(String arg0)
    {
        return turbineUser.removeTemp(arg0);
    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#setAccessCounter(int)
     */
    public void setAccessCounter(int arg0)
    {

    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#setAccessCounterForSession(int)
     */
    public void setAccessCounterForSession(int arg0)
    {

    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#setLastAccessDate()
     */
    public void setLastAccessDate()
    {

    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#setLastLogin(java.util.Date)
     */
    public void setLastLogin(Date arg0)
    {

    }
    /*
     * @see org.apache.turbine.om.security.User#setPassword(java.lang.String)
     */
    public void setPassword(String arg0)
    {
        ((org.apache.fulcrum.security.entity.User)entity).setPassword(arg0);
    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#setPerm(java.lang.String, java.lang.Object)
     */
    public void setPerm(String arg0, Object arg1)
    {

    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#setPermStorage(java.util.Hashtable)
     */
    public void setPermStorage(Hashtable arg0)
    {

    }
    /*
     * @see org.apache.turbine.om.security.User#setTempStorage(java.util.Hashtable)
     */
    public void setTempStorage(Hashtable arg0)
    {
		turbineUser.setTempStorage(arg0);
    }
    /* D
     * @see org.apache.turbine.om.security.User#setTemp(java.lang.String, java.lang.Object)
     */
    public void setTemp(String arg0, Object arg1)
    {
		turbineUser.setTemp(arg0,arg1);
    }
    /* Adaper for user name to name.
     * @see org.apache.turbine.om.security.User#setUserName(java.lang.String)
     */
    public void setUserName(String arg0)
    {
        setName(arg0);
    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#setFirstName(java.lang.String)
     */
    public void setFirstName(String arg0)
    {

    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#setLastName(java.lang.String)
     */
    public void setLastName(String arg0)
    {

    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#setCreateDate(java.util.Date)
     */
    public void setCreateDate(Date arg0)
    {

    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#setEmail(java.lang.String)
     */
    public void setEmail(String arg0)
    {

    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#isConfirmed()
     */
    public boolean isConfirmed()
    {

        return false;
    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#setConfirmed(java.lang.String)
     */
    public void setConfirmed(String arg0)
    {

    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#getConfirmed()
     */
    public String getConfirmed()
    {
        return null;
    }
    /* Does Nothing.
     * @see org.apache.turbine.om.security.User#updateLastLogin()
     */
    public void updateLastLogin() throws Exception
    {
    }
    /* Does Nothing.
     * @see javax.servlet.http.HttpSessionBindingListener#valueBound(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void valueBound(HttpSessionBindingEvent arg0)
    {
    }
    /* Does Nothing.
     * @see javax.servlet.http.HttpSessionBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void valueUnbound(HttpSessionBindingEvent arg0)
    {
    }


}
