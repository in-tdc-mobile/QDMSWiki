package com.mariapps.qdmswiki.search.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SearchTypeModel implements Serializable {

    @SerializedName("pId")
    public Integer id;
    @SerializedName("pSearchType")
    public String searchType;

    public SearchTypeModel(Integer id, String searchType) {
        this.id = id;
        this.searchType = searchType;
    }

    public Integer getId() {
        return id;
    }

    public String getSearchType() {
        return searchType;
    }
}
