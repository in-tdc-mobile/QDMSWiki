package com.mariapps.qdmswiki.serviceclasses;


import java.io.IOException;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by aruna.ramakrishnan on 14-08-2019
 */

public class RXResponseTransformer<T> implements Observable.Transformer<Response<T>, T> {

    @Override
    public Observable<T> call(Observable<Response<T>> responseObservable) {
        return responseObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Response<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(Response<T> response) {
                        Timber.d("RXResponseTransformer" + response.code() + "");
                        switch (response.code()) {
                            case 200:
                                return Observable.just(response.body());
                            case 400:
                                try {
                                    if (response.errorBody() != null) {
                                        return Observable.error(new APIException(response.code(), response.errorBody().string(), null));
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            case 208:
                                try {
                                    if (response.errorBody() != null) {
                                        return Observable.error(new APIException(response.code(), response.errorBody().string(), null));
                                    } else if (response.body() != null) {
                                        return Observable.just(response.body());
                                    } else {
                                        return Observable.error(new APIException(response.code(), "Internal Server Error", null));
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            case 422:
                                try {
                                    return Observable.error(new APIException(response.code(), response.errorBody().string(), null));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            case 307:
                                try {
                                    return Observable.error(new APIException(response.code(), response.errorBody().string(), null));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            case 403:
                                try {
                                    return Observable.error(new APIException(response.code(), response.errorBody().string(), null));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            case 401:
                                try {
                                    return Observable.error(new APIException(response.code(), response.errorBody().string(), null));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            case 500:
                                return Observable.error(new APIException(response.code(), "Internal Server Error", null));
                            default:
                                return Observable.error(new Exception("unknown error"));
                        }
                    }
                });

    }
}