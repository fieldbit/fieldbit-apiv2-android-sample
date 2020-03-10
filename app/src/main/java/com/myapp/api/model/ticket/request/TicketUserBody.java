package com.myapp.api.model.ticket.request;

import com.google.gson.annotations.SerializedName;
import com.myapp.api.model.ticket.custom_field_request.TicketKnowledgeCustomField;

import java.util.ArrayList;

public class TicketUserBody {
    @SerializedName("possibleParticipants")
    ArrayList<String> participants;

    @SerializedName("customFieldValues")
    ArrayList<TicketKnowledgeCustomField> configurations;

    String type;

    public TicketUserBody(ArrayList<String> participants, ArrayList<TicketKnowledgeCustomField> configurations, String type) {
        this.participants = participants;
        this.configurations = configurations;
        this.type = type;
    }
}
