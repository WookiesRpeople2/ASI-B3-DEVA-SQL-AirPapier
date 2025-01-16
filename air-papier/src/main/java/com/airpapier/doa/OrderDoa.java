package com.airpapier.doa;

import com.airpapier.database.DataBaseConnection;
import com.airpapier.model.Order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class OrderDoa {
    public List<Map<String, Object>> getAllOrders() throws SQLException {
        String query = "SELECT * FROM orders";
        return DataBaseConnection.getResults(query);
    }

    public List<Map<String, Object>> getOrderById(String id) throws SQLException{
        String query = "SELECT * FROM orders WHERE id = '" + id + "'";
        return DataBaseConnection.getResults(query);
    }

    public List<Map<String, Object>> createOrder(Order order) throws SQLException {
        String query = "INSERT INTO orders (client_id, total_price, quantity) VALUES ('" +
                order.getClient_id() + "', " +
                order.getTotal_price() + ", " +
                order.getQuantity() +
                ")";

        return DataBaseConnection.getResults(query);
    }

    public List<Map<String, Object>> updateOrder(String id, Order order) throws SQLException {
        String query = "UPDATE orders SET client_id = '" + order.getClient_id() +
                "', total_price = " + order.getTotal_price() +
                ", quantity = " + order.getQuantity() +
                ", category_id = " +
                " WHERE id = '" + id + "'";
        return DataBaseConnection.getResults(query);
    }

    public void deleteOrder(String id) throws SQLException {
        String query = "DELETE FROM orders WHERE id = '" + id + "'";

        DataBaseConnection.getResults(query);
    }
}
