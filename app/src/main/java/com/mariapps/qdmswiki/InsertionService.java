package com.mariapps.qdmswiki;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkEntryModel;
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
import com.mariapps.qdmswiki.notification.model.NotificationModel;
import com.mariapps.qdmswiki.notification.model.ReceiverModel;
import com.mariapps.qdmswiki.search.model.SearchModel;
import com.mariapps.qdmswiki.usersettings.UserInfoModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsModel;
import com.mariapps.qdmswiki.utils.DateUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import io.objectbox.Box;


public class InsertionService extends Service {
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

    Box<DocumentModelObj> dbox;
    Box<ArticleModelObj> abox;
    SessionManager sessionManager;
    HomeDatabase homeDatabase;
    NotificationManager notificationManager;
    int urlNum = 0;
    PendingIntent contentIntent;
    final String CHANNEL_ID = "10001";
    final String CHANNEL_NAME = "Default";
    String destDirectory = "";
    String zipFilePath = "";
    String zipFilename = "";
    String flag = "no";
    AsyncTask<String,Void,String> startinsertionasync=null;





    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        dbox = ObjectBox.get().boxFor(DocumentModelObj.class);
        abox = ObjectBox.get().boxFor(ArticleModelObj.class);
        sessionManager = new SessionManager(this.getApplicationContext());
        homeDatabase = HomeDatabase.getInstance(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initChannels(this, "Processing Files", "QDMS");
        } else {
            notificationManager = (NotificationManager)
                    this.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setSmallIcon(R.drawable.app_icon)
                    .setContentTitle("QDMS")
                    // .setContentIntent(contentIntent)
                    .setContentText("Processing Files");
            notificationManager.notify(2, mBuilder.build());
            startForeground(2, mBuilder.build());
        }

        destDirectory = intent.getStringExtra("destDirectory");
        zipFilePath = Environment.getExternalStorageDirectory() + "/QDMSWiki/" + intent.getStringExtra("zipFilePath");
        zipFilename = intent.getStringExtra("zipFilePath");
        downloadEntityLists = intent.getParcelableArrayListExtra("downloadEntityLists");
        urlNum = Integer.valueOf(intent.getStringExtra("urlNum"));
       setStartinsertionasync();
        return START_STICKY;
    }

    public void setStartinsertionasync(){
        startinsertionasync=new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                Log.e("asyncstask","doInBackground");
                startInsertion();
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("asyncstask","onPostExecute");
                startdownload()
;            }
        };
        startinsertionasync.execute();
    }

    @Override
    public void onDestroy() {
        if(startinsertionasync!=null){
            Log.e("asyncstask","iscanclled");
            startinsertionasync.cancel(true);
        }
        super.onDestroy();

    }

    private void startdownload() {
            try {
                for (int i1 = 0; i1 < userSettingsList.size(); i1++) {
                    try {
                        appendLog("Session User Info Id " + sessionManager.getUserInfoId() + ": User settings user id " + userSettingsList.get(i1).getUserID());
                        if (userSettingsList.get(i1).getUserID().equals(sessionManager.getUserInfoId())) {
                            appendLog("Extracting user settings");
                            homeDatabase.homeDao().deleteUserSettingsEntity();
                            homeDatabase.homeDao().insertUserSettings(userSettingsList.get(i1));
                            break;
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
                for (int i1 = 0; i1 < notificationList.size(); i1++) {
                    List<ReceiverModel> receiverList = notificationList.get(i1).getReceviers();
                    for (int j = 0; j < receiverList.size(); j++) {
                        try {
                            appendLog("Session User Info Id " + sessionManager.getUserInfoId() + " : Receiver id " + receiverList.get(j).getRecevierId());
                            if (receiverList.get(j).getRecevierId().equals(sessionManager.getUserInfoId())) {
                                appendLog("Extracting notifications");
                                notificationList.get(i1).setIsUnread(receiverList.get(j).getUnread());
                                homeDatabase.homeDao().deleteNotification(notificationList.get(i1).getId());
                                homeDatabase.homeDao().insertNotification(notificationList.get(i1));
                                break;
                            }
                        } catch (Exception e) {

                        }

                    }
                }
                Log.e("insertionservice", "filedelete");
                File file = new File(Environment.getExternalStorageDirectory() + "/QDMSWiki/" + zipFilename);
                if (file.exists()) {
                    file.delete();
                }
                File extractedFiles = new File(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles");
                if (extractedFiles.exists()) {
                    extractedFiles.delete();
                }
                if (urlNum == downloadEntityLists.size()) {
                    Log.e("insertionservice", "getInsertcompletedall");
                    try {
                        sessionManager.setKeyLastUpdatedFileName(downloadEntityLists.get(urlNum - 1).getFileName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    AppConfig.getInsertcompletedall().postValue("completed");
                } else if (urlNum < downloadEntityLists.size()) {
                    Log.e("insertionservice", "urlNum < downloadEntityLists.size()");
                    urlNum = urlNum + 1;
                    Intent intentStartDownload = new Intent(getApplicationContext(), DownloadService.class);
                    intentStartDownload.putExtra("url", downloadEntityLists.get(urlNum).getDownloadLink());
                    intentStartDownload.putExtra("filename", downloadEntityLists.get(urlNum).getFileName());
                    intentStartDownload.putExtra("urlNum", urlNum);
                    intentStartDownload.putParcelableArrayListExtra("downloadEntityLists", (ArrayList) downloadEntityLists);
                    if (isMyServiceRunning(DownloadService.class)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            try {
                                sessionManager.setKeyLastUpdatedFileName(downloadEntityLists.get(urlNum - 1).getFileName());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Log.e("insertionservice", "startForegroundService(intentStartDownload)");
                            startForegroundService(intentStartDownload);
                            stopSelf();
                            AppConfig.getInsertcompletedonce().postValue("Starting to download next file");
                        } else {
                            try {
                                Log.e("insertionservice", "startForegroundService(intentStartDownload)");
                                sessionManager.setKeyLastUpdatedFileName(downloadEntityLists.get(urlNum - 1).getFileName());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            AppConfig.getInsertcompletedonce().postValue("Starting to download next file");
                            startService(intentStartDownload);
                            stopSelf();
                        }
                    }

                }
            } catch (Exception e) {
                Log.e("insertionservice", e.getLocalizedMessage());
            }

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


    public void startInsertion() {

        if (!destDirectory.equals("") && !zipFilePath.equals("")) {
            AppConfig.getInsertstarted().postValue("started");
            AppConfig.getInsertprogress().postValue("File unzipping");
            ZipFile zip = null;
            try {
                byte[] buffer = new byte[1024];
                File destDir = new File(destDirectory+urlNum);
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
            } finally {
                if (zip != null) {
                    try {
                        zip.close();
                    } catch (IOException ignored) {
                    }
                }
            }
            appendLog("Unzipping success");
            AppConfig.getInsertprogress().postValue("File unzipping completed");


            try {
                File folder = new File(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles"+urlNum); //This is just to cast to a File type since you pass it as a String
                File[] filesInFolder = folder.listFiles(); // This returns all the folders and files in your path
                for (int i = 0; i < filesInFolder.length; i++) { //For each of the entries do:
                    if (filesInFolder[i].isDirectory()) {
                        appendLog("Extracting directory " + filesInFolder[i].getName());
                        File[] filesInsideFolder = filesInFolder[i].listFiles();
                        for (int j = 0; j < filesInsideFolder.length; j++) {
                            appendLog("Copying " + filesInsideFolder[i].getName() + " to image folder");
                            copyFile(new File(filesInsideFolder[i].getAbsolutePath()), new File(Environment.getExternalStorageDirectory() + "/QDMSWiki/Images/" + filesInsideFolder[i].getName()));
                        }

                    } else {
                        if (filesInFolder[i].getName().contains("file")) {
                            try {
                                fileList.clear();
                                try {
                                    Log.e("allocatedbefore", "" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
                                    fileList.addAll(readJsonStream(new FileInputStream(filesInFolder[i])));
                                    Log.e("allocatedafter", "" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
                                } catch (Exception e) {
                                    Log.e("catchedreader", "" + filesInFolder[i].getName() + "   " + e.getLocalizedMessage());
                                    try {
                                        JsonParser parser = new JsonParser();
                                        JsonObject data = (JsonObject) parser.parse(new FileReader(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles/" + filesInFolder[i].getName()));//path to the JSON file.
                                        JsonArray jsonArray = data.getAsJsonArray("fileChunks");
                                        AppConfig.getInsertprogress().postValue("Inserting files to database");
                                        fileList.addAll(new Gson().fromJson(jsonArray.toString(), new TypeToken<List<FileListModel>>() {
                                        }.getType()));
                                    } catch (Exception e1) {
                                        Log.e("catchegsonparser", "" + filesInFolder[i].getName() + "   " + e.getLocalizedMessage());
                                    }
                                }
                                homeDatabase.homeDao().insertFileListModelbylist(fileList);
                                //Log.e("fileinsertion","list size is "+fileList.size()+"  count is "+filecount+"filenameis  "+fileList.get(0).getId());
                                // Log.e("allocatedafter3", "" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
                            } catch (JsonSyntaxException e) {
                                appendLog("File json syntax exception : " + e.getMessage());
                                e.printStackTrace();
                            }
                        } else if (filesInFolder[i].getName().contains("docs")) {
                            try {
                                appendLog("Extracting document " + filesInFolder[i].getName());
                                documentList.clear();
                                try {
                                    documentList.addAll(readJsonStreamfordoc(new FileInputStream(filesInFolder[i])));
                                } catch (Exception e) {
                                    JsonParser parser = new JsonParser();
                                    JsonObject data = (JsonObject) parser.parse(new FileReader(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles/" + filesInFolder[i].getName()));//path to the JSON file.
                                    JsonArray jsonArray = data.getAsJsonArray("Documents");
                                    documentList.addAll(new Gson().fromJson(jsonArray.toString(), new TypeToken<List<DocumentModel>>() {
                                    }.getType()));
                                }
                                //documentList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<DocumentModel>>() {}.getType());
                                if (sessionManager.getKeyIsSeafarerLogin().equals("True")) {
                                    List<DocumentModelObj> objList = new ArrayList<>();
                                    for (int i1 = 0; i1 < documentList.size(); i1++) {
                                        if (documentList.get(i1).getVesselIds().size() > 0 || documentList.get(i1).getPassengersVesselIds().size() > 0) {
                                            documentList.get(i1).setIsRecommended("NO");
                                            List<TagModel> tagList = documentList.get(i).getTags();
                                            homeDatabase.homeDao().insertTag(tagList);
                                            DocumentModelObj obj = new DocumentModelObj(documentList.get(i1).id, documentList.get(i1).documentName, documentList.get(i1).documentData);
                                            objList.add(obj);
                                            //homePresenter.insertDocumentsSingle(documentList.get(i));
                                        }
                                    }
                                    dbox.put(objList);
                                    homeDatabase.homeDao().insertDocumentbylist(documentList);
                                    AppConfig.getInsertprogress().postValue("Inserting documents to database");
                                } else {
                                    List<DocumentModelObj> objList = new ArrayList<>();
                                    for (int i1 = 0; i1 < documentList.size(); i1++) {
                                        documentList.get(i1).setIsRecommended("NO");
                                        List<TagModel> tagList = documentList.get(i1).getTags();
                                        homeDatabase.homeDao().insertTag(tagList);
                                        DocumentModelObj obj = new DocumentModelObj(documentList.get(i1).id, documentList.get(i1).documentName, documentList.get(i1).documentData);
                                        objList.add(obj);
                                        //homePresenter.insertDocumentsSingle(documentList.get(i));
                                    }
                                    dbox.put(objList);
                                    homeDatabase.homeDao().insertDocumentbylist(documentList);
                                    Log.e("docinsertion", "list size is " + documentList.size() + "docname is " + documentList.get(0).documentName);
                                    AppConfig.getInsertprogress().postValue("Inserting documents to database");
                                }

                            } catch (JsonSyntaxException e) {
                                appendLog("Doc json syntax exception : " + e.getMessage());
                                e.printStackTrace();
                            }
                        } else if (filesInFolder[i].getName().contains("art")) { //check that it's not a dir
                            try {
                                appendLog("Extracting article " + filesInFolder[i].getName());
                                articleList.clear();
                                try {
                                    articleList.addAll(readJsonStreamforarticle(new FileInputStream(filesInFolder[i])));
                                } catch (Exception e) {
                                    JsonParser parser = new JsonParser();
                                    JsonObject data = (JsonObject) parser.parse(new FileReader(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles/" + filesInFolder[i].getName()));//path to the JSON file.
                                    JsonArray jsonArray = data.getAsJsonArray("Articles");
                                    articleList.addAll(new Gson().fromJson(jsonArray.toString(), new TypeToken<List<ArticleModel>>() {
                                    }.getType()));
                                }
                                // articleList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<ArticleModel>>() {}.getType());
                                List<ArticleModel> newlist = new ArrayList<>();
                                if (sessionManager.getKeyIsSeafarerLogin().equals("True")) {
                                    List<ArticleModelObj> objList = new ArrayList<>();
                                    for (int i1 = 0; i1 < articleList.size(); i1++) {
                                        if (articleList.get(i1).getArticleToVesselIds().size() > 0 || articleList.get(i1).getArticleToPassengersVesselIds().size() > 0) {
                                            newlist.add(articleList.get(i1));
                                            ArticleModelObj obj = new ArticleModelObj(articleList.get(i1).getId(), articleList.get(i1).getArticleName(), articleList.get(i1).documentData);
                                            objList.add(obj);
                                            //homePresenter.deleteArticlessingle(articleList.get(i));
                                        }
                                    }
                                    abox.put(objList);
                                    homeDatabase.homeDao().insertArticlebylist(newlist);
                                    AppConfig.getInsertprogress().postValue("Inserting articles to database");
                                } else {
                                    List<ArticleModelObj> objList = new ArrayList<>();
                                    for (int i1 = 0; i1 < articleList.size(); i1++) {
                                        ArticleModelObj obj = new ArticleModelObj(articleList.get(i1).getId(), articleList.get(i1).getArticleName(), articleList.get(i1).documentData);
                                        objList.add(obj);
                                    }
                                    abox.put(objList);
                                    homeDatabase.homeDao().insertArticlebylist(articleList);
                                    AppConfig.getInsertprogress().postValue("Inserting articles to database");
                                }
                            } catch (JsonSyntaxException e) {
                                appendLog("Article json syntax exception : " + e.getMessage());
                                e.printStackTrace();
                            }
                        } else if (filesInFolder[i].getName().contains("image")) {
                            //check that it's not a dir
                            JsonParser parser = new JsonParser();
                            JsonObject data = (JsonObject) parser.parse(new FileReader(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles/" + filesInFolder[i].getName()));//path to the JSON file.
                            try {
                                appendLog("Extracting image " + filesInFolder[i].getName());
                                JsonArray jsonArray = data.getAsJsonArray("Images");
                                imageList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<ImageModel>>() {
                                }.getType());
//                                for (int i = 0; i < imageList.size(); i++) {
//                                    homePresenter.deleteImage(imageList.get(i));
//
                                for (int i1 = 0; i1 < imageList.size(); i1++) {
                                    try {
                                        if (imageList.get(i1).getImageStream() != null && !imageList.get(i1).getImageStream().isEmpty())
                                            decodeFile(imageList.get(i1).getImageStream(), imageList.get(i1).getImageName());
                                        AppConfig.getInsertprogress().postValue("Inserting images to database");
                                    } catch (Exception e) {
                                        try {
                                            if (imageList.get(i1).getImageDataAsString() != null && !imageList.get(i1).getImageDataAsString().isEmpty())
                                                decodeFile(imageList.get(i1).getImageDataAsString(), imageList.get(i1).getImageName());
                                            AppConfig.getInsertprogress().postValue("Inserting images to database");
                                        } catch (Exception e1) {
                                            continue;
                                        }
                                    }
                                }

                            } catch (JsonSyntaxException e) {
                                appendLog("Image json syntax exception : " + e.getMessage());
                                e.printStackTrace();
                            }
                        } else if (filesInFolder[i].getName().contains("category")) {
                            JsonParser parser = new JsonParser();
                            JsonObject data = (JsonObject) parser.parse(new FileReader(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles/" + filesInFolder[i].getName()));//path to the JSON file.
                            try {
                                appendLog("Extracting category " + filesInFolder[i].getName());
                                JsonArray jsonArray = data.getAsJsonArray("Categories");
                                categoryList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<CategoryModel>>() {
                                }.getType());
                                for (int i1 = 0; i1 < categoryList.size(); i1++) {
                                    homeDatabase.homeDao().deleteCategory(categoryList.get(i1).getId());
                                    homeDatabase.homeDao().insertCategory(categoryList.get(i1));
                                    AppConfig.getInsertprogress().postValue("Inserting categories to database");
                                }
                            } catch (JsonSyntaxException e) {
                                appendLog("Category json syntax exception : " + e.getMessage());
                                e.printStackTrace();
                            }
                        } else if (filesInFolder[i].getName().contains("bookmarks")) {
                            JsonParser parser = new JsonParser();
                            JsonObject data = (JsonObject) parser.parse(new FileReader(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles/" + filesInFolder[i].getName()));//path to the JSON file.
                            try {
                                appendLog("Extracting bookmark " + filesInFolder[i].getName());
                                JsonArray jsonArray = data.getAsJsonArray("Bookmarks");
                                bookmarkList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<BookmarkModel>>() {
                                }.getType());
                                //homePresenter.deleteBookmarks(bookmarkList);
                                for (int i2 = 0; i2 < bookmarkList.size(); i2++) {
                                    homeDatabase.homeDao().deleteBookmark(bookmarkList.get(i2).getId());
                                    List<BookmarkEntryModel> bookmarkEntryList = bookmarkList.get(i2).getBookmarkEntries();
                                    for (int j = 0; j < bookmarkEntryList.size(); j++) {
                                        bookmarkEntryList.get(j).setDocumentId(bookmarkList.get(i2).getDocumentId());
                                    }
                                    for (int i1 = 0; i1 < bookmarkEntryList.size(); i1++) {
                                        homeDatabase.homeDao().deleteBookmarkEntrybyid(bookmarkEntryList.get(i1).getBookmarkId());
                                        homeDatabase.homeDao().insertBookmarkEntriessingle(bookmarkEntryList.get(i1));
                                        AppConfig.getInsertprogress().postValue("Inserting bookmarks to database");
                                    }
                                    //homePresenter.insertBookmarkEntries(bookmarkEntryList);
                                }
                            } catch (JsonSyntaxException e) {
                                appendLog("Bookmark json syntax exception : " + e.getMessage());
                                e.printStackTrace();
                            }
                        } else if (filesInFolder[i].getName().contains("notifications")) {
                            JsonParser parser = new JsonParser();
                            JsonObject data = (JsonObject) parser.parse(new FileReader(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles/" + filesInFolder[i].getName()));//path to the JSON file.
                            try {
                                JsonArray jsonArray = data.getAsJsonArray("Notifications");
                                notificationList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<NotificationModel>>() {
                                }.getType());
                            } catch (JsonSyntaxException e) {
                                appendLog("Notification json syntax exception : " + e.getMessage());
                                e.printStackTrace();
                            }
                        } else if (filesInFolder[i].getName().contains("userInfo")) {
                            JsonParser parser = new JsonParser();
                            JsonObject data = (JsonObject) parser.parse(new FileReader(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles/" + filesInFolder[i].getName()));//path to the JSON file.
                            try {
                                appendLog("Extracting user info " + filesInFolder[i].getName());
                                JsonArray jsonArray = data.getAsJsonArray("UserInfo");
                                userInfoList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<UserInfoModel>>() {
                                }.getType());
                                for (int i1 = 0; i1 < userInfoList.size(); i1++) {
                                    appendLog("User Id " + sessionManager.getUserId() + " : User Info Id " + userInfoList.get(i1).getUserId());
                                    if (String.valueOf(userInfoList.get(i1).getUserId()).equals(sessionManager.getUserId())) {
                                        sessionManager.setUserInfoId(userInfoList.get(i1).getId());
                                        break;
                                    }
                                }
                                for (int i1 = 0; i1 < userInfoList.size(); i1++) {
                                    homeDatabase.homeDao().insertUserInfo(userInfoList.get(i1));
                                    if (!(String.valueOf(userInfoList.get(i1).getUserId()).equals(sessionManager.getUserId()))) {
                                        userInfoList.get(i1).setImageName("");
                                    } else {
                                        userInfoList.get(i1).setImageName(userInfoList.get(i1).getImageName());
                                    }
                                }
                            } catch (JsonSyntaxException e) {
                                appendLog("USer Info json syntax exception : " + e.getMessage());
                                e.printStackTrace();
                            }
                        } else if (filesInFolder[i].getName().contains("userSet")) {
                            JsonParser parser = new JsonParser();
                            JsonObject data = (JsonObject) parser.parse(new FileReader(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles/" + filesInFolder[i].getName()));//path to the JSON file.
                            try {
                                JsonArray jsonArray = data.getAsJsonArray("UserSettings");
                                userSettingsList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<UserSettingsModel>>() {
                                }.getType());
                            } catch (JsonSyntaxException e) {
                                appendLog("User settings json syntax exception : " + e.getMessage());
                                e.printStackTrace();
                            }

                        } else if (filesInFolder[i].getName().contains("forms")) {
                            JsonParser parser = new JsonParser();
                            JsonObject data = (JsonObject) parser.parse(new FileReader(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles/" + filesInFolder[i].getName()));//path to the JSON file.
                            try {
                                appendLog("Extracting form " + filesInFolder[i].getName());
                                JsonArray jsonArray = data.getAsJsonArray("Forms");
                                formsList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<FormsModel>>() {
                                }.getType());
                                for (int i1 = 0; i1 < formsList.size(); i1++) {
                                    homeDatabase.homeDao().deleteForm(formsList.get(i1).getId());
                                    homeDatabase.homeDao().insertForm(formsList.get(i1));

                                }
                            } catch (JsonSyntaxException e) {
                                appendLog("Forms json syntax exception : " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }



                }


            } catch (Exception e) {
                Log.e("insertionservice", e.getLocalizedMessage());
                appendLog("Exception : " + e.getMessage());
                //progressDialog.dismiss();

            }


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

    public List<FileListModel> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<FileListModel> messages = new ArrayList<FileListModel>();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("fileChunks")) {
                reader.beginArray();
                while (reader.hasNext()) {
                    FileListModel message = new Gson().fromJson(reader, FileListModel.class);
                    messages.add(message);
                }
            }
        }
        reader.endArray();
        reader.close();
        return messages;
    }


    public List<DocumentModel> readJsonStreamfordoc(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<DocumentModel> messages = new ArrayList<DocumentModel>();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("Documents")) {
                reader.beginArray();
                while (reader.hasNext()) {
                    DocumentModel message = new Gson().fromJson(reader, DocumentModel.class);
                    messages.add(message);
                }
            }
        }
        reader.endArray();
        reader.close();
        return messages;
    }


    public List<ArticleModel> readJsonStreamforarticle(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<ArticleModel> messages = new ArrayList<ArticleModel>();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("Articles")) {
                reader.beginArray();
                while (reader.hasNext()) {
                    ArticleModel message = new Gson().fromJson(reader, ArticleModel.class);
                    messages.add(message);
                }
            }
        }
        reader.endArray();
        reader.close();
        return messages;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initChannels(Context context, String msg, String title) {
        notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
        notificationChannel.setShowBadge(true);
        notificationChannel.setName("QDMS");
        notificationChannel.setDescription(msg);
        notificationManager.createNotificationChannel(notificationChannel);
        Notification notification = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(R.drawable.app_icon)
                .setChannelId(CHANNEL_ID)
                .setContentIntent(contentIntent)
                .build();
        notificationManager.notify(2, notification);
        startForeground(2, notification);
    }

}
