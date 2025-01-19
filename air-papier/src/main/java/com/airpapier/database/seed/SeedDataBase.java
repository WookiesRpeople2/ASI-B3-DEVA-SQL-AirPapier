package com.airpapier.database.seed;

import com.airpapier.database.DataBaseConnection;
import org.jooq.DSLContext;
import org.jooq.Record1;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.jooq.impl.DSL.*;

public class SeedDataBase extends RandomDataGenerator {
    private static final int SEED_COUNT = 10;
    private static final List<String> clientIds = new ArrayList<>();
    private static final List<String> supplierIds = new ArrayList<>();
    private static final List<String> categoryIds = new ArrayList<>();
    private static final List<String> productIds = new ArrayList<>();
    private static DSLContext dsl;

    public static void main(String[] args) {
        DataBaseConnection.getInstance().initializeConnection();
        dsl = DataBaseConnection.getDsl();

        seedIndependentTables();
        seedProducts();
        seedProductSuppliers();
        seedOrdersAndLines();

        System.out.println("Database seeded successfully.");
    }

    private static void seedIndependentTables() {
        for (int i = 0; i < SEED_COUNT; i++) {
            clientIds.add(insertData("clients", Map.of(
                    "name", generateRandomName(),
                    "email", generateRandomEmail(),
                    "telephone", generateRandomPhone(),
                    "address", generateRandomAddress()
            )));

            supplierIds.add(insertData("suppliers", Map.of(
                    "name", generateRandomName(),
                    "email", generateRandomEmail(),
                    "telephone", generateRandomPhone()
            )));

            categoryIds.add(insertData("categories", Map.of(
                    "name", generateRandomCategoryName(),
                    "description", generateRandomDescription()
            )));
        }
    }

    private static void seedProducts() {
        for (int i = 0; i < SEED_COUNT; i++) {
            String productId = insertData("products", Map.of(
                    "reference", generateRandomReference(),
                    "price", generateRandomPrice(),
                    "quantity", generateRandomQuantity(),
                    "category_id", getRandomElement(categoryIds)
            ));
            productIds.add(productId);
        }
    }

    private static void seedProductSuppliers() {
        for (String productId : productIds) {
            int supplierCount = RANDOM.nextInt(3) + 1;
            for (int i = 0; i < supplierCount; i++) {
                insertData("product_suppliers", Map.of(
                        "product_id", productId,
                        "supplier_id", getRandomElement(supplierIds),
                        "supply_price", generateRandomPrice()
                ));
            }
        }
    }

    private static void seedOrdersAndLines() {
        for (int i = 0; i < SEED_COUNT; i++) {
            String orderId = insertData("orders", Map.of(
                    "client_id", getRandomElement(clientIds),
                    "total_price", generateRandomPrice(),
                    "quantity", generateRandomQuantity()
            ));

            seedOrderLines(orderId);
        }
    }

    private static void seedOrderLines(String orderId) {
        int orderLineCount = RANDOM.nextInt(5) + 1;
        for (int j = 0; j < orderLineCount; j++) {
            insertData("order_lines", Map.of(
                    "order_id", orderId,
                    "product_id", getRandomElement(productIds),
                    "status", generateRandomStatus()
            ));
        }
    }

    private static String insertData(String tableName, Map<String, Object> values) {
        dsl.insertInto(table(tableName))
                .set(values)
                .execute();

        if (hasIdColumn(tableName)) {
            Record1<Object> result = dsl.select(field("id"))
                    .from(tableName)
                    .orderBy(field("id").desc())
                    .limit(1)
                    .fetchOne();

            return result != null ? (String) result.value1() : null;
        }

        return null;
    }

    private static boolean hasIdColumn(String tableName) {
        return dsl.meta()
                .getTables(tableName)
                .stream()
                .flatMap(table -> Arrays.stream(table.fields()))
                .anyMatch(field -> field.getName().equalsIgnoreCase("id"));
    }
}