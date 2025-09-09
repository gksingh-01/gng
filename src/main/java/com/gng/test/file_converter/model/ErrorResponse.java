package com.gng.test.file_converter.model;

public class ErrorResponse {

    private final String status = "Error";

    private int httpErrorCode;
    private String description;

    public ErrorResponse() {

    }

    public ErrorResponse(int httpErrorCode, String description) {
        this.httpErrorCode = httpErrorCode;
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public int getHttpErrorCode() {
        return httpErrorCode;
    }

    public void setHttpErrorCode(int httpErrorCode) {
        this.httpErrorCode = httpErrorCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
