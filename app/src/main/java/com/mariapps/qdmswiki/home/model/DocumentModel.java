package com.mariapps.qdmswiki.home.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class DocumentModel implements Serializable {

    @SerializedName("_id")
    public Integer id;
    @SerializedName("appId")
    public String appId;
    @SerializedName("DocumentName")
    public String documentName;
    @SerializedName("DocumentData")
    public String documentData;

    public DocumentModel(Integer id, String appId, String documentName, String documentData) {
        this.id = id;
        this.appId = appId;
        this.documentName = documentName;
        this.documentData = documentData;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentData() {
        return documentData;
    }

    public void setDocumentData(String documentData) {
        this.documentData = documentData;
    }
}
