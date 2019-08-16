package com.mariapps.qdmswiki.home.presenter;

import android.content.Context;

import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.view.HomeView;
import com.mariapps.qdmswiki.serviceclasses.APIException;
import com.mariapps.qdmswiki.serviceclasses.ApiServiceFactory;
import com.mariapps.qdmswiki.serviceclasses.ServiceController;
import com.mariapps.qdmswiki.serviceclasses.SimpleObserver;

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

    public void deleteDocuments(DocumentModel documentModel) {
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
                insertDocuments(documentModel);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void insertDocuments(final DocumentModel documentModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (documentModel != null) {
                    homeDatabase.homeDao().insertDocument(documentModel);
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
