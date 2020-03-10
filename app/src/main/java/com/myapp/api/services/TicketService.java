package com.myapp.api.services;

import com.myapp.api.model.ticket.response.ExportToPdf;
import com.myapp.api.model.ticket.response.GuestModeTicket;
import com.myapp.api.model.ticket.request.TicketAdminBody;
import com.myapp.api.model.ticket.request.TicketInstanceAdminBody;
import com.myapp.api.model.ticket.request.TicketInstanceUserBody;
import com.myapp.api.model.ticket.request.TicketUserBody;
import com.myapp.api.model.ticket.custom_field_response.TicketKnowledgeCustomFieldResponse;
import com.myapp.api.model.ticket.response.TicketInformation;
import com.myapp.api.model.ticket.response.TicketResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface TicketService {

    // Getting the ticket information
    @GET("v2/ticket")
    Call<TicketInformation> getTicketInfo(@Header("Authorization") String userToken, @Header("ticketId") long ticketId);

    // Getting the organization tickets and knowledge configurations
    @GET("v2/customFieldconfiguration")
    Call<TicketKnowledgeCustomFieldResponse> getOrgConfiguration(@Header("Authorization") String userToken);

    // Getting the organization tickets and knowledge configurations
    @GET("v2/ticket/exportToPdf")
    Call<ExportToPdf> exportTicketToPdf(@Header("Authorization") String userToken, @Header("ticketId") long ticketId);

    // Getting the organization tickets and knowledge configurations
    @GET("v2/ticket/url")
    Call<GuestModeTicket> getTicketGuestMode(@Header("Authorization") String userToken, @Header("ticketId") long ticketId);
    // Admin create regular ticket
    @POST("v2/admin/ticket")
    Call<TicketResponse> createAdminTicket(@Header("Authorization") String userToken, @Body TicketAdminBody ticketAdminBody);

    //Admin create instance ticket
    @POST("v2/admin/ticket/instant")
    Call<TicketResponse> createAdminInstanceTicket(@Header("Authorization") String userToken, @Body TicketInstanceAdminBody ticketInstanceAdminBody);

    // User create a regular ticket
    @POST("v2/ticket")
    Call<TicketResponse> createUserTicket(@Header("Authorization") String userToken, @Body TicketUserBody ticketUserBody);

    // User create instance ticket
    @POST("v2/ticket/instant")
    Call<TicketResponse> createUserInstanceTicket(@Header("Authorization") String userToken, @Body TicketInstanceUserBody ticketInstanceUserBody);
}
