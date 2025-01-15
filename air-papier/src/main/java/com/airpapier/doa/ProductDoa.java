package com.airpapier.doa;

import com.airpapier.database.DataBaseConnection;
import com.airpapier.database.ResultSetMapper;
import com.airpapier.interfaces.RowMapper;
import com.airpapier.model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//In-band SQL Injection

public class ProductDoa {
    private final RowMapper<Product> productMap = ResultSetMapper.makeMapper(Product.class);
    private final Connection conn = DataBaseConnection.getConnection();

    public List<Product> getAllProducts() throws SQLException{
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";

        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        return ResultSetMapper.mapToList(rs, productMap);
    }

    public List<Map<String, Object>> getProductById(String id) throws SQLException{
        String query = "SELECT * FROM products WHERE id = '" + id + "'";
        return DataBaseConnection.getResaults(query);
    }

    public List<Map<String, Object>> createProduct(Product product) throws SQLException {
        String query = "INSERT INTO products (reference, price, quantity, category_id) VALUES ('" +
                product.getReference() + "', " +
                product.getPrice() + ", " +
                product.getQuantity() + ", " +
                product.getCategory_id() + ")";

        return DataBaseConnection.getResaults(query);
    }

    public List<Map<String, Object>> updateProduct(String id, List<Map<String, Object>> product) throws SQLException {
        String query = "UPDATE products SET reference = '" + product.get(0).get("reference") +
                "', price = " + product.get(0).get("price") +
                ", quantity = " + product.get(0).get("quantity") +
                ", category_id = " + product.get(0).get("category_id") +
                " WHERE id = '" + id + "'";
        return DataBaseConnection.getResaults(query);
    }

    public List<Map<String, Object>>  deleteProduct(String id) throws SQLException {
        String query = "DELETE FROM products WHERE id = '" + id + "'";

        return DataBaseConnection.getResaults(query);
    }
}
