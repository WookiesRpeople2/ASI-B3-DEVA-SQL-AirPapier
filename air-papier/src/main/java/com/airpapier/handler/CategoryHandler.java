package com.airpapier.handler;

import com.airpapier.doa.CategoryDoa;
import com.airpapier.lib.Context;
import com.airpapier.lib.ErrorResponse;
import com.airpapier.lib.SuccessResponse;
import com.airpapier.model.Category;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CategoryHandler {
    private final CategoryDoa categoryDoa = new CategoryDoa();

    public void getAllCategories(Context ctx) throws SQLException, IOException {
        List<Map<String, Object>> categories = categoryDoa.getAllCategories();
        ctx.response().json(categories, 200);
    }

    public void getCategoryById(Context ctx) throws SQLException, IOException {
        List<Map<String, Object>> category = categoryDoa.getCategoryById(ctx.request().param("categoryId"));
        ctx.response().json(category, 200);
    }

    public void createCategory(Context ctx) throws SQLException, IOException {
        Category newCategory = ctx.request().body(Category.class);
        List<Map<String, Object>> createdProduct = categoryDoa.createCategory(newCategory);
        ctx.response().json(createdProduct, 200);
    }

    public void updateCategory(Context ctx) throws SQLException, IOException {
        String categoryId = ctx.request().param("categoryId");
        Category updatedCategory = ctx.request().body(Category.class);

        List<Map<String, Object>> existingCategory = categoryDoa.getCategoryById(categoryId);
        if (existingCategory.get(0) == null) {
            ctx.response().json(new ErrorResponse("Product not found"), 404);
            return;
        }

        for (Map<String, Object> row : existingCategory) {
            if (row.containsKey("name") && updatedCategory.getName() != null) {
                row.put("name", updatedCategory.getName());
            }
            if (row.containsKey("description") && updatedCategory.getDescription() != null) {
                row.put("description", updatedCategory.getDescription());
            }
        }

        List<Map<String, Object>> result = categoryDoa.updateCategory(categoryId, (Category) existingCategory.get(0));
        ctx.response().json(result, 200);
    }

    public void deleteCategory(Context ctx) throws SQLException, IOException {
        String categoryId = ctx.request().param("categoryId");

        List<Map<String, Object>> existingCategory = categoryDoa.getCategoryById(categoryId);
        if (existingCategory.get(0) == null) {
            ctx.response().json(new ErrorResponse("Product not found"), 404);
            return;
        }

        categoryDoa.deleteCategory(categoryId);
        ctx.response().json(new SuccessResponse("Product deleted successfully"), 200);
    }
}
