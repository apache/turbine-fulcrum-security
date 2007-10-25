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

import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.fulcrum.security.entity.SecurityEntity;
import org.apache.fulcrum.security.entity.impl.SecurityEntityImpl;
import org.apache.turbine.util.security.TurbineSecurityException;
/**
 * Base class for all adapters.  Provides methods shared by all
 * SecurityEntity objects.  The IntegerConverter handles converting
 * id's from various implementations to the int/Integer expected by
 * Turbine.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class BaseAdapter
{
    SecurityEntity entity = null;
    private Converter converter = new IntegerConverter();
    public BaseAdapter()
    {
        super();
        entity = new SecurityEntityImpl();
        entity.setName("");
    }
    public BaseAdapter(org.apache.fulcrum.security.entity.SecurityEntity entity)
    {
        super();
        this.entity = entity;
    }
    public int getId()
    {
        return getIdAsObj().intValue();
    }
    /*
     * @see org.apache.turbine.om.security.SecurityEntity#getIdAsObj()
     */
    public Integer getIdAsObj()
    {
        return (Integer) converter.convert(Integer.class, entity.getId());
    }
    public String getName()
    {
        return entity.getName();
    }
	public void setName(String name)
	 {
		throw new RuntimeException("Unsupported operation");
	 }
    /* Not implemented.
     * @see org.apache.turbine.om.security.SecurityEntity#setId(int)
     */
    public void setId(int arg0)
    {
        throw new RuntimeException("Unsupported operation");
    }
    /* Not implemented.
	 * @see org.apache.turbine.om.security.Group#save()
	 */
    public void save() throws TurbineSecurityException
    {
        throw new RuntimeException("Unsupported operation");
    }
    /* Not implemented.
     * @see org.apache.turbine.om.security.Group#remove()
     */
    public void remove() throws TurbineSecurityException
    {
        throw new RuntimeException("Unsupported operation");
    }
    /* Not implemented.
	 * @see org.apache.turbine.om.security.Group#rename(java.lang.String)
	 */
    public void rename(String arg0) throws TurbineSecurityException
    {
        throw new RuntimeException("Unsupported operation");
    }

    public SecurityEntity getSecurityEntity(){
        return entity;
    }
}
