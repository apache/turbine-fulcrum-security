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
import net.sf.hibernate.Transaction;
import net.sf.hibernate.avalon.HibernateService;

import org.apache.avalon.framework.component.Component;
import org.apache.fulcrum.security.entity.SecurityEntity;
import org.apache.fulcrum.security.spi.AbstractManager;
import org.apache.fulcrum.security.util.DataBackendException;
/**
 *
 * This base implementation persists to a database via Hibernate. it provides methods shared by all
 * Hibernate SPI managers.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id$
 */
public class PersistenceHelperHibernateServiceImpl
    extends AbstractManager
    implements PersistenceHelper, Component
{
    protected HibernateService hibernateService;
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
     * Returns a hibernate session that has been opened if it was null or not
     * connected or not open.
     * @return An Open hibernate session.
     * @throws HibernateException
     */
    public Session retrieveSession() throws HibernateException
    {
        if (session == null || (!session.isConnected() && !session.isOpen()))
        {
            session = getHibernateService().openSession();
        }
        return session;
    }

    /**
     * In some environments (like ECM) the service ends up getting it's own
     * copy of the HibernateService.  In those environments, we might want to
     * pass in a different HibernateService instead.
     *
     * @param hibernateService The hibernateService to set.
     */
    public void setHibernateService(HibernateService hibernateService)
    {
        this.hibernateService = hibernateService;
    }

    /**
     * Lazy loads the hibernateservice if it hasn't been requested yet.
     * @return the hibernate service
     */
    public HibernateService getHibernateService() throws HibernateException
    {
        if (hibernateService == null)
        {
            hibernateService =
                (HibernateService) resolve(HibernateService.ROLE);

        }
        return hibernateService;
    }

    public void dispose(){
        release(hibernateService);
        super.dispose();
    }

}
