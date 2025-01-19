package com.airpapier.model;

import com.airpapier.validators.Validator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@JsonDeserialize(builder = Order.OrderBuilder.class)
public class Order {

    private String id;

    @JsonProperty("client_id")
    @NotBlank(message = Validator.REQUIRED)
    private String client_id;

    @JsonProperty("total_price")
    @DecimalMin(value = "0.0", inclusive = false, message = Validator.NOT_BIG_ENOUGH)
    private BigDecimal total_price;

    @JsonProperty("quantity")
    @Min(value = 0, message = Validator.NOT_BIG_ENOUGH)
    private Integer quantity;

    private String created_at;
    private String updated_at;

}
