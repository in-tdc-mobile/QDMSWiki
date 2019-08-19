package com.mariapps.qdmswiki.home.presenter;

import android.content.Context;

import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.model.ArticleModel;
import com.mariapps.qdmswiki.home.model.CategoryModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.model.TagModel;
import com.mariapps.qdmswiki.home.view.HomeView;
import com.mariapps.qdmswiki.serviceclasses.APIException;
import com.mariapps.qdmswiki.serviceclasses.ApiServiceFactory;
import com.mariapps.qdmswiki.serviceclasses.ServiceController;
import com.mariapps.qdmswiki.serviceclasses.SimpleObserver;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter {

    private HomeView homeView;
    private HomeDatabase homeDatabase;
    private ServiceController serviceController;
    String url;

    public HomePresenter(Context context, HomeView homeView) {
        this.homeView = homeView;
        serviceController = ApiServiceFactory.getInstance().getFacade();
        homeDatabase = HomeDatabase.getInstance(context);
        url = "https://drive.google.com/uc?export=download&id=1su4nzOn6-wHOay-uCkF1ajOgj47ajbiC";
    }

    public String getDownloadUrl() {
        return url;
//        serviceController.getDownloadUrl()
//                .subscribe(new SimpleObserver<String>() {
//
//
//                    @Override
//                    public void onNext(String downloadUrl) {
//                        super.onNext(downloadUrl);
//                        homeView.onGetDownloadUrlSuccess(downloadUrl);
//                    }
//
//                    @Override
//                    public void onNetworkFailure() {
//                        super.onNetworkFailure();
//                    }
//
//                    @Override
//                    public void onAPIError(APIException e) {
//                        super.onAPIError(e);
//                        homeView.onGetDownloadUrlError(e);
//                    }
//                });
    }

    public void deleteDocuments(ArrayList<DocumentModel> documentModels) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteDocumentEntity();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                insertDocuments(documentModels);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void insertDocuments(final ArrayList<DocumentModel> documentModels) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (documentModels != null) {
                    homeDatabase.homeDao().insertDocument(documentModels);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {

            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void deleteTags(ArrayList<TagModel> tagModelArrayList) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteTagEntity();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                insertTags(tagModelArrayList);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void insertTags(final ArrayList<TagModel> tagModelArrayList) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (tagModelArrayList != null) {
                    homeDatabase.homeDao().insertTag(tagModelArrayList);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {

            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void deleteArticles(ArrayList<ArticleModel> articleModelArrayList) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteArticleEntity();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                insertArticles(articleModelArrayList);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void insertArticles(final ArrayList<ArticleModel> articleModelArrayList) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (articleModelArrayList != null) {
                    homeDatabase.homeDao().insertArticle(articleModelArrayList);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {

            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void deleteCategories(ArrayList<CategoryModel> categoryModelArrayList) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteCategoryEntity();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                insertCategories(categoryModelArrayList);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void insertCategories(final ArrayList<CategoryModel> categoryModelArrayList) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (categoryModelArrayList != null) {
                    homeDatabase.homeDao().insertCategory(categoryModelArrayList);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {

            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }
}
