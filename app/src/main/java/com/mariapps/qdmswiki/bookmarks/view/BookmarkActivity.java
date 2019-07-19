package com.mariapps.qdmswiki.bookmarks.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.bookmarks.adapter.BookmarkAdapter;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkModel;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.documents.view.DocumentInfoViewActivity;
import com.mariapps.qdmswiki.documents.view.DocumentViewActivity;
import com.mariapps.qdmswiki.home.view.HomeActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BookmarkActivity extends BaseActivity {
    @BindView(R.id.headingTV)
    CustomTextView headingTV;
    @BindView(R.id.homeTV)
    CustomTextView homeTV;
    @BindView(R.id.nameTV)
    CustomTextView nameTV;
    @BindView(R.id.titleTV)
    CustomTextView titleTV;
    @BindView(R.id.bookmarksRV)
    CustomRecyclerView bookmarksRV;

    private BookmarkAdapter bookmarkAdapter;
    private ArrayList<BookmarkModel> bookMarkList = new ArrayList<>();

    @Override
    protected void setUpPresenter() {

    }

    @Override
    protected void isNetworkAvailable(boolean isConnected) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        ButterKnife.bind(this);

        bookmarksRV.setHasFixedSize(true);
        bookmarksRV.setLayoutManager(new LinearLayoutManager(this));
        bookmarksRV.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        initView();
        initRecyclerView();
    }

    private void initRecyclerView() {
        bookMarkList.add(new BookmarkModel(1,"Procedures"));
        bookMarkList.add(new BookmarkModel(2,"Data breach risk assessment"));
        bookMarkList.add(new BookmarkModel(3,"Data protection impact assessment (DPIA)"));
        bookMarkList.add(new BookmarkModel(4,"Non- routine third party data requests"));
        bookMarkList.add(new BookmarkModel(5,"Cookie consent"));

        bookmarkAdapter = new BookmarkAdapter(this,bookMarkList);
        bookmarksRV.setAdapter(bookmarkAdapter);

        bookmarkAdapter.setRowClickListener(new BookmarkAdapter.RowClickListener() {
            @Override
            public void onItemClicked(BookmarkModel bookmarkModel) {
                Intent intent = new Intent(BookmarkActivity.this, DocumentViewActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        headingTV.setText(getString(R.string.string_bookmarks));
        titleTV.setText(getString(R.string.string_bookmarks));
    }

    @OnClick({R.id.backBtn, R.id.homeTV})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.homeTV:
                Intent homeIntent = new Intent(BookmarkActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                break;
        }
    }

}
