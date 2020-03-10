package com.myapp.api.model.ticket.request;

import com.google.gson.annotations.SerializedName;
import com.myapp.api.model.ticket.custom_field_request.TicketKnowledgeCustomField;

import java.util.ArrayList;

public class TicketInstanceUserBody {

    @SerializedName("endUserEmail")
    String endUserEmail;

    @SerializedName("endUserCountryCode")
    String countryCode;

    @SerializedName("endUserPhoneNumber")
    String phoneNumber;

    @SerializedName("endUserName")
    String endUserName;

    @SerializedName("sendType")
    String sendType;

    @SerializedName("customFieldValues")
    ArrayList<TicketKnowledgeCustomField> configurations;

    public TicketInstanceUserBody(String endUserEmail, String countryCode, String phoneNumber, String endUserName, String sendType, ArrayList<TicketKnowledgeCustomField> configurations) {
        this.endUserEmail = endUserEmail;
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
        this.endUserName = endUserName;
        this.sendType = sendType;
        this.configurations = configurations;
    }
}
