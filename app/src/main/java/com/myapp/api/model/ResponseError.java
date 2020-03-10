package com.myapp.api.model;

import com.google.gson.annotations.SerializedName;

public class ResponseError {
    @SerializedName("errorDesc")
    private String errorDescription;
    @SerializedName("errorMessage")
    private String message;

    public ResponseError(String errorDescription, String message) {
        this.errorDescription = errorDescription;
        this.message = message;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ResponseError{" +
                "errorDescription='" + errorDescription + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
