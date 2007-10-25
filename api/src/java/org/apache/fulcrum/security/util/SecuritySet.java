package org.apache.fulcrum.security.util;

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

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.lang.StringUtils;
import org.apache.fulcrum.security.entity.SecurityEntity;
/**
 * This class represents a set of Security Entities. It makes it easy to build
 * a UI. It wraps a TreeSet object to enforce that only relevant methods are
 * available. TreeSet's contain only unique Objects (no duplicates) based on the
 * ID.  They may or may not have a name, that depends on the implementation.
 * Want to get away frm requiring an ID and a name... Nothing should force
 * Name to be unique in the basic architecture of Fulcrum Security.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @author <a href="mailto:jmcnally@collab.net">John D. McNally</a>
 * @author <a href="mailto:bmclaugh@algx.net">Brett McLaughlin</a>
 * @author <a href="mailto:marco@intermeta.de">Marco Kn&uuml;ttel</a>
 * @author <a href="mailto:hps@intermeta.de">Henning P. Schmiedehausen</a>
 * @version $Id$
 */
public abstract class SecuritySet implements Serializable, Set
{
    /** Map for "name" -> "security object" */
  //  protected Map nameMap = null;
    /** Map for "id" -> "security object" */
    protected Map idMap = null;
    /**
	 * Constructs an empty Set
	 */
    public SecuritySet()
    {
        //nameMap = new TreeMap();
        idMap = new TreeMap();
    }

    /*
     * To enable the typesafe handling, make this abstract
     * and rely on the implementing classes like RoleSet to
     * properly cast the Object type.
     *
     * @see java.util.Collection#add(java.lang.Object)
     */
    public abstract boolean add(Object o);
    /**
	 * Returns a set of security objects in this object.
	 *
	 * @return A Set Object
	 *
	 */
    public Set getSet()
    {
        return new HashSet(idMap.values());
    }
    /**
	 * Returns a set of Names in this Object.
	 *
	 * @return The Set of Names in this Object, backed by the actual data.
	 */
    public Set getNames()
    {
        Set names = new HashSet();
        for (Iterator i = getSet().iterator();i.hasNext();){
            SecurityEntity se = (SecurityEntity)i.next();
            names.add(se.getName());
        }
        return names;
        //return nameMap.keySet();
    }
    /**
	 * Returns a set of Id values in this Object.
	 *
	 * @return The Set of Ids in this Object, backed by the actual data.
	 */
    public Set getIds()
    {
        return idMap.keySet();
    }
    /**
	 * Removes all Objects from this Set.
	 */
    public void clear()
    {
      //  nameMap.clear();
        idMap.clear();
    }
    /**
	 * Searches if an Object with a given name is in the Set
	 *
	 * @param roleName Name of the Security Object.
	 * @return True if argument matched an Object in this Set; false if no
	 *         match.
	 */
    public boolean containsName(String name)
    {

		return (StringUtils.isNotEmpty(name))
				   ? getNames().contains(name.toLowerCase())
				   : false;
        /*
         *        return (StringUtils.isNotEmpty(name))
         *            ? nameMap.containsKey(name.toLowerCase())
         *            : false;
         */
    }
    /**
	 * Searches if an Object with a given Id is in the Set
	 *
	 * @param id Id of the Security Object.
	 * @return True if argument matched an Object in this Set; false if no
	 *         match.
	 */
    public boolean containsId(Object id)
    {
        return (id == null) ? false : idMap.containsKey(id);
    }
    /**
	 * Returns an Iterator for Objects in this Set.
	 *
	 * @return An iterator for the Set
	 */
    public Iterator iterator()
    {
        return idMap.values().iterator();
    }
    /**
	 * Returns size (cardinality) of this set.
	 *
	 * @return The cardinality of this Set.
	 */
    public int size()
    {
        return idMap.size();
    }
    /**
	 * list of role names in this set
	 *
	 * @return The string representation of this Set.
	 */
    public String toString()
    {
        StringBuffer sbuf = new StringBuffer(12 * size());
        for (Iterator it = idMap.keySet().iterator(); it.hasNext();)
        {
            sbuf.append((String) it.next());
            if (it.hasNext())
            {
                sbuf.append(", ");
            }
        }
        return sbuf.toString();
    }
    // methods from Set
    public boolean addAll(Collection collection)
    {
        return add((Collection) collection);
    }
    public boolean isEmpty()
    {
        return idMap.isEmpty();
    }
    public boolean containsAll(Collection collection)
    {
        for (Iterator i = collection.iterator(); i.hasNext();)
        {
            Object object = i.next();
            if (!contains(object))
            {
                return false;
            }
        }
        return true;
    }
    public boolean removeAll(Collection collection)
    {
        boolean changed = false;
        for (Iterator i = collection.iterator(); i.hasNext();)
        {
            Object object = i.next();
            boolean result = remove(object);
            if (result)
            {
                changed = true;
            }
        }
        return changed;
    }
    public boolean retainAll(Collection collection)
    {
        throw new RuntimeException("not implemented");
    }
    /*
     * (non-Javadoc)
     *
     * @see java.util.Collection#toArray()
     */
    public Object[] toArray()
    {
        return getSet().toArray();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Collection#contains(java.lang.Object)
     */
    public boolean contains(Object o)
    {
        if (o == null)
        {
            return false;
        }
        else
        {
            return containsName(((SecurityEntity) o).getName());
        }
    }
    /*
     * (non-Javadoc)
     *
     * @see java.util.Collection#remove(java.lang.Object)
     */
    public boolean remove(Object o)
    {
        SecurityEntity se = (SecurityEntity)o;
		boolean res = contains(se);
		idMap.remove(se.getId());
		return res;
    }
    /*
     * (non-Javadoc)
     *
     * @see java.util.Collection#toArray(java.lang.Object[])
     */
    public Object[] toArray(Object[] a)
    {
        return getSet().toArray(a);
    }

    public SecurityEntity getByName(String name){
        SecurityEntity securityEntity = null;
        for (Iterator i = getSet().iterator();i.hasNext();){
            SecurityEntity se = (SecurityEntity)i.next();
            if(se.getName().equalsIgnoreCase(name)){
                securityEntity = se;
                break;
            }
        }
        return securityEntity;


    }
}
