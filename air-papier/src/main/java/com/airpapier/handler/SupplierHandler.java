package com.airpapier.handler;

import com.airpapier.doa.GeneralDoa;
import com.airpapier.lib.Context;
import com.airpapier.lib.ErrorResponse;
import com.airpapier.lib.SuccessResponse;
import com.airpapier.model.Supplier;

import java.io.IOException;
import java.util.List;

public class SupplierHandler {
    private final GeneralDoa<Supplier> supplierDoa = new GeneralDoa<>("suppliers", Supplier.class);

    public void getAllSuppliers(Context ctx) throws IOException {
        try {
            List<Supplier> suppliers = supplierDoa.getAll();
            if (suppliers.isEmpty()) {
                ctx.response().json(new SuccessResponse("No suppliers found"), 200);
                return;
            }
            ctx.response().json(suppliers, 200);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    public void getSupplierById(Context ctx) throws IOException {
        try {
            String supplierId = ctx.request().param("supplierId");
            Supplier supplier = supplierDoa.getById(supplierId);
            if (supplier == null) {
                ctx.response().json(new ErrorResponse("Supplier not found"), 404);
                return;
            }
            ctx.response().json(supplier, 200);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    public void createSupplier(Context ctx) throws IOException {
        try {
            Supplier newSupplier = ctx.request().body(Supplier.class);
            supplierDoa.create(newSupplier);
            ctx.response().json(new SuccessResponse("Supplier successfully created", newSupplier), 201);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    public void updateSupplier(Context ctx) throws IOException {
        try {
            String supplierId = ctx.request().param("supplierId");
            Supplier updatedSupplier = ctx.request().body(Supplier.class);
            if (updatedSupplier == null) {
                ctx.response().json(new ErrorResponse("Request body is required"), 400);
                return;
            }

            Supplier existingSupplier = supplierDoa.getById(supplierId);
            if (existingSupplier == null) {
                ctx.response().json(new ErrorResponse("Supplier not found"), 404);
                return;
            }

            updateSupplierFields(existingSupplier, updatedSupplier);

            supplierDoa.update(supplierId, existingSupplier);
            ctx.response().json(new SuccessResponse("Supplier updated successfully", existingSupplier), 200);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    public void deleteSupplier(Context ctx) throws IOException {
        try {
            String supplierId = ctx.request().param("supplierId");
            Supplier existingSupplier = supplierDoa.getById(supplierId);
            if (existingSupplier == null) {
                ctx.response().json(new ErrorResponse("Supplier not found"), 404);
                return;
            }

            supplierDoa.delete(supplierId);
            ctx.response().json(new SuccessResponse("Supplier deleted successfully"), 200);
        } catch (Exception e) {
            ctx.response().json(new ErrorResponse("Unexpected error: " + e.getMessage()), 500);
        }
    }

    private void updateSupplierFields(Supplier existingSupplier, Supplier updatedSupplier) {
        if (updatedSupplier.getName() != null) {
            existingSupplier.setName(updatedSupplier.getName());
        }
        if (updatedSupplier.getEmail() != null) {
            existingSupplier.setEmail(updatedSupplier.getEmail());
        }
        if (updatedSupplier.getTelephone() != null) {
            existingSupplier.setTelephone(updatedSupplier.getTelephone());
        }
    }
}
