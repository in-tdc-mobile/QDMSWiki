package com.mariapps.qdmswiki.home.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.home.database.HomeTypeConverter;

@Entity(tableName = "TagEntity")
@TypeConverters(HomeTypeConverter.class)
public class TagModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public Long uId;

    @ColumnInfo(name = "Id")
    @SerializedName("_id")
    private Integer id;

    @ColumnInfo(name = "AppId")
    @SerializedName("appId")
    private String appId;

    @ColumnInfo(name = "TagName")
    @SerializedName("name")
    private String name;

    @ColumnInfo(name = "IsActive")
    @SerializedName("IsActive")
    private boolean isActive;

    public TagModel(Integer id, String appId, String name, boolean isActive) {
        this.id = id;
        this.appId = appId;
        this.name = name;
        this.isActive = isActive;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    protected TagModel(Parcel in) {
        id = in.readInt();
        appId = in.readString();
        name = in.readString();
        isActive = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(appId);
        dest.writeString(name);
        dest.writeByte((byte) (isActive ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TagModel> CREATOR = new Creator<TagModel>() {
        @Override
        public TagModel createFromParcel(Parcel in) {
            return new TagModel(in);
        }

        @Override
        public TagModel[] newArray(int size) {
            return new TagModel[size];
        }
    };
}
