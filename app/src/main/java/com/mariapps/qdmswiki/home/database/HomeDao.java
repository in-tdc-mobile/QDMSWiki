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

import java.util.ArrayList;

@Dao
public interface HomeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDocument(ArrayList<DocumentModel> documentModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertArticle(ArrayList<ArticleModel> articleModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTag(ArrayList<TagModel> tagModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategory(ArrayList<CategoryModel> categoryModel);

    @Query("SELECT DocumentData FROM DocumentEntity")
    String getDocumentData();

    @Query("SELECT * FROM DocumentEntity")
    ArrayList<DocumentModel> getDocuments();

    @Query("SELECT * FROM ArticleEntity")
    ArrayList<ArticleModel> getArticles();

    @Query("SELECT * FROM TagEntity")
    ArrayList<TagModel> getTags();

    @Query("SELECT * FROM CategoryEntity")
    ArrayList<CategoryModel> getCategory();

    @Query("DELETE FROM DocumentEntity")
    void deleteDocumentEntity();

    @Query("DELETE FROM ArticleEntity")
    void deleteArticleEntity();

    @Query("DELETE FROM TagEntity")
    void deleteTagEntity();

    @Query("DELETE FROM CategoryEntity")
    void deleteCategoryEntity();

}
