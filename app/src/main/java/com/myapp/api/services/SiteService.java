package com.myapp.api.services;

import com.myapp.api.model.site.request.DeleteSiteBody;
import com.myapp.api.model.site.request.SiteBody;
import com.myapp.api.model.site.request.SiteListBody;
import com.myapp.api.model.site.response.AddedSite;
import com.myapp.api.model.site.response.DeletedSite;
import com.myapp.api.model.site.response.UpdatedSite;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SiteService {

    // Getting the Site information
    @GET("v2/sites/{id}")
    Call<SiteBody> getSiteInfo(@Header("Authorization") String userToken, @Path("id") String siteId);

    // Add new site
    @POST("v2/sites")
    Call<AddedSite> addNewSite(@Header("Authorization") String userToken, @Body SiteListBody siteListBody);

    // update existing sites
    @PUT("v2/sites")
    Call<UpdatedSite> updateSite(@Header("Authorization") String userToken, @Body SiteListBody siteListBody);

    // delete existing sites
    @POST("v2/sites/delete")
    Call<DeletedSite> deleteSite(@Header("Authorization") String userToken, @Body DeleteSiteBody deleteSiteBody);
}
