package com.mariapps.qdmswiki.home.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.nfc.Tag;

import com.mariapps.qdmswiki.bookmarks.model.BookmarkEntryModel;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkModel;
import com.mariapps.qdmswiki.home.model.ArticleModel;
import com.mariapps.qdmswiki.home.model.CategoryModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.model.TagModel;
import com.mariapps.qdmswiki.notification.model.NotificationModel;
import com.mariapps.qdmswiki.notification.model.ReceiverModel;
import com.mariapps.qdmswiki.search.model.SearchModel;
import com.mariapps.qdmswiki.usersettings.UserInfoModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsCategoryModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsTagModel;

import java.util.List;

@Dao
public interface HomeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDocument(List<DocumentModel> documentModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertArticle(List<ArticleModel> articleModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTag(List<TagModel> tagModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategory(List<CategoryModel> categoryModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNotifications(NotificationModel notificationModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReceivers(ReceiverModel receiverModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBookmarks(List<BookmarkModel> bookmarkModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBookmarkEntries(List<BookmarkEntryModel> bookmarkEntryModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserSettings(UserSettingsModel userSettingsModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserSettingsTag(List<UserSettingsTagModel> userSettingsTagModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserSettingsCategory(List<UserSettingsCategoryModel> userSettingsCategoryModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserInfo(List<UserInfoModel> userInfoModel);

    @Query("SELECT document.Id as id, " +
            " 'Document' as type, " +
            " document.DocumentName as name, " +
            " document.CategoryId as categoryId," +
            " category.CategoryName as categoryName " +
            " FROM DocumentEntity as document " +
            " LEFT JOIN CategoryEntity as category" +
            " ON category.Id = document.CategoryId")
    List<SearchModel> getAllDocuments();

    @Query("SELECT article.Id as id, " +
            " 'Article' as type, " +
            " article.ArticleName as name, " +
            " article.CategoryIds as categoryId," +
            " 'Article' as categoryName " +
            " FROM ArticleEntity as article " +
            " LEFT JOIN CategoryEntity as category" +
            " ON category.Id = article.CategoryIds")
    List<SearchModel> getAllArticles();

    @Query("SELECT category.Id as id, " +
            " 'Folder' as type, " +
            " category.CategoryName as name, " +
            " category.Id as categoryId," +
            " '' as categoryName " +
            " FROM CategoryEntity as category "+
            " WHERE category.CategoryName != 'Entire QDMS'")
    List<SearchModel> getAllCategories();

    @Query("SELECT document.Id as id, " +
            " 'Document' as type, " +
            " document.DocumentName as name, " +
            " document.CategoryId as categoryId," +
            " category.CategoryName as categoryName " +
            " FROM DocumentEntity as document " +
            " LEFT JOIN CategoryEntity as category" +
            " ON category.Id = document.CategoryId"+
            " UNION "+
            " SELECT article.Id as id, " +
            " 'Article' as type, " +
            " article.ArticleName as name, " +
            " article.CategoryIds as categoryId," +
            " 'Article' as categoryName " +
            " FROM ArticleEntity as article " +
            " LEFT JOIN CategoryEntity as category" +
            " ON category.Id = article.CategoryIds")
    List<SearchModel> getAllDocumentsAndArticles();

    @Query("SELECT document.Id as id, " +
            " 'Document' as type, " +
            " document.DocumentName as name, " +
            " document.CategoryId as categoryId," +
            " category.CategoryName as categoryName " +
            " FROM DocumentEntity as document " +
            " LEFT JOIN CategoryEntity as category" +
            " ON category.Id = document.CategoryId"+
            " UNION "+
            " SELECT category.Id as id, " +
            " 'Folder' as type, " +
            " category.CategoryName as name, " +
            " category.Id as categoryId," +
            " '' as categoryName " +
            " FROM CategoryEntity as category " +
            " WHERE category.CategoryName != 'Entire QDMS'")
    List<SearchModel> getAllDocumentsAndFolders();

    @Query("SELECT document.Id, " +
            " document.DocumentName, " +
            " document.CategoryId," +
            " document.Version," +
            " document.tags," +
            " document.Date," +
            " category.CategoryName as categoryName " +
            " FROM DocumentEntity as document " +
            " LEFT JOIN CategoryEntity as category" +
            " ON category.Id = document.CategoryId" +
            " ORDER BY document.ApprovedDate desc")
    List<DocumentModel> getDocuments();


    @Query("SELECT document.Id, " +
            " document.DocumentName, " +
            " document.CategoryId," +
            " document.Version," +
            " document.tags," +
            " document.Date, " +
            " category.CategoryName as categoryName " +
            " FROM DocumentEntity as document " +
            " LEFT JOIN CategoryEntity as category" +
            " ON category.Id = document.CategoryId" +
            " WHERE document.isRecommended = 'YES'")
    List<DocumentModel> getRecommendedDocuments();

    @Query("SELECT article.Id, " +
            " article.ArticleName, " +
            " article.CategoryIds," +
            " article.Version," +
            " article.tags," +
            " article.Date " +
            " FROM ArticleEntity as article "+
            " ORDER BY article.ApprovedDate desc")
    List<ArticleModel> getArticles();

    @Query("SELECT category.Id as folderid, " +
            " category.CategoryName as categoryName, " +
            " 'Folder' as type, " +
            " category.Id as categoryId " +
            " FROM CategoryEntity as category " +
            " WHERE category.Parent = (SELECT category1.Id as id " +
                    " FROM CategoryEntity as category1 " +
                    " WHERE category1.categoryName = 'Entire QDMS')"+
            " ORDER BY category.DisplayOrder")
    List<DocumentModel> getParentFolders();

    @Query("SELECT document.Id as id, " +
            " 'Document' as type, " +
            " document.DocumentName as categoryName, " +
            " document.CategoryId as catId " +
            " FROM DocumentEntity as document " +
            " WHERE document.CategoryId=:parentId" +
            " UNION "+
            " SELECT category.Id as id, " +
            " 'Folder' as type, " +
            " category.CategoryName as categoryName, " +
            " category.Id as catId" +
            " FROM CategoryEntity as category" +
            " WHERE category.Parent=:parentId")
    List<DocumentModel> getChildFoldersList(String parentId);

    @Query("SELECT document.Id, " +
            " document.DocumentName, " +
            " document.DocumentData, " +
            " document.Date" +
            " FROM DocumentEntity as document " +
            " WHERE document.Id=:documentId")
    DocumentModel getDocumentData(String documentId);


    @Query("SELECT article.Id, " +
            " article.ArticleName, " +
            " article.DocumentData, " +
            " article.Date" +
            " FROM ArticleEntity as article " +
            " WHERE article.Id=:articleId")
    ArticleModel getArticleData(String articleId);

    @Query("SELECT document.Id as id, " +
            " 'Document' as type, " +
            " document.DocumentName as name, " +
            " document.CategoryId as categoryId, " +
            " category.CategoryName as categoryName " +
            " FROM DocumentEntity as document " +
            " INNER JOIN CategoryEntity as category "+
            " ON document.CategoryId = category.Id "+
            " AND document.CategoryId=:folderId " +
            " UNION "+
            " SELECT article.Id as id, " +
            " 'Article' as type, " +
            " article.ArticleName as name, " +
            " article.CategoryIds as categoryId, " +
            " 'Article' as categoryName "+
            " FROM ArticleEntity as article " +
            " WHERE article.categoryIds =:folderId")
    List<SearchModel> getAllDocumentsAndArticlesInsideFolder(String folderId);

    @Query("SELECT document.Id as id, " +
            " 'Document' as type, " +
            " document.DocumentName as name, " +
            " document.CategoryId as categoryId, " +
            " category.CategoryName as categoryName "+
            " FROM DocumentEntity as document " +
            " INNER JOIN CategoryEntity as category "+
            " ON document.CategoryId = category.Id "+
            " AND document.CategoryId=:folderId " +
            " UNION "+
            " SELECT category.Id as id, " +
            " 'Folder' as type, " +
            " category.CategoryName as name, " +
            " category.Id as categoryId, " +
            " '' as categoryName "+
            " FROM CategoryEntity as category" +
            " WHERE category.Parent=:folderId")
    List<SearchModel> getAllDocumentsAndFoldersInsideFolder(String folderId);

    @Query("SELECT document.Id as id, " +
            " 'Document' as type, " +
            " document.DocumentName as name, " +
            " document.CategoryId as categoryId, " +
            " category.CategoryName as categoryName "+
            " FROM DocumentEntity as document " +
            " INNER JOIN CategoryEntity as category "+
            " ON document.CategoryId = category.Id "+
            " AND document.CategoryId=:folderId ")
    List<SearchModel> getAllDocumentsInsideFolder(String folderId);

    @Query("SELECT article.Id as id, " +
            " 'Article' as type, " +
            " article.ArticleName as name, " +
            " article.CategoryIds as categoryId, " +
            " 'Article' as categoryName "+
            " FROM ArticleEntity as article " +
            " WHERE article.categoryIds=:folderId")
    List<SearchModel> getAllArticlesInsideFolder(String folderId);

    @Query("SELECT category.Id as id, " +
            " 'Folder' as type, " +
            " category.CategoryName as name, " +
            " category.Id as categoryId," +
            " '' as categoryName " +
            " FROM CategoryEntity as category"+
            " WHERE category.Parent=:folderId")
    List<SearchModel> getAllCategoriesInsideFolder(String folderId);

    @Query("SELECT article.Id as id, " +
            " 'Article' as type, " +
            " article.ArticleName as name, " +
            " article.CategoryIds as categoryId, " +
            " 'Article' as categoryName "+
            " FROM ArticleEntity as article " +
            " WHERE article.categoryIds=:folderId " +
            " UNION "+
            " SELECT category.Id as id, " +
            " 'Folder' as type, " +
            " category.CategoryName as name, " +
            " category.Id as categoryId, " +
            " '' as categoryName "+
            " FROM CategoryEntity as category" +
            " WHERE category.Parent=:folderId")
    List<SearchModel> getAllArticlesAndFoldersInsideFolder(String folderId);

    @Query("SELECT document.Id as id, " +
            " 'Document' as type, " +
            " document.DocumentName as name, " +
            " document.CategoryId as categoryId, " +
            " category.CategoryName as categoryName "+
            " FROM DocumentEntity as document " +
            " INNER JOIN CategoryEntity as category "+
            " ON document.CategoryId = category.Id "+
            " AND document.CategoryId=:folderId " +
            " UNION "+
            " SELECT article.Id as id, " +
            " 'Article' as type, " +
            " article.ArticleName as name, " +
            " article.CategoryIds as categoryId, " +
            " 'Article'as categoryName "+
            " FROM ArticleEntity as article " +
            " WHERE article.categoryIds=:folderId " +
            " UNION "+
            " SELECT category.Id as id, " +
            " 'Folder' as type, " +
            " category.CategoryName as name, " +
            " category.Id as categoryId, " +
            " ''as categoryName "+
            " FROM CategoryEntity as category" +
            " WHERE category.Parent=:folderId")
    List<SearchModel> getAllDocumentsArticlesAndFoldersInsideFolder(String folderId);

    @Query("SELECT article.Id as id, " +
            " 'Article' as type, " +
            " article.ArticleName as name, " +
            " article.CategoryIds as categoryId," +
            " 'Article' as categoryName " +
            " FROM ArticleEntity as article " +
            " LEFT JOIN CategoryEntity as category"+
            " ON category.Id = article.CategoryIds"+
            " UNION "+
            " SELECT category.Id as id, " +
            " 'Folder' as type, " +
            " category.CategoryName as name, " +
            " category.Id as categoryId," +
            " '' as categoryName " +
            " FROM CategoryEntity as category "+
            " WHERE category.CategoryName != 'Entire QDMS'")
    List<SearchModel> getAllArticlesAndFolders();

    @Query("SELECT document.Id as id, " +
            " 'Document' as type, " +
            " document.DocumentName as name, " +
            " document.CategoryId as categoryId," +
            " category.CategoryName as categoryName " +
            " FROM DocumentEntity as document " +
            " LEFT JOIN CategoryEntity as category" +
            " ON category.Id = document.CategoryId"+
            " UNION "+
            " SELECT article.Id as id, " +
            " 'Article' as type, " +
            " article.ArticleName as name, " +
            " article.CategoryIds as categoryId," +
            " 'Article' as categoryName " +
            " FROM ArticleEntity as article " +
            " LEFT JOIN CategoryEntity as category" +
            " ON category.Id = article.CategoryIds"+
            " UNION "+
            " SELECT category.Id as id, " +
            " 'Folder' as type, " +
            " '' as name, " +
            " category.Id as categoryId," +
            " '' as categoryName " +
            " FROM CategoryEntity as category "+
            " WHERE category.CategoryName != 'Entire QDMS'")
    List<SearchModel> getAllDocumentsArticlesAndFolders();

    @Query("SELECT user.Id, " +
            " user.Name, " +
            " user.Designation, " +
            " user.Email," +
            " user.ImageName," +
            " user.LoginName" +
            " FROM UserInfoEntity as user " +
            " WHERE user.UserId=:userId")
    UserInfoModel getUserImage(String userId);

    @Query(" SELECT bookmark.BookmarkEntries " +
            " FROM BookMarkEntity as bookmark " +
            " WHERE bookmark.DocumentId=:documentId")
    BookmarkModel getBookmarkEntries(String documentId);

    @Query( " UPDATE DocumentEntity SET isRecommended = 'YES' WHERE Id =:documentId")
    void updateIsRecommended(String documentId);

    @Query("SELECT CategoryName FROM CategoryEntity WHERE Id =:id")
    String getCategoryName(String id);

    @Query( " SELECT * FROM CategoryEntity WHERE Id=:parentId AND CategoryName != 'Entire QDMS'")
    CategoryModel getCategoryDetailsOfSelectedDocument(String parentId);

    @Query("SELECT * FROM TagEntity")
    List<TagModel> getTags();

    @Query("SELECT * FROM CategoryEntity")
    List<CategoryModel> getCategory();

    @Query("SELECT notification.Message, " +
            " notification.SendTime, " +
            " notification.Receviers, " +
            " userInfo.Name as senderName "+
            " FROM NotificationEntity as notification " +
            " LEFT JOIN userinfoentity as userinfo "+
            " ON notification.SenderId = userinfo.Id")
    List<NotificationModel> getNotifications();

    @Query("SELECT * FROM BookMarkEntity")
    List<BookmarkModel> getBookmarks();

    @Query("DELETE FROM DocumentEntity")
    void deleteDocumentEntity();

    @Query("DELETE FROM ArticleEntity")
    void deleteArticleEntity();

    @Query("DELETE FROM TagEntity")
    void deleteTagEntity();

    @Query("DELETE FROM CategoryEntity")
    void deleteCategoryEntity();

    @Query("DELETE FROM NotificationEntity")
    void deleteNotificationEntity();

    @Query("DELETE FROM ReceiverEntity")
    void deleteReceiverEntity();

    @Query("DELETE FROM BookMarkEntity")
    void deleteBookmarkEntity();

    @Query("DELETE FROM BookMarkEntryEntity")
    void deleteBookmarkEntryEntity();

    @Query("DELETE FROM UserSettingsEntity")
    void deleteUserSettingsEntity();

    @Query("DELETE FROM UserSettingsTagEntity")
    void deleteUserSettingsTagEntity();

    @Query("DELETE FROM UserSettingsCategoryEntity")
    void deleteUserSettingsCategoryEntity();

    @Query("DELETE FROM UserInfoEntity")
    void deleteUserInfoEntity();
}

