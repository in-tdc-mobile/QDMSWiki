package com.mariapps.qdmswiki.login.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.commonmodels.CommonEntity;

/**
 * Created by aruna.ramakrishnan on 14-08-2019
 */

@Entity(tableName = "loginResponse")
public  class LoginResponse {

    @PrimaryKey(autoGenerate = true)
    public Long uId;

    public Long getFie_id() {
        return uId;
    }

    public void setFie_id(Long uId) {
        this.uId = uId;
    }

    @SerializedName("UserEntity")
    @Embedded
    private UserEntity userentity;

    @SerializedName("CommonEntity")
    @Embedded
    private CommonEntity commonEntity;

    @ColumnInfo(name = "ApplicationId")
    @SerializedName("ApplicationId")
    private int applicationId;

    @ColumnInfo(name = "LastSelectedCompanyId")
    @SerializedName("LastSelectedCompanyId")
    private int lastSelectionCompanyId;

    @ColumnInfo(name = "LastSelectedCompanyName")
    @SerializedName("LastSelectedCompanyName")
    private String lastSelectedCompanyName;

    @ColumnInfo(name = "pIs_Owner")
    @SerializedName("IsOwner")
    private String isOwner;


    public String getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(String isOwner) {
        this.isOwner = isOwner;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public void setLastSelectionCompanyId(int lastSelectionCompanyId) {
        this.lastSelectionCompanyId = lastSelectionCompanyId;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public int getLastSelectionCompanyId() {
        return lastSelectionCompanyId;
    }

    public UserEntity getUserentity() {
        return userentity;
    }

    public CommonEntity getCommonEntity() {
        return commonEntity;
    }

    public void setUserentity(UserEntity userentity) {
        this.userentity = userentity;
    }

    public void setCommonEntity(CommonEntity commonEntity) {
        this.commonEntity = commonEntity;
    }

    public String getLastSelectedCompanyName() {
        return lastSelectedCompanyName;
    }

    public void setLastSelectedCompanyName(String lastSelectedCompanyName) {
        this.lastSelectedCompanyName = lastSelectedCompanyName;
    }

    public static class UserEntity {

        @ColumnInfo(name = "ApiToken")
        @SerializedName("ApiToken")
        private String apitoken;

        @ColumnInfo(name = "VesselObjectId")
        @SerializedName("VesselObjectId")
        private String vesselobjectid;

        @ColumnInfo(name = "IsDbUser")
        @SerializedName("IsDbUser")
        private String isdbuser;

        @ColumnInfo(name = "DefaultAppId")
        @SerializedName("DefaultAppId")
        private String defaultappid;

        @ColumnInfo(name = "VesselUser")
        @SerializedName("VesselUser")
        private String vesseluser;

        @ColumnInfo(name = "Fax")
        @SerializedName("Fax")
        private String Fax;

        @ColumnInfo(name = "Phone1")
        @SerializedName("Phone1")
        private String Phone1;

        @ColumnInfo(name = "Phone2")
        @SerializedName("Phone2")
        private String Phone2;

        @ColumnInfo(name = "Active")
        @SerializedName("Active")
        private String active;

        @ColumnInfo(name = "CompanyTypeId")
        @SerializedName("CompanyTypeId")
        private String companytypeid;

        @ColumnInfo(name = "CompanyId")
        @SerializedName("CompanyId")
        private String companyid;


        @ColumnInfo(name = "Email")
        @SerializedName("Email")
        private String email;

        @ColumnInfo(name = "LoginName")
        @SerializedName("LoginName")
        private String loginname;

        @ColumnInfo(name = "Name")
        @SerializedName("Name")
        private String name;

        @ColumnInfo(name = "Id")
        @SerializedName("Id")
        private String id;

        public String getApitoken() {
            return apitoken;
        }

        public String getVesselobjectid() {
            return vesselobjectid;
        }

        public String getIsdbuser() {
            return isdbuser;
        }

        public String getDefaultappid() {
            return defaultappid;
        }

        public String getVesseluser() {
            return vesseluser;
        }

        public String getActive() {
            return active;
        }

        public String getCompanytypeid() {
            return companytypeid;
        }

        public String getCompanyid() {
            return companyid;
        }

        public String getEmail() {
            return email;
        }

        public String getLoginname() {
            return loginname;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public void setApitoken(String apitoken) {
            this.apitoken = apitoken;
        }

        public void setVesselobjectid(String vesselobjectid) {
            this.vesselobjectid = vesselobjectid;
        }

        public void setIsdbuser(String isdbuser) {
            this.isdbuser = isdbuser;
        }

        public void setDefaultappid(String defaultappid) {
            this.defaultappid = defaultappid;
        }

        public void setVesseluser(String vesseluser) {
            this.vesseluser = vesseluser;
        }

        public void setActive(String active) {
            this.active = active;
        }

        public void setCompanytypeid(String companytypeid) {
            this.companytypeid = companytypeid;
        }

        public void setCompanyid(String companyid) {
            this.companyid = companyid;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setLoginname(String loginname) {
            this.loginname = loginname;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFax() {
            return Fax;
        }

        public void setFax(String fax) {
            Fax = fax;
        }

        public String getPhone1() {
            return Phone1;
        }

        public void setPhone1(String phone1) {
            Phone1 = phone1;
        }

        public String getPhone2() {
            return Phone2;
        }

        public void setPhone2(String phone2) {
            Phone2 = phone2;
        }
    }




}
