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
        clientDoa.createClient(newClient);
        ctx.response().json(new SuccessResponse("Client successfully created"), 200);
    }

    public void updateClient(Context ctx) throws SQLException, IOException {
        String clientId = ctx.request().param("clientId");
        Client updatedClient = ctx.request().body(Client.class);

        Map<String, Object> existingClientMap = clientDoa.getClientById(clientId).get(0);

        if (existingClientMap.isEmpty()) {
            ctx.response().json(new ErrorResponse("Client not found"), 404);
            return;
        }

        Client existingClient = Client.builder()
                .id((String) existingClientMap.get("id"))
                .name((String) existingClientMap.get("name"))
                .email((String) existingClientMap.get("email"))
                .telephone((String) existingClientMap.get("telephone"))
                .address((String) existingClientMap.get("address"))
                .build();

        if (updatedClient.getName() != null) {
            existingClient.setName(updatedClient.getName());
        }
        if (updatedClient.getEmail() != null) {
            existingClient.setEmail(updatedClient.getEmail());
        }
        if (updatedClient.getTelephone() != null) {
            existingClient.setTelephone(updatedClient.getTelephone());
        }
        if (updatedClient.getAddress() != null) {
            existingClient.setAddress(updatedClient.getAddress());
        }

        clientDoa.updateClient(clientId, existingClient);
        ctx.response().json(new SuccessResponse("Product successfully updated"), 200);
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
