package com.mariapps.qdmswiki.search.model;


public class SearchTypeModel {

    public Integer id;
    public String searchType;
    private boolean isSelected;

    public SearchTypeModel(Integer id, String searchType, boolean isSelected) {
        this.id = id;
        this.searchType = searchType;
        this.isSelected = isSelected;
    }

    public Integer getId() {
        return id;
    }

    public String getSearchType() {
        return searchType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
