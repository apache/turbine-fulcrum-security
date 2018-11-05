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

import org.apache.commons.lang3.StringUtils;
import org.apache.fulcrum.security.entity.SecurityEntity;

/**
 * This class represents a set of Security Entities. It makes it easy to build a
 * UI. It wraps a TreeSet object to enforce that only relevant methods are
 * available. TreeSet's contain only unique Objects (no duplicates) based on the
 * ID. They may or may not have a name, that depends on the implementation. Want
 * to get away from requiring an ID and a name... Nothing should force Name to
 * be unique in the basic architecture of Fulcrum Security.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @author <a href="mailto:jmcnally@collab.net">John D. McNally</a>
 * @author <a href="mailto:bmclaugh@algx.net">Brett McLaughlin</a>
 * @author <a href="mailto:marco@intermeta.de">Marco Kn&uuml;ttel</a>
 * @author <a href="mailto:hps@intermeta.de">Henning P. Schmiedehausen</a>
 * @version $Id$
 */
public abstract class SecuritySet<T extends SecurityEntity> implements Serializable, Set<T>, Iterable<T> {
	/** Serial version */
	private static final long serialVersionUID = 2251987059226422569L;

	/** Map for "name" is "security object" */
	protected Map<String, T> nameMap = null;

	/** Map for "id" is "security object" */
	protected Map<Object, T> idMap = null;

	/**
	 * Constructs an empty Set
	 */
	public SecuritySet() {
		nameMap = new TreeMap<String, T>(String.CASE_INSENSITIVE_ORDER);
		idMap = new TreeMap<Object, T>();
	}

	/**
	 * Returns a set of security objects in this object.
	 *
	 * @return A Set Object
	 *
	 */
	public Set<T> getSet() {
		return new HashSet<T>(idMap.values());
	}

	/**
	 * Returns a set of Names in this Object.
	 *
	 * @return The Set of Names in this Object, backed by the actual data.
	 */
	public Set<String> getNames() {
		return nameMap.keySet();
	}

	/**
	 * Returns a set of Id values in this Object.
	 *
	 * @return The Set of Ids in this Object, backed by the actual data.
	 */
	public Set<Object> getIds() {
		return idMap.keySet();
	}

	/**
	 * Removes all Objects from this Set.
	 */
	@Override
	public void clear() {
		nameMap.clear();
		idMap.clear();
	}

	/**
	 * Searches if an Object with a given name is in the Set
	 *
	 * @param name Name of the Security Object.
	 * @return True if argument matched an Object in this Set; false if no match.
	 */
	public boolean containsName(String name) {
		return (StringUtils.isNotEmpty(name)) ? nameMap.containsKey(name) : false;
	}

	/**
	 * Searches if an Object with a given Id is in the Set
	 *
	 * @param id Id of the Security Object.
	 * @return True if argument matched an Object in this Set; false if no match.
	 */
	public boolean containsId(Object id) {
		return (id == null) ? false : idMap.containsKey(id);
	}

	/**
	 * Returns an Iterator for Objects in this Set.
	 *
	 * @return An iterator for the Set
	 */
	@Override
	public Iterator<T> iterator() {
		return idMap.values().iterator();
	}

	/**
	 * Returns size (cardinality) of this set.
	 *
	 * @return The cardinality of this Set.
	 */
	@Override
	public int size() {
		return idMap.size();
	}

	/**
	 * list of role names in this set
	 *
	 * @return The string representation of this Set.
	 */
	@Override
	public String toString() {
		StringBuilder sbuf = new StringBuilder(12 * size());

		for (Iterator<T> it = iterator(); it.hasNext();) {
			T se = it.next();
			sbuf.append('[');
			sbuf.append(se.getName());
			sbuf.append(" -> ");
			sbuf.append(se.getId());
			sbuf.append(']');
			if (it.hasNext()) {
				sbuf.append(", ");
			}
		}

		return sbuf.toString();
	}

	// methods from Set
	/**
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	@Override
	public boolean add(T o) {
		if (contains(o)) {
			return false;
		}

		if (o.getId() != null) {
			idMap.put(o.getId(), o);
		}
		if (o.getName() != null) {
			nameMap.put(o.getName(), o);
		}

		return true;
	}

	/**
	 * Adds the entities in a Collection to this SecuritySet.
	 *
	 * @param collection A Collection of entities.
	 * @return True if this Set changed as a result; false if no change to this Set
	 *         occurred (this Set already contained all members of the added Set).
	 */
	public boolean add(Collection<? extends T> collection) {
		return addAll(collection);
	}

	@Override
	public boolean addAll(Collection<? extends T> collection) {
		boolean res = false;

		for (T o : collection) {
			res |= add(o);
		}

		return res;
	}

	@Override
	public boolean isEmpty() {
		return idMap.isEmpty();
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		for (Object object : collection) {
			if (!contains(object)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		boolean changed = false;
		for (Object object : collection) {
			boolean result = remove(object);
			if (result) {
				changed = true;
			}
		}

		return changed;
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		throw new RuntimeException("not implemented");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Collection#toArray()
	 */
	@Override
	public Object[] toArray() {
		return getSet().toArray();
	}

	/**
	 * Checks whether this SecuritySet contains an entity.
	 *
	 * @param o An entity.
	 * @return True if this Set contains the entity, false otherwise.
	 */
	@Override
	public boolean contains(Object o) {
		if (o == null || !(o instanceof SecurityEntity)) {
			return false;
		} else {
			return containsId(((SecurityEntity) o).getId());
		}
	}

	/**
	 * Removes an entity from this SecuritySet.
	 *
	 * @param o An entity.
	 * @return True if this Set contained the entity before it was removed.
	 */
	@Override
	public boolean remove(Object o) {
		if (o instanceof SecurityEntity) {
			boolean res = contains(o);
			idMap.remove(((SecurityEntity) o).getId());
			nameMap.remove(((SecurityEntity) o).getName());
			return res;
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see java.util.Set#toArray(java.lang.Object[])
	 */
	@Override
	public <A> A[] toArray(A[] a) {
		return getSet().toArray(a);
	}

	/**
	 * Returns an entity with the given name, if it is contained in this
	 * SecuritySet.
	 *
	 * @param name Name of entity.
	 * @return entity if argument matched an entity in this Set; null if no match.
	 */
	public T getByName(String name) {
		return nameMap.get(name);
	}

	/**
	 * Returns an entity with the given id, if it is contained in this SecuritySet.
	 *
	 * @param id ID of entity.
	 * @return entity if argument matched an entity in this Set; null if no match.
	 */
	public T getById(Object id) {
		return idMap.get(id);
	}
}
