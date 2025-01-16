package com.airpapier.database;
import com.airpapier.Config;
import lombok.Getter;

import java.sql.*;
import java.util.*;

public class DataBaseConnection {
    private static final String URL = Config.getInstance().getDb().getDbUrl();
    private static final String USER = Config.getInstance().getDb().getDbUser();
    private static final String PASSWORD = Config.getInstance().getDb().getDbPassword();
    @Getter private static Connection connection;

    public static void initializeConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            if (connection != null) {
                System.out.println("Successfully connected to the database!");
            }
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static List<Map<String, Object>> getResults(String query) throws SQLException {
        String[] queries = query.split(";");
        List<Map<String, Object>> allResults = new ArrayList<>();

        for (String q : queries) {
            if (!query.trim().isEmpty()) {
                allResults.addAll(processResults(query.trim()));
            }
        }

        return allResults;
    }

    private static List<Map<String, Object>> processResults(String query) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        System.out.println(query);
        Statement stmt = connection.createStatement();
        if (query.trim().toUpperCase().startsWith("SELECT")) {
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                }
                results.add(row);
            }
        } else {
            stmt.executeUpdate(query);
        }
        return results;
    }
}