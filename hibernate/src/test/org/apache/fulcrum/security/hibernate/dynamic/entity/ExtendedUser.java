package org.apache.fulcrum.security.hibernate.dynamic.entity;

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

import org.apache.fulcrum.security.model.dynamic.entity.impl.DynamicUserImpl;

/**
 * User to test subclassing an existing class and then persisting with
 * Hibernate.
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class ExtendedUser extends DynamicUserImpl
{
    /** Serial version */
    private static final long serialVersionUID = 2806989534919229034L;

    private String NTDomain;
    private String NTName;
    private String Email;

    /**
     * @return Returns the email.
     */
    public String getEmail()
    {
        return Email;
    }

    /**
     * @param email
     *            The email to set.
     */
    public void setEmail(String email)
    {
        Email = email;
    }

    /**
     * @return Returns the nTDomain.
     */
    public String getNTDomain()
    {
        return NTDomain;
    }

    /**
     * @param domain
     *            The nTDomain to set.
     */
    public void setNTDomain(String domain)
    {
        NTDomain = domain;
    }

    /**
     * @return Returns the nTName.
     */
    public String getNTName()
    {
        return NTName;
    }

    /**
     * @param name
     *            The nTName to set.
     */
    public void setNTName(String name)
    {
        NTName = name;
    }
}
