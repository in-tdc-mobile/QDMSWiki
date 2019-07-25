package com.mariapps.qdmswiki.search.model;

import android.print.PrinterId;

/**
 * Created by elby.samson on 06,March,2019
 */
public class FilterBooleanItem {

    private boolean isFolderSelected;
    private boolean isDocumentSelected;
    private boolean isArticleSelected;
    private boolean isFormSelected;
    private String charString;

    public FilterBooleanItem(boolean isFolderSelected, boolean isDocumentSelected, boolean isArticleSelected, boolean isFormSelected, String charString) {
        this.isFolderSelected = isFolderSelected;
        this.isDocumentSelected = isDocumentSelected;
        this.isArticleSelected = isArticleSelected;
        this.isFormSelected = isFormSelected;
        this.charString = charString;
    }

    public boolean isFolderSelected() {
        return isFolderSelected;
    }

    public void setFolderSelected(boolean folderSelected) {
        isFolderSelected = folderSelected;
    }

    public boolean isDocumentSelected() {
        return isDocumentSelected;
    }

    public void setDocumentSelected(boolean documentSelected) {
        isDocumentSelected = documentSelected;
    }

    public boolean isArticleSelected() {
        return isArticleSelected;
    }

    public void setArticleSelected(boolean articleSelected) {
        isArticleSelected = articleSelected;
    }

    public boolean isFormSelected() {
        return isFormSelected;
    }

    public void setFormSelected(boolean formSelected) {
        isFormSelected = formSelected;
    }

    public String getCharString() {
        return charString;
    }

    public void setCharString(String charString) {
        this.charString = charString;
    }
}
