package com.mariapps.qdmswiki.serviceclasses;

import com.mariapps.qdmswiki.QDMSWikiApplication;
import com.mariapps.qdmswiki.login.model.LoginRequestObj;
import com.mariapps.qdmswiki.login.model.LoginResponse;

import rx.Observable;

/**
 * Created by aruna.ramakrishnan on 14-08-2019
 */


public class ServiceControllerImpl implements ServiceController {
    private QDMSWikiApi service = QDMSWikiApplication.getInstance().getAPI();


    @Override
    public Observable<LoginResponse> getLoggedIn(LoginRequestObj loginRequestObj) {
        return service.getLoggedIn(loginRequestObj).compose(new RXResponseTransformer<>());
    }

    @Override
    public Observable<String> getDownloadUrl() {
        return null;
    }
}