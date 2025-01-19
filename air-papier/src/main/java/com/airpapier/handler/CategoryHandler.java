
package com.airpapier.handler;

import com.airpapier.doa.GeneralDoa;
import com.airpapier.lib.Context;
import com.airpapier.lib.ErrorResponse;
import com.airpapier.lib.SuccessResponse;
import com.airpapier.model.Category;

import java.io.IOException;
import java.util.List;

public class CategoryHandler {
    private final GeneralDoa<Category> categoryDoa = new GeneralDoa<>("categories", Category.class);

    public void getAllCategories(Context ctx) throws IOException {
        try {
            List<Category> categories = categoryDoa.getAll();
            if (categories.isEmpty()) {
                ctx.response().json(new SuccessResponse("No categories found"), 200);
                return;
            }
            ctx.response().json(categories, 200);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    public void getCategoryById(Context ctx) throws IOException {
        try {
            String categoryId = ctx.request().param("categoryId");

            Category category = categoryDoa.getById(categoryId);
            if (category == null) {
                ctx.response().json(new ErrorResponse("Category not found"), 404);
                return;
            }

            ctx.response().json(category, 200);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    public void createCategory(Context ctx) throws IOException {
        try {
            Category newCategory = ctx.request().body(Category.class);
            categoryDoa.create(newCategory);
            ctx.response().json(new SuccessResponse("Category successfully created", newCategory), 201);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    public void updateCategory(Context ctx) throws IOException {
        try {
            String categoryId = ctx.request().param("categoryId");
            Category updatedCategory = ctx.request().body(Category.class);
            if (updatedCategory == null) {
                ctx.response().json(new ErrorResponse("Request body is required"), 400);
                return;
            }

            Category existingCategory = categoryDoa.getById(categoryId);
            if (existingCategory == null) {
                ctx.response().json(new ErrorResponse("Category not found"), 404);
                return;
            }

            updateCategoryFields(existingCategory, updatedCategory);

            categoryDoa.update(categoryId, existingCategory);
            ctx.response().json(new SuccessResponse("Category successfully updated", existingCategory), 200);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    public void deleteCategory(Context ctx) throws IOException {
        try {
            String categoryId = ctx.request().param("categoryId");

            Category existingCategory = categoryDoa.getById(categoryId);
            if (existingCategory == null) {
                ctx.response().json(new ErrorResponse("Category not found"), 404);
                return;
            }

            categoryDoa.delete(categoryId);
            ctx.response().json(new SuccessResponse("Category deleted successfully"), 200);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    private void updateCategoryFields(Category existingCategory, Category updatedCategory) {
        if (updatedCategory.getName() != null) {
            existingCategory.setName(updatedCategory.getName());
        }
        if (updatedCategory.getDescription() != null) {
            existingCategory.setDescription(updatedCategory.getDescription());
        }
    }
}