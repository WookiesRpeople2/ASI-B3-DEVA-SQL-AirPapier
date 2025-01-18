package com.airpapier.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@JsonDeserialize(builder = Supplier.SupplierBuilder.class)
public class Supplier {
    private String id;
    @JsonProperty("name") private String name;
    @JsonProperty("email") private String email;
    @JsonProperty("telephone") private String telephone;
}
