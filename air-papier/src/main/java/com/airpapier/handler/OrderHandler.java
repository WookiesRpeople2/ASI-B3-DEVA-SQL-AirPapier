package com.airpapier.handler;

import com.airpapier.doa.OrderDoa;
import com.airpapier.lib.Context;
import com.airpapier.lib.ErrorResponse;
import com.airpapier.lib.SuccessResponse;
import com.airpapier.model.Order;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class OrderHandler {
    private final OrderDoa orderDoa = new OrderDoa();

    public void getAllOrders(Context ctx) throws SQLException, IOException {
        List<Map<String, Object>> orders = orderDoa.getAllOrders();
        ctx.response().json(orders, 200);
    }

    public void getOrderById(Context ctx) throws SQLException, IOException {
        List<Map<String, Object>> order = orderDoa.getOrderById(ctx.request().param("orderId"));
        ctx.response().json(order, 200);
    }

    public void createOrder(Context ctx) throws SQLException, IOException {
        Order newOrder = ctx.request().body(Order.class);
        orderDoa.createOrder(newOrder);
        ctx.response().json(new SuccessResponse("Order successfully created"), 200);
    }

    public void updateOrder(Context ctx) throws SQLException, IOException {
        String orderId = ctx.request().param("orderId");
        Order updatedOrder = ctx.request().body(Order.class);

        Map<String, Object> existingOrderMap = orderDoa.getOrderById(orderId).get(0);
        if (existingOrderMap.isEmpty()) {
            ctx.response().json(new ErrorResponse("Order not found"), 404);
            return;
        }

        Order existingOrder = Order.builder()
                .id((String) existingOrderMap.get("id"))
                .client_id((String) existingOrderMap.get("client_id"))
                .total_price((BigDecimal) existingOrderMap.get("total_price"))
                .quantity((Integer) existingOrderMap.get("quantity"))
                .build();

        if (updatedOrder.getClient_id() != null) {
            existingOrder.setClient_id(updatedOrder.getClient_id());
        }
        if (updatedOrder.getTotal_price() != null) {
            existingOrder.setTotal_price(updatedOrder.getTotal_price());
        }
        if (updatedOrder.getQuantity() != null) {
            existingOrder.setQuantity(updatedOrder.getQuantity());
        }

        orderDoa.updateOrder(orderId, existingOrder);
        ctx.response().json(new SuccessResponse("Order successfully Updated"), 200);
    }

    public void deleteOrder(Context ctx) throws SQLException, IOException {
        String orderId = ctx.request().param("orderId");

        List<Map<String, Object>> existingOrder = orderDoa.getOrderById(orderId);
        if (existingOrder.get(0) == null) {
            ctx.response().json(new ErrorResponse("Order not found"), 404);
            return;
        }

        orderDoa.deleteOrder(orderId);
        ctx.response().json(new SuccessResponse("Order deleted successfully"), 200);
    }

}
