package com.airpapier.handler;

import com.airpapier.doa.SupplierDoa;
import com.airpapier.lib.Context;
import com.airpapier.lib.ErrorResponse;
import com.airpapier.lib.SuccessResponse;
import com.airpapier.model.Supplier;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SupplierHandler {
    private final SupplierDoa SupplierDoa = new SupplierDoa();

    public void getAllSuppliers(Context ctx) throws SQLException, IOException {
        List<Map<String, Object>> suppliers = SupplierDoa.getAllSuppliers();
        ctx.response().json(suppliers, 200);
    }

    public void getSupplierById(Context ctx) throws SQLException, IOException {
        List<Map<String, Object>> Supplier = SupplierDoa.getSupplierById(ctx.request().param("supplierId"));
        ctx.response().json(Supplier, 200);
    }

    public void createSupplier(Context ctx) throws SQLException, IOException {
        Supplier newSupplier = ctx.request().body(Supplier.class);
        SupplierDoa.createSupplier(newSupplier);
        ctx.response().json(new SuccessResponse("Supplier successfully created"), 200);
    }

    public void updateSupplier(Context ctx) throws SQLException, IOException {
        String supplierId = ctx.request().param("supplierId");
        Supplier updatedSupplier = ctx.request().body(Supplier.class);

        Map<String, Object> existingSupplierMap = SupplierDoa.getSupplierById(supplierId).get(0);
        if (existingSupplierMap.isEmpty()) {
            ctx.response().json(new ErrorResponse("Product not found"), 404);
            return;
        }

        Supplier existingSupplier = Supplier.builder()
                        .id((String) existingSupplierMap.get("id"))
                        .email((String) existingSupplierMap.get("email"))
                        .name((String) existingSupplierMap.get("name"))
                        .email((String) existingSupplierMap.get("email"))
                        .telephone((String) existingSupplierMap.get("telephone"))
                        .build();

        if (updatedSupplier.getEmail() != null) {
            existingSupplier.setEmail(updatedSupplier.getEmail());
        }
        if (updatedSupplier.getName() != null) {
            existingSupplier.setName(updatedSupplier.getName());
        }
        if (updatedSupplier.getTelephone() != null) {
            existingSupplier.setTelephone(updatedSupplier.getTelephone());
        }

        SupplierDoa.updateSupplier(supplierId, existingSupplier);
        ctx.response().json(new SuccessResponse("Supplier successfully Updated"), 200);
    }

    public void deleteSupplier(Context ctx) throws SQLException, IOException {
        String SupplierId = ctx.request().param("supplierId");

        List<Map<String, Object>> existingSupplier = SupplierDoa.getSupplierById(SupplierId);
        if (existingSupplier.get(0) == null) {
            ctx.response().json(new ErrorResponse("Supplier not found"), 404);
            return;
        }

        SupplierDoa.deleteSupplier(SupplierId);
        ctx.response().json(new SuccessResponse("Supplier deleted successfully"), 200);
    }
}
