package com.myapp.api.model.ticket.response;

public class ExportToPdf {
    String pdfUrl;

    public ExportToPdf(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }
}
