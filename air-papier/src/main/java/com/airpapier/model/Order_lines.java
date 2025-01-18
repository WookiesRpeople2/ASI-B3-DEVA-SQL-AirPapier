package com.airpapier.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Order_lines {
    private String id;
    @JsonProperty("order_id") private String order_id;
    @JsonProperty("product_id") private BigDecimal product_id;
}
