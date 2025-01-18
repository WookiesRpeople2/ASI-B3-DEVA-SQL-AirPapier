package com.airpapier.routes;

import com.airpapier.handler.SupplierHandler;
import com.airpapier.lib.Router;
import com.sun.net.httpserver.HttpServer;

public class SupplierRoutes extends Router {

    public SupplierRoutes() {
        SupplierHandler supplierHandler = new SupplierHandler();
        get("/", supplierHandler::getAllSuppliers);
        get("/:supplierId", supplierHandler::getSupplierById);
        post("/", supplierHandler::createSupplier);
        patch("/:supplierId", supplierHandler::updateSupplier);
        delete("/:supplierId", supplierHandler::deleteSupplier);

    }
}
