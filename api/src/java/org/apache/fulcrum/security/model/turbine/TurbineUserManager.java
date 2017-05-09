package org.apache.fulcrum.security.model.turbine;

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
import org.apache.fulcrum.security.UserManager;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.turbine.entity.TurbineUser;
import org.apache.fulcrum.security.util.UnknownEntityException;

/**
 * This interface describes the methods that glue the "turbine" model together.
 * 
 * This Fulcrum user manager is as used as a delegate in the default Turbine user manager.
 * 
 * The user &lt;T extends {@link User}&gt; is wrapped using an extended user model, 
 * which includes at least {@link TurbineUser} interface.
 *  
 * This interface's methods are wrapped in Turbine user manager (org.apache.turbine.services.security.UserManager) either in a method with the same name (and very similar signature) 
 * or mapped to method names as listed below:
 * 
 * <ul>
 * <li>Turbine (framework) user manager method(s) -> method(s) in this (Fulcrum )manager 
 * <li>createAccount -> {@link #addUser(User, String)}
 * <li>removeAccount -> {@link #removeUser(User)}
 * <li>store -> {@link #saveUser(User)}
 * <li>retrieve (2x)-> {@link #getUser(String)}, {@link #getUser(String, String)}
 * <li>retrieveList -> {@link #getAllUsers()}
 * <li>accountExists (2x)-> {@link #checkExists(String)}, {@link #checkExists(User)}
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public interface TurbineUserManager extends UserManager
{
    /**
     * Constructs an User object to represent an anonymous user of the
     * application.
     *
     * @return An anonymous Turbine User. 
     * @throws UnknownEntityException
     *             if the anonymous User object couldn't be constructed.
     */
    <T extends User> T getAnonymousUser() throws UnknownEntityException;

    /**
     * Checks whether a passed user object matches the anonymous user pattern
     * according to the configured user manager
     *
     * @param An
     *            user object
     *
     * @return True if this is an anonymous user
     *
     */
    boolean isAnonymousUser(User u);
}
