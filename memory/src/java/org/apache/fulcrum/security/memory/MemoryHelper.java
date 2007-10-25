package org.apache.fulcrum.security.memory;
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

import java.util.Iterator;
import java.util.List;

import org.apache.fulcrum.security.entity.SecurityEntity;


/**
 *
 * This implementation keeps all objects in memory.  This is mostly meant to help
 * with testing and prototyping of ideas.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class MemoryHelper
{

    /** Our Unique ID counter */
    private static int uniqueId = 0;

    public static Integer getUniqueId()
    {
        return new Integer(++uniqueId);
    }

    public static boolean checkExists(List securityEntities, String name){
        boolean exists = false;
        for (Iterator i = securityEntities.iterator(); i.hasNext();)
        {
            SecurityEntity securityEntity = (SecurityEntity) i.next();
            if (securityEntity.getName().equalsIgnoreCase(name))
            {
                exists = true;
            }
        }
        return exists;
    }
}
