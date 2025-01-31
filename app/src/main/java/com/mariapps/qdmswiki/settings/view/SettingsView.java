package com.mariapps.qdmswiki.settings.view;


import com.mariapps.qdmswiki.home.model.DownloadFilesResponseModel;
import com.mariapps.qdmswiki.settings.model.LogoutRespObj;

public interface SettingsView {
    void onLogoutSucess(LogoutRespObj logoutRespObj);
    void onLogoutError();

    void onGetDownloadFilesSuccess(DownloadFilesResponseModel downloadFilesResponseModel);
    void onGetDownloadFilesError();

}
