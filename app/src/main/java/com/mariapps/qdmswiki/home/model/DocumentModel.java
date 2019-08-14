package com.mariapps.qdmswiki.home.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.home.database.HomeTypeConverter;
import java.io.Serializable;
import java.util.List;

@Entity(tableName = "DocumentEntity")
@TypeConverters(HomeTypeConverter.class)
public class DocumentModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public Long uId;

    @ColumnInfo(name = "Id")
    @SerializedName("_id")
    public Integer id;

    @ColumnInfo(name = "CategoryId")
    @SerializedName("CategoryId")
    public String categoryId;

    @ColumnInfo(name = "documentCode")
    @SerializedName("DocumentCode")
    public String documentCode;

    @ColumnInfo(name = "DocumentName")
    @SerializedName("DocumentName")
    public String documentName;

    @ColumnInfo(name = "DocumentData")
    @SerializedName("DocumentData")
    public String documentData;

    @SerializedName("Tags")
    private List<TagModel> tagModelList;

    @SerializedName("CategoryModel")
    private List<CategoryModel> categoryModelList;

    @SerializedName("ArticleModel")
    private List<ArticleModel> articleModelList;

    public DocumentModel(Integer id, String categoryId, String documentCode, String documentName, String documentData, List<TagModel> tagModelList, List<CategoryModel> categoryModelList, List<ArticleModel> articleModelList) {
        this.id = id;
        this.categoryId = categoryId;
        this.documentCode = documentCode;
        this.documentName = documentName;
        this.documentData = documentData;
        this.tagModelList = tagModelList;
        this.categoryModelList = categoryModelList;
        this.articleModelList = articleModelList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentData() {
        return documentData;
    }

    public void setDocumentData(String documentData) {
        this.documentData = documentData;
    }

    public List<TagModel> getTagModelList() {
        return tagModelList;
    }

    public void setTagModelList(List<TagModel> tagModelList) {
        this.tagModelList = tagModelList;
    }

    public List<CategoryModel> getCategoryModelList() {
        return categoryModelList;
    }

    public void setCategoryModelList(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
    }

    public List<ArticleModel> getArticleModelList() {
        return articleModelList;
    }

    public void setArticleModelList(List<ArticleModel> articleModelList) {
        this.articleModelList = articleModelList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected DocumentModel(Parcel in) {
        if (in.readByte() == 0) {
            uId = null;
        } else {
            uId = in.readLong();
        }
        categoryId = in.readString();
        documentCode = in.readString();
        documentName = in.readString();
        documentData = in.readString();
        articleModelList = in.createTypedArrayList(ArticleModel.CREATOR);
        tagModelList = in.createTypedArrayList(TagModel.CREATOR);
        categoryModelList = in.createTypedArrayList(CategoryModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (uId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(uId);
        }
        dest.writeString(categoryId);
        dest.writeString(documentCode);
        dest.writeString(documentName);
        dest.writeString(documentData);
        dest.writeTypedList(articleModelList);
        dest.writeTypedList(tagModelList);
        dest.writeTypedList(categoryModelList);
    }

    public static final Creator<DocumentModel> CREATOR = new Creator<DocumentModel>() {
        @Override
        public DocumentModel createFromParcel(Parcel in) {
            return new DocumentModel(in);
        }

        @Override
        public DocumentModel[] newArray(int size) {
            return new DocumentModel[size];
        }
    };

}
