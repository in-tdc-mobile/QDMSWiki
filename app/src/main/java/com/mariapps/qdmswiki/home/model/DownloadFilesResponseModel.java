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

        public DownloadEntityList(Long uId, String downloadLink, String fileName, String fileSize) {
            this.uId = uId;
            this.downloadLink = downloadLink;
            this.fileName = fileName;
            this.fileSize = fileSize;
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


        protected DownloadEntityList(Parcel in) {
            if (in.readByte() == 0) {
                uId = null;
            } else {
                uId = in.readLong();
            }
            downloadLink = in.readString();
            fileName = in.readString();
            fileSize = in.readString();
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
