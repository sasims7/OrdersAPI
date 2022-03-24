package com.aws.lambda.apis;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
	
	public static Connection getDatabaseConnection() {
		Connection conn = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager
					.getConnection("jdbc:DB_HOST://localhost:5432/dbName",
							"UserName", "Password");
			//conn.setAutoCommit(false);
			System.out.println("Opened database connection successfully");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}
