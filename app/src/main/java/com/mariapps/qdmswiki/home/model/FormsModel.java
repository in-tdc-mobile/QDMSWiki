package com.mariapps.qdmswiki.home.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.home.database.HomeTypeConverter;

@Entity(tableName = "FormsEntity")
@TypeConverters(HomeTypeConverter.class)
public class FormsModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public Long uId;

    @ColumnInfo(name = "Id")
    @SerializedName("_id")
    private String id;

    @ColumnInfo(name = "FileName")
    @SerializedName("FileName")
    private String fileName;

    @ColumnInfo(name = "FileContentType")
    @SerializedName("FileContentType")
    private String fileContentType;

    @ColumnInfo(name = "ContentLength")
    @SerializedName("ContentLength")
    private Integer contentLength;

    @ColumnInfo(name = "FileID")
    @SerializedName("FileID")
    private String fileID;

    @ColumnInfo(name = "categoryID")
    @SerializedName("categoryID")
    private String categoryID;

    @ColumnInfo(name = "FileExtention")
    @SerializedName("FileExtention")
    private String fileExtention;

    public FormsModel(Long uId, String id,String fileName, String fileContentType, Integer contentLength, String fileID, String categoryID, String fileExtention) {
        this.uId = uId;
        this.id = id;
        this.fileName = fileName;
        this.fileContentType = fileContentType;
        this.contentLength = contentLength;
        this.fileID = fileID;
        this.categoryID = categoryID;
        this.fileExtention = fileExtention;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public Integer getContentLength() {
        return contentLength;
    }

    public void setContentLength(Integer contentLength) {
        this.contentLength = contentLength;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getFileExtention() {
        return fileExtention;
    }

    public void setFileExtention(String fileExtention) {
        this.fileExtention = fileExtention;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    protected FormsModel(Parcel in) {
        if (in.readByte() == 0) {
            uId = null;
        } else {
            uId = in.readLong();
        }
        id = in.readString();
        fileName = in.readString();
        fileContentType = in.readString();
        contentLength = in.readInt();
        fileID = in.readString();
        categoryID = in.readString();
        fileExtention = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (uId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(uId);
        }
        dest.writeString(id);
        dest.writeString(fileName);
        dest.writeString(fileContentType);
        dest.writeInt(contentLength);
        dest.writeString(fileID);
        dest.writeString(categoryID);
        dest.writeString(fileExtention);
    }

    public static final Creator<FormsModel> CREATOR = new Creator<FormsModel>() {
        @Override
        public FormsModel createFromParcel(Parcel in) {
            return new FormsModel(in);
        }

        @Override
        public FormsModel[] newArray(int size) {
            return new FormsModel[size];
        }
    };

}


