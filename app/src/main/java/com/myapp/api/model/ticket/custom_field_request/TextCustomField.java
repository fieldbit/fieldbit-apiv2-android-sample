package com.myapp.api.model.ticket.custom_field_request;

public class TextCustomField extends TicketKnowledgeCustomField {


    String value;
    public TextCustomField(long id,String value) {
        super(id);
        this.value = value;
    }

    public TextCustomField(long id, String type, String value) {
        super(id, type);
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
