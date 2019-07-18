package com.mariapps.qdmswiki.notification.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.notification.adapter.NotificationAdapter;
import com.mariapps.qdmswiki.notification.model.NotificationModel;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class NotificationActivity extends BaseActivity {

    @BindView(R.id.headingTV)
    CustomTextView headingTV;
    @BindView(R.id.backBtn)
    AppCompatImageView backBtn;
    @BindView(R.id.rvNotifications)
    CustomRecyclerView rvNotifications;

    private NotificationAdapter notificationAdapter;
    private ArrayList<NotificationModel> notificationList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        rvNotifications.setHasFixedSize(true);
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        rvNotifications.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        initView();
        initRecycler();
    }

    @Override
    protected void setUpPresenter() {

    }

    @Override
    protected void isNetworkAvailable(boolean isConnected) {

    }

    @OnClick({R.id.backBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
        }
    }

    private void initView() {
        headingTV.setText(getString(R.string.string_notifications));
    }

    private void initRecycler() {

        notificationList.add(new NotificationModel(1,"General Data Protection Manual","Document Updated",
                "Frank Lasse (BSM DE)","12 hrs ago"));
        notificationList.add(new NotificationModel(2,"Passenger Ship Safety Management","Document Updated",
                "Frank Lasse (BSM DE)","18 hrs ago"));
        notificationList.add(new NotificationModel(3,"General Data Protection Manual","Document Updated",
                "Frank Lasse (BSM DE)","7 days ago"));
        notificationList.add(new NotificationModel(4,"General Data Protection Manual","Document Updated",
                "Frank Lasse (BSM DE)","5 days ago"));
        notificationList.add(new NotificationModel(5,"General Data Protection Manual","Document Updated",
                "Frank Lasse (BSM DE)","12 hrs ago"));
        notificationList.add(new NotificationModel(6,"General Data Protection Manual","Document Updated",
                "Frank Lasse (BSM DE)","12 hrs ago"));
        notificationList.add(new NotificationModel(7,"General Data Protection Manual","Document Updated",
                "Frank Lasse (BSM DE)","12 hrs ago"));


        notificationAdapter = new NotificationAdapter(this, notificationList);
        rvNotifications.setAdapter(notificationAdapter);
    }

}
