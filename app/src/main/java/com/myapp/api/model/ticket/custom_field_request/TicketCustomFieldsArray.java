package com.myapp.api.model.ticket.custom_field_request;

import java.util.ArrayList;

public class TicketCustomFieldsArray {
    ArrayList<TicketKnowledgeCustomField> array;

    public TicketCustomFieldsArray(ArrayList<TicketKnowledgeCustomField> array) {
        this.array = array;
    }

    public ArrayList<TicketKnowledgeCustomField> getArray() {
        return array;
    }
}
