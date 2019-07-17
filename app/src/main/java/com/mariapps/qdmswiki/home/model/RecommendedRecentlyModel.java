package com.mariapps.qdmswiki.home.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecommendedRecentlyModel implements Serializable {

    @SerializedName("pId")
    public Integer id;
    @SerializedName("pName")
    public String name;
    @SerializedName("pCategory")
    public String category;
    @SerializedName("pVersionNo")
    public String versionNo;

    public RecommendedRecentlyModel(Integer id, String name, String category, String versionNo) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.versionNo = versionNo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }
}
