package com.mariapps.qdmswiki.home.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "RecentlyViewedEntity")
public class RecentlyViewedModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public Long uId;

    @ColumnInfo(name = "DocumentId")
    private String documentId;

    @ColumnInfo(name = "DocumentName")
    private String documentName;

    @ColumnInfo(name = "CategoryId")
    private String categoryId;

    @ColumnInfo(name = "CategoryName")
    private String categoryName;

    @ColumnInfo(name = "Version")
    private String version;

    @ColumnInfo(name = "ViewedDate")
    private String viewedDate;

    public RecentlyViewedModel(String documentId, String documentName, String categoryId, String categoryName, String version, String viewedDate) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.version = version;
        this.viewedDate = viewedDate;
    }


    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getViewedDate() {
        return viewedDate;
    }

    public void setViewedDate(String viewedDate) {
        this.viewedDate = viewedDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected RecentlyViewedModel(Parcel in) {
        if (in.readByte() == 0) {
            uId = null;
        } else {
            uId = in.readLong();
        }
        documentId = in.readString();
        documentName = in.readString();
        categoryId = in.readString();
        categoryName = in.readString();
        version = in.readString();
        viewedDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (uId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(uId);
        }
        dest.writeString(documentId);
        dest.writeString(documentName);
        dest.writeString(categoryId);
        dest.writeString(categoryName);
        dest.writeString(version);
        dest.writeString(viewedDate);
    }

    public static final Creator<RecentlyViewedModel> CREATOR = new Creator<RecentlyViewedModel>() {
        @Override
        public RecentlyViewedModel createFromParcel(Parcel in) {
            return new RecentlyViewedModel(in);
        }

        @Override
        public RecentlyViewedModel[] newArray(int size) {
            return new RecentlyViewedModel[size];
        }
    };

}
