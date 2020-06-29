package com.mariapps.qdmswiki.serviceclasses;

import com.mariapps.qdmswiki.LogResponse;
import com.mariapps.qdmswiki.SendIdtoServerModel;
import com.mariapps.qdmswiki.UpdateStatusModel;
import com.mariapps.qdmswiki.home.model.DownloadFilesRequestModel;
import com.mariapps.qdmswiki.home.model.DownloadFilesResponseModel;
import com.mariapps.qdmswiki.login.model.LoginRequestObj;
import com.mariapps.qdmswiki.login.model.LoginResponse;
import com.mariapps.qdmswiki.settings.model.LogoutRequestObj;
import com.mariapps.qdmswiki.settings.model.LogoutRespObj;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    Observable<Response<DownloadFilesResponseModel>> getUrls(@Body DownloadFilesRequestModel downloadFilesRequestModel);



    @POST("Home/FetchMongoDbById")
    Call<ResponseBody> sendAllidstoServerapi(@Body SendIdtoServerModel sendIdtoServerModel);

    @POST("Home/UpdateLastProcessedStatus")
    Call<ResponseBody> UpdateLastProcessedStatus(@Body UpdateStatusModel updateStatusModel);


    @Multipart
    @POST("Home/SendEmail")
    Call<ResponseBody> senderrorlogs(
            @Part MultipartBody.Part file,
            @Part MultipartBody.Part logfile,
            @Part("userId") RequestBody userId,
            @Part("appVersion") RequestBody appVersion,
            @Part("deviceName") RequestBody deviceName,
            @Part("deviceType") RequestBody deviceType,
            @Part("EmpId") RequestBody EmpId,
            @Part("DeviceId") RequestBody DeviceId
    );

    //" @Part MultipartBody.Part file,"
}
