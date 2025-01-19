package com.airpapier.model;

import com.airpapier.validators.Validator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonDeserialize(builder = Category.CategoryBuilder.class)
public class Category {

    private String id;

    @JsonProperty("name")
    @NotBlank(message = Validator.REQUIRED)
    @Size(max = 100, message = Validator.SIZE_TOO_LONG)
    private String name;

    @JsonProperty("description")
    @NotBlank(message = Validator.REQUIRED)
    @Size(max = 255, message = Validator.SIZE_TOO_LONG)
    private String description;
}
