package com.epam.voucherifyintegrator.model;

public class CustomResponse {
    private final ResponseStatus status;
    private final String message;
    private final Object result;

    public CustomResponse(ResponseStatus status, String message, Object result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getResult() {
        return result;
    }
}
