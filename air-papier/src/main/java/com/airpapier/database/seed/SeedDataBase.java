package com.airpapier.database.seed;

import com.airpapier.Config;
import com.airpapier.database.DataBaseConnection;
import com.airpapier.database.PreparedStatementSetter;
import com.airpapier.database.ResultSetMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SeedDataBase extends RandomDataGenerator {
    private static final int SEED_COUNT = 10;

    private static final String URL = Config.getInstance().getDb().getDbUrl();
    private static final String USER = Config.getInstance().getDb().getDbUser();
    private static final String PASSWORD = Config.getInstance().getDb().getDbPassword();
    private static final Connection connection;
    static {
        DataBaseConnection.initializeConnection();
        connection = DataBaseConnection.getConnection();
    }

    private static final List<String> clientIds = new ArrayList<>();
    private static final List<String> supplierIds = new ArrayList<>();
    private static final List<String> categoryIds = new ArrayList<>();
    private static final List<String> productIds = new ArrayList<>();

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        seedIndependentTables();
        seedProducts();
        seedProductSuppliers();
        seedOrdersAndLines();

        System.out.println("Database seeded successfully.");
    }

    private static void seedIndependentTables() throws SQLException {
        for (int i = 0; i < SEED_COUNT; i++) {
            clientIds.add(insertData("clients", new String[]{"name", "email", "telephone", "address"}, generateRandomName(),
                    generateRandomEmail(), generateRandomPhone(), generateRandomAddress()));

            supplierIds.add(insertData("suppliers", new String[]{"name", "email", "telephone"}, generateRandomName(),
                    generateRandomEmail(), generateRandomPhone()));

            categoryIds.add(insertData("categories", new String[]{"name", "description"}, generateRandomCategoryName(),
                    generateRandomDescription()));
        }
    }

    private static void seedProducts() throws SQLException {
        for (int i = 0; i < SEED_COUNT; i++) {
            String productId = insertData("products", new String[]{"reference", "price", "quantity", "category_id"}, generateRandomReference(),
                    generateRandomPrice(), generateRandomQuantity(),
                    getRandomElement(categoryIds));
            productIds.add(productId);
        }
    }

    private static void seedProductSuppliers() throws SQLException {
        for (String productId : productIds) {
            int supplierCount = RANDOM.nextInt(3) + 1;
            for (int i = 0; i < supplierCount; i++) {
                insertData("product_suppliers", new String[]{"product_id", "supplier_id", "supply_price"}, productId,
                        getRandomElement(supplierIds),
                        generateRandomPrice());
            }
        }
    }

    private static void seedOrdersAndLines() throws SQLException {
        for (int i = 0; i < SEED_COUNT; i++) {
            String orderId = insertData("orders", new String[]{"client_id", "total_price", "quantity"},
                    getRandomElement(clientIds),
                    generateRandomPrice(), generateRandomQuantity());

            seedOrderLines(orderId);
        }
    }

    private static void seedOrderLines(String orderId) throws SQLException {
        int orderLineCount = RANDOM.nextInt(5) + 1;
        for (int j = 0; j < orderLineCount; j++) {
            insertData("order_lines", new String[]{"order_id", "product_id"}, orderId,
                    getRandomElement(productIds));
        }
    }


    private static String insertData(String tableName, String[] columnNames, Object... columnValues) throws SQLException {
        if (columnNames.length != columnValues.length) {
            throw new IllegalArgumentException("The number of column names must match the number of column values.");
        }

        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ");
        sqlBuilder.append(tableName).append(" (");
        makeSqlValues(sqlBuilder, columnNames);
        sqlBuilder.append(") VALUES (");
        makeSqlValues(sqlBuilder, Stream.of(columnValues).map(value -> "?").toArray());
        sqlBuilder.append(")");

        String sql = sqlBuilder.toString();
        PreparedStatement statement = connection.prepareStatement(sql);
        PreparedStatementSetter.setParams(statement, columnValues);
        statement.executeUpdate();

        return hasIdColumn(tableName) ? selectId(tableName) : null;
    }

    private static boolean hasIdColumn(String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet columns = metaData.getColumns(null, null, tableName, "id");
        return columns.next();
    }

    private static String selectId(String tableName) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(String.valueOf("SELECT * FROM " + tableName + " ORDER BY id DESC LIMIT 1;"));
        ResultSet rs = stmt.executeQuery();
        return ResultSetMapper.mapColumn(rs, "id", String.class);
    }

    private static <T> void makeSqlValues(StringBuilder sb, T[] arr) {
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1)
                sb.append(",");
        }
    }
}