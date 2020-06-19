
package com.mariapps.qdmswiki;

import java.util.List;



public class SendAllIdModel {

    private String appVersion;
    private List<ArticleIdList> articleIdList;
    private String deviceName;
    private String deviceType;
    private List<DocumentsIdList> documentsIdList;
    private List<FileChunkIdList> fileChunkIdList;
    private List<ImageNameList> imageNameList;
    private String userId;

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public void setArticleIdList(List<ArticleIdList> articleIdList) {
        this.articleIdList = articleIdList;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public void setDocumentsIdList(List<DocumentsIdList> documentsIdList) {
        this.documentsIdList = documentsIdList;
    }

    public void setFileChunkIdList(List<FileChunkIdList> fileChunkIdList) {
        this.fileChunkIdList = fileChunkIdList;
    }

    public void setImageNameList(List<ImageNameList> imageNameList) {
        this.imageNameList = imageNameList;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static class DocumentsIdList {

        private String id;

        public DocumentsIdList(String id) {
            this.id = id;
        }
    }

    public static class ArticleIdList {

        private String id;

        public ArticleIdList(String id) {
            this.id = id;
        }
    }

    public static class ImageNameList {

        private String name;

        public ImageNameList(String name) {
            this.name = name;
        }
    }

    public static class FileChunkIdList {

        private String id;

        public FileChunkIdList(String id) {
            this.id = id;
        }
    }

}


