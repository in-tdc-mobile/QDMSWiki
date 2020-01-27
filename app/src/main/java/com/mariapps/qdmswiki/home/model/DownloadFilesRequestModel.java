package com.mariapps.qdmswiki.home.model;

import com.google.gson.annotations.SerializedName;

public class DownloadFilesRequestModel {

    @SerializedName("File_Name")
    private String fileName;
    @SerializedName("deviceid")
    private String deviceid;
    @SerializedName("empid")
    private String empid;


    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public DownloadFilesRequestModel(String fileName, String deviceid, String empid) {
        this.fileName = fileName;
        this.deviceid = deviceid;
        this.empid = empid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
