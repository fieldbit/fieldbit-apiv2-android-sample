package com.myapp.api.model.ticket.custom_field_request;

public class UrgentCustomField extends TicketKnowledgeCustomField {
    boolean value;



    public UrgentCustomField(long id, boolean value ) {
        super(id);
        this.value = value;
    }

    public UrgentCustomField(long id, String type) {
        super(id, type);
    }
    public UrgentCustomField(long id, String type, boolean value) {
        super(id, type);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

}
