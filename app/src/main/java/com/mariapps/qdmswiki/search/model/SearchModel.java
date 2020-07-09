package com.mariapps.qdmswiki.search.model;

import android.arch.persistence.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchModel implements Serializable {


    private String id;
    private String name;
    private String type;
    private String categoryId;
    private String categoryName;
    private String version;
   /* public ArrayList<String> vesselIds;
    public ArrayList<String> passengersVesselIds;*/


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

/*    public List<String> getVesselIds() {
        return vesselIds;
    }

    public void setVesselIds(ArrayList<String> vesselIds) {
        this.vesselIds = vesselIds;
    }


    public ArrayList<String> getPassengersVesselIds() {
        return passengersVesselIds;
    }

    public void setPassengersVesselIds(ArrayList<String> passengersVesselIds) {
        this.passengersVesselIds = passengersVesselIds;
    }*/
}
