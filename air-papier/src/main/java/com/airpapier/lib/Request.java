package com.airpapier.lib;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import lombok.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class Request {
    private final HttpExchange exchange;
    @Setter private static Map<String, String> pathParams = new HashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public String param(String find){
        return pathParams.get(find);
    }

    public String query(String paramName) {
        String query = exchange.getRequestURI().getQuery();
        if (query == null) {
            return null;
        }

        Map<String, String> queryParams = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                queryParams.put(entry[0], entry[1]);
            } else {
                queryParams.put(entry[0], "");
            }
        }

        return queryParams.get(paramName);
    }

    public <T> T body(Class<T> valueType) throws IOException {
        try (InputStream requestBody = exchange.getRequestBody()) {
            return objectMapper.readValue(requestBody, valueType);
        }
    }
}
