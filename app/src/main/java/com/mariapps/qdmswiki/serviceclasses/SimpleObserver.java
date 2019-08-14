package com.mariapps.qdmswiki.serviceclasses;


import java.net.UnknownHostException;

import rx.Observer;


/**
 * Created by elby.samson on 07,December,2018
 */

public class SimpleObserver<T> implements Observer<T> {


    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

        if (e instanceof UnknownHostException) {
            onNetworkFailure();
            return;
        }

        APIException apiException = new APIException(900, e.getMessage(), "Api Exception");

        if (e instanceof APIException) {
            apiException =
                    new APIException(((APIException) e).getCode(), ((APIException) e).getDescription(),
                            ((APIException) e).getInnerException());
        }

        onAPIError(apiException);
    }




    public void onAPIError(APIException e) {

    }

    public void onNetworkFailure() {

    }


    @Override
    public void onNext(T t) {

    }
}
