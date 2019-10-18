package com.mariapps.qdmswiki.home.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.home.database.HomeTypeConverter;


@Entity(tableName = "ImageEntity")
@TypeConverters(HomeTypeConverter.class)
public class ImageModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public Long uId;

    @ColumnInfo(name = "Id")
    @SerializedName("_id")
    private String id;

    @ColumnInfo(name = "ImageName")
    @SerializedName("ImageName")
    private String imageName;

    @ColumnInfo(name = "DisplayName")
    @SerializedName("DisplayName")
    private String displayName;

    @ColumnInfo(name = "ImageDataAsString")
    @SerializedName("ImageDataAsString")
    private String imageDataAsString;

    @ColumnInfo(name = "ImageStream")
    @SerializedName("ImageStream")
    private String imageStream;

    public ImageModel(Long uId, String id, String imageName, String displayName, String imageDataAsString, String imageStream) {
        this.uId = uId;
        this.id = id;
        this.imageName = imageName;
        this.displayName = displayName;
        this.imageDataAsString = imageDataAsString;
        this.imageStream = imageStream;
    }

    public Long getuId() {
        return uId;
    }

    public void setuId(Long uId) {
        this.uId = uId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getImageDataAsString() {
        return imageDataAsString;
    }

    public void setImageDataAsString(String imageDataAsString) {
        this.imageDataAsString = imageDataAsString;
    }

    public String getImageStream() {
        return imageStream;
    }

    public void setImageStream(String imageStream) {
        this.imageStream = imageStream;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected ImageModel(Parcel in) {
        if (in.readByte() == 0) {
            uId = null;
        } else {
            uId = in.readLong();
        }
        id = in.readString();

        imageName = in.readString();
        displayName = in.readString();
        imageDataAsString = in.readString();
        imageStream = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (uId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(uId);
        }
        dest.writeString(id);
        dest.writeString(imageName);
        dest.writeString(displayName);
        dest.writeString(imageDataAsString);
        dest.writeString(imageStream);
    }

    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel(Parcel in) {
            return new ImageModel(in);
        }

        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };
}
