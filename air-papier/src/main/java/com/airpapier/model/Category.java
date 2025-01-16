package com.airpapier.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonDeserialize(builder = Product.ProductBuilder.class)
public class Category {
    private String id;
    @JsonProperty("name") private String name;
    @JsonProperty("description") private String description;
}
