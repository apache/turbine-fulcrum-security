package org.apache.fulcrum.security.ldap;

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

import org.apache.fulcrum.security.entity.User;

/**
 * LDAPUser extends User and provides access to a user who accesses the
 * system via LDAP.
 *
 * @author <a href="mailto:cberry@gluecode.com">Craig D. Berry</a>
 * @author <a href="mailto:tadewunmi@gluecode.com">Tracy M. Adewunmi</a>
 * @author <a href="mailto:lflournoy@gluecode.com">Leonard J. Flournoy </a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @author <a href="mailto:hhernandez@itweb.com.mx">Humberto Hernandez</a>
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id: LDAPUser.java 534527 2007-05-02 16:10:59Z tv $
 */
public interface LDAPUser extends User
{
    /**
     * Returns the Email for this user.  If this is defined, then
     * the user is considered logged in.
     *
     * @return A String with the user's Email.
     */
    public String getEmail();

    /**
     * Returns the first name for this user.  If this is defined, then
     * the user is considered logged in.
     *
     * @return A String with the user's first name.
     */
    public String getFirstName();
    
    /**
     * Returns the last name for this user.  If this is defined, then
     * the user is considered logged in.
     *
     * @return A String with the user's last name.
     */
    public String getLastName();

    /**
     * Set the users Email
     *
     * @param email The new email.
     */
    public void setEmail(String email);
    
    /**
     * Set the users First Name
     *
     * @param fname The new firstname.
     */
    public void setFirstName(String fname);

    /**
     * Set the users Last Name
     * Sets the last name for this user.
     *
     * @param lname The new lastname.
     */
    public void setLastName(String lname);
}
