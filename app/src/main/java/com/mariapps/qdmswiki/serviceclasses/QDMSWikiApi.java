package com.mariapps.qdmswiki.serviceclasses;

import com.mariapps.qdmswiki.home.model.DownloadFilesResponseModel;
import com.mariapps.qdmswiki.login.model.LoginRequestObj;
import com.mariapps.qdmswiki.login.model.LoginResponse;
import com.mariapps.qdmswiki.settings.model.LogoutRequestObj;
import com.mariapps.qdmswiki.settings.model.LogoutRespObj;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by aruna.ramakrishnan on 14-08-2019
 */

public interface QDMSWikiApi {

    @POST("Home/LoginQdms")
    Observable<Response<LoginResponse>> getLoggedIn(@Body LoginRequestObj loginRequestObj);

    @POST("Home/LogoutQdms")
    Observable<Response<LogoutRespObj>> getLoggedOut(@Body LogoutRequestObj logoutRequestObj);

    @POST("Home/DownloadFiles")
    Observable<Response<DownloadFilesResponseModel>> getUrls(String fileName);

}
