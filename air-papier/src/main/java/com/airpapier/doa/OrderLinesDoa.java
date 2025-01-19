package com.airpapier.doa;

import com.airpapier.database.DataBaseConnection;
import com.airpapier.database.DynamicMapper;
import com.airpapier.model.Order;
import org.jooq.DSLContext;
import org.jooq.*;
import org.jooq.Record;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.jooq.impl.DSL.*;

public class OrderLinesDoa {
    private final DSLContext dsl = DataBaseConnection.getDsl();

    public List<Map<String, Object>> getAllOrdersWithProducts() {
        return dsl.select(
                        field("p.*"),
                        field("o.id").as("order_id"),
                        field("o.client_id"),
                        field("o.total_price").as("order_total_price"),
                        field("o.quantity").as("order_quantity")
                )
                .from(table("products").as("p"))
                .leftJoin(table("order_lines").as("ol"))
                .on(field("p.id").eq(field("ol.product_id")))
                .leftJoin(table("orders").as("o"))
                .on(field("ol.order_id").eq(field("o.id")))
                .fetch()
                .intoMaps();
    }

    public List<Map<String, Object>> getProductsByOrderId(String orderId) {
        return dsl.select(
                        field("p.id").as("product_id"),
                        field("p.reference").as("product_reference"),
                        field("p.price").as("product_price"),
                        field("p.quantity").as("product_quantity"),
                        field("p.category_id").as("product_category_id"),
                        field("ol.order_id"),
                        field("o.client_id"),
                        field("o.total_price").as("order_total_price"),
                        field("o.quantity").as("order_quantity")
                )
                .from(table("products").as("p"))
                .leftJoin(table("order_lines").as("ol"))
                .on(field("p.id").eq(field("ol.product_id")))
                .leftJoin(table("orders").as("o"))
                .on(field("ol.order_id").eq(field("o.id")))
                .where(field("o.id").eq(orderId))
                .fetch()
                .intoMaps();
    }

    public List<Map<String, Object>> getOrdersByClientId(String clientId) {
        return dsl.select(
                        field("o.id").as("order_id"),
                        field("o.client_id").as("client_id"),
                        field("o.total_price").as("total_price"),
                        field("o.quantity").as("order_quantity")
                )
                .from(table("orders").as("o"))
                .leftJoin(table("order_lines").as("ol")).on(field("o.id").eq(field("ol.order_id")))
                .leftJoin(table("products").as("p")).on(field("ol.product_id").eq(field("p.id")))
                .where(field("o.client_id").eq(clientId))
                .fetch()
                .intoMaps();
    }

    public List<Map<String, Object>> getOrdersByProduct(String productId) {
        return dsl.select( field("o.id").as("order_id"),
                        field("o.client_id").as("client_id"),
                        field("o.total_price").as("total_price"),
                        field("o.quantity").as("order_quantity"))
                .from(table("orders").as("o"))
                .join(table("order_lines").as("ol")).on(field("o.id").eq(field("ol.order_id")))
                .where(field("ol.product_id").eq(productId))
                .fetch()
                .intoMaps();
    }

    public List<Map<String, Object>> searchOrders(Map<String, Object> criteria) {
        SelectConditionStep<?> query = dsl.select(field("o.id").as("order_id"),
                        field("o.client_id").as("client_id"),
                        field("o.total_price").as("total_price"),
                        field("o.quantity").as("order_quantity"))
                .from(table("orders").as("o"))
                .leftJoin(table("order_lines").as("ol")).on(field("o.id").eq(field("ol.order_id")))
                .leftJoin(table("products").as("p")).on(field("ol.product_id").eq(field("p.id")))
                .where(noCondition());

        Condition condition = noCondition();

        if (criteria.containsKey("clientId")) {
            condition = condition.and(field("o.client_id").eq(criteria.get("clientId")));
        }
        if (criteria.containsKey("status")) {
            condition = condition.and(field("ol.status").eq(criteria.get("status")));
        }
        if (criteria.containsKey("productId")) {
            condition = condition.and(field("ol.product_id").eq(criteria.get("productId")));
        }
        if (criteria.containsKey("startDate")) {
            condition = condition.and(field("o.created_at").ge(criteria.get("startDate")));
        }
        if (criteria.containsKey("endDate")) {
            condition = condition.and(field("o.created_at").le(criteria.get("endDate")));
        }

        return query.$where(condition).fetch().intoMaps();
    }

    public List<Map<String, Object>> getTopSellingProducts(LocalDate startDate, LocalDate endDate) {
        return dsl.select(
                        field("p.reference").as("product"),
                        count().as("total_sales")
                )
                .from(table("products").as("p"))
                .join(table("order_lines").as("ol")).on(field("p.id").eq(field("ol.product_id")))
                .join(table("orders").as("o")).on(field("ol.order_id").eq(field("o.id")))
                .where(field("o.created_at").between(startDate).and(endDate))
                .groupBy(field("p.id"), field("p.reference"))
                .orderBy(field("total_sales").desc())
                .fetch()
                .intoMaps();
    }

    public List<Map<String, Object>> getOrderFromOrderLines(String orderId) {
        return dsl.select(
                        field("o.id").as("order_id"),
                        field("o.client_id"),
                        field("o.total_price").as("order_total_price"),
                        field("o.quantity").as("order_quantity")
                )
                .from(table("orders").as("o"))
                .leftJoin(table("order_lines").as("ol")).on(field("o.id").eq(field("ol.order_id")))
                .where(field("o.id").eq(orderId))
                .fetch()
                .intoMaps();
    }
    public boolean checkAndUpdateStock(String productId, int requestedQuantity) {
        Record1<Object> stockRecord = dsl.select(field("quantity"))
                .from(table("products"))
                .where(field("id").eq(productId))
                .fetchOne();

        if (stockRecord != null) {
            int currentStock = (int) stockRecord.value1();
            if (currentStock >= requestedQuantity) {
                dsl.update(table("products"))
                        .set(field("quantity"), currentStock - requestedQuantity)
                        .where(field("id").eq(productId))
                        .execute();
                return true;
            }
        }
        return false;
    }

    public List<Map<String, Object>> getLowStockProducts(int threshold) {
        return dsl.select(
                        field("reference").as("product"),
                        field("quantity").as("current_stock")
                )
                .from(table("products"))
                .where(field("quantity").le(threshold))
                .orderBy(field("quantity"))
                .fetch()
                .intoMaps();
    }
}
