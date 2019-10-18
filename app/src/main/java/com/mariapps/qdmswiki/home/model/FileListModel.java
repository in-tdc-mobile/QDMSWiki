package com.mariapps.qdmswiki.home.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.home.database.HomeTypeConverter;

@Entity(tableName = "FileChunksEntity")
@TypeConverters(HomeTypeConverter.class)
public class FileListModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public Long uId;

    @ColumnInfo(name = "Id")
    @SerializedName("_id")
    private String id;

    @ColumnInfo(name = "FilesId")
    @SerializedName("files_id")
    private String filesId;

    @ColumnInfo(name = "N")
    @SerializedName("n")
    private String n;

    @ColumnInfo(name = "Data")
    @SerializedName("data")
    private String data;

    public FileListModel(Long uId, String id, String filesId, String n, String data) {
        this.uId = uId;
        this.id = id;
        this.filesId = filesId;
        this.n = n;
        data = data;
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

    public String getFilesId() {
        return filesId;
    }

    public void setFilesId(String filesId) {
        this.filesId = filesId;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected FileListModel(Parcel in) {
        if (in.readByte() == 0) {
            uId = null;
        } else {
            uId = in.readLong();
        }
        id = in.readString();
        filesId = in.readString();
        n = in.readString();
        data = in.readString();
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
        dest.writeString(filesId);
        dest.writeString(n);
        dest.writeString(data);
    }

    public static final Creator<FileListModel> CREATOR = new Creator<FileListModel>() {
        @Override
        public FileListModel createFromParcel(Parcel in) {
            return new FileListModel(in);
        }

        @Override
        public FileListModel[] newArray(int size) {
            return new FileListModel[size];
        }
    };
}
