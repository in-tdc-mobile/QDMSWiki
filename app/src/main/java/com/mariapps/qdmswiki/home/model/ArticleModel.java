package com.mariapps.qdmswiki.home.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.nfc.Tag;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkEntryModel;
import com.mariapps.qdmswiki.home.database.HomeTypeConverter;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "ArticleEntity")
@TypeConverters(HomeTypeConverter.class)
public class ArticleModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public Long uId;

    @ColumnInfo(name = "Id")
    @SerializedName("_id")
    private String id;

//    @ColumnInfo(name = "AppId")
//    @SerializedName("appId")
//    private String appId;

//    @ColumnInfo(name = "DraftId")
//    @SerializedName("DraftId")
//    private String draftId;

    @ColumnInfo(name = "ArticleName")
    @SerializedName("ArticleName")
    private String articleName;

    @ColumnInfo(name = "ArticleNumber")
    @SerializedName("ArticleNumber")
    private Integer articleNumber;

    @SerializedName("DocumentData")
    public String documentData;

    @SerializedName("CategoryId")
    public List<String> categoryIds;

    @SerializedName("CategoryNames")
    public List<String> categoryNames;

//    @ColumnInfo(name = "IsActive")
//    @SerializedName("IsActive")
//    private boolean isActive;

    @SerializedName("Tags")
    private List<TagModel> tags;

    @ColumnInfo(name = "Version")
    @SerializedName("Version")
    private Double version;

    @ColumnInfo(name = "ApprovedBy")
    @SerializedName("ApprovedBy")
    private String approvedBy;

    @ColumnInfo(name = "ApprovedDate")
    @SerializedName("ApprovedDate")
    private String approvedDate;

    @ColumnInfo(name = "Date")
    @SerializedName("Date")
    private String date;

    @ColumnInfo(name = "ArticleToOfficeIds")
    @SerializedName("ArticleToOfficeIds")
    private List<String> articleToOfficeIds = null;

    @ColumnInfo(name = "ArticleToVesselIds")
    @SerializedName("ArticleToVesselIds")
    private List<String> articleToVesselIds = null;

    public ArticleModel(String id, String articleName, String documentData, List<String> categoryIds) {
        this.id = id;
//        this.appId = appId;
//        this.draftId = draftId;
        this.articleName = articleName;
        this.documentData = documentData;
        this.categoryIds = categoryIds;
//        this.isActive = isActive;
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

//    public String getAppId() {
//        return appId;
//    }
//
//    public void setAppId(String appId) {
//        this.appId = appId;
//    }
//
//    public String getDraftId() {
//        return draftId;
//    }
//
//    public void setDraftId(String draftId) {
//        this.draftId = draftId;
//    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getDocumentData() {
        return documentData;
    }

    public void setDocumentData(String documentData) {
        this.documentData = documentData;
    }

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(ArrayList<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

//    public boolean isActive() {
//        return isActive;
//    }
//
//    public void setActive(boolean active) {
//        isActive = active;
//    }

    public Integer getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(Integer articleNumber) {
        this.articleNumber = articleNumber;
    }

    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<TagModel> getTags() {
        return tags;
    }

    public void setTags(List<TagModel> tags) {
        this.tags = tags;
    }

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
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

    public List<String> getArticleToOfficeIds() {
        return articleToOfficeIds;
    }

    public void setArticleToOfficeIds(List<String> articleToOfficeIds) {
        this.articleToOfficeIds = articleToOfficeIds;
    }

    public List<String> getArticleToVesselIds() {
        return articleToVesselIds;
    }

    public void setArticleToVesselIds(List<String> articleToVesselIds) {
        this.articleToVesselIds = articleToVesselIds;
    }

    public List<String> getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(List<String> categoryNames) {
        this.categoryNames = categoryNames;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected ArticleModel(Parcel in) {
        id = in.readString();
//        appId = in.readString();
//        draftId = in.readString();
        articleName = in.readString();
        articleNumber = in.readInt();
     //   documentData = in.readString();
        categoryIds = in.readArrayList(ArticleModel.class.getClassLoader());
        tags = in.createTypedArrayList(TagModel.CREATOR);
        version = in.readDouble();
        approvedBy = in.readString();
        approvedDate = in.readString();
        date = in.readString();
        articleToOfficeIds = in.readArrayList(ArticleModel.class.getClassLoader());
        articleToVesselIds = in.readArrayList(ArticleModel.class.getClassLoader());
        categoryNames = in.readArrayList(ArticleModel.class.getClassLoader());
        //isActive = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
//        dest.writeString(appId);
//        dest.writeString(draftId);
        dest.writeString(articleName);
        dest.writeInt(articleNumber);
       // dest.writeString(documentData);
        dest.writeList(categoryIds);
        dest.writeTypedList(tags);
        dest.writeDouble(version);
        dest.writeString(approvedBy);
        dest.writeString(approvedDate);
        dest.writeString(date);
        dest.writeList(articleToOfficeIds);
        dest.writeList(articleToVesselIds);
        dest.writeList(categoryNames);
        //dest.writeByte((byte) (isActive ? 1 : 0));
    }

    public static final Creator<ArticleModel> CREATOR = new Creator<ArticleModel>() {
        @Override
        public ArticleModel createFromParcel(Parcel in) {
            return new ArticleModel(in);
        }

        @Override
        public ArticleModel[] newArray(int size) {
            return new ArticleModel[size];
        }
    };

}
