package com.mariapps.qdmswiki.usersettings;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.home.database.HomeTypeConverter;
import java.util.List;

@Entity(tableName = "UserSettingsEntity")
@TypeConverters(HomeTypeConverter.class)
public class UserSettingsModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public Long uId;

    @ColumnInfo(name = "Id")
    @SerializedName("_id")
    private String id;

    @ColumnInfo(name = "UserId")
    @SerializedName("UID")
    private String userID;

    @SerializedName("Tags")
    private List<UserSettingsTagModel> tags;

    @SerializedName("Category")
    private List<UserSettingsCategoryModel> category;

    public UserSettingsModel(String id, String userID, List<UserSettingsTagModel> tags, List<UserSettingsCategoryModel> category) {
        this.id = id;
        this.userID = userID;
        this.tags = tags;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<UserSettingsTagModel> getTags() {
        return tags;
    }

    public void setTags(List<UserSettingsTagModel> tags) {
        this.tags = tags;
    }

    public List<UserSettingsCategoryModel> getCategory() {
        return category;
    }

    public void setCategory(List<UserSettingsCategoryModel> category) {
        this.category = category;
    }

    public static final Creator<UserSettingsModel> CREATOR = new Creator<UserSettingsModel>() {
        @Override
        public UserSettingsModel createFromParcel(Parcel in) {
            return new UserSettingsModel(in);
        }

        @Override
        public UserSettingsModel[] newArray(int size) {
            return new UserSettingsModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userID);
        dest.writeTypedList(tags);
        dest.writeTypedList(category);
    }

    protected UserSettingsModel(Parcel in) {
        userID = in.readString();
        tags = in.createTypedArrayList(UserSettingsTagModel.CREATOR);
        category = in.createTypedArrayList(UserSettingsCategoryModel.CREATOR);
    }
}
