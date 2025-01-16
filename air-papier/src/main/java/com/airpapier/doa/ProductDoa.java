package com.airpapier.doa;

import com.airpapier.database.DataBaseConnection;
import com.airpapier.model.Product;
import java.sql.*;
import java.util.List;
import java.util.Map;

//In-band SQL Injection

public class ProductDoa {
    public List<Map<String, Object>> getAllProducts() throws SQLException{
        String query = "SELECT * FROM products";
        return DataBaseConnection.getResults(query);
    }

    public List<Map<String, Object>> getProductById(String id) throws SQLException{
        String query = "SELECT * FROM products WHERE id = '" + id + "'";
        return DataBaseConnection.getResults(query);
    }

    public List<Map<String, Object>> createProduct(Product product) throws SQLException {
        String query = "INSERT INTO products (reference, price, quantity, category_id) VALUES ('" +
                product.getReference() + "', " +
                product.getPrice() + ", " +
                product.getQuantity() + ", " +
                product.getCategory_id() + ")";

        return DataBaseConnection.getResults(query);
    }

    public List<Map<String, Object>> updateProduct(String id, Product product) throws SQLException {
        String query = "UPDATE products SET reference = '" + product.getReference() +
                "', price = " + product.getPrice() +
                ", quantity = " + product.getQuantity() +
                ", category_id = " + product.getCategory_id() +
                " WHERE id = '" + id + "'";
        return DataBaseConnection.getResults(query);
    }

    public void deleteProduct(String id) throws SQLException {
        String query = "DELETE FROM products WHERE id = '" + id + "'";

        DataBaseConnection.getResults(query);
    }
}
