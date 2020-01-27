package com.mariapps.qdmswiki.bookmark.presenter;

import android.content.Context;
import android.util.Log;

import com.mariapps.qdmswiki.bookmarks.model.BookmarkEntryModel;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkModel;
import com.mariapps.qdmswiki.bookmarks.view.BookmarkView;
import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.serviceclasses.ApiServiceFactory;
import com.mariapps.qdmswiki.serviceclasses.ServiceController;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class BookmarkPresenter {
    private BookmarkView bookmarkView;
    private ServiceController serviceController;
    private HomeDatabase homeDatabase;
    List<BookmarkEntryModel> bookmarkEntryModels;

    public BookmarkPresenter(BookmarkView bookmarkView, Context context){
        this.bookmarkView=bookmarkView;
        homeDatabase = HomeDatabase.getInstance(context);
        serviceController= ApiServiceFactory.getInstance().getFacade();
    }


    public void getBookMarkEntryList(String documentId) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                bookmarkEntryModels = homeDatabase.homeDao().getBookmarkEntries(documentId);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onComplete() {
                bookmarkView.onBookMarkEntryListSuccess(bookmarkEntryModels);
            }
            @Override
            public void onError(Throwable e) {
                Log.e("frombookmarkremoved",e.getLocalizedMessage());
            }
        });
    }


    public void deleteBookmark(String documentId, String bookmarkId){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteBookmarkEntry(documentId,bookmarkId);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                bookmarkView.onBookmarkDeleteSucess();
            }

            @Override
            public void onError(Throwable e) {

            }
        });

    }
}
