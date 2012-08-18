package org.apache.fulcrum.security.entity.impl;

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

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.fulcrum.security.entity.SecurityEntity;

/**
 * Base class for all objects implementing SecurityEnitity. This class
 * automatically lowercases the name. So the permission "EDIT" will equal "eDit"
 * and "edit";
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class SecurityEntityImpl implements SecurityEntity
{
    private String name;

    private Object id;

    /**
     * @return
     */
    public Object getId()
    {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Object id)
    {
        this.id = id;
    }

    /**
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * Pass in the name for this entity. Also lowercases it.
     * 
     * @param name
     */
    public void setName(String name)
    {
        if (name == null)
        {
            throw new IllegalArgumentException("Must provide a valid name for all SecurityEntities.");
        }

        this.name = name.toLowerCase();
    }

    @Override
    public String toString()
    {
        return getClass().getName() + " (id:" + getId() + " name:" + getName() + ")";
    }

    /**
     * Check if this object is equal to another
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o)
    {
        boolean equals = true;
        Object id = getId();

        if (o == null || id == null)
        {
            equals = false;
        }
        else if (!(o instanceof SecurityEntity))
        {
            equals = false;
        }
        else
        {
            equals = id.equals(((SecurityEntity) o).getId());
        }
        return equals;
    }

    /**
     * Calculate a hash code for this object
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(47, 11).append(getId()).append(getName()).toHashCode();
    }
}
