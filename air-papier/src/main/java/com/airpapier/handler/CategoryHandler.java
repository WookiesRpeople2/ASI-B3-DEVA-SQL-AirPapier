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
        categoryDoa.createCategory(newCategory);
        ctx.response().json(new SuccessResponse("Category successfully created"), 200);
    }

    public void updateCategory(Context ctx) throws SQLException, IOException {
        String categoryId = ctx.request().param("categoryId");
        Category updatedCategory = ctx.request().body(Category.class);

        Map<String, Object> existingCategoryMap = categoryDoa.getCategoryById(categoryId).get(0);

        if (existingCategoryMap.isEmpty()) {
            ctx.response().json(new ErrorResponse("Category not found"), 404);
            return;
        }

        Category existingCategory = Category.builder()
                .id((String) existingCategoryMap.get("id"))
                .name((String) existingCategoryMap.get("name"))
                .description((String) existingCategoryMap.get("description"))
                .build();

        if (updatedCategory.getName() != null) {
            existingCategory.setName(updatedCategory.getName());
        }
        if (updatedCategory.getDescription() != null) {
            existingCategory.setDescription(updatedCategory.getDescription());
        }

        categoryDoa.updateCategory(categoryId, existingCategory);
        ctx.response().json(new SuccessResponse("Category successfully updated"), 200);
    }

    public void deleteCategory(Context ctx) throws SQLException, IOException {
        String categoryId = ctx.request().param("categoryId");

        List<Map<String, Object>> existingCategory = categoryDoa.getCategoryById(categoryId);
        if (existingCategory.get(0) == null) {
            ctx.response().json(new ErrorResponse("Category not found"), 404);
            return;
        }

        categoryDoa.deleteCategory(categoryId);
        ctx.response().json(new SuccessResponse("Category deleted successfully"), 200);
    }
}
