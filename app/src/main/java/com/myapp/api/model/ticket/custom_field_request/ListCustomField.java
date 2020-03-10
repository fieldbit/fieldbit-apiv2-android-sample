package com.myapp.api.model.ticket.custom_field_request;

import java.util.ArrayList;

public class ListCustomField extends TicketKnowledgeCustomField {
    private ArrayList<Integer> value;
    public ListCustomField(long id, ArrayList<Integer> values) {
        super(id);
        this.value = values;
    }

    public ArrayList<Integer> getValue() {
        return value;
    }

    public void setValue(ArrayList<Integer> value) {
        this.value = value;
    }

    public ListCustomField(long id, String type, ArrayList<Integer> value) {
        super(id, type);
        this.value = value;
    }
}
