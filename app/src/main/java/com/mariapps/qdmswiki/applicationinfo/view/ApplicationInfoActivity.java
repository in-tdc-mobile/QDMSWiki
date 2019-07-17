package com.mariapps.qdmswiki.applicationinfo.view;

import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mariapps.qdmswiki.BuildConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.SessionManager;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.home.view.HomeActivity;
import com.mariapps.qdmswiki.notification.view.NotificationActivity;
import com.mariapps.qdmswiki.settings.view.SettingsActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ApplicationInfoActivity extends BaseActivity {
    @BindView(R.id.headingTV)
    CustomTextView headingTV;
    @BindView(R.id.homeTV)
    CustomTextView homeTV;
    @BindView(R.id.accountsTV)
    CustomTextView accountsTV;
    @BindView(R.id.applicationInfoTV)
    CustomTextView applicationInfoTV;
    @BindView(R.id.applicationNameTV)
    CustomTextView applicationNameTV;
    @BindView(R.id.applicationVersionTV)
    CustomTextView applicationVersionTV;
    @BindView(R.id.dateTV)
    CustomTextView dateTV;
    @BindView(R.id.environmentTV)
    CustomTextView environmentTV;
    @BindView(R.id.clientTV)
    CustomTextView clientTV;

    private SessionManager sessionManager;
    private ArrayList<String> galleryList = new ArrayList<>();
    private ArrayList<String> details = new ArrayList<>();
    private View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_info);
        ButterKnife.bind(this);
        sessionManager=new SessionManager(this);

        initView();
        setDetails();
    }

    private void setDetails() {
        applicationNameTV.setText(getResources().getString(R.string.app_name));
        applicationVersionTV.setText(BuildConfig.VERSION_NAME);
        dateTV.setText("06-MAY-2019");
        environmentTV.setText("Office");
        clientTV.setText("BSM");
    }

    @Override
    protected void setUpPresenter() {

    }

    @Override
    protected void isNetworkAvailable(boolean isConnected) {

    }

    private void initView() {
        headingTV.setText(getString(R.string.string_application_info));
    }

    @OnClick()
    public void onClick() {
        onBackPressed();
    }

    @OnClick({R.id.backBtn, R.id.homeTV, R.id.accountsTV})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.homeTV:
                Intent homeIntent = new Intent(ApplicationInfoActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                break;
            case R.id.accountsTV:
                Intent settingsIntent = new Intent(ApplicationInfoActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
        }
    }
}
