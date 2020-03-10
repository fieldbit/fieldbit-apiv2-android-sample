package com.myapp.api;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.myapp.api.model.ResponseError;
import com.myapp.api.model.auth.request.Auth;
import com.myapp.api.model.site.request.DeleteSiteBody;
import com.myapp.api.model.site.request.SiteBody;
import com.myapp.api.model.site.request.SiteListBody;
import com.myapp.api.model.site.response.AddedSite;
import com.myapp.api.model.site.response.DeletedSite;
import com.myapp.api.model.site.response.UpdatedSite;
import com.myapp.api.services.RetrofitInstance;
import com.myapp.api.services.SiteService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.myapp.api.Network.Errors.HTTP_RESPONSE_AUTHENTICATION_ERROR;
import static com.myapp.api.Network.Errors.HTTP_RESPONSE_AUTHORIZATION_ERROR;
import static com.myapp.api.Network.Errors.HTTP_RESPONSE_NOT_FOUND;
import static com.myapp.api.Network.Errors.HTTP_RESPONSE_OK;
import static com.myapp.api.Network.Errors.HTTP_UNKNOWN_ERROR;

public class SitesActivity extends BaseActivity {


    private static final String TAG = "SITE_ACTIVITY";


    private static final String SITE_ID = "";
    private static final String SITE_NAME = "";
    private static final String SITE_ADDRESS = "";
    private static final String SITE_LATITUDE = "";
    private static final String SITE_LONGITUDE = "";
    private static final String DELETED_SITE_ID = "";
    private Auth adminAuth;
    private Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sites);

        Button btGetSite = findViewById(R.id.bt_get_site);
        Button btAddSite = findViewById(R.id.bt_add_new_site);
        Button btUpdateSite = findViewById(R.id.bt_update_site);
        Button btDeleteSite = findViewById(R.id.bt_delete_site);

        initiateAuthToken();
        final SiteService siteService = RetrofitInstance.getRetrofitInstance().create(SiteService.class);

        btGetSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSiteInfo(siteService, adminAuth.getAccessToken(), SITE_ID);
            }
        });

        btAddSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewSite(siteService, adminAuth.getAccessToken());
            }
        });

        btUpdateSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateExistingSite(siteService, adminAuth.getAccessToken());
            }
        });

        btDeleteSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteExistingSite(siteService, adminAuth.getAccessToken());
            }
        });
    }

    private void deleteExistingSite(SiteService siteService, String accessToken) {
        ArrayList<String> arraySites = new ArrayList<String>() {
            {
                add(DELETED_SITE_ID);
            }
        };

        DeleteSiteBody deletedSites = new DeleteSiteBody(arraySites);
        Call<DeletedSite> call = siteService.deleteSite(accessToken, deletedSites);
        call.enqueue(new Callback<DeletedSite>() {
            @Override
            public void onResponse(Call<DeletedSite> call, Response<DeletedSite> response) {
                if (response.code() == HTTP_RESPONSE_OK) {
                    Log.d(TAG, "onResponse: " + response.body());
                    DeletedSite deletedSite = response.body();
                    Toast.makeText(SitesActivity.this, "site removed: " + deletedSite.getSitesRemoved(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<DeletedSite> call, Throwable t) {
                Toast.makeText(SitesActivity.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateExistingSite(SiteService siteService, String accessToken) {
        final SiteBody firstSite = new SiteBody(
                SITE_ID,
                SITE_NAME,
                SITE_ADDRESS,
                SITE_LATITUDE,
                SITE_LONGITUDE
        );
        ArrayList<SiteBody> arraySites = new ArrayList<SiteBody>() {
            {
                add(firstSite);
            }
        };

        SiteListBody sites = new SiteListBody(arraySites);
        Call<UpdatedSite> call = siteService.updateSite(accessToken, sites);
        call.enqueue(new Callback<UpdatedSite>() {
            @Override
            public void onResponse(Call<UpdatedSite> call, Response<UpdatedSite> response) {
                if (response.code() == HTTP_RESPONSE_OK) {
                    Log.d(TAG, "onResponse: " + response.body());
                    UpdatedSite updatedSite = response.body();
                    Toast.makeText(SitesActivity.this, "sites updated: " + updatedSite.getSitesUpdated(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<UpdatedSite> call, Throwable t) {
                Toast.makeText(SitesActivity.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNewSite(SiteService siteService, String accessToken) {
        final SiteBody firstSite = new SiteBody(
                SITE_ID,
                SITE_NAME,
                SITE_ADDRESS,
                SITE_LATITUDE,
                SITE_LONGITUDE
        );
        ArrayList<SiteBody> arraySites = new ArrayList<SiteBody>() {
            {
                add(firstSite);
            }
        };

        SiteListBody sites = new SiteListBody(arraySites);
        Call<AddedSite> call = siteService.addNewSite(accessToken, sites);
        call.enqueue(new Callback<AddedSite>() {
            @Override
            public void onResponse(Call<AddedSite> call, Response<AddedSite> response) {
                if (response.code() == HTTP_RESPONSE_OK) {
                    AddedSite siteAdded = response.body();
                    Log.d(TAG, "onResponse: " + response.body());
                    if (!isDebugMode()) {
                        Toast.makeText(SitesActivity.this, siteAdded.getSiteAdded(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<AddedSite> call, Throwable t) {
                Toast.makeText(SitesActivity.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSiteInfo(SiteService siteService, String accessToken, String siteId) {

        Call<SiteBody> call = siteService.getSiteInfo(accessToken, siteId);
        call.enqueue(new Callback<SiteBody>() {
            @Override
            public void onResponse(Call<SiteBody> call, Response<SiteBody> response) {
                if (response.code() == HTTP_RESPONSE_OK) {
                    SiteBody site = response.body();
                    Log.d(TAG, "onResponse: " + site.toString());
                    if (!isDebugMode()) {
                        Toast.makeText(SitesActivity.this, site.getId(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<SiteBody> call, Throwable t) {
                Toast.makeText(SitesActivity.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Initialize users(Admin and user) from the Main activity
    private void initiateAuthToken() {
        Gson gson = new Gson();
        adminAuth = gson.fromJson(getIntent().getStringExtra("admin_json"), Auth.class);
        auth = gson.fromJson(getIntent().getStringExtra("user_json"), Auth.class);
    }


    private void printError(int code, ResponseError errorResponse) {
        Log.d(TAG, "onResponse: error code:" + code + "\n" +
                errorResponse.toString());
        if (!isDebugMode()) {
            Toast.makeText(SitesActivity.this, errorResponse.getErrorDescription(), Toast.LENGTH_SHORT).show();
        }
    }
}
