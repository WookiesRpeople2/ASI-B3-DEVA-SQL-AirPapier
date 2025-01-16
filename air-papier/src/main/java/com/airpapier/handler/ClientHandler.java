package com.airpapier.handler;

import com.airpapier.doa.ClientDoa;
import com.airpapier.lib.Context;
import com.airpapier.lib.ErrorResponse;
import com.airpapier.lib.SuccessResponse;
import com.airpapier.model.Client;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ClientHandler {
    private final ClientDoa clientDoa = new ClientDoa();

    public void getAllClients(Context ctx) throws SQLException, IOException {
            List<Map<String, Object>> categories = clientDoa.getAllClients();
            ctx.response().json(categories, 200);
    }

    public void getClientById(Context ctx) throws SQLException, IOException {
        List<Map<String, Object>> client = clientDoa.getClientById(ctx.request().param("clientId"));
        ctx.response().json(client, 200);
    }

    public void createClient(Context ctx) throws SQLException, IOException {
        Client newClient = ctx.request().body(Client.class);
        List<Map<String, Object>> createdProduct = clientDoa.createClient(newClient);
        ctx.response().json(createdProduct, 200);
    }

    public void updateClient(Context ctx) throws SQLException, IOException {
        String clientId = ctx.request().param("clientId");
        Client updatedClient = ctx.request().body(Client.class);

        List<Map<String, Object>> existingClient = clientDoa.getClientById(clientId);
        if (existingClient.get(0) == null) {
            ctx.response().json(new ErrorResponse("Product not found"), 404);
            return;
        }

        for (Map<String, Object> row : existingClient) {
            if (row.containsKey("name") && updatedClient.getName() != null) {
                row.put("name", updatedClient.getName());
            }
            if (row.containsKey("email") && updatedClient.getEmail() != null) {
                row.put("email", updatedClient.getEmail());
            }
            if (row.containsKey("telephone") && updatedClient.getTelephone() != null) {
                row.put("telephone", updatedClient.getTelephone());
            }
            if (row.containsKey("address") && updatedClient.getAddress() != null) {
                row.put("ddress", updatedClient.getAddress());
            }
        }

        List<Map<String, Object>> result = clientDoa.updateClient(clientId, (Client) existingClient.get(0));
        ctx.response().json(result, 200);
    }

    public void deleteClient(Context ctx) throws SQLException, IOException {
        String clientId = ctx.request().param("clientId");

        List<Map<String, Object>> existingClient = clientDoa.getClientById(clientId);
        if (existingClient.get(0) == null) {
            ctx.response().json(new ErrorResponse("Product not found"), 404);
            return;
        }

        clientDoa.deleteClient(clientId);
        ctx.response().json(new SuccessResponse("Product deleted successfully"), 200);
    }
}
