package com.mariapps.qdmswiki.settings.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.mariapps.qdmswiki.BuildConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.SessionManager;
import com.mariapps.qdmswiki.applicationinfo.view.ApplicationInfoActivity;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.settings.adapter.SettingsAdapter;
import com.mariapps.qdmswiki.settings.model.SettingsItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity{
    @BindView(R.id.headingTV)
    CustomTextView headingTV;
    @BindView(R.id.backBtn)
    AppCompatImageView backBtn;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarMain)
    AppBarLayout appBarMain;
    @BindView(R.id.listRV)
    RecyclerView listRV;

    private SettingsAdapter settingsAdapter;
    private List<SettingsItem> settingsItems=new ArrayList<>();
    SessionManager sessionManager;

    @Override
    protected void setUpPresenter() {

    }

    @Override
    protected void isNetworkAvailable(boolean isConnected) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        sessionManager=new SessionManager(this);

        initView();
        initRecycler();
    }

    private void initRecycler() {
        listRV.setHasFixedSize(true);
        listRV.setLayoutManager(new LinearLayoutManager(this));
        settingsAdapter = new SettingsAdapter(this,initSettingList());
        settingsAdapter.setSettingsListener(new SettingsAdapter.SettingsListener() {
            @Override
            public void onSettingsClicked(int position) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        Intent intent = new Intent(SettingsActivity.this, ApplicationInfoActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        break;
                }
            }
        });
        listRV.setAdapter(settingsAdapter);
    }

    private void initView() {
        headingTV.setText(getString(R.string.string_settings));
    }

    private List<SettingsItem> initSettingList(){
        settingsItems.clear();
        settingsItems.add(new SettingsItem(R.drawable.ic_settings_inactive,"Settings",R.color.black));
        settingsItems.add(new SettingsItem(R.drawable.ic_help,"Help",R.color.black));
        settingsItems.add(new SettingsItem(R.drawable.ic_help,"App Info",R.color.black));
        settingsItems.add(new SettingsItem(R.drawable.ic_logout,"Logout",R.color.red_900));
        return settingsItems;
    }

    @OnClick(R.id.backBtn)
    public void onClick() {
        onBackPressed();
    }

}
