package com.mariapps.qdmswiki.documents.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DepartmentModel implements Serializable {

    @SerializedName("pId")
    public Integer pId;
    @SerializedName("pDepartmentName")
    public String pDepartmentName;

    public DepartmentModel(Integer pId,String pDepartmentName) {
        this.pDepartmentName = pDepartmentName;
        this.pId = pId;
    }

    public String getpDepartmentName() {
        return pDepartmentName;
    }

    public Integer getpId() {
        return pId;
    }
}
