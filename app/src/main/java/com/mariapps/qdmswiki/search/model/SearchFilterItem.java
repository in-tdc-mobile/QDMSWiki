package com.mariapps.qdmswiki.search.model;

/**
 * Created by elby.samson on 05,March,2019
 */
public class SearchFilterItem {
    private String filterName;
    private int filterId;
    private boolean isSelected;

    public SearchFilterItem(String filterName, int filterId, boolean isSelected) {
        this.filterName = filterName;
        this.filterId = filterId;
        this.isSelected=isSelected;
    }

    public String getFilterName() {
        return filterName;
    }

    public int getFilterId() {
        return filterId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public void setFilterId(int filterId) {
        this.filterId = filterId;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
