package com.mariapps.qdmswiki.search.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SearchModel implements Serializable {

    @SerializedName("pId")
    public Integer id;
    @SerializedName("pType")
    public String type;
    @SerializedName("pName")
    public String name;
    @SerializedName("pDescription")
    public String description;

    public SearchModel(Integer id, String type, String name, String description) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
