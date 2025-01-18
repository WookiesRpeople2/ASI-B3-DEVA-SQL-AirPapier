package com.airpapier;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppTest 
{
    private final Map<String, TestEndpoint> endpoints = new HashMap<>();
    private final Map<String, String> testIds = new HashMap<>();

    @BeforeAll
    void setup() {
        RestAssured.config = RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.socket.timeout", 10000)
                        .setParam("http.connection.timeout", 10000)
                        .setParam("http.connection-manager.timeout", 10000));
        RestAssured.baseURI = "http://localhost:3000";
        initializeEndpoints();
        fetchTestIds();
    }

    private void initializeEndpoints() {
        endpoints.put("categories", new TestEndpoint(
                """
                {
                    "name": "TEST",
                    "description": "Updated Description"
                }
                """,
                """
                {
                    "description": "Updated Description"
                }
                """
        ));

        endpoints.put("products", new TestEndpoint(
                """
                {
                    "reference": "REFTEST",
                    "price": 149.99,
                    "quantity": 10,
                    "category_id": "place"
                }
                """,
                """
                {
                    "price": 149.99
                }
                """
        ));

        endpoints.put("suppliers", new TestEndpoint(
                """
                {
                    "name" : "TEST",
                    "email": "test@test.com",
                    "telephone": "0987654321"
                }
                """,
                """
                {
                    "telephone": "0987654321"
                }
                """
        ));

        endpoints.put("orders", new TestEndpoint(
                """
                {
                    "client_id": "placeholder",
                    "total_price": 111.00,
                    "quantity": 255
                }
                """,
                """
                {
                    "total_price": 222.00
                }
                """
        ));

        endpoints.put("clients", new TestEndpoint(
                """
                {
                    "name": "TEST",
                    "email": "test@test.com",
                    "telephone": "1234567891",
                    "address": "Updated Address"
                }
                """,
                """
                {
                    "address": "Updated Address"
                }
                """
        ));
    }

    private void fetchTestIds() {
        endpoints.forEach((name, endpoint) -> {
            String id = fetchFirstId(name);
            if (id != null) {
                testIds.put(name, id);
                DynamicRoutes(testIds);
                System.out.println("Found test ID for " + name + ": " + id);
            } else {
                System.out.println("Warning: No existing ID found for " + name);
            }
        });
    }

    private String fetchFirstId(String endpoint) {
        try {
            return given()
                    .when()
                    .get("/api/" + endpoint + "/")
                    .then()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .extract()
                    .path("[0].id");
        } catch (Exception e) {
            System.out.println("Error fetching ID for " + endpoint + ": " + e.getMessage());
            return null;
        }
    }

    private void DynamicRoutes( Map<String, String> testIds){
        if (testIds.containsKey("clients")) {
            String clientId = testIds.get("clients");
            TestEndpoint ordersEndpoint = new TestEndpoint(
                    String.format("""
                {
                    "client_id": "%s",
                    "total_price": 111.00,
                    "quantity": 255
                }
                """, clientId),
                    """
                    {
                        "total_price": 222.00
                    }
                    """
            );
            endpoints.put("orders", ordersEndpoint);
        }
        if (testIds.containsKey("categories")) {
            String categoryId = testIds.get("categories");
            TestEndpoint productsEndpoint = new TestEndpoint(
                    String.format("""
                {
                    "reference": "REFTEST",
                    "price": 149.99,
                    "quantity": 10,
                    "category_id": "%s"
                }
                """, categoryId),
                    """
                    {
                        "price": 222.00
                    }
                    """
            );
            endpoints.put("products", productsEndpoint);
        }
    }

    private static class TestEndpoint {
        final String postPayload;
        final String updatePayload;

        TestEndpoint(String postPayload, String updatePayload) {
            this.postPayload = postPayload;
            this.updatePayload = updatePayload;
        }
    }

    @TestFactory
    Stream<DynamicTest> generateTests() {
        List<DynamicTest> allTests = new ArrayList<>();

        endpoints.forEach((name, endpoint) -> {
            System.out.println(testIds);
            if (testIds.containsKey(name)) {
                String testId = testIds.get(name);

                allTests.add(DynamicTest.dynamicTest(
                        "1. GET all " + name,
                        () -> testGetAll(name)
                ));

                allTests.add(DynamicTest.dynamicTest(
                        "2. GET " + name + " by ID",
                        () -> testGetById(name, testId)
                ));

                allTests.add(DynamicTest.dynamicTest(
                        "3. POST " + name ,
                        () -> testCreate(name, endpoint.postPayload)
                ));

                allTests.add(DynamicTest.dynamicTest(
                        "4. PATCH " + name,
                        () -> testUpdate(name, testId, endpoint.updatePayload)
                ));
            }
        });

        return allTests.stream();
    }


    private void testGetAll(String endpoint) {
        given()
                .when()
                .get("/api/" + endpoint)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", not(empty()));
    }

    private void testGetById(String endpoint, String id) {
        given()
                .when()
                .get("/api/" + endpoint + "/" + id)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", hasItem(id));
    }

    private void testCreate(String endpoint, String requestBody) {
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/" + endpoint + "/")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", not(empty()));;
    }

    private void testUpdate(String endpoint, String id, String requestBody) {
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .patch("/api/" + endpoint + "/" + id)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", not(empty()));;
    }
}
