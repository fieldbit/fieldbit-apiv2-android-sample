package com.myapp.api.model.ticket.request;

import com.google.gson.annotations.SerializedName;
import com.myapp.api.model.ticket.custom_field_request.TicketKnowledgeCustomField;

import java.util.ArrayList;

public class TicketInstanceAdminBody {
    @SerializedName("expert")
    String expertNames;

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



    public TicketInstanceAdminBody(
            String expertNames,
            String endUserEmail,
            String countryCode,
            String phoneNumber,
            String endUserName,
            String sendType,
            ArrayList<TicketKnowledgeCustomField> configurations
    ) {
        this.expertNames = expertNames;
        this.endUserEmail = endUserEmail;
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
        this.endUserName = endUserName;
        this.sendType = sendType;
        this.configurations = configurations;

    }


    public String getExpertNames() {
        return expertNames;
    }

    public String getEndUserEmail() {
        return endUserEmail;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEndUserName() {
        return endUserName;
    }

    public String getSendType() {
        return sendType;
    }

    public ArrayList<TicketKnowledgeCustomField> getConfigurations() {
        return configurations;
    }

}
