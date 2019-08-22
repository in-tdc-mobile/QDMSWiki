package com.mariapps.qdmswiki.usersettings;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.home.database.HomeTypeConverter;

@Entity(tableName = "UserSettingsCategoryEntity")
@TypeConverters(HomeTypeConverter.class)
public class UserSettingsCategoryModel implements Parcelable {

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

    @ColumnInfo(name = "Slug")
    @SerializedName("slug")
    private String slug;

    @ColumnInfo(name = "Ancestors")
    @SerializedName("ancestors")
    private String ancestors;

    @ColumnInfo(name = "IsFullVisibilityOn")
    @SerializedName("IsFullVisibilityOn")
    private Boolean isFullVisibilityOn;

    @ColumnInfo(name = "IsLeafNode")
    @SerializedName("IsLeafNode")
    private Boolean isLeafNode;

    @ColumnInfo(name = "Type")
    @SerializedName("Type")
    private String type;

    @ColumnInfo(name = "DisplayOrder")
    @SerializedName("DisplayOrder")
    private String displayOrder;

    @ColumnInfo(name = "IsDashBoardEnabled")
    @SerializedName("IsDashBoardEnabled")
    private Boolean isDashBoardEnabled;

    public UserSettingsCategoryModel(String id, String appId, String name, String parent, boolean isActive) {
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public Boolean getFullVisibilityOn() {
        return isFullVisibilityOn;
    }

    public void setFullVisibilityOn(Boolean fullVisibilityOn) {
        isFullVisibilityOn = fullVisibilityOn;
    }

    public Boolean getLeafNode() {
        return isLeafNode;
    }

    public void setLeafNode(Boolean leafNode) {
        isLeafNode = leafNode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getIsDashBoardEnabled() {
        return isDashBoardEnabled;
    }

    public void setIsDashBoardEnabled(Boolean isDashBoardEnabled) {
        this.isDashBoardEnabled = isDashBoardEnabled;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    protected UserSettingsCategoryModel(Parcel in) {
        id = in.readString();
        appId = in.readString();
        name = in.readString();
        parent = in.readString();
        isActive = in.readByte() != 0;
        slug = in.readString();
        ancestors = in.readString();
        isFullVisibilityOn = in.readByte() != 0;
        isLeafNode = in.readByte() != 0;
        type = in.readString();
        displayOrder = in.readString();
        isDashBoardEnabled = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(appId);
        dest.writeString(name);
        dest.writeString(parent);
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeString(slug);
        dest.writeString(ancestors);
        dest.writeByte((byte) (isFullVisibilityOn ? 1 : 0));
        dest.writeByte((byte) (isLeafNode ? 1 : 0));
        dest.writeString(type);
        dest.writeString(displayOrder);
        dest.writeByte((byte) (isDashBoardEnabled ? 1 : 0));
    }

    public static final Creator<UserSettingsCategoryModel> CREATOR = new Creator<UserSettingsCategoryModel>() {
        @Override
        public UserSettingsCategoryModel createFromParcel(Parcel in) {
            return new UserSettingsCategoryModel(in);
        }

        @Override
        public UserSettingsCategoryModel[] newArray(int size) {
            return new UserSettingsCategoryModel[size];
        }
    };
}
