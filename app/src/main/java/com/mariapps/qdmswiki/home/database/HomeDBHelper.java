package com.mariapps.qdmswiki.home.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aruna.ramakrishnan on 07/08/2019.
 */
public class HomeDBHelper extends SQLiteOpenHelper {

    public Boolean isDelete = true;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Home.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String VARCHAR_TYPE = " VARCHAR";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_DOCUMENT_COLLECCTION =
            "CREATE TABLE " + HomeContract.DocumentCollection.TABLE_NAME + " (" +
                    HomeContract.DocumentCollection.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                    HomeContract.DocumentCollection.COLUMN_NAME_ID + TEXT_TYPE + COMMA_SEP +
                    HomeContract.DocumentCollection.COLUMN_NAME_APP_ID + TEXT_TYPE + COMMA_SEP +
                    HomeContract.DocumentCollection.COLUMN_NAME_DOCUMENT_NAME + TEXT_TYPE + COMMA_SEP +
                    HomeContract.DocumentCollection.COLUMN_NAME_DOCUMENT_DATA + TEXT_TYPE +

                    " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + HomeContract.DocumentCollection.TABLE_NAME;
    private static final String SQL_DELETE_ALL_ENTRIES =
            "Delete from  " + HomeContract.DocumentCollection.TABLE_NAME;

    public HomeDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        isDelete = true;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DOCUMENT_COLLECCTION);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}