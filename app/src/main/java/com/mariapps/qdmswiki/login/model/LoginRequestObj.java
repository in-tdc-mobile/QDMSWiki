package com.mariapps.qdmswiki.login.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aruna.ramakrishnan on 14-08-2019
 */

public  class LoginRequestObj {

    @SerializedName("Username")
    private String username;
    @SerializedName("Password")
    private String password;
    @SerializedName("Token_Id")
    private String tokenId;
    @SerializedName("Device_Type")
    private String deviceType;
    @SerializedName("DeviceId")
    private String deviceId;
    @SerializedName("Version")
    private String version;
    @SerializedName("AuthType")
    private String authType;

    public LoginRequestObj(String username, String password, String tokenId, String deviceType, String deviceId, String version, String authType) {
        this.username = username;
        this.password = password;
        this.tokenId = tokenId;
        this.deviceType = deviceType;
        this.deviceId = deviceId;
        this.version = version;
        this.authType = authType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getTokenId() {
        return tokenId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getVersion() {
        return version;
    }

    public String getAuthType() {
        return authType;
    }
}
