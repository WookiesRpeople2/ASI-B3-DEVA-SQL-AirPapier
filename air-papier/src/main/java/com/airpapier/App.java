package com.airpapier;

import com.airpapier.database.DataBaseConnection;
import com.airpapier.lib.Router;
import com.airpapier.lib.Server;
import com.airpapier.middleware.Headers;
import com.airpapier.routes.*;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;


public class App
{
    public static void main( String[] args ) throws IOException {
        DataBaseConnection.getInstance().initializeConnection();
        int port = Config.getInstance().getPort().getPortNumber();
        HttpServer server = Server.CreateServer(port);
        Router router = new Router();

        router.useMiddleware(new Headers());
        router.combineRoutes("/api/products", new ProductRoutes());
        router.combineRoutes("/api/categories", new CategoryRoutes());
        router.combineRoutes("/api/suppliers", new SupplierRoutes());
        router.combineRoutes("/api/clients", new ClientRouter());
        router.combineRoutes("/api/orders", new OrderRoutes());
        router.combineRoutes("/api/orderLines", new OrderLinesRouter());

        Server.listen();
        System.out.println("Server started on port " + port);
    }

}
