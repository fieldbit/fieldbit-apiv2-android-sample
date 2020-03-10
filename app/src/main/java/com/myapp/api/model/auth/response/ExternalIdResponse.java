package com.myapp.api.model.auth.response;

public class ExternalIdResponse {
    private double numberUsersModified;

    public ExternalIdResponse(double numberUsersModified) {
        this.numberUsersModified = numberUsersModified;
    }

    public double getNumberUsersModified() {
        return numberUsersModified;
    }
}
