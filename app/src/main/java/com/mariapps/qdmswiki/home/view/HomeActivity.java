package com.mariapps.qdmswiki.home.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mariapps.qdmswiki.AppConfig;
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
import com.mariapps.qdmswiki.search.view.FolderStructureActivity;
import com.mariapps.qdmswiki.serviceclasses.APIException;
import com.mariapps.qdmswiki.settings.view.SettingsActivity;
import com.mariapps.qdmswiki.usersettings.UserInfoModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsCategoryModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsTagModel;
import com.mariapps.qdmswiki.utils.ScreenUtils;
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
    private int downloadID;
    private int progressBarStatus;
    private Handler progressBarHandler = new Handler();
    private FetchListener fetchListener;
    private String zipFileName;

    private DocumentModel documentModel;
    List<DocumentModel> childList = new ArrayList<>();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context = this;
        sessionManager = new SessionManager(HomeActivity.this);
        homeDatabase = HomeDatabase.getInstance(HomeActivity.this);
        progressDialog = new ProgressDialog(HomeActivity.this);


        homePresenter.getDownloadUrl(new DownloadFilesRequestModel(sessionManager.getKeyLastUpdatedFileName()));
        //beginDownload("https://qdmswiki2019.blob.core.windows.net/updateversions/20191022100905.zip");
        setSupportActionBar(toolbar);
        mainVP.setCurrentItem(0);


        getParentFolders();
        initViewpager();
        initBottomNavigation();
        setNotificationCount();
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
        mainVP.post(new Runnable() {
            @Override
            public void run() {
                if (mainVP.getCurrentItem() == 0 && sessionManager.isFirstTimeLaunch())
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

    private void beginDownload(String url,String zipFileName) {
        progressLayout.setVisibility(View.VISIBLE);
        linLayout.setAlpha(0.15f);
        relLayout.setAlpha(0.15f);
        
        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(this)
                .enableRetryOnNetworkGain(true)
                .build();

        fetch = Fetch.Impl.getInstance(fetchConfiguration);

        final Request request = new Request(url, Environment.getExternalStorageDirectory() + "/QDMSWiki/"+zipFileName);
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
                Decompress decompress = new Decompress(Environment.getExternalStorageDirectory() + "/QDMSWiki/"+zipFileName, Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles");
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
            progressLayout.setVisibility(View.GONE);
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

    @Override
    public void onGetDownloadFilesSuccess(DownloadFilesResponseModel downloadFilesResponseModel) {
        downloadEntityLists = downloadFilesResponseModel.getDownloadEntityList();

        if (downloadEntityLists.size() > 0) {
            beginDownload(downloadEntityLists.get(0).getDownloadLink(),downloadEntityLists.get(0).getFileName());
        }

    }

    @Override
    public void onGetDownloadFilesError() {

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
                            } catch (Exception e) {
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

            File file = new File(Environment.getExternalStorageDirectory() + "/QDMSWiki/"+zipFileName);
            if (file.exists()) {
                file.delete();
            }

            File file1 = new File(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles");
            if (file1.exists()) {
                file1.delete();
            }

            fetch.removeListener(fetchListener);
            urlNum = urlNum + 1;
            if(urlNum == downloadEntityLists.size()-1)
                sessionManager.setKeyLastUpdatedFileName(downloadEntityLists.get(urlNum).getFileName());
            else
                beginDownload(downloadEntityLists.get(urlNum).getDownloadLink(),downloadEntityLists.get(urlNum).getFileName());

        }

    }

    public void decodeFile(String strFile, String filename) {
        try {
            if (!strFile.isEmpty()) {
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


