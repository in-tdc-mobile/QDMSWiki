package com.mariapps.qdmswiki.login.view;

import com.mariapps.qdmswiki.login.model.LoginResponse;

public interface LoginView {

    void onLoginSuccess(LoginResponse loginResponse);

    void onLoginError(String msg);
}
