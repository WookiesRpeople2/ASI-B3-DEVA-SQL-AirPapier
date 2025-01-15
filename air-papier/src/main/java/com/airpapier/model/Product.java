package com.airpapier.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@JsonDeserialize(builder = Product.ProductBuilder.class)
public class Product {
    private String id;
    @JsonProperty("reference") private String reference;
    @JsonProperty("price") private BigDecimal price;
    @JsonProperty("quantity") private Integer quantity;
    @JsonProperty("category_id") private String category_id;
}