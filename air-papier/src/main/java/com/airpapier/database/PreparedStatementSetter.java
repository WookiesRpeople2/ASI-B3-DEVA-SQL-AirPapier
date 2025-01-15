package com.airpapier.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.sql.Types;

public class PreparedStatementSetter {
    public static void setParams(PreparedStatement ps, Object... params) throws SQLException {
        if (params == null) return;

        for (int i = 0; i < params.length; i++) {
            setParameter(ps, i + 1, params[i]);
        }

    }

    private static void setParameter(PreparedStatement ps, int index, Object param) throws SQLException {
        if (param == null) {
            ps.setNull(index, Types.NULL);
            return;
        }

        switch (param.getClass().getSimpleName()) {
            case "String":
                ps.setString(index, (String) param);
                break;
            case "Integer":
                ps.setInt(index, (Integer) param);
                break;
            case "Long":
                ps.setLong(index, (Long) param);
                break;
            case "Double":
                ps.setDouble(index, (Double) param);
                break;
            case "Boolean":
                ps.setBoolean(index, (Boolean) param);
                break;
            case "Date":
                ps.setTimestamp(index, new java.sql.Timestamp(((Date) param).getTime()));
                break;
            default:
                ps.setObject(index, param);
        }
    }
}