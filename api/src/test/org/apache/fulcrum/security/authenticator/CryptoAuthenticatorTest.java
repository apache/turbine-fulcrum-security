package org.apache.fulcrum.security.authenticator;
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

import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.dynamic.entity.impl.DynamicUserImpl;
import org.apache.fulcrum.testcontainer.BaseUnitTest;

/**
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class CryptoAuthenticatorTest extends BaseUnitTest
{
    private static final String preDefinedInput = "Oeltanks";
    private static final String preDefinedResult = "uVDiJHaavRYX8oWt5ctkaa7j1cw=";
    /**
    	* Constructor for CryptoAuthenticatorTest.
    	* @param arg0
    	*/
    public CryptoAuthenticatorTest(String arg0)
    {
        super(arg0);
    }
    public void setUp()
    {
        try
        {
            this.setRoleFileName("src/test/CryptoRoleConfig.xml");
            this.setConfigurationFileName("src/test/CryptoComponentConfig.xml");
        }
        catch (Exception e)
        {
            fail(e.toString());
        }
    }
    public void testAuthenticate() throws Exception
    {
        User user = new DynamicUserImpl();
        user.setName("Bob");
        user.setPassword(preDefinedResult);
        Authenticator authenticator = (Authenticator)lookup(Authenticator.ROLE);
        assertTrue(authenticator.authenticate(user, preDefinedInput));
        assertFalse(authenticator.authenticate(user, "mypassword"));
    }
}
