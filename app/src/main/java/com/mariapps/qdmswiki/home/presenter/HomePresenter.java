package com.mariapps.qdmswiki.home.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mariapps.qdmswiki.ArticleModelObj;
import com.mariapps.qdmswiki.ArticleModelObj_;
import com.mariapps.qdmswiki.DocumentModelObj;
import com.mariapps.qdmswiki.DocumentModelObj_;
import com.mariapps.qdmswiki.ObjectBox;
import com.mariapps.qdmswiki.TagModelObj;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkEntryModel;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkModel;
import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.model.ArticleModel;
import com.mariapps.qdmswiki.home.model.CategoryModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.model.DownloadFilesRequestModel;
import com.mariapps.qdmswiki.home.model.DownloadFilesResponseModel;
import com.mariapps.qdmswiki.home.model.FileBlobListModel;
import com.mariapps.qdmswiki.home.model.FileListModel;
import com.mariapps.qdmswiki.home.model.FormsModel;
import com.mariapps.qdmswiki.home.model.ImageModel;
import com.mariapps.qdmswiki.home.model.TagModel;
import com.mariapps.qdmswiki.home.view.HomeView;
import com.mariapps.qdmswiki.notification.model.NotificationModel;
import com.mariapps.qdmswiki.notification.model.ReceiverModel;
import com.mariapps.qdmswiki.search.model.SearchModel;
import com.mariapps.qdmswiki.serviceclasses.APIException;
import com.mariapps.qdmswiki.serviceclasses.ApiServiceFactory;
import com.mariapps.qdmswiki.serviceclasses.ServiceController;
import com.mariapps.qdmswiki.serviceclasses.SimpleObserver;
import com.mariapps.qdmswiki.usersettings.UserInfoModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsCategoryModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsTagModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.objectbox.Box;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter {
    private HomeView homeView;
    private HomeDatabase homeDatabase;
    private ServiceController serviceController;
    String url;
    List<NotificationModel> notificationList;
    List<DocumentModel> folderList = new ArrayList<>();
    List<SearchModel> childList = new ArrayList<>();
    UserInfoModel userInfoModel;
    CategoryModel categoryModel;
    DocumentModel documentModel;
    ArticleModel articleModel;
    String categoryName;
    Box<ArticleModelObj> abox;
    Box<DocumentModelObj> dbox;
   // CompositeDisposable maindisposable;

    public HomePresenter(Context context, HomeView homeView) {
        this.homeView = homeView;
        serviceController = ApiServiceFactory.getInstance().getFacade();
        homeDatabase = HomeDatabase.getInstance(context);
        //url = "http://10.201.1.19:8899/QDMSMobileService/api/Home/DownloadFile";
        //url = "http://pal4-demo-app.westeurope.cloudapp.azure.com:8099/file/Extract1.zip";
        //url = "https://qdmswiki2019.blob.core.windows.net/sqldata/09102019124722.zip";//700mb
        //url = "https://qdmswiki2019.blob.core.windows.net/baseversions/20191015.zip";//new 700 mb
        //url = "https://qdmswiki2019.blob.core.windows.net/sqldata/09102019072523.zip";//90 mb
        url = "https://qdmswiki2019.blob.core.windows.net/updateversions/20191022100905.zip";//18 mb

    }

    public void getDownloadUrl(DownloadFilesRequestModel downloadFilesRequestModel) {
        serviceController.getUrls(downloadFilesRequestModel)
                .subscribe(new SimpleObserver<DownloadFilesResponseModel>() {
                    @Override
                    public void onNext(DownloadFilesResponseModel downloadFilesResponseModel) {
                        super.onNext(downloadFilesResponseModel);
                        homeView.onGetDownloadFilesSuccess(downloadFilesResponseModel);
                    }
                    @Override
                    public void onNetworkFailure() {
                        super.onNetworkFailure();
                    }

                    @Override
                    public void onAPIError(APIException e) {
                        super.onAPIError(e);
                        homeView.onGetDownloadFilesError();
                    }
                });
    }

    public void deleteDocument(final DocumentModel documentModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (documentModel != null) {
                    homeDatabase.homeDao().deleteDocument(documentModel.getId());
                    try {
                        dbox.query().equal(DocumentModelObj_.id,documentModel.getId()).build().remove();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //  dbox.query().equal(DocumentModelObj_.id,documentModel.getId()).build().remove();
                   /* AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                dbox.query().equal(DocumentModelObj_.id,documentModel.getId()).build().remove();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });*/
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
              //  maindisposable.add(d);
            }
            @Override
            public void onComplete() {
                Log.e("onCompletedocdelete",documentModel.documentName+"  "+documentModel.id);
                insertDocuments(documentModel);
            }
            @Override
            public void onError(Throwable e) {
                Log.e("docdeleteerror",e.getLocalizedMessage());
            }
        });
    }


    public void insertDocuments(final DocumentModel documentModel) {
        if(dbox==null){
            dbox = ObjectBox.get().boxFor(DocumentModelObj.class);
        }
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (documentModel != null) {
                    homeDatabase.homeDao().insertDocument(documentModel);
                    try {
                        dbox.put(new DocumentModelObj(documentModel.id,documentModel.documentName,documentModel.documentData));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                   /* AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            dbox.put(new DocumentModelObj(documentModel.id,documentModel.documentName,documentModel.documentData));
                        }
                    });*/
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
               // maindisposable.add(d);
            }

            @Override
            public void onComplete() {
               // maindisposable.clear();
                Log.e("onCompletedocinsert",documentModel.documentName+"  "+documentModel.id);
            }
            @Override
            public void onError(Throwable e) {
               // maindisposable.clear();
            Log.e("errorininsert",e.getLocalizedMessage());
            }
        });
    }





    public void insertTags(final List<TagModel> tagList) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (tagList != null) {
                    homeDatabase.homeDao().insertTag(tagList);
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

    public void deleteArticles(ArticleModel articleModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteArticle(articleModel.getId());
                try {
                    abox.query().equal(ArticleModelObj_.id,articleModel.getId()).build().remove();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /*AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            abox.query().equal(ArticleModelObj_.id,documentModel.getId()).build().remove();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });*/

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
               // maindisposable.add(d);

            }

            @Override
            public void onComplete() {
                Log.e("onCompletearticeldelete",articleModel.getArticleName()+"  "+articleModel.getId());
                insertArticles(articleModel);
            }


            @Override
            public void onError(Throwable e) {
                Log.e("onCompletearticeldelerr",e.getLocalizedMessage());

            }
        });
    }

    public void insertArticles(final ArticleModel articleModel) {
        if(abox==null){
            abox = ObjectBox.get().boxFor(ArticleModelObj.class);
        }
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (articleModel != null) {
                    homeDatabase.homeDao().insertArticle(articleModel);
                    try {
                        abox.put(new ArticleModelObj(articleModel.getId(),articleModel.getArticleName(),articleModel.documentData));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                   /* AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("insertedarticelids",articleModel.getId());
                           abox.put(new ArticleModelObj(articleModel.getId(),articleModel.getArticleName(),articleModel.documentData));
                        }
                    });*/
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            //    maindisposable.add(d);

            }

            @Override
            public void onComplete() {
              //  maindisposable.clear();
                Log.e("onCompletearticelinsert",articleModel.getArticleName()+"  "+articleModel.getId());
            }
            @Override
            public void onError(Throwable e) {
            //    maindisposable.clear();
                Log.e("articleerrorininsert",e.getLocalizedMessage());
            }
        });
    }

    public void deleteCategory(CategoryModel categoryModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteCategory(categoryModel.getId());
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                insertCategories(categoryModel);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void insertCategories(final CategoryModel categoryModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (categoryModel != null) {
                    homeDatabase.homeDao().insertCategory(categoryModel);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                homeView.onInsertCategoryDetailsSuccess();
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void deleteNotification(NotificationModel notificationModel) {

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteNotification(notificationModel.getId());
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                insertNotification(notificationModel);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void insertNotification(final NotificationModel notificationModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (notificationModel != null) {
                    homeDatabase.homeDao().insertNotification(notificationModel);
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

    public void deleteReceivers(String notificationId, final ReceiverModel receiverModels) {

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteReceiver(notificationId);
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

    public void insertReceivers(final ReceiverModel receiverModels) {
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

    public void deleteBookmark(BookmarkModel bookmarkModel) {

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteBookmark(bookmarkModel.getId());
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                insertBookmark(bookmarkModel);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void insertBookmark(final BookmarkModel bookmarkModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (bookmarkModel != null) {
                    homeDatabase.homeDao().insertBookmark(bookmarkModel);
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

    public void deleteBookmarkEntries(BookmarkEntryModel bookmarkEntryModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteBookmarkEntrybyid(bookmarkEntryModel.getBookmarkId());
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onComplete(){
                insertBookmarkEntries(bookmarkEntryModel);
            }
            @Override
            public void onError(Throwable e) {
            }
        });
    }


















    public void insertBookmarkEntries(BookmarkEntryModel bookmarkEntryModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (bookmarkEntryModel != null) {
                    homeDatabase.homeDao().insertBookmarkEntriessingle(bookmarkEntryModel);
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

    public void deleteUserInfo(final UserInfoModel userInfoModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (userInfoModel != null) {
                    homeDatabase.homeDao().deleteUserInfoEntity(userInfoModel.getId());
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                insertUserInfo(userInfoModel);
            }

            @Override
            public void onComplete() {
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void insertUserInfo(final UserInfoModel userInfoModel) {
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
                childList = homeDatabase.homeDao().getAllDocumentsInsideFolder(parentId);
                for(int i=0;i<childList.size();i++){
                    if(childList.get(i).getType().equals("Article")){
                        List<String> categoryIds = Collections.singletonList(childList.get(i).getCategoryId().substring(1, childList.get(i).getCategoryId().length() - 1));
                        childList.get(i).setCategoryName(homeDatabase.homeDao().getCategoryName(categoryIds.get(0).replace("\"","")));
                    }
                }

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                homeView.onGetChildFoldersList(childList);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void getUserImage(String userId) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                userInfoModel = homeDatabase.homeDao().getUserImage(userId);

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                homeView.onGetUserImageSuccess(userInfoModel);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void getCategoryDetailsOfSelectedDocument(String folderId) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                categoryModel = homeDatabase.homeDao().getCategoryDetailsOfSelectedDocument(folderId);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete()
            {
                homeView.onGetCategoryDetailsSuccess(categoryModel);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void updateIsRecommended(String documentId) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().updateIsRecommended(documentId);

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

    public void getDocumentInfo(String documentId) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                documentModel = homeDatabase.homeDao().getDocumentInfo(documentId);

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                homeView.onGetDocumentInfoSuccess(documentModel);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void getArticleInfo(String articleId) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                articleModel = homeDatabase.homeDao().getArticleInfo(articleId);

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                homeView.onGetArticleInfoSuccess(articleModel);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void getNotificationCount() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                notificationList = homeDatabase.homeDao().getNotificationCount();

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                homeView.onGetNotificationCountSuccess(notificationList);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void deleteFile(FileListModel fileListModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deletefileListModel(fileListModel.getId());
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                insertFileListModel(fileListModel);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void insertFileListModel(final FileListModel fileListModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (fileListModel != null) {
                    homeDatabase.homeDao().insertFileListModel(fileListModel);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                homeView.onInsertFileListSuccess();
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }


    public void deleteForm(FormsModel formsModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteForm(formsModel.getId());
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                insertForm(formsModel);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void insertForm(final FormsModel formsModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (formsModel != null) {
                    homeDatabase.homeDao().insertForm(formsModel);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                homeView.onInsertFormSuccess();
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }


    public void deleteImage(ImageModel imageModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                homeDatabase.homeDao().deleteImage(imageModel.getId());
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                insertImage(imageModel);
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void insertImage(final ImageModel imageModel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (imageModel != null) {
                    homeDatabase.homeDao().insertImage(imageModel);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                homeView.onInsertImageSuccess();
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

//    public void getCategoryName(String categoryId) {
//        Completable.fromAction(new Action() {
//            @Override
//            public void run() throws Exception {
//                if (categoryId != null) {
//                    categoryName = homeDatabase.homeDao().getCategoryName(categoryId);
//                }
//            }
//        }).observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onComplete() {
//                homeView.onGetCategoryNameSuccess(categoryName);
//            }
//
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//        });
//    }

}
