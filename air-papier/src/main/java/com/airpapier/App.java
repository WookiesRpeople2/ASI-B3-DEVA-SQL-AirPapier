package com.airpapier;

import com.airpapier.database.DataBaseConnection;
import com.airpapier.lib.Router;
import com.airpapier.middleware.Headers;
import com.airpapier.routes.CategoryRoutes;
import com.airpapier.routes.ProductRoutes;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App
{
    static {
        DataBaseConnection.initializeConnection();
    }

    public static void main( String[] args ) throws IOException {
        int port = Config.getInstance().getPort().getPortNumber();
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        Router router = new Router(server);

        router.useMiddleware(new Headers());
        router.combineRoutes("/api/products", new ProductRoutes(server));
        router.combineRoutes("/api/categories", new CategoryRoutes(server));

        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port " + port);
    }

}
