package com.airpapier.database;

import com.airpapier.interfaces.RowMapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResultSetMapper {

    public static <T> RowMapper<T> makeMapper(Class<T> classInstance) {
        return rs -> {
            try {
                Method builderMethod = classInstance.getMethod("builder");
                Object builder = builderMethod.invoke(null);
                Field[] fields = classInstance.getDeclaredFields();

                for (Field field : fields) {
                    try {
                        String fieldName = field.getName();
                        Object value = rs.getObject(fieldName);

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


    public static <T> T mapSingle(ResultSet rs, RowMapper<T> mapper) throws SQLException {
        return rs.next() ? mapper.mapRow(rs) : null;
    }

    public static <T> List<T> mapToList(ResultSet rs, RowMapper<T> mapper) throws SQLException {
        List<T> items = new ArrayList<>();
        while (rs.next()) {
            items.add(mapper.mapRow(rs));
        }
        return items;
    }

    public static <T> T mapColumn(ResultSet rs, String columnName, Class<T> type) throws SQLException {
        if (rs.next()) {
            return rs.getObject(columnName, type);
        }
        return null;
    }

    public static <T> List<T> queryAndMap(Connection conn, String sql,
                                          RowMapper<T> mapper, Object... params) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            PreparedStatementSetter.setParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                return mapToList(rs, mapper);
            }
        }
    }

}
