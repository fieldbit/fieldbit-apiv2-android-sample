package com.myapp.api.usecase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.myapp.api.BaseActivity;
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
import com.myapp.api.model.ticket.custom_field_response.CustomFieldBase;
import com.myapp.api.model.ticket.custom_field_response.ListOptions;
import com.myapp.api.model.ticket.custom_field_response.TicketKnowledgeCustomFieldResponse;
import com.myapp.api.services.RetrofitInstance;
import com.myapp.api.services.TicketService;

import java.util.ArrayList;
import java.util.HashMap;

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

public class CustomFieldActivity extends BaseActivity {

    private static final String TAG = "CUSTOM_FIELD_ACTIVITY";

    private Auth adminAuth;
    private ProgressBar progressBarCustomFields;
    private TicketKnowledgeCustomFieldResponse ticketKnowledgeCustomFieldResponse;
    private LinearLayout cfLinearLayout;
    private Button btResult;
    private HashMap<String, Pair<TicketKnowledgeCustomField, View>> customViewMap = new HashMap<>();
    private ArrayList<Pair<TicketKnowledgeCustomField, View>> arrayOfFields;
    private final TicketService ticketService = RetrofitInstance.getRetrofitInstance().create(TicketService.class);
    private CustomFieldBase field;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_field);

        initiateViews();
        initiateAuthToken();
        showProgressBar(true);
        getTicketCustomField(ticketService, adminAuth.getAccessToken());

        btResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnResult();
            }
        });
    }

    private void initiateViews() {
        progressBarCustomFields = findViewById(R.id.pb_custom_field);
        btResult = findViewById(R.id.bt_result);
        cfLinearLayout = findViewById(R.id.linear_layout_custom_field);
        cfLinearLayout.setOrientation(LinearLayout.VERTICAL);
        arrayOfFields = new ArrayList<>();

    }

    private void returnResult() {
        Gson gson = new Gson();
        Intent returnIntent = new Intent();

        ArrayList<TicketKnowledgeCustomField> customFields = getResultFromCustomField();
        TicketCustomFieldsArray ticketCustomFieldsArray = new TicketCustomFieldsArray(customFields);
        String customFieldJson = gson.toJson(ticketCustomFieldsArray);

        returnIntent.putExtra("ticketCustomFields", customFieldJson);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private ArrayList<TicketKnowledgeCustomField> getResultFromCustomField() {
        ArrayList<TicketKnowledgeCustomField> customFields = new ArrayList<>();
        Log.d(TAG, "getResultFromCustomField: " + cfLinearLayout.getChildCount());

        for (int i = 0; i < arrayOfFields.size(); i++) {
            final Pair<TicketKnowledgeCustomField, View> pair = arrayOfFields.get(i);
            if (pair.first.getType().equals(URGENT)) {
                CheckBox cbUrgent = (CheckBox) pair.second;
                Log.d(TAG, "getResultFromCustomField: " + cbUrgent.isChecked());
                customFields.add(new UrgentCustomField(pair.first.getId(), URGENT, cbUrgent.isChecked()));

            } else if (pair.first.getType().equals(BOOLEAN)) {
                CheckBox cbBoolean = (CheckBox) pair.second;
                Log.d(TAG, "getResultFromCustomField: " + cbBoolean.isChecked());
                customFields.add(new BooleanCustomField(pair.first.getId(), BOOLEAN, cbBoolean.isChecked()));
            } else if (pair.first.getType().equals(TEXT)) {
                EditText etText = (EditText) pair.second;
                Log.d(TAG, "getResultFromCustomField: " + etText.getText().toString());
                customFields.add(new TextCustomField(pair.first.getId(), TEXT, etText.getText().toString()));
            } else if (pair.first.getType().equals(ASSET)) {
                EditText etSite = (EditText) pair.second;
                Log.d(TAG, "getResultFromCustomField: " + etSite.getText().toString());
                customFields.add(new SiteCustomField(pair.first.getId(), ASSET, etSite.getText().toString()));
            } else if (pair.first.getType().equals(LIST)) {
                final Spinner spList = (Spinner) pair.second;
                Log.d(TAG, "getResultFromCustomField: " + spList.getSelectedItem().toString());

                final int finalI = i;
                customFields.add(new ListCustomField(pair.first.getId(), LIST, new ArrayList<Integer>() {{
                    Log.d(TAG, "instance initializer: " + ticketKnowledgeCustomFieldResponse.getField(finalI).getOptionId(spList.getSelectedItem().toString()));
                    add((int) ticketKnowledgeCustomFieldResponse.getField(finalI).getOptionId(spList.getSelectedItem().toString()));
                }}));
            }
        }
        return customFields;
    }

    private void getTicketCustomField(TicketService ticketService, String accessToken) {
        Call<TicketKnowledgeCustomFieldResponse> call = ticketService.getOrgConfiguration(accessToken);
        call.enqueue(new Callback<TicketKnowledgeCustomFieldResponse>() {

            @Override
            public void onResponse(Call<TicketKnowledgeCustomFieldResponse> call, Response<TicketKnowledgeCustomFieldResponse> response) {
                showProgressBar(false);
                if (response.code() == HTTP_RESPONSE_OK) {
                    Log.d(TAG, "onResponse: " + response.body());
                    ticketKnowledgeCustomFieldResponse = response.body();
                    initializeScreen();
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

    private void initializeScreen() {
        for (int i = 0; i < ticketKnowledgeCustomFieldResponse.getFields().size(); i++) {
            Log.d(TAG, "initializeScreen: " + ticketKnowledgeCustomFieldResponse.getField(i).getType());
            field = ticketKnowledgeCustomFieldResponse.getField(i);
            if (field.getType().equals(URGENT)) {
                CheckBox cbField = new CheckBox(this);
                cbField.setText(field.getName());
                cfLinearLayout.addView(cbField);
                arrayOfFields.add(new Pair<TicketKnowledgeCustomField, View>(new UrgentCustomField((long) field.getId(), URGENT), cbField));

            } else if (field.getType().equals(BOOLEAN)) {
                CheckBox cbField = new CheckBox(this);
                cbField.setText(field.getName());
                cfLinearLayout.addView(cbField);
                arrayOfFields.add(new Pair<TicketKnowledgeCustomField, View>(new BooleanCustomField((long) field.getId(), BOOLEAN), cbField));

            } else if (field.getType().equals(LIST)) {
                Spinner listSpinner = new Spinner(this);
                ArrayList<ListOptions> options = field.getListOptions();
                ArrayList<String> listAdapter = new ArrayList<>();
                for (int j = 0; j < options.size(); j++) {
                    listAdapter.add(options.get(j).getName());
                }
                field.setMapList();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listAdapter);
                listSpinner.setAdapter(adapter);
                arrayOfFields.add(new Pair<TicketKnowledgeCustomField, View>(new ListCustomField((long) field.getId(), LIST, new ArrayList<Integer>()), listSpinner));
                cfLinearLayout.addView(listSpinner);

            } else if (field.getType().equals(ASSET)) {
                EditText etField = new EditText(this);
                etField.setHint(field.getName());
                cfLinearLayout.addView(etField);
                arrayOfFields.add(new Pair<TicketKnowledgeCustomField, View>(new SiteCustomField((long) field.getId(), ASSET, ""), etField));

            } else if (field.getType().equals(TEXT)) {
                EditText etField = new EditText(this);
                etField.setHint(field.getName());
                cfLinearLayout.addView(etField);
                arrayOfFields.add(new Pair<TicketKnowledgeCustomField, View>(new TextCustomField((long) field.getId(), TEXT, ""), etField));
            }
        }
    }


    private void showProgressBar(boolean show) {
        if (show) {
            progressBarCustomFields.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            progressBarCustomFields.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    private void initiateAuthToken() {
        Gson gson = new Gson();
        adminAuth = gson.fromJson(getIntent().getStringExtra("admin_json"), Auth.class);
    }

    private void printError(int code, ResponseError errorResponse) {
        Log.d(TAG, "onResponse: error code:" + code + "\n" +
                errorResponse.toString());
        if (!isDebugMode()) {
            Toast.makeText(CustomFieldActivity.this, errorResponse.getErrorDescription(), Toast.LENGTH_SHORT).show();
        }
    }
}
