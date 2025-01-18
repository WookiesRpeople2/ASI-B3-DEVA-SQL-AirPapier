package com.airpapier.lib;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    public static HttpServer server;

    public static HttpServer CreateServer(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        return server;
    }

    public static void listen(){
        server.setExecutor(null);
        server.start();
    }
}
