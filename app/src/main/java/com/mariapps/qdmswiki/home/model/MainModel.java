package com.mariapps.qdmswiki.home.model;

import android.arch.persistence.room.TypeConverters;
import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkModel;
import com.mariapps.qdmswiki.home.database.HomeTypeConverter;
import com.mariapps.qdmswiki.notification.model.NotificationModel;
import com.mariapps.qdmswiki.usersettings.UserInfoModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsModel;

import java.util.List;

@TypeConverters(HomeTypeConverter.class)
public class MainModel {


    @SerializedName("Categories")
    private List<CategoryModel> categoryModels;
    @SerializedName("Notifications")
    private List<NotificationModel> notificationModels;
    @SerializedName("Bookmarks")
    private List<BookmarkModel> bookmarkModels;
    @SerializedName("UserSettings")
    private List<UserSettingsModel> userSettingsModels;
    @SerializedName("UserInfo")
    private List<UserInfoModel> userInfoModels;
    @SerializedName("fileChunks")
    private List<FileListModel> fileListModels;
    @SerializedName("Forms")
    private List<FormsModel> formsModels;



    public List<CategoryModel> getCategoryModels() {
        return categoryModels;
    }

    public void setCategoryModels(List<CategoryModel> categoryModels) {
        this.categoryModels = categoryModels;
    }

    public List<NotificationModel> getNotificationModels() {
        return notificationModels;
    }

    public void setNotificationModels(List<NotificationModel> notificationModels) {
        this.notificationModels = notificationModels;
    }

    public List<BookmarkModel> getBookmarkModels() {
        return bookmarkModels;
    }

    public void setBookmarkModels(List<BookmarkModel> bookmarkModels) {
        this.bookmarkModels = bookmarkModels;
    }

    public List<UserSettingsModel> getUserSettingsModels() {
        return userSettingsModels;
    }

    public void setUserSettingsModels(List<UserSettingsModel> userSettingsModels) {
        this.userSettingsModels = userSettingsModels;
    }

    public List<UserInfoModel> getUserInfoModels() {
        return userInfoModels;
    }

    public void setUserInfoModels(List<UserInfoModel> userInfoModels) {
        this.userInfoModels = userInfoModels;
    }

    public List<FileListModel> getFileListModels() {
        return fileListModels;
    }

    public void setFileListModels(List<FileListModel> fileListModels) {
        this.fileListModels = fileListModels;
    }

    public List<FormsModel> getFormsModels() {
        return formsModels;
    }

    public void setFormsModels(List<FormsModel> formsModels) {
        this.formsModels = formsModels;
    }
}
