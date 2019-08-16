package com.mariapps.qdmswiki.home.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.mariapps.qdmswiki.home.model.ArticleModel;
import com.mariapps.qdmswiki.home.model.CategoryModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.model.MainModel;
import com.mariapps.qdmswiki.home.model.TagModel;

@Dao
public interface HomeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDocument(DocumentModel documentModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertArticle(ArticleModel articleModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTag(TagModel tagModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategory(CategoryModel categoryModel);

    @Query("SELECT DocumentData FROM DocumentEntity")
    String getDocuments();

    @Query("SELECT * FROM ArticleEntity")
    DocumentModel getArtcles();

    @Query("SELECT * FROM TagEntity")
    DocumentModel getTags();

    @Query("SELECT * FROM CategoryEntity")
    DocumentModel getCategory();

    @Query("DELETE FROM DocumentEntity")
    void deleteDocumentEntity();

    @Query("DELETE FROM ArticleEntity")
    void deleteArticleEntity();

    @Query("DELETE FROM TagEntity")
    void deleteTagEntity();

    @Query("DELETE FROM CategoryEntity")
    void deleteCategoryEntity();

}
