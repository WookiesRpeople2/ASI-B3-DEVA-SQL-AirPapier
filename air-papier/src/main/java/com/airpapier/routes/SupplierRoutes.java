package com.airpapier.routes;

import com.airpapier.handler.SupplierHandler;
import com.airpapier.lib.Router;
import com.sun.net.httpserver.HttpServer;

public class SupplierRoutes extends Router {

    public SupplierRoutes(HttpServer server) {
        super(server);
        SupplierHandler supplierHandler = new SupplierHandler();
        get("/", supplierHandler::getAllSuppliers);
        get("/:productId", supplierHandler::getSupplierById);
        post("/", supplierHandler::createSupplier);
        patch("/:productId", supplierHandler::updateSupplier);
        delete("/:productId", supplierHandler::deleteSupplier);

    }
}
