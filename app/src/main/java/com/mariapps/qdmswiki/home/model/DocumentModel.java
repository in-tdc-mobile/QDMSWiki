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
    public String id;

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

    @ColumnInfo(name = "Version")
    @SerializedName("Version")
    public String version;

    @ColumnInfo(name = "IsPublished")
    @SerializedName("IsPublished")
    public boolean isPublished;

    @SerializedName("Tags")
    private List<TagModel> tagModelList;


    public DocumentModel(String id, String categoryId, String documentCode, String documentName, String documentData, String version, boolean isPublished, List<TagModel> tagModelList) {
        this.id = id;
        this.categoryId = categoryId;
        this.documentCode = documentCode;
        this.documentName = documentName;
        this.documentData = documentData;
        this.version = version;
        this.isPublished = isPublished;
        this.tagModelList = tagModelList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    public List<TagModel> getTagModelList() {
        return tagModelList;
    }

    public void setTagModelList(List<TagModel> tagModelList) {
        this.tagModelList = tagModelList;
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
        version = in.readString();
        isPublished = in.readInt() == 1;
        tagModelList = in.createTypedArrayList(TagModel.CREATOR);
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
        dest.writeString(version);
        dest.writeInt(isPublished ? 1 : 0);
        dest.writeTypedList(tagModelList);
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
