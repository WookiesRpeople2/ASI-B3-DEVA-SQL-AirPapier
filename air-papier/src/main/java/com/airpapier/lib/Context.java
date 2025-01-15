package com.airpapier.lib;

import com.sun.net.httpserver.HttpExchange;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
public class Context {
    final Request request;
    final Response response;

    public Context(HttpExchange exchange){
        request = new Request(exchange);
        response = new Response(exchange);
    }
}
