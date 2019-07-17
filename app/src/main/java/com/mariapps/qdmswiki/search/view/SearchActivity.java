package com.mariapps.qdmswiki.search.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.mariapps.qdmswiki.MainActivity;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.custom.CustomEditText;
import com.mariapps.qdmswiki.custom.CustomViewPager;
import com.mariapps.qdmswiki.documents.adapter.DocumentsAdapter;
import com.mariapps.qdmswiki.documents.model.DepartmentModel;
import com.mariapps.qdmswiki.documents.model.DocumentsModel;
import com.mariapps.qdmswiki.documents.view.DocumetViewActivity;
import com.mariapps.qdmswiki.home.adapter.RecommendedRecentlyAdapter;
import com.mariapps.qdmswiki.home.model.RecommendedRecentlyModel;
import com.mariapps.qdmswiki.search.adapter.SearchTypeAdapter;
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
    RecyclerView rvSearchType;
    @BindView(R.id.rvSearchList)
    RecyclerView rvSearchList;

    private ArrayList<SearchTypeModel> searchType = new ArrayList<>();
    private SearchTypeAdapter searchTypeAdapter;

    private RecommendedRecentlyAdapter recommendedRecentlyAdapter;
    private ArrayList<RecommendedRecentlyModel> documentsList = new ArrayList<>();

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
        searchType.add(new SearchTypeModel(1,"Folder"));
        searchType.add(new SearchTypeModel(2,"Document"));
        searchType.add(new SearchTypeModel(3,"Article"));
        searchType.add(new SearchTypeModel(4,"Forms"));

        searchTypeAdapter = new SearchTypeAdapter(SearchActivity.this,searchType);
        rvSearchType.setAdapter(searchTypeAdapter);
    }

    private void setSearchList() {
        documentsList.add(new RecommendedRecentlyModel(1, "General Data Protection Manual","GDPR Manual","V1"));
        documentsList.add(new RecommendedRecentlyModel(2, "ECDIS Manual","Information Technology","V2"));
        documentsList.add(new RecommendedRecentlyModel(3, "ISO Ethical Ship Operations Policy","Ethical Ship Operations Policy","V1"));
        documentsList.add(new RecommendedRecentlyModel(4, "Safety Management Manual Appendix","Safety Management Manual","V1"));
        documentsList.add(new RecommendedRecentlyModel(5, "LPG Carrier Manual","LPG Carrier Manual","V2"));
        documentsList.add(new RecommendedRecentlyModel(6, "General Data Protection Manual","GDPR Manual","V1"));
        documentsList.add(new RecommendedRecentlyModel(7, "ECDIS Manual","Information Technology","V2"));

        recommendedRecentlyAdapter = new RecommendedRecentlyAdapter(this, documentsList, "SEARCH");
        rvSearchList.setAdapter(recommendedRecentlyAdapter);

        recommendedRecentlyAdapter.setRowClickListener(new RecommendedRecentlyAdapter.RowClickListener() {
            @Override
            public void onItemClicked(RecommendedRecentlyModel recommendedRecentlyModel) {
                Intent intent = new Intent(SearchActivity.this, DocumetViewActivity.class);
                startActivity(intent);

            }
        });
    }

    @OnClick(R.id.backBtn)
    public void onClick() {
        onBackPressed();
    }
}
