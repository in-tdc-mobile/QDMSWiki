package com.mariapps.qdmswiki.search.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.custom.CustomEditText;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.documents.view.DocumentViewActivity;
import com.mariapps.qdmswiki.home.adapter.RecommendedRecentlyAdapter;
import com.mariapps.qdmswiki.home.model.RecommendedRecentlyModel;
import com.mariapps.qdmswiki.search.adapter.SearchAdapter;
import com.mariapps.qdmswiki.search.adapter.SearchTypeAdapter;
import com.mariapps.qdmswiki.search.model.SearchModel;
import com.mariapps.qdmswiki.search.model.SearchTypeModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.backBtn)
    AppCompatImageView backBtn;
    @BindView(R.id.searchET)
    CustomEditText searchET;
    @BindView(R.id.rvSearchType)
    CustomRecyclerView rvSearchType;
    @BindView(R.id.rvSearchList)
    CustomRecyclerView rvSearchList;

    private ArrayList<SearchTypeModel> searchType = new ArrayList<>();
    private SearchTypeAdapter searchTypeAdapter;
    private SearchAdapter searchAdapter;
    private ArrayList<SearchModel> searchList = new ArrayList<>();

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
        searchType.add(new SearchTypeModel(1, "Folder", false));
        searchType.add(new SearchTypeModel(2, "Document", false));
        searchType.add(new SearchTypeModel(3, "Article", false));
        searchType.add(new SearchTypeModel(4, "Forms", false));

        searchTypeAdapter = new SearchTypeAdapter(SearchActivity.this, searchType);
        rvSearchType.setAdapter(searchTypeAdapter);

        searchTypeAdapter.setItemClickListener(new SearchTypeAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(SearchTypeModel item) {
                    if (item.isSelected()) {
                        item.setSelected(false);
                    } else {
                        item.setSelected(true);
                    }

                searchTypeAdapter.notifyDataSetChanged();

            }
        });
    }

    private void setSearchList() {
        searchList.add(new SearchModel(1, "Folder", "GDPR Manual", "General Data Protection Manual"));
        searchList.add(new SearchModel(2, "File", "Information Technology", "ECDIS Manual"));
        searchList.add(new SearchModel(3, "Folder", "Ethical Ship Operations Policy", "ISO Ethical Ship Operations Policy"));
        searchList.add(new SearchModel(4, "File", "Safety Management Manual", "Safety Management Manual Appendix"));
        searchList.add(new SearchModel(5, "File", "LPG Carrier Manual", "LPG Carrier Manual"));
        searchList.add(new SearchModel(6, "Folder", "GDPR Manual", "General Data Protection Manual"));
        searchList.add(new SearchModel(7, "File", "Information Technology", "ECDIS Manual"));

        searchAdapter = new SearchAdapter(this, searchList);
        rvSearchList.setAdapter(searchAdapter);

        searchAdapter.setItemClickListener(new SearchAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(SearchModel item) {
                Intent intent = new Intent(SearchActivity.this, DocumentViewActivity.class);
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.backBtn)
    public void onClick() {
        onBackPressed();
    }
}
