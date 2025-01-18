package com.airpapier.routes;

import com.airpapier.handler.ProductHandler;
import com.airpapier.lib.Router;
import com.sun.net.httpserver.HttpServer;

public class ProductRoutes extends Router {

    public ProductRoutes() {
        ProductHandler productHandler = new ProductHandler();
        get("/", productHandler::getAllProducts);
        get("/:productId", productHandler::getProductById);
        post("/", productHandler::createProduct);
        patch("/:productId", productHandler::updateProduct);
        delete("/:productId", productHandler::deleteProduct);

    }
}