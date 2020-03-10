package com.myapp.api.model.auth.request;

import java.io.Serializable;

public class AuthSuccess extends Auth implements Serializable {

    private String accessToken;
    private long tokenDuration;

    public AuthSuccess(String accessToken, long tokenDuration) {
        this.accessToken = accessToken;
        this.tokenDuration = tokenDuration;

    }
}
