package com.myapp.api.model.ticket.custom_field_response;

import java.util.ArrayList;

public class TicketKnowledgeCustomFieldResponse {
    ArrayList<CustomFieldBase> fields;

    public TicketKnowledgeCustomFieldResponse(ArrayList<CustomFieldBase> fields) {
        this.fields = fields;
    }

    public ArrayList<CustomFieldBase> getFields() {
        return fields;
    }
    public CustomFieldBase getField(int index){
        return fields.get(index);
    }
}
