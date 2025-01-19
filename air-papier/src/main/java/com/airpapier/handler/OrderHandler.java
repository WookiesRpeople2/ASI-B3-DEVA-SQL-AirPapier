package com.airpapier.handler;

import com.airpapier.doa.GeneralDoa;
import com.airpapier.doa.OrderLinesDoa;
import com.airpapier.lib.Context;
import com.airpapier.lib.ErrorResponse;
import com.airpapier.lib.SuccessResponse;
import com.airpapier.model.Order;
import org.jooq.Record;
import org.jooq.Result;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderHandler {
    private final GeneralDoa<Order> orderDoa = new GeneralDoa<>("orders", Order.class);
    private final OrderLinesDoa orderLinesDoa = new OrderLinesDoa();

    public void getAllOrders(Context ctx) throws IOException {
        try {
            String start = ctx.request().query("start");
            String end = ctx.request().query("end");
            String clientId = ctx.request().query("clientId");
            String status = ctx.request().query("status");
            String productId = ctx.request().query("productId");

            if(start != null || end !=null || clientId != null || status != null || productId != null) {
                Map<String, Object> criteria = new HashMap<>();

                if (start != null) {
                    criteria.put("startDate", LocalDate.parse(start));
                }
                if (end != null) {
                    criteria.put("endDate", LocalDate.parse(end));
                }
                if (clientId != null) {
                    criteria.put("clientId", Integer.parseInt(clientId));
                }
                if (status != null) {
                    criteria.put("status", status);
                }
                if (productId != null) {
                    criteria.put("productId", Integer.parseInt(productId));
                }

                List<Map<String, Object>> orders = orderLinesDoa.searchOrders(criteria);
                ctx.response().json(orders, 200);
                return;
            }
            List<Order> orders = orderDoa.getAll();
            if (orders.isEmpty()) {
                ctx.response().json(new SuccessResponse("No orders found"), 200);
                return;
            }
            ctx.response().json(orders, 200);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    public void getOrderById(Context ctx) throws IOException {
        try {
            String orderId = ctx.request().param("orderId");
            Order order = orderDoa.getById(orderId);
            if (order == null) {
                ctx.response().json(new ErrorResponse("Order not found"), 404);
                return;
            }
            ctx.response().json(order, 200);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    public void createOrder(Context ctx) throws IOException {
        try {
            Order newOrder = ctx.request().body(Order.class);
            orderDoa.create(newOrder);
            ctx.response().json(new SuccessResponse("Order successfully created", newOrder), 201);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    public void updateOrder(Context ctx) throws IOException {
        try {
            String orderId = ctx.request().param("orderId");
            Order updatedOrder = ctx.request().body(Order.class);
            if (updatedOrder == null) {
                ctx.response().json(new ErrorResponse("Request body is required"), 400);
                return;
            }

            Order existingOrder = orderDoa.getById(orderId);
            if (existingOrder == null) {
                ctx.response().json(new ErrorResponse("Order not found"), 404);
                return;
            }

            updateOrderFields(existingOrder, updatedOrder);

            orderDoa.update(orderId, existingOrder);
            ctx.response().json(new SuccessResponse("Order updated successfully", existingOrder), 200);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    public void deleteOrder(Context ctx) throws IOException {
        try {
            String orderId = ctx.request().param("orderId");

            Order existingOrder = orderDoa.getById(orderId);
            if (existingOrder == null) {
                ctx.response().json(new ErrorResponse("Order not found"), 404);
                return;
            }

            orderDoa.delete(orderId);
            ctx.response().json(new SuccessResponse("Order deleted successfully"), 200);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    private void updateOrderFields(Order existingOrder, Order updatedOrder) {
        if (updatedOrder.getClient_id() != null) {
            existingOrder.setClient_id(updatedOrder.getClient_id());
        }
        if (updatedOrder.getTotal_price() != null) {
            existingOrder.setTotal_price(updatedOrder.getTotal_price());
        }
        if (updatedOrder.getQuantity() != null) {
            existingOrder.setQuantity(updatedOrder.getQuantity());
        }
    }

}
