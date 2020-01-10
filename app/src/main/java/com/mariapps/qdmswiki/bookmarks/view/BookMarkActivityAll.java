package com.mariapps.qdmswiki.bookmarks.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.bookmarks.adapter.BookmarkAdapter;
import com.mariapps.qdmswiki.bookmarks.adapter.BookmarkAdapterAll;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkEntryModel;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.home.database.HomeDao;
import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.model.ArticleModel;
import com.mariapps.qdmswiki.home.model.CategoryModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.model.DownloadFilesResponseModel;
import com.mariapps.qdmswiki.home.presenter.HomePresenter;
import com.mariapps.qdmswiki.home.view.HomeActivity;
import com.mariapps.qdmswiki.home.view.HomeView;
import com.mariapps.qdmswiki.notification.model.NotificationModel;
import com.mariapps.qdmswiki.search.adapter.BreadCrumbAdapter;
import com.mariapps.qdmswiki.search.model.BreadCrumbItem;
import com.mariapps.qdmswiki.search.model.SearchModel;
import com.mariapps.qdmswiki.search.view.DocumentViewFragment;
import com.mariapps.qdmswiki.search.view.FolderStructureActivity;
import com.mariapps.qdmswiki.serviceclasses.APIException;
import com.mariapps.qdmswiki.usersettings.UserInfoModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

import static com.mariapps.qdmswiki.search.view.DocumentViewFragment.BOOKMARKID;

public  class BookMarkActivityAll extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    BookmarkAdapterAll adapterAll;
    LinearLayoutManager layoutManager;
    List<BookmarkEntryModel> list = new ArrayList<>();
    ProgressDialog progressDialog;
    CustomTextView headingTV;
    CustomTextView homeTV;
    CustomTextView nameTV;
    CustomTextView titleTV;
    ImageView backbtn;
    DocumentModel documentModel=null,documentModelbm;
    private HomePresenter homePresenter;
    private BreadCrumbAdapter breadCrumbAdapter;
    public static final String BOOKMARKID = "BOOKMARK_ID";
    List<BreadCrumbItem> breadCrumbItems = new ArrayList<>();
    List<BreadCrumbItem> allParents = new ArrayList<>();
    List<List<BreadCrumbItem>> allbreadcrumblist = new ArrayList<>();
    List<List<BreadCrumbItem>> allbreadcrumblist1 = new ArrayList<>();
    List<String> categorylist = new ArrayList<>();
    List<CategoryModel> categoryModelList = new ArrayList<>();
    CategoryModel categoryModel;
    int count =0;
    String catid="";
    String cid = "";
    List<String> heading = new ArrayList<>();
    List<String> headinglist = new ArrayList<>();
    HashMap<String,String> headlist= new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark_all);
        recyclerView=findViewById(R.id.bookmarkallrv);
        headingTV = findViewById(R.id.headingTV);
        homeTV = findViewById(R.id.homeTV);
        backbtn = findViewById(R.id.backBtn);
        nameTV = findViewById(R.id.nameTV);
        titleTV = findViewById(R.id.titleTV);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        adapterAll = new BookmarkAdapterAll(this,list,allbreadcrumblist);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterAll);
        initView();
        getBookMarkEntryList();
        adapterAll.setRowClickListener(bookmarkEntryModel ->{
            if(bookmarkEntryModel!=null)
            getdocdet(bookmarkEntryModel.getDocumentId(),bookmarkEntryModel.getBookmarkId());
                });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }



    public void getBookMarkEntryList() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                list.clear();
                list.addAll( HomeDatabase.getInstance(getApplicationContext()).homeDao().getBookmarkEntriesall());

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                progressDialog.show();
            }

            @Override
            public void onComplete() {
                Log.e("getBookMarkEntryList",list.size()+"");
                getdocdetcomlete();
                //adapterAll.notifyDataSetChanged();
               // progressDialog.dismiss();

            }
            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
              // adapterAll.notifyDataSetChanged();
                Log.e("getBookMarkEntryList",e.getLocalizedMessage());
            }
        });


    }

    public void getdocdetcomlete(){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                for (int i = 0; i < list.size(); i++) {
                    try {
                        categorylist.add(HomeDatabase.getInstance(getApplicationContext()).homeDao().getDocumentDetails(list.get(i).getDocumentId()).categoryId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                progressDialog.show();
            }
            @Override
            public void onComplete() {
           // getcatedetailscomlete();
                createbreadcrumb();
            }
            @Override
            public void onError(Throwable e) {
                Log.e("errorx",e.getLocalizedMessage());
                progressDialog.dismiss();
            }
        });
    }








    public void createbreadcrumb(){

      /*  for (int i = 0; i < categorylist.size(); i++) {
            getCategoryDetailsOfSelectedDocument(categorylist.get(i));
        }

        progressDialog.dismiss();*/


        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                categoryModelList = HomeDatabase.getInstance(getApplicationContext()).homeDao().getCategoryDetailsOfSelectedDocumentlist();
                Log.e("thisissize",categoryModelList.size()+"");
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
               // progressDialog.show();
            }
            //59e8704d3b76db5b78458f9b
            @Override
            public void onComplete() {
                for (int i = 0; i < categorylist.size(); i++) {
                    createlist(categorylist.get(i));
                }
                //createlist("59e8704d3b76db5b78458f9b");
                heading.size();
                headinglist.size();
            }
            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
            }
        });
    }

   /* public void createlist(String cid){
        List<String> heading = new ArrayList<>();
        List<String> headinglist = new ArrayList<>();
        HashMap<String,String> headlist= new HashMap<>();
        String headingall="";
        for (int i = 0; i < categorylist.size(); i++) {
            cid = categorylist.get(i);
            for (int i1 = 0; i1 < categoryModelList.size(); i1++) {
                CategoryModel cm = categoryModelList.get(i1);
                if (cid.equals(cm.getParent())) {
                    if (!heading.contains(cm.getName())) {
                        heading.add(cm.getName());
                      //  cid = cm.getParent();
                    }
                }
            }
            for (int i1 = 0; i1 < heading.size(); i1++) {
              headingall = headingall+"/"+heading.get(i1);
            }
            headinglist.add(headingall);
            headingall="";
        }
    }*/


    public void createlist(String cid){
        String headingall="";
        for (int i1 = 0; i1 < categoryModelList.size(); i1++) {
            CategoryModel cm = categoryModelList.get(i1);
            if (cid.equals(cm.getId())) {
                if (!heading.contains(cm.getName())) {
                    heading.add(cm.getName());
                    cid = cm.getParent();
                    for (int i2 = 0; i2 < heading.size(); i2++) {
                        headingall = headingall+"/"+heading.get(i2);
                    }
                    if(!headinglist.contains(headingall)){
                        headinglist.add(headingall);
                    }
                    headingall="";
                }
            }
        }
    }





    public void getdocdet(String docid,String bookid) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
           documentModel= HomeDatabase.getInstance(getApplicationContext()).homeDao().getDocumentData(docid)  ;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                progressDialog.show();
            }

            @Override
            public void onComplete() {
                progressDialog.dismiss();
                Log.e("getBookMarkEntryList",docid);
                if(documentModel!=null){
                    Intent intent = new Intent(BookMarkActivityAll.this, FolderStructureActivity.class);
                    intent.putExtra(AppConfig.BUNDLE_PAGE, "Document");
                    intent.putExtra(AppConfig.BUNDLE_TYPE, "Document");
                    intent.putExtra(AppConfig.BUNDLE_NAME, documentModel.getDocumentName());
                    intent.putExtra(AppConfig.BUNDLE_FOLDER_NAME, documentModel.getCategoryName());
                    intent.putExtra(AppConfig.BUNDLE_ID, documentModel.getId());
                    intent.putExtra(AppConfig.BUNDLE_FOLDER_ID, documentModel.getCategoryId());
                    intent.putExtra(AppConfig.BUNDLE_VERSION, documentModel.getVersion());
                    intent.putExtra(AppConfig.BOOKMARK_ID, bookid);
                    intent.putExtra(AppConfig.BOOKMARK_ALL,"yes");
                  //  setResult(DocumentViewFragment.RESULT_CODE, intent);
                    startActivity(intent);
                }

            }
            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
                Log.e("getBookMarkEntryList",e.getLocalizedMessage());

            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.headingTV:
                onBackPressed();
                break;
            case R.id.homeTV:
                Intent homeIntent = new Intent(BookMarkActivityAll.this, HomeActivity.class);
                startActivity(homeIntent);
                break;
        }
    }

    private void initView() {
        headingTV.setText(getString(R.string.string_bookmarks));
        //titleTV.setText(getString(R.string.string_bookmarks));
    }

    public void getCategoryDetailsOfSelectedDocument(String folderId) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                categoryModel = HomeDatabase.getInstance(getApplicationContext()).homeDao().getCategoryDetailsOfSelectedDocument(folderId);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
           List<BreadCrumbItem>  crumbs = new ArrayList<>();
                if (categoryModel == null) {
                    for (int i = allParents.size() - 1; i >= 0; i--) {
                        crumbs.add(allParents.get(i));
                    }
                    allbreadcrumblist.add(crumbs);
                   /* try {
                        Log.e("breadcrimgsize",breadCrumbItems.size()+"");
                        if(breadCrumbItems.size()>0){
                            for (int i = 0; i < breadCrumbItems.size(); i++) {
                                Log.e("breadcrimgsizeitems",breadCrumbItems.get(i).getHeading()+"");
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                    allParents.clear();
                } else {
                    allParents.add(new BreadCrumbItem(categoryModel.getName(), categoryModel.getId()));
                    getCategoryDetailsOfSelectedDocument(categoryModel.getParent());
                }

                Log.e("sizeofallcrumbs",allbreadcrumblist.size()+"");
            }
            @Override
            public void onError(Throwable e) {

            }
        });
    }


    public void getdocdet(String docid) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                documentModelbm = HomeDatabase.getInstance(getApplicationContext()).homeDao().getDocumentDetails(docid);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                if (documentModelbm != null) {
                    Log.e("documentModel", documentModelbm.categoryId + "  " + documentModelbm.documentNumber);
                    getCategoryDetailsOfSelectedDocument(documentModelbm.categoryId);
                }

            }

            @Override
            public void onError(Throwable e) {
                Log.e("getBookMarkEntryList", e.getLocalizedMessage());

            }
        });
    }


}
