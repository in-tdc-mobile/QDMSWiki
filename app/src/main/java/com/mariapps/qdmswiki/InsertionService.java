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
import com.mariapps.qdmswiki.home.presenter.HomePresenter;
import com.mariapps.qdmswiki.home.view.HomeView;
import com.mariapps.qdmswiki.notification.model.NotificationModel;
import com.mariapps.qdmswiki.notification.model.ReceiverModel;
import com.mariapps.qdmswiki.search.model.SearchModel;
import com.mariapps.qdmswiki.serviceclasses.APIException;
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


public class InsertionService extends Service implements HomeView {
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
   // int urlNum = 0;
    PendingIntent contentIntent;
    final String CHANNEL_ID = "10001";
    final String CHANNEL_NAME = "Default";
    String destDirectory = "";
    String zipFilePath = "";
    String zipFilename = "";
    String type = "";
    String flag = "no";
    AsyncTask<String,Void,String> startinsertionasync=null;
    HomePresenter homePresenter;
    Boolean iscompleted =false;




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("iservice","onStartCommand");
       /* if(isMyServiceRunning(DownloadService.class)){
            //stopService(new Intent(this,DownloadService.class));
            stopSelf();
        }*/
        if(ObjectBox.get()==null){
            ObjectBox.init(getApplicationContext());
        }
        dbox = ObjectBox.get().boxFor(DocumentModelObj.class);
        abox = ObjectBox.get().boxFor(ArticleModelObj.class);
        sessionManager = new SessionManager(this.getApplicationContext());
        homePresenter = new HomePresenter(this,this);
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
        type = intent.getStringExtra("Type");
        downloadEntityLists = intent.getParcelableArrayListExtra("downloadEntityLists");
     //   urlNum = Integer.parseInt(intent.getStringExtra("urlNum"));
      //  Log.e("urlNum",urlNum+"");
        Log.e("zipFilename",zipFilename+"");
        Log.e("zipFilePath",zipFilePath);
        setStartinsertionasync();
        return START_NOT_STICKY;
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
            Log.e("asyncstask","iscancelled");
            startinsertionasync.cancel(true);
        }
        if(sessionManager.geturlno()==downloadEntityLists.size()){
            Log.e("insertionservice", "getInsertcompletedall");
            try {
                sessionManager.setKeyLastUpdatedFileName(downloadEntityLists.get(sessionManager.geturlno() - 1).getFileName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            sessionManager.seturlno("0");
            AppConfig.getInsertcompletedall().postValue("completed");

        }

        //sessionManager.geturlno()<downloadEntityLists.size() was this condition
        else if(sessionManager.geturlno()<downloadEntityLists.size()) {
            if(iscompleted){
                Intent intentStartDownload = new Intent(getApplicationContext(), DownloadService.class);
                intentStartDownload.putExtra("url", downloadEntityLists.get(sessionManager.geturlno()).getDownloadLink());
                intentStartDownload.putExtra("filename", downloadEntityLists.get(sessionManager.geturlno()).getFileName());
                intentStartDownload.putExtra("Type", downloadEntityLists.get(sessionManager.geturlno()).getType());
                intentStartDownload.putExtra("urlNum", sessionManager.geturlno()+"");
                intentStartDownload.putParcelableArrayListExtra("downloadEntityLists", (ArrayList) downloadEntityLists);
                if (!isMyServiceRunning(DownloadService.class)) {
                    Log.e("dservice caled","from i service  urlnois"+sessionManager.geturlno());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        try {
                            sessionManager.setKeyLastUpdatedFileName(downloadEntityLists.get(sessionManager.geturlno() - 1).getFileName());
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
                            sessionManager.setKeyLastUpdatedFileName(downloadEntityLists.get(sessionManager.geturlno() - 1).getFileName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        AppConfig.getInsertcompletedonce().postValue("Starting to download next file");
                        startService(intentStartDownload);
                    }
                }
            }
        }

        super.onDestroy();

    }

    private void startdownload() {
            try {
                try {
                    for (int i1 = 0; i1 < userSettingsList.size(); i1++) {
                        try {
                            appendLog("Session User Info Id " + sessionManager.getUserInfoId() + ": User settings user id " + userSettingsList.get(i1).getUserID());
                            if (userSettingsList.get(i1).getUserID().equals(sessionManager.getUserInfoId())) {
                                appendLog("Extracting user settings");
                                homeDatabase.homeDao().deleteUserSettingsEntityByUserId(userSettingsList.get(i1).getUserID());
                                homeDatabase.homeDao().insertUserSettings(userSettingsList.get(i1));
                                break;
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    }
                }
                catch (Exception e){
                    Log.e("insertionservice1", e.getLocalizedMessage());
                }

                try {
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
                }
                catch (Exception e){
                    Log.e("insertionservice2", e.getLocalizedMessage());
                }

                Log.e("insertionservice", "filedelete");
                try {
                    File file = new File(Environment.getExternalStorageDirectory() + "/QDMSWiki/" + zipFilename);
                    if (file.exists()) {
                        file.delete();
                    }
                }
                catch (Exception e){
                    Log.e("File delete exception",e.getMessage());
                }

                try {
                    File extractedFiles = new File(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles"+sessionManager.geturlno());
                    String[] children = extractedFiles.list();
                    for (int i = 0; i < children.length; i++)
                    {
                        new File(extractedFiles, children[i]).delete();
                    }
                    if (extractedFiles.exists()) {
                        extractedFiles.delete();
                    }
                }
                catch (Exception e){
                    Log.e("Folder delete exception",e.getMessage());
                }

                if (sessionManager.geturlno() == downloadEntityLists.size()) {
                    Log.e("insertionservice", "getInsertcompletedall");
                    try {
                        sessionManager.setKeyLastUpdatedFileName(downloadEntityLists.get(sessionManager.geturlno() - 1).getFileName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sessionManager.seturlno("0");
                    AppConfig.getInsertcompletedall().postValue("completed");
                }
                else if (sessionManager.geturlno() < downloadEntityLists.size()) {
                    Log.e("insertionservice", "urlNum < downloadEntityLists.size()");
                    sessionManager.addurlno();
                    stopForeground(true);
                    stopSelf();
                    iscompleted=true;
                }
            } catch (Exception e) {
                Log.e("insertionserviceexcept", e.getLocalizedMessage());
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
                File destDir = new File(destDirectory+sessionManager.geturlno());
                Log.e("exractdfolder",destDir.getAbsolutePath());
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
                File folder = new File(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles"+sessionManager.geturlno()); //This is just to cast to a File type since you pass it as a String
                Log.e("insertionfolder",folder.getAbsolutePath());
                File[] filesInFolder = folder.listFiles(); // This returns all the folders and files in your path
                for (int i = 0; i < filesInFolder.length; i++) { //For each of the entries do:
                    if (filesInFolder[i].isDirectory()) {
                        appendLog("Extracting directory " + filesInFolder[i].getName());
                        File[] filesInsideFolder = filesInFolder[i].listFiles();
                        for (int j = 0; j < filesInsideFolder.length; j++) {
                            appendLog("Copying " + filesInsideFolder[j].getName() + " to image folder");
                            copyFile(new File(filesInsideFolder[j].getAbsolutePath()), new File(Environment.getExternalStorageDirectory() + "/QDMSWiki/Images/" + filesInsideFolder[j].getName()));
                        }
                    } else {
                        if (filesInFolder[i].getName().contains("file")) {
                            try {
                                fileList.clear();
                                if (type.equals("U")) {
                                    try {
                                        fileList.addAll(readJsonStream(new FileInputStream(filesInFolder[i])));
                                        for (int index = 0;index < fileList.size(); index++) {
                                            homePresenter.deleteFile(fileList.get(index));
                                        }
                                    } catch (IOException e) {
                                        sessionManager.putJsonError("y");
                                        appendErrorLog("Zipfile: "+zipFilename +"File: "+filesInFolder[i].getName()+" , "+"Error: "+e.getLocalizedMessage());
                                        e.printStackTrace();
                                    }
                                }

                                else{
                                    try {
                                        Log.e("allocatedbefore", "" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
                                        Log.e("filenameis",filesInFolder[i].getName());
                                        fileList.addAll(readJsonStream(new FileInputStream(filesInFolder[i])));
                                        Log.e("allocatedafter", "" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
                                    } catch (Exception e) {
                                        sessionManager.putJsonError("y");
                                        appendErrorLog("Zipfile: "+zipFilename +"File: "+filesInFolder[i].getName()+" , "+"Error: "+e.getLocalizedMessage());
                                        Log.e("catchedreader", "" + filesInFolder[i].getName() + "   " + e.getLocalizedMessage());
                                    }
                                    homeDatabase.homeDao().insertFileListModelbylist(fileList);
                                    //Log.e("fileinsertion","list size is "+fileList.size()+"  count is "+filecount+"filenameis  "+fileList.get(0).getId());
                                    // Log.e("allocatedafter3", "" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
                                }

                            } catch (JsonSyntaxException e) {
                                sessionManager.putJsonError("y");
                                appendErrorLog("Zipfile: "+zipFilename +"File: "+filesInFolder[i].getName()+" , "+"Error: "+e.getLocalizedMessage());
                                appendLog("File json syntax exception : " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        else if (filesInFolder[i].getName().contains("docs")) {
                            try {
                                appendLog("Extracting document " + filesInFolder[i].getName());
                                documentList.clear();
                                if(type.equals("U")){
                                    try {
                                        documentList.addAll(readJsonStreamfordoc(new FileInputStream(filesInFolder[i])));
                                        if (sessionManager.getKeyIsSeafarerLogin().equals("True")) {
                                            for (int i1 = 0; i1 < documentList.size(); i1++) {
                                                if (documentList.get(i1).getVesselIds().size() > 0 || documentList.get(i1).getPassengersVesselIds().size() > 0) {
                                                    documentList.get(i1).setIsRecommended("NO");
                                                    List<TagModel> tagList = documentList.get(i).getTags();
                                                    homeDatabase.homeDao().insertTag(tagList);
                                                    homePresenter.insertDocumentsSingle(documentList.get(i));
                                                }
                                            }
                                            AppConfig.getInsertprogress().postValue("Inserting documents to database");
                                        } else {
                                            for (int i1 = 0; i1 < documentList.size(); i1++) {
                                                documentList.get(i1).setIsRecommended("NO");
                                                List<TagModel> tagList = documentList.get(i1).getTags();
                                                homeDatabase.homeDao().insertTag(tagList);
                                                homePresenter.insertDocumentsSingle(documentList.get(i));
                                            }
                                        }
                                    } catch (IOException e) {
                                        sessionManager.putJsonError("y");
                                        appendErrorLog("Zipfile: "+zipFilename +"File: "+filesInFolder[i].getName()+" , "+"Error: "+e.getLocalizedMessage());
                                        e.printStackTrace();
                                    }
                                }
                                else{
                                    try {
                                        documentList.addAll(readJsonStreamfordoc(new FileInputStream(filesInFolder[i])));
                                    } catch (Exception e) {
                                        sessionManager.putJsonError("y");
                                        appendErrorLog("Zipfile: "+zipFilename +"File: "+filesInFolder[i].getName()+" , "+"Error: "+e.getLocalizedMessage());
                                    }
                                    //documentList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<DocumentModel>>() {}.getType());
                                    if (sessionManager.getKeyIsSeafarerLogin().equals("True")) {
                                        List<DocumentModelObj> objList = new ArrayList<>();
                                        List<DocumentModel> docList = new ArrayList<>();
                                        for (int i1 = 0; i1 < documentList.size(); i1++) {
                                            if (documentList.get(i1).getVesselIds().size() > 0 || documentList.get(i1).getPassengersVesselIds().size() > 0) {
                                                documentList.get(i1).setIsRecommended("NO");
                                                List<TagModel> tagList = documentList.get(i).getTags();
                                                homeDatabase.homeDao().insertTag(tagList);
                                                DocumentModelObj obj = new DocumentModelObj(documentList.get(i1).id, documentList.get(i1).documentName, documentList.get(i1).documentData);
                                                objList.add(obj);
                                                docList.add(documentList.get(i1));
                                                //homePresenter.insertDocumentsSingle(documentList.get(i));
                                            }
                                        }
                                        dbox.put(objList);
                                        homeDatabase.homeDao().insertDocumentbylist(docList);
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
                                }
                            } catch (JsonSyntaxException e)
                            {
                                sessionManager.putJsonError("y");
                                appendErrorLog("Zipfile: "+zipFilename +"File: "+filesInFolder[i].getName()+" , "+"Error: "+e.getLocalizedMessage());
                                appendLog("Doc json syntax exception : " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        else if (filesInFolder[i].getName().contains("art")) { //check that it's not a dir
                            try {
                                appendLog("Extracting article " + filesInFolder[i].getName());
                                articleList.clear();
                                if(type.equals("U")){
                                    try {
                                        articleList.addAll(readJsonStreamforarticle(new FileInputStream(filesInFolder[i])));
                                        if (sessionManager.getKeyIsSeafarerLogin().equals("True")) {
                                            for (int i1 = 0; i1 < articleList.size(); i1++) {
                                                if (articleList.get(i1).getArticleToVesselIds().size() > 0 || articleList.get(i1).getArticleToPassengersVesselIds().size() > 0) {
                                                    homePresenter.deleteArticlessingle(articleList.get(i));
                                                }
                                            }
                                            AppConfig.getInsertprogress().postValue("Inserting articles to database");
                                        } else {
                                            for (int i1 = 0; i1 < articleList.size(); i1++) {
                                                homePresenter.deleteArticlessingle(articleList.get(i));
                                            }
                                        }
                                    } catch (IOException e) {
                                        sessionManager.putJsonError("y");
                                        appendErrorLog("Zipfile: "+zipFilename +"File: "+filesInFolder[i].getName()+" , "+"Error: "+e.getLocalizedMessage());
                                        e.printStackTrace();
                                    }
                                }
                                else{
                                    try {
                                        articleList.addAll(readJsonStreamforarticle(new FileInputStream(filesInFolder[i])));
                                    } catch (Exception e) {
                                        sessionManager.putJsonError("y");
                                        appendErrorLog("Zipfile: "+zipFilename +"File: "+filesInFolder[i].getName()+" , "+"Error: "+e.getLocalizedMessage());
                                        JsonParser parser = new JsonParser();
                                        JsonObject data = (JsonObject) parser.parse(new FileReader(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles"+sessionManager.geturlno()+"/" + filesInFolder[i].getName()));//path to the JSON file.
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
                                }
                            } catch (JsonSyntaxException e) {
                                sessionManager.putJsonError("y");
                                appendErrorLog("Zipfile: "+zipFilename +"File: "+filesInFolder[i].getName()+" , "+"Error: "+e.getLocalizedMessage());
                                appendLog("Article json syntax exception : " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        else if (filesInFolder[i].getName().contains("image")) {
                            try {
                                JsonParser parser = new JsonParser();
                                JsonObject data = (JsonObject) parser.parse(new FileReader(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles"+sessionManager.geturlno()+"/" + filesInFolder[i].getName()));//path to the JSON file.
                                appendLog("Extracting image " + filesInFolder[i].getName());
                                JsonArray jsonArray = data.getAsJsonArray("Images");
                                imageList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<ImageModel>>() {
                                }.getType());
                                appendLog("Extracting image size" + imageList.size()+"");
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
                                            appendLog("Extracting image size error" + e1.getLocalizedMessage());
                                            continue;
                                        }
                                    }
                                }

                            } catch (JsonSyntaxException e) {
                                sessionManager.putJsonError("y");
                                appendErrorLog("Zipfile: "+zipFilename +"File: "+filesInFolder[i].getName()+" , "+"Error: "+e.getLocalizedMessage());
                                appendLog("Image json syntax exception : " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        else if (filesInFolder[i].getName().contains("category")) {

                            try {
                                JsonParser parser = new JsonParser();
                                JsonObject data = (JsonObject) parser.parse(new FileReader(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles"+sessionManager.geturlno()+"/" + filesInFolder[i].getName()));//path to the JSON file.
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
                                sessionManager.putJsonError("y");
                                appendErrorLog("Zipfile: "+zipFilename +"File: "+filesInFolder[i].getName()+" , "+"Error: "+e.getLocalizedMessage());
                                appendLog("Category json syntax exception : " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        else if (filesInFolder[i].getName().contains("bookmarks")) {

                            try {
                                JsonParser parser = new JsonParser();
                                JsonObject data = (JsonObject) parser.parse(new FileReader(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles"+sessionManager.geturlno()+"/" + filesInFolder[i].getName()));//path to the JSON file.
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
                                sessionManager.putJsonError("y");
                                appendErrorLog("Zipfile: "+zipFilename +"File: "+filesInFolder[i].getName()+" , "+"Error: "+e.getLocalizedMessage());
                                appendLog("Bookmark json syntax exception : " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        else if (filesInFolder[i].getName().contains("notifications")) {
                            try {
                                JsonParser parser = new JsonParser();
                                JsonObject data = (JsonObject) parser.parse(new FileReader(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles"+sessionManager.geturlno()+"/" + filesInFolder[i].getName()));//path to the JSON file.
                                JsonArray jsonArray = data.getAsJsonArray("Notifications");
                                notificationList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<NotificationModel>>() {
                                }.getType());
                            } catch (JsonSyntaxException e) {
                                sessionManager.putJsonError("y");
                                appendErrorLog("Zipfile: "+zipFilename +"File: "+filesInFolder[i].getName()+" , "+"Error: "+e.getLocalizedMessage());
                                appendLog("Notification json syntax exception : " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        else if (filesInFolder[i].getName().contains("userInfo")) {
                            try {
                                JsonParser parser = new JsonParser();
                                JsonObject data = (JsonObject) parser.parse(new FileReader(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles"+sessionManager.geturlno()+"/" + filesInFolder[i].getName()));//path to the JSON file.
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
                                    if (!(String.valueOf(userInfoList.get(i1).getUserId()).equals(sessionManager.getUserId()))) {
                                        userInfoList.get(i1).setImageName("");
                                    } else {
                                        userInfoList.get(i1).setImageName(userInfoList.get(i1).getImageName());
                                    }
                                    homeDatabase.homeDao().deleteUserInfoEntity(userInfoList.get(i1).getId());
                                    homeDatabase.homeDao().insertUserInfo(userInfoList.get(i1));
                                }
                            } catch (JsonSyntaxException e) {
                                sessionManager.putJsonError("y");
                                appendErrorLog("Zipfile: "+zipFilename +"File: "+filesInFolder[i].getName()+" , "+"Error: "+e.getLocalizedMessage());
                                appendLog("USer Info json syntax exception : " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        else if (filesInFolder[i].getName().contains("userSet")) {
                            try {
                                JsonParser parser = new JsonParser();
                                JsonObject data = (JsonObject) parser.parse(new FileReader(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles"+sessionManager.geturlno()+"/" + filesInFolder[i].getName()));//path to the JSON file.
                                JsonArray jsonArray = data.getAsJsonArray("UserSettings");
                                userSettingsList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<UserSettingsModel>>() {
                                }.getType());
                            } catch (JsonSyntaxException e) {
                                sessionManager.putJsonError("y");
                                appendErrorLog("Zipfile: "+zipFilename +"File: "+filesInFolder[i].getName()+" , "+"Error: "+e.getLocalizedMessage());
                                appendLog("User settings json syntax exception : " + e.getMessage());
                                e.printStackTrace();
                            }

                        }
                        else if (filesInFolder[i].getName().contains("forms")) {
                            try {
                                JsonParser parser = new JsonParser();
                                JsonObject data = (JsonObject) parser.parse(new FileReader(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles"+sessionManager.geturlno()+"/" + filesInFolder[i].getName()));//path to the JSON file.
                                appendLog("Extracting form " + filesInFolder[i].getName());
                                JsonArray jsonArray = data.getAsJsonArray("Forms");
                                formsList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<FormsModel>>() {
                                }.getType());
                                for (int i1 = 0; i1 < formsList.size(); i1++) {
                                    homeDatabase.homeDao().deleteForm(formsList.get(i1).getId());
                                    homeDatabase.homeDao().insertForm(formsList.get(i1));

                                }
                            } catch (JsonSyntaxException e) {
                                sessionManager.putJsonError("y");
                                appendErrorLog("Zipfile: "+zipFilename +"File: "+filesInFolder[i].getName()+" , "+"Error: "+e.getLocalizedMessage());
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
                if (!new File(ext.getAbsolutePath() + "/QDMSWiki/Images").exists()) {
                    File mydir = new File(ext.getAbsolutePath() + "/QDMSWiki/Images");
                    mydir.mkdir();
                }
                byte[] data = Base64.decode(strFile, Base64.DEFAULT);
                File mydir = new File(ext.getAbsolutePath() + "/QDMSWiki/Images");
                File file = new File(mydir, filename);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(data);
                fos.close();
                appendLog(file.getName());
            }

        } catch (IOException e) {
            appendLog("error from decodefile  "+e.getLocalizedMessage());
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


    public void appendErrorLog(String text) {
        File logFile = new File("sdcard/QDMSWiki/qdms_error_file.txt");
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
            buf.append(text + " :  " +DateUtils.getCurrentDate());
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
