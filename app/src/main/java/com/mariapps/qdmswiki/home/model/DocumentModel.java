package com.mariapps.qdmswiki.home.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.home.database.HomeTypeConverter;
import java.io.Serializable;
import java.util.List;

@Entity(tableName = "DocumentEntity")
@TypeConverters(HomeTypeConverter.class)
public class DocumentModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public Long uId;

    @ColumnInfo(name = "Id")
    @SerializedName("_id")
    public String id;

    @ColumnInfo(name = "CategoryId")
    @SerializedName("CategoryId")
    public String categoryId;

    @ColumnInfo(name = "DocumentCode")
    @SerializedName("DocumentCode")
    public String documentCode;

    @ColumnInfo(name = "DocumentName")
    @SerializedName("DocumentName")
    public String documentName;

    @ColumnInfo(name = "DocumentNumber")
    @SerializedName("DocumentNumber")
    public String documentNumber;

    @ColumnInfo(name = "DocumentData")
    @SerializedName("DocumentData")
    public String documentData;

    @ColumnInfo(name = "ApprovedBy")
    @SerializedName("ApprovedBy")
    private String approvedBy;

    @ColumnInfo(name = "ApprovedDate")
    @SerializedName("ApprovedDate")
    private String approvedDate;

    @ColumnInfo(name = "Date")
    @SerializedName("Date")
    private String date;

    @ColumnInfo(name = "Version")
    @SerializedName("Version")
    public String version;

    @ColumnInfo(name = "ToolTip")
    @SerializedName("ToolTip")
    public String toolTip;

    @SerializedName("Tags")
    private List<TagModel> tags;

    @SerializedName("CategoryName")
    private String categoryName;

    @SerializedName("Type")
    private String type;

    @SerializedName("folderid")
    private String folderid;

    @SerializedName("isRecommended")
    private String isRecommended;

    @SerializedName("catId")
    private String catId;

    public DocumentModel(String id, String categoryId, String documentCode, String documentName, String documentData, String version,List<TagModel> tags) {
        this.id = id;
        this.categoryId = categoryId;
        this.documentCode = documentCode;
        this.documentName = documentName;
        this.documentData = documentData;
        this.version = version;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentData() {
        return documentData;
    }

    public void setDocumentData(String documentData) {
        this.documentData = documentData;
    }

    public Long getuId() {
        return uId;
    }

    public void setuId(Long uId) {
        this.uId = uId;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public List<TagModel> getTags() {
        return tags;
    }

    public void setTags(List<TagModel> tags) {
        this.tags = tags;
    }


    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<TagModel> getTagModelList() {
        return tags;
    }

    public void setTagModelList(List<TagModel> tagModelList) {
        this.tags = tags;
    }

    public String getToolTip() {
        return toolTip;
    }

    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public String getFolderid() {
        return folderid;
    }

    public void setFolderid(String folderid) {
        this.folderid = folderid;
    }

    public String getIsRecommended() {
        return isRecommended;
    }

    public void setIsRecommended(String isRecommended) {
        this.isRecommended = isRecommended;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected DocumentModel(Parcel in) {
        if (in.readByte() == 0) {
            uId = null;
        } else {
            uId = in.readLong();
        }
        categoryId = in.readString();
        categoryName = in.readString();
        documentCode = in.readString();
        documentName = in.readString();
        documentNumber = in.readString();
        documentData = in.readString();
        toolTip = in.readString();
        version = in.readString();
        tags = in.createTypedArrayList(TagModel.CREATOR);
        approvedBy = in.readString();
        approvedDate = in.readString();
        date = in.readString();
        type = in.readString();
        folderid = in.readString();
        isRecommended = in.readString();
        catId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (uId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(uId);
        }
        dest.writeString(categoryId);
        dest.writeString(categoryName);
        dest.writeString(documentCode);
        dest.writeString(documentName);
        dest.writeString(documentNumber);
        dest.writeString(documentData);
        dest.writeString(toolTip);
        dest.writeString(version);
        dest.writeTypedList(tags);
        dest.writeString(approvedBy);
        dest.writeString(approvedDate);
        dest.writeString(date);
        dest.writeString(type);
        dest.writeString(folderid);
        dest.writeString(isRecommended);
        dest.writeString(catId);
    }

    public static final Creator<DocumentModel> CREATOR = new Creator<DocumentModel>() {
        @Override
        public DocumentModel createFromParcel(Parcel in) {
            return new DocumentModel(in);
        }

        @Override
        public DocumentModel[] newArray(int size) {
            return new DocumentModel[size];
        }
    };

}
