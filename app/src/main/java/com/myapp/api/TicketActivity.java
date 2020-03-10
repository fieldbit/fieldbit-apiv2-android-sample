package com.myapp.api;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.myapp.api.model.ResponseError;
import com.myapp.api.model.ticket.response.ExportToPdf;
import com.myapp.api.model.ticket.response.GuestModeTicket;
import com.myapp.api.model.ticket.request.TicketAdminBody;
import com.myapp.api.model.ticket.request.TicketInstanceAdminBody;
import com.myapp.api.model.ticket.request.TicketInstanceUserBody;
import com.myapp.api.model.ticket.custom_field_response.CustomFieldBase;
import com.myapp.api.model.ticket.custom_field_response.TicketKnowledgeCustomFieldResponse;
import com.myapp.api.model.ticket.custom_field_request.BooleanCustomField;
import com.myapp.api.model.ticket.custom_field_request.ListCustomField;
import com.myapp.api.model.ticket.custom_field_request.SiteCustomField;
import com.myapp.api.model.ticket.custom_field_request.TextCustomField;
import com.myapp.api.model.ticket.custom_field_request.TicketKnowledgeCustomField;
import com.myapp.api.model.auth.request.Auth;
import com.myapp.api.model.ticket.request.TicketUserBody;
import com.myapp.api.model.ticket.response.TicketInformation;
import com.myapp.api.model.ticket.response.TicketResponse;
import com.myapp.api.services.RetrofitInstance;
import com.myapp.api.services.TicketService;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.myapp.api.Network.Errors.HTTP_RESPONSE_AUTHENTICATION_ERROR;
import static com.myapp.api.Network.Errors.HTTP_RESPONSE_AUTHORIZATION_ERROR;
import static com.myapp.api.Network.Errors.HTTP_RESPONSE_NOT_FOUND;
import static com.myapp.api.Network.Errors.HTTP_RESPONSE_OK;
import static com.myapp.api.Network.Errors.HTTP_UNKNOWN_ERROR;

public class TicketActivity extends BaseActivity {

    private static final String TAG = "TICKET_ACTIVITY";


    private ProgressBar progressBar;

    private HashMap<String, String> sendType = new HashMap<String, String>() {{
        put("email", "EMAIL");
        put("sms", "SMS");
        put("both", "ALL");
    }};

    private Auth adminAuth;
    private Auth auth;
    private long ticketNumber;

    private static String EXPERT_EMAIL = "";
    private static String TECH_NAME = "";
    private static String END_USER_EMAIL = "";
    private static String END_USER_COUNTRY_CODE = "";
    private static String PHONE_NUMBER = "";
    private static String END_USER_NAME = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        initiateAuthToken();

        final TicketService ticketService = RetrofitInstance.getRetrofitInstance().create(TicketService.class);

        Button btAdminCreateTicket = findViewById(R.id.bt_admin_create_ticket);
        Button btAdminCreateInstanceTicket = findViewById(R.id.bt_admin_create_instance_ticket);
        Button btUserCreateTicket = findViewById(R.id.bt_user_create_ticket);
        Button btUserCreateInstanceTicket = findViewById(R.id.bt_user_create_instance_ticket);
        Button btGetTicketInfo = findViewById(R.id.bt_get_ticket_information);
        Button btGetTicketKnowledgeConfig = findViewById(R.id.bt_get_ticket_knowledge_config);
        Button btExportToPdf = findViewById(R.id.bt_export_pdf);
        Button btGuestModeTicket = findViewById(R.id.bt_guest_mode_ticket);

        progressBar = findViewById(R.id.pb_ticket);

        ticketNumber = 45216L;

        btGetTicketInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar(true);
                getTicketInfo(ticketService, adminAuth.getAccessToken(), ticketNumber);
            }
        });

        btExportToPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar(true);
                exportToPdf(ticketService, adminAuth.getAccessToken(), ticketNumber);
            }
        });

        btGetTicketKnowledgeConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar(true);
                getOrgConfiguration(ticketService, adminAuth.getAccessToken());
            }
        });

        btAdminCreateTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar(true);
                createAdminTicket(ticketService, adminAuth.getAccessToken());
            }
        });

        btAdminCreateInstanceTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar(true);
                createAdminInstanceTicket(ticketService, adminAuth.getAccessToken());
            }
        });
        btUserCreateTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar(true);
                createUserTicket(ticketService, adminAuth.getAccessToken());
            }
        });
        btUserCreateInstanceTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar(true);
                createUserInstanceTicket(ticketService, adminAuth.getAccessToken());
            }
        });
        btGuestModeTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar(true);
                getTicketToken(ticketService, adminAuth.getAccessToken(), ticketNumber);
            }
        });

    }


    //Only admin end points

    private void createAdminInstanceTicket(TicketService ticketService, String accessToken) {
        ArrayList<TicketKnowledgeCustomField> customFields = new ArrayList<>();
        TicketInstanceAdminBody ticketInstanceAdminBody =
                new TicketInstanceAdminBody(
                        EXPERT_EMAIL,
                        END_USER_EMAIL,
                        END_USER_COUNTRY_CODE,
                        PHONE_NUMBER,
                        END_USER_NAME,
                        sendType.get("email"),
                        customFields);

        Call<TicketResponse> call = ticketService.createAdminInstanceTicket(accessToken, ticketInstanceAdminBody);
        call.enqueue(new Callback<TicketResponse>() {
            @Override
            public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                showProgressBar(false);
                if (response.code() == HTTP_RESPONSE_OK) {
                    Log.d(TAG, "onResponse: " + response.body());
                    TicketResponse ticket = response.body();
                    if (!isDebugMode()) {
                        Toast.makeText(TicketActivity.this, ticket.getTicketId(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Gson gson = new Gson();
                    ResponseError errorResponse = gson.fromJson(response.errorBody().charStream(), ResponseError.class);
                    if (response.code() == HTTP_RESPONSE_AUTHENTICATION_ERROR) {
                        printError(response.code(), errorResponse);
                    }
                    if (response.code() == HTTP_RESPONSE_AUTHORIZATION_ERROR) {
                        printError(response.code(), errorResponse);
                    }
                    if (response.code() == HTTP_RESPONSE_NOT_FOUND) {
                        printError(response.code(), errorResponse);
                    }
                    if (response.code() == HTTP_UNKNOWN_ERROR) {
                        printError(response.code(), errorResponse);
                    }
                }
            }

            @Override
            public void onFailure(Call<TicketResponse> call, Throwable t) {
                Toast.makeText(TicketActivity.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void createAdminTicket(TicketService ticketService, String accessToken) {
        ArrayList<String> experts = new ArrayList<String>() {
            {
                add(EXPERT_EMAIL);
            }
        };
        ArrayList<String> techs = new ArrayList<String>() {
            {
                add(TECH_NAME);

            }
        };
        ArrayList<TicketKnowledgeCustomField> config = new ArrayList<>();
        TicketAdminBody ticketAdminBody = new TicketAdminBody(
                experts,
                techs,
                config,
                "GET_HELP"
        );

        Call<TicketResponse> call = ticketService.createAdminTicket(accessToken, ticketAdminBody);
        call.enqueue(new Callback<TicketResponse>() {
            @Override
            public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                showProgressBar(false);
                if (response.code() == HTTP_RESPONSE_OK) {
                    Log.d(TAG, "onResponse: " + response.body());
                    TicketResponse ticket = response.body();

                    if (!isDebugMode()) {
                        Toast.makeText(TicketActivity.this, ticket.getTicketId(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Gson gson = new Gson();
                    ResponseError errorResponse = gson.fromJson(response.errorBody().charStream(), ResponseError.class);
                    if (response.code() == HTTP_RESPONSE_AUTHENTICATION_ERROR) {
                        printError(response.code(), errorResponse);
                    }
                    if (response.code() == HTTP_RESPONSE_AUTHORIZATION_ERROR) {
                        printError(response.code(), errorResponse);
                    }
                    if (response.code() == HTTP_RESPONSE_NOT_FOUND) {
                        printError(response.code(), errorResponse);
                    }
                    if (response.code() == HTTP_UNKNOWN_ERROR) {
                        printError(response.code(), errorResponse);
                    }
                }
            }

            @Override
            public void onFailure(Call<TicketResponse> call, Throwable t) {
                Toast.makeText(TicketActivity.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Both admin and user end point

    private void getTicketToken(TicketService ticketService, String accessToken, final long ticketNumber) {
        Call<GuestModeTicket> call = ticketService.getTicketGuestMode(accessToken, ticketNumber);
        call.enqueue(new Callback<GuestModeTicket>() {
            @Override
            public void onResponse(Call<GuestModeTicket> call, Response<GuestModeTicket> response) {
                showProgressBar(false);
                if (response.code() == HTTP_RESPONSE_OK) {
                    GuestModeTicket guestModeTicket = response.body();
                    String ticketUrl = Network.GUEST_MODE_BASE_URL + ticketNumber + "?token" + guestModeTicket.getUrlToken();
                    if (!isDebugMode()) {
                        Toast.makeText(TicketActivity.this, ticketUrl, Toast.LENGTH_SHORT).show();
                    }
                    Log.d(TAG, "onResponse: " + guestModeTicket.getUrlToken());
                    Log.d(TAG, "ticketUrl: " + ticketUrl);

                } else {
                    Gson gson = new Gson();
                    ResponseError errorResponse = gson.fromJson(response.errorBody().charStream(), ResponseError.class);
                    if (response.code() == HTTP_RESPONSE_AUTHENTICATION_ERROR) {
                        printError(response.code(), errorResponse);
                    }
                    if (response.code() == HTTP_RESPONSE_AUTHORIZATION_ERROR) {
                        printError(response.code(), errorResponse);
                    }
                    if (response.code() == HTTP_RESPONSE_NOT_FOUND) {
                        printError(response.code(), errorResponse);
                    }
                    if (response.code() == HTTP_UNKNOWN_ERROR) {
                        printError(response.code(), errorResponse);
                    }
                }
            }

            @Override
            public void onFailure(Call<GuestModeTicket> call, Throwable t) {
                Toast.makeText(TicketActivity.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void createUserInstanceTicket(TicketService ticketService, String accessToken) {

        ArrayList<TicketKnowledgeCustomField> ticketCustomFields = new ArrayList<>();
        TicketInstanceUserBody ticketInstanceUserBody =
                new TicketInstanceUserBody(
                        END_USER_EMAIL,
                        END_USER_COUNTRY_CODE,
                        PHONE_NUMBER,
                        END_USER_NAME,
                        "EMAIL",
                        ticketCustomFields);

        Call<TicketResponse> call = ticketService.createUserInstanceTicket(accessToken, ticketInstanceUserBody);
        Log.d(TAG, "createUserInstanceTicket: " + call.request().body());
        call.enqueue(new Callback<TicketResponse>() {

            @Override
            public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                showProgressBar(false);
                if (response.code() == HTTP_RESPONSE_OK) {
                    Log.d(TAG, "onResponse: " + response.body());
                    TicketResponse ticket = response.body();
                    if (!isDebugMode()) {
                        Toast.makeText(TicketActivity.this, ticket.getTicketId(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Gson gson = new Gson();
                    ResponseError errorResponse = gson.fromJson(response.errorBody().charStream(), ResponseError.class);
                    if (response.code() == HTTP_RESPONSE_AUTHENTICATION_ERROR) {
                        printError(response.code(), errorResponse);
                    }
                    if (response.code() == HTTP_RESPONSE_AUTHORIZATION_ERROR) {
                        printError(response.code(), errorResponse);
                    }
                    if (response.code() == HTTP_RESPONSE_NOT_FOUND) {
                        printError(response.code(), errorResponse);
                    }
                    if (response.code() == HTTP_UNKNOWN_ERROR) {
                        printError(response.code(), errorResponse);
                    }
                }
            }

            @Override
            public void onFailure(Call<TicketResponse> call, Throwable t) {
                Toast.makeText(TicketActivity.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUserTicket(TicketService ticketService, String accessToken) {
        ArrayList<TicketKnowledgeCustomField> ticketCustomFields = new ArrayList<>();
        ArrayList<String> participants = new ArrayList<String>() {
            {
                add(TECH_NAME);
            }
        };
        TicketUserBody ticketUserBody =
                new TicketUserBody(
                        participants,
                        ticketCustomFields,
                        "GET_HELP"
                );
        Call<TicketResponse> call = ticketService.createUserTicket(accessToken, ticketUserBody);
        call.enqueue(new Callback<TicketResponse>() {
            @Override
            public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                showProgressBar(false);
                Log.d(TAG, "onResponse: " + response);
                if (response.code() == HTTP_RESPONSE_OK) {
                    Log.d(TAG, "onResponse: " + response.body());
                    TicketResponse ticket = response.body();
                    if (!isDebugMode()) {
                        Toast.makeText(TicketActivity.this, ticket.getTicketId(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Gson gson = new Gson();
                    ResponseError errorResponse = gson.fromJson(response.errorBody().charStream(), ResponseError.class);
                    if (response.code() == HTTP_RESPONSE_AUTHENTICATION_ERROR) {
                        printError(response.code(), errorResponse);
                    }
                    if (response.code() == HTTP_RESPONSE_AUTHORIZATION_ERROR) {
                        printError(response.code(), errorResponse);
                    }
                    if (response.code() == HTTP_RESPONSE_NOT_FOUND) {
                        printError(response.code(), errorResponse);
                    }
                    if (response.code() == HTTP_UNKNOWN_ERROR) {
                        printError(response.code(), errorResponse);
                    }
                }
            }

            @Override
            public void onFailure(Call<TicketResponse> call, Throwable t) {
                Toast.makeText(TicketActivity.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getOrgConfiguration(TicketService ticketService, String accessToken) {
        Call<TicketKnowledgeCustomFieldResponse> call = ticketService.getOrgConfiguration(accessToken);
        call.enqueue(new Callback<TicketKnowledgeCustomFieldResponse>() {

            @Override
            public void onResponse(Call<TicketKnowledgeCustomFieldResponse> call, Response<TicketKnowledgeCustomFieldResponse> response) {
                showProgressBar(false);
                if (response.code() == HTTP_RESPONSE_OK) {
                    Log.d(TAG, "onResponse: " + response.body());
                    TicketKnowledgeCustomFieldResponse ticketKnowledgeCustomFieldResponse = response.body();

                    CustomFieldBase customFieldBase = ticketKnowledgeCustomFieldResponse.getField(0);
                    Log.d(TAG, "onResponse: " + customFieldBase.getName());
                    if (!isDebugMode()) {
                        Toast.makeText(TicketActivity.this, customFieldBase.getName(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Gson gson = new Gson();
                    ResponseError errorResponse = gson.fromJson(response.errorBody().charStream(), ResponseError.class);
                    if (response.code() == HTTP_RESPONSE_AUTHENTICATION_ERROR) {
                        printError(response.code(), errorResponse);
                    }
                    if (response.code() == HTTP_RESPONSE_AUTHORIZATION_ERROR) {
                        printError(response.code(), errorResponse);
                    }
                    if (response.code() == HTTP_RESPONSE_NOT_FOUND) {
                        printError(response.code(), errorResponse);
                    }
                    if (response.code() == HTTP_UNKNOWN_ERROR) {
                        printError(response.code(), errorResponse);
                    }
                }
            }

            @Override
            public void onFailure(Call<TicketKnowledgeCustomFieldResponse> call, Throwable t) {

            }
        });
    }

    private void exportToPdf(TicketService ticketService, String accessToken, long ticketNumber) {
        Call<ExportToPdf> call = ticketService.exportTicketToPdf(accessToken, ticketNumber);
        call.enqueue(new Callback<ExportToPdf>() {

            @Override
            public void onResponse(Call<ExportToPdf> call, Response<ExportToPdf> response) {
                showProgressBar(false);
                if (response.code() == HTTP_RESPONSE_OK) {
                    ExportToPdf pdfFile = response.body();
                    Log.d(TAG, "onResponse: " + response.body().getPdfUrl());
                    if (!isDebugMode()) {
                        Toast.makeText(TicketActivity.this, pdfFile.getPdfUrl(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Gson gson = new Gson();
                    ResponseError errorResponse = gson.fromJson(response.errorBody().charStream(), ResponseError.class);
                    if (response.code() == HTTP_RESPONSE_AUTHENTICATION_ERROR) {
                        printError(response.code(), errorResponse);
                    }
                    if (response.code() == HTTP_RESPONSE_AUTHORIZATION_ERROR) {
                        printError(response.code(), errorResponse);
                    }
                    if (response.code() == HTTP_RESPONSE_NOT_FOUND) {
                        printError(response.code(), errorResponse);
                    }
                    if (response.code() == HTTP_UNKNOWN_ERROR) {
                        printError(response.code(), errorResponse);
                    }
                }
            }

            @Override
            public void onFailure(Call<ExportToPdf> call, Throwable t) {

            }
        });
    }

    private void getTicketInfo(TicketService ticketService, String accessToken, long ticketNumber) {
        Call<TicketInformation> call = ticketService.getTicketInfo(accessToken, ticketNumber);
        call.enqueue(new Callback<TicketInformation>() {

            @Override
            public void onResponse(Call<TicketInformation> call, Response<TicketInformation> response) {
                showProgressBar(false);
                if (response.code() == HTTP_RESPONSE_OK) {
                    Log.d(TAG, "onResponse: " + response.body());

                    TicketInformation ticketInformation = response.body();
                    if (!isDebugMode()) {
                        Toast.makeText(TicketActivity.this, ticketInformation.getTicketStatus(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Gson gson = new Gson();
                    ResponseError errorResponse = gson.fromJson(response.errorBody().charStream(), ResponseError.class);
                    if (response.code() == HTTP_RESPONSE_AUTHENTICATION_ERROR) {
                        printError(response.code(), errorResponse);
                    }
                    if (response.code() == HTTP_RESPONSE_AUTHORIZATION_ERROR) {
                        printError(response.code(), errorResponse);
                    }
                    if (response.code() == HTTP_RESPONSE_NOT_FOUND) {
                        printError(response.code(), errorResponse);
                    }
                    if (response.code() == HTTP_UNKNOWN_ERROR) {
                        printError(response.code(), errorResponse);
                    }
                }
            }

            @Override
            public void onFailure(Call<TicketInformation> call, Throwable t) {
                Toast.makeText(TicketActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initiateAuthToken() {
        Intent intent = getIntent();
        Gson gson = new Gson();
        Log.d(TAG, "initiateAuthToken: " + getIntent().getStringExtra("admin_json"));
        adminAuth = gson.fromJson(getIntent().getStringExtra("admin_json"), Auth.class);
        auth = gson.fromJson(getIntent().getStringExtra("user_json"), Auth.class);
    }


    private void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }


    private void printError(int code, ResponseError errorResponse) {
        Log.d(TAG, "onResponse: error code:" + code + "\n" +
                errorResponse.toString());
        if (!isDebugMode()) {
            Toast.makeText(TicketActivity.this, errorResponse.getErrorDescription(), Toast.LENGTH_SHORT).show();
        }
    }
}
