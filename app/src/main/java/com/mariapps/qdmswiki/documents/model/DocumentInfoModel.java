package com.mariapps.qdmswiki.documents.model;

import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.home.model.TagModel;

import java.io.Serializable;
import java.util.ArrayList;

public class DocumentInfoModel implements Serializable {

    @SerializedName("pId")
    public Integer id;
    @SerializedName("pDocumentNumber")
    public String documentNumber;
    @SerializedName("pDocumentVersion")
    public String documentVersion;
    @SerializedName("pLocation")
    public String location;
    @SerializedName("pPublishedOn")
    public String publishedOn;
    @SerializedName("pApprovedBy")
    public String approvedBy;
    @SerializedName("pSites")
    public String sites;
    @SerializedName("pTags")
    public ArrayList<TagModel> tags;
    @SerializedName("pNoOfUsers")
    public String noOfUsers;
    @SerializedName("pUsers")
    public ArrayList<UserModel> users;
    @SerializedName("isUserShown")
    public boolean isUserShown;

    public DocumentInfoModel(Integer id, String documentNumber, String documentVersion, String location, String publishedOn, String approvedBy, String sites, ArrayList<TagModel> tags, String noOfUsers, ArrayList<UserModel> users, boolean isUerShown) {
        this.id = id;
        this.documentNumber = documentNumber;
        this.documentVersion = documentVersion;
        this.location = location;
        this.publishedOn = publishedOn;
        this.approvedBy = approvedBy;
        this.sites = sites;
        this.tags = tags;
        this.noOfUsers = noOfUsers;
        this.users = users;
        this.isUserShown = isUerShown;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentVersion() {
        return documentVersion;
    }

    public void setDocumentVersion(String documentVersion) {
        this.documentVersion = documentVersion;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(String publishedOn) {
        this.publishedOn = publishedOn;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getSites() {
        return sites;
    }

    public void setSites(String sites) {
        this.sites = sites;
    }

    public ArrayList<TagModel> getTags() {
        return tags;
    }

    public void setTags(ArrayList<TagModel> tags) {
        this.tags = tags;
    }

    public String getNoOfUsers() {
        return noOfUsers;
    }

    public void setNoOfUsers(String noOfUsers) {
        this.noOfUsers = noOfUsers;
    }

    public ArrayList<UserModel> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserModel> users) {
        this.users = users;
    }

    public boolean isUserShown() {
        return isUserShown;
    }

    public void setUserShown(boolean userShown) {
        isUserShown = userShown;
    }
}
