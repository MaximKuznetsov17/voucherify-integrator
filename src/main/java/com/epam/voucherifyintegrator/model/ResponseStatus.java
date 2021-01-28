package com.epam.voucherifyintegrator.model;

public enum ResponseStatus {
    SUCCESS("Success!"),
    FAILED("Failed");

    private final String status;

    ResponseStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
