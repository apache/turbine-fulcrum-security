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

/**
 * This interface represents the extended functionality of a user.
 *
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id: User.java 1374616 2012-08-18 17:26:07Z tv $
 */
public interface ExtendedUser extends User
{
    /**
     * Returns the first name for this user.
     *
     * @return A String with the user's first name.
     */

    String getFirstName();

    /**
     * Returns the last name for this user.
     *
     * @return A String with the user's last name.
     */
    String getLastName();

    /**
     * Returns the email address for this user.
     *
     * @return A String with the user's email address.
     */
    String getEmail();

    /**
     * Sets the first name for this user.
     *
     * @param firstName User's first name.
     */
    void setFirstName(String firstName);

    /**
     * Sets the last name for this user.
     *
     * @param lastName User's last name.
     */
    void setLastName(String lastName);

    /**
     * Sets the email address.
     *
     * @param address The email address.
     */
    void setEmail(String address);

    /**
     * Returns the value of the objectdata for this user.
     * Objectdata is a storage area used
     * to store the permanent storage table from the User
     * object.
     *
     * @return The bytes in the objectdata for this user
     */
    byte[] getObjectdata();

    /**
     * Sets the value of the objectdata for the user
     *
     * @param objectdata The new permanent storage for the user
     */
    void setObjectdata(byte[] objectdata);
}
