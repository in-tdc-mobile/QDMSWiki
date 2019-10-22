package com.mariapps.qdmswiki.serviceclasses;

import com.mariapps.qdmswiki.home.model.DownloadFilesResponseModel;
import com.mariapps.qdmswiki.login.model.LoginRequestObj;
import com.mariapps.qdmswiki.login.model.LoginResponse;
import com.mariapps.qdmswiki.settings.model.LogoutRequestObj;
import com.mariapps.qdmswiki.settings.model.LogoutRespObj;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by aruna.ramakrishnan on 14-08-2019
 */

public interface ServiceController {

    Observable<LoginResponse> getLoggedIn(LoginRequestObj loginRequestObj);

    Observable<LogoutRespObj> getLoggedOut(LogoutRequestObj logoutRequestObj);

    Observable<DownloadFilesResponseModel> getUrls(String fileName);
}



