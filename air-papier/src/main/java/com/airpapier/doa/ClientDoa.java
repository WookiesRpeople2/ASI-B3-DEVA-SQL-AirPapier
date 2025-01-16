package com.airpapier.doa;

import com.airpapier.database.DataBaseConnection;
import com.airpapier.model.Client;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

//In-band SQL Injection

public class ClientDoa {
    public List<Map<String, Object>> getAllClients() throws SQLException{
        String query = "SELECT * FROM clients";
        return DataBaseConnection.getResults(query);
    }

    public List<Map<String, Object>> getClientById(String id) throws SQLException{
        String query = "SELECT * FROM clients WHERE id = '" + id + "'";
        return DataBaseConnection.getResults(query);
    }

    public List<Map<String, Object>> createClient(Client client) throws SQLException {
        String query = "INSERT INTO clients (name, email, telephone, address) VALUES ('" +
                client.getName() + "', " +
                client.getEmail() + ", " +
                client.getTelephone() + ", " +
                client.getAddress() + ")";

        return DataBaseConnection.getResults(query);
    }

    public List<Map<String, Object>> updateClient(String id, Client client) throws SQLException {
        String query = "UPDATE clients SET name = '" + client.getName() +
                "', email = " + client.getEmail() +
                ", telephone = " + client.getTelephone() +
                ", address = " + client.getAddress() +
                " WHERE id = '" + id + "'";
        return DataBaseConnection.getResults(query);
    }

    public void deleteClient(String id) throws SQLException {
        String query = "DELETE FROM clients WHERE id = '" + id + "'";

        DataBaseConnection.getResults(query);
    }
}
