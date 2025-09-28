package com.ai.azure.functions.logic.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.azure.functions.logic.db.connection.MySQLConnection;

public class AzureFunctionsRepo {
	
	public List<Map<String, String>> getAllFunctions() throws SQLException {
		
		List<Map<String, String>> functions = new ArrayList<>();
		try (Connection conn = MySQLConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM azure_functions")) {
			while (rs.next()) {
				Map<String, String> f = new HashMap<>();
				f.put("id", String.valueOf(rs.getInt("id")));
				f.put("name", rs.getString("name"));
				f.put("url", rs.getString("url"));
				functions.add(f);
			}
		}
		return functions;
	}

	public void addFunction(String name, String url) throws SQLException {
		String sql = "INSERT INTO azure_functions (name, url) VALUES (?, ?)";
		try (Connection conn = MySQLConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, name);
			ps.setString(2, url);
			ps.executeUpdate();
		}
	}

	public void updateFunction(int id, String name, String url) throws SQLException {
		String sql = "UPDATE azure_functions SET name = ?, url = ? WHERE id = ?";
		try (Connection conn = MySQLConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, name);
			ps.setString(2, url);
			ps.setInt(3, id);
			ps.executeUpdate();
		}
	}

	public void deleteFunction(int id) throws SQLException {
		String sql = "DELETE FROM azure_functions WHERE id = ?";
		try (Connection conn = MySQLConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}
}
