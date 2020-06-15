package com.mariapps.qdmswiki.home.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.home.database.HomeTypeConverter;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "DocumentEntity")
@TypeConverters(HomeTypeConverter.class)
public class DocumentModel implements Parcelable {


    @PrimaryKey
    @NotNull
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

    @SerializedName("ApprovedName")
    private String approvedName;

    @ColumnInfo(name = "OfficeIds")
    @SerializedName("OfficeIds")
    public List<String> officeIds;

    @ColumnInfo(name = "VesselIds")
    @SerializedName("VesselIds")
    public List<String> vesselIds;

    @ColumnInfo(name = "PassengersVesselIds")
    @SerializedName("PassengersVesselIds")
    public List<String> passengersVesselIds;

    public DocumentModel(@NotNull String id, String categoryId, String documentCode, String documentName/*, String documentData,*/, String version,List<TagModel> tags) {
        this.id = id;
        this.categoryId = categoryId;
        this.documentCode = documentCode;
        this.documentName = documentName;
     //   this.documentData = documentData;
        this.version = version;
        this.tags = tags;
    }

    public String getId() {
        if(id==null){
            id="";
        }
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

    public String getApprovedName() {
        return approvedName;
    }

    public void setApprovedName(String approvedName) {
        this.approvedName = approvedName;
    }


    public List<String> getOfficeIds() {
        return officeIds;
    }

    public void setOfficeIds(List<String> officeIds) {
        this.officeIds = officeIds;
    }

    public List<String> getVesselIds() {
        return vesselIds;
    }

    public void setVesselIds(List<String> vesselIds) {
        this.vesselIds = vesselIds;
    }

    public List<String> getPassengersVesselIds() {
        return passengersVesselIds;
    }

    public void setPassengersVesselIds(List<String> passengersVesselIds) {
        this.passengersVesselIds = passengersVesselIds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected DocumentModel(Parcel in) {
        categoryId = in.readString();
        categoryName = in.readString();
        documentCode = in.readString();
        documentName = in.readString();
        documentNumber = in.readString();
       // documentData = in.readString();
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
        approvedName = in.readString();
        officeIds = in.readArrayList(ArticleModel.class.getClassLoader());
        vesselIds = in.readArrayList(ArticleModel.class.getClassLoader());
        passengersVesselIds = in.readArrayList(ArticleModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(categoryId);
        dest.writeString(categoryName);
        dest.writeString(documentCode);
        dest.writeString(documentName);
        dest.writeString(documentNumber);
       // dest.writeString(documentData);
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
        dest.writeString(approvedName);
        dest.writeList(officeIds);
        dest.writeList(vesselIds);
        dest.writeList(passengersVesselIds);
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