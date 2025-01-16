package com.airpapier.handler;

import com.airpapier.doa.CategoryDoa;
import com.airpapier.doa.ProductDoa;
import com.airpapier.lib.Context;
import com.airpapier.lib.ErrorResponse;
import com.airpapier.lib.SuccessResponse;
import com.airpapier.model.Category;
import com.airpapier.model.Product;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CategoryHandler {
    private final CategoryDoa categoryDoa = new CategoryDoa();

    public void getAllCategories(Context ctx) throws SQLException, IOException {
            List<Map<String, Object>> products = categoryDoa.getAllCaegories();
            ctx.response().json(products, 200);
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
        Product updatedProduct = ctx.request().body(Product.class);

        List<Map<String, Object>> existingCategory = categoryDoa.getCategoryById(categoryId);
        if (existingCategory.get(0) == null) {
            ctx.response().json(new ErrorResponse("Product not found"), 404);
            return;
        }

        for (Map<String, Object> row : existingCategory) {
            if (row.containsKey("reference") && updatedProduct.getReference() != null) {
                row.put("reference", updatedProduct.getReference());
            }
            if (row.containsKey("price") && updatedProduct.getPrice() != null) {
                row.put("price", updatedProduct.getPrice());
            }
            if (row.containsKey("quantity") && updatedProduct.getQuantity() != null) {
                row.put("quantity", updatedProduct.getQuantity());
            }
            if (row.containsKey("category_id") && updatedProduct.getCategory_id() != null) {
                row.put("category_id", updatedProduct.getCategory_id());
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
