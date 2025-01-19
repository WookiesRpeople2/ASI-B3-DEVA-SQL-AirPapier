package com.airpapier.handler;

import com.airpapier.doa.GeneralDoa;
import com.airpapier.doa.OrderLinesDoa;
import com.airpapier.lib.Context;
import com.airpapier.lib.ErrorResponse;
import com.airpapier.lib.SuccessResponse;
import com.airpapier.model.Order;
import com.airpapier.model.OrderLines;
import com.airpapier.model.Product;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ProductHandler {
    private final GeneralDoa<Product> productDoa = new GeneralDoa<>("products", Product.class);
    private final OrderLinesDoa orderLinesDoa = new OrderLinesDoa();

    public void getAllProducts(Context ctx) throws IOException {
        try {
            List<Product> products = productDoa.getAll();
            if (products.isEmpty()) {
                ctx.response().json(new SuccessResponse("No products found"), 200);
                return;
            }
            ctx.response().json(products, 200);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    public void getTop(Context ctx) throws IOException {
        try {
            LocalDate today = LocalDate.now();
            LocalDate lastMonth = today.minusMonths(1);
            LocalDate thisMonth = today.withDayOfMonth(today.lengthOfMonth());
            List<Map<String, Object>> products = orderLinesDoa.getTopSellingProducts(lastMonth, thisMonth);
            if (products.isEmpty()) {
                ctx.response().json(new ErrorResponse("Product not found"), 404);
                return;
            }
            ctx.response().json(products, 200);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    public void getLowStock(Context ctx) throws IOException {
        try {
            List<Map<String, Object>> products = orderLinesDoa.getLowStockProducts(4);
            if (products.isEmpty()) {
                ctx.response().json(new ErrorResponse("no Products"), 404);
                return;
            }
            ctx.response().json(products, 200);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    public void getProductById(Context ctx) throws IOException {
        try {
            String productId = ctx.request().param("productId");
            Product product = productDoa.getById(productId);
            if (product == null) {
                ctx.response().json(new ErrorResponse("Product not found"), 404);
                return;
            }
            ctx.response().json(product, 200);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    public void getOrdersByProductId(Context ctx) throws IOException {
        try {
            String productId = ctx.request().param("productId");
            List<Map<String, Object>> order = orderLinesDoa.getOrdersByProduct(productId);
            if (order == null) {
                ctx.response().json(new ErrorResponse("Product not found"), 404);
                return;
            }
            ctx.response().json(order, 200);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }


    public void createProduct(Context ctx) throws IOException {
        try {
            Product newProduct = ctx.request().body(Product.class);
            productDoa.create(newProduct);
            ctx.response().json(new SuccessResponse("Product created successfully", newProduct), 201);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    public void updateProduct(Context ctx) throws IOException {
        try {
            String productId = ctx.request().param("productId");
            Product updatedProduct = ctx.request().body(Product.class);
            if (updatedProduct == null) {
                ctx.response().json(new ErrorResponse("Request body is required"), 400);
                return;
            }

            Product existingProduct = productDoa.getById(productId);
            if (existingProduct == null) {
                ctx.response().json(new ErrorResponse("Product not found"), 404);
                return;
            }

            updateProductFields(existingProduct, updatedProduct);

            try {
                productDoa.update(productId, existingProduct);
                ctx.response().json(new SuccessResponse("Product updated successfully", existingProduct), 200);
            } catch (Exception e) {
                ctx.response().json(new ErrorResponse("Failed to update product: " + e.getMessage()), 500);
            }
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    public void buy(Context ctx) throws IOException {
        String productId = ctx.request().param("productId");
        OrderLines orderLines = ctx.request().body(OrderLines.class);
        Map<String, Object> existingOrderMap = orderLinesDoa.getOrderFromOrderLines(orderLines.getOrder_id()).get(0);
        if (existingOrderMap.isEmpty()) {
            ctx.response().json(new ErrorResponse("Product not found"), 404);
            return;
        }

        Order order = Order.builder()
                .id((String) existingOrderMap.get("id"))
                .client_id((String) existingOrderMap.get("client_id"))
                .total_price((BigDecimal) existingOrderMap.get("total_price"))
                .quantity((Integer) existingOrderMap.get("quantity"))
                .build();

        if(orderLinesDoa.checkAndUpdateStock(productId, order.getQuantity())){
            ctx.response().json(order, 200);
        }else{
            ctx.response().json(new ErrorResponse("This order can not be processed"), 200);
        }
    }

    public void deleteProduct(Context ctx) throws IOException {
        try {
            String productId = ctx.request().param("productId");
            Product existingProduct = productDoa.getById(productId);
            if (existingProduct == null) {
                ctx.response().json(new ErrorResponse("Product not found"), 404);
                return;
            }

            try {
                productDoa.delete(productId);
                ctx.response().json(new SuccessResponse("Product deleted successfully"), 200);
            } catch (Exception e) {
                ctx.response().json(new ErrorResponse("Failed to delete product: " + e.getMessage()), 500);
            }
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    private void updateProductFields(Product existingProduct, Product updatedProduct) {
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
    }
}
