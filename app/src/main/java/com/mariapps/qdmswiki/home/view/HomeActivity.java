package com.mariapps.qdmswiki.home.view;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.SessionManager;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkEntryModel;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkModel;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.custom.CustomViewPager;
import com.mariapps.qdmswiki.custom.taptargetview.TapTarget;
import com.mariapps.qdmswiki.custom.taptargetview.TapTargetView;
import com.mariapps.qdmswiki.home.database.HomeDao;
import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.model.ArticleModel;
import com.mariapps.qdmswiki.home.model.CategoryModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.model.FileBlobListModel;
import com.mariapps.qdmswiki.home.model.FileListModel;
import com.mariapps.qdmswiki.home.model.FormsModel;
import com.mariapps.qdmswiki.home.model.ImageModel;
import com.mariapps.qdmswiki.home.model.MainModel;
import com.mariapps.qdmswiki.home.model.TagModel;
import com.mariapps.qdmswiki.home.presenter.HomePresenter;
import com.mariapps.qdmswiki.notification.model.NotificationModel;
import com.mariapps.qdmswiki.notification.model.ReceiverModel;
import com.mariapps.qdmswiki.notification.view.NotificationActivity;
import com.mariapps.qdmswiki.search.view.FolderStructureActivity;
import com.mariapps.qdmswiki.serviceclasses.APIException;
import com.mariapps.qdmswiki.settings.view.SettingsActivity;
import com.mariapps.qdmswiki.usersettings.UserInfoModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsCategoryModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsTagModel;
import com.mariapps.qdmswiki.utils.ScreenUtils;
import com.mariapps.qdmswiki.utils.ShowCasePreferenceUtil;
import com.squareup.okhttp.ResponseBody;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.DownloadNotification;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.FetchNotificationManager;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.DownloadBlock;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class HomeActivity extends BaseActivity implements HomeView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.frameLayoutOne)
    FrameLayout frameLayoutOne;
    @BindView(R.id.frameLayoutTwo)
    FrameLayout frameLayoutTwo;
    @BindView(R.id.linLayout)
    LinearLayout linLayout;
    @BindView(R.id.progressLayout)
    LinearLayout progressLayout;
    @BindView(R.id.relLayout)
    RelativeLayout relLayout;
    @BindView(R.id.navFL)
    FrameLayout navFL;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.mainVP)
    CustomViewPager mainVP;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottom_navigation;
    @BindView(R.id.userImageIV)
    AppCompatImageView userImageIV;
    @BindView(R.id.notificationIV)
    AppCompatImageView notificationIV;
    @BindView(R.id.appBarMain)
    AppBarLayout appBarMain;
    @BindView(R.id.notificationsBadgeTextView)
    CustomTextView notificationsBadgeTextView;
    @BindView(R.id.donut_progress)
    DonutProgress donut_progress;

    private ActionBarDrawerToggle mDrawerToggle;
    private HomePresenter homePresenter;
    private HomeDatabase homeDatabase;
    private MainViewPager mainViewPager;
    private NavigationDrawerFragment navigationDrawerFragment;
    private SessionManager sessionManager;
    int currentPosition = 0;
    private int newPosition = 0;
    private HomeActivity context;
    private ProgressDialog progressDialog;
    private JSONObject jObj;
    private int downloadID;
    private HomeDao homeDao;
    private Gson gson;
    private int progressBarStatus;
    private Handler progressBarHandler = new Handler();
    private MainModel mainModel;
    private DownloadManager downloadManager;
    private ShowCasePreferenceUtil util;
    private boolean isSearchShowCaseShown = false;
    public static NotificationManager mNotifyManager;
    public static NotificationCompat.Builder notificationBuilder;
    private FetchListener fetchListener;

    private DocumentModel documentModel;
    List<DocumentModel> childList = new ArrayList<>();
    List<DocumentModel> parentFolderList = new ArrayList<>();
    List<DocumentModel> recommendedList = new ArrayList<>();
    List<ReceiverModel> receiverList = new ArrayList<>();
    List<BookmarkEntryModel> bookmarkEntryList = new ArrayList<>();
    private long totalBytes = 250000;

    List<DocumentModel> documentList = new ArrayList<>();
    List<ArticleModel> articleList = new ArrayList<>();
    List<CategoryModel> categoryList = new ArrayList<>();
    List<NotificationModel> notificationList = new ArrayList<>();
    List<BookmarkModel> bookmarkList = new ArrayList<>();
    List<UserSettingsModel> userSettingsList = new ArrayList<>();
    List<UserInfoModel> userInfoList = new ArrayList<>();
    List<FileListModel> fileList = new ArrayList<>();
    List<FormsModel> formsList = new ArrayList<>();
    List<ImageModel> imageList = new ArrayList<>();
    List<DocumentModel> documentsList = new ArrayList<>();

//    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
//                DownloadManager.Query query = new DownloadManager.Query();
//                query.setFilterById(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0));
//                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//                Cursor cursor = manager.query(query);
//                if (cursor.moveToFirst()) {
//                    if (cursor.getCount() > 0) {
//                        int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
//                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
//                            Toast.makeText(HomeActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
//                            Decompress decompress = new Decompress(Environment.getExternalStorageDirectory() + "/QDMSWiki/Import.zip", Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles");
//                            decompress.execute();
//                        } else {
//                            int message = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON));
//                            Toast.makeText(HomeActivity.this, String.valueOf(message), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//            }
//        }
//    };


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       // registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        context = this;
        sessionManager = new SessionManager(HomeActivity.this);
        homeDatabase = HomeDatabase.getInstance(HomeActivity.this);
        progressDialog = new ProgressDialog(HomeActivity.this);
        homePresenter = new HomePresenter(this, this);
        util = new ShowCasePreferenceUtil(this);

        String url = homePresenter.getDownloadUrl();
        mainModel = new MainModel();
        beginDownload(url);
        setSupportActionBar(toolbar);
        mainVP.setCurrentItem(0);


        getParentFolders();
        initViewpager();
        initBottomNavigation();
        setNotificationCount();
    }

    public int doOperation() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadID);
        int progress = 0;
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int sizeIndex = c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
            int downloadedIndex = c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
            long size = c.getInt(sizeIndex);
            long downloaded = c.getInt(downloadedIndex);
            if (size != -1) progress = (int) (downloaded * 100 / size);
        }
        return progress;
    }

    private void getParentFolders() {
        homePresenter.getParentFolders();
    }

    @OnClick({R.id.userImageIV, R.id.notificationIV})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userImageIV:
                Intent settingsIntent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.notificationIV:
                Intent notificationsIntent = new Intent(HomeActivity.this, NotificationActivity.class);
                startActivity(notificationsIntent);
                break;
        }
    }

    public void setupFragments(Fragment fragment, boolean addTOstack, boolean doAnimation) {
        try {
            if (fragment != null) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                if (doAnimation)
                    fragmentTransaction.setCustomAnimations(R.anim.enter_right, R.anim.exit_left);
                fragmentTransaction.replace(R.id.navFL, fragment);
                if (addTOstack) fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        } catch (IllegalStateException e) {
            Log.d("IllegalStateException", "Exception", e);
        }

    }

    private void initNavDrawer() {

        ViewGroup.LayoutParams layoutParams = navFL.getLayoutParams();
        layoutParams.width = (ScreenUtils.getScreenWidth(this) * 3) / 4;
        navFL.setLayoutParams(layoutParams);


        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset == 0) {
                    // drawer closed
                } else {
                    setupFragments(findFragmentById(AppConfig.FRAG_NAV_DRAWER), false, false);
                }
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else
                    drawerLayout.openDrawer((int) Gravity.START);
            }

        });

        drawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_menu);
        mDrawerToggle.syncState();
        setupFragments(findFragmentById(AppConfig.FRAG_NAV_DRAWER), false, false);
    }

    private void initBottomNavigation() {
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case 0:
                        mainVP.setCurrentItem(0);
                        return true;
                    case 1:
                        mainVP.setCurrentItem(1);
                        return true;
                    case 2:
                        mainVP.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });

        bottom_navigation.setItemIconTintList(null);
        bottom_navigation.getMenu().clear();
        Menu menu = bottom_navigation.getMenu();
        menu.add(Menu.NONE, 0, Menu.NONE, "HOME")
                .setIcon(R.drawable.drawable_home_selecter);

        menu.add(Menu.NONE, 1, Menu.NONE, "DOCUMENTS")
                .setIcon(R.drawable.drawable_document_selector);

        menu.add(Menu.NONE, 2, Menu.NONE, "ARTICLES")
                .setIcon(R.drawable.drawable_article_selector);

        //setBadgeCount();
    }

    private void setBadgeCount() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottom_navigation.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(1);

        View notificationBadge = LayoutInflater.from(this).inflate(R.layout.notification_badge, menuView, false);
        TextView textView = notificationBadge.findViewById(R.id.notificationsBadgeTextView);
        textView.setText("15");
        itemView.addView(notificationBadge);
    }

    private void setNotificationCount() {
        homePresenter.getNotificationCount();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void setUpPresenter() {
//        homePresenter = new HomePresenter(this, this);
//        String url = homePresenter.getDownloadUrl();
//        beginDownload(url);
    }

    @Override
    protected void isNetworkAvailable(boolean isConnected) {

    }

    private void initViewpager() {
        mainViewPager = new MainViewPager(getSupportFragmentManager());
        mainViewPager.setCount(3);
        mainVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int newPos) {
                newPosition = newPos;
                bottom_navigation.setSelectedItemId(newPosition);
                currentPosition = newPosition;

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mainVP.setOffscreenPageLimit(3);
        mainVP.setAdapter(mainViewPager);
        mainVP.post(new Runnable(){
            @Override
            public void run() {
                if(mainVP.getCurrentItem() == 0)
                    mainViewPager.setShowCaseForSearch();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public Fragment findFragmentById(String key) {

        switch (key) {
            case AppConfig.FRAG_NAV_DRAWER:
                navigationDrawerFragment = new NavigationDrawerFragment();
                Bundle bundlenavDrawer = new Bundle();
                bundlenavDrawer.putSerializable(AppConfig.BUNDLE_NAV_DRAWER, (Serializable) parentFolderList);
                navigationDrawerFragment.setNavigationListener(new NavigationDrawerFragment.NavigationListener() {
                    @Override
                    public void onItemClicked(DocumentModel docModel) {
                        documentModel = docModel;
                        homePresenter.getChildFoldersList(documentModel.getFolderid());

                    }
                });

                navigationDrawerFragment.setArguments(bundlenavDrawer);
                return navigationDrawerFragment;
            case AppConfig.FRAG_NAV_DETAILS_DRAWER:
                NavigationDetailFragment navigationDetailFragment = new NavigationDetailFragment();
                navigationDetailFragment.setNavigationDetailListener(new NavigationDetailFragment.NavigationDetailListener() {
                    @Override
                    public void onItemClicked(DocumentModel docModel) {
                        documentModel = docModel;
                        if (documentModel.getType().equals("Folder"))
                            homePresenter.getChildFoldersList(documentModel.getCatId());
                        else {
                            Intent intent = new Intent(HomeActivity.this, FolderStructureActivity.class);
                            intent.putExtra(AppConfig.BUNDLE_PAGE, "Home");
                            intent.putExtra(AppConfig.BUNDLE_TYPE, documentModel.getType());
                            intent.putExtra(AppConfig.BUNDLE_NAME, documentModel.getCategoryName());
                            intent.putExtra(AppConfig.BUNDLE_FOLDER_NAME, documentModel.getCategoryName());
                            intent.putExtra(AppConfig.BUNDLE_ID, documentModel.getId());
                            intent.putExtra(AppConfig.BUNDLE_FOLDER_ID, documentModel.getCatId());
                            intent.putExtra(AppConfig.BUNDLE_VERSION, documentModel.getVersion());
                            startActivity(intent);
                        }
                    }

                });
                Bundle bundleNavDetail = new Bundle();
                bundleNavDetail.putString(AppConfig.BUNDLE_FOLDER_NAME, documentModel.getCategoryName());
                bundleNavDetail.putSerializable(AppConfig.BUNDLE_NAV_DETAILS_LIST, (Serializable) childList);
                navigationDetailFragment.setArguments(bundleNavDetail);
                return navigationDetailFragment;
            default:

                return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void beginDownload(String url) {
        File file = new File(Environment.getExternalStorageDirectory(), "/QDMSWiki/ExtractedFiles");
        if (file.exists()) {
//            ReadAndInsertJsonData readAndInsertJsonData = new ReadAndInsertJsonData();
//            readAndInsertJsonData.execute();
            progressLayout.setVisibility(View.GONE);
            return;

        } else {

            FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(this)
                    .setDownloadConcurrentLimit(3)
                    .enableRetryOnNetworkGain(true)
                    .enableAutoStart(true)
                    .setNotificationManager(new FetchNotificationManager() {
                        @NotNull
                        @Override
                        public String getNotificationManagerAction() {
                            return null;
                        }

                        @NotNull
                        @Override
                        public BroadcastReceiver getBroadcastReceiver() {
                            return null;
                        }

                        @Override
                        public boolean updateGroupSummaryNotification(int i, NotificationCompat.@NotNull Builder builder, @NotNull List<? extends DownloadNotification> list, @NotNull Context context) {
                            return false;
                        }

                        @Override
                        public void updateNotification(NotificationCompat.@NotNull Builder builder, @NotNull DownloadNotification downloadNotification, @NotNull Context context) {

                        }

                        @Override
                        public void notify(int i) {

                        }

                        @NotNull
                        @Override
                        public PendingIntent getGroupActionPendingIntent(int i, @NotNull List<? extends DownloadNotification> list, DownloadNotification.@NotNull ActionType actionType) {
                            return null;
                        }

                        @NotNull
                        @Override
                        public PendingIntent getActionPendingIntent(@NotNull DownloadNotification downloadNotification, DownloadNotification.@NotNull ActionType actionType) {
                            return null;
                        }

                        @Override
                        public void cancelOngoingNotifications() {

                        }

                        @Override
                        public void cancelNotification(int i) {

                        }

                        @Override
                        public void createNotificationChannels(@NotNull Context context, @NotNull NotificationManager notificationManager) {

                        }

                        @NotNull
                        @Override
                        public String getChannelId(int i, @NotNull Context context) {
                            return null;
                        }

                        @Override
                        public boolean postDownloadUpdate(@NotNull Download download) {
                            return false;
                        }

                        @NotNull
                        @Override
                        public NotificationCompat.Builder getNotificationBuilder(int i, int i1) {
                            mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationBuilder = new NotificationCompat.Builder(getApplicationContext());
                            notificationBuilder.setContentTitle("QDMSWiki")
                                    .setContentText("Downloading..")
                                    .setSmallIcon(R.drawable.ic_more_download).setOngoing(true);
                            return notificationBuilder;
                        }

                        @Override
                        public boolean shouldUpdateNotification(@NotNull DownloadNotification downloadNotification) {
                            return false;
                        }

                        @Override
                        public boolean shouldCancelNotification(@NotNull DownloadNotification downloadNotification) {
                            return false;
                        }

                        @Override
                        public void registerBroadcastReceiver() {

                        }

                        @Override
                        public void unregisterBroadcastReceiver() {

                        }

                        @NotNull
                        @Override
                        public Fetch getFetchInstanceForNamespace(@NotNull String s) {
                            return null;
                        }

                        @Override
                        public long getNotificationTimeOutMillis() {
                            return 0;
                        }

                        @NotNull
                        @Override
                        public String getDownloadNotificationTitle(@NotNull Download download) {
                            return null;
                        }

                        @NotNull
                        @Override
                        public String getSubtitleText(@NotNull Context context, @NotNull DownloadNotification downloadNotification) {
                            return null;
                        }
                    })
                    .build();

            Fetch fetch = Fetch.Impl.getInstance(fetchConfiguration);

            final Request request = new Request(url, Environment.getExternalStorageDirectory()+"/QDMSWiki/Import.zip");
            request.setPriority(Priority.HIGH);
            request.setNetworkType(NetworkType.ALL);
            request.addHeader("clientKey", "SD78DF93_3947&MVNGHE1WONG");

            fetch.enqueue(request, updatedRequest -> {

            }, error -> {
                //An error occurred enqueuing the request.
            });

            fetchListener = new FetchListener() {
                @Override
                public void onWaitingNetwork(@NotNull Download download) {
                    fetch.resume(downloadID);
                }

                @Override
                public void onStarted(@NotNull Download download, @NotNull List<? extends DownloadBlock> list, int i) {
                    if (request.getId() == download.getId()) {
                        downloadID = download.getId();
                    }
                }

                @Override
                public void onProgress(@NotNull Download download, long l, long l1) {

                    if(notificationBuilder != null) {
                        Log.e("DESCARGANDO", "Progreso: " + download.getProgress());
                        String progreso = download.getProgress() + "% - " + getBytesToMBString(download.getDownloaded()) + "/" + getBytesToMBString(download.getTotal()) + "";


                        notificationBuilder.setContentTitle(download.getTag());
                        notificationBuilder.setContentText(progreso);
                        notificationBuilder.setProgress((int) download.getTotal(), (int) download.getDownloaded(), false);
                        notificationBuilder.setOngoing(false);
                        Intent intents = new Intent(getApplicationContext(), HomeActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                                intents, PendingIntent.FLAG_CANCEL_CURRENT);
                        notificationBuilder.setContentIntent(pendingIntent);

                        mNotifyManager.notify(download.getId(), notificationBuilder.build());
                    }
                    int progress = download.getProgress();
                    donut_progress.setProgress(progress);
                }


                @Override
                public void onError(@NotNull Download download, @NotNull Error error, @org.jetbrains.annotations.Nullable Throwable throwable) {
                    Toast.makeText(HomeActivity.this, error.getHttpResponse().toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onDownloadBlockUpdated(@NotNull Download download, @NotNull DownloadBlock downloadBlock, int i) {

                }

                @Override
                public void onAdded(@NotNull Download download) {

                }

                @Override
                public void onQueued(@NotNull Download download, boolean waitingOnNetwork) {
                    if (request.getId() == download.getId()) {
                        //showDownloadInList(download);
                    }
                }

                @Override
                public void onCompleted(@NotNull Download download) {
                    Toast.makeText(HomeActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
                    Decompress decompress = new Decompress(Environment.getExternalStorageDirectory() + "/QDMSWiki/Import.zip", Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles");
                    decompress.execute();
                }

                @Override
                public void onPaused(@NotNull Download download) {
                }

                @Override
                public void onResumed(@NotNull Download download) {
                }

                @Override
                public void onCancelled(@NotNull Download download) {

                }

                @Override
                public void onRemoved(@NotNull Download download) {

                }

                @Override
                public void onDeleted(@NotNull Download download) {

                }
            };

            fetch.addListener(fetchListener);
//
//            PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
//                    .setDatabaseEnabled(true)
//                    .setReadTimeout(30_000)
//                    .setConnectTimeout(30_000)
//                    .build();
//            PRDownloader.initialize(getApplicationContext(), config);
//
//            downloadID = PRDownloader.download(url, Environment.getExternalStorageDirectory() + "/QDMSWiki", "Import.zip")
//                    .build()
//                    .setOnStartOrResumeListener(new OnStartOrResumeListener() {
//                        @Override
//                        public void onStartOrResume() {
//                            PRDownloader.resume(downloadID);
//                        }
//                    })
//                    .setOnPauseListener(new OnPauseListener() {
//                        @Override
//                        public void onPause() {
//                            PRDownloader.pause(downloadID);
//                        }
//                    })
//                    .setOnCancelListener(new OnCancelListener() {
//                        @Override
//                        public void onCancel() {
//
//                        }
//                    })
//                    .setOnProgressListener(new OnProgressListener() {
//                        @Override
//                        public void onProgress(Progress progress) {
//                            long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
//                            donut_progress.setProgress((int) progressPercent);
//                        }
//                    })
//                    .start(new OnDownloadListener() {
//                        @Override
//                        public void onDownloadComplete() {
//                            Toast.makeText(HomeActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
//                            Decompress decompress = new Decompress(Environment.getExternalStorageDirectory() + "/QDMSWiki/Import.zip", Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles");
//                            decompress.execute();
//                        }
//
//                        @Override
//                        public void onError(com.downloader.Error error) {
//                            PRDownloader.pause(downloadID);
//                            Toast.makeText(HomeActivity.this, error.getServerErrorMessage(), Toast.LENGTH_SHORT).show();
//                        }
//
//
//
//                    });


        }

//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
//                    .setTitle("QDMSWiki")// Title of the Download Notification
//                    .setDescription("Downloading")// Description of the Download Notification
//                    .setAllowedOverRoaming(true)
//                    .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
//                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
//                    .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
//                    .setMimeType("application/zip");
//
//            downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//            downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.
//


    }


    private static String getBytesToMBString(long bytes){
        return String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00));
    }



    private void setProgress(){
        progressBarStatus = 0;
        new Thread(new Runnable() {
            public void run() {
                while (progressBarStatus < 100) {

                    // performing operation
                    progressBarStatus = doOperation();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // Updating the progress bar

                    linLayout.setAlpha(0.15f);
                    relLayout.setAlpha(0.15f);
                    progressBarHandler.post(new Runnable() {
                        public void run() {
                            donut_progress.setProgress(progressBarStatus);
                        }
                    });
                }
                // performing operation if file is downloaded,
                if (progressBarStatus >= 100) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    private class Decompress extends AsyncTask<Void, Integer, String> {

        private String zipFilePath;
        private String destDirectory;
        private int per = 0;

        public Decompress(String _zipFile, String _location) {
            zipFilePath = _zipFile;
            destDirectory = _location;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Unzipping file...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                File destDir = new File(destDirectory);
                if (!destDir.exists()) {
                    destDir.mkdir();
                }
                ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
                ZipEntry entry = zipIn.getNextEntry();
                // iterates over entries in the zip file
                while (entry != null) {
                    String filePath = destDirectory + File.separator + entry.getName();
                    if (!entry.isDirectory()) {
                        // if the entry is a file, extracts it
                        extractFile(zipIn, filePath);
                    } else {
                        // if the entry is a directory, make the directory
                        File dir = new File(filePath);
                        dir.mkdir();
                    }
                    zipIn.closeEntry();
                    entry = zipIn.getNextEntry();
                }
            } catch (Exception e) {
                progressDialog.dismiss();
                Log.e("Decompress", "unzip", e);
                return "Error";
            }

            return "Success";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result.equals("Success")) {
                ReadAndInsertJsonData readAndInsertJsonData = new ReadAndInsertJsonData();
                readAndInsertJsonData.execute();
            }
        }
    }

    /**
     * Extracts a zip entry (file entry)
     * Extracts a zip entry (file entry)
     *
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[1024];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onGetDownloadUrlSuccess(String url) {

    }

    @Override
    public void onGetDownloadUrlError(APIException e) {

    }

    @Override
    public void onGetParentFolderSuccess(List<DocumentModel> folderList) {
        parentFolderList = folderList;
        initNavDrawer();
    }

    @Override
    public void onGetChildFoldersList(List<DocumentModel> list) {
        childList = list;
        setupFragments(findFragmentById(AppConfig.FRAG_NAV_DETAILS_DRAWER), true, true);
    }

    @Override
    public void onGetUserImageSuccess(UserInfoModel userInfoModel) {
        try {
            byte[] decodedString = Base64.decode(userInfoModel.getImageName().replace("data:image/jpg;base64,", ""), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            userImageIV.setImageBitmap(bitmap);
        } catch (Exception e) {
        }
    }

    @Override
    public void onGetUserImageError() {

    }

    @Override
    public void onGetCategoryDetailsSuccess(CategoryModel categoryModel) {

    }

    @Override
    public void onGetCategoryDetailsError() {

    }

    @Override
    public void onInsertCategoryDetailsSuccess() {
        getParentFolders();
    }

    @Override
    public void onInsertCategoryDetailsError() {

    }

    @Override
    public void onGetDocumentInfoSuccess(DocumentModel documentModel) {

    }

    @Override
    public void onGetDocumentInfoError() {

    }

    @Override
    public void onGetNotificationCountSuccess(List<NotificationModel> notificationList) {
        int notificationCount = 0;
        for (int i = 0; i < notificationList.size(); i++) {
            List<ReceiverModel> receiverList = notificationList.get(i).getReceviers();
            for (int j = 0; j < receiverList.size(); j++) {
                if (receiverList.get(j).getRecevierId().equals(sessionManager.getUserInfoId()) && receiverList.get(j).getUnread()) {
                    notificationCount = notificationCount + 1;
                }
            }
        }
        notificationsBadgeTextView.setText(String.valueOf(notificationCount));
    }

    @Override
    public void onGetNotificationCountError() {

    }

    @Override
    public void onInsertFileListSuccess() {

    }

    @Override
    public void onInsertFileListError() {

    }

    @Override
    public void onInsertFormSuccess() {

    }

    @Override
    public void onInsertFormError() {

    }

    @Override
    public void onInsertImageSuccess() {

    }

    @Override
    public void onInsertImageError() {

    }

    public class ReadAndInsertJsonData extends AsyncTask<String, Integer, String> {

        JSONObject jsonObject;

        public ReadAndInsertJsonData() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            createImageFolder();
            progressLayout.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            linLayout.setAlpha(1.0f);
            relLayout.setAlpha(1.0f);
            progressDialog.setMessage("Extracting files...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            File file1 = null;
            try {
                gson = new GsonBuilder()
                        .setLenient()
                        .create();
                File folder = new File(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles"); //This is just to cast to a File type since you pass it as a String
                File[] filesInFolder = folder.listFiles(); // This returns all the folders and files in your path
                for (File file : filesInFolder) { //For each of the entries do:
                    file1 = file;
                    JsonParser parser = new JsonParser();
                    JsonObject data = (JsonObject) parser.parse(new FileReader(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles/" + file.getName()));//path to the JSON file.
                    if (!file.isDirectory() && file.getName().contains("docs")) {
                        JsonArray jsonArray = data.getAsJsonArray("Documents");
                        documentList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<DocumentModel>>() {
                        }.getType());
                        homePresenter.insertDocuments(documentList);

                        for (int i = 0; i < documentList.size(); i++) {
                            documentList.get(i).setIsRecommended("NO");
                            List<TagModel> tagList = documentList.get(i).getTags();
                            homePresenter.insertTags(tagList);
                        }


                    } else if (!file.isDirectory() && file.getName().contains("art")) { //check that it's not a dir
                        JsonArray jsonArray = data.getAsJsonArray("Articles");
                        articleList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<ArticleModel>>() {
                        }.getType());
                        homePresenter.insertArticles(articleList);
                    } else if (!file.isDirectory() && file.getName().contains("file")) { //check that it's not a dir
                        JsonArray jsonArray = data.getAsJsonArray("fileChunks");
                        fileList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<FileListModel>>() {
                        }.getType());
                        homePresenter.insertFileLists(fileList);
                    } else if (!file.isDirectory() && file.getName().contains("image")) { //check that it's not a dir
                        JsonArray jsonArray = data.getAsJsonArray("Images");
                        imageList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<ImageModel>>() {
                        }.getType());
                        homePresenter.insertImageLists(imageList);
                        for (int i = 0; i < imageList.size(); i++) {
                            try {
                                decodeFile(imageList.get(i).getImageStream(), imageList.get(i).getImageName());
                            }
                            catch (Exception e){
                                decodeFile(imageList.get(i).getImageDataAsString(), imageList.get(i).getImageName());
                            }

                        }
                    } else {
                        if (!file.isDirectory() && file.getName().contains("category")) {
                            JsonArray jsonArray = data.getAsJsonArray("Categories");
                            categoryList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<CategoryModel>>() {
                            }.getType());
                            homePresenter.insertCategories(categoryList);
                        } else if (!file.isDirectory() && file.getName().contains("bookmarks")) {
                            JsonArray jsonArray = data.getAsJsonArray("Bookmarks");
                            bookmarkList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<BookmarkModel>>() {
                            }.getType());
                            homePresenter.insertBookmarks(bookmarkList);
                            for (int i = 0; i < bookmarkList.size(); i++) {
                                List<BookmarkEntryModel> bookmarkEntryList = bookmarkList.get(i).getBookmarkEntries();
                                for (int j = 0; j < bookmarkEntryList.size(); j++) {
                                    bookmarkEntryList.get(j).setDocumentId(bookmarkList.get(i).getDocumentId());
                                }
                                homePresenter.insertBookmarkEntries(bookmarkEntryList);
                            }
                        } else if (!file.isDirectory() && file.getName().contains("notifications")) {
                            JsonArray jsonArray = data.getAsJsonArray("Notifications");
                            notificationList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<NotificationModel>>() {
                            }.getType());
                        } else if (!file.isDirectory() && file.getName().contains("userInfo")) {
                            JsonArray jsonArray = data.getAsJsonArray("UserInfo");
                            userInfoList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<UserInfoModel>>() {
                            }.getType());

                            for (int i = 0; i < userInfoList.size(); i++) {
                                if (String.valueOf(userInfoList.get(i).getUserId()).equals(sessionManager.getUserId())) {
                                    sessionManager.setUserInfoId(userInfoList.get(i).getId());
                                    break;
                                }
                            }

                            for (int i = 0; i < userInfoList.size(); i++) {
                                if (!(String.valueOf(userInfoList.get(i).getUserId()).equals(sessionManager.getUserId()))) {
                                    userInfoList.get(i).setImageName("");
                                } else {
                                    userInfoList.get(i).setImageName(userInfoList.get(i).getImageName());
                                }
                            }
                            homePresenter.insertUserInfo(userInfoList);

                        } else if (!file.isDirectory() && file.getName().contains("userSet")) {
                            JsonArray jsonArray = data.getAsJsonArray("UserSettings");
                            userSettingsList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<UserSettingsModel>>() {
                            }.getType());

                        } else if (!file.isDirectory() && file.getName().contains("forms")) {
                            JsonArray jsonArray = data.getAsJsonArray("Forms");
                            formsList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<FormsModel>>() {
                            }.getType());
                            homePresenter.insertFormsList(formsList);
                        }
                    }

                }

                return "Success";

            } catch (OutOfMemoryError e) {
                Toast.makeText(getApplicationContext(), file1.getName(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return "Error";
            } catch (Exception e) {
                progressDialog.dismiss();
                return "Error";
            }

        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            setRecommendedList();


            for (int i = 0; i < userSettingsList.size(); i++) {
                if (userSettingsList.get(i).getUserID().equals(sessionManager.getUserInfoId())) {
                    homePresenter.insertUserSettings(userSettingsList.get(i));
                } else
                    continue;
            }

            for (int i = 0; i < notificationList.size(); i++) {
                List<ReceiverModel> receiverList = notificationList.get(i).getReceviers();
                for (int j = 0; j < receiverList.size(); j++) {
                    if (receiverList.get(j).getRecevierId().equals(sessionManager.getUserInfoId())) {
                        homePresenter.insertNotifications(notificationList.get(i));
                        break;
                    }
                }
            }

            mainViewPager.updateRecentlyList(new ArrayList<>());
            setNotificationCount();

            File file = new File(Environment.getExternalStorageDirectory() + "/QDMSWiki/Import.zip");
            if (file.exists()) {
                file.delete();
            }

        }

    }

    public void decodeFile(String strFile, String filename) {
        try {
            if(!strFile.isEmpty()) {
                byte[] data = Base64.decode(strFile, Base64.DEFAULT);
                File ext = Environment.getExternalStorageDirectory();
                File mydir = new File(ext.getAbsolutePath() + "/QDMSWiki/Images");
                File file = new File(mydir, filename);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(data);
                fos.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void saveImage(String Image, String name)
    {

        BufferedOutputStream bufferedOutputStream = null;
        final byte[] imgBytesData = android.util.Base64.decode(Image,
                android.util.Base64.DEFAULT);

        final File file;
        try {
            file = File.createTempFile("image", null, context.getCacheDir());
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            bufferedOutputStream = new BufferedOutputStream(
                    fileOutputStream);
            try {
                bufferedOutputStream.write(imgBytesData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        finally {
            try {
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        try{
//            byte[] imageBytes=Base64.decode(Image, Base64.DEFAULT);
//            InputStream is = new ByteArrayInputStream(imageBytes);
//            Bitmap image=BitmapFactory.decodeStream(is);
//
//            String mBaseFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/QDMSWiki/Images";
////
////            if (!new File(mBaseFolderPath).exists()) {
////                new File(mBaseFolderPath).mkdir();
////            }
//            String mFilePath = mBaseFolderPath + "/" + name;
//
//            File file = new File(mFilePath);
//
//            FileOutputStream stream = new FileOutputStream(file);
//
//            if (!file.exists()){
//                file.createNewFile();
//            }
//
//            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
//
//            is.close();
//            image.recycle();
//
//            stream.flush();
//            stream.close();
//        }
//        catch(Exception e)
//        {
//            Log.v("SaveFile",""+e);
//        }
    }



    private void createImageFolder() {
        File ext = Environment.getExternalStorageDirectory();
        File mydir = new File(ext.getAbsolutePath() + "/QDMSWiki/Images");
        if (!mydir.exists()) {
            mydir.mkdirs();
        }
    }

    private void setRecommendedList() {
        //Recommended logic
        documentList = getDocumentList();
        for (int i = 0; i < documentList.size(); i++) {
            boolean isRecommended = false;
            List<TagModel> tagList = documentList.get(i).getTags();
            for (int j = 0; j < userSettingsList.size(); j++) {
                if (userSettingsList.get(j).getUserID().equals(sessionManager.getUserInfoId())) {
                    //loop tags
                    List<UserSettingsTagModel> userSettingsTagList = userSettingsList.get(j).getTags();
                    for (int k = 0; k < tagList.size(); k++) {
                        for (int l = 0; l < userSettingsTagList.size(); l++) {
                            if (tagList.get(k).getId().equals(userSettingsTagList.get(l).getId())) {
                                documentList.get(i).setIsRecommended("YES");
                                homePresenter.updateIsRecommended(documentList.get(i).getId());
                                isRecommended = true;
                                break;
                            }
                        }
                    }
                    //loop categories
                    if (!isRecommended) {
                        List<UserSettingsCategoryModel> userSettingsCategoryList = userSettingsList.get(j).getCategory();
                        for (int k = 0; k < tagList.size(); k++) {
                            for (int l = 0; l < userSettingsCategoryList.size(); l++) {
                                if (documentList.get(i).getCategoryId().equals(userSettingsCategoryList.get(l).getId())) {
                                    documentList.get(i).setIsRecommended("YES");
                                    homePresenter.updateIsRecommended(documentList.get(i).getId());
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        getRecommendedList();

//        for (int i = 0; i < documentList.size(); i++) {
//            if (documentList.get(i).getIsRecommended().equals("YES"))
//                recommendedList.add(documentList.get(i));
//        }
    }

    public List<DocumentModel> getDocumentList() {

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                documentsList = homeDatabase.homeDao().getDocuments();
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

        return documentsList;
    }


    public void getRecommendedList() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                recommendedList = homeDatabase.homeDao().getRecommendedDocuments();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                mainViewPager.updateRecommendedList(recommendedList);
                progressDialog.dismiss();
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void updateUserImage() {
        homePresenter.getUserImage(sessionManager.getUserId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}


