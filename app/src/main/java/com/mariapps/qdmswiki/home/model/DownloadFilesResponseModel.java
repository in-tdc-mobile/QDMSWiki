package com.mariapps.qdmswiki.home.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkEntryModel;
import com.mariapps.qdmswiki.commonmodels.CommonEntity;

import java.io.Serializable;
import java.util.List;

public class DownloadFilesResponseModel {

    @PrimaryKey(autoGenerate = true)
    public Long uId;

    @SerializedName("DownloadEntityList")
    private List<DownloadEntityList> downloadEntityList;

    @SerializedName("CommonEntity")
    @Embedded
    private CommonEntity commonEntity;

    public Long getuId() {
        return uId;
    }

    public List<DownloadEntityList> getDownloadEntityList() {
        return downloadEntityList;
    }

    public CommonEntity getCommonEntity() {
        return commonEntity;
    }

    public static class DownloadEntityList implements Parcelable {

        @PrimaryKey(autoGenerate = true)
        public Long uId;

        @ColumnInfo(name = "DownloadLink")
        @SerializedName("DownloadLink")
        public String downloadLink;

        @ColumnInfo(name = "FileName")
        @SerializedName("FileName")
        public String fileName;

        @ColumnInfo(name = "FileSize")
        @SerializedName("FileSize")
        public String fileSize;

        @ColumnInfo(name = "Type")
        @SerializedName("Type")
        public String type;

        @ColumnInfo(name = "ZipSize")
        @SerializedName("ZipSize")
        public String ZipSize;


        public DownloadEntityList(Long uId, String downloadLink, String fileName, String fileSize, String type, String zipSize) {
            this.uId = uId;
            this.downloadLink = downloadLink;
            this.fileName = fileName;
            this.fileSize = fileSize;
            this.type = type;
            ZipSize = zipSize;
        }

        public Long getuId() {
            return uId;
        }

        public void setuId(Long uId) {
            this.uId = uId;
        }

        public String getDownloadLink() {
            return downloadLink;
        }

        public void setDownloadLink(String downloadLink) {
            this.downloadLink = downloadLink;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileSize() {
            return fileSize;
        }

        public void setFileSize(String fileSize) {
            this.fileSize = fileSize;
        }

        public String getZipSize() {
            return ZipSize;
        }

        public void setZipSize(String zipSize) {
            ZipSize = zipSize;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        protected DownloadEntityList(Parcel in) {
            if (in.readByte() == 0) {
                uId = null;
            } else {
                uId = in.readLong();
            }
            downloadLink = in.readString();
            fileName = in.readString();
            fileSize = in.readString();
            type = in.readString();
            ZipSize=in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (uId == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeLong(uId);
            }
            dest.writeString(downloadLink);
            dest.writeString(fileName);
            dest.writeString(fileSize);
            dest.writeString(type);
            dest.writeString(ZipSize);
        }


        public static final Creator<DownloadEntityList> CREATOR = new Creator<DownloadEntityList>() {
            @Override
            public DownloadEntityList createFromParcel(Parcel in) {
                return new DownloadEntityList(in);
            }

            @Override
            public DownloadEntityList[] newArray(int size) {
                return new DownloadEntityList[size];
            }
        };
    }


}
