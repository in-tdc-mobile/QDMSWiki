package com.mariapps.qdmswiki.home.presenter;

import android.content.Context;
import com.mariapps.qdmswiki.home.view.HomeView;
import com.mariapps.qdmswiki.serviceclasses.APIException;
import com.mariapps.qdmswiki.serviceclasses.ApiServiceFactory;
import com.mariapps.qdmswiki.serviceclasses.ServiceController;
import com.mariapps.qdmswiki.serviceclasses.SimpleObserver;

public class HomePresenter {

    private HomeView homeView;
    private ServiceController serviceController;
    String url;

    public HomePresenter(Context context, HomeView homeView) {
        this.homeView = homeView;
        serviceController = ApiServiceFactory.getInstance().getFacade();
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
}
