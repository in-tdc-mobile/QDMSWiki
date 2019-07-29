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
import com.mariapps.qdmswiki.search.adapter.SearchResultAdapter;
import com.mariapps.qdmswiki.search.adapter.SearchFilterAdapter;
import com.mariapps.qdmswiki.search.model.FilterBooleanItem;
import com.mariapps.qdmswiki.search.model.SearchModel;
import com.mariapps.qdmswiki.search.model.SearchFilterModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.backBtn)
    AppCompatImageView backBtn;
    @BindView(R.id.searchET)
    CustomEditText searchET;
    @BindView(R.id.rvSearchType)
    CustomRecyclerView rvSearchType;
    @BindView(R.id.rvSearchList)
    CustomRecyclerView rvSearchList;

    private ArrayList<SearchFilterModel> searchType = new ArrayList<>();
    private SearchFilterAdapter searchFilterAdapter;
    private SearchResultAdapter searchResultAdapter;
    private ArrayList<SearchModel> searchList = new ArrayList<>();
    private boolean isFolderSelected = false;
    private boolean isDocumentSelected = false;
    private boolean isArticleSelected = false;
    private boolean isFormSelected = false;

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

        setSearchTypeData();
        setSearchList();
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
                            onSearch();
                        } else {
                            item.setSelected(true);
                            isFolderSelected = true;
                            onSearch();
                        }

                    } else if (item.getSearchType().equals("Document")) {
                        if (item.isSelected()) {
                            item.setSelected(false);
                            isDocumentSelected = false;
                            onSearch();
                        } else {
                            item.setSelected(true);
                            isDocumentSelected = true;
                            onSearch();
                        }

                    } else if (item.getSearchType().equals("Article")) {

                        if (item.isSelected()) {
                            item.setSelected(false);
                            isArticleSelected = false;
                            onSearch();
                        } else {
                            item.setSelected(true);
                            isArticleSelected = true;
                            onSearch();
                        }
                    } else if (item.getSearchType().equals("Forms")) {
                        if (item.isSelected()) {
                            item.setSelected(false);
                            isFormSelected = false;
                            onSearch();
                        } else {
                            item.setSelected(true);
                            isFormSelected = true;
                            onSearch();
                        }

                    }
                searchFilterAdapter.notifyDataSetChanged();

            }
        });
    }

    private void setSearchList() {
        searchList.add(new SearchModel(1, -1,"Folder", "GDPR Manual", "General Data Protection Manual"));
        searchList.add(new SearchModel(2, 1,"Forms", "Information Technology", "ECDIS Manual"));
        searchList.add(new SearchModel(3, -1,"Folder", "Ethical Ship Operations Policy", "ISO Ethical Ship Operations Policy"));
        searchList.add(new SearchModel(4, 3,"Forms", "Safety Management Manual", "Safety Management Manual Appendix"));
        searchList.add(new SearchModel(5, 1,"Forms", "LPG Carrier Manual", "LPG Carrier Manual"));
        searchList.add(new SearchModel(6, -1,"Folder", "GDPR Manual", "General Data Protection Manual"));
        searchList.add(new SearchModel(7, 1,"Forms", "Information Technology", "ECDIS Manual"));
        searchList.add(new SearchModel(8, 6,"Article", "Safety Management Manual", "Safety Management Manual Appendix"));
        searchList.add(new SearchModel(9, 3,"Document", "LPG Carrier Manual", "LPG Carrier Manual"));
        searchList.add(new SearchModel(10, 1,"Article", "GDPR Manual", "General Data Protection Manual"));
        searchList.add(new SearchModel(11, 1,"Document", "Information Technology", "ECDIS Manual"));

        searchResultAdapter = new SearchResultAdapter(this, searchList);
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
}
