package com.airpapier.routes;

import com.airpapier.handler.CategoryHandler;
import com.airpapier.lib.Router;
import com.sun.net.httpserver.HttpServer;

public class CategoryRoutes extends Router {

    public CategoryRoutes(){
        CategoryHandler categoryHandler = new CategoryHandler();
        get("/", categoryHandler::getAllCategories);
        get("/:categoryId", categoryHandler::getCategoryById);
        post("/", categoryHandler::createCategory);
        patch("/:categoryId", categoryHandler::updateCategory);
        delete("/:categoryId", categoryHandler::deleteCategory);
    }
}
