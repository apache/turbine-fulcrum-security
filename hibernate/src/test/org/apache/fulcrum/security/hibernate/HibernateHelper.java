package org.apache.fulcrum.security.hibernate;
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

import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.tool.hbm2ddl.SchemaExport;
/**
 * @author Eric Pugh
 *
 * This class allows us to dynamically populate the hsql database with our schema.
 */
public class HibernateHelper
{
    private static SessionFactory sessions;
    /**
     * @return
     */
    public static SessionFactory getSessions()
    {
        return sessions;
    }

    public static void exportSchema(Configuration cfg) throws Exception
    {

        new SchemaExport(cfg).create(true, true);
        sessions = cfg.buildSessionFactory();
    }
}
