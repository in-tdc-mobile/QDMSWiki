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
import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.notification.adapter.NotificationAdapter;
import com.mariapps.qdmswiki.notification.model.NotificationModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class NotificationActivity extends BaseActivity {

    @BindView(R.id.headingTV)
    CustomTextView headingTV;
    @BindView(R.id.backBtn)
    AppCompatImageView backBtn;
    @BindView(R.id.rvNotifications)
    CustomRecyclerView rvNotifications;

    private NotificationAdapter notificationAdapter;
    private List<NotificationModel> notificationList = new ArrayList<>();
    private HomeDatabase homeDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        rvNotifications.setHasFixedSize(true);
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        rvNotifications.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        homeDatabase = HomeDatabase.getInstance(NotificationActivity.this);

        initView();
        getNotificationList();
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
        notificationAdapter = new NotificationAdapter(this, notificationList);
        rvNotifications.setAdapter(notificationAdapter);
    }

    public void getNotificationList() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                notificationList = homeDatabase.homeDao().getNotifications();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                initRecycler();
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

}
