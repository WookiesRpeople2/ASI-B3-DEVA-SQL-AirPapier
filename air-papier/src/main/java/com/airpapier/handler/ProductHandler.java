package com.airpapier.handler;

import com.airpapier.doa.ProductDoa;
import com.airpapier.lib.Context;
import com.airpapier.lib.ErrorResponse;
import com.airpapier.lib.SuccessResponse;
import com.airpapier.model.Product;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ProductHandler {
    private final ProductDoa productDoa = new ProductDoa();

    public void getAllProducts(Context ctx) throws SQLException, IOException {
            List<Map<String, Object>> products = productDoa.getAllProducts();
            ctx.response().json(products, 200);
    }

    public void getProductById(Context ctx) throws SQLException, IOException {
        List<Map<String, Object>> product = productDoa.getProductById(ctx.request().param("productId"));
        ctx.response().json(product, 200);
    }

    public void createProduct(Context ctx) throws SQLException, IOException {
        Product newProduct = ctx.request().body(Product.class);
        List<Map<String, Object>> createdProduct = productDoa.createProduct(newProduct);
        ctx.response().json(createdProduct, 200);
    }

    public void updateProduct(Context ctx) throws SQLException, IOException {
        String productId = ctx.request().param("productId");
        Product updatedProduct = ctx.request().body(Product.class);

        List<Map<String, Object>> existingProduct = productDoa.getProductById(productId);
        if (existingProduct.get(0) == null) {
            ctx.response().json(new ErrorResponse("Product not found"), 404);
            return;
        }

        for (Map<String, Object> row : existingProduct) {
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

        List<Map<String, Object>> result = productDoa.updateProduct(productId, (Product)existingProduct.get(0));
        ctx.response().json(result, 200);
    }

    public void deleteProduct(Context ctx) throws SQLException, IOException {
        String productId = ctx.request().param("productId");

        List<Map<String, Object>> existingProduct = productDoa.getProductById(productId);
        if (existingProduct.get(0) == null) {
            ctx.response().json(new ErrorResponse("Product not found"), 404);
            return;
        }

        productDoa.deleteProduct(productId);
        ctx.response().json(new SuccessResponse("Product deleted successfully"), 200);
    }

}
