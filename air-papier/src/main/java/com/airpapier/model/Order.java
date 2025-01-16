package com.airpapier.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@JsonDeserialize(builder = Order.OrderBuilder.class)
public class Order {
    private String id;
    @JsonProperty("client_id") private String client_id;
    @JsonProperty("total_price") private BigDecimal total_price;
    @JsonProperty("quantity") private Integer quantity;
}
