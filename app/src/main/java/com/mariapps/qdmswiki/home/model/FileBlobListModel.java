package com.mariapps.qdmswiki.home.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.home.database.HomeTypeConverter;

@Entity(tableName = "FileBlobListEntity")
@TypeConverters(HomeTypeConverter.class)
public class FileBlobListModel  implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public Long uId;

    @ColumnInfo(name = "Id")
    @SerializedName("id")
    private String id;

    @ColumnInfo(name = "FileBlob")
    @SerializedName("fileBlob")
    private String fileBlob;

    public FileBlobListModel(Long uId, String id, String fileBlob) {
        this.uId = uId;
        this.id = id;
        this.fileBlob = fileBlob;
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

    public String getFileBlob() {
        return fileBlob;
    }

    public void setFileBlob(String fileBlob) {
        this.fileBlob = fileBlob;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected FileBlobListModel(Parcel in) {
        if (in.readByte() == 0) {
            uId = null;
        } else {
            uId = in.readLong();
        }
        id = in.readString();
        fileBlob = in.readString();

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
        dest.writeString(fileBlob);

    }

    public static final Creator<FileBlobListModel> CREATOR = new Creator<FileBlobListModel>() {
        @Override
        public FileBlobListModel createFromParcel(Parcel in) {
            return new FileBlobListModel(in);
        }

        @Override
        public FileBlobListModel[] newArray(int size) {
            return new FileBlobListModel[size];
        }
    };
}
