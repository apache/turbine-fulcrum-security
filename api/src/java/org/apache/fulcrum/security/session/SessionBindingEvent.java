package org.apache.fulcrum.security.session;


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
 * This interface is modelled after
 * jakarta.servlet.http.HttpSessionBindingListener.
 * 
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @version $Id$
 */
public interface SessionBindingEvent
{
    /**
     * Returns the name with which the object is bound to or unbound from the
     * session.
     * 
     * @return The name used for binding.
     */
    String getName();

    /**
     * Returns the session to or from which the object is bound or unbound.
     * 
     * @return A session object.
     */
    Session getSession();
}
