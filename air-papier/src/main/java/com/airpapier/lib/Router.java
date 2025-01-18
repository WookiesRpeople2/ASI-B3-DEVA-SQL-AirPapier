package com.airpapier.lib;

import com.airpapier.interfaces.Handler;
import com.airpapier.interfaces.Middleware;
import com.sun.net.httpserver.HttpServer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class Router extends Server{
    @Setter private Middleware useMiddleware;
    private final RouteNode root = new RouteNode();
    private String basePath = "";

    private static class RouteNode {
        private final Map<String, RouteNode> children = new HashMap<>();
        private final Map<String, Handler> handlers = new HashMap<>();
        private String paramName;

        public void addHandler(String method, Handler handler) {
            handlers.put(method, handler);
        }
    }

    private static class RouteMatch {
        Handler handler;
        Map<String, String> params;

        RouteMatch(Handler handler, Map<String, String> params) {
            this.handler = handler;
            this.params = params;
        }
    }

    public void get(String path, Handler handler) {
        addRoute("GET", path, handler);
    }

    public void post(String path, Handler handler) {
        addRoute("POST", path, handler);
    }

    public void patch(String path, Handler handler) {
        addRoute("PATCH", path, handler);
    }

    public void delete(String path, Handler handler) {
        addRoute("DELETE", path, handler);
    }

    public void combineRoutes(String rootPath, Router subRouter) {
        if (subRouter != null) {
            subRouter.basePath = rootPath;

            subRouter.root.handlers.forEach((method, handler) -> {
                addRoute(method, rootPath + "/", handler);
            });

            copyRoutesRecursive(subRouter.root, rootPath, "");
        }

        server.createContext(rootPath, exchange -> {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            RouteMatch match = findRoute(path, method);
            if (match != null) {
                if (useMiddleware != null) {
                    useMiddleware.apply(exchange);
                }

                Context ctx = new Context(exchange);
                if (!match.params.isEmpty()) {
                    Request.setPathParams(match.params);
                }

                try {
                    match.handler.handle(ctx);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                exchange.sendResponseHeaders(404, -1);
                exchange.close();
            }
        });
    }

    public void combineRoutes(String rootPath) {
        combineRoutes(rootPath, null);
    }

    private void copyRoutesRecursive(RouteNode node, String rootPath, String currentPath) {
        node.handlers.forEach((method, handler) -> {
            String fullPath = rootPath + currentPath;
            addRoute(method, fullPath, handler);
        });

        node.children.forEach((segment, childNode) -> {
            String nextPath;
            if (segment.equals("*")) {
                nextPath = currentPath + "/:" + childNode.paramName;
            } else {
                nextPath = currentPath + "/" + segment;
            }
            copyRoutesRecursive(childNode, rootPath, nextPath);
        });
    }

    private void addRoute(String method, String path, Handler handler) {
        path = normalizePath(path);

        String[] segments = path.split("/");
        RouteNode current = root;

        for (String segment : segments) {
            if (segment.isEmpty()) continue;

            if (segment.startsWith(":")) {
                current.children.putIfAbsent("*", new RouteNode());
                current = current.children.get("*");
                current.paramName = segment.substring(1);
            } else {
                current.children.putIfAbsent(segment, new RouteNode());
                current = current.children.get(segment);
            }
        }
        current.addHandler(method, handler);
    }

    private RouteMatch findRoute(String path, String method) {
        path = normalizePath(path);

        String[] segments = path.split("/");
        RouteNode current = root;
        Map<String, String> params = new HashMap<>();

        for (String segment : segments) {
            if (segment.isEmpty()) continue;

            RouteNode nextNode = current.children.get(segment);
            if (nextNode != null) {
                current = nextNode;
            } else if (current.children.containsKey("*")) {
                current = current.children.get("*");
                params.put(current.paramName, segment);
            } else {
                return null;
            }
        }

        Handler handler = current.handlers.get(method);
        return handler != null ? new RouteMatch(handler, params) : null;
    }

    private String normalizePath(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        if (path.length() > 1 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        path = path.replaceAll("/+", "/");
        return path;
    }
}