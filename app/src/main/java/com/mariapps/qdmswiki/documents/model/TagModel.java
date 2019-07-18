package com.mariapps.qdmswiki.documents.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TagModel implements Serializable {
    @SerializedName("_id")
    public Integer id;
    @SerializedName("appId")
    public Integer appId;
    @SerializedName("Name")
    public String name;
    @SerializedName("IsActive")
    public String isActive;


    public TagModel(Integer id, Integer appId, String name, String isActive) {
        this.id = id;
        this.appId = appId;
        this.name = name;
        this.isActive = isActive;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
