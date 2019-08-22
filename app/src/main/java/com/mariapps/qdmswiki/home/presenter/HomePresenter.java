package com.mariapps.qdmswiki.home.presenter;

import android.content.Context;

import com.mariapps.qdmswiki.bookmarks.model.BookmarkEntryModel;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkModel;
import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.model.ArticleModel;
import com.mariapps.qdmswiki.home.model.CategoryModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.model.TagModel;
import com.mariapps.qdmswiki.home.view.HomeView;
import com.mariapps.qdmswiki.notification.model.NotificationModel;
import com.mariapps.qdmswiki.notification.model.ReceiverModel;
import com.mariapps.qdmswiki.serviceclasses.APIException;
import com.mariapps.qdmswiki.serviceclasses.ApiServiceFactory;
import com.mariapps.qdmswiki.serviceclasses.ServiceController;
import com.mariapps.qdmswiki.serviceclasses.SimpleObserver;
import com.mariapps.qdmswiki.usersettings.UserInfoModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsCategoryModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsTagModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter {

    private HomeView homeView;
    private HomeDatabase homeDatabase;
    private ServiceController serviceController;
    String url;
    List<DocumentModel> folderList = new ArrayList<>();

    public HomePresenter(Context context, HomeView homeView) {
        this.homeView = homeView;
        serviceController = ApiServiceFactory.getInstance().getFacade();
        homeDatabase = HomeDatabase.getInstance(context);
        url = "http://pal4-demo-app.westeurope.cloudapp.azure.com:8099/file/Extract1.zip";
        //url = "https://drive.google.com/uc?export=download&id=1pxjQIdxMLFJmg2bg4Y4sVkk6bFHIWihr";
    }

    public String getDownloadUrl() {
        return url;
//        serviceController.getDownloadUrl()
//                .subscribe(new SimpleObserver<String>() {
//
//
//                    @Override
//                    public void onNext(String downloadUrl) {
//                        super.onNext(downloadUrl);
//                        homeView.onGetDownloadUrlSuccess(downloadUrl);
//                    }
//
//                    @Override
//                    public void onNetworkFailure() {
//                        super.onNetworkFailure();
//                    }
//
//                    @Override
//                    public void onAPIError(APIException e) {
//                        super.onAPIError(e);
//                        homeView.onGetDownloadUrlError(e);
//                    }
//                });
    }

    public void deleteDocuments(List<DocumentModel> documentModels) {

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteDocumentEntity();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                insertDocuments(documentModels);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void insertDocuments(final List<DocumentModel> documentModels) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (documentModels != null) {
                    homeDatabase.homeDao().insertDocument(documentModels);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {

            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void deleteTags(List<TagModel> tagModelArrayList) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteTagEntity();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                insertTags(tagModelArrayList);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void insertTags(final List<TagModel> tagModelArrayList) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (tagModelArrayList != null) {
                    homeDatabase.homeDao().insertTag(tagModelArrayList);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void deleteArticles(List<ArticleModel> articleModelArrayList) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteArticleEntity();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                insertArticles(articleModelArrayList);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void insertArticles(final List<ArticleModel> articleModelArrayList) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (articleModelArrayList != null) {
                    homeDatabase.homeDao().insertArticle(articleModelArrayList);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void deleteCategories(List<CategoryModel> categoryModelArrayList) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteCategoryEntity();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                insertCategories(categoryModelArrayList);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void insertCategories(final List<CategoryModel> categoryModelArrayList) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (categoryModelArrayList != null) {
                    homeDatabase.homeDao().insertCategory(categoryModelArrayList);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void deleteNotifications(List<NotificationModel> notificationModels) {

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteNotificationEntity();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                insertNotifications(notificationModels);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void insertNotifications(final List<NotificationModel> notificationModels) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (notificationModels != null) {
                    homeDatabase.homeDao().insertNotifications(notificationModels);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void deleteReceivers(List<ReceiverModel> receiverModels) {

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteReceiverEntity();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                insertReceivers(receiverModels);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void insertReceivers(final List<ReceiverModel> receiverModels) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (receiverModels != null) {
                    homeDatabase.homeDao().insertReceivers(receiverModels);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void deleteBookmarks(List<BookmarkModel> bookmarkModels) {

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteBookmarkEntity();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                insertBookmarks(bookmarkModels);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void insertBookmarks(final List<BookmarkModel> bookmarkModels) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (bookmarkModels != null) {
                    homeDatabase.homeDao().insertBookmarks(bookmarkModels);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void deleteBookmarkEntries(List<BookmarkEntryModel> bookmarkEntryModels) {

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteBookmarkEntryEntity();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                insertBookmarkEntries(bookmarkEntryModels);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void insertBookmarkEntries(final List<BookmarkEntryModel> bookmarkEntryModels) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (bookmarkEntryModels != null) {
                    homeDatabase.homeDao().insertBookmarkEntries(bookmarkEntryModels);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void deleteUserSettings(UserSettingsModel userSettingsModels) {

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteUserSettingsEntity();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                insertUserSettings(userSettingsModels);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void insertUserSettings(final UserSettingsModel userSettingsModels) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (userSettingsModels != null) {
                    homeDatabase.homeDao().insertUserSettings(userSettingsModels);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void deleteUserSettingsTag(List<UserSettingsTagModel> userSettingsTagModel) {

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteUserSettingsTagEntity();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                insertUserSettingsTag(userSettingsTagModel);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void insertUserSettingsTag(final List<UserSettingsTagModel> userSettingsTagModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (userSettingsTagModel != null) {
                    homeDatabase.homeDao().insertUserSettingsTag(userSettingsTagModel);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void deleteUserSettingsCategory(List<UserSettingsCategoryModel> userSettingsCategoryModel) {

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteUserSettingsCategoryEntity();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                insertUserSettingsCategory(userSettingsCategoryModel);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void insertUserSettingsCategory(final List<UserSettingsCategoryModel> userSettingsCategoryModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (userSettingsCategoryModel != null) {
                    homeDatabase.homeDao().insertUserSettingsCategory(userSettingsCategoryModel);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void deleteUserInfo(List<UserInfoModel> userInfoModel) {

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteUserInfoEntity();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                insertUserInfo(userInfoModel);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void insertUserInfo(final List<UserInfoModel> userInfoModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (userInfoModel != null) {
                    homeDatabase.homeDao().insertUserInfo(userInfoModel);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }


    public void getParentFolders() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                    folderList = homeDatabase.homeDao().getParentFolders();

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                homeView.onGetParentFolderSuccess(folderList);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void getChildFoldersList(String parentId) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                folderList = homeDatabase.homeDao().getChildFoldersList(parentId);

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                homeView.onGetChildFoldersList(folderList);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

}
