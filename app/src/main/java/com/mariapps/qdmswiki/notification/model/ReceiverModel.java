package com.mariapps.qdmswiki.notification.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.home.database.HomeTypeConverter;

@Entity(tableName = "ReceiverEntity")
@TypeConverters(HomeTypeConverter.class)

public class ReceiverModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public Long uId;

    @SerializedName("RecevierId")
    private String recevierId;

    @SerializedName("IsUnread")
    private Boolean isUnread;

    public Long getuId() {
        return uId;
    }

    public void setuId(Long uId) {
        this.uId = uId;
    }

    public String getRecevierId() {
        return recevierId;
    }

    public void setRecevierId(String recevierId) {
        this.recevierId = recevierId;
    }

    public Boolean getUnread() {
        return isUnread;
    }

    public void setUnread(Boolean unread) {
        isUnread = unread;
    }

    public ReceiverModel(Long uId, String recevierId, Boolean isUnread) {
        this.uId = uId;
        this.recevierId = recevierId;
        this.isUnread = isUnread;
    }

    protected ReceiverModel(Parcel in) {
        if (in.readByte() == 0) {
            uId = null;
        } else {
            uId = in.readLong();
        }
        recevierId = in.readString();
        isUnread = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (uId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(uId);
        }
        dest.writeString(recevierId);
        dest.writeInt(isUnread ? 1 : 0);
    }

    public static final Creator<ReceiverModel> CREATOR = new Creator<ReceiverModel>() {
        @Override
        public ReceiverModel createFromParcel(Parcel in) {
            return new ReceiverModel(in);
        }

        @Override
        public ReceiverModel[] newArray(int size) {
            return new ReceiverModel[size];
        }
    };
}
