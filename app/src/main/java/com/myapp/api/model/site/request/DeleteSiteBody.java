package com.myapp.api.model.site.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DeleteSiteBody {
    @SerializedName("ids")
    private ArrayList<String> deletedSites;

    public DeleteSiteBody(ArrayList<String> deletedSites) {
        this.deletedSites = deletedSites;
    }

    public ArrayList<String> getDeletedSites() {
        return deletedSites;
    }

    public void setDeletedSites(ArrayList<String> deletedSites) {
        this.deletedSites = deletedSites;
    }
}
