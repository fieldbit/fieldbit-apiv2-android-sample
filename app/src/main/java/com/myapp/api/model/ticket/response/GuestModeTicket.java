package com.myapp.api.model.ticket.response;

public class GuestModeTicket {
    String urlToken;

    public GuestModeTicket(String urlToken) {
        this.urlToken = urlToken;
    }

    public String getUrlToken() {
        return urlToken;
    }
}
