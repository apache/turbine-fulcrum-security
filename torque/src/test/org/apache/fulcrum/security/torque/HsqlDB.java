package org.apache.fulcrum.security.torque;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.hsqldb.jdbcDriver;

/**
 * This Class provides a nice Setup for a Hypersonic SQL Server, which will only
 * be held in Memory.
 *
 * It allows loading of .sql Files, to setup Tests easily.
 *
 * @author <a href="jh@byteaction.de">J&#252;rgen Hoffmann</a>
 */
public class HsqlDB {
	private Connection connection = null;
	private static String URI = "jdbc:hsqldb:.";

	public HsqlDB(String loadFile) throws Exception {
		Class.forName(jdbcDriver.class.getName());
		this.connection = DriverManager.getConnection(URI, "SA", "");
		if (StringUtils.isNotEmpty(loadFile)) {
			loadSqlFile(loadFile);
		}
	}

	public HsqlDB(File loadFile) throws Exception {
		this(loadFile.getAbsolutePath());
	}

	public Connection getConnection() {
		return connection;
	}

	public void close() {
		try {
			connection.close();
		} catch (Exception e) {
		}
	}

	/**
	 * Load a sql file and process its statements
	 */
	private void loadSqlFile(String fileName) throws Exception {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			// execute each statement on its own
			List<String> commands = getFileContents(fileName);
			for (String cmd : commands)
				statement.executeUpdate(cmd);

		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}

	private List<String> getFileContents(String fileName) throws Exception {

		StringBuilder sb = new StringBuilder(1000);
		Scanner s = null;
		try {
			InputStream is = new FileInputStream(new File(fileName));
			s = new Scanner(is);
			s.useDelimiter("(\r?\n)");

			boolean inComment = false;
			while (s.hasNext()) {
				String line = s.next();

				if (inComment == true) {
					if (line.endsWith("*/"))
						inComment = false;
				} else {
					// test for comments
					if (!line.startsWith("--")) {
						if (line.trim().length() > 0) {
							sb.append(line);
						}
					}
				}
			}
		} finally {
			if (s != null)
				s.close();
		}

		String sql = sb.toString();
		sb = new StringBuilder();
		String[] commands = sql.split(";");
		List<String> sqlCmds = new ArrayList<>();
		for (String cmd : commands)
			sqlCmds.add(cmd + ";\n");

		return sqlCmds;
	}

	public void addSQL(String sqlFile) throws Exception {
		if (StringUtils.isNotEmpty(sqlFile)) {
			loadSqlFile(sqlFile);
		}
	}

	public void addSQL(File sqlFile) throws Exception {
		this.addSQL(sqlFile.getAbsolutePath());
	}
}
