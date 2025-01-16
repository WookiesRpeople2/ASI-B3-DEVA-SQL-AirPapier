package com.airpapier.handler;

import com.airpapier.doa.OrderDoa;
import com.airpapier.lib.Context;
import com.airpapier.lib.ErrorResponse;
import com.airpapier.lib.SuccessResponse;
import com.airpapier.model.Order;

import java.io.IOException;
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
        List<Map<String, Object>> createdOrder = orderDoa.createOrder(newOrder);
        ctx.response().json(createdOrder, 200);
    }

    public void updateOrder(Context ctx) throws SQLException, IOException {
        String orderId = ctx.request().param("orderId");
        Order updatedOrder = ctx.request().body(Order.class);

        List<Map<String, Object>> existingOrder = orderDoa.getOrderById(orderId);
        if (existingOrder.get(0) == null) {
            ctx.response().json(new ErrorResponse("Order not found"), 404);
            return;
        }

        for (Map<String, Object> row : existingOrder) {
            if (row.containsKey("client_id") && updatedOrder.getClient_id() != null) {
                row.put("client_id", updatedOrder.getClient_id());
            }
            if (row.containsKey("total_price") && updatedOrder.getTotal_price() != null) {
                row.put("total_price", updatedOrder.getTotal_price());
            }
            if (row.containsKey("quantity") && updatedOrder.getQuantity() != null) {
                row.put("quantity", updatedOrder.getQuantity());
            }
        }

        List<Map<String, Object>> result = orderDoa.updateOrder(orderId, (Order)existingOrder.get(0));
        ctx.response().json(result, 200);
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
