package com.mariapps.qdmswiki.bookmarks.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.home.database.HomeTypeConverter;
import com.mariapps.qdmswiki.home.model.ArticleModel;
import com.mariapps.qdmswiki.notification.model.ReceiverModel;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "BookMarkEntity")
@TypeConverters(HomeTypeConverter.class)
public class BookmarkModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public Long uId;

    @ColumnInfo(name = "Id")
    @SerializedName("_id")
    private String id;

    @ColumnInfo(name = "DocumentId")
    @SerializedName("DocumentId")
    private String documentId;

    @ColumnInfo(name = "UserId")
    @SerializedName("UserId")
    private String userId;

    @ColumnInfo(name = "appId")
    @SerializedName("appId")
    private String appId;

    @ColumnInfo(name = "BookmarkEntries")
    @SerializedName("BookmarkEntries")
    private List<BookmarkEntryModel> bookmarkEntries;

    @ColumnInfo(name = "IsActive")
    @SerializedName("IsActive")
    private Boolean isActive;

    public BookmarkModel(Long uId, String id, String documentId, String userId, String appId, List<BookmarkEntryModel> bookmarkEntries, Boolean isActive) {
        this.uId = uId;
        this.id = id;
        this.documentId = documentId;
        this.userId = userId;
        this.appId = appId;
        this.bookmarkEntries = bookmarkEntries;
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

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public List<BookmarkEntryModel> getBookmarkEntries() {
        return bookmarkEntries;
    }

    public void setBookmarkEntries(List<BookmarkEntryModel> bookmarkEntries) {
        this.bookmarkEntries = bookmarkEntries;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    protected BookmarkModel(Parcel in) {
        if (in.readByte() == 0) {
            uId = null;
        } else {
            uId = in.readLong();
        }
        id = in.readString();
        documentId = in.readString();
        userId = in.readString();
        appId = in.readString();
        bookmarkEntries = in.createTypedArrayList(BookmarkEntryModel.CREATOR);
        isActive = in.readByte() != 0;
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
        dest.writeString(id);
        dest.writeString(documentId);
        dest.writeString(userId);
        dest.writeString(appId);
        dest.writeTypedList(bookmarkEntries);
        dest.writeInt(isActive ? 1 : 0);
    }

    public static final Creator<BookmarkModel> CREATOR = new Creator<BookmarkModel>() {
        @Override
        public BookmarkModel createFromParcel(Parcel in) {
            return new BookmarkModel(in);
        }

        @Override
        public BookmarkModel[] newArray(int size) {
            return new BookmarkModel[size];
        }
    };
}
