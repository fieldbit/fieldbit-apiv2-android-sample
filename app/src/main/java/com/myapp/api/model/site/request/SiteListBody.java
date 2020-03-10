package com.myapp.api.model.site.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SiteListBody {
    @SerializedName("addAssetsApiRequest")
    private ArrayList<SiteBody> sites;

    public SiteListBody(ArrayList<SiteBody> sites) {
        this.sites = sites;
    }

    public ArrayList<SiteBody> getSites() {
        return sites;
    }

}
