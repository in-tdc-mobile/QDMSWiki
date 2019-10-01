package com.mariapps.qdmswiki.settings.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by elby.samson on 29,March,2019
 */
public class LogoutRequestObj {

    @SerializedName("EmpId")
    private String empId;
    @SerializedName("DeviceId")
    private String deviceId;

    public LogoutRequestObj(String empId, String deviceId) {
        this.empId = empId;
        this.deviceId = deviceId;
    }


}
