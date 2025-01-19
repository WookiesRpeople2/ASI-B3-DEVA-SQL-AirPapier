package com.airpapier.database;
import com.airpapier.Config;
import com.airpapier.interfaces.RowMapper;
import lombok.Getter;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;

public class DataBaseConnection {
    private static final String URL = Config.getInstance().getDb().getDbUrl();
    private static final String USER = Config.getInstance().getDb().getDbUser();
    private static final String PASSWORD = Config.getInstance().getDb().getDbPassword();
    @Getter static final DataBaseConnection instance = new DataBaseConnection();
    @Getter private static Connection connection;
    @Getter private static DSLContext dsl;


    public void initializeConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            dsl = DSL.using(connection, SQLDialect.MYSQL);
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

    public static <T, R extends Record> RecordMapper<R,T> makeMapper(Class<T> classInstance) {
        return record -> {
            try {
                Method builderMethod = classInstance.getMethod("builder");
                Object builder = builderMethod.invoke(null);
                Field[] fields = classInstance.getDeclaredFields();

                for (Field field : fields) {
                    try {
                        String fieldName = field.getName();
                        System.out.println(fieldName);
                        Object value = record.get(fieldName, field.getType());

                        if (value != null) {
                            Method setterMethod = builder.getClass().getMethod(fieldName, field.getType());
                            setterMethod.invoke(builder, value);
                        }
                    } catch (Exception e) {
                        System.err.println("Error processing field: " + e.getMessage());
                    }
                }

                Method buildMethod = builder.getClass().getMethod("build");
                Object builtObject = buildMethod.invoke(builder);

                return classInstance.cast(builtObject);
            } catch (Exception e) {
                System.err.println("\n=== Error in mapping process ===");
                throw new RuntimeException("Error mapping ResultSet to " + classInstance.getSimpleName() +
                        ". Details: " + e.getMessage(), e);
            }
        };
    };
}