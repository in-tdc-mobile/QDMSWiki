package com.mariapps.qdmswiki.home.view;

import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.serviceclasses.APIException;

import java.util.List;

public interface HomeView {

    void onGetDownloadUrlSuccess(String url);
    void onGetDownloadUrlError(APIException e);

    void onGetParentFolderSuccess(List<DocumentModel> documentModels);
    void onGetChildFoldersList(List<DocumentModel> documentModels);
}
