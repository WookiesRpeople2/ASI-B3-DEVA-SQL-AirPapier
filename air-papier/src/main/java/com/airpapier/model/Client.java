package com.airpapier.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonDeserialize(builder = Client.ClientBuilder.class)
public class Client {
    private String id;
    @JsonProperty("name") private String name;
    @JsonProperty("email") private String email;
    @JsonProperty("telephone") private String telephone;
    @JsonProperty("address") private String address;
}
