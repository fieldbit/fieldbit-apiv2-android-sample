package com.myapp.api.model.site.response;

public class DeletedSite {
    private String sitesRemoved;

    public DeletedSite(String siteUpdated) {
        this.sitesRemoved = siteUpdated;
    }

    public String getSitesRemoved() {
        return sitesRemoved;
    }
}
