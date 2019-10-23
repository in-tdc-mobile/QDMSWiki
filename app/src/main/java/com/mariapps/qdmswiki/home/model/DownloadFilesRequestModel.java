package com.mariapps.qdmswiki.home.model;

import com.google.gson.annotations.SerializedName;

public class DownloadFilesRequestModel {

    @SerializedName("File_Name")
    private String fileName;

    public DownloadFilesRequestModel(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
