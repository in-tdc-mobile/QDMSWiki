package com.mariapps.qdmswiki.notification.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NotificationModel implements Serializable {

    @SerializedName("pId")
    public Integer id;
    @SerializedName("pHeading")
    public String heading;
    @SerializedName("pStatus")
    public String status;
    @SerializedName("pUpdatedBy")
    public String updatedBy;
    @SerializedName("pTime")
    public String time;

    public NotificationModel(Integer id, String heading, String status, String updatedBy, String time) {
        this.id = id;
        this.heading = heading;
        this.status = status;
        this.updatedBy = updatedBy;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public String getHeading() {
        return heading;
    }

    public String getStatus() {
        return status;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public String getTime() {
        return time;
    }
}
