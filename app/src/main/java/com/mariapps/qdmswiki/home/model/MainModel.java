package com.mariapps.qdmswiki.home.model;

import android.arch.persistence.room.TypeConverters;
import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.home.database.HomeTypeConverter;

import java.util.List;

@TypeConverters(HomeTypeConverter.class)
public class MainModel {

    @SerializedName("DocumentCollection")
    private List<DocumentModel> documentModels;
    @SerializedName("Category")
    private List<CategoryModel> categoryModels;
    @SerializedName("Article")
    private List<ArticleModel> articleModels;

    public List<DocumentModel> getDocumentCollection() {
        return documentModels;
    }

    public void setDocumentCollection(List<DocumentModel> documentModels) {
        this.documentModels = documentModels;
    }

    public List<CategoryModel> getCategory() {
        return categoryModels;
    }

    public void setCategory(List<CategoryModel> categoryModels) {
        this.categoryModels = categoryModels;
    }

    public List<ArticleModel> getArticle() {
        return articleModels;
    }

    public void setArticle(List<ArticleModel> articleModels) {
        this.articleModels = articleModels;
    }
}
