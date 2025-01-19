package com.airpapier.routes;

import com.airpapier.handler.ProductHandler;
import com.airpapier.lib.Router;
import com.sun.net.httpserver.HttpServer;

public class ProductRoutes extends Router {

    public ProductRoutes() {
        ProductHandler productHandler = new ProductHandler();
        get("/", productHandler::getAllProducts);
        get("/notify", productHandler::getLowStock);
        get("/top", productHandler::getTop);
        get("/:productId", productHandler::getProductById);
        get("/:productId/orders", productHandler::getOrdersByProductId);
        post("/", productHandler::createProduct);
        patch("/:productId", productHandler::updateProduct);
        patch("/:productId/buy", productHandler::buy);
        delete("/:productId", productHandler::deleteProduct);

    }
}