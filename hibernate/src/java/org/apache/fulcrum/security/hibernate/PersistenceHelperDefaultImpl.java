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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * 
 * This persistenceHelper expects you to either pass in a SessionFactory to use,
 * or it will create one from a hibernate.cfg.xml in the root of the classpath.
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id: PersistenceHelperDefaultImpl.java 1374014 2012-08-16 19:47:27Z
 *          tv $
 */
public class PersistenceHelperDefaultImpl extends AbstractManager implements PersistenceHelper, Configurable, Initializable, Disposable
{
    private Configuration configuration;
    private SessionFactory sessionFactory;
    private Session session;

    /**
     * Deletes an entity object
     * 
     * @param entity
     *            The object to be removed
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
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
            transaction = null;
        }
        catch (HibernateException he)
        {
            throw new DataBackendException("Problem removing entity:" + he.getMessage(), he);
        }
        finally
        {
            if (transaction != null)
            {
                transaction.rollback();
            }
        }
    }

    /**
     * Stores changes made to an object
     * 
     * @param entity
     *            The object to be saved
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
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
            transaction = null;
        }
        catch (HibernateException he)
        {
            if (he.getMessage().indexOf("Another object was associated with this id") > -1)
            {
                session.close();
                updateEntity(entity);
            }
            else
            {
                throw new DataBackendException("updateEntity(" + entity + ")", he);
            }
        }
        finally
        {
            if (transaction != null)
            {
                transaction.rollback();
            }
        }
    }

    /**
     * adds an entity
     * 
     * @param entity
     *            The object to be saved
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
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
            transaction = null;
        }
        catch (HibernateException he)
        {
            throw new DataBackendException("addEntity(entity)", he);
        }
        finally
        {
            if (transaction != null)
            {
                transaction.rollback();
            }
        }
    }

    /**
     * Returns a hibernate session, or if is null, opens one.
     * 
     * @return An Open hibernate session.
     * @throws HibernateException generic exception
     */
    public Session retrieveSession() throws HibernateException
    {
        if (session == null)
        {
            session = getSessionFactory().openSession();
        }
        return session;
    }

    /**
     * @return the hibernate configuration
     */
    public Configuration getConfiguration()
    {
        return configuration;
    }

    /**
     * In some environments we will load the session factory up and pass it in.
     * 
     * @param sessionFactory
     *            The hibernateService to set.
     */
    public void setSessionFactory(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Lazy loads the sessionFactory if it hasn't been requested yet.
     * 
     * @return the hibernate service
     */
    public SessionFactory getSessionFactory() throws HibernateException
    {
        return sessionFactory;
    }

    /* (non-Javadoc)
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    public void initialize() throws Exception
    {
        sessionFactory = configuration.buildSessionFactory();
    }

    /* (non-Javadoc)
     * @see org.apache.fulcrum.security.spi.AbstractManager#dispose()
     */
    @Override
    public void dispose()
    {
        sessionFactory.close();
        super.dispose();
    }

    /* (non-Javadoc)
     * @see org.apache.avalon.framework.configuration.Configurable#configure(org.apache.avalon.framework.configuration.Configuration)
     */
    public void configure(org.apache.avalon.framework.configuration.Configuration conf) throws ConfigurationException
    {
        configuration = new Configuration();

        // read properties
        org.apache.avalon.framework.configuration.Configuration[] props = conf.getChild("session-factory").getChildren("property");

        for (org.apache.avalon.framework.configuration.Configuration prop : props)
        {
            String value = prop.getValue(null);
            String key = prop.getAttribute("name", null);

            if (key != null && value != null)
            {
                configuration.setProperty(key, value);
            }
            else
            {
                throw new ConfigurationException("Invalid configuration", prop);
            }
        }

        // read mappings
        org.apache.avalon.framework.configuration.Configuration[] maps = conf.getChild("mappings").getChildren("resource");

        for (org.apache.avalon.framework.configuration.Configuration map : maps)
        {
            String value = map.getValue(null);

            if (value != null)
            {
                configuration.addResource(value);
            }
            else
            {
                throw new ConfigurationException("Invalid configuration", map);
            }
        }
    }
}
