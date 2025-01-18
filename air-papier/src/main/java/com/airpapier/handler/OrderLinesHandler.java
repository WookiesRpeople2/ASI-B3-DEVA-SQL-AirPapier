package com.airpapier.handler;

import com.airpapier.doa.OrderLinesDoa;
import com.airpapier.lib.Context;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class OrderLinesHandler {
    private final OrderLinesDoa orderLinesDoa = new OrderLinesDoa();

    public void getAllProductsOrders(Context ctx) throws SQLException, IOException {
        List<Map<String, Object>> productsOrders = orderLinesDoa.getAllOrdersWithProducts();
        ctx.response().json(productsOrders, 200);
    }

    public void getProductsByOrderId(Context ctx) throws SQLException, IOException {
            String orderId = ctx.request().param("orderId");
            List<Map<String, Object>> products = orderLinesDoa.getProductsByOrderId(orderId);
            ctx.response().json(products, 200);
    }
}
