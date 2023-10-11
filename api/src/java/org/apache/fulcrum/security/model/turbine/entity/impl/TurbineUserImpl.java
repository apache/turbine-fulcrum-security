package org.apache.fulcrum.security.model.turbine.entity.impl;

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

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.fulcrum.security.model.turbine.entity.TurbineUser;

/**
 * Represents the "turbine" model where permissions are in a many to many
 * relationship to roles, roles are related to groups are related to users, all
 * in many to many relationships.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id: TurbineUser.java 437451 2006-08-27 20:20:44Z tv $
 */
public class TurbineUserImpl extends AbstractTurbineSecurityEntityImpl implements TurbineUser
{
    /**
     * Serial number
     */
    private static final long serialVersionUID = -7309619325167081811L;

    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private transient byte[] objectData;
    
    
    public TurbineUserImpl()
    {
      
    }

    /**
     * Get the password
     *
     * @return the password
     */
    @Override
	public String getPassword()
    {
        return password;
    }

    /**
     * Set the password
     *
     * @param password the new password
     */
    @Override
	public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Returns the first name of the User
     *
     * @return The first name of the User
     */
    @Override
	public String getFirstName()
    {
        return this.firstName;
    }

    /**
     * Sets the first name of the User
     *
     * @param firstName The new first name of the User
     */
    @Override
	public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the User
     *
     * @return The last name of the User
     */
    @Override
	public String getLastName()
    {
        return this.lastName;
    }

    /**
     * Sets the last name of User
     *
     * @param lastName The new last name of the User
     */
    @Override
	public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * Returns the email address of the user
     *
     * @return The email address of the user
     */
    @Override
	public String getEmail()
    {
        return this.email;
    }

    /**
     * Sets the new email address of the user
     *
     * @param email The new email address of the user
     */
    @Override
	public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * Returns the value of the objectdata for this user.
     * Objectdata is a storage area used
     * to store the permanent storage table from the User
     * object.
     *
     * @return The bytes in the objectdata for this user
     */
    @Override
	public byte[] getObjectdata()
    {
        return this.objectData;
    }

    /**
     * Sets the value of the objectdata for the user
     *
     * @param objectdata The new permanent storage for the user
     */
    @Override
	public void setObjectdata(byte[] objectdata)
    {
        this.objectData = objectdata;
    }

    /**
     * Calculate a hash code for this object
     *
     * @see org.apache.fulcrum.security.entity.impl.SecurityEntityImpl#hashCode()
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(41, 15)
            .append(getPassword())
            .append(getFirstName())
            .append(getLastName())
            .append(getEmail())
            .appendSuper(super.hashCode()).toHashCode();
    }
}
