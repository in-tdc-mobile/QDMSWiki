package com.mariapps.qdmswiki.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.custom.CustomButton;
import com.mariapps.qdmswiki.custom.CustomProgressBar;
import com.mariapps.qdmswiki.home.view.HomeActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anim_slide_up);
        // Start animation
        splashIV.startAnimation(slide_up);
    }

    @Override
    protected void setUpPresenter() {

    }

    @Override
    protected void isNetworkAvailable(boolean isConnected) {

    }

    @OnClick({R.id.loginBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBtn:
               // if(!usernameET.getText().toString().equals("") && !passwordET.getText().toString().equals("")) {
                    Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.anim_scale_down);
                    animation1.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                          Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                          startActivity(intent);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    loginBtn.startAnimation(animation1);
//                }else {
//                    Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
//                }

                break;

        }
    }
}
