package com.mariapps.qdmswiki.serviceclasses;

import com.mariapps.qdmswiki.login.model.LoginRequestObj;
import com.mariapps.qdmswiki.login.model.LoginResponse;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by aruna.ramakrishnan on 14-08-2019
 */

public interface ServiceController {

    Observable<LoginResponse> getLoggedIn(LoginRequestObj loginRequestObj);

    Observable<String> getDownloadUrl();
}



