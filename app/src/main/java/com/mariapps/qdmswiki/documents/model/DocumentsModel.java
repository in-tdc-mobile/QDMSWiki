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
    @SerializedName("pDepartments")
    public ArrayList<DepartmentModel> departments;

    public DocumentsModel(Integer id, String documentName, String category, String date, String time, ArrayList<DepartmentModel> departments) {
        this.id = id;
        this.documentName = documentName;
        this.category = category;
        this.date = date;
        this.time = time;
        this.departments = departments;
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

    public ArrayList<DepartmentModel> getDepartments() {
        return departments;
    }
}
