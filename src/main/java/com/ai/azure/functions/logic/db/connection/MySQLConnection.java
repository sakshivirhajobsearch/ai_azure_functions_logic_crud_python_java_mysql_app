package com.ai.azure.functions.logic.db.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
	public static Connection getConnection() throws SQLException {
		String url = "jdbc:mysql://localhost:3306/ai_azure_functions_logic?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
		String user = "root";
		String password = "admin";
		return DriverManager.getConnection(url, user, password);
	}
}
