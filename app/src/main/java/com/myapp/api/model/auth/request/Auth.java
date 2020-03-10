package com.myapp.api.model.auth.request;

import java.io.Serializable;

public class Auth implements Serializable {

    private String accessToken;
    private long tokenDuration;

    public Auth() {

    }

    public Auth(String accessToken, long tokenDuration) {
        this.accessToken = accessToken;
        this.tokenDuration = tokenDuration;

    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getTokenDuration() {
        return tokenDuration;
    }


    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setTokenDuration(long tokenDuration) {
        this.tokenDuration = tokenDuration;
    }

}
