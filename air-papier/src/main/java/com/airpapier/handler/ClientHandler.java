package com.airpapier.handler;

import com.airpapier.doa.GeneralDoa;
import com.airpapier.doa.OrderLinesDoa;
import com.airpapier.lib.Context;
import com.airpapier.lib.ErrorResponse;
import com.airpapier.lib.SuccessResponse;
import com.airpapier.model.Client;
import com.airpapier.model.Order;
import org.jooq.Record;
import org.jooq.Result;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClientHandler {
    private final GeneralDoa<Client> clientDoa = new GeneralDoa<>("clients", Client.class);
    private final OrderLinesDoa orderLinesDoa = new OrderLinesDoa();

    public void getAllClients(Context ctx) throws IOException {
        try {
            List<Client> clients = clientDoa.getAll();
            if (clients.isEmpty()) {
                ctx.response().json(new SuccessResponse("No clients found"), 200);
                return;
            }
            ctx.response().json(clients, 200);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    public void getClientById(Context ctx) throws IOException {
        try {
            String clientId = ctx.request().param("clientId");
            Client client = clientDoa.getById(clientId);
            System.out.println(client);
            if (client == null) {
                ctx.response().json(new ErrorResponse("Client not found"), 404);
                return;
            }
            ctx.response().json(client, 200);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    public void getOrderByClientId(Context ctx) throws IOException {
        try {
            String clientId = ctx.request().param("clientId");
            List<Map<String, Object>> order = orderLinesDoa.getOrdersByClientId(clientId);

            if (order.isEmpty()) {
                ctx.response().json(new ErrorResponse("Client not found"), 404);
                return;
            }

            ctx.response().json(order, 200);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    public void createClient(Context ctx) throws IOException {
        try {
            Client newClient = ctx.request().body(Client.class);
            clientDoa.create(newClient);
            ctx.response().json(newClient, 201);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    public void updateClient(Context ctx) throws IOException {
        try {
            String clientId = ctx.request().param("clientId");
            Client updatedClient = ctx.request().body(Client.class);
            if (updatedClient == null) {
                ctx.response().json(new ErrorResponse("Request body is required"), 400);
                return;
            }

            Client existingClient = clientDoa.getById(clientId);
            if (existingClient== null) {
                ctx.response().json(new ErrorResponse("Client not found"), 404);
                return;
            }

            updateClientFields(existingClient, updatedClient);

            clientDoa.update(clientId, existingClient);
            ctx.response().json(new SuccessResponse("Client successfully updated", existingClient), 200);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    public void deleteClient(Context ctx) throws IOException {
        try {
            String clientId = ctx.request().param("clientId");
            if (clientId == null || clientId.trim().isEmpty()) {
                ctx.response().json(new ErrorResponse("Client ID is required"), 400);
                return;
            }

            Client existingClient = clientDoa.getById(clientId);
            if (existingClient == null) {
                ctx.response().json(new ErrorResponse("Client not found"), 404);
                return;
            }

            clientDoa.delete(clientId);
            ctx.response().json(new SuccessResponse("Client deleted successfully"), 200);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    private void updateClientFields(Client existingClient, Client updatedClient) {
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
    }
}
