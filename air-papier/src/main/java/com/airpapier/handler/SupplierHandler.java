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
        List<Map<String, Object>> categories = SupplierDoa.getAllSuppliers();
        ctx.response().json(categories, 200);
    }

    public void getSupplierById(Context ctx) throws SQLException, IOException {
        List<Map<String, Object>> Supplier = SupplierDoa.getSupplierById(ctx.request().param("supplierId"));
        ctx.response().json(Supplier, 200);
    }

    public void createSupplier(Context ctx) throws SQLException, IOException {
        Supplier newSupplier = ctx.request().body(Supplier.class);
        List<Map<String, Object>> createdProduct = SupplierDoa.createSupplier(newSupplier);
        ctx.response().json(createdProduct, 200);
    }

    public void updateSupplier(Context ctx) throws SQLException, IOException {
        String SupplierId = ctx.request().param("supplierId");
        Supplier updatedSupplier = ctx.request().body(Supplier.class);

        List<Map<String, Object>> existingSupplier = SupplierDoa.getSupplierById(SupplierId);
        if (existingSupplier.get(0) == null) {
            ctx.response().json(new ErrorResponse("Product not found"), 404);
            return;
        }

        for (Map<String, Object> row : existingSupplier) {
            if (row.containsKey("name") && updatedSupplier.getName() != null) {
                row.put("name", updatedSupplier.getName());
            }
            if (row.containsKey("email") && updatedSupplier.getEmail() != null) {
                row.put("email", updatedSupplier.getEmail());
            }
            if (row.containsKey("telephone") && updatedSupplier.getTelephone() != null) {
                row.put("telephone", updatedSupplier.getTelephone());
            }
        }

        List<Map<String, Object>> result = SupplierDoa.updateSupplier(SupplierId, (Supplier) existingSupplier.get(0));
        ctx.response().json(result, 200);
    }

    public void deleteSupplier(Context ctx) throws SQLException, IOException {
        String SupplierId = ctx.request().param("supplierId");

        List<Map<String, Object>> existingSupplier = SupplierDoa.getSupplierById(SupplierId);
        if (existingSupplier.get(0) == null) {
            ctx.response().json(new ErrorResponse("Product not found"), 404);
            return;
        }

        SupplierDoa.deleteSupplier(SupplierId);
        ctx.response().json(new SuccessResponse("Product deleted successfully"), 200);
    }
}
