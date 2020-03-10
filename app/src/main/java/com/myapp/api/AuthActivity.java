package com.myapp.api;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.myapp.api.model.ResponseError;
import com.myapp.api.model.auth.request.Auth;
import com.myapp.api.model.auth.request.ModifiedArrayUser;
import com.myapp.api.model.auth.request.ModifiedUserBody;
import com.myapp.api.model.auth.response.ExternalIdResponse;
import com.myapp.api.services.AuthenticationService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.myapp.api.Network.Errors.HTTP_RESPONSE_AUTHENTICATION_ERROR;
import static com.myapp.api.Network.Errors.HTTP_RESPONSE_AUTHORIZATION_ERROR;
import static com.myapp.api.Network.Errors.HTTP_RESPONSE_NOT_FOUND;
import static com.myapp.api.Network.Errors.HTTP_RESPONSE_OK;
import static com.myapp.api.Network.Errors.HTTP_UNKNOWN_ERROR;
import static com.myapp.api.services.RetrofitInstance.getRetrofitInstance;

public class AuthActivity extends BaseActivity {


    private static final String TAG = "AUTH_ACTIVITY";
    private static String USER_NAME = "";
    private static String MODIFIED_USER_EMAIL = "";
    private static String MODIFIED_USER_NAME = "";

    private Auth adminAuth;
    private Auth auth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Button btAdminAuthButton = findViewById(R.id.bt_admin_auth);
        Button btUserAuthButton = findViewById(R.id.bt_user_auth);
        Button btUserRenewToken = findViewById(R.id.bt_renew_user);
        Button btSetUserExternalId = findViewById(R.id.bt_set_identifier);
        Button btOpenTicketEndPoints = findViewById(R.id.bt_tickets);
        Button btOpenSitesEndPoints = findViewById(R.id.bt_sites);

        progressBar = findViewById(R.id.progressBar);


        setTitle();
        //adminAuth = new Auth("",0L);
        final AuthenticationService authenticationService = getRetrofitInstance().create(AuthenticationService.class);

        btAdminAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar(true);
                adminAuth(authenticationService);
            }
        });

        btUserAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (adminAuth == null) {
                    Toast.makeText(AuthActivity.this, "Please authenticate with the admin user first", Toast.LENGTH_SHORT).show();
                } else {
                    showProgressBar(true);
                    userAuth(authenticationService, adminAuth.getAccessToken(), USER_NAME);
                }
            }
        });

        btUserRenewToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auth == null) {
                    Toast.makeText(AuthActivity.this, "Please authenticate with the user first", Toast.LENGTH_SHORT).show();
                } else {
                    showProgressBar(true);
                    renewUserToken(authenticationService, auth.getAccessToken(), USER_NAME);
                }

            }
        });

        btSetUserExternalId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (adminAuth == null) {
                    Toast.makeText(AuthActivity.this, "Please authenticate with the admin user first", Toast.LENGTH_SHORT).show();
                } else {
                    showProgressBar(true);
                    setExternalId(authenticationService, adminAuth.getAccessToken());
                }
            }
        });

        btOpenTicketEndPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTicketActivity();
            }
        });

        btOpenSitesEndPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSitesEndPoints();
            }
        });
    }

    private void setTitle() {
        TextView tvIdAndSecret = (TextView) findViewById(R.id.tv_id_secret);

        String title = "client id: " + getString(R.string.api_id) + "\nclient secret: " + getString(R.string.api_secret);
        tvIdAndSecret.setText(title);
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


    private void openSitesEndPoints() {
        Intent intent = new Intent(this, SitesActivity.class);
        String adminJson;
        String userJson;
        Gson gson = new Gson();

        if (adminAuth == null) {
            adminAuth = new Auth("", 0);
        }
        if (auth == null) {
            auth = new Auth("", 0);
        }
        adminJson = gson.toJson(adminAuth);
        userJson = gson.toJson(auth);
        intent.putExtra("admin_json", adminJson);
        intent.putExtra("user_json", userJson);
        startActivity(intent);
    }

    private void openTicketActivity() {
        Intent intent = new Intent(this, TicketActivity.class);
        String adminJson;
        String userJson;
        Gson gson = new Gson();

        if (adminAuth == null) {
            adminAuth = new Auth("", 0);
        }
        if (auth == null) {
            auth = new Auth("", 0);
        }
        adminJson = gson.toJson(adminAuth);
        userJson = gson.toJson(auth);
        intent.putExtra("admin_json", adminJson);
        intent.putExtra("user_json", userJson);
        startActivity(intent);
    }

    private void setExternalId(AuthenticationService authenticationService, String accessToken) {

        ModifiedArrayUser userArrays = new ModifiedArrayUser(new ArrayList<ModifiedUserBody>() {{
            add(new ModifiedUserBody(MODIFIED_USER_EMAIL, MODIFIED_USER_NAME));
        }});
        Call<ExternalIdResponse> call = authenticationService.setExternalId(accessToken, userArrays);
        call.enqueue(new Callback<ExternalIdResponse>() {
            @Override
            public void onResponse(Call<ExternalIdResponse> call, Response<ExternalIdResponse> response) {
                showProgressBar(false);
                if (response.code() == HTTP_RESPONSE_OK) {
                    Log.d(TAG, "onResponse: " + response.body());
                    ExternalIdResponse externalIdResponse = response.body();
                    Toast.makeText(AuthActivity.this, String.valueOf(externalIdResponse.getNumberUsersModified()), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<ExternalIdResponse> call, Throwable t) {

            }
        });
    }

    private void renewUserToken(AuthenticationService authenticationService, String accessToken, String userName) {
        Call<Auth> call = authenticationService.userRenewToken(accessToken, userName);

        call.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                showProgressBar(false);
                if (response.code() == HTTP_RESPONSE_OK) {
                    Log.d(TAG, "onResponse: " + response.body().getAccessToken());
                    auth = response.body();
                    Toast.makeText(AuthActivity.this, auth.getAccessToken(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AuthActivity.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void userAuth(AuthenticationService authenticationService, String accessToken, String userName) {

        if (adminAuth == null) {
            adminAuth = new Auth("", 0);
        }
        Call<Auth> call = authenticationService.userAuth(accessToken, userName);
        call.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                showProgressBar(false);
                if (response.code() == HTTP_RESPONSE_OK) {
                    Log.d(TAG, "onResponse: " + response.body().getAccessToken());
                    auth = response.body();
                    Toast.makeText(AuthActivity.this, auth.getAccessToken(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AuthActivity.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void adminAuth(AuthenticationService authenticationService) {

        //From the account -> api tab  copy the api_id and api_secret to the string res
        Call<Auth> call = authenticationService.adminAuth(getString(R.string.api_id), getString(R.string.api_secret));
        call.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                showProgressBar(false);
                @Network.Errors int CODE = HTTP_RESPONSE_OK;
                if (response.code() == HTTP_RESPONSE_OK) {
                    Log.d(TAG, "onResponse: " + response.body().getAccessToken());
                    adminAuth = response.body();
                    if (!isDebugMode()) {
                        Toast.makeText(AuthActivity.this, adminAuth.getAccessToken(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<Auth> call, Throwable t) {
                Toast.makeText(AuthActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void printError(int code, ResponseError errorResponse) {
        Log.d(TAG, "onResponse: error code:" + code + "\n" +
                errorResponse.toString());
        if (!isDebugMode()) {
            Toast.makeText(AuthActivity.this, errorResponse.getErrorDescription(), Toast.LENGTH_SHORT).show();
        }
    }


}
