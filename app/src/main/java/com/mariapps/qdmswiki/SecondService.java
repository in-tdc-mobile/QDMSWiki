package com.mariapps.qdmswiki;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
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
import com.mariapps.qdmswiki.home.view.HomeActivity;
import com.mariapps.qdmswiki.home.view.MainViewPager;
import com.mariapps.qdmswiki.home.view.NavigationDrawerFragment;
import com.mariapps.qdmswiki.notification.model.NotificationModel;
import com.mariapps.qdmswiki.search.model.SearchModel;
import com.mariapps.qdmswiki.usersettings.UserInfoModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsModel;
import com.mariapps.qdmswiki.utils.DateUtils;
import com.mariapps.qdmswiki.utils.MessageEvent;
import com.tonyodev.fetch2.FetchListener;

import org.greenrobot.eventbus.EventBus;

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

public class SecondService  extends Service {

    JsonParser parser = new JsonParser();
    JsonArray jsonArray;
    private HomePresenter homePresenter;
    private SessionManager sessionManager;

    List<CategoryModel> categoryList = new ArrayList<>();
    List<NotificationModel> notificationList = new ArrayList<>();
    List<BookmarkModel> bookmarkList = new ArrayList<>();
    List<UserSettingsModel> userSettingsList = new ArrayList<>();
    List<UserInfoModel> userInfoList = new ArrayList<>();
    List<FileListModel> fileList = new ArrayList<>();
    List<FormsModel> formsList = new ArrayList<>();
    List<ImageModel> imageList = new ArrayList<>();
    List<DownloadFilesResponseModel.DownloadEntityList> downloadEntityLists = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sessionManager = new SessionManager(this);

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

                    JsonObject data = (JsonObject) parser.parse(new FileReader(Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles/" + file.getName()));//path to the JSON file.
                    if (file.getName().contains("image")) { //check that it's not a dir
                        try {
                            appendLog("Extracting image " + file.getName());
                            jsonArray = data.getAsJsonArray("Images");
                            imageList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<ImageModel>>() {
                            }.getType());
//                                for (int i = 0; i < imageList.size(); i++) {
//                                    homePresenter.deleteImage(imageList.get(i));
//                                }
                            for (int i = 0; i < imageList.size();i++) {
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

                        } catch (JsonSyntaxException e) {
                            appendLog("Image json syntax exception : "+e.getMessage());
                            e.printStackTrace();
                        }
                    } else if (file.getName().contains("category")) {
                        try {
                            appendLog("Extracting category " + file.getName());
                            jsonArray = data.getAsJsonArray("Categories");
                            categoryList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<CategoryModel>>() {
                            }.getType());
                            for (int i = 0; i < categoryList.size(); i++) {
                                homePresenter.deleteCategory(categoryList.get(i));
                            }
                        } catch (JsonSyntaxException e) {
                            appendLog("Category json syntax exception : "+e.getMessage());
                            e.printStackTrace();
                        }
                    } else if (file.getName().contains("bookmarks")) {
                        try {
                            appendLog("Extracting bookmark " + file.getName());
                            jsonArray = data.getAsJsonArray("Bookmarks");
                            bookmarkList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<BookmarkModel>>() {
                            }.getType());
                            //homePresenter.deleteBookmarks(bookmarkList);
                            for (int i = 0; i < bookmarkList.size(); i++) {
                                homePresenter.deleteBookmark(bookmarkList.get(i));
                                List<BookmarkEntryModel> bookmarkEntryList = bookmarkList.get(i).getBookmarkEntries();
                                for (int j = 0; j < bookmarkEntryList.size(); j++) {
                                    bookmarkEntryList.get(j).setDocumentId(bookmarkList.get(i).getDocumentId());
                                }
                                for (int i1 = 0; i1 < bookmarkEntryList.size(); i1++) {
                                    homePresenter.deleteBookmarkEntries(bookmarkEntryList.get(i1));
                                }
                                //homePresenter.insertBookmarkEntries(bookmarkEntryList);
                            }
                        } catch (JsonSyntaxException e) {
                            appendLog("Bookmark json syntax exception : "+e.getMessage());
                            e.printStackTrace();
                        }
                    } else if (file.getName().contains("notifications")) {
                        try {
                            jsonArray = data.getAsJsonArray("Notifications");
                            notificationList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<NotificationModel>>() {
                            }.getType());
                        } catch (JsonSyntaxException e) {
                            appendLog("Notification json syntax exception : "+e.getMessage());
                            e.printStackTrace();
                        }
                    } else if (file.getName().contains("userInfo")) {
                        try {
                            appendLog("Extracting user info " + file.getName());
                            jsonArray = data.getAsJsonArray("UserInfo");
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
                        } catch (JsonSyntaxException e) {
                            appendLog("USer Info json syntax exception : "+e.getMessage());
                            e.printStackTrace();
                        }
                    } else if (file.getName().contains("userSet")) {
                        try {
                            jsonArray = data.getAsJsonArray("UserSettings");
                            userSettingsList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<UserSettingsModel>>() {
                            }.getType());
                        } catch (JsonSyntaxException e) {
                            appendLog("User settings json syntax exception : "+e.getMessage());
                            e.printStackTrace();
                        }

                    } else if (file.getName().contains("forms")) {
                        try {
                            appendLog("Extracting form " + file.getName());
                            jsonArray = data.getAsJsonArray("Forms");
                            formsList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<FormsModel>>() {
                            }.getType());
                            for (int i = 0; i < formsList.size(); i++) {
                                homePresenter.deleteForm(formsList.get(i));

                            }
                        } catch (JsonSyntaxException e) {
                            appendLog("Forms json syntax exception : "+e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (OutOfMemoryError e) {
            appendLog("Out of memory : "+e.getMessage());
        } catch (Exception e) {
            appendLog("Exception : "+e.getMessage());
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


}

