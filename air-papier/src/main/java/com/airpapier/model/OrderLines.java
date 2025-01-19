package com.airpapier.model;

import com.airpapier.validators.Validator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;


@Data
@Builder
@JsonDeserialize(builder = OrderLines.OrderLinesBuilder.class)
public class OrderLines {
    private String id;

    @JsonProperty("order_id")
    @NotBlank(message = Validator.REQUIRED)
    private String order_id;

    @JsonProperty("product_id")
    @DecimalMin(value = "0.0", inclusive = false, message = Validator.NOT_BIG_ENOUGH)
    private BigDecimal product_id;
}
