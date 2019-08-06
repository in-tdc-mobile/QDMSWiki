package com.mariapps.qdmswiki.bookmarks.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.bookmarks.adapter.BookmarkAdapter;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkModel;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.documents.view.DocumentInfoViewActivity;
import com.mariapps.qdmswiki.home.view.HomeActivity;
import com.mariapps.qdmswiki.search.view.FolderStructureActivity;

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
    private String folderName;
    private Integer id;


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

        try{
            Bundle bundle = getIntent().getExtras();
            id = bundle.getInt(AppConfig.BUNDLE_FOLDER_ID);
            folderName = bundle.getString(AppConfig.BUNDLE_FOLDER_NAME);
        }
        catch (Exception e){}

        bookmarksRV.setHasFixedSize(true);
        bookmarksRV.setLayoutManager(new LinearLayoutManager(this));
        bookmarksRV.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        nameTV.setText(folderName);

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
                Intent intent = new Intent(BookmarkActivity.this, FolderStructureActivity.class);
                intent.putExtra(AppConfig.BUNDLE_TYPE,"Document");
                intent.putExtra(AppConfig.BUNDLE_FOLDER_NAME,bookmarkModel.getBookMark());
                intent.putExtra(AppConfig.BUNDLE_FOLDER_ID,bookmarkModel.getId());
                startActivity(intent);
            }
        });
    }

    private void initView() {
        headingTV.setText(getString(R.string.string_bookmarks));
        titleTV.setText(getString(R.string.string_bookmarks));
    }

    @OnClick({R.id.backBtn, R.id.nameTV, R.id.homeTV})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.nameTV:
                onBackPressed();
                break;
            case R.id.homeTV:
                Intent homeIntent = new Intent(BookmarkActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                break;
        }
    }

}
