package com.mariapps.qdmswiki.home.view;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.ArticleModelObj;
import com.mariapps.qdmswiki.DocumentModelObj;
import com.mariapps.qdmswiki.DownloadService;
import com.mariapps.qdmswiki.ObjectBox;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.SessionManager;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkEntryModel;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkModel;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.custom.CustomViewPager;
import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.model.ArticleModel;
import com.mariapps.qdmswiki.home.model.CategoryModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.model.DownloadFilesRequestModel;
import com.mariapps.qdmswiki.home.model.DownloadFilesResponseModel;
import com.mariapps.qdmswiki.home.model.FileListModel;
import com.mariapps.qdmswiki.home.model.FormsModel;
import com.mariapps.qdmswiki.home.model.ImageModel;
import com.mariapps.qdmswiki.home.model.TagModel;
import com.mariapps.qdmswiki.home.presenter.HomePresenter;
import com.mariapps.qdmswiki.notification.model.NotificationModel;
import com.mariapps.qdmswiki.notification.model.ReceiverModel;
import com.mariapps.qdmswiki.notification.view.NotificationActivity;
import com.mariapps.qdmswiki.search.model.SearchModel;
import com.mariapps.qdmswiki.search.view.FolderStructureActivity;
import com.mariapps.qdmswiki.serviceclasses.APIException;
import com.mariapps.qdmswiki.settings.view.SettingsActivity;
import com.mariapps.qdmswiki.usersettings.UserInfoModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsCategoryModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsTagModel;
import com.mariapps.qdmswiki.utils.DonutProgress;
import com.mariapps.qdmswiki.utils.ScreenUtils;
import com.mariapps.qdmswiki.utils.ShowCasePreferenceUtil;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.DownloadBlock;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import butterknife.BindView;
import butterknife.OnClick;
import io.objectbox.Box;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

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
    private int downloadID;
    private int progressBarStatus;
    private Handler progressBarHandler = new Handler();
    private FetchListener fetchListener;
    private String zippedFileName;
    private DocumentModel documentModel;
    List<SearchModel> childList = new ArrayList<>();
    List<DocumentModel> parentFolderList = new ArrayList<>();
    List<DocumentModel> recommendedList = new ArrayList<>();
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
    List<DownloadFilesResponseModel.DownloadEntityList> downloadEntityLists = new ArrayList<>();
    private int urlNum = 0;
    private Fetch fetch;
    private String log;
    private String folderId;
    private String folderName;
    private String url = "";


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ObjectBox.init(this);
      /*  Box<DocumentModelObj> box = ObjectBox.get().boxFor(DocumentModelObj.class);
      // box.removeAll();
        Box<ArticleModelObj> abox = ObjectBox.get().boxFor(ArticleModelObj.class);
      // abox.removeAll();
        Log.e("sizeofdocumentsdocbox",box.getAll().size()+"");
        Log.e("sizeofdocumentsabox",abox.getAll().size()+"");*/
        // Log.e("sizeofdocuments",box.getAll().get(1).documentName);
        context = this;
        sessionManager = new SessionManager(HomeActivity.this);
        homeDatabase = HomeDatabase.getInstance(HomeActivity.this);
        progressDialog = new ProgressDialog(HomeActivity.this);
        //sessionManager.setKeyLastUpdatedFileName("20191203");//"20191121092627"
        homePresenter.getDownloadUrl(new DownloadFilesRequestModel(sessionManager.getKeyLastUpdatedFileName()));
        //
        // ("https://qdmswiki2k19.blob.core.windows.net/update/20191114153246.zip","20191114153246.zip");
        setSupportActionBar(toolbar);
        mainVP.setCurrentItem(0);
        getParentFolders();
        initViewpager();
        initBottomNavigation();
        setNotificationCount();


        if (isMyServiceRunning(DownloadService.class)) {
            progressLayout.setVisibility(View.VISIBLE);
        } else {
            progressLayout.setVisibility(View.GONE);
        }
        AppConfig.getDwnldcmplted().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                progressLayout.setVisibility(View.GONE);
                HomeActivity.Decompress decompress = new HomeActivity.Decompress(Environment.getExternalStorageDirectory() + "/QDMSWiki/" + zippedFileName, Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles");
                decompress.execute();
            }
        });
        AppConfig.getDwnldstarted().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                appendLog("Downloading url " + url);
                progressLayout.setVisibility(View.VISIBLE);
                linLayout.setAlpha(0.15f);
                relLayout.setAlpha(0.15f);
            }
        });

        AppConfig.getDwnlderror().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //    appendLog("Error while Downloading " + error.getHttpResponse());
            }
        });

        AppConfig.getDwnldprgress().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer s) {
                Log.e("serviceprogress", s + "");
                donut_progress.setProgress(Math.round(s));
            }
        });
        //  Decompress decompress = new Decompress(Environment.getExternalStorageDirectory() + "/QDMSWiki/" + "20191218.zip", Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles");
        //decompress.execute();
       // ReadAndInsertJsonData readAndInsertJsonData = new ReadAndInsertJsonData();
       // readAndInsertJsonData.execute();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("onStop", "stopped");
    }

    private void getParentFolders() {
        homePresenter.getParentFolders();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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
        homePresenter = new HomePresenter(this, this);
//        String url = homePresenter.getDownloadUrl();
//        beginDownload(url);
    }


    @Override
    protected void isNetworkAvailable(boolean isConnected) {

    }

    private void initViewpager() {
        mainViewPager = new MainViewPager(getSupportFragmentManager());
        mainViewPager.setCount(3);
//        mainViewPager.setMainVPListener(new MainViewPager.MainVPListener() {
//            @Override
//            public void onInitDashBoard() {
//
//            }
//
//            @Override
//            public void loadDocuments() {
//                getCurrentDocumentList();
//            }
//
//            @Override
//            public void loadArticles() {
//                getArticleList();
//            }
//        });
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
                    public void onItemClicked(SearchModel searchModel) {
                        //documentModel = docModel;
                        if (searchModel.getType().equals("Category"))
                            homePresenter.getChildFoldersList("%" + searchModel.getCategoryId() + "%");
                        else {
                            Intent intent = new Intent(HomeActivity.this, FolderStructureActivity.class);
                            intent.putExtra(AppConfig.BUNDLE_PAGE, "Home");
                            intent.putExtra(AppConfig.BUNDLE_TYPE, searchModel.getType());
                            intent.putExtra(AppConfig.BUNDLE_NAME, searchModel.getName());
                            intent.putExtra(AppConfig.BUNDLE_ID, searchModel.getId());
                            if (searchModel.getType().equals("Article")) {
                                List<String> categoryIds = Collections.singletonList(searchModel.getCategoryId().substring(1, searchModel.getCategoryId().length() - 1));
                                intent.putExtra(AppConfig.BUNDLE_FOLDER_ID, categoryIds.get(0).replace("\"", ""));
                            } else {
                                intent.putExtra(AppConfig.BUNDLE_FOLDER_ID, searchModel.getCategoryId());
                                intent.putExtra(AppConfig.BUNDLE_FOLDER_NAME, searchModel.getCategoryName());
                            }
                            intent.putExtra(AppConfig.BUNDLE_VERSION, searchModel.getVersion());
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

    public void beginDownload(String url, String zipFileName) {
        this.url = url;
        zippedFileName = zipFileName;
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra("url", url);
        intent.putExtra("filename", zipFileName);
        if(!url.equals("")&&!zipFileName.equals("")){
            if(!isMyServiceRunning(DownloadService.class)){
                Log.e("service","notrunning");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent);
                } else {
                    context.startService(new Intent(context, DownloadService.class));
                }
            }
            else {
                Log.e("service","isrunning");
            }
        }
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
            appendLog("Unzipping " + zipFilePath);
            progressDialog.setMessage("Unzipping file...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            ZipFile zip = null;
            try {
                byte[] buffer = new byte[1024];
                File destDir = new File(destDirectory);
                if (!destDir.exists()) {
                    destDir.mkdir();
                }
                FileInputStream fis = new FileInputStream(zipFilePath);
                ZipInputStream zipIn = new ZipInputStream(fis);
                ZipEntry entry = zipIn.getNextEntry();
                while (entry != null) {
                    String fileName = entry.getName();
                    File newFile = new File(destDir + File.separator + fileName);
                    //create directories for sub directories in zip
                    new File(newFile.getParent()).mkdirs();
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zipIn.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    zipIn.closeEntry();
                    entry = zipIn.getNextEntry();
                }

                zipIn.closeEntry();
                zipIn.close();
                fis.close();
            } catch (Exception e) {
                appendLog("Unzipping error " + e.getMessage());
                return "Error";
            } finally {
                if (zip != null) {
                    try {
                        zip.close();
                    } catch (IOException ignored) {
                    }
                }
            }
            appendLog("Unzipping success");
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
        zipIn.closeEntry();
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
    public void onGetChildFoldersList(List<SearchModel> list) {
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

    @Override
    public void onGetDownloadFilesSuccess(DownloadFilesResponseModel downloadFilesResponseModel) {
        if (downloadFilesResponseModel.getDownloadEntityList() != null) {
            downloadEntityLists = downloadFilesResponseModel.getDownloadEntityList();
            appendLog("DownloadEntityList size =" + downloadEntityLists.size());
            if (downloadFilesResponseModel != null && downloadEntityLists.size() > 0) {
                beginDownload(downloadEntityLists.get(urlNum).getDownloadLink(), downloadEntityLists.get(urlNum).getFileName());
                urlNum = urlNum + 1;
                //added lines new
            }
        }
    }


    /*public void beginDownloadwithdwnldmgr(String url,String zippedFileName){
        Log.e("filename and size",zippedFileName);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Downloading Files");
        request.setTitle("QDMS");
request.setAllowedNetworkTypes(
               DownloadManager.Request.NETWORK_WIFI
                      | DownloadManager.Request.NETWORK_MOBILE)
            .setAllowedOverRoaming(false).setTitle("QDMS")
              .setDestinationInExternalPublicDir("/QDMSWiki/", zippedFileName);
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }*/


    /* private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
         @Override
         public void onReceive(Context context, Intent intent) {
             //Fetching the download id received with the broadcast
             long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
             //Checking if the received broadcast is for our enqueued download by matching download id
             if (downloadID == id) {
                 Toast.makeText(HomeActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
             }
         }
     };*/
    public void appendLog(String text) {
        File logFile = new File("sdcard/QDMSWiki/qdms_log_file.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onGetDownloadFilesError() {

    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (destFile.exists())
            return;
        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }


    public class ReadAndInsertJsonData extends AsyncTask<String, Integer, String> {

        JSONObject jsonObject;

        public ReadAndInsertJsonData() {


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            createImageFolder();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            if (urlNum == downloadEntityLists.size()) {
                linLayout.setAlpha(1.0f);
                relLayout.setAlpha(1.0f);
            }
            appendLog("Extracting files...");
            progressDialog.setMessage("Extracting files...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                File folder = new File(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles"); //This is just to cast to a File type since you pass it as a String
                File[] filesInFolder = folder.listFiles(); // This returns all the folders and files in your path
                for (File file : filesInFolder) { //For each of the entries do:
                    if (file.isDirectory()) {
                        appendLog("Extracting directory " + file.getName());
                        File[] filesInsideFolder = file.listFiles();
                        for (File eachFile : filesInsideFolder) {
                            appendLog("Copying " + eachFile.getName() + " to image folder");
                            copyFile(new File(eachFile.getAbsolutePath()), new File(Environment.getExternalStorageDirectory() + "/QDMSWiki/Images/" + eachFile.getName()));
                        }
                    } else {
                        JsonParser parser = new JsonParser();
                        JsonObject data = (JsonObject) parser.parse(new FileReader(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles/" + file.getName()));//path to the JSON file.
                        if (file.getName().contains("docs")) {
                            appendLog("Extracting document " + file.getName());
                            JsonArray jsonArray = data.getAsJsonArray("Documents");
                            documentList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<DocumentModel>>() {
                            }.getType());
                            for (int i = 0; i < documentList.size(); i++) {
                                homePresenter.deleteDocument(documentList.get(i));
                            }
                            for (int i = 0; i < documentList.size(); i++) {
                                documentList.get(i).setIsRecommended("NO");
                                List<TagModel> tagList = documentList.get(i).getTags();
                                homePresenter.insertTags(tagList);
                            }
                        } else if (file.getName().contains("art")) { //check that it's not a dir
                            appendLog("Extracting article " + file.getName());
                            JsonArray jsonArray = data.getAsJsonArray("Articles");
                            articleList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<ArticleModel>>() {
                            }.getType());
                            for (int i = 0; i < articleList.size(); i++) {
                                homePresenter.deleteArticles(articleList.get(i));
                            }
                        } else if (file.getName().contains("file")) { //check that it's not a dir
                            appendLog("Extracting file " + file.getName());
                            JsonArray jsonArray = data.getAsJsonArray("fileChunks");
                            fileList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<FileListModel>>() {
                            }.getType());

                            for (int i = 0; i < fileList.size(); i++) {
                                homePresenter.deleteFile(fileList.get(i));
                            }
                        } else if (file.getName().contains("image")) { //check that it's not a dir
                            appendLog("Extracting image " + file.getName());
                            JsonArray jsonArray = data.getAsJsonArray("Images");
                            imageList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<ImageModel>>() {
                            }.getType());
                            for (int i = 0; i < imageList.size(); i++) {
                                homePresenter.deleteImage(imageList.get(i));
                            }
                            for (int i = 0; i < imageList.size(); i++) {
                                try {
                                    if (imageList.get(i).getImageStream() != null && !imageList.get(i).getImageStream().isEmpty())
                                        decodeFile(imageList.get(i).getImageStream(), imageList.get(i).getImageName());
                                } catch (Exception e) {
                                    try {
                                        if (imageList.get(i).getImageDataAsString() != null && !imageList.get(i).getImageDataAsString().isEmpty())
                                            decodeFile(imageList.get(i).getImageDataAsString(), imageList.get(i).getImageName());
                                    } catch (Exception e1) {
                                        continue;
                                    }
                                }

                            }
                        } else if (file.getName().contains("category")) {
                            appendLog("Extracting category " + file.getName());
                            JsonArray jsonArray = data.getAsJsonArray("Categories");
                            categoryList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<CategoryModel>>() {
                            }.getType());
                            for (int i = 0; i < categoryList.size(); i++) {
                                homePresenter.deleteCategory(categoryList.get(i));
                            }
                        } else if (file.getName().contains("bookmarks")) {
                            appendLog("Extracting bookmark " + file.getName());
                            JsonArray jsonArray = data.getAsJsonArray("Bookmarks");
                            bookmarkList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<BookmarkModel>>() {
                            }.getType());
                            //homePresenter.deleteBookmarks(bookmarkList);
                            for (int i = 0; i < bookmarkList.size(); i++) {
                                homePresenter.deleteBookmark(bookmarkList.get(i));
                                List<BookmarkEntryModel> bookmarkEntryList = bookmarkList.get(i).getBookmarkEntries();
                                for (int j = 0; j < bookmarkEntryList.size(); j++) {
                                    bookmarkEntryList.get(j).setDocumentId(bookmarkList.get(i).getDocumentId());
                                }
                                homePresenter.insertBookmarkEntries(bookmarkEntryList);
                            }
                        } else if (file.getName().contains("notifications")) {
                            JsonArray jsonArray = data.getAsJsonArray("Notifications");
                            notificationList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<NotificationModel>>() {
                            }.getType());
                        } else if (file.getName().contains("userInfo")) {
                            appendLog("Extracting user info " + file.getName());
                            JsonArray jsonArray = data.getAsJsonArray("UserInfo");
                            userInfoList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<UserInfoModel>>() {
                            }.getType());

                            for (int i = 0; i < userInfoList.size(); i++) {
                                appendLog("User Id " + sessionManager.getUserId() + " : User Info Id " + userInfoList.get(i).getUserId());
                                if (String.valueOf(userInfoList.get(i).getUserId()).equals(sessionManager.getUserId())) {
                                    sessionManager.setUserInfoId(userInfoList.get(i).getId());
                                    break;
                                }
                            }

                            for (int i = 0; i < userInfoList.size(); i++) {
                                homePresenter.insertUserInfo(userInfoList.get(i));
                                if (!(String.valueOf(userInfoList.get(i).getUserId()).equals(sessionManager.getUserId()))) {
                                    userInfoList.get(i).setImageName("");
                                } else {
                                    userInfoList.get(i).setImageName(userInfoList.get(i).getImageName());
                                }
                            }


                        } else if (file.getName().contains("userSet")) {
                            JsonArray jsonArray = data.getAsJsonArray("UserSettings");
                            userSettingsList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<UserSettingsModel>>() {
                            }.getType());

                        } else if (file.getName().contains("forms")) {
                            appendLog("Extracting form " + file.getName());
                            JsonArray jsonArray = data.getAsJsonArray("Forms");
                            formsList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<FormsModel>>() {
                            }.getType());
                            for (int i = 0; i < formsList.size(); i++) {
                                homePresenter.deleteForm(formsList.get(i));

                            }
                        }
                    }
                }


                return "Success";

            } catch (OutOfMemoryError e) {
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
            for (int i = 0; i < userSettingsList.size(); i++) {
                try {
                    appendLog("Session User Info Id " + sessionManager.getUserInfoId() + ": User settings user id " + userSettingsList.get(i).getUserID());
                    if (userSettingsList.get(i).getUserID().equals(sessionManager.getUserInfoId())) {
                        appendLog("Extracting user settings");
                        homePresenter.deleteUserSettings(userSettingsList.get(i));
                        break;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            for (int i = 0; i < notificationList.size(); i++) {
                List<ReceiverModel> receiverList = notificationList.get(i).getReceviers();
                for (int j = 0; j < receiverList.size(); j++) {
                    try {
                        appendLog("Session User Info Id " + sessionManager.getUserInfoId() + " : Receiver id " + receiverList.get(j).getRecevierId());
                        if (receiverList.get(j).getRecevierId().equals(sessionManager.getUserInfoId())) {
                            appendLog("Extracting notifications");
                            notificationList.get(i).setIsUnread(receiverList.get(j).getUnread());
                            homePresenter.deleteNotification(notificationList.get(i));
                            break;
                        }
                    } catch (Exception e) {

                    }
                }
            }
            //mainViewPager.updateRecentlyList(new ArrayList<>());
            setNotificationCount();
            getParentFolders();
            File file = new File(Environment.getExternalStorageDirectory() + "/QDMSWiki/" + zippedFileName);
            if (file.exists()) {
                file.delete();
            }
            File extractedFiles = new File(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles");
            if (extractedFiles.exists()) {
                extractedFiles.delete();
            }
            //fetch.removeListener(fetchListener);
            if (urlNum == downloadEntityLists.size()) {
                setRecommendedList();
                appendLog("Finished downloading all base/updated versions");
                try {
                    sessionManager.setKeyLastUpdatedFileName(downloadEntityLists.get(urlNum - 1).getFileName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
                mainVP.post(new Runnable() {

                    @Override
                    public void run() {
                        if (mainVP.getCurrentItem() == 0 && sessionManager.isFirstTimeLaunch()) {
                            ShowCasePreferenceUtil util = new ShowCasePreferenceUtil(HomeActivity.this);
                            try {
                                mainViewPager.setShowCaseForSearch(util);
                            } catch (Exception e) {
                            }
                            sessionManager.setFirstTimeLaunch(false);
                        }
                    }
                });
            } else if (urlNum < downloadEntityLists.size()) {
                beginDownload(downloadEntityLists.get(urlNum).getDownloadLink(), downloadEntityLists.get(urlNum).getFileName());
                urlNum = urlNum + 1;
            }
        }
    }

    public void decodeFile(String strFile, String filename) {
        try {
            if (!strFile.isEmpty()) {
                File ext = Environment.getExternalStorageDirectory();
                if (!new File(ext.getAbsolutePath() + "/QDMSWiki/Images/" + filename).exists()) {
                    byte[] data = Base64.decode(strFile, Base64.DEFAULT);
                    File mydir = new File(ext.getAbsolutePath() + "/QDMSWiki/Images");
                    File file = new File(mydir, filename);
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(data);
                    fos.close();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void createImageFolder() {
        File ext = Environment.getExternalStorageDirectory();
        File mydir = new File(ext.getAbsolutePath() + "/QDMSWiki/Images");
        if (!mydir.exists()) {
            mydir.mkdirs();
        }
    }

    public void setRecommendedList() {

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteRecommendedDocuments();
                documentList = homeDatabase.homeDao().getDocuments();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
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
                                for (int l = 0; l < userSettingsCategoryList.size(); l++) {
                                    if (documentList.get(i).getCategoryId().equals(userSettingsCategoryList.get(l).getId())) {
                                        documentList.get(i).setIsRecommended("YES");
                                        homePresenter.updateIsRecommended(documentList.get(i).getId());
                                        isRecommended = true;
                                        break;
                                    }

                                }
                            }
                        }
                    }
                }

                getRecommendedList();
            }


            @Override
            public void onError(Throwable e) {

            }
        });

    }

    public void getCurrentDocumentList() {

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
                for (int i = 0; i < documentsList.size(); i++) {
                    System.out.println("document " + documentsList.get(i).getDocumentName() + ":" + documentsList.get(i).getCategoryName());
                }

                mainViewPager.updateDocumentList(documentsList);
                getArticleList();
            }


            @Override
            public void onError(Throwable e) {

            }
        });

    }

    public void getArticleList() {

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                articleList = homeDatabase.homeDao().getArticles();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                for (int i = 0; i < articleList.size(); i++) {
                    System.out.println("article " + articleList.get(i).getArticleName() + ":" + articleList.get(i).getCategoryIds());
                }
                mainViewPager.updateArticleList(articleList);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
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
                //getCurrentDocumentList();
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


    @Override
    protected void onResume() {
        super.onResume();
        setNotificationCount();
    }
}


