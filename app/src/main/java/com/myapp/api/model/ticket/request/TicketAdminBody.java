package com.myapp.api.model.ticket.request;

import com.google.gson.annotations.SerializedName;
import com.myapp.api.model.ticket.custom_field_request.TicketKnowledgeCustomField;

import java.util.ArrayList;

public class TicketAdminBody {
    @SerializedName("experts")
    ArrayList<String> expertsNames;

    @SerializedName("technicians")
    ArrayList<String> techs;

    @SerializedName("customFieldValues")
    ArrayList<TicketKnowledgeCustomField> configurations;

    String type;

    public TicketAdminBody(ArrayList<String> expertsNames, ArrayList<String> techs, ArrayList<TicketKnowledgeCustomField> configurations, String type) {
        this.expertsNames = expertsNames;
        this.techs = techs;
        this.configurations = configurations;
        this.type = type;
    }


}
