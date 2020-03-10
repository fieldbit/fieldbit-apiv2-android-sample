package com.myapp.api.model.ticket.response;

import com.google.gson.annotations.SerializedName;

public class TicketInformation {

    @SerializedName("status")
    String ticketStatus;
    @SerializedName("type")
    String ticketType;
    long lastUpdated;
    long duration;

    public TicketInformation(String ticketStatus, String ticketType, long lastUpdated, long duration) {
        this.ticketStatus = ticketStatus;
        this.ticketType = ticketType;
        this.lastUpdated = lastUpdated;
        this.duration = duration;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public String getTicketType() {
        return ticketType;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public long getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "TicketInformation{" +
                "ticketStatus='" + ticketStatus + "\n" +
                ", ticketType='" + ticketType + "\n" +
                ", lastUpdated=" + lastUpdated + "\n" +
                ", duration=" + duration + "\n" +
                '}';
    }
}
