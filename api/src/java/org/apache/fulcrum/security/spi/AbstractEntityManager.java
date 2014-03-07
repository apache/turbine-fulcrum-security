package org.apache.fulcrum.security.spi;

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
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

/**
 * 
 * This abstract implementation provides most of the functionality that a
 * manager will need.
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public abstract class AbstractEntityManager extends AbstractManager implements Configurable
{
    private String className;
    private static final String CLASS_NAME_KEY = "className";
    
    private Boolean customPeer = false;  //  used for torque which uses per object peer classes
    
    private String peerClassName;
    private static final String PEER_CLASS_NAME_KEY = "peerClassName";

    /**
     * Avalon Service lifecycle method
     */
    public void configure(Configuration conf) throws ConfigurationException
    {
        className = conf.getChild(CLASS_NAME_KEY).getValue();
       
        peerClassName = conf.getChild( PEER_CLASS_NAME_KEY).getValue( null );
        if (peerClassName != null) {
            setPeerClassName( peerClassName );
            setCustomPeer(true);
        } 
    }

    /**
     * @return Returns the className.
     */
    public String getClassName()
    {
        return className;
    }

    /**
     * @param className
     *            The className to set.
     */
    public void setClassName(String className)
    {
        this.className = className;
    }

    public Boolean getCustomPeer()
    {
        return customPeer;
    }

    public void setCustomPeer( Boolean customPeer )
    {
        this.customPeer = customPeer;
    }

    public String getPeerClassName()
    {
        return peerClassName;
    }

    public void setPeerClassName( String peerClassName )
    {
        this.peerClassName = peerClassName;
    }


}
