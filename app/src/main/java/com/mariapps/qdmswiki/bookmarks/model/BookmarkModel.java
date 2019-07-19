package com.mariapps.qdmswiki.bookmarks.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookmarkModel implements Serializable {
    @SerializedName("id")
    public Integer id;
    @SerializedName("bookMark")
    public String bookMark;

    public BookmarkModel(Integer id, String bookMark) {
        this.id = id;
        this.bookMark = bookMark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookMark() {
        return bookMark;
    }

    public void setBookMark(String bookMark) {
        this.bookMark = bookMark;
    }
}
