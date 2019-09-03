package com.mariapps.qdmswiki.home.view;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.gson.Gson;
import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.SessionManager;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkEntryModel;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkModel;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.custom.CustomViewPager;
import com.mariapps.qdmswiki.home.database.HomeDao;
import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.model.ArticleModel;
import com.mariapps.qdmswiki.home.model.CategoryModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;
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
import org.json.JSONObject;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import butterknife.BindView;
import butterknife.OnClick;
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
    private JSONObject jObj;
    private long downloadID;
    private HomeDao homeDao;
    private Gson gson;
    private int progressBarStatus;
    private Handler progressBarHandler = new Handler();
    private DownloadManager downloadManager;

    private DocumentModel documentModel;
    List<DocumentModel> childList = new ArrayList<>();
    List<DocumentModel> parentFolderList = new ArrayList<>();
    List<DocumentModel> recommendedList = new ArrayList<>();
    List<ReceiverModel> receiverList = new ArrayList<>();
    List<BookmarkEntryModel> bookmarkEntryList = new ArrayList<>();
    private long totalBytes = 250000;

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Toast.makeText(HomeActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
                try {
                    unzip(Environment.getExternalStorageDirectory() + "/QDMSWiki/Import", Environment.getExternalStorageDirectory() + "/QDMSWiki");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private BroadcastReceiver onDownloadProgress = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        //registerReceiver(onDownloadProgress,new IntentFilter(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));

        context = this;
        sessionManager = new SessionManager(HomeActivity.this);
        homeDatabase = HomeDatabase.getInstance(HomeActivity.this);
        setSupportActionBar(toolbar);

        getParentFolders();
        //initNavDrawer();
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
            if (size != -1) progress = (int) (downloaded*100.0/size);
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

        setBadgeCount();
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
        notificationsBadgeTextView.setText("10");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void setUpPresenter() {
        progressDialog = new ProgressDialog(HomeActivity.this);
        homePresenter = new HomePresenter(this, this);
        String url = homePresenter.getDownloadUrl();
        beginDownload(url);
    }

    @Override
    protected void isNetworkAvailable(boolean isConnected) {

    }

    private void initViewpager() {
        mainViewPager = new MainViewPager(getSupportFragmentManager());
        mainViewPager.setCount(3);

        mainVP.setOffscreenPageLimit(3);
        mainVP.setAdapter(mainViewPager);
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
                            intent.putExtra(AppConfig.BUNDLE_TYPE, documentModel.getType());
                            intent.putExtra(AppConfig.BUNDLE_NAME, documentModel.getCategoryName());
                            intent.putExtra(AppConfig.BUNDLE_FOLDER_NAME, documentModel.getCategoryName());
                            intent.putExtra(AppConfig.BUNDLE_ID, documentModel.getId());
                            intent.putExtra(AppConfig.BUNDLE_FOLDER_ID, documentModel.getCatId());
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
        File file = new File(Environment.getExternalStorageDirectory(), "/QDMSWiki/Import");
        if (file.exists()) {
            return;
//            ReadAndInsertJsonData readAndInsertJsonData = new ReadAndInsertJsonData();
//            readAndInsertJsonData.execute();
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
                    .setTitle("QDMSWiki")// Title of the Download Notification
                    .setDescription("Downloading")// Description of the Download Notification
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                    .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
                    .setRequiresCharging(false)// Set if charging is required to begin the download
                    .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                    .setMimeType("application/zip")
                    .setAllowedOverRoaming(true);// Set if download is allowed on roaming network

            downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.

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

    }

    public void unzip(String zipFilePath, String destDirectory) throws IOException {
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
        ReadAndInsertJsonData readAndInsertJsonData = new ReadAndInsertJsonData();
        readAndInsertJsonData.execute();
        zipIn.close();
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


    public class ReadAndInsertJsonData extends AsyncTask<String, Integer, MainModel> {

        JSONObject jsonObject;
        MainModel mainModel;

        public ReadAndInsertJsonData() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressLayout.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            linLayout.setAlpha(1.0f);
            relLayout.setAlpha(1.0f);
            progressDialog.setMessage("Extracting files...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected MainModel doInBackground(String... params) {
            try {
                gson = new Gson();
                mainModel = gson.fromJson(new FileReader(Environment.getExternalStorageDirectory() + "/QDMSWiki/Extract1/MasterList.json"), MainModel.class);
            } catch (Exception e) {
                progressDialog.dismiss();
            }

            return mainModel;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(MainModel mainModel) {
            super.onPostExecute(mainModel);

            List<DocumentModel> documentList = mainModel.getDocumentModels();
            List<ArticleModel> articleList = mainModel.getArticleModels();
            List<CategoryModel> categoryList = mainModel.getCategoryModels();
            List<NotificationModel> notificationList = mainModel.getNotificationModels();
            List<BookmarkModel> bookmarkList = mainModel.getBookmarkModels();
            List<UserSettingsModel> userSettingsList = mainModel.getUserSettingsModels();
            List<UserInfoModel> userInfoList = mainModel.getUserInfoModels();

            //inserting
            for (int i = 0; i < documentList.size(); i++) {
                documentList.get(i).setIsRecommended("NO");
            }

            homePresenter.deleteCategories(categoryList);
            homePresenter.deleteBookmarks(bookmarkList);
            homePresenter.deleteBookmarkEntries();


            for (int i = 0; i < documentList.size(); i++) {
                List<TagModel> tagList = documentList.get(i).getTags();
                homePresenter.deleteTags(tagList);
            }

            for (int i = 0; i < documentList.size(); i++) {
                for (int k = 0; k < categoryList.size(); k++) {
                    if (documentList.get(i).getCategoryId().equals(categoryList.get(k).getId())) {
                        documentList.get(i).setCategoryName(categoryList.get(k).getName());
                    }
                }
            }
            homePresenter.deleteDocuments(documentList);

            for (int i = 0; i < articleList.size(); i++) {
                List<String> categoryNames = new ArrayList<>();
                List<String> categoryIds = articleList.get(i).getCategoryIds();
                for (int j = 0; j < categoryIds.size(); j++) {
                    for (int k = 0; k < categoryList.size(); k++) {
                        if (categoryIds.get(j).equals(categoryList.get(k).getId())) {
                            categoryNames.add(j, categoryList.get(k).getName());
                        }
                    }
                }
                articleList.get(i).setCategoryNames(categoryNames);
            }
            homePresenter.deleteArticles(articleList);

            for (int i = 0; i < bookmarkList.size(); i++) {
                List<BookmarkEntryModel> bookmarkEntryList = bookmarkList.get(i).getBookmarkEntries();
                homePresenter.insertBookmarkEntries(bookmarkEntryList);
            }

            for (int i = 0; i < userInfoList.size(); i++) {
                if (!String.valueOf(userInfoList.get(i).getUserId()).equals(sessionManager.getUserId())) {
                    userInfoList.get(i).setImageName("");
                } else {
                    sessionManager.setUserInfoId(userInfoList.get(i).getId());
                    userInfoList.get(i).setImageName(userInfoList.get(i).getImageName());
                }
            }
            homePresenter.deleteUserInfo(userInfoList);

            for (int i = 0; i < userSettingsList.size(); i++) {
                if (userSettingsList.get(i).getUserID().equals(sessionManager.getUserInfoId())) {
                    homePresenter.deleteUserSettings(userSettingsList.get(i));
                } else
                    continue;
            }

            for (int i = 0; i < notificationList.size(); i++) {
                List<ReceiverModel> receiverList = notificationList.get(i).getReceviers();
                for (int j = 0; j < receiverList.size(); j++) {
                    if (receiverList.get(j).getRecevierId().equals(sessionManager.getUserInfoId())) {
                        homePresenter.deleteNotifications(notificationList.get(i));
                        homePresenter.deleteReceivers(receiverList.get(j));
                        break;
                    }
                }
            }

            //Recommended logic
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

            for (int i = 0; i < documentList.size(); i++) {
                if (documentList.get(i).getIsRecommended().equals("YES"))
                    recommendedList.add(documentList.get(i));
            }

            mainViewPager.updateDocumentList(documentList);
            mainViewPager.updateArticleList(articleList);
            getRecommendedList();
            mainViewPager.updateRecentlyList(new ArrayList<>());
            progressDialog.dismiss();


        }

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
        unregisterReceiver(onDownloadComplete);
    }
}


