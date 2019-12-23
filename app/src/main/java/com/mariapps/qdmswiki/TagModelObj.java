package com.mariapps.qdmswiki;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.home.database.HomeTypeConverter;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class TagModelObj   {

   @Id
    public Long uId;


    private String id;


    private String appId;


    private String name;


    private boolean isActive;

    public TagModelObj(String id, String appId, String name, boolean isActive) {
        this.id = id;
        this.appId = appId;
        this.name = name;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    protected TagModelObj(Parcel in) {
        id = in.readString();
        appId = in.readString();
        name = in.readString();
        isActive = in.readByte() != 0;
    }

}
