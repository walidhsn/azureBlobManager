package com.tn.esprit.azureblobstorage.entity;

public class UploadedFile {
    private String url;
    private String uniqueFileName;
    public UploadedFile() {
    }

    public UploadedFile(String url, String uniqueFileName) {
        this.url = url;
        this.uniqueFileName = uniqueFileName;
    }

    public String getUrl() {
        return url;
    }

    public String getUniqueFileName() {
        return uniqueFileName;
    }
}
