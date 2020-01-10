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
import com.mariapps.qdmswiki.bookmark.presenter.BookmarkPresenter;
import com.mariapps.qdmswiki.bookmarks.adapter.BookmarkAdapter;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkEntryModel;
import com.mariapps.qdmswiki.bookmarks.model.BookmarkModel;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.documents.view.DocumentInfoViewActivity;
import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.view.HomeActivity;
import com.mariapps.qdmswiki.notification.view.NotificationActivity;
import com.mariapps.qdmswiki.search.view.DocumentViewFragment;
import com.mariapps.qdmswiki.search.view.FolderStructureActivity;
import com.mariapps.qdmswiki.settings.presenter.SettingsPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class BookmarkActivity extends BaseActivity implements BookmarkView{
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
    BookmarkPresenter bookmarkPresenter;
    private List<BookmarkEntryModel> bookmarkModel;
    private String folderName;
    private String documentId;
    private HomeDatabase homeDatabase;
    private int deletePosition;

    @Override
    protected void setUpPresenter() {
        bookmarkPresenter=new BookmarkPresenter(this,this);
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
            documentId = bundle.getString(AppConfig.BUNDLE_ID);
            folderName = bundle.getString(AppConfig.BUNDLE_FOLDER_NAME);
        }
        catch (Exception e){}
        bookmarksRV.setHasFixedSize(true);
        bookmarksRV.setLayoutManager(new LinearLayoutManager(this));
        bookmarksRV.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        nameTV.setText(folderName);
        homeDatabase = HomeDatabase.getInstance(BookmarkActivity.this);
        initView();
        bookmarkPresenter.getBookMarkEntryList(documentId);
    }

    private void initRecyclerView(List<BookmarkEntryModel> bookmarkEntryModels) {
        bookmarkAdapter = new BookmarkAdapter(this,bookmarkEntryModels);
        bookmarksRV.setAdapter(bookmarkAdapter);

        bookmarkAdapter.setRowClickListener(new BookmarkAdapter.RowClickListener() {
            @Override
            public void onItemClicked(BookmarkEntryModel bookmarkEntryModel) {
                Intent intent = new Intent();
                intent.putExtra(DocumentViewFragment.BOOKMARKID, bookmarkEntryModel.getBookmarkId());
                setResult(DocumentViewFragment.RESULT_CODE, intent); // You can also send result without any data using setResult(int resultCode)
                finish();

            }
        });

        bookmarkAdapter.setDeleteClickListener(new BookmarkAdapter.DeleteClickListener() {
            @Override
            public void onDeleteClicked(BookmarkEntryModel bookmarkEntryModel, int position) {
                deletePosition = position;
                bookmarkPresenter.deleteBookmark(bookmarkEntryModel.getDocumentId(),bookmarkEntryModel.getBookmarkId());
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

    @Override
    public void onBookMarkEntryListSuccess(List<BookmarkEntryModel> bookmarkEntryModels) {
        bookmarkModel = bookmarkEntryModels;
        initRecyclerView(bookmarkEntryModels);
    }

    @Override
    public void onBookMarkEntryListError() {
    }

    @Override
    public void onBookmarkDeleteSucess() {
        bookmarkModel.remove(deletePosition);
        bookmarkAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBookmarkDeleteError() {

    }
}
