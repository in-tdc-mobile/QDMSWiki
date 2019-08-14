package com.mariapps.qdmswiki.commonmodels;

import android.arch.persistence.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by aruna.ramakrishnan on 14-08-2019
 */
public class CommonEntity implements Serializable {

    @ColumnInfo(name = "ApiToken1")
    @SerializedName("ApiToken")
    private String apitoken;

    @ColumnInfo(name = "Message")
    @SerializedName("Message")
    private String message;

    @ColumnInfo(name = "TransactionStatus")
    @SerializedName("TransactionStatus")
    private String transactionstatus;

    @ColumnInfo(name = "TimeStamp")
    @SerializedName("TimeStamp")
    private String timestamp;

    @ColumnInfo(name = "IsAuthourized")
    @SerializedName("IsAuthourized")
    private String isauthourized;

    public String getApitoken() {
        return apitoken;
    }

    public String getMessage() {
        return message;
    }

    public String getTransactionstatus() {
        return transactionstatus;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getIsauthourized() {
        return isauthourized;
    }

    public void setApitoken(String apitoken) {
        this.apitoken = apitoken;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTransactionstatus(String transactionstatus) {
        this.transactionstatus = transactionstatus;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setIsauthourized(String isauthourized) {
        this.isauthourized = isauthourized;
    }
}
