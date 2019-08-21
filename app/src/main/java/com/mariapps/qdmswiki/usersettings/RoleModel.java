package com.mariapps.qdmswiki.usersettings;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.home.database.HomeTypeConverter;

@Entity(tableName = "RoleModelEntity")
@TypeConverters(HomeTypeConverter.class)
public class RoleModel {

    @PrimaryKey(autoGenerate = true)
    public Long uId;

    @ColumnInfo(name = "Id")
    @SerializedName("_id")
    private String id;

    @ColumnInfo(name = "AppId")
    @SerializedName("appId")
    private String appId;

    @ColumnInfo(name = "Name")
    @SerializedName("Name")
    private String name;

    @ColumnInfo(name = "DisplayName")
    @SerializedName("DisplayName")
    private String displayName;

    @ColumnInfo(name = "Description")
    @SerializedName("Description")
    private String description;

    @ColumnInfo(name = "IsActive")
    @SerializedName("IsActive")
    private boolean isActive;




}
