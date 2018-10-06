package org.apache.fulcrum.security.torque.basic;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.basic.entity.BasicUser;
import org.apache.fulcrum.security.model.basic.test.AbstractModelManagerTest;
import org.apache.fulcrum.security.torque.HsqlDB;
import org.apache.fulcrum.security.torque.om.TorqueBasicGroupPeer;
import org.apache.fulcrum.security.torque.om.TorqueBasicUserGroupPeer;
import org.apache.fulcrum.security.torque.om.TorqueBasicUserPeer;
import org.apache.torque.TorqueException;
import org.apache.torque.criteria.Criteria;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @author <a href="jh@byteaction.de">J&#252;rgen Hoffmann</a>
 * @version $Id:$
 */
public class TorqueBasicModelManagerTest extends AbstractModelManagerTest
{
    protected static HsqlDB hsqlDB = null;

    @Override
	@Before
    public void setUp() throws Exception
    {
        if(hsqlDB == null)
        {
            hsqlDB = new HsqlDB("src/test/fulcrum-basic-schema.sql");
            hsqlDB.addSQL("src/test/id-table-schema.sql");
            hsqlDB.addSQL("src/test/fulcrum-basic-schema-idtable-init.sql");
        }
        this.setRoleFileName("src/test/BasicTorqueRoleConfig.xml");
        this.setConfigurationFileName("src/test/BasicTorqueComponentConfig.xml");

        securityService = (SecurityService) lookup(SecurityService.ROLE);
        super.setUp();
    }

    @Override
    @Test
	public void testRevokeAllUser() throws Exception
    {
        super.testRevokeAllUser();
        User user = userManager.getUserInstance("Clint2");
        assertEquals(0, ((BasicUser) user).getGroups().size());
    }

    @Override
    @After
	public void tearDown()
    {
        // cleanup tables
        try
        {
            Criteria criteria = new Criteria();
            criteria.where(TorqueBasicUserGroupPeer.GROUP_ID, 0, Criteria.GREATER_THAN);
            criteria.where(TorqueBasicUserGroupPeer.USER_ID, 0, Criteria.GREATER_THAN);
            TorqueBasicUserGroupPeer.doDelete(criteria);

            criteria = new Criteria();
            criteria.where(TorqueBasicUserPeer.USER_ID, 0, Criteria.GREATER_THAN);
            TorqueBasicUserPeer.doDelete(criteria);

            criteria = new Criteria();
            criteria.where(TorqueBasicGroupPeer.GROUP_ID, 0, Criteria.GREATER_THAN);
            TorqueBasicGroupPeer.doDelete(criteria);
        }
        catch (TorqueException e)
        {
            e.printStackTrace();
            fail(e.toString());
        }

        securityService = null;
    }


}
