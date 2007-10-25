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

import org.apache.fulcrum.security.entity.SecurityEntity;
import org.apache.fulcrum.security.entity.impl.SecurityEntityImpl;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicUser;
import org.apache.fulcrum.security.model.dynamic.entity.impl.DynamicUserImpl;
/**
 * Test that we can use a BaseAdapter with a SecurityEntity that has
 * various types of Id objects.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class BaseAdapterTest extends TestCase
{
    /**
     * Constructor for GroupAdapterTest.
     * @param arg0
     */
    public BaseAdapterTest(String arg0)
    {
        super(arg0);
    }
    public void testWithInteger()
    {
        SecurityEntity entity = new SecurityEntityImpl();
        entity.setId(new Integer(56));
        BaseAdapter ba = new BaseAdapter(entity);
        assertEquals(56, ba.getId());
        assertEquals(new Integer(56), ba.getIdAsObj());

    }
    public void testWithLong()
    {
        SecurityEntity entity = new SecurityEntityImpl();
        entity.setId(new Long(56));
        BaseAdapter ba = new BaseAdapter(entity);
        assertEquals(56, ba.getId());
        assertEquals(new Integer(56), ba.getIdAsObj());

    }
    public void testWithString()
    {
        SecurityEntity entity = new SecurityEntityImpl();
        entity.setId("56");
        BaseAdapter ba = new BaseAdapter(entity);
        assertEquals(56, ba.getId());
        assertEquals(new Integer(56), ba.getIdAsObj());

    }

    public void testGettingNameForNullentity()
    {
        BaseAdapter ba = new BaseAdapter();
        assertEquals("", ba.getName());
    }

    public void testGetSecurityEntity()
    {
        DynamicUser user = new DynamicUserImpl();
        user.setName("bob");
        BaseAdapter ba = new BaseAdapter(user);
        assertTrue(ba.getSecurityEntity() instanceof DynamicUser);
        DynamicUser retUser = (DynamicUser)ba.getSecurityEntity();
        assertEquals(user.getName(),retUser.getName());
		assertSame(user,retUser);
    }
}
