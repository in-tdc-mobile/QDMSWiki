package com.mariapps.qdmswiki.serviceclasses;

import com.mariapps.qdmswiki.login.model.LoginRequestObj;
import com.mariapps.qdmswiki.login.model.LoginResponse;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by aruna.ramakrishnan on 14-08-2019
 */

public interface QDMSWikiApi {

    @POST("Home/AuthenticateUser")
    Observable<Response<LoginResponse>> getLoggedIn(@Body LoginRequestObj loginRequestObj);

    @POST("Home/GetDownloadUrl")
    Observable<Response<String>> getDownloadUrl();

}
