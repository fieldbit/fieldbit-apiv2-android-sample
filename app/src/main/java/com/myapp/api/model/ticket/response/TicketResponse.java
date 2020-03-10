package com.myapp.api.model.ticket.response;

public class TicketResponse {
    String ticketId;

    public TicketResponse(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketId() {
        return ticketId;
    }
}
