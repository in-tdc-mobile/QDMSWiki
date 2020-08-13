package com.mariapps.qdmswiki;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.home.model.TagModel;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity

public class ArticleModelObj {

    @Id
    public Long uId;


    @SerializedName("_id")
    private String id;

    @SerializedName("ArticleName")
    private String articleName;


    @SerializedName("DocumentData")
    public String documentData;



    public ArticleModelObj(String id, String articleName, String documentData) {
        this.id = id;
        this.articleName = articleName;
        this.documentData = documentData;
    }

    public ArticleModelObj() {
    }

    public Long getuId() {
        return uId;
    }

    public void setuId(Long uId) {
        this.uId = uId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }




    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getDocumentData() {
        return documentData;
    }

    public void setDocumentData(String documentData) {
        this.documentData = documentData;
    }



}
