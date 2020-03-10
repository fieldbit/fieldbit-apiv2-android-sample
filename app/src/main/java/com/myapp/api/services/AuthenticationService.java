package com.myapp.api.services;

import com.myapp.api.model.auth.request.Auth;
import com.myapp.api.model.auth.request.ModifiedArrayUser;
import com.myapp.api.model.auth.response.ExternalIdResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;

public interface AuthenticationService {

    @GET("v2/admin/auth/token")
    Call<Auth> adminAuth(@Header("api_id") String apiId, @Header("api_secret") String apiSecret);

    @GET("v2/admin/auth/userToken")
    Call<Auth> userAuth(@Header("Authorization") String adminAuth, @Header("userIdentifier") String userName);

    @GET("v2/user/auth/renewToken")
    Call<Auth> userRenewToken(@Header("Authorization") String userAuth, @Header("userIdentifier") String userName);

    @PUT("v2/admin/user")
    Call<ExternalIdResponse> setExternalId(@Header("Authorization") String adminAuth, @Body ModifiedArrayUser modifiedArrayUser);
}
