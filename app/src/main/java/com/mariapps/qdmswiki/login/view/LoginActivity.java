package com.mariapps.qdmswiki.login.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.SessionManager;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.custom.CustomButton;
import com.mariapps.qdmswiki.custom.CustomProgressBar;
import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.view.HomeActivity;
import com.mariapps.qdmswiki.login.database.Login;
import com.mariapps.qdmswiki.login.model.LoginRequestObj;
import com.mariapps.qdmswiki.login.model.LoginResponse;
import com.mariapps.qdmswiki.login.presenter.LoginPresenter;
import com.mariapps.qdmswiki.settings.view.SettingsActivity;
import com.mariapps.qdmswiki.utils.CommonUtils;
import com.mariapps.qdmswiki.walkthrough.view.WalkthroughActivity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.PrimitiveIterator;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginView{

    @BindView(R.id.splashIV)
    AppCompatImageView splashIV;
    @BindView(R.id.loginBtn)
    CustomButton loginBtn;
    @BindView(R.id.loadinLoadingPB)
    CustomProgressBar loadinLoadingPB;
    @BindView(R.id.usernameET)
    TextInputEditText usernameET;
    @BindView(R.id.passwordET)
    TextInputEditText passwordET;

    private LoginPresenter loginPresenter;
    private SessionManager sessionManager;
    String userEmail="";
    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 123;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(LoginActivity.this);
        setContentView(R.layout.activity_login);

        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
            }
        }


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anim_slide_up);
        // Start animation
        splashIV.startAnimation(slide_up);
    }

    @Override
    protected void setUpPresenter() {
        loginPresenter = new LoginPresenter(this,this);
    }

    @Override
    protected void isNetworkAvailable(boolean isConnected) {

    }

    private void createFolder(){
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "QDMSWiki");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            // Do something on success
        } else {
            // Do something else on failure
        }
    }

    @OnClick({R.id.loginBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBtn:
                if (!usernameET.getText().toString().equals("") && !passwordET.getText().toString().equals("")) {
                    userEmail=usernameET.getText().toString();
                    Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.anim_scale_down);
                    animation1.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            loginBtn.setVisibility(View.GONE);
                            loadinLoadingPB.showProgressBar();
                            loadinLoadingPB.setVisibility(View.VISIBLE);

                            sessionManager.setDeviceId(CommonUtils.getDeviceId(LoginActivity.this));
                                loginPresenter.getLoggedIn(new LoginRequestObj(usernameET.getText().toString(), passwordET.getText().toString(), sessionManager.getKeyFcmTokenId(), "ANDROID",
                                        sessionManager.getDeviceId(), "1", "Closed"));
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    loginBtn.startAnimation(animation1);
                } else {
                    Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE: {
                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        }
                    }
                }
            }
        }

    }

    @Override
    public void onLoginSuccess(LoginResponse loginResponse) {
        if (loginResponse != null) {
            if (loginResponse.getCommonEntity() != null) {
                if (loginResponse.getCommonEntity().getTransactionstatus() != null && loginResponse.getCommonEntity().getTransactionstatus().equals("Y")) {
                    sessionManager.setLoggedin(true);
                    sessionManager.setUserName(loginResponse.getLoginQdms().getName());
                    sessionManager.setUserId(loginResponse.getLoginQdms().getUserId());
                    sessionManager.setKeyIsSeafarerLogin(loginResponse.getLoginQdms().getIsSeafarerLogin());
                    sessionManager.setUserEmail(userEmail);
                    if (sessionManager.isFirstTimeLaunch()) {
                        Intent intent = new Intent(LoginActivity.this, WalkthroughActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                } else if (loginResponse.getCommonEntity().getMessage() != null) {
                    Toast.makeText(LoginActivity.this, loginResponse.getCommonEntity().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            createFolder();
        }
        loginBtn.setVisibility(View.VISIBLE);
        loadinLoadingPB.setVisibility(View.GONE);
    }

    public void clearData(){
        sessionManager.removeSessionAll();
        HomeDatabase homeDatabase = HomeDatabase.getInstance(LoginActivity.this);
        new AsyncTask<String,Void,String>(){
            @Override
            protected String doInBackground(String... strings) {
                homeDatabase.clearAllTables();
                return "";
            }
        }.execute();
    }

    @Override
    public void onLoginError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        loginBtn.setVisibility(View.VISIBLE);
        loadinLoadingPB.setVisibility(View.GONE);
    }

}
