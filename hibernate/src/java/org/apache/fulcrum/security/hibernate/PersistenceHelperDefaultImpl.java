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
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.fulcrum.security.entity.SecurityEntity;
import org.apache.fulcrum.security.spi.AbstractManager;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
/**
 *
 * This persistenceHelper expects you to either pass in a SessionFactory to use,
 * or it will create one from a hibernate.cfg.xml in the root of the classpath.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class PersistenceHelperDefaultImpl
    extends AbstractManager
    implements PersistenceHelper, Configurable, Initializable, Disposable
{
    private Configuration configuration;
    private SessionFactory sessionFactory;
    private Session session;

    /**
     * Deletes an entity object
     *
     * @param role The object to be removed
     * @throws DataBackendException if there was an error accessing the data backend.
     * @throws UnknownEntityException if the object does not exist.
     */
    public void removeEntity(SecurityEntity entity) throws DataBackendException
    {
        Transaction transaction = null;

        try
        {
            Session session = retrieveSession();
            transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();
        }
        catch (HibernateException he)
        {
            try
            {
                transaction.rollback();
            }
            catch (HibernateException hex)
            {
                // ignore
            }
            throw new DataBackendException(
                "Problem removing entity:" + he.getMessage(),
                he);
        }
    }
    /**
     * Stores changes made to an object
     *
     * @param role The object to be saved
     * @throws DataBackendException if there was an error accessing the data backend.
     * @throws UnknownEntityException if the role does not exist.
     */
    public void updateEntity(SecurityEntity entity) throws DataBackendException
    {
        Transaction transaction = null;

        try
        {
            Session session = retrieveSession();
            transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
        }
        catch (HibernateException he)
        {
            try
            {
                if (transaction != null)
                {
                    transaction.rollback();
                }
                if (he
                    .getMessage()
                    .indexOf("Another object was associated with this id")
                    > -1)
                {
                    session.close();
                    updateEntity(entity);
                }
                else
                {
                    throw new DataBackendException(
                        "updateEntity(" + entity + ")",
                        he);
                }
            }
            catch (HibernateException hex)
            {
                // ignore
            }

        }
        return;
    }
    /**
     * adds an entity
     *
     * @param role The object to be saved
     * @throws DataBackendException if there was an error accessing the data backend.
     * @throws UnknownEntityException if the role does not exist.
     */
    public void addEntity(SecurityEntity entity) throws DataBackendException
    {
        Transaction transaction = null;

        try
        {
            Session session = retrieveSession();
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
        }
        catch (HibernateException he)
        {
            try
            {
                transaction.rollback();
            }
            catch (HibernateException hex)
            {
                // ignore
            }
            throw new DataBackendException("addEntity(s,name)", he);
        }
        return;
    }

    /**
     * Returns a hibernate session, or if is null, opens one.
     * @return An Open hibernate session.
     * @throws HibernateException
     */
    public Session retrieveSession() throws HibernateException
    {
        if (session == null )
        {
            session = getSessionFactory().openSession();
        }
        return session;
    }

    /**
     * Return the hibernate configuration
     */
    public Configuration getConfiguration()
    {
        return configuration;
    }

    /**
     * In some environments we will load the session factory up
     * and pass it in.
     *
     * @param hibernateService The hibernateService to set.
     */
    public void setSessionFactory(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Lazy loads the sessionFactory if it hasn't been requested yet.
     * @return the hibernate service
     */
    public SessionFactory getSessionFactory() throws HibernateException
    {
        return sessionFactory;
    }

    /** Avalon lifecycle method
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    public void initialize() throws Exception
    {
        sessionFactory = configuration.buildSessionFactory();
    }

    /** Avalon lifecycle method
     * @see org.apache.fulcrum.security.spi.AbstractManager#dispose()
     */
    public void dispose()
    {
        sessionFactory.close();
        super.dispose();
    }

    /** Avalon lifecycle method
     * @see org.apache.avalon.framework.configuration.Configurable#configure(org.apache.avalon.framework.configuration.Configuration)
     */
    public void configure(org.apache.avalon.framework.configuration.Configuration conf) throws ConfigurationException
    {
        configuration = new Configuration();

        // read properties
        org.apache.avalon.framework.configuration.Configuration[] props =
            conf.getChild("session-factory").getChildren("property");

        for (int i = 0; i < props.length; i++)
        {
            String value = props[i].getValue(null);
            String key = props[i].getAttribute("name", null);

            if (key != null && value != null)
            {
                configuration.setProperty(key, value);
            }
            else
            {
                throw new ConfigurationException("Invalid configuration", props[i]);
            }
        }

        // read mappings
        org.apache.avalon.framework.configuration.Configuration[] maps =
            conf.getChild("mappings").getChildren("resource");

        for (int i = 0; i < maps.length; i++)
        {
            String value = maps[i].getValue(null);

            if (value != null)
            {
                configuration.addResource(value);
            }
            else
            {
                throw new ConfigurationException("Invalid configuration", maps[i]);
            }
        }
    }
}
