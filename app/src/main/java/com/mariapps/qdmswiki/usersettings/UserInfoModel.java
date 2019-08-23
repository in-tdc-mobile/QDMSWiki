package com.mariapps.qdmswiki.usersettings;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.home.database.HomeTypeConverter;

@Entity(tableName = "UserInfoEntity")
@TypeConverters(HomeTypeConverter.class)
public class UserInfoModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public Long uId;

    @ColumnInfo(name = "Id")
    @SerializedName("_id")
    private String id;

    @ColumnInfo(name = "UserId")
    @SerializedName("UserId")
    private Integer userId;

    @ColumnInfo(name = "Email")
    @SerializedName("Email")
    private String email;

    @ColumnInfo(name = "ImageName")
    @SerializedName("ImageName")
    private String imageName;

    @ColumnInfo(name = "Designation")
    @SerializedName("Designation")
    private String designation;

    @ColumnInfo(name = "Name")
    @SerializedName("Name")
    private String name;

    @ColumnInfo(name = "LoginName")
    @SerializedName("LoginName")
    private String loginName;

    public UserInfoModel(String id, Integer userId, String email, String imageName, String designation, String name, String loginName) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.imageName = imageName;
        this.designation = designation;
        this.name = name;
        this.loginName = loginName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public static final Creator<UserInfoModel> CREATOR = new Creator<UserInfoModel>() {
        @Override
        public UserInfoModel createFromParcel(Parcel in) {
            return new UserInfoModel(in);
        }

        @Override
        public UserInfoModel[] newArray(int size) {
            return new UserInfoModel[size];
        }
    };

    protected UserInfoModel(Parcel in) {
        id = in.readString();
        userId = in.readInt();
        email = in.readString();
        imageName = in.readString();
        designation = in.readString();
        name = in.readString();
        loginName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(userId);
        dest.writeString(email);
        dest.writeString(imageName);
        dest.writeString(designation);
        dest.writeString(name);
        dest.writeString(loginName);

    }
}
