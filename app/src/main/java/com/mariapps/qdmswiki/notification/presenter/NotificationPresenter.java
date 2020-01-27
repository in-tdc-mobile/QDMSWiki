package com.mariapps.qdmswiki.notification.presenter;

import android.content.Context;

import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.model.ArticleModel;
import com.mariapps.qdmswiki.home.model.CategoryModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.view.HomeView;
import com.mariapps.qdmswiki.notification.model.NotificationModel;
import com.mariapps.qdmswiki.notification.view.NotificationView;
import com.mariapps.qdmswiki.serviceclasses.ApiServiceFactory;
import com.mariapps.qdmswiki.serviceclasses.ServiceController;
import com.mariapps.qdmswiki.usersettings.UserInfoModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class NotificationPresenter {

    private NotificationView notificationView;
    private HomeDatabase homeDatabase;
    private ServiceController serviceController;
    DocumentModel documentModel;
    ArticleModel articleModel;

    public NotificationPresenter(Context context, NotificationView notificationView) {
        this.notificationView = notificationView;
        serviceController = ApiServiceFactory.getInstance().getFacade();
        homeDatabase = HomeDatabase.getInstance(context);
    }


    public void getDocumentDetails(String documentId) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                documentModel = homeDatabase.homeDao().getDocumentDetails(documentId);

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                notificationView.onGetDocumentInfoSuccess(documentModel);
            }
            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void getArticleDetail(String articleId) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                articleModel = homeDatabase.homeDao().getArticleDetail(articleId);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onComplete() {
                notificationView.onGetArticleInfoSuccess(articleModel);
            }
            @Override
            public void onError(Throwable e) {
            }
        });
    }
}
