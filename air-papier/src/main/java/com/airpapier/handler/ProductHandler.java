package com.airpapier.handler;

import com.airpapier.doa.ProductDoa;
import com.airpapier.lib.Context;
import com.airpapier.lib.ErrorResponse;
import com.airpapier.lib.SuccessResponse;
import com.airpapier.model.Product;
import java.io.IOException;
import java.math.BigDecimal;
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
        productDoa.createProduct(newProduct);
        ctx.response().json(new SuccessResponse("Product successfully created"), 200);
    }

    public void updateProduct(Context ctx) throws SQLException, IOException {
        String productId = ctx.request().param("productId");
        Product updatedProduct = ctx.request().body(Product.class);

        Map<String, Object> existingProductMaps = productDoa.getProductById(productId).get(0);
        if (existingProductMaps.isEmpty()) {
            ctx.response().json(new ErrorResponse("Product not found"), 404);
            return;
        }

        Product existingProduct = Product.builder()
                .id((String) existingProductMaps.get("id"))
                .reference((String) existingProductMaps.get("reference"))
                .price((BigDecimal) existingProductMaps.get("price"))
                .quantity((Integer) existingProductMaps.get("quantity"))
                .category_id((String) existingProductMaps.get("category_id"))
                .build();

        if (updatedProduct.getReference() != null) {
            existingProduct.setReference(updatedProduct.getReference());
        }
        if (updatedProduct.getPrice() != null) {
            existingProduct.setPrice(updatedProduct.getPrice());
        }
        if (updatedProduct.getQuantity() != null) {
            existingProduct.setQuantity(updatedProduct.getQuantity());
        }
        if (updatedProduct.getCategory_id() != null) {
            existingProduct.setCategory_id(updatedProduct.getCategory_id());
        }

        productDoa.updateProduct(productId, existingProduct);
        ctx.response().json(new SuccessResponse("Product successfully updated"), 200);
    }

    public void deleteProduct(Context ctx) throws SQLException, IOException {
        String productId = ctx.request().param("productId");

        List<Map<String, Object>> existingProduct = productDoa.getProductById(productId);
        if (existingProduct.isEmpty()) {
            ctx.response().json(new ErrorResponse("Product not found"), 404);
            return;
        }

        productDoa.deleteProduct(productId);
        ctx.response().json(new SuccessResponse("Product deleted successfully"), 200);
    }

}
