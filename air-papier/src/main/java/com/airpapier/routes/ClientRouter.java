package com.airpapier.routes;

import com.airpapier.handler.ClientHandler;
import com.airpapier.handler.ProductHandler;
import com.airpapier.lib.Router;
import com.sun.net.httpserver.HttpServer;

public class ClientRouter extends Router {

    public ClientRouter(){
        ClientHandler clientHandler = new ClientHandler();
        get("/", clientHandler::getAllClients);
        get("/:clientId", clientHandler::getClientById);
        get("/:clientId/orders", clientHandler::getOrderByClientId);
        post("/", clientHandler::createClient);
        patch("/:clientId", clientHandler::updateClient);
        delete("/:clientId", clientHandler::deleteClient);
    }
}
