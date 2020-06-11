package com.mariapps.qdmswiki;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkModel;
import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.model.ArticleModel;
import com.mariapps.qdmswiki.home.model.CategoryModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.model.DownloadFilesResponseModel;
import com.mariapps.qdmswiki.home.model.FileListModel;
import com.mariapps.qdmswiki.home.model.FormsModel;
import com.mariapps.qdmswiki.home.model.ImageModel;
import com.mariapps.qdmswiki.home.model.TagModel;
import com.mariapps.qdmswiki.home.presenter.HomePresenter;
import com.mariapps.qdmswiki.home.view.HomeActivity;
import com.mariapps.qdmswiki.home.view.HomeView;
import com.mariapps.qdmswiki.home.view.MainViewPager;
import com.mariapps.qdmswiki.home.view.NavigationDrawerFragment;
import com.mariapps.qdmswiki.notification.model.NotificationModel;
import com.mariapps.qdmswiki.search.model.SearchModel;
import com.mariapps.qdmswiki.serviceclasses.APIException;
import com.mariapps.qdmswiki.usersettings.UserInfoModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsModel;
import com.mariapps.qdmswiki.utils.DateUtils;
import com.mariapps.qdmswiki.utils.MessageEvent;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.DownloadBlock;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class FirstService extends Service implements HomeView {

    private ActionBarDrawerToggle mDrawerToggle;
    JsonArray jsonArray;
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        homePresenter = new HomePresenter(this, this);

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
                        try {
                            appendLog("Extracting document " + file.getName());
                            jsonArray = data.getAsJsonArray("Documents");
                            documentList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<DocumentModel>>() {
                            }.getType());
                            if (sessionManager.getKeyIsSeafarerLogin().equals("True")) {
                                for (int i = 0; i < documentList.size(); i++) {
                                    if (documentList.get(i).getVesselIds().size() > 0 || documentList.get(i).getPassengersVesselIds().size() > 0) {
                                        homePresenter.deleteDocument(documentList.get(i));
                                        documentList.get(i).setIsRecommended("NO");
                                        List<TagModel> tagList = documentList.get(i).getTags();
                                        homePresenter.insertTags(tagList);
                                    } else
                                        continue;
                                }
                            } else {
                                for (int i = 0; i < documentList.size(); i++) {
                                    homePresenter.deleteDocument(documentList.get(i));
                                    documentList.get(i).setIsRecommended("NO");
                                    List<TagModel> tagList = documentList.get(i).getTags();
                                    homePresenter.insertTags(tagList);
                                }
                            }

                        } catch (JsonSyntaxException e) {
                            appendLog("Doc json syntax exception : "+e.getMessage());
                            e.printStackTrace();
                        }
                    } else if (file.getName().contains("art")) { //check that it's not a dir
                        try {
                            appendLog("Extracting article " + file.getName());
                            jsonArray = data.getAsJsonArray("Articles");
                            articleList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<ArticleModel>>() {
                            }.getType());
                            if (sessionManager.getKeyIsSeafarerLogin().equals("True")) {
                                for (int i = 0; i < articleList.size(); i++) {
                                    if (articleList.get(i).getArticleToVesselIds().size() > 0 || articleList.get(i).getArticleToPassengersVesselIds().size() > 0) {
                                        homePresenter.deleteArticles(articleList.get(i));
                                    }
                                }
                            } else {
                                for (int i = 0; i < articleList.size(); i++) {
                                    homePresenter.deleteArticles(articleList.get(i));
                                }
                            }
                        } catch (JsonSyntaxException e) {
                            appendLog("Article json syntax exception : "+e.getMessage());
                            e.printStackTrace();
                        }
                    } else if (file.getName().contains("file")) { //check that it's not a dir
                        try {
                            appendLog("Extracting file " + file.getName());
                            jsonArray = data.getAsJsonArray("fileChunks");
                            fileList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<FileListModel>>() {
                            }.getType());

                            for (int i = 0; i < fileList.size(); i++) {
                                homePresenter.deleteFile(fileList.get(i));
                            }
                        } catch (JsonSyntaxException e) {
                            appendLog("File json syntax exception : "+e.getMessage());
                            e.printStackTrace();
                        }
                    }

                }
            }

        } catch (OutOfMemoryError e) {
            appendLog("Out of memory : "+e.getMessage());
            progressDialog.dismiss();
        } catch (Exception e) {
            appendLog("Exception : "+e.getMessage());
            progressDialog.dismiss();
        }

        EventBus.getDefault().post(new MessageEvent());

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.e("onTaskRemoved","onTaskRemoved");
    }

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
            buf.append(text + " :  " + DateUtils.getCurrentDate());
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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


    @Override
    public void onGetDownloadUrlSuccess(String url) {

    }

    @Override
    public void onGetDownloadUrlError(APIException e) {

    }

    @Override
    public void onGetParentFolderSuccess(List<DocumentModel> documentModels) {

    }

    @Override
    public void onGetChildFoldersList(List<SearchModel> documentModels) {

    }

    @Override
    public void onGetUserImageSuccess(UserInfoModel userInfoModel) {

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

    }

    @Override
    public void onGetDownloadFilesError() {

    }

    @Override
    public void onGetArticleInfoSuccess(ArticleModel articleModel) {

    }

    @Override
    public void onGetArticleInfoError() {

    }
}

