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

import junit.framework.TestCase;

import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.dynamic.entity.impl.DynamicUserImpl;
/**
 * Test that we can use a UserAdapter properly.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class UserAdapterTest extends TestCase
{
    /**
     * Constructor for UserAdapterTest.
     * @param arg0
     */
    public UserAdapterTest(String arg0)
    {
        super(arg0);
    }
    public void testWithInteger()
    {
        User user = new DynamicUserImpl();
        user.setId(new Integer(56));
        UserAdapter ga = new UserAdapter(user);
        assertEquals(56, ga.getId());
        assertEquals(new Integer(56), ga.getIdAsObj());

    }
    public void testWithLong()
    {
        User user = new DynamicUserImpl();
        user.setId(new Long(56));
        UserAdapter ga = new UserAdapter(user);
        assertEquals(56, ga.getId());
        assertEquals(new Integer(56), ga.getIdAsObj());

    }
    public void testWithString()
    {
        User user = new DynamicUserImpl();
        user.setId("56");
        UserAdapter ga = new UserAdapter(user);
        assertEquals(56, ga.getId());
        assertEquals(new Integer(56), ga.getIdAsObj());

    }

    public void testSetGetTemp(){
		User user = new DynamicUserImpl();
		user.setId("56");
		UserAdapter ga = new UserAdapter(user);
		Double d = new Double(10.243);
		ga.setTemp("temp",d);
		assertSame(ga.getTemp("temp"),d);
    }
}
