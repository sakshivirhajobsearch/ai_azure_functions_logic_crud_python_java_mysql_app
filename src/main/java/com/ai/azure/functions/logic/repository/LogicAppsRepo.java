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

public class LogicAppsRepo {
	
	public List<Map<String, String>> getAllLogicApps() throws SQLException {
		
		List<Map<String, String>> apps = new ArrayList<>();
		try (Connection conn = MySQLConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM logic_apps")) {
			while (rs.next()) {
				Map<String, String> a = new HashMap<>();
				a.put("id", String.valueOf(rs.getInt("id")));
				a.put("name", rs.getString("name"));
				a.put("url", rs.getString("url"));
				apps.add(a);
			}
		}
		return apps;
	}

	public void addLogicApp(String name, String url) throws SQLException {
		String sql = "INSERT INTO logic_apps (name, url) VALUES (?, ?)";
		try (Connection conn = MySQLConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, name);
			ps.setString(2, url);
			ps.executeUpdate();
		}
	}

	public void updateLogicApp(int id, String name, String url) throws SQLException {
		String sql = "UPDATE logic_apps SET name = ?, url = ? WHERE id = ?";
		try (Connection conn = MySQLConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, name);
			ps.setString(2, url);
			ps.setInt(3, id);
			ps.executeUpdate();
		}
	}

	public void deleteLogicApp(int id) throws SQLException {
		String sql = "DELETE FROM logic_apps WHERE id = ?";
		try (Connection conn = MySQLConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}
}
