package com.mariapps.qdmswiki.documents.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserModel implements Serializable {
    @SerializedName("_id")
    public Integer id;
    @SerializedName("name")
    public String name;

    public UserModel(Integer id, String name) {
        this.id = id;
        this.name = name;
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
}
