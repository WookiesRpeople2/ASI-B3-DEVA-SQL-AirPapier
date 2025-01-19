package com.airpapier.model;

import com.airpapier.validators.Validator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.jooq.Record;

import java.math.BigDecimal;

@Data
@Builder
@JsonDeserialize(builder = Product.ProductBuilder.class)
public class Product {
    private String id;

    @JsonProperty("reference")
    @NotBlank(message = Validator.REQUIRED)
    @Size(max = 50, message = Validator.SIZE_TOO_LONG)
    private String reference;

    @JsonProperty("price")
    @DecimalMin(value = "0.0", inclusive = false, message = Validator.NOT_BIG_ENOUGH)
    private BigDecimal price;

    @JsonProperty("quantity")
    @Min(value = 0, message = Validator.NOT_BIG_ENOUGH)
    private Integer quantity;

    @JsonProperty("category_id")
    @NotBlank(message = Validator.REQUIRED)
    private String category_id;
}