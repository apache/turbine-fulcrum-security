package org.apache.fulcrum.security.ldap.dynamic;

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

import java.util.Hashtable;

import org.apache.fulcrum.security.ldap.LDAPUser;
import org.apache.fulcrum.security.ldap.LDAPUserManagerImpl;
import org.apache.fulcrum.security.model.dynamic.entity.impl.DynamicUserImpl;

/**
 * LDAPDynamicUser implements User and provides access to a user who accesses the
 * system via LDAP.
 *
 * @author <a href="mailto:cberry@gluecode.com">Craig D. Berry</a>
 * @author <a href="mailto:tadewunmi@gluecode.com">Tracy M. Adewunmi</a>
 * @author <a href="mailto:lflournoy@gluecode.com">Leonard J. Flournoy </a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @author <a href="mailto:hhernandez@itweb.com.mx">Humberto Hernandez</a>
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id: LDAPDynamicUser.java 534527 2007-05-02 16:10:59Z tv $
 */
public class LDAPDynamicUser extends DynamicUserImpl
    implements LDAPUser
{
    /** Serial Version UID */
    private static final long serialVersionUID = 3953123276619326752L;

    /** This is data that will survive a servlet engine restart. */
    private Hashtable permStorage = null;

    /** This is data that will not survive a servlet engine restart. */
    private Hashtable tempStorage = null;

    /**
     * Constructor.
     * Create a new User and set the createDate.
     */
    public LDAPDynamicUser()
    {
        tempStorage = new Hashtable(10);
        permStorage = new Hashtable(10);
    }

    /**
     * Returns the Email for this user.  If this is defined, then
     * the user is considered logged in.
     *
     * @return A String with the user's Email.
     */
    public String getEmail()
    {
        String tmp = null;

        tmp = (String) getPerm(LDAPUserManagerImpl.LDAP_USER_EMAIL_KEY);
        if (tmp != null && tmp.length() == 0)
        {
            tmp = null;
        }
        return tmp;
    }

    /**
     * Get an object from permanent storage.
     * @param name The object's name.
     * @return An Object with the given name.
     */
    public Object getPerm(String name)
    {
        return permStorage.get(name);
    }

    /**
     * Get an object from permanent storage; return default if value
     * is null.
     *
     * @param name The object's name.
     * @param def A default value to return.
     * @return An Object with the given name.
     */
    public Object getPerm(String name, Object def)
    {
        try
        {
            Object val = permStorage.get(name);

            if (val == null)
            {
                return def;
            }
            return val;
        }
        catch (Exception e)
        {
            return def;
        }
    }

    /**
     * This should only be used in the case where we want to save the
     * data to the database.
     *
     * @return A Hashtable.
     */
    public Hashtable getPermStorage()
    {
        if (this.permStorage == null)
        {
            this.permStorage = new Hashtable();
        }
        return this.permStorage;
    }

    /**
     * Get an object from temporary storage.
     *
     * @param name The object's name.
     * @return An Object with the given name.
     */
    public Object getTemp(String name)
    {
        return tempStorage.get(name);
    }

    /**
     * Get an object from temporary storage; return default if value
     * is null.
     *
     * @param name The object's name.
     * @param def A default value to return.
     * @return An Object with the given name.
     */
    public Object getTemp(String name, Object def)
    {
        Object val;

        try
        {
            val = tempStorage.get(name);
            if (val == null)
            {
                val = def;
            }
        }
        catch (Exception e)
        {
            val = def;
        }
        return val;
    }

    /**
     * Returns the first name for this user.  If this is defined, then
     * the user is considered logged in.
     *
     * @return A String with the user's first name.
     */
    public String getFirstName()
    {
        String tmp = null;

        tmp = (String) getPerm(LDAPUserManagerImpl.LDAP_USER_FIRSTNAME_KEY);
        if (tmp != null && tmp.length() == 0)
        {
            tmp = null;
        }
        return tmp;
    }

    /**
     * Returns the last name for this user.  If this is defined, then
     * the user is considered logged in.
     *
     * @return A String with the user's last name.
     */
    public String getLastName()
    {
        String tmp = null;

        tmp = (String) getPerm(LDAPUserManagerImpl.LDAP_USER_LASTNAME_KEY);
        if (tmp != null && tmp.length() == 0)
        {
            tmp = null;
        }
        return tmp;
    }

    /**
     * Remove an object from temporary storage and return the object.
     *
     * @param name The name of the object to remove.
     * @return An Object.
     */
    public Object removeTemp(String name)
    {
        return tempStorage.remove(name);
    }

    /**
     * Set the users Email
     *
     * @param email The new email.
     */
    public void setEmail(String email)
    {
        setPerm(LDAPUserManagerImpl.LDAP_USER_EMAIL_KEY, email);
    }

    /**
     * Set the users First Name
     *
     * @param fname The new firstname.
     */
    public void setFirstName(String fname)
    {
        setPerm(LDAPUserManagerImpl.LDAP_USER_FIRSTNAME_KEY, fname);
    }

    /**
     * Set the users Last Name
     * Sets the last name for this user.
     *
     * @param lname The new lastname.
     */
    public void setLastName(String lname)
    {
        setPerm(LDAPUserManagerImpl.LDAP_USER_LASTNAME_KEY, lname);
    }

    /**
     * Put an object into permanent storage.
     *
     * @param name The object's name.
     * @param value The object.
     */
    public void setPerm(String name, Object value)
    {
        permStorage.put(name, value);
    }

    /**
     * This should only be used in the case where we want to save the
     * data to the database.
     *
     * @param stuff A Hashtable.
     */
    public void setPermStorage(Hashtable stuff)
    {
        this.permStorage = stuff;
    }

    /**
     * This should only be used in the case where we want to save the
     * data to the database.
     *
     * @return A Hashtable.
     */
    public Hashtable getTempStorage()
    {
        if (this.tempStorage == null)
        {
            this.tempStorage = new Hashtable();
        }
        return this.tempStorage;
    }

    /**
     * This should only be used in the case where we want to save the
     * data to the database.
     *
     * @param storage A Hashtable.
     */
    public void setTempStorage(Hashtable storage)
    {
        this.tempStorage = storage;
    }

    /**
     * Put an object into temporary storage.
     *
     * @param name The object's name.
     * @param value The object.
     */
    public void setTemp(String name, Object value)
    {
        tempStorage.put(name, value);
    }
}
