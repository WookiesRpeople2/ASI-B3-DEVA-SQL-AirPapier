package com.airpapier.routes;

import com.airpapier.handler.OrderHandler;
import com.airpapier.handler.ProductHandler;
import com.airpapier.lib.Router;
import com.sun.net.httpserver.HttpServer;

public class OrderRoutes extends Router {
    public OrderRoutes(HttpServer server) {
        super(server);
        OrderHandler orderHandler = new OrderHandler();
        get("/", orderHandler::getAllOrders);
        get("/:orderId", orderHandler::getOrderById);
        post("/", orderHandler::createOrder);
        patch("/:orderId", orderHandler::updateOrder);
        delete("/:orderId", orderHandler::deleteOrder);

    }
}
