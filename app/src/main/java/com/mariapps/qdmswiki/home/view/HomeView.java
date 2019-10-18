package com.mariapps.qdmswiki.home.view;

import com.mariapps.qdmswiki.home.model.ArticleModel;
import com.mariapps.qdmswiki.home.model.CategoryModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.notification.model.NotificationModel;
import com.mariapps.qdmswiki.search.model.SearchModel;
import com.mariapps.qdmswiki.serviceclasses.APIException;
import com.mariapps.qdmswiki.usersettings.UserInfoModel;

import java.util.List;

public interface HomeView {

    void onGetDownloadUrlSuccess(String url);
    void onGetDownloadUrlError(APIException e);

    void onGetParentFolderSuccess(List<DocumentModel> documentModels);
    void onGetChildFoldersList(List<DocumentModel> documentModels);

    void onGetUserImageSuccess(UserInfoModel userInfoModel);
    void onGetUserImageError();

    void onGetCategoryDetailsSuccess(CategoryModel categoryModel);
    void onGetCategoryDetailsError();

    void onInsertCategoryDetailsSuccess();
    void onInsertCategoryDetailsError();

    void onGetDocumentInfoSuccess(DocumentModel documentModel);
    void onGetDocumentInfoError();

    void onGetNotificationCountSuccess(List<NotificationModel> notificationList);
    void onGetNotificationCountError();

    void onInsertFileListSuccess();
    void onInsertFileListError();

    void onInsertFormSuccess();
    void onInsertFormError();

    void onInsertImageSuccess();
    void onInsertImageError();

}
