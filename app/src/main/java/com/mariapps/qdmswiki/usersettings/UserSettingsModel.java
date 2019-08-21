//package com.mariapps.qdmswiki.usersettings;
//
//import android.arch.persistence.room.ColumnInfo;
//import android.arch.persistence.room.Entity;
//import android.arch.persistence.room.PrimaryKey;
//import android.arch.persistence.room.TypeConverters;
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import com.google.gson.annotations.SerializedName;
//import com.mariapps.qdmswiki.home.database.HomeTypeConverter;
//import com.mariapps.qdmswiki.home.model.DocumentModel;
//
//@Entity(tableName = "UserSettingsEntity")
//@TypeConverters(HomeTypeConverter.class)
//public class UserSettingsModel implements Parcelable {
//
//    @PrimaryKey(autoGenerate = true)
//    public Long uId;
//
//    @ColumnInfo(name = "Id")
//    @SerializedName("_id")
//    public String id;
//
//
//
//    public static final Creator<UserSettingsModel> CREATOR = new Creator<UserSettingsModel>() {
//        @Override
//        public UserSettingsModel createFromParcel(Parcel in) {
//            return new UserSettingsModel(in);
//        }
//
//        @Override
//        public UserSettingsModel[] newArray(int size) {
//            return new UserSettingsModel[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//
//    }
//}
