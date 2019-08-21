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

@Entity(tableName = "BookMarkEntryEntity")
@TypeConverters(HomeTypeConverter.class)
public class BookmarkEntryModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public Long uId;

    @ColumnInfo(name = "BookmarkId")
    @SerializedName("BookmarkId")
    private String bookmarkId;

    @ColumnInfo(name = "BookmarkTitle")
    @SerializedName("BookmarkTitle")
    private String bookmarkTitle;

    public String getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(String bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public String getBookmarkTitle() {
        return bookmarkTitle;
    }

    public void setBookmarkTitle(String bookmarkTitle) {
        this.bookmarkTitle = bookmarkTitle;
    }

    public BookmarkEntryModel(Long uId, String bookmarkId, String bookmarkTitle) {
        this.uId = uId;
        this.bookmarkId = bookmarkId;
        this.bookmarkTitle = bookmarkTitle;
    }

    public static final Creator<BookmarkEntryModel> CREATOR = new Creator<BookmarkEntryModel>() {
        @Override
        public BookmarkEntryModel createFromParcel(Parcel in) {
            return new BookmarkEntryModel(in);
        }

        @Override
        public BookmarkEntryModel[] newArray(int size) {
            return new BookmarkEntryModel[size];
        }
    };

    protected BookmarkEntryModel(Parcel in) {
        if (in.readByte() == 0) {
            uId = null;
        } else {
            uId = in.readLong();
        }
        bookmarkId = in.readString();
        bookmarkTitle = in.readString();
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
        dest.writeString(bookmarkId);
        dest.writeString(bookmarkTitle);
    }
}
