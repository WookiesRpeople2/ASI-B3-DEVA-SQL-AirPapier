package com.airpapier.model;

import com.airpapier.validators.Validator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonDeserialize(builder = Client.ClientBuilder.class)
public class Client {

    private String id;

    @JsonProperty("name")
    @NotBlank(message = Validator.REQUIRED)
    @Size(max = 100, message = Validator.SIZE_TOO_LONG)
    private String name;

    @JsonProperty("email")
    @NotBlank(message = Validator.REQUIRED)
    @Email(message = Validator.EMAIL_INVALID)
    private String email;

    @JsonProperty("telephone")
    @NotBlank(message = Validator.REQUIRED)
    @Pattern(regexp = Validator.TELEPHONE_REGEX, message = Validator.TELEPHONE_INVALID)
    private String telephone;

    @JsonProperty("address")
    @NotBlank(message = Validator.REQUIRED)
    @Size(max = 200, message = Validator.SIZE_TOO_LONG)
    private String address;
}
