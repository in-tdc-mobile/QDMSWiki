package com.mariapps.qdmswiki.settings.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mariapps.qdmswiki.BuildConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.SessionManager;
import com.mariapps.qdmswiki.applicationinfo.view.ApplicationInfoActivity;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.home.view.HomeActivity;
import com.mariapps.qdmswiki.login.view.LoginActivity;
import com.mariapps.qdmswiki.settings.adapter.SettingsAdapter;
import com.mariapps.qdmswiki.settings.model.LogoutRequestObj;
import com.mariapps.qdmswiki.settings.model.LogoutRespObj;
import com.mariapps.qdmswiki.settings.model.SettingsItem;
import com.mariapps.qdmswiki.settings.presenter.SettingsPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity implements SettingsView{
    @BindView(R.id.headingTV)
    CustomTextView headingTV;
    @BindView(R.id.backBtn)
    AppCompatImageView backBtn;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarMain)
    AppBarLayout appBarMain;
    @BindView(R.id.listRV)
    CustomRecyclerView listRV;

    private SettingsAdapter settingsAdapter;
    SettingsPresenter settingsPresenter;
    private List<SettingsItem> settingsItems=new ArrayList<>();
    SessionManager sessionManager;

    @Override
    protected void setUpPresenter() {
        settingsPresenter=new SettingsPresenter(this,this);
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
                       createAlert();
                        break;
                }
            }
        });
        listRV.setAdapter(settingsAdapter);
    }

    private void createAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);

        // Set the custom layout as alert dialog view
        builder.setView(dialogView);

        // Get the custom alert dialog view widgets reference
        TextView txtTitle = (TextView) dialogView.findViewById(R.id.txtTitle);
        TextView txtMessage = (TextView) dialogView.findViewById(R.id.txtMessage);
        Button btn_positive = (Button) dialogView.findViewById(R.id.btnPositive);
        Button btn_negative = (Button) dialogView.findViewById(R.id.btnNegative);

        // Create the alert dialog
        final AlertDialog dialog = builder.create();

        txtTitle.setText("Logout");
        txtMessage.setText(R.string.proceedMsg);
        txtTitle.setTextSize(16);
        txtTitle.setTextColor(getResources().getColor(R.color.searchDocument));
        txtMessage.setTextColor(getResources().getColor(R.color.searchDocument));

        btn_negative.setText("NO");
        btn_positive.setText("YES");

        // Set positive/yes button click listener
        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the alert dialog
                dialog.dismiss();
                settingsPresenter.getLoggedOut(new LogoutRequestObj(sessionManager.getUserId(),sessionManager.getDeviceId()));
                sessionManager.removeSession();

                Intent logoutIntent = new Intent(SettingsActivity.this, LoginActivity.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logoutIntent);

            }
        });

        // Set negative/no button click listener
        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the alert dialog
                dialog.dismiss();
            }
        });

        // Display the custom alert dialog on interface
        dialog.show();
    }

    private void initView() {
        headingTV.setText(getString(R.string.string_settings));
    }

    private List<SettingsItem> initSettingList(){
        settingsItems.clear();
        settingsItems.add(new SettingsItem(R.drawable.ic_settings_inactive,"Settings",R.color.black));
        settingsItems.add(new SettingsItem(R.drawable.ic_help,"Help",R.color.black));
        settingsItems.add(new SettingsItem(R.drawable.ic_app_info,"App Info",R.color.black));
        settingsItems.add(new SettingsItem(R.drawable.ic_logout,"Logout",R.color.red_900));
        return settingsItems;
    }

    @OnClick(R.id.backBtn)
    public void onClick() {
        onBackPressed();
    }

    @Override
    public void onLogoutSucess(LogoutRespObj logoutRespObj) {

    }

    @Override
    public void onLogoutError() {

    }
}
