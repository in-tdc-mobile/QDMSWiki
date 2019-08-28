package com.mariapps.qdmswiki.notification.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.home.database.HomeTypeConverter;
import com.mariapps.qdmswiki.home.model.ArticleModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.model.TagModel;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "NotificationEntity")
@TypeConverters(HomeTypeConverter.class)
public class NotificationModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public Long uId;

    @ColumnInfo(name = "Id")
    @SerializedName("_id")
    private String id;

    @ColumnInfo(name = "AppId")
    @SerializedName("AppId")
    private String appId;

    @ColumnInfo(name = "SenderId")
    @SerializedName("SenderId")
    private String senderId;

    @ColumnInfo(name = "Receviers")
    @SerializedName("Receviers")
    private List<ReceiverModel> receviers;

    @ColumnInfo(name = "Message")
    @SerializedName("Message")
    private String message;

    @ColumnInfo(name = "SendTime")
    @SerializedName("SendTime")
    private String sendTime;

    @ColumnInfo(name = "ContentDataType")
    @SerializedName("ContentDataType")
    private Integer contentDataType;

    @ColumnInfo(name = "DeliveryType")
    @SerializedName("DeliveryType")
    private Integer deliveryType;

    @ColumnInfo(name = "MessageType")
    @SerializedName("MessageType")
    private Integer messageType;

    @ColumnInfo(name = "IsCritical")
    @SerializedName("IsCritical")
    private Boolean isCritical;

    @ColumnInfo(name = "IsServiceMessage")
    @SerializedName("IsServiceMessage")
    private Boolean isServiceMessage;

    @ColumnInfo(name = "RequestURL")
    @SerializedName("RequestURL")
    private String requestURL;

    @ColumnInfo(name = "EventId")
    @SerializedName("EventId")
    private String eventId;

    @ColumnInfo(name = "Isvisible")
    @SerializedName("Isvisible")
    private Boolean isvisible;

    @ColumnInfo(name = "IsReplicated")
    @SerializedName("IsReplicated")
    private Boolean isReplicated;

    @ColumnInfo(name = "EventDescription")
    @SerializedName("EventDescription")
    private Double eventDescription;

    @SerializedName("SenderName")
    private String senderName;

    public NotificationModel(Long uId, String id, String appId, String senderId, List<ReceiverModel> receviers, String message, String sendTime, Integer contentDataType, Integer deliveryType, Integer messageType, Boolean isCritical, Boolean isServiceMessage, String requestURL, String eventId, Boolean isvisible, Boolean isReplicated, Double eventDescription) {
        this.uId = uId;
        this.id = id;
        this.appId = appId;
        this.senderId = senderId;
        this.receviers = receviers;
        this.message = message;
        this.sendTime = sendTime;
        this.contentDataType = contentDataType;
        this.deliveryType = deliveryType;
        this.messageType = messageType;
        this.isCritical = isCritical;
        this.isServiceMessage = isServiceMessage;
        this.requestURL = requestURL;
        this.eventId = eventId;
        this.isvisible = isvisible;
        this.isReplicated = isReplicated;
        this.eventDescription = eventDescription;
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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public List<ReceiverModel> getReceviers() {
        return receviers;
    }

    public void setReceviers(List<ReceiverModel> receviers) {
        this.receviers = receviers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getContentDataType() {
        return contentDataType;
    }

    public void setContentDataType(Integer contentDataType) {
        this.contentDataType = contentDataType;
    }

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Boolean getCritical() {
        return isCritical;
    }

    public void setCritical(Boolean critical) {
        isCritical = critical;
    }

    public Boolean getServiceMessage() {
        return isServiceMessage;
    }

    public void setServiceMessage(Boolean serviceMessage) {
        isServiceMessage = serviceMessage;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Boolean getIsvisible() {
        return isvisible;
    }

    public void setIsvisible(Boolean isvisible) {
        this.isvisible = isvisible;
    }

    public Boolean getReplicated() {
        return isReplicated;
    }

    public void setReplicated(Boolean replicated) {
        isReplicated = replicated;
    }

    public Double getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(Double eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    protected NotificationModel(Parcel in) {
        if (in.readByte() == 0) {
            uId = null;
        } else {
            uId = in.readLong();
        }
        appId = in.readString();
        senderId = in.readString();
        receviers = in.createTypedArrayList(ReceiverModel.CREATOR);
        message = in.readString();
        sendTime = in.readString();
        contentDataType = in.readInt();
        deliveryType = in.readInt();
        messageType = in.readInt();
        isCritical = in.readByte() != 0;
        isServiceMessage = in.readByte() != 0;
        requestURL = in.readString();
        eventId = in.readString();
        isvisible = in.readByte() != 0;
        isReplicated = in.readByte() != 0;
        eventDescription = in.readDouble();
        senderName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (uId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(uId);
        }


        dest.writeString(appId);
        dest.writeString(senderId);
        dest.writeTypedList(receviers);
        dest.writeString(message);
        dest.writeString(sendTime);
        dest.writeInt(contentDataType);
        dest.writeInt(deliveryType);
        dest.writeInt(messageType);
        dest.writeInt(isCritical ? 1 : 0);
        dest.writeInt(isServiceMessage ? 1 : 0);
        dest.writeString(requestURL);
        dest.writeString(eventId);
        dest.writeInt(isvisible ? 1 : 0);
        dest.writeInt(isReplicated ? 1 : 0);
        dest.writeDouble(eventDescription);
        dest.writeString(senderName);
    }

    public static final Creator<NotificationModel> CREATOR = new Creator<NotificationModel>() {
        @Override
        public NotificationModel createFromParcel(Parcel in) {
            return new NotificationModel(in);
        }

        @Override
        public NotificationModel[] newArray(int size) {
            return new NotificationModel[size];
        }
    };

}
