package com.mariapps.qdmswiki;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.home.database.HomeTypeConverter;
import com.mariapps.qdmswiki.home.model.TagModel;

import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class DocumentModelObj  {

    @Id

    public Long uId;
    public String id;
    public String documentName;
    public String documentData;



    public DocumentModelObj(String id,String documentName, String documentData) {
        this.id = id;
        this.documentName = documentName;
        this.documentData = documentData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }




}
