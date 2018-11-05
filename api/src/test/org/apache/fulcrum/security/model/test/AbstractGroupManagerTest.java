package org.apache.fulcrum.security.model.test;

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

import org.apache.fulcrum.security.GroupManager;
import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.model.dynamic.entity.DynamicGroup;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.GroupSet;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.apache.fulcrum.testcontainer.BaseUnit4Test;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Eric Pugh
 */
public abstract class AbstractGroupManagerTest extends BaseUnit4Test
{
    protected Group group;
    protected GroupManager groupManager;
    protected SecurityService securityService;


    /*
     * Class to test for Group getGroupInstance()
     */
    @Test
    public void testGetGroupInstance() throws Exception
    {
        group = groupManager.getGroupInstance();
        assertNotNull(group);
        assertTrue(group.getName() == null);
    }

    /*
     * Class to test for Group getGroupInstance(String)
     */
    @Test
    public void testGetGroupInstanceString() throws Exception
    {
        group = groupManager.getGroupInstance("DOG_CATCHER");
        assertEquals("DOG_CATCHER".toLowerCase(), group.getName());
    }
    @Test
    public void testGetGroup() throws Exception
    {
        group = groupManager.getGroupInstance("DOG_CATCHER2");
        groupManager.addGroup(group);
        Group group2 = groupManager.getGroupByName("DOG_CATCHER2");
        assertEquals(group.getName(), group2.getName());
    }
    @Test
    public void testGetGroupByName() throws Exception
    {
        group = groupManager.getGroupInstance("CLEAN_KENNEL");
        groupManager.addGroup(group);
        Group group2 = groupManager.getGroupByName("CLEAN_KENNEL");
        assertEquals(group.getName(), group2.getName());
        group2 = groupManager.getGroupByName("Clean_KeNNel");
        assertEquals(group.getName(), group2.getName());
    }
    @Test
    public void testGetGroupById() throws Exception
    {
        group = groupManager.getGroupInstance("CLEAN_KENNEL_A");
        groupManager.addGroup(group);
        Group group2 = groupManager.getGroupById(group.getId());
        assertEquals(group.getName(), group2.getName());
    }
    @Test
    public void testGetAllGroups() throws Exception
    {
        int size = groupManager.getAllGroups().size();
        group = groupManager.getGroupInstance("CLEAN_KENNEL_J");
        groupManager.addGroup(group);
        GroupSet groupSet = groupManager.getAllGroups();
        assertEquals(size + 1, groupSet.size());
    }
    @Test
    public void testRemoveGroup() throws Exception
    {
        group = groupManager.getGroupInstance("CLEAN_KENNEL_K");
        groupManager.addGroup(group);
        int size = groupManager.getAllGroups().size();
        if (group instanceof DynamicGroup)
        {
            assertEquals(0, ((DynamicGroup) group).getUsers().size());
            assertEquals(0, ((DynamicGroup) group).getRoles().size());
        }
        groupManager.removeGroup(group);
        try
        {
            Group group2 = groupManager.getGroupById(group.getId());
            fail("Should have thrown UEE");
        }
        catch (UnknownEntityException uee)
        {
            // good
        }
        assertEquals(size - 1, groupManager.getAllGroups().size());
    }
    @Test
    public void testRenameGroup() throws Exception
    {
        group = groupManager.getGroupInstance("CLEAN_KENNEL_X");
        groupManager.addGroup(group);
        int size = groupManager.getAllGroups().size();
        groupManager.renameGroup(group, "CLEAN_GROOMING_ROOM");
        Group group2 = groupManager.getGroupById(group.getId());
        assertEquals("CLEAN_GROOMING_ROOM".toLowerCase(), group2.getName());
        assertEquals(size, groupManager.getAllGroups().size());
    }
    @Test
    public void testCheckExists() throws Exception
    {
        group = groupManager.getGroupInstance("GREET_PEOPLE");
        groupManager.addGroup(group);
        assertTrue(groupManager.checkExists(group));
        Group group2 = groupManager.getGroupInstance("WALK_DOGS");
        assertFalse(groupManager.checkExists(group2));
    }
    @Test
    public void testCheckExistsWithString() throws Exception
    {
        group = groupManager.getGroupInstance("GREET_PEOPLE2");
        groupManager.addGroup(group);
        assertTrue(groupManager.checkExists(group.getName()));
        Group group2 = groupManager.getGroupInstance("WALK_DOGS2");
        assertFalse(groupManager.checkExists(group2.getName()));
    }

    /*
     * Class to test for boolean checkExists(string)
     */
    @Test
    public void testAddGroupTwiceFails() throws Exception
    {
        group = groupManager.getGroupInstance("EATLUNCH");
        groupManager.addGroup(group);
        assertTrue(groupManager.checkExists(group.getName()));
        Group group2 = groupManager.getGroupInstance("EATLUNCH");
        try
        {
            groupManager.addGroup(group2);
        }
        catch (EntityExistsException uee)
        {
            // good
        }
    }
    @Test
    public void testAddGroup() throws Exception
    {
        group = groupManager.getGroupInstance("CLEAN_RABBIT_HUTCHES");
        assertNull(group.getId());
        groupManager.addGroup(group);
        assertNotNull(group.getId());
        assertNotNull(groupManager.getGroupById(group.getId()));
    }

}
