package com.mariapps.qdmswiki.login.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.mariapps.qdmswiki.login.model.LoginResponse;


/**
 * Created by elby.samson on 06,February,2019
 */

@Database(entities = {Login.class, LoginResponse.class}, version = 2,exportSchema = false)
public abstract class LoginDatabase extends RoomDatabase {

    private static volatile LoginDatabase INSTANCE;

    public abstract LoginDao loginDao();

    public static LoginDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LoginDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                            LoginDatabase.class, "Login.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}