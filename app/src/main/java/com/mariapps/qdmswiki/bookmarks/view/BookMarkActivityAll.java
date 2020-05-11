package com.mariapps.qdmswiki.bookmarks.view;

import android.app.ProgressDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.bookmark.presenter.BookmarkPresenter;
import com.mariapps.qdmswiki.bookmarks.adapter.BookmarkAdapter;
import com.mariapps.qdmswiki.bookmarks.adapter.BookmarkAdapterAll;
import com.mariapps.qdmswiki.bookmarks.adapter.BookmarkAdapterAllFaded;
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
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.schedulers.Schedulers;

import static com.mariapps.qdmswiki.search.view.DocumentViewFragment.BOOKMARKID;

public class BookMarkActivityAll extends AppCompatActivity implements View.OnClickListener, BookmarkView {
    RecyclerView recyclerView;
   // BookmarkAdapterAll adapterAll;
    BookmarkAdapterAllFaded adapterAll;
    LinearLayoutManager layoutManager;
    List<BookmarkEntryModel> list = new ArrayList<>();
    ProgressDialog progressDialog;
    CustomTextView headingTV;
    CustomTextView homeTV;
    CustomTextView nameTV;
    CustomTextView titleTV;
    ImageView backbtn;
    DocumentModel documentModel = null, documentModelbm;
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
    String catid = "";
    String cid = "";
    List<String> heading = new ArrayList<>();
    List<String> headinglist = new ArrayList<>();
    HashMap<String, String> headlist = new HashMap<>();
    private int deletePosition;
    BottomSheetFragment sheetFragment = new BottomSheetFragment();
    BookmarkPresenter bookmarkPresenter;
    BookmarkEntryModel model;
    MutableLiveData<Integer> mutableLiveData = new MutableLiveData<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark_all);
        bookmarkPresenter = new BookmarkPresenter(this, this);
        recyclerView = findViewById(R.id.bookmarkallrv);
        headingTV = findViewById(R.id.headingTV);
        homeTV = findViewById(R.id.homeTV);
        backbtn = findViewById(R.id.backBtn);
        nameTV = findViewById(R.id.nameTV);
        titleTV = findViewById(R.id.titleTV);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading");
        adapterAll = new BookmarkAdapterAllFaded(this, list, allbreadcrumblist);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        initView();
        getBookMarkEntryList();
        adapterAll.setRowClickListener(bookmarkEntryModel -> {
            if (bookmarkEntryModel != null)
                getdocdet(bookmarkEntryModel.getDocumentId(), bookmarkEntryModel.getBookmarkId());
        });

     /*   adapterAll.setRowLongClickListener(new BookmarkAdapterAll.RowLongClickListner() {
            @Override
            public void onItemClicked(BookmarkEntryModel bookmarkEntryModel, int position) {
                model = bookmarkEntryModel;
                deletePosition = position;
                sheetFragment.show(getSupportFragmentManager(), "bottomsheet");
            }
        });*/
        adapterAll.setRowLongClickListener(new BookmarkAdapterAllFaded.RowLongClickListner() {
            @Override
            public void onItemClicked(BookmarkEntryModel bookmarkEntryModel, int position) {
                model = bookmarkEntryModel;
                deletePosition = position;
                sheetFragment.show(getSupportFragmentManager(), "bottomsheet");
            }
        });

        sheetFragment.setbuttonclickedlistner(new BottomSheetFragment.buttonclicked() {
            @Override
            public void clickedbuttons(int pos) {
                if (pos == 1) {
                    bookmarkPresenter.deleteBookmark(model.getDocumentId(), model.getBookmarkId());
                    sheetFragment.dismiss();
                    getBookMarkEntryList();
                    Toast.makeText(BookMarkActivityAll.this, "Bookmark Removed", Toast.LENGTH_SHORT).show();

                } else {
                    sheetFragment.dismiss();
                }
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mutableLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                integer++;
                getallnew(integer);
                if (integer == list.size()) {
                    progressDialog.dismiss();
                    Log.e("sizeofallbreadcrumb",allbreadcrumblist.size()+"");
                    recyclerView.setAdapter(adapterAll);
                }
            }
        });
    }


    public void getBookMarkEntryList() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                list.clear();
                list.addAll(HomeDatabase.getInstance(getApplicationContext()).homeDao().getBookmarkEntriesall());
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
                Log.e("getBookMarkEntryList", list.size() + "");
                getallnew(0);
                //adapterAll.notifyDataSetChanged();
                // progressDialog.dismiss();
            }
            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
                // adapterAll.notifyDataSetChanged();
                Log.e("getBookMarkEntryList", e.getLocalizedMessage());
            }
        });
    }

    public void getallnew(int count) {
        if (list.size() > count)
            getdocdet(list.get(count).getDocumentId(), count);
    }


    public void getdocdet(String docid, String bookid) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                documentModel = HomeDatabase.getInstance(getApplicationContext()).homeDao().getDocumentDetails(docid);
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
                Log.e("getBookMarkEntryList", docid);
                if (documentModel != null) {
                    Intent intent = new Intent(BookMarkActivityAll.this, FolderStructureActivity.class);
                    intent.putExtra(AppConfig.BUNDLE_PAGE, "Document");
                    intent.putExtra(AppConfig.BUNDLE_TYPE, "Document");
                    intent.putExtra(AppConfig.BUNDLE_NAME, documentModel.getDocumentName());
                    intent.putExtra(AppConfig.BUNDLE_FOLDER_NAME, documentModel.getCategoryName());
                    intent.putExtra(AppConfig.BUNDLE_ID, documentModel.getId());
                    intent.putExtra(AppConfig.BUNDLE_FOLDER_ID, documentModel.getCategoryId());
                    intent.putExtra(AppConfig.BUNDLE_VERSION, documentModel.getVersion());
                    intent.putExtra(AppConfig.BOOKMARK_ID, bookid);
                    intent.putExtra(AppConfig.BOOKMARK_ALL, "yes");
                    //  setResult(DocumentViewFragment.RESULT_CODE, intent);
                    startActivity(intent);
                }
            }
            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
                Log.e("getBookMarkEntryList", e.getLocalizedMessage());
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

    public void getCategoryDetailsOfSelectedDocument(String folderId, int count) {
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
                List<BreadCrumbItem> crumbs = new ArrayList<>();
                if (categoryModel == null) {
                    for (int i = allParents.size() - 1; i >= 0; i--) {
                        crumbs.add(allParents.get(i));
                    }
                    allbreadcrumblist.add(crumbs);
                    allParents.clear();
                    mutableLiveData.postValue(count);
                } else {
                    allParents.add(new BreadCrumbItem(categoryModel.getName(), categoryModel.getId()));
                    getCategoryDetailsOfSelectedDocument(categoryModel.getParent(), count);
                }
                Log.e("sizeofallcrumbs", allbreadcrumblist.size() + "");
            }

            @Override
            public void onError(Throwable e) {
                mutableLiveData.postValue(count);
            }
        });
    }


    public void getdocdet(String docid, int count) {
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
                    Log.e("documentModelnotnull", docid + "  " + count);
                    getCategoryDetailsOfSelectedDocument(documentModelbm.categoryId, count);
                } else {
                    allbreadcrumblist.add(null);
                    Log.e("documentModelnull", docid + "  " + count);
                    mutableLiveData.postValue(count);
                }
            }

            @Override
            public void onError(Throwable e) {
                allbreadcrumblist.add(null);
                mutableLiveData.postValue(count);
                Log.e("getBookMarkEntryListerr", e.getLocalizedMessage());

            }
        });
    }


    @Override
    public void onBookMarkEntryListSuccess(List<BookmarkEntryModel> bookmarkEntryModels) {

    }

    @Override
    public void onBookMarkEntryListError() {

    }

    @Override
    public void onBookmarkDeleteSucess() {

    }

    @Override
    public void onBookmarkDeleteError() {

    }
}
