package com.mariapps.qdmswiki.home.view;

import com.mariapps.qdmswiki.serviceclasses.APIException;

public interface HomeView {

    void onGetDownloadUrlSuccess(String url);
    void onGetDownloadUrlError(APIException e);
}
