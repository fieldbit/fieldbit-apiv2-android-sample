package com.myapp.api.model.auth.request;

public class ModifiedUserBody {
    private String userName;
    private String userIdentifier;

    public ModifiedUserBody(String userName, String userIdentifier) {
        this.userName = userName;
        this.userIdentifier = userIdentifier;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }
}
