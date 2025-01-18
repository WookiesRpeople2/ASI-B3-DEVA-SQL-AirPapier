package com.airpapier.routes;

import com.airpapier.handler.OrderLinesHandler;
import com.airpapier.lib.Router;
import com.sun.net.httpserver.HttpServer;

public class OrderLinesRouter extends Router {

    public OrderLinesRouter() {
        OrderLinesHandler orderLinesHandler = new OrderLinesHandler();
        get("/", orderLinesHandler::getAllProductsOrders);
        get("/:orderId", orderLinesHandler::getProductsByOrderId);
    }
}
