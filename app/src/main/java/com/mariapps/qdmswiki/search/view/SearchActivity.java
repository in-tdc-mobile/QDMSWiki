package com.mariapps.qdmswiki.search.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.custom.CustomEditText;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.search.adapter.SearchResultAdapter;
import com.mariapps.qdmswiki.search.adapter.SearchFilterAdapter;
import com.mariapps.qdmswiki.search.model.FilterBooleanItem;
import com.mariapps.qdmswiki.search.model.SearchModel;
import com.mariapps.qdmswiki.search.model.SearchFilterModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.backBtn)
    AppCompatImageView backBtn;
    @BindView(R.id.searchET)
    CustomEditText searchET;
    @BindView(R.id.rvSearchType)
    CustomRecyclerView rvSearchType;
    @BindView(R.id.rvSearchList)
    CustomRecyclerView rvSearchList;

    private List<SearchFilterModel> searchType = new ArrayList<>();
    private SearchFilterAdapter searchFilterAdapter;
    private SearchResultAdapter searchResultAdapter;
    private List<SearchModel> searchList = new ArrayList<>();
    private boolean isFolderSelected = false;
    private boolean isDocumentSelected = true;
    private boolean isArticleSelected = false;
    private boolean isFormSelected = false;
    private HomeDatabase homeDatabase;

    Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        rvSearchType.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        rvSearchType.setHasFixedSize(true);

        rvSearchList.setHasFixedSize(true);
        rvSearchList.setLayoutManager(new LinearLayoutManager(this));
        rvSearchList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        homeDatabase = HomeDatabase.getInstance(SearchActivity.this);
        getSearchList();
        setSearchTypeData();
    }


    @Override
    protected void setUpPresenter() {

    }

    @Override
    protected void isNetworkAvailable(boolean isConnected) {

    }

    private void setSearchTypeData() {
        searchType.add(new SearchFilterModel(1, "Folder", false));
        searchType.add(new SearchFilterModel(2, "Document", false));
        searchType.add(new SearchFilterModel(3, "Article", false));
        searchType.add(new SearchFilterModel(4, "Forms", false));

        searchFilterAdapter = new SearchFilterAdapter(SearchActivity.this, searchType);
        rvSearchType.setAdapter(searchFilterAdapter);

        searchFilterAdapter.setItemClickListener(new SearchFilterAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(SearchFilterModel item) {
                    if (item.getSearchType().equals("Folder")) {
                        if (item.isSelected()) {
                            item.setSelected(false);
                            isFolderSelected = false;
                            getSearchList();
                        } else {
                            item.setSelected(true);
                            isFolderSelected = true;
                            getSearchList();
                        }

                    } else if (item.getSearchType().equals("Document")) {
                        if (item.isSelected()) {
                            item.setSelected(false);
                            isDocumentSelected = false;
                            getSearchList();
                        } else {
                            item.setSelected(true);
                            isDocumentSelected = true;
                            getSearchList();
                        }

                    } else if (item.getSearchType().equals("Article")) {

                        if (item.isSelected()) {
                            item.setSelected(false);
                            isArticleSelected = false;
                            getSearchList();
                        } else {
                            item.setSelected(true);
                            isArticleSelected = true;
                            getSearchList();
                        }
                    } else if (item.getSearchType().equals("Forms")) {
                        if (item.isSelected()) {
                            item.setSelected(false);
                            isFormSelected = false;
                            getSearchList();
                        } else {
                            item.setSelected(true);
                            isFormSelected = true;
                            getSearchList();
                        }

                    }
                searchFilterAdapter.notifyDataSetChanged();

            }
        });
    }

    private void setData() {
        searchResultAdapter = new SearchResultAdapter(this, searchList,"SEARCH");
        rvSearchList.setAdapter(searchResultAdapter);

        searchResultAdapter.setItemClickListener(new SearchResultAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(SearchModel item) {
                Intent intent = new Intent(SearchActivity.this, FolderStructureActivity.class);
                intent.putExtra(AppConfig.BUNDLE_TYPE,item.getType());
                intent.putExtra(AppConfig.BUNDLE_FOLDER_NAME,item.getName());
                intent.putExtra(AppConfig.BUNDLE_FOLDER_ID,item.getId());
                startActivity(intent);

            }
        });
    }

    @OnClick(R.id.backBtn)
    public void onClick() {
        onBackPressed();
    }


    @OnTextChanged(R.id.searchET)
    void onSearch() {
        if (searchResultAdapter != null) {
            searchResultAdapter.getFilter().filter(gson.toJson(new FilterBooleanItem(isFolderSelected, isDocumentSelected, isArticleSelected, isFormSelected, searchET.getText().toString())));
        }
    }

    public void getSearchList() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if(isDocumentSelected && isArticleSelected)
                    searchList = homeDatabase.homeDao().getAllDocumentsAndArticles();
                else if(isDocumentSelected && isFolderSelected)
                    searchList = homeDatabase.homeDao().getAllDocumentsAndFolders();
                else if(isDocumentSelected && !isArticleSelected)
                    searchList = homeDatabase.homeDao().getAllDocuments();
                else if(!isDocumentSelected && isArticleSelected)
                    searchList = homeDatabase.homeDao().getAllArticles();
                else if(isFolderSelected && !isDocumentSelected && !isArticleSelected)
                    searchList = homeDatabase.homeDao().getAllCategories();


            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                setData();
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

}
