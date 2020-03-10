package com.myapp.api.usecase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myapp.api.BaseActivity;
import com.myapp.api.Network;
import com.myapp.api.R;
import com.myapp.api.model.ResponseError;
import com.myapp.api.model.auth.request.Auth;
import com.myapp.api.model.ticket.custom_field_request.BooleanCustomField;
import com.myapp.api.model.ticket.custom_field_request.ListCustomField;
import com.myapp.api.model.ticket.custom_field_request.SiteCustomField;
import com.myapp.api.model.ticket.custom_field_request.TextCustomField;
import com.myapp.api.model.ticket.custom_field_request.TicketCustomFieldsArray;
import com.myapp.api.model.ticket.custom_field_request.TicketKnowledgeCustomField;
import com.myapp.api.model.ticket.custom_field_request.UrgentCustomField;
import com.myapp.api.model.ticket.request.TicketAdminBody;
import com.myapp.api.model.ticket.response.TicketInformation;
import com.myapp.api.model.ticket.response.TicketResponse;
import com.myapp.api.services.AuthenticationService;
import com.myapp.api.services.TicketService;
import com.myapp.api.util.RuntimeTypeAdapterFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.myapp.api.Network.CustomFieldType.ASSET;
import static com.myapp.api.Network.CustomFieldType.BOOLEAN;
import static com.myapp.api.Network.CustomFieldType.LIST;
import static com.myapp.api.Network.CustomFieldType.TEXT;
import static com.myapp.api.Network.CustomFieldType.URGENT;
import static com.myapp.api.Network.Errors.HTTP_RESPONSE_AUTHENTICATION_ERROR;
import static com.myapp.api.Network.Errors.HTTP_RESPONSE_AUTHORIZATION_ERROR;
import static com.myapp.api.Network.Errors.HTTP_RESPONSE_NOT_FOUND;
import static com.myapp.api.Network.Errors.HTTP_RESPONSE_OK;
import static com.myapp.api.Network.Errors.HTTP_UNKNOWN_ERROR;
import static com.myapp.api.services.RetrofitInstance.getRetrofitInstance;

public class MainFlowActivity extends BaseActivity {

    private final static String GET_HELP = "GET_HELP";
    private final static String PROVIDE_HELP = "PROVIDE_HELP";

    private EditText etExpertEmail;
    private EditText etTechEmail;
    private Button btCreateTicket;
    private EditText etTicketNumber;
    private Button btGetTicketInformation;
    private TextView tvTicketInformation;
    private ProgressBar progressBar;
    private Button btSetCustomFields;
    private TicketCustomFieldsArray customFields;
    private RadioGroup rgTicketType;
    private RadioButton rbGetHelp;
    private RadioButton rbProvideHelp;

    private String ticketType = GET_HELP;

    private static final String TAG = "BASE_FLOW";

    private Auth adminAuth;
    private Auth auth;

    final AuthenticationService authenticationService = getRetrofitInstance().create(AuthenticationService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_a_ticket);

        initElements();
        getAdminAuthToken(authenticationService);


        customFields = new TicketCustomFieldsArray(new ArrayList<TicketKnowledgeCustomField>());
        btCreateTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etExpertEmail.getText().toString().isEmpty() || etTechEmail.getText().toString().isEmpty()) {
                    Toast.makeText(MainFlowActivity.this, "please fill expert and tech email", Toast.LENGTH_SHORT).show();
                } else {
                    showProgressBar(true);
                    createAdminTicket(getRetrofitInstance().create(TicketService.class), adminAuth.getAccessToken(), etExpertEmail.getText().toString(), etTechEmail.getText().toString());
                }
            }
        });

        btGetTicketInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etTicketNumber.getText().toString().isEmpty()) {
                    Toast.makeText(MainFlowActivity.this, "please fill the ticket number", Toast.LENGTH_SHORT).show();
                } else {
                    showProgressBar(true);
                    getTicketInformation(getRetrofitInstance().create(TicketService.class), adminAuth.getAccessToken(), Integer.parseInt(etTicketNumber.getText().toString()));
                }
            }
        });

        btSetCustomFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCustomFieldConfiguration();
            }
        });

        rgTicketType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rbGetHelp.isChecked()) {
                    ticketType = GET_HELP;
                } else {
                    ticketType = PROVIDE_HELP;
                }
            }
        });

    }

    private void getCustomFieldConfiguration() {

        Intent intent = new Intent(this, CustomFieldActivity.class);
        String adminJson;
        Gson gson = new Gson();

        adminJson = gson.toJson(adminAuth);
        intent.putExtra("admin_json", adminJson);
        startActivityForResult(intent, 1);

    }

    private void getTicketInformation(TicketService ticketService, String accessToken, final int ticketNumber) {
        Call<TicketInformation> call = ticketService.getTicketInfo(accessToken, ticketNumber);
        call.enqueue(new Callback<TicketInformation>() {

            @Override
            public void onResponse(Call<TicketInformation> call, Response<TicketInformation> response) {
                showProgressBar(false);
                if (response.code() == HTTP_RESPONSE_OK) {
                    Log.d(TAG, "onResponse: " + response.body());

                    TicketInformation ticketInformationResponse = response.body();

                    String ticketInfo = "Ticket number:" + ticketNumber + "\n" +
                            "Ticket status:" + ticketInformationResponse.getTicketStatus() + "\n" +
                            "Ticket type: " + ticketInformationResponse.getTicketType() + "\n" +
                            "Ticket duration: " + ticketInformationResponse.getDuration() + "\n" +
                            "Ticket last updated: " + ticketInformationResponse.getLastUpdated();

                    tvTicketInformation.setText(ticketInfo);

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
                showProgressBar(false);
                Toast.makeText(MainFlowActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void createAdminTicket(TicketService ticketService, String accessToken, final String expertEmail, final String techEmail) {
        ArrayList<String> experts = new ArrayList<String>() {
            {
                add(expertEmail);
            }
        };
        ArrayList<String> techs = new ArrayList<String>() {
            {
                add(techEmail);

            }
        };
        ArrayList<TicketKnowledgeCustomField> config = setCustomField();

        customFields.getArray();
        TicketAdminBody ticketAdminBody = new TicketAdminBody(
                experts,
                techs,
                config,
                ticketType
        );

        Call<TicketResponse> call = ticketService.createAdminTicket(accessToken, ticketAdminBody);
        call.enqueue(new Callback<TicketResponse>() {
            @Override
            public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                showProgressBar(false);
                if (response.code() == HTTP_RESPONSE_OK) {
                    Log.d(TAG, "onResponse: " + response.body());
                    TicketResponse ticket = response.body();
                    Toast.makeText(MainFlowActivity.this, "ticket: " + ticket.getTicketId() + " got opened", Toast.LENGTH_SHORT).show();
                    etTicketNumber.setText(ticket.getTicketId());

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
                showProgressBar(false);
                Toast.makeText(MainFlowActivity.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<TicketKnowledgeCustomField> setCustomField() {
        return new ArrayList<TicketKnowledgeCustomField>() {{
            for (int i = 0; i < customFields.getArray().size(); i++) {

                if (customFields.getArray().get(i).getType().equals(ASSET)) {
                    //SiteCustomField site = (SiteCustomField) customFields.getArray().get(i);
                    //add(new SiteCustomField(customFields.getArray().get(i).getId(),site.getValue()));
                } else if (customFields.getArray().get(i).getType().equals(TEXT)) {
                    TextCustomField textField = (TextCustomField) customFields.getArray().get(i);
                    add(new TextCustomField(customFields.getArray().get(i).getId(), textField.getValue()));

                } else if (customFields.getArray().get(i).getType().equals(URGENT)) {
                    UrgentCustomField urgentField = (UrgentCustomField) customFields.getArray().get(i);
                    add(new UrgentCustomField(customFields.getArray().get(i).getId(), urgentField.getValue()));
                } else if (customFields.getArray().get(i).getType().equals(BOOLEAN)) {
                    BooleanCustomField booleanField = (BooleanCustomField) customFields.getArray().get(i);
                    add(new BooleanCustomField(customFields.getArray().get(i).getId(), booleanField.getValue()));
                } else if (customFields.getArray().get(i).getType().equals(LIST)) {
                    ListCustomField listField = (ListCustomField) customFields.getArray().get(i);
                    add(new ListCustomField(customFields.getArray().get(i).getId(), listField.getValue()));
                }

            }
        }};
    }

    private void getAdminAuthToken(AuthenticationService authenticationService) {
        //From the account -> api tab  copy the api_id and api_secret to the string res
        Call<Auth> call = authenticationService.adminAuth(getString(R.string.api_id), getString(R.string.api_secret));
        call.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                showProgressBar(false);
                if (response.code() == HTTP_RESPONSE_OK) {
                    Log.d(TAG, "onResponse: " + response.body().getAccessToken());
                    adminAuth = response.body();
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
            public void onFailure(Call<Auth> call, Throwable t) {
                showProgressBar(false);
                Toast.makeText(MainFlowActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initElements() {
        etExpertEmail = findViewById(R.id.et_expert);
        etTechEmail = findViewById(R.id.et_tech);
        btCreateTicket = findViewById(R.id.bt_create_ticket);
        etTicketNumber = findViewById(R.id.et_ticket_number);
        btGetTicketInformation = findViewById(R.id.bt_get_ticket_info);
        progressBar = findViewById(R.id.main_flow_progressbar);
        tvTicketInformation = findViewById(R.id.tv_ticket_info);
        btSetCustomFields = findViewById(R.id.bt_set_custom_field);
        rgTicketType = findViewById(R.id.rgType);
        rbGetHelp = findViewById(R.id.rb_get_help);
        rbProvideHelp = findViewById(R.id.rb_provide_help);
    }

    private void printError(int code, ResponseError errorResponse) {
        Log.d(TAG, "onResponse: error code:" + code + "\n" +
                errorResponse.toString());
        if (!isDebugMode()) {
            Toast.makeText(MainFlowActivity.this, errorResponse.getErrorDescription(), Toast.LENGTH_SHORT).show();
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Gson gson = getBaseFieldsEntitiesGson();
                String s = data.getStringExtra("ticketCustomFields");

                Log.d(TAG, "onActivityResult: " + s);

                customFields = gson.fromJson(s, TicketCustomFieldsArray.class);

                Log.d(TAG, "onActivityResult: " + customFields.getArray());

            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }//onActivityResult

    public static RuntimeTypeAdapterFactory<TicketKnowledgeCustomField> getCustomFieldsFactory() {
        return RuntimeTypeAdapterFactory.of(TicketKnowledgeCustomField.class, "type")
                .registerSubtype(BooleanCustomField.class, Network.CustomFieldType.BOOLEAN)
                .registerSubtype(UrgentCustomField.class, Network.CustomFieldType.URGENT)
                .registerSubtype(TextCustomField.class, Network.CustomFieldType.TEXT)
                .registerSubtype(ListCustomField.class, Network.CustomFieldType.LIST)
                .registerSubtype(SiteCustomField.class, Network.CustomFieldType.ASSET);
    }

    private static Gson getBaseFieldsEntitiesGson() {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapterFactory(getCustomFieldsFactory());
        return gsonBuilder.create();
    }
}
