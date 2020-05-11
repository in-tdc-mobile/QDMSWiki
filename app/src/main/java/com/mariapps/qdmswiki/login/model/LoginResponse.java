package com.mariapps.qdmswiki.login.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.commonmodels.CommonEntity;

/**
 * Created by aruna.ramakrishnan on 26-08-2019
 */

@Entity(tableName = "loginResponse")
public  class LoginResponse {

    @PrimaryKey(autoGenerate = true)
    public Long uId;

    @SerializedName("LoginQdms")
    @Embedded
    private LoginQdms loginQdms;

    @SerializedName("CommonEntity")
    @Embedded
    private CommonEntity commonEntity;

    public LoginQdms getLoginQdms() {
        return loginQdms;
    }

    public void setLoginQdms(LoginQdms loginQdms) {
        this.loginQdms = loginQdms;
    }

    public CommonEntity getCommonEntity() {
        return commonEntity;
    }

    public void setCommonEntity(CommonEntity commonEntity) {
        this.commonEntity = commonEntity;
    }

    public static class LoginQdms {

        @ColumnInfo(name = "UserId")
        @SerializedName("UserId")
        private String userId;

        @ColumnInfo(name = "LoginName")
        @SerializedName("LoginName")
        private String loginName;

        @ColumnInfo(name = "Name")
        @SerializedName("Name")
        private String name;

        @ColumnInfo(name = "Email")
        @SerializedName("Email")
        private String email;

        @ColumnInfo(name = "Rank")
        @SerializedName("Rank")
        private String rank;

        @ColumnInfo(name = "ApiToken")
        @SerializedName("ApiToken")
        private String apitoken;

        @ColumnInfo(name = "VesselName")
        @SerializedName("VesselName")
        private String vesselName;

        @ColumnInfo(name = "VesselMaidenName")
        @SerializedName("VesselMaidenName")
        private String vesselMaidenName;


        @ColumnInfo(name = "IsWikiLogin")
        @SerializedName("IsWikiLogin")
        private String isWikiLogin;

        @ColumnInfo(name = "CompanyId")
        @SerializedName("CompanyId")
        private String companyId;

        @ColumnInfo(name = "DefaultCompanyName")
        @SerializedName("DefaultCompanyName")
        private String defaultCompanyName;

        @ColumnInfo(name = "IsSeafarerLogin")
        @SerializedName("IsSeafarerLogin")
        private String isSeafarerLogin;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getApitoken() {
            return apitoken;
        }

        public void setApitoken(String apitoken) {
            this.apitoken = apitoken;
        }

        public String getVesselName() {
            return vesselName;
        }

        public void setVesselName(String vesselName) {
            this.vesselName = vesselName;
        }

        public String getVesselMaidenName() {
            return vesselMaidenName;
        }

        public void setVesselMaidenName(String vesselMaidenName) {
            this.vesselMaidenName = vesselMaidenName;
        }

        public String getIsWikiLogin() {
            return isWikiLogin;
        }

        public void setIsWikiLogin(String isWikiLogin) {
            this.isWikiLogin = isWikiLogin;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getDefaultCompanyName() {
            return defaultCompanyName;
        }

        public void setDefaultCompanyName(String defaultCompanyName) {
            this.defaultCompanyName = defaultCompanyName;
        }

        public String getIsSeafarerLogin() {
            return isSeafarerLogin;
        }

        public void setIsSeafarerLogin(String seafarerLogin) {
            isSeafarerLogin = seafarerLogin;
        }
    }






}
