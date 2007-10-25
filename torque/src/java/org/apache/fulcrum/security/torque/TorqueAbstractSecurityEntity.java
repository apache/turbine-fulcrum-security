package org.apache.fulcrum.security.torque;
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
import java.sql.Connection;

import org.apache.fulcrum.security.entity.SecurityEntity;
import org.apache.torque.TorqueException;
import org.apache.torque.om.BaseObject;
/**
 * This abstract class provides the SecurityInterface to the managers.
 *
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * @version $Id:$
 */
public abstract class TorqueAbstractSecurityEntity extends BaseObject
    implements SecurityEntity
{
    /**
     * Get a numeric entity id
     *
     * @return numeric id of this entity
     */
    public abstract Integer getEntityId();

    /**
     * Set a numeric entity id
     *
     * @param id numeric id of this entity
     */
    public abstract void setEntityId(Integer id) throws TorqueException;

    /**
     * Get the name of the entity
     *
     * @return name of this entity
     */
    public abstract String getEntityName();

    /**
     * Set the name of the entity
     *
     * @param name the name of the entity
     */
    public abstract void setEntityName(String name);

    /**
     * Retrieve attached objects such as users, permissions, ...
     *
     * @param con A database connection
     */
    public abstract void retrieveAttachedObjects(Connection con) throws TorqueException;

    /**
     * Update this instance to the database with all dependend objects
     *
     * @param con A database connection
     */
    public abstract void update(Connection con) throws TorqueException;

    /**
     * Get the name of the connection pool associated to this object
     *
     * @return the logical Torque database name
     */
    public abstract String getDatabaseName();

    /**
     * Delete this entity
     *
     * @throws TorqueException if any database operation fails
     */
    public abstract void delete() throws TorqueException;

    /**
     * @see org.apache.fulcrum.security.entity.SecurityEntity#getId()
     */
    public Object getId()
    {
        return getEntityId();
    }

    /**
     * @see org.apache.fulcrum.security.entity.SecurityEntity#setId(java.lang.Object)
     */
    public void setId(Object id)
    {
        try
        {
            setEntityId((Integer)id);
        }
        catch (TorqueException e)
        {
            // should not happen
        }
    }

    /**
     * @see org.apache.fulcrum.security.entity.SecurityEntity#getName()
     */
    public String getName()
    {
        return getEntityName();
    }

    /**
     * @see org.apache.fulcrum.security.entity.SecurityEntity#setName(java.lang.String)
     */
    public void setName(String name)
    {
        if (name != null)
        {
            setEntityName(name.toLowerCase());
        }
    }
}
