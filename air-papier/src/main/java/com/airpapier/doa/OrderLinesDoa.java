package com.airpapier.doa;

import com.airpapier.database.DataBaseConnection;
import com.airpapier.lib.Context;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class OrderLinesDoa {

    public List<Map<String, Object>> getAllOrdersWithProducts() throws SQLException {
        String query = """
            SELECT p.*, o.id as order_id, o.client_id, o.total_price as order_total_price, o.quantity as order_quantity
            FROM products p
            LEFT JOIN order_lines ol ON p.id = ol.product_id
            LEFT JOIN orders o ON ol.order_id = o.id
            """;
        return DataBaseConnection.getResults(query);
    }

    public List<Map<String, Object>> getProductsByOrderId(String orderId) throws SQLException {
        String query = """
            SELECT p.*, ol.order_id, o.client_id, o.total_price as order_total_price, o.quantity as order_quantity
            FROM products p
            LEFT JOIN order_lines ol ON p.id = ol.product_id
            LEFT JOIN orders o ON ol.order_id = o.id
            WHERE o.id = '""" + orderId + "'";
        return DataBaseConnection.getResults(query);
    }
}
