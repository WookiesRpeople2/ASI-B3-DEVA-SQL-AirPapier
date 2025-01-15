package com.airpapier.interfaces;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

public interface Middleware {
    void apply(HttpExchange exchange) throws IOException;
}