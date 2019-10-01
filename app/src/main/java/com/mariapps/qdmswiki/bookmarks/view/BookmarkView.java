package com.mariapps.qdmswiki.bookmarks.view;


import com.mariapps.qdmswiki.bookmarks.model.BookmarkEntryModel;

import java.util.List;

public interface BookmarkView {

    void onBookMarkEntryListSuccess(List<BookmarkEntryModel> bookmarkEntryModels);
    void onBookMarkEntryListError();

    void onBookmarkDeleteSucess();
    void onBookmarkDeleteError();
}
