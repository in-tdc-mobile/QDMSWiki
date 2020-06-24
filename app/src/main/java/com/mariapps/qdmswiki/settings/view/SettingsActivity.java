package com.mariapps.qdmswiki.settings.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mariapps.qdmswiki.APIClient;
import com.mariapps.qdmswiki.ArtDetail;
import com.mariapps.qdmswiki.BookmarkDetail;
import com.mariapps.qdmswiki.BuildConfig;
import com.mariapps.qdmswiki.CatDetail;
import com.mariapps.qdmswiki.DocDetail;
import com.mariapps.qdmswiki.FileDetail;
import com.mariapps.qdmswiki.FormDetail;
import com.mariapps.qdmswiki.ImageDetail;
import com.mariapps.qdmswiki.LogResponse;
import com.mariapps.qdmswiki.NotificationDetail;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.SendIdtoServerModel;
import com.mariapps.qdmswiki.SessionManager;
import com.mariapps.qdmswiki.UserInfoDetail;
import com.mariapps.qdmswiki.UserSetDetail;
import com.mariapps.qdmswiki.applicationinfo.view.ApplicationInfoActivity;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.bookmarks.view.BookMarkActivityAll;
import com.mariapps.qdmswiki.bookmarks.view.BookmarkActivity;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.model.DownloadFilesRequestModel;
import com.mariapps.qdmswiki.home.model.DownloadFilesResponseModel;
import com.mariapps.qdmswiki.home.view.HomeActivity;
import com.mariapps.qdmswiki.login.view.LoginActivity;
import com.mariapps.qdmswiki.serviceclasses.QDMSWikiApi;
import com.mariapps.qdmswiki.settings.adapter.SettingsAdapter;
import com.mariapps.qdmswiki.settings.model.LogoutRequestObj;
import com.mariapps.qdmswiki.settings.model.LogoutRespObj;
import com.mariapps.qdmswiki.settings.model.SettingsItem;
import com.mariapps.qdmswiki.settings.presenter.SettingsPresenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    ProgressDialog progressDialog;

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
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Checking for Updates");
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
                        Intent intent = new Intent(SettingsActivity.this, BookMarkActivityAll.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(SettingsActivity.this, ApplicationInfoActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        try {
                            progressDialog.show();
                            settingsPresenter.getDownloadUrl(new DownloadFilesRequestModel(sessionManager.getKeyLastUpdatedFileName(),sessionManager.getDeviceId(),sessionManager.getUserId()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3:
                        String mailBody = " Comments : " + "\n\n\n" +
                                " Name : " + sessionManager.getUserName() + "\n" +
                                " OS   : Android Version " + android.os.Build.VERSION.RELEASE + "\n" +
                                " App Version : " + BuildConfig.VERSION_NAME + "\n" +
                                " App Name : " +  getResources().getString(R.string.app_name) + "\n" +
                                " Device : " + android.os.Build.MODEL + "\n";
                        Intent sendIntent = new Intent(Intent.ACTION_SEND);
                        sendIntent.setType("text/plain");
                        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@mariapps.com"});
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "QDMWiki Android support [UserID: " +sessionManager.getUserId()+"]");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, mailBody);
                        Uri uri = FileProvider.getUriForFile(SettingsActivity.this, BuildConfig.APPLICATION_ID + ".provider",new File("sdcard/QDMSWiki/qdms_log_file.txt"));
                        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        startActivity(Intent.createChooser(sendIntent, "Send Email"));
                        break;
                    case 4:
                        showdialogforrecheck();
                        break;
                    case 5:
                        createAlert();
                        break;
                }
            }
        });
        listRV.setAdapter(settingsAdapter);
    }

    public void showdialogforrecheck(){
        AlertDialog.Builder bu = new AlertDialog.Builder(SettingsActivity.this);
        bu.setTitle("Are you sure you want to continue?");
        bu.setMessage("This will send a mail and cross check with production data and get as an update if anything is missing");
        bu.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendAllIdstoServer();
                dialog.dismiss();
            }
        });

        bu.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        bu.show();
    }


    public void sendAllIdstoServer(){
        progressDialog.setMessage("Processing your request");
        progressDialog.setCancelable(false);
        progressDialog.show();
     HomeDatabase   homeDatabase = HomeDatabase.getInstance(SettingsActivity.this);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                SendIdtoServerModel sendIdtoServerModel = new SendIdtoServerModel();
                sendIdtoServerModel.setAppVersion(BuildConfig.VERSION_NAME);
                sendIdtoServerModel.setDeviceName(android.os.Build.MODEL);
                sendIdtoServerModel.setDeviceType("ANDROID "+android.os.Build.VERSION.RELEASE);
                sendIdtoServerModel.setUserId(sessionManager.getUserId());


                List<ArtDetail> ArtDetailList = new ArrayList<>();
                List<DocDetail> DocDetailList = new ArrayList<>();
                List<FileDetail> FileDetailList = new ArrayList<>();
                List<FormDetail> FormDetailList = new ArrayList<>();
                List<UserSetDetail> UserSetDetailList = new ArrayList<>();
                List<UserInfoDetail> UserInfoDetailList = new ArrayList<>();
                List<NotificationDetail> NotificationDetailList = new ArrayList<>();
                List<CatDetail> CatDetailList = new ArrayList<>();
                List<ImageDetail> ImageDetailList = new ArrayList<>();
                List<BookmarkDetail> BookmarkDetailList = new ArrayList<>();


                List<String> fileids = new ArrayList<>();
                fileids.addAll(homeDatabase.homeDao().getFileids());
                for (int i = 0; i < fileids.size(); i++) {
                    FileDetailList.add(new FileDetail(fileids.get(i)));
                }

                List<String> articleids = new ArrayList<>();
                articleids.addAll(homeDatabase.homeDao().getartids());
                for (int i = 0; i < articleids.size(); i++) {
                    ArtDetailList.add(new ArtDetail(articleids.get(i)));
                }

                List<String> docids = new ArrayList<>();
                docids.addAll(homeDatabase.homeDao().getdocids());
                for (int i = 0; i < docids.size(); i++) {
                    DocDetailList.add(new DocDetail(docids.get(i)));
                }

                List<String> bookmarkids = new ArrayList<>();
                bookmarkids.addAll(homeDatabase.homeDao().getbookmarkids());
                for (int i = 0; i < bookmarkids.size(); i++) {
                    BookmarkDetailList.add(new BookmarkDetail(bookmarkids.get(i)));
                }

                List<String> formids = new ArrayList<>();
                formids.addAll(homeDatabase.homeDao().getformids());
                for (int i = 0; i < formids.size(); i++) {
                    FormDetailList.add(new FormDetail(formids.get(i)));
                }

                List<String> usersetids = new ArrayList<>();
                usersetids.addAll(homeDatabase.homeDao().getusersetids());
                for (int i = 0; i < usersetids.size(); i++) {
                    UserSetDetailList.add(new UserSetDetail(usersetids.get(i)));
                }

                List<String> notifids = new ArrayList<>();
                notifids.addAll(homeDatabase.homeDao().getnotifids());
                for (int i = 0; i < notifids.size(); i++) {
                    NotificationDetailList.add(new NotificationDetail(notifids.get(i)));
                }

                List<String> userinfoids = new ArrayList<>();
                userinfoids.addAll(homeDatabase.homeDao().getuserinfoids());
                for (int i = 0; i < userinfoids.size(); i++) {
                    UserInfoDetailList.add(new UserInfoDetail(userinfoids.get(i)));
                }

                List<String> catids = new ArrayList<>();
                catids.addAll(homeDatabase.homeDao().getcatids());
                for (int i = 0; i < catids.size(); i++) {
                    CatDetailList.add(new CatDetail(catids.get(i)));
                }



                File mydir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/QDMSWiki/Images");
                if (mydir.exists()) {
                    File[] imageids = mydir.listFiles();
                    for (int i = 0; i < imageids.length; i++) {
                        ImageDetailList.add(new ImageDetail(imageids[i].getName()));
                    }
                }


                sendIdtoServerModel.setArtDetails(ArtDetailList);
                sendIdtoServerModel.setDocDetails(DocDetailList);
                sendIdtoServerModel.setFileDetails(FileDetailList);
                sendIdtoServerModel.setFormDetails(FormDetailList);
                sendIdtoServerModel.setBookmarkDetails(BookmarkDetailList);
                sendIdtoServerModel.setImageDetails(ImageDetailList);
                sendIdtoServerModel.setUserInfoDetails(UserInfoDetailList);
                sendIdtoServerModel.setUserSetDetails(UserSetDetailList);
                sendIdtoServerModel.setNotificationDetails(NotificationDetailList);
                sendIdtoServerModel.setCatDetails(CatDetailList);
                QDMSWikiApi service = APIClient.getClient().create(QDMSWikiApi.class);
                service.sendAllidstoServerapi(sendIdtoServerModel).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject job;
                            try {
                                String resp = response.body().string();
                                job = new JSONObject(resp);
                                JSONObject job1 = job   .getJSONObject("CommonEntity");
                                Log.e("isauth",job1.getString("IsAuthourized"));
                                Log.e("trans",job1.getString("TransactionStatus"));
                                if (job1.getString("TransactionStatus").equals("Y")) {
                                    try {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                AlertDialog.Builder bu = new AlertDialog.Builder(SettingsActivity.this);
                                                bu.setTitle("QDMS Wiki");
                                                bu.setMessage("We will cross check the data and will provide an update if anything is missing");
                                                bu.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                                bu.show();
                                            }
                                        });
                                        Log.e("success alllogtoserver",response.body().toString());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Log.e("success alllogtoserver",response.body().toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        progressDialog.dismiss();
                        try {
                            Log.e("onFail alllogtoserver",t.getLocalizedMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });

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
//        settingsItems.add(new SettingsItem(R.drawable.ic_settings_inactive,"Settings",R.color.black));
////        settingsItems.add(new SettingsItem(R.drawable.ic_help,"Help",R.color.black));
        settingsItems.add(new SettingsItem(R.drawable.ic_bookmark_color,"BookMarks",R.color.black));
        settingsItems.add(new SettingsItem(R.drawable.ic_app_info,"App Info",R.color.black));
        settingsItems.add(new SettingsItem(R.drawable.ic_check_update,"Check for QDMS data updates",R.color.black));
        settingsItems.add(new SettingsItem(R.drawable.app_support,"App Support",R.color.black));
        settingsItems.add(new SettingsItem(R.drawable.recheck_data_icon,"Re-Check for Data",R.color.black));
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

    @Override
    public void onGetDownloadFilesSuccess(DownloadFilesResponseModel downloadFilesResponseModel) {
        progressDialog.dismiss();
        if(downloadFilesResponseModel.getDownloadEntityList() == null || downloadFilesResponseModel.getDownloadEntityList().size() == 0)
            Toast.makeText(SettingsActivity.this,"No updates available",Toast.LENGTH_LONG).show();
        else {
            Intent homeIntent = new Intent(SettingsActivity.this, HomeActivity.class);
            startActivity(homeIntent);
        }
    }

    @Override
    public void onGetDownloadFilesError() {

    }
}
