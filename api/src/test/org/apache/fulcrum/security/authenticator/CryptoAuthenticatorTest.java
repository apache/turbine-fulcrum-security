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

import static org.junit.Assert.*;

import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.dynamic.entity.impl.DynamicUserImpl;
import org.apache.fulcrum.testcontainer.BaseUnit5Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class CryptoAuthenticatorTest extends BaseUnit5Test

{
    private static final String preDefinedInput = "Oeltanks";
    private static final String preDefinedResult = "XBSqev4ilv7P7852G2rL5WgX3FLy8VzfOY+tVq+xjek=";


    @BeforeEach
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

    @Test
    public void testAuthenticate() throws Exception
    {
        User user = new DynamicUserImpl();
        user.setName("Bob");
        user.setPassword(preDefinedResult);
        Authenticator authenticator = (Authenticator) lookup(Authenticator.ROLE);
        assertTrue(authenticator.authenticate(user, preDefinedInput));
        assertFalse(authenticator.authenticate(user, "mypassword"));
    }
    
}
