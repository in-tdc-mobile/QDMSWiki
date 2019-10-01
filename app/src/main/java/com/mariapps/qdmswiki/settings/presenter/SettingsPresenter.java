package com.mariapps.qdmswiki.settings.presenter;

import android.content.Context;
import com.mariapps.qdmswiki.serviceclasses.APIException;
import com.mariapps.qdmswiki.serviceclasses.ApiServiceFactory;
import com.mariapps.qdmswiki.serviceclasses.ServiceController;
import com.mariapps.qdmswiki.serviceclasses.SimpleObserver;
import com.mariapps.qdmswiki.settings.model.LogoutRequestObj;
import com.mariapps.qdmswiki.settings.model.LogoutRespObj;
import com.mariapps.qdmswiki.settings.view.SettingsView;

public class SettingsPresenter {
    private SettingsView settingsView;
    private ServiceController serviceController;

    public SettingsPresenter(SettingsView settingsView, Context context){
        this.settingsView=settingsView;
        serviceController= ApiServiceFactory.getInstance().getFacade();
    }


    public void getLoggedOut(LogoutRequestObj logoutRequestObj){
        serviceController.getLoggedOut(logoutRequestObj).subscribe(new SimpleObserver<LogoutRespObj>(){
            @Override
            public void onAPIError(APIException e) {
                super.onAPIError(e);
                settingsView.onLogoutError();
            }

            @Override
            public void onNetworkFailure() {
                super.onNetworkFailure();
            }

            @Override
            public void onNext(LogoutRespObj logoutRespObj) {
                super.onNext(logoutRespObj);
                settingsView.onLogoutSucess(logoutRespObj);
            }
        });
    }
}
