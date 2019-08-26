package com.mariapps.qdmswiki.login.presenter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.mariapps.qdmswiki.SessionManager;
import com.mariapps.qdmswiki.login.database.Login;
import com.mariapps.qdmswiki.login.database.LoginDatabase;
import com.mariapps.qdmswiki.login.model.LoginRequestObj;
import com.mariapps.qdmswiki.login.model.LoginResponse;
import com.mariapps.qdmswiki.login.view.LoginView;
import com.mariapps.qdmswiki.serviceclasses.APIException;
import com.mariapps.qdmswiki.serviceclasses.ApiServiceFactory;
import com.mariapps.qdmswiki.serviceclasses.ServiceController;
import com.mariapps.qdmswiki.serviceclasses.SimpleObserver;
import com.mariapps.qdmswiki.utils.AppLogger;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter {
    private LoginView loginView;
    private ServiceController serviceController;
    private LoginDatabase loginDatabase;
    private SessionManager sessionManager;

    public LoginPresenter(Context context, LoginView view) {
        this.loginView = view;
        serviceController = ApiServiceFactory.getInstance().getFacade();
        sessionManager = new SessionManager(context);
        loginDatabase = LoginDatabase.getInstance(context);
    }

    public void getLoggedIn(LoginRequestObj loginRequestObj) {

        serviceController.getLoggedIn(loginRequestObj)
                .subscribe(new SimpleObserver<LoginResponse>() {


                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        super.onNext(loginResponse);
                        AppLogger.d("getLoggedIn", loginResponse.toString());
                        loginView.onLoginSuccess(loginResponse);
                    }

                    @Override
                    public void onNetworkFailure() {
                        super.onNetworkFailure();
                    }

                    @Override
                    public void onAPIError(APIException e) {
                        super.onAPIError(e);
                        loginView.onLoginError("Error authenticating user. The username and password is incorrect.");
                    }
                });
    }

    public void insertLogin(final List<Login> logins) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                loginDatabase.loginDao().delete();
                loginDatabase.loginDao().insertAll(logins);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                AppLogger.d("onSubscribe", "Insert");
            }

            @Override
            public void onComplete() {
                AppLogger.d("onCompleted", "Insert");
            }

            @Override
            public void onError(Throwable e) {
                AppLogger.d("onError", "Insert");
            }
        });
    }

    public void insertLoginResponse(final LoginResponse loginResponse) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (loginResponse != null && loginResponse.getLoginQdms() != null) {
                    sessionManager.setLoginParams(true, loginResponse.getLoginQdms().getUserId(), sessionManager.getKeyFcmTokenId(),
                            loginResponse.getLoginQdms().getApitoken(),loginResponse.getLoginQdms().getName(), loginResponse.getLoginQdms().getLoginName());
                    loginDatabase.loginDao().deleteLoginResponse();
                    loginDatabase.loginDao().insetLoginResponse(loginResponse);
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
