package com.mariapps.qdmswiki.documents.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class DocumentsModel implements Serializable {

    @SerializedName("pId")
    public Integer id;
    @SerializedName("pDocumentName")
    public String documentName;
    @SerializedName("pCategory")
    public String category;
    @SerializedName("pDate")
    public String date;
    @SerializedName("pTime")
    public String time;
    @SerializedName("pVersionNo")
    public String versionNo;
    @SerializedName("pDepartments")
    public ArrayList<TagModel> departments;

    public DocumentsModel(Integer id, String documentName, String category, String date, String time, ArrayList<TagModel> departments) {
        this.id = id;
        this.documentName = documentName;
        this.category = category;
        this.date = date;
        this.time = time;
        this.departments = departments;
    }

    public DocumentsModel(Integer id, String documentName, String category, String versionNo) {
        this.id = id;
        this.documentName = documentName;
        this.category = category;
        this.versionNo = versionNo;
    }

    public Integer getId() {
        return id;
    }

    public String getDocumentName() {
        return documentName;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public ArrayList<TagModel> getDepartments() {
        return departments;
    }

    public String getVersionNo() {
        return versionNo;
    }
}
