package com.airpapier.lib;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;

@AllArgsConstructor
public class Response {
    private final HttpExchange exchange;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void json(Object response, int statusCode) throws IOException {
        byte[] responseBytes = objectMapper.writeValueAsBytes(response);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

}
