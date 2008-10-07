package org.apache.fulcrum.security.ldap;

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
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.spi.AbstractUserManager;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.PasswordMismatchException;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.apache.fulcrum.security.util.UserSet;

/**
 * This implementation uses ldap for retrieving user data.
 *
 * @author <a href="mailto:jon@collab.net">Jon S. Stevens</a>
 * @author <a href="mailto:john.mcnally@clearink.com">John D. McNally</a>
 * @author <a href="mailto:frank.kim@clearink.com">Frank Y. Kim</a>
 * @author <a href="mailto:cberry@gluecode.com">Craig D. Berry</a>
 * @author <a href="mailto:Rafal.Krzewski@e-point.pl">Rafal Krzewski</a>
 * @author <a href="mailto:tadewunmi@gluecode.com">Tracy M. Adewunmi</a>
 * @author <a href="mailto:lflournoy@gluecode.com">Leonard J. Flournoy</a>
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @author <a href="mailto:hhernandez@itweb.com.mx">Humberto Hernandez</a>
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @author <a href="mailto:tv@apache.org">Thomas Vandahl</a>
 * 
 * @version $Id:LDAPUserManagerImpl.java 535465 2007-05-05 06:58:06Z tv $
 */
public class LDAPUserManagerImpl extends AbstractUserManager
{
    /** Property group key */
    public static final String LDAP_KEY = "ldap";

    /** Property key */
    public static final String LDAP_ADMIN_USERNAME_KEY = "adminUsername";

    /** Property key */
    public static final String LDAP_ADMIN_PASSWORD_KEY = "adminPassword";

    /** Property key */
    public static final String LDAP_HOST_KEY = "host";

    /** Property default value */
    public static final String LDAP_HOST_DEFAULT = "localhost";

    /** Property key */
    public static final String LDAP_PORT_KEY = "port";

    /** Property default value */
    public static final String LDAP_PORT_DEFAULT = "389";

    /** Property key */
    public static final String LDAP_PROVIDER_KEY = "provider";

    /** Property default value */
    public static final String LDAP_PROVIDER_DEFAULT =
            "com.sun.jndi.ldap.LdapCtxFactory";

    /** Property key */
    public static final String LDAP_BASE_SEARCH_KEY = "basesearch";

    /** Property key */
    public static final String LDAP_AUTH_KEY = "securityAuthentication";

    /** Property default value */
    public static final String LDAP_AUTH_DEFAULT = "simple";

    /** Property group key */
    public static final String LDAP_USER_KEY = "userAttributes";

    /** Property key */
    public static final String LDAP_USER_OBJECTCLASS_KEY = "objectClass";

    /** Property default value */
    public static final String LDAP_USER_OBJECTCLASS_DEFAULT = "turbineUser";

    /** Property key */
    public static final String LDAP_USER_USERID_KEY = "userid";

    /** Property default value */
    public static final String LDAP_USER_USERID_DEFAULT = "uid";

    /** Property key */
    public static final String LDAP_USER_USERNAME_KEY = "username";

    /** Property default value */
    public static final String LDAP_USER_USERNAME_DEFAULT = "turbineUserUniqueId";

    /** Property key */
    public static final String LDAP_USER_FIRSTNAME_KEY = "firstname";

    /** Property default value */
    public static final String LDAP_USER_FIRSTNAME_DEFAULT = "turbineUserFirstName";

    /** Property key */
    public static final String LDAP_USER_LASTNAME_KEY = "lastname";

    /** Property default value */
    public static final String LDAP_USER_LASTNAME_DEFAULT = "turbineUserLastName";

    /** Property key */
    public static final String LDAP_USER_EMAIL_KEY = "email";

    /** Property default value */
    public static final String LDAP_USER_EMAIL_DEFAULT = "turbineUserMailAddress";

    /** Property key */
    public static final String LDAP_USER_PASSWORD_KEY = "password";

    /** Property default value */
    public static final String LDAP_USER_PASSWORD_DEFAULT = "userPassword";


    /** Credentials to use for admin binding */
    protected String ldapAdminUsername;

    /** Credentials to use for admin binding */
    protected String ldapAdminPassword;

    /** Host name of the LDAP server */
    protected String ldapHost;

    /** Port number of the LDAP server */
    protected String ldapPort;

    /** The provider class to use for binding */
    protected String ldapProvider;

    /** Base DN */
    protected String ldapBasesearch;

    /** LDAP Authentication type */
    protected String ldapSecurityAuthentication;

    /** LDAP Attribute to use for the users object class */
    protected String ldapObjectClass;

    /** LDAP Attribute to use for the unique user id */
    protected String ldapUserid;

    /** LDAP Attribute to use for the unique user name */
    protected String ldapUsername;

    /** LDAP Attribute to use for the users first name */
    protected String ldapFirstname;

    /** LDAP Attribute to use for the users last name */
    protected String ldapLastname;

    /** LDAP Attribute to use for the users email address */
    protected String ldapEmail;

    /** LDAP Attribute to use for the users password */
    protected String ldapPassword;

    /** Our Unique ID counter */
    private static int uniqueId = 0;

    /** Provide a unique id for a user */
    private String getUniqueId()
    {
        return String.valueOf(++uniqueId);
    }
    
    /**
     * Check whether a specified user's account exists.
     * 
     * The login name is used for looking up the account.
     * 
     * @param userName
     *            The name of the user to be checked.
     * @return true if the specified account exists
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     */
    public boolean checkExists(String userName) throws DataBackendException
    {
        try
        {
            User ldapUser = getUser(userName);
        }
        catch (UnknownEntityException ex)
        {
            return false;
        }

        return true;
    }

    /**
     * Retrieves all users defined in the system.
     * 
     * @return the names of all users defined in the system.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     */
    public UserSet getAllUsers() throws DataBackendException
    {
        UserSet users = new UserSet();

        try
        {
            DirContext ctx = bindAsAdmin();

            String filter = "(" + this.ldapUsername + "=*)";

            /*
             * Create the default search controls.
             */
            SearchControls ctls = new SearchControls();

            NamingEnumeration answer =
                    ctx.search(this.ldapBasesearch, filter, ctls);

            while (answer.hasMore())
            {
                SearchResult sr = (SearchResult) answer.next();
                Attributes attribs = sr.getAttributes();

                User ldapUser = getUserInstance();
                setLDAPAttributes(ldapUser, attribs);

                users.add(ldapUser);
            }
        }
        catch (NamingException ex)
        {
            throw new DataBackendException(
                    "The LDAP server specified is unavailable", ex);
        }

        return users;
    }

    /**
     * Removes an user account from the system.
     * 
     * @param user
     *            the object describing the account to be removed.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the user account is not present.
     */
    public void removeUser(User user) throws DataBackendException, UnknownEntityException
    {
        if (!checkExists(user))
        {
            throw new UnknownEntityException("The account '"
                    + user.getName() + "' does not exist");
        }

        try
        {
            String name = getDN(user);

            DirContext ctx = bindAsAdmin();

            ctx.unbind(name);
        }
        catch (NamingException ex)
        {
            throw new DataBackendException("NamingException caught", ex);
        }
    }

    /**
     * Creates new user account with specified attributes.
     * 
     * @param user
     *            the object describing account to be created.
     * @param password
     *            The password to use for the account.
     * 
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     */
    protected User persistNewUser(User user) throws DataBackendException
    {
        if (checkExists(user))
        {
            throw new DataBackendException("The account '"
                    + user.getName() + "' already exists");
        }
        
        /* 
         * Set a numeric id in case the framework does not provide one.
         * This is meant to be a last-resort solution and should not be
         * relied upon.
         */
        if (user.getId() == null)
        {
            user.setId(getUniqueId());
        }

        try
        {
            Attributes attrs = getLDAPAttributes(user);
            String name = getDN(user);

            DirContext ctx = bindAsAdmin();

            ctx.bind(name, null, attrs);
        }
        catch (NamingException ex)
        {
            throw new DataBackendException("NamingException caught", ex);
        }

        return user;
    }

    /**
     * Stores User attributes. The User is required to exist in the system.
     * 
     * @param role
     *            The User to be stored.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the role does not exist.
     */
    public void saveUser(User user) throws DataBackendException, UnknownEntityException
    {
        if (!checkExists(user))
        {
            throw new UnknownEntityException("The account '"
                    + user.getName() + "' does not exist");
        }

        try
        {
            Attributes attrs = getLDAPAttributes(user);
            String name = getDN(user);

            DirContext ctx = bindAsAdmin();

            ctx.modifyAttributes(name, DirContext.REPLACE_ATTRIBUTE, attrs);
        }
        catch (NamingException ex)
        {
            throw new DataBackendException("NamingException caught", ex);
        }
    }

    /**
     * Override password change. We do not support it with LDAP
     * 
     * @see org.apache.fulcrum.security.spi.AbstractUserManager#changePassword(org.apache.fulcrum.security.entity.User, java.lang.String, java.lang.String)
     */
    public void changePassword(User user, String oldPassword, String newPassword) throws PasswordMismatchException,
            UnknownEntityException, DataBackendException
    {
        throw new DataBackendException("Changing passwords is not supported with LDAP.");
    }

    /**
     * Override password change. We do not support it with LDAP
     * 
     * @see org.apache.fulcrum.security.spi.AbstractUserManager#forcePassword(org.apache.fulcrum.security.entity.User, java.lang.String)
     */
    public void forcePassword(User user, String password) throws UnknownEntityException, DataBackendException
    {
        throw new DataBackendException("Changing passwords is not supported with LDAP.");
    }

    /**
     * Retrieve a user from persistent storage using username as the
     * key.
     *
     * @param name the name of the user.
     * 
     * @return an User object.
     * @exception UnknownEntityException if the user's account does not
     *            exist in the database.
     * @exception DataBackendException if there is a problem accessing the
     *            storage.
     */
    public User getUser(String name) throws DataBackendException, UnknownEntityException
    {
        try
        {
            DirContext ctx = bindAsAdmin();

            /*
             * Define the search.
             */
            String filter = "(" + this.ldapUsername + "=" + name + ")";

            /*
             * Create the default search controls.
             */
            SearchControls ctls = new SearchControls();

            NamingEnumeration answer =
                    ctx.search(this.ldapBasesearch, filter, ctls);

            if (answer.hasMore())
            {
                SearchResult sr = (SearchResult) answer.next();
                Attributes attribs = sr.getAttributes();
                
                User ldapUser = getUserInstance();
                setLDAPAttributes(ldapUser, attribs);

                return ldapUser;
            }
            else
            {
                throw new UnknownEntityException("The given user "
                        + name + " does not exist.");
            }
        }
        catch (NamingException ex)
        {
            throw new DataBackendException(
                    "The LDAP server specified is unavailable", ex);
        }
    }

    /**
     * Retrieve a user from persistent storage using the primary key
     *
     * @param id The primary key object
     * @return an User object.
     * @throws UnknownEntityException if the user's record does not
     *         exist in the database.
     * @throws DataBackendException if there is a problem accessing the
     *         storage.
     */
    public User getUserById(Object id) throws DataBackendException, UnknownEntityException
    {
        try
        {
            DirContext ctx = bindAsAdmin();

            /*
             * Define the search.
             */
            String filter = "(" + this.ldapUserid + "=" + String.valueOf(id) + ")";

            /*
             * Create the default search controls.
             */
            SearchControls ctls = new SearchControls();

            NamingEnumeration answer =
                    ctx.search(this.ldapBasesearch, filter, ctls);

            if (answer.hasMore())
            {
                SearchResult sr = (SearchResult) answer.next();
                Attributes attribs = sr.getAttributes();

                User ldapUser = getUserInstance();
                setLDAPAttributes(ldapUser, attribs);

                return ldapUser;
            }
            else
            {
                throw new UnknownEntityException("No user exists for the id "
                        + String.valueOf(id));
            }
        }
        catch (NamingException ex)
        {
            throw new DataBackendException(
                    "The LDAP server specified is unavailable", ex);
        }
    }

    /**
     * Avalon Service lifecycle method
     * 
     * @see org.apache.fulcrum.security.spi.AbstractEntityManager#configure(org.apache.avalon.framework.configuration.Configuration)
     */
    public void configure(Configuration conf) throws ConfigurationException
    {
        super.configure(conf);
        
        Configuration ldap = conf.getChild(LDAP_KEY, false);
        
        if (ldap == null)
        {
            throw new ConfigurationException("LDAP configuration is mandatory.", conf);
        }
        
        this.ldapBasesearch = ldap.getChild(LDAP_BASE_SEARCH_KEY).getValue(null);

        if (this.ldapBasesearch == null)
        {
            throw new ConfigurationException("LDAP configuration requires a base search domain", ldap);
        }

        this.ldapAdminUsername = ldap.getChild(LDAP_ADMIN_USERNAME_KEY).getValue(null);

        if (this.ldapAdminUsername == null)
        {
            throw new ConfigurationException("LDAP configuration requires an admin user", ldap);
        }

        this.ldapAdminPassword = ldap.getChild(LDAP_ADMIN_PASSWORD_KEY).getValue(null);

        if (this.ldapAdminPassword == null)
        {
            throw new ConfigurationException("LDAP configuration requires an admin password", ldap);
        }

        this.ldapHost = ldap.getChild(LDAP_HOST_KEY).getValue(LDAP_HOST_DEFAULT);
        this.ldapPort = ldap.getChild(LDAP_PORT_KEY).getValue(LDAP_PORT_DEFAULT);
        this.ldapProvider = ldap.getChild(LDAP_PROVIDER_KEY).getValue(LDAP_PROVIDER_DEFAULT);
        this.ldapSecurityAuthentication = ldap.getChild(LDAP_AUTH_KEY).getValue(LDAP_AUTH_DEFAULT);
        
        Configuration ldapUser = ldap.getChild(LDAP_USER_KEY, false);
        
        if (ldapUser == null)
        {
            getLogger().info("No LDAP user attributes defined, using defaults.");
            this.ldapObjectClass = LDAP_USER_OBJECTCLASS_DEFAULT;
            this.ldapUserid = LDAP_USER_USERID_DEFAULT;
            this.ldapUsername = LDAP_USER_USERNAME_DEFAULT;
            this.ldapFirstname = LDAP_USER_FIRSTNAME_DEFAULT;
            this.ldapLastname = LDAP_USER_LASTNAME_DEFAULT;
            this.ldapEmail = LDAP_USER_EMAIL_DEFAULT;
            this.ldapPassword = LDAP_USER_PASSWORD_DEFAULT;
        }
        else
        {
            this.ldapObjectClass = ldapUser.getChild(LDAP_USER_OBJECTCLASS_KEY).getValue(LDAP_USER_OBJECTCLASS_DEFAULT);
            this.ldapUserid = ldapUser.getChild(LDAP_USER_USERID_KEY).getValue(LDAP_USER_USERID_DEFAULT);
            this.ldapUsername = ldapUser.getChild(LDAP_USER_USERNAME_KEY).getValue(LDAP_USER_USERNAME_DEFAULT);
            this.ldapFirstname = ldapUser.getChild(LDAP_USER_FIRSTNAME_KEY).getValue(LDAP_USER_FIRSTNAME_DEFAULT);
            this.ldapLastname = ldapUser.getChild(LDAP_USER_LASTNAME_KEY).getValue(LDAP_USER_LASTNAME_DEFAULT);
            this.ldapEmail = ldapUser.getChild(LDAP_USER_EMAIL_KEY).getValue(LDAP_USER_EMAIL_DEFAULT);
            this.ldapPassword = ldapUser.getChild(LDAP_USER_PASSWORD_KEY).getValue(LDAP_USER_PASSWORD_DEFAULT);
        }
    }
    
    /**
     * Bind as the admin user.
     *
     * @throws NamingException when an error occurs with the named server.
     * @return a new DirContext.
     */
    public DirContext bindAsAdmin()
            throws NamingException
    {
        return bind(this.ldapAdminUsername, this.ldapAdminPassword);
    }

    /**
     * Creates an initial context.
     *
     * @param username admin username supplied in configuration.
     * @param password admin password supplied in configuration.
     * @throws NamingException when an error occurs with the named server.
     * @return a new DirContext.
     */
    public DirContext bind(String username, String password)
            throws NamingException
    {
        String providerURL = "ldap://" + this.ldapHost + ":" + this.ldapPort;

        /*
         * creating an initial context using Sun's client
         * LDAP Provider.
         */
        Hashtable env = new Hashtable();

        env.put(Context.INITIAL_CONTEXT_FACTORY, this.ldapProvider);
        env.put(Context.PROVIDER_URL, providerURL);
        env.put(Context.SECURITY_AUTHENTICATION, this.ldapSecurityAuthentication);
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, password);

        DirContext ctx = new javax.naming.directory.InitialDirContext(env);

        return ctx;
    }
    
    /**
     * Populates the user with values obtained from the LDAP Service.
     * This method could be redefined in subclasses.
     * @param user The user to set the parameters to
     * @param attribs The attributes obtained from LDAP.
     * @throws NamingException if there was an error with JNDI.
     */
    protected void setLDAPAttributes(User user, Attributes attribs)
            throws NamingException
    {
        Attribute attr;

        // Set the User id.
        attr = attribs.get(this.ldapUserid);
        if (attr != null && attr.get() != null)
        {
            try
            {
                user.setId(attr.get());
            }
            catch (Exception ex)
            {
                getLogger().error("Exception caught:", ex);
            }
        }

        // Set the Username.
        attr = attribs.get(this.ldapUsername);
        if (attr != null && attr.get() != null)
        {
            user.setName(attr.get().toString());
        }

        if (user instanceof LDAPUser)
        {
            LDAPUser u = (LDAPUser)user;
            
            // Set the Firstname.
            attr = attribs.get(this.ldapFirstname);
            if (attr != null && attr.get() != null)
            {
                u.setFirstName(attr.get().toString());
            }
    
            // Set the Lastname.
            attr = attribs.get(this.ldapLastname);
            if (attr != null && attr.get() != null)
            {
                u.setLastName(attr.get().toString());
            }
    
            // Set the E-Mail
            attr = attribs.get(this.ldapEmail);
            if (attr != null && attr.get() != null)
            {
                u.setEmail(attr.get().toString());
            }
        }
    }

    /**
     * Get the JNDI Attributes used to store the user in LDAP.
     * This method could be redefined in a subclass.
     *
     * @param user The user which attributes to get
     *
     * @throws NamingException if there is a JNDI error.
     * @return The JNDI attributes of the user.
     */
    protected Attributes getLDAPAttributes(User user)
            throws NamingException
    {
        Attributes attribs = new BasicAttributes();

        // Set the objectClass
        {
            Attribute attr = new BasicAttribute("objectClass", this.ldapObjectClass);
            attribs.put(attr);
        }

        // Set the User id. This should be numeric and must not be null
        Object value = user.getId();

        if (value != null)
        {
            Attribute attr = new BasicAttribute(this.ldapUserid, value);
            attribs.put(attr);
        }

        // Set the Username.
        value = user.getName();

        if (value != null)
        {
            Attribute attr = new BasicAttribute(this.ldapUsername, value);
            attribs.put(attr);
        }

        // Set the Password
        value = user.getPassword();

        if (value != null)
        {
            Attribute attr = new BasicAttribute(this.ldapPassword, value);
            attribs.put(attr);
        }

        if (user instanceof LDAPUser)
        {
            LDAPUser u = (LDAPUser)user;
            
            // Set the Firstname.
            value = u.getFirstName();
    
            if (value != null)
            {
                Attribute attr = new BasicAttribute(this.ldapFirstname, value);
                attribs.put(attr);
            }
    
            // Set the Lastname.
            value = u.getLastName();
    
            if (value != null)
            {
                Attribute attr = new BasicAttribute(this.ldapLastname, value);
                attribs.put(attr);
            }
    
            // Set the E-Mail.
            value = u.getEmail();
    
            if (value != null)
            {
                Attribute attr = new BasicAttribute(this.ldapEmail, value);
                attribs.put(attr);
            }
        }

        return attribs;
    }
    
    /**
     * Gets the distinguished name (DN) of the User.
     * This method could be redefined in a subclass.
     * 
     * @param user The user to provide the DN for
     * 
     * @return The Distinguished Name of the user.
     */
    public String getDN(User user)
    {
        String dn = this.ldapUsername + "=" + user.getName() + "," + this.ldapBasesearch;

        return dn;
    }
}
