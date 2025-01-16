package com.airpapier.doa;

import com.airpapier.database.DataBaseConnection;
import com.airpapier.model.Supplier;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SupplierDoa {
    public List<Map<String, Object>> getAllSuppliers() throws SQLException {
        String query = "SELECT * FROM suppliers";
        return DataBaseConnection.getResults(query);
    }

    public List<Map<String, Object>> getSupplierById(String id) throws SQLException{
        String query = "SELECT * FROM suppliers WHERE id = '" + id + "'";
        return DataBaseConnection.getResults(query);
    }

    public List<Map<String, Object>> createSupplier(Supplier supplier) throws SQLException {
        String query = "INSERT INTO suppliers (name, email, telephone, address) VALUES ('" +
                supplier.getName() + "', " +
                supplier.getEmail() + ", " +
                supplier.getTelephone() +
                ")";

        return DataBaseConnection.getResults(query);
    }

    public List<Map<String, Object>> updateSupplier(String id, Supplier supplier) throws SQLException {
        String query = "UPDATE suppliers SET name = '" + supplier.getName() +
                "', email = " + supplier.getEmail() +
                ", telephone = " + supplier.getTelephone() +
                " WHERE id = '" + id + "'";
        return DataBaseConnection.getResults(query);
    }

    public void deleteSupplier(String id) throws SQLException {
        String query = "DELETE FROM suppliers WHERE id = '" + id + "'";

        DataBaseConnection.getResults(query);
    }
}
