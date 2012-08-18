package org.apache.fulcrum.security.util;

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

import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.model.dynamic.entity.impl.DynamicGroupImpl;

/**
 * Test using a SecuritySet. Useing various subclasses since it is Abstract.
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class SecuritySetTest extends TestCase
{

    /**
     * Defines the testcase name for JUnit.
     * 
     * @param name
     *            the testcase's name.
     */
    public SecuritySetTest(String name)
    {
        super(name);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(SecuritySetTest.class);
    }

    public void testNull() throws Exception
    {
        SecuritySet<Group> securitySet = new GroupSet();
        assertFalse(securitySet.contains(null));
    }

    public void testContainsName()
    {
        SecuritySet<Group> securitySet = new GroupSet();
        assertFalse(securitySet.containsName(null));
        Group g = new DynamicGroupImpl();
        g.setName("BOB");

        ((GroupSet) securitySet).add(g);
        assertTrue(((GroupSet) securitySet).containsName("bob"));
        assertTrue(((GroupSet) securitySet).containsName("BOB"));

    }

    public void testRemoveAll()
    {
        SecuritySet<Group> securitySet = new GroupSet();
        assertFalse(securitySet.containsName(null));
        Group g = new DynamicGroupImpl();
        g.setName("BOB");
        g.setId("BOB");

        ((GroupSet) securitySet).add(g);

        SecuritySet<Group> securitySet2 = new GroupSet();
        assertFalse(securitySet.containsName(null));
        g = new DynamicGroupImpl();
        g.setName("BOB");
        g.setId("BOB");

        ((GroupSet) securitySet2).add(g);
        securitySet.removeAll(securitySet2);
        assertEquals(0, securitySet.size());
    }

    public void testToArray() throws Exception
    {
        SecuritySet<Group> securitySet = getTestData();
        Object array[] = securitySet.toArray();
        assertEquals(2, array.length);
        Object array2[] = new Object[2];
        array2[0] = "hi";
        Object array3[] = securitySet.toArray(array2);
        assertEquals(2, array3.length);
    }

    public void testAdd() throws Exception
    {
        GroupSet securitySet = (GroupSet) getTestData();
        Group g = new DynamicGroupImpl();
        g.setName("Michael");
        g.setId("Michael");
        assertTrue(securitySet.add(g));
    }

    private SecuritySet<Group> getTestData()
    {
        SecuritySet<Group> securitySet = new GroupSet();
        assertFalse(securitySet.containsName(null));
        Group g = new DynamicGroupImpl();
        g.setName("JOE");
        g.setId("JOE");

        Group g2 = new DynamicGroupImpl();
        g2.setName("RICK");
        g2.setId("RICK");

        ((GroupSet) securitySet).add(g);
        ((GroupSet) securitySet).add(g2);

        return securitySet;
    }

}
