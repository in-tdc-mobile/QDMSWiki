package com.mariapps.qdmswiki.splash.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.SessionManager;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.home.view.HomeActivity;
import com.mariapps.qdmswiki.login.view.LoginActivity;

/**
 * Created by elby.samson on 02,January,2019
 */
public class SplashScreenActivity extends BaseActivity {

    private SessionManager sessionManager;

    @Override
    protected void setUpPresenter() {

    }

    @Override
    protected void isNetworkAvailable(boolean isConnected) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getApplicationContext());
        setContentView(R.layout.activity_splash);
        setTheme(R.style.AppTheme);

        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                if (sessionManager.isLoggedIn()) {
                    SplashScreenActivity.super.startActivityWithFlag(HomeActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK, Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                } else {
                    SplashScreenActivity.super.startActivityWithFlag(LoginActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK, Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();

                }

            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();

    }
}
