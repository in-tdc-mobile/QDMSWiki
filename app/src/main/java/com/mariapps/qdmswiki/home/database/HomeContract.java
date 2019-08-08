package com.mariapps.qdmswiki.home.database;

import android.provider.BaseColumns;

public class HomeContract {

    public HomeContract() {
    }

    public static abstract class DocumentCollection implements BaseColumns {
        public static final String TABLE_NAME = "DocumentCollection";
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_APP_ID = "appId";
        public static final String COLUMN_NAME_DOCUMENT_NAME = "DocumentName";
        public static final String COLUMN_NAME_DOCUMENT_DATA = "DocumentData";
        public static final String COLUMN_NAME_NULLABLE = "";
    }

}
