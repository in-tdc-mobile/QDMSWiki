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
import android.widget.Toast;

import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.model.ArticleModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.presenter.HomePresenter;
import com.mariapps.qdmswiki.notification.adapter.NotificationAdapter;
import com.mariapps.qdmswiki.notification.model.NotificationModel;
import com.mariapps.qdmswiki.notification.model.ReceiverModel;
import com.mariapps.qdmswiki.notification.presenter.NotificationPresenter;
import com.mariapps.qdmswiki.search.view.DocumentViewFragment;
import com.mariapps.qdmswiki.search.view.FolderStructureActivity;

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

public class NotificationActivity extends BaseActivity implements NotificationView{

    @BindView(R.id.headingTV)
    CustomTextView headingTV;
    @BindView(R.id.backBtn)
    AppCompatImageView backBtn;
    @BindView(R.id.rvNotifications)
    CustomRecyclerView rvNotifications;

    private NotificationAdapter notificationAdapter;
    private List<NotificationModel> notificationList = new ArrayList<>();
    private List<ReceiverModel> receiverList = new ArrayList<>();
    private HomeDatabase homeDatabase;
    private NotificationPresenter notificationPresenter;
    private NotificationModel model = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        rvNotifications.setHasFixedSize(true);
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        rvNotifications.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        homeDatabase = HomeDatabase.getInstance(NotificationActivity.this);
        initView();
        initRecycler();
        getNotificationList();
    }

    @Override
    protected void setUpPresenter() {
        notificationPresenter = new NotificationPresenter(this, this);
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
        notificationAdapter.setRowClickListener(new NotificationAdapter.RowClickListener() {
            @Override
            public void onItemClicked(NotificationModel notificationModel) {
                model=notificationModel;
                if(notificationModel.getIsUnread() != null && notificationModel.getIsUnread())
                    //getReceiverList(notificationModel.getReceviers(),notificationModel);
                    deleteNotification(notificationModel);
                if(notificationModel.getEventDescription()==0 || notificationModel.getEventDescription()==0.0){
                    notificationPresenter.getDocumentDetails(notificationModel.getEventId());
                }
                else{
                    notificationPresenter.getArticleDetail(notificationModel.getEventId());
                }
            }
        });
        rvNotifications.setAdapter(notificationAdapter);
    }






    public void deleteNotification(NotificationModel notificationModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteNotification(notificationModel.getId());
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                notificationModel.setIsUnread(false);
                insertNotification(notificationModel);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void insertNotification(final NotificationModel notificationModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (notificationModel != null) {
                    homeDatabase.homeDao().insertNotification(notificationModel);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
             getNotificationList();
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }


    public void getNotificationList() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                notificationList.clear();
                notificationList.addAll(homeDatabase.homeDao().getNotifications());
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onComplete() {
                notificationAdapter.notifyDataSetChanged();
            }
            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void getReceiverList(List<ReceiverModel> receviers, NotificationModel notificationModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                for(int i=0;i<receviers.size();i++){
                    receviers.get(i).setUnread(false);
                    homeDatabase.homeDao().updateNotification(notificationModel.getId(),0);
                }
                notificationModel.setReceviers(receviers);

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


    @Override
    public void onGetDocumentInfoSuccess(DocumentModel documentModel)   {
        try {
            model.setIsUnread(false);
            deleteNotification(model);
            Intent intent = new Intent(NotificationActivity.this, FolderStructureActivity.class);
            intent.putExtra(AppConfig.BUNDLE_PAGE,"Document");
            intent.putExtra(AppConfig.BUNDLE_TYPE, "Document");
            intent.putExtra(AppConfig.BUNDLE_NAME, documentModel.getDocumentName());
            intent.putExtra(AppConfig.BUNDLE_ID, documentModel.getId());
            intent.putExtra(AppConfig.BUNDLE_VERSION,documentModel.getVersion());
            intent.putExtra(AppConfig.BUNDLE_FOLDER_ID, documentModel.getCategoryId());
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Document not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetDocumentInfoError() {

    }

    @Override
    public void onGetArticleInfoSuccess(ArticleModel articleModel) {
        try {
            model.setIsUnread(false);
            deleteNotification(model);
            Intent intent = new Intent(NotificationActivity.this, FolderStructureActivity.class);
            intent.putExtra(AppConfig.BUNDLE_PAGE,"Article");
            intent.putExtra(AppConfig.BUNDLE_TYPE, "Article");
            intent.putExtra(AppConfig.BUNDLE_NAME, articleModel.getArticleName());
            intent.putExtra(AppConfig.BUNDLE_ID, articleModel.getId());
            intent.putExtra(AppConfig.BUNDLE_VERSION,articleModel.getVersion());
            if(articleModel.getCategoryIds().size() > 0)
                intent.putExtra(AppConfig.BUNDLE_FOLDER_ID, articleModel.getCategoryIds().get(0));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Article not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetArticleInfoError() {

    }
}
