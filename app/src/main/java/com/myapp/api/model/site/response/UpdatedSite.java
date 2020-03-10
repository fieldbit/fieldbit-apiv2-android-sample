package com.myapp.api.model.site.response;

public class UpdatedSite {
    private String sitesUpdated;

    public UpdatedSite(String siteUpdated) {
        this.sitesUpdated = siteUpdated;
    }

    public String getSitesUpdated() {
        return sitesUpdated;
    }
}
