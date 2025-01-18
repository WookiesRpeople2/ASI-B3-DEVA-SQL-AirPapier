package com.airpapier.doa;

import com.airpapier.database.DataBaseConnection;
import com.airpapier.model.Category;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

//time based SQL Injection

public class CategoryDoa {
    public List<Map<String, Object>> getAllCategories() throws SQLException{
        String query = "SELECT * FROM categories";
        return DataBaseConnection.getResults(query);
    }

    public List<Map<String, Object>> getCategoryById(String id) throws SQLException{
        String query = "SELECT * FROM categories WHERE id = '" + id + "'";
        return DataBaseConnection.getResults(query);
    }

    public void createCategory(Category category) throws SQLException {
        String query = "INSERT INTO categories (name, description) VALUES ('" +
                category.getName() + "', '" +
                category.getDescription() +
                "')";
        DataBaseConnection.getResults(query);
    }

    public void updateCategory(String id, Category category) throws SQLException {
        String query = "UPDATE categories SET name = '" + category.getName() + "'" +
                ", description = '" + category.getDescription() + "'" +
                " WHERE id = '" + id + "'";
        DataBaseConnection.getResults(query);
    }

    public void deleteCategory(String id) throws SQLException {
        String query = "DELETE FROM products WHERE id = '" + id + "'";
        DataBaseConnection.getResults(query);
    }
}
