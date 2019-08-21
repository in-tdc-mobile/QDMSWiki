package com.mariapps.qdmswiki.home.view;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkEntryModel;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkModel;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.custom.CustomViewPager;
import com.mariapps.qdmswiki.home.database.HomeDao;
import com.mariapps.qdmswiki.home.model.ArticleModel;
import com.mariapps.qdmswiki.home.model.CategoryModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.model.MainModel;
import com.mariapps.qdmswiki.home.model.NavDrawerObj;
import com.mariapps.qdmswiki.home.model.TagModel;
import com.mariapps.qdmswiki.home.presenter.HomePresenter;
import com.mariapps.qdmswiki.notification.model.NotificationModel;
import com.mariapps.qdmswiki.notification.model.ReceiverModel;
import com.mariapps.qdmswiki.notification.view.NotificationActivity;
import com.mariapps.qdmswiki.search.view.FolderStructureActivity;
import com.mariapps.qdmswiki.serviceclasses.APIException;
import com.mariapps.qdmswiki.settings.view.SettingsActivity;
import com.mariapps.qdmswiki.utils.ScreenUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements HomeView{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.frameLayoutOne)
    FrameLayout frameLayoutOne;
    @BindView(R.id.frameLayoutTwo)
    FrameLayout frameLayoutTwo;
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

    private ActionBarDrawerToggle mDrawerToggle;
    private HomePresenter homePresenter;
    private MainViewPager mainViewPager;
    private NavigationDrawerFragment navigationDrawerFragment;
//    private ArrayList<NavDrawerObj.MenuItemsEntity> menuItemsEntities;
//    private NavDrawerObj.MenuItemsEntity menuItemsEntity;
//    private NavDrawerObj navDrawerObj;
    int currentPosition = 0;
    private int newPosition = 0;
    private HomeActivity context;
    private ProgressDialog progressDialog;
    private JSONObject jObj;
    private long downloadID;
    private HomeDao homeDao;
    private Gson gson;

    private DocumentModel documentModel;
    List<DocumentModel> documentList = new ArrayList<>();
    List<DocumentModel> childList = new ArrayList<>();
    List<DocumentModel> parentFolderList = new ArrayList<>();
    List<TagModel> tagList = new ArrayList<>();
    List<ArticleModel> articleList = new ArrayList<>();
    List<CategoryModel> categoryList = new ArrayList<>();
    List<NotificationModel> notificationList = new ArrayList<>();
    List<ReceiverModel> receiverList = new ArrayList<>();
    List<BookmarkModel> bookmarkList = new ArrayList<>();
    List<BookmarkEntryModel> bookmarkEntryList = new ArrayList<>();

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Toast.makeText(HomeActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
                try {
                    unzip(Environment.getExternalStorageDirectory()+"/QDMSWiki/Import",Environment.getExternalStorageDirectory()+"/QDMSWiki");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        context = this;
        setSupportActionBar(toolbar);
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
        menu.add(Menu.NONE,0, Menu.NONE, "HOME")
                .setIcon(R.drawable.drawable_home_selecter);

        menu.add(Menu.NONE, 1, Menu.NONE, "DOCUMENTS")
                .setIcon(R.drawable.drawable_document_selector);

        menu.add(Menu.NONE, 2, Menu.NONE, "ARTICLES")
                .setIcon(R.drawable.drawable_article_selector);

       setBadgeCount();
    }

    private void setBadgeCount()
    {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottom_navigation.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(1);

        View notificationBadge = LayoutInflater.from(this).inflate(R.layout.notification_badge, menuView, false);
        TextView textView = notificationBadge.findViewById(R.id.notificationsBadgeTextView);
        textView.setText("15");
        itemView.addView(notificationBadge);
    }

    private void setNotificationCount()
    {
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
                        if(documentModel.getType().equals("FOLDER"))
                            homePresenter.getChildFoldersList(documentModel.getFolderid());
                        else
                        {
                            Intent intent = new Intent(HomeActivity.this, FolderStructureActivity.class);
                            intent.putExtra(AppConfig.BUNDLE_TYPE,documentModel.getType());
                            intent.putExtra(AppConfig.BUNDLE_FOLDER_NAME,"");
                            intent.putExtra(AppConfig.BUNDLE_FOLDER_ID,1);
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
    private void beginDownload(String url){
        File file=new File(Environment.getExternalStorageDirectory(),"/QDMSWiki/Import");
        if(file.exists())
            return;

        progressDialog.setMessage("Downloading files...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url))
                .setTitle("Dummy File")// Title of the Download Notification
                .setDescription("Downloading")// Description of the Download Notification
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
                .setRequiresCharging(false)// Set if charging is required to begin the download
                .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                .setMimeType("application/zip")
                .setAllowedOverRoaming(true);// Set if download is allowed on roaming network

        DownloadManager downloadManager= (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.
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

    public class ReadAndInsertJsonData extends AsyncTask<String, Integer, MainModel> {

        JSONObject jsonObject;
        MainModel mainModel;

        public ReadAndInsertJsonData() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Extracting files...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected MainModel doInBackground(String... params) {
            try {
                gson = new Gson();
                String result = "";
                mainModel = gson.fromJson(new FileReader(Environment.getExternalStorageDirectory()+"/QDMSWiki/Extract1/MasterList.json"), MainModel.class);
//                File file = new File(Environment.getExternalStorageDirectory(),"/QDMSWiki/Extract1/MasterList.json");
//                BufferedReader br = new BufferedReader(new FileReader(file));
//                String line;
//                while ((line = br.readLine()) != null) {
//                    result += line;
//                }
//                jsonObject = new JSONObject(result);
//                br.close();

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

            List<DocumentModel> documentModelList =  mainModel.getDocumentModels();
            JsonElement element = gson.toJsonTree(documentModelList, new TypeToken<List<DocumentModel>>() {}.getType());
            JsonArray documentArray = element.getAsJsonArray();
            Type documentType = new TypeToken<ArrayList<DocumentModel>>() {}.getType();
            documentList= new Gson().fromJson(documentArray.toString(), documentType);

            for (int i = 0; i < documentArray.size(); i++)
            {
                JsonObject jOBJ = documentArray.get(i).getAsJsonObject();
                JsonArray tagArray = jOBJ.getAsJsonArray("Tags");
                Type tagType = new TypeToken<ArrayList<TagModel>>() {}.getType();
                tagList= new Gson().fromJson(tagArray.toString(), tagType);
            }

            List<ArticleModel> articleList = mainModel.getArticleModels();
            JsonElement element1 = gson.toJsonTree(articleList, new TypeToken<List<ArticleModel>>() {}.getType());
            JsonArray articleArray = element1.getAsJsonArray();
            Type articleType = new TypeToken<ArrayList<ArticleModel>>() {}.getType();
            articleList= new Gson().fromJson(articleArray.toString(), articleType);

            List<CategoryModel> categoryList = mainModel.getCategoryModels();
            JsonElement element2 = gson.toJsonTree(categoryList, new TypeToken<List<CategoryModel>>() {}.getType());
            JsonArray categoryArray = element2.getAsJsonArray();
            Type categoryType = new TypeToken<ArrayList<CategoryModel>>() {}.getType();
            categoryList= new Gson().fromJson(categoryArray.toString(), categoryType);

            List<NotificationModel> notificationList = mainModel.getNotificationModels();
            JsonElement element3 = gson.toJsonTree(notificationList, new TypeToken<List<NotificationModel>>() {}.getType());
            JsonArray notificationArray = element3.getAsJsonArray();
            Type notificationType = new TypeToken<ArrayList<NotificationModel>>() {}.getType();
            notificationList= new Gson().fromJson(notificationArray.toString(), notificationType);

            for (int i = 0; i < notificationArray.size(); i++)
            {
                JsonObject jOBJ = notificationArray.get(i).getAsJsonObject();
                JsonArray recevierArray = jOBJ.getAsJsonArray("Receviers");
                Type receiverType = new TypeToken<ArrayList<ReceiverModel>>() {}.getType();
                receiverList= new Gson().fromJson(recevierArray.toString(), receiverType);
            }

            List<BookmarkModel> bookmarkList = mainModel.getBookmarkModels();
            JsonElement element4 = gson.toJsonTree(bookmarkList, new TypeToken<List<BookmarkModel>>() {}.getType());
            JsonArray bookmarkArray = element4.getAsJsonArray();
            Type bookmarkType = new TypeToken<ArrayList<BookmarkModel>>() {}.getType();
            bookmarkList= new Gson().fromJson(bookmarkArray.toString(), bookmarkType);

            for (int i = 0; i < bookmarkArray.size(); i++)
            {
                JsonObject jOBJ = bookmarkArray.get(i).getAsJsonObject();
                JsonArray bookmarkEntryArray = jOBJ.getAsJsonArray("BookmarkEntries");

                Type bookMarkEntryType = new TypeToken<ArrayList<BookmarkEntryModel>>() {}.getType();
                bookmarkEntryList= new Gson().fromJson(bookmarkEntryArray.toString(), bookMarkEntryType);
            }


            //inserting dara
            homePresenter.deleteDocuments(documentList);
            homePresenter.deleteTags(tagList);
            homePresenter.deleteArticles(articleList);
            homePresenter.deleteCategories(categoryList);
            homePresenter.deleteNotifications(notificationList);
            homePresenter.deleteReceivers(receiverList);
            homePresenter.deleteBookmarks(bookmarkList);
            homePresenter.deleteBookmarkEntries(bookmarkEntryList);

            mainViewPager.notifyDataSetChanged();
            progressDialog.dismiss();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }
}

