package com.mariapps.qdmswiki.home.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.home.database.HomeTypeConverter;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "ArticleEntity")
@TypeConverters(HomeTypeConverter.class)
public class ArticleModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public Long uId;

    @ColumnInfo(name = "Id")
    @SerializedName("_id")
    private String id;

    @ColumnInfo(name = "AppId")
    @SerializedName("appId")
    private String appId;

    @ColumnInfo(name = "DraftId")
    @SerializedName("DraftId")
    private String draftId;

    @ColumnInfo(name = "ArticleName")
    @SerializedName("ArticleName")
    private String articleName;

    @ColumnInfo(name = "DocumentData")
    @SerializedName("DocumentData")
    public String documentData;

    @ColumnInfo(name = "CategoryIds")
    @SerializedName("CategoryId")
    public List<String> categoryIds;

    @ColumnInfo(name = "IsActive")
    @SerializedName("IsActive")
    private boolean isActive;

    public ArticleModel(String id, String appId, String draftId, String articleName, String documentData, List<String> categoryIds, boolean isActive) {
        this.id = id;
        this.appId = appId;
        this.draftId = draftId;
        this.articleName = articleName;
        this.documentData = documentData;
        this.categoryIds = categoryIds;
        this.isActive = isActive;
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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDraftId() {
        return draftId;
    }

    public void setDraftId(String draftId) {
        this.draftId = draftId;
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

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(ArrayList<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected ArticleModel(Parcel in) {
        id = in.readString();
        appId = in.readString();
        draftId = in.readString();
        articleName = in.readString();
        documentData = in.readString();
        categoryIds = in.readArrayList(ArticleModel.class.getClassLoader());
        isActive = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(appId);
        dest.writeString(draftId);
        dest.writeString(articleName);
        dest.writeString(documentData);
        dest.writeList(categoryIds);
        dest.writeByte((byte) (isActive ? 1 : 0));
    }

    public static final Creator<ArticleModel> CREATOR = new Creator<ArticleModel>() {
        @Override
        public ArticleModel createFromParcel(Parcel in) {
            return new ArticleModel(in);
        }

        @Override
        public ArticleModel[] newArray(int size) {
            return new ArticleModel[size];
        }
    };

}
