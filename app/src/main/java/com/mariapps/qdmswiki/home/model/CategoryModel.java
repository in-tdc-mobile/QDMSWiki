package com.mariapps.qdmswiki.home.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.home.database.HomeTypeConverter;

@Entity(tableName = "CategoryEntity")
@TypeConverters(HomeTypeConverter.class)
public class CategoryModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public Long uId;

    @ColumnInfo(name = "Id")
    @SerializedName("_id")
    private String id;

    @ColumnInfo(name = "AppId")
    @SerializedName("appId")
    private String appId;

    @ColumnInfo(name = "CategoryName")
    @SerializedName("Name")
    private String name;

    @ColumnInfo(name = "Parent")
    @SerializedName("parent")
    private String parent;

    @ColumnInfo(name = "IsActive")
    @SerializedName("IsActive")
    private boolean isActive;

    public CategoryModel(String id, String appId, String name, String parent, boolean isActive) {
        this.id = id;
        this.appId = appId;
        this.name = name;
        this.parent = parent;
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

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    protected CategoryModel(Parcel in) {
        id = in.readString();
        appId = in.readString();
        name = in.readString();
        parent = in.readString();
        isActive = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(appId);
        dest.writeString(name);
        dest.writeString(parent);
        dest.writeByte((byte) (isActive ? 1 : 0));
    }

    public static final Creator<CategoryModel> CREATOR = new Creator<CategoryModel>() {
        @Override
        public CategoryModel createFromParcel(Parcel in) {
            return new CategoryModel(in);
        }

        @Override
        public CategoryModel[] newArray(int size) {
            return new CategoryModel[size];
        }
    };
}
