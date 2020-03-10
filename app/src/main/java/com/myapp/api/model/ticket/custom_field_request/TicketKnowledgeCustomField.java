package com.myapp.api.model.ticket.custom_field_request;

public class TicketKnowledgeCustomField {

    private long id;
    private String type;

    public TicketKnowledgeCustomField(long id, String type) {
        this.id = id;
        this.type = type;
    }

    public TicketKnowledgeCustomField(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }
}
