package org.apache.fulcrum.security.entity;

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
import java.io.Serializable;
/**
 * This interface represents the basic functionality of a user.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public interface User extends Serializable, SecurityEntity
{
    /**
    * Returns the user's password. This method should not be used by
    * the application directly, because it's meaning depends upon
    * the implementation of UserManager that manages this particular
    * user object. Some implementations will use this attribute for
    * storing a password encrypted in some way, other will not use
    * it at all, when user entered password is presented to some external
    * authority (like NT domain controller) to validate it.
    * See also {@link org.apache.fulcrum.security.UserManager#authenticate(User,String)}.
    *
    * @return A String with the password for the user.
    */
    String getPassword();

    /**
     * Set password. Application should not use this method
     * directly, see {@link #getPassword()}.
     * See also {@link org.apache.fulcrum.security.UserManager#changePassword(User,String,String)}.
     *
     * @param password The new password.
     */
    void setPassword(String password);

}
