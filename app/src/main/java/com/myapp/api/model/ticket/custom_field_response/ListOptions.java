package com.myapp.api.model.ticket.custom_field_response;

public class ListOptions {
    long id;
    String name;

    public ListOptions(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
