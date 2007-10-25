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


import java.lang.reflect.Method;
import java.lang.reflect.Modifier;


/**
 * This class generates result strings which represent source code
 * for the implementation of a delegate interface.  Provide an interface,
 * delegating class type and a variable name and then call the various
 * methods to get the imports, reference variable declaration, and methods.
 * <p>This saves time when wanting to (for example) implement a collection and
 * delegate it to a private array list.
 * <p>I am using this by running it in an IDE and printing the strings (see the
 * main method) and then copying the results into the class I am developing.
 * <p><b>Note that you should review the methods generated.  In the main method
 * test you should notice that Collection requires hashcode be implemented, but
 * you probably do not want to delegate this method to the ArrayList.</b>
 * @author  Mike Barbre
 * @version 1.0
 */
public class InterfaceDelegator {
	private Class theInterface = null;
	private Class theDelegate = null;
	private String delegateName = null;

	/**
	 * Construct an InterfaceDelegator
	 */
	public InterfaceDelegator () {
	}

	/**
	 * Set the interface class that is to be implemented
	 * @param c
	 */
	public void setInterface (Class c) {
		theInterface = c;
	}

	/**
	 * Get the interface class
	 * @return
	 */
	public Class getInterface () {
		return  theInterface;
	}

	/**
	 * Set the delegate class type.
	 * @param c
	 */
	public void setDelegate (Class c) {
		theDelegate = c;
	}

	/**
	 * Get the delegate class type
	 * @return
	 */
	public Class getDelegate () {
		return  theDelegate;
	}

	/**
	 * Set the delegates variable name.
	 * @param name
	 */
	public void setDelegateName (String name) {
		delegateName = name;
	}

	/**
	 * Get the delegates variable name.
	 * @return
	 */
	public String getDelegateName () {
		return  delegateName;
	}

	/**
	 * Get an array of import statements should be included.  This is a
	 * suggestion only, and currently of minimal use.
	 * @return
	 */
	public String[] getImportStatements () {
		String[] statements = new String[2];
		int index = 0;
		if (getInterface() != null) {
			statements[index++] = "import " + getInterface().getPackage().getName() + ".*;";
		}
		if (getDelegate() != null) {
			statements[index++] = "import " + getDelegate().getPackage().getName() + ".*;";
		}
		return  statements;
	}

	/**
	 * Get the declaration statement for the class level delegate variable.
	 * @return
	 */
	public String getDelegateDeclaration () {
		return  "private " + getDelegate().getName() + " " + getDelegateName() + " = new " + getDelegate().getName() + "();";
	}

	/**
	 * Get an unformatted array of method calls that implement the interface by
	 * referring calls to the delegate.
	 * @return
	 */
	public String[] getMethods () {
		try {
			Method[] m = getInterface().getDeclaredMethods();
			String[] s = new String[m.length];
			for (int i = 0; i < m.length; i++) {
				String mods = Modifier.toString(m[i].getModifiers() - Modifier.ABSTRACT);
				Class returnClass = m[i].getReturnType();
				String returnType = "";
				if (returnClass.isArray())
					returnType = returnClass.getComponentType().getName() + "[]";
				else
					returnType = returnClass.getName();
				String name = m[i].getName();
				Class[] except = m[i].getExceptionTypes();
				Class[] parms = m[i].getParameterTypes();
				s[i] = mods + " " + returnType + " " + name + "(";
				String delegateCall = getDelegateName() + "." + name + "(";
				if (parms != null)
					for (int j = 0; j < parms.length; j++) {
						if (j > 0) {
							s[i] += ", ";
							delegateCall += ", ";
						}
						if (parms[j].isArray()) {
							String type = parms[j].getComponentType().getName();
							String shortName = type.substring(type.lastIndexOf('.') + 1);
							shortName = shortName.substring(0, 1).toUpperCase() + shortName.substring(1);
							s[i] += type + "[] a" + shortName;
							delegateCall += "a" + shortName;
						}
						else {
							String shortName = parms[j].getName().substring(parms[j].getName().lastIndexOf('.') + 1);
							shortName = shortName.substring(0, 1).toUpperCase() + shortName.substring(1);
							s[i] += parms[j].getName() + " a" + shortName;
							delegateCall += "a" + shortName;
						}
					}
				s[i] += ")";
				delegateCall += ");";
				if (except.length > 0) {
					s[i] += " throws ";
					for (int j = 0; j < except.length; j++) {
						if (j > 0) {
							s[i] += ", ";
						}
						s[i] += except[j].getName();
					}
				}
				if (returnType.equals("void") == false)
					s[i] += " { return " + delegateCall + " }";
				else
					s[i] += " { " + delegateCall + " }";
			}
			return  s;
		} catch (Exception e) {
			e.printStackTrace();
			return  null;
		}
	}

	/**
	 * Test
	 */
	public static void main (String[] args) {
		InterfaceDelegator id = new InterfaceDelegator();
		id.setDelegate(org.apache.fulcrum.security.adapter.turbine.AccessControlListAdapter.class);
		id.setInterface(org.apache.turbine.util.security.AccessControlList.class);
		id.setDelegateName("acl");
		System.out.println();
		System.out.println("-- Import Statements --");
		String[] s = id.getImportStatements();
		for (int i = 0; i < s.length; i++) {
			System.out.println(s[i]);
		}
		System.out.println();
		System.out.println("-- Declaration --");
		System.out.println(id.getDelegateDeclaration());
		System.out.println();
		System.out.println("-- Implementation methods --");
		s = id.getMethods();
		for (int i = 0; i < s.length; i++) {
			System.out.println(s[i]);
		}
	}
}
