package com.mariapps.qdmswiki.home.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkEntryModel;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkModel;
import com.mariapps.qdmswiki.home.model.ArticleModel;
import com.mariapps.qdmswiki.home.model.CategoryModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.model.FileListModel;
import com.mariapps.qdmswiki.home.model.FormsModel;
import com.mariapps.qdmswiki.home.model.TagModel;
import com.mariapps.qdmswiki.notification.model.NotificationModel;
import com.mariapps.qdmswiki.notification.model.ReceiverModel;
import com.mariapps.qdmswiki.search.model.SearchModel;
import com.mariapps.qdmswiki.usersettings.UserInfoModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsCategoryModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsTagModel;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class HomeTypeConverter {

    private static Gson gson = new Gson();
//    @TypeConverter
//    public static List<MainModel> mainEntityToList(String data) {
//        if (data == null) {
//            return Collections.emptyList();
//        }
//
//        Type listType = new TypeToken<List<MainModel>>() {}.getType();
//
//        return gson.fromJson(data, listType);
//    }
//
//    @TypeConverter
//    public static String mainEntityToString(List<MainModel> mainModelList) {
//        return gson.toJson(mainModelList);
//    }

    @TypeConverter
    public static List<DocumentModel> documentEntityToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<DocumentModel>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String documentEntityToString(List<DocumentModel> documentModelList) {
        return gson.toJson(documentModelList);
    }



    @TypeConverter
    public static List<SearchModel> searchEntityToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<SearchModel>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String searchEntityToString(List<SearchModel> searchModelList) {
        return gson.toJson(searchModelList);
    }





    @TypeConverter
    public static List<TagModel> tagEntityToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<TagModel>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String tagEntityToString(List<TagModel> tagModelList) {
        return gson.toJson(tagModelList);
    }

    @TypeConverter
    public static List<CategoryModel> categoryEntityToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<CategoryModel>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String categoryEntityToString(List<CategoryModel> categoryModelList) {
        return gson.toJson(categoryModelList);
    }

    @TypeConverter
    public static List<ArticleModel> articleEntityToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<ArticleModel>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String articleEntityToString(List<ArticleModel> articleModelList) {
        return gson.toJson(articleModelList);
    }

    @TypeConverter
    public static List<BookmarkModel> bookMarkEntityToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<BookmarkModel>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String bookMarkEntityToString(List<BookmarkModel> bookmarkModelList) {
        return gson.toJson(bookmarkModelList);
    }

    @TypeConverter
    public static List<BookmarkEntryModel> bookMarkEntryEntityToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<BookmarkEntryModel>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String bookMarkEntryEntityToString(List<BookmarkEntryModel> bookmarkEntryModelList) {
        return gson.toJson(bookmarkEntryModelList);
    }

    @TypeConverter
    public static List<NotificationModel> notificationEntityToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<NotificationModel>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String notificationEntityToString(List<NotificationModel> notificationModels) {
        return gson.toJson(notificationModels);
    }


    @TypeConverter
    public static List<ReceiverModel> receiverEntityToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<ReceiverModel>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String receiverEntityToString(List<ReceiverModel> receiverModels) {
        return gson.toJson(receiverModels);
    }

    @TypeConverter
    public static List<String> categoryIdsToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<String>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String categoryIdsToString(List<String> categoryIds) {
        return gson.toJson(categoryIds);
    }

    @TypeConverter
    public static List<UserSettingsTagModel> userSettinsgTagEntityToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<UserSettingsTagModel>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String userSettinsgTagEntityToString(List<UserSettingsTagModel> userSettingsTagModel) {
        return gson.toJson(userSettingsTagModel);
    }

    @TypeConverter
    public static List<UserSettingsCategoryModel> userSettinsgCategoryEntityToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<UserSettingsCategoryModel>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String userSettinsgCategoryEntityToString(List<UserSettingsCategoryModel> userSettingsCategoryModel) {
        return gson.toJson(userSettingsCategoryModel);
    }


    @TypeConverter
    public static List<UserSettingsModel> userSettinsgEntityToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<UserSettingsModel>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String userSettinsgEntityToString(List<UserSettingsModel> userSettingsModel){
        return gson.toJson(userSettingsModel);
    }

    @TypeConverter
    public static List<UserInfoModel> userInfoEntityToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<UserInfoModel>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String userInfoEntityToString(List<UserInfoModel> userInfoModel){
        return gson.toJson(userInfoModel);
    }

    @TypeConverter
    public static List<FileListModel> fileListEntityToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<FileListModel>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String fileListEntityToString(List<FileListModel> fileListModel){
        return gson.toJson(fileListModel);
    }

    @TypeConverter
    public static List<FormsModel> formsEntityToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<FormsModel>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String formsEntityToString(List<FormsModel> formsModel){
        return gson.toJson(formsModel);
    }

}
