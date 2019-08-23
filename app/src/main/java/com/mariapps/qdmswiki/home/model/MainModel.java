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

    @SerializedName("Documents")
    private List<DocumentModel> documentModels;
    @SerializedName("Articles")
    private List<ArticleModel> articleModels;
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

    public List<DocumentModel> getDocumentCollection() {
        return documentModels;
    }

    public void setDocumentCollection(List<DocumentModel> documentModels) {
        this.documentModels = documentModels;
    }

    public List<CategoryModel> getCategory() {
        return categoryModels;
    }

    public void setCategory(List<CategoryModel> categoryModels) {
        this.categoryModels = categoryModels;
    }

    public List<ArticleModel> getArticle() {
        return articleModels;
    }

    public void setArticle(List<ArticleModel> articleModels) {
        this.articleModels = articleModels;
    }

    public List<DocumentModel> getDocumentModels() {
        return documentModels;
    }

    public void setDocumentModels(List<DocumentModel> documentModels) {
        this.documentModels = documentModels;
    }

    public List<ArticleModel> getArticleModels() {
        return articleModels;
    }

    public void setArticleModels(List<ArticleModel> articleModels) {
        this.articleModels = articleModels;
    }

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
}
