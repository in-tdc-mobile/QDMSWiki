package com.mariapps.qdmswiki.notification.view;

import com.mariapps.qdmswiki.home.model.ArticleModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;

public interface NotificationView {

    void onGetDocumentInfoSuccess(DocumentModel documentModel);
    void onGetDocumentInfoError();

    void onGetArticleInfoSuccess(ArticleModel articleModel);
    void onGetArticleInfoError();

}
