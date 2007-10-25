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
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.cfg.Configuration;

import org.apache.avalon.framework.component.Component;
import org.apache.fulcrum.security.entity.SecurityEntity;
import org.apache.fulcrum.security.spi.AbstractManager;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.UnknownEntityException;
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
    implements PersistenceHelper, Component
{
    private SessionFactory sessionFactory;
    private Session session;
    protected Transaction transaction;

    /**
     * Deletes an entity object
     *
     * @param role The object to be removed
     * @throws DataBackendException if there was an error accessing the data backend.
     * @throws UnknownEntityException if the object does not exist.
     */
    public void removeEntity(SecurityEntity entity) throws DataBackendException
    {
        try
        {
            session = retrieveSession();
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
        try
        {

            session = retrieveSession();

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
        try
        {
            session = retrieveSession();
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
            }
            throw new DataBackendException("addEntity(s,name)", he);
        }
        return;
    }

    /**
     * Allow an external session to be passed in.
     * @param session
     */
    public void setSession(Session session){
        this.session = session;
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
        if (sessionFactory == null)
        {
            sessionFactory = new Configuration().buildSessionFactory();

        }
        return sessionFactory;
    }


}
