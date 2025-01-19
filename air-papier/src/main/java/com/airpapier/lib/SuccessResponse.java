package com.airpapier.lib;

import lombok.Getter;

@Getter
public class SuccessResponse {
    private final String message;
    private final Object data;

    public SuccessResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public SuccessResponse(String message) {
        this.message = message;
        this.data = null;
    }
}
