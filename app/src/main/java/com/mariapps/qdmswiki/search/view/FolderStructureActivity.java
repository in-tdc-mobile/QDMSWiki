package com.mariapps.qdmswiki.search.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.home.model.CategoryModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.model.DownloadFilesResponseModel;
import com.mariapps.qdmswiki.home.presenter.HomePresenter;
import com.mariapps.qdmswiki.home.view.HomeView;
import com.mariapps.qdmswiki.notification.model.NotificationModel;
import com.mariapps.qdmswiki.search.adapter.BreadCrumbAdapter;
import com.mariapps.qdmswiki.search.model.BreadCrumbItem;
import com.mariapps.qdmswiki.search.model.SearchModel;
import com.mariapps.qdmswiki.serviceclasses.APIException;
import com.mariapps.qdmswiki.usersettings.UserInfoModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FolderStructureActivity extends BaseActivity implements HomeView{

    @BindView(R.id.backBtn)
    AppCompatImageView backBtn;
    @BindView(R.id.headingTV)
    CustomTextView headingTV;
    @BindView(R.id.homeTV)
    CustomTextView homeTV;
    @BindView(R.id.linLayout)
    LinearLayout linLayout;
    @BindView(R.id.breadCrumbRV)
    CustomRecyclerView breadCrumbRV;

    private HomePresenter homePresenter;
    private BreadCrumbAdapter breadCrumbAdapter;
    private String page;
    private String type = "Document";
    private String name = "";
    private String folderName = "";
    private String id;
    private String categoryId;
    private String version;
    List<BreadCrumbItem> breadCrumbItems = new ArrayList<>();
    List<BreadCrumbItem> allParents = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_structure);

        if (getIntent() != null && getIntent().getExtras() != null) {
            page = getIntent().getExtras().getString(AppConfig.BUNDLE_PAGE);
            type = getIntent().getExtras().getString(AppConfig.BUNDLE_TYPE);
            name = getIntent().getExtras().getString(AppConfig.BUNDLE_NAME);
            folderName = getIntent().getExtras().getString(AppConfig.BUNDLE_FOLDER_NAME);
            id = getIntent().getExtras().getString(AppConfig.BUNDLE_ID);
            categoryId = getIntent().getExtras().getString(AppConfig.BUNDLE_FOLDER_ID);
            version = getIntent().getExtras().getString(AppConfig.BUNDLE_VERSION);
            headingTV.setText(name);
        }

        if(page.equals("DocumentView"))
            linLayout.setVisibility(View.GONE);
        else
            linLayout.setVisibility(View.VISIBLE);

        if (type.equals("Document") || type.equals("Article")) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            DocumentViewFragment documentViewFragment = new DocumentViewFragment();
            Bundle args = new Bundle();
            args.putString(AppConfig.BUNDLE_TYPE, type);
            args.putString(AppConfig.BUNDLE_ID, id);
            args.putString(AppConfig.BUNDLE_NAME, name);
            args.putString(AppConfig.BUNDLE_FOLDER_ID, categoryId);
            args.putString(AppConfig.BUNDLE_FOLDER_NAME, folderName);
            args.putString(AppConfig.BUNDLE_VERSION, version);
            documentViewFragment.setArguments(args);
            ft.replace(R.id.frameLayout, documentViewFragment);
            ft.commit();
            if(!page.equals("DocumentView"))
                getBreadCrumbDetails(categoryId);

        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FolderFragment folderFragment = new FolderFragment();
            Bundle args = new Bundle();
            args.putString(AppConfig.BUNDLE_FOLDER_ID, categoryId);
            args.putString(AppConfig.BUNDLE_FOLDER_NAME, folderName);
            folderFragment.setArguments(args);
            ft.addToBackStack(null);
            ft.add(R.id.frameLayout, folderFragment);
            ft.commit();
            if(!page.equals("DocumentView"))
                initBreadCrumb(name, categoryId);
        }

    }

    public void getBreadCrumbDetails(String categoryId) {
        homePresenter.getCategoryDetailsOfSelectedDocument(categoryId);
    }


    private void initBreadCrumbForDoc() {
        breadCrumbRV.setHasFixedSize(true);
        breadCrumbRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        breadCrumbAdapter = new BreadCrumbAdapter(breadCrumbItems, this);
        breadCrumbAdapter.setBreadCrumbListener(new BreadCrumbAdapter.BreadCrumbListener() {

            @Override
            public void onClick(int count, BreadCrumbItem breadCrumbItem) {
                popUptoPosition(count);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                FolderFragment folderFragment = new FolderFragment();
                Bundle args = new Bundle();
                args.putString(AppConfig.BUNDLE_FOLDER_ID, breadCrumbItem.getId());
                args.putString(AppConfig.BUNDLE_FOLDER_NAME, breadCrumbItem.getHeading());
                folderFragment.setArguments(args);
                replaceFragments(folderFragment,breadCrumbItem.getId(),breadCrumbItem.getHeading());
            }
        });

        breadCrumbRV.setAdapter(breadCrumbAdapter);
    }

    public void initBreadCrumb(String folderName, String id) {
        breadCrumbRV.setHasFixedSize(true);
        breadCrumbRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        breadCrumbAdapter = new BreadCrumbAdapter(initBreadCrumbList(folderName, id), this);
        breadCrumbAdapter.setBreadCrumbListener(new BreadCrumbAdapter.BreadCrumbListener() {

            @Override
            public void onClick(int count, BreadCrumbItem breadCrumbItem) {
                popUptoPosition(count);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                FolderFragment folderFragment = new FolderFragment();
                Bundle args = new Bundle();
                args.putString(AppConfig.BUNDLE_FOLDER_ID, breadCrumbItem.getId());
                args.putString(AppConfig.BUNDLE_FOLDER_NAME, breadCrumbItem.getHeading());
                folderFragment.setArguments(args);
                replaceFragments(folderFragment,breadCrumbItem.getId(),breadCrumbItem.getHeading());
            }
        });

        breadCrumbRV.setAdapter(breadCrumbAdapter);
    }


    private List<BreadCrumbItem> initBreadCrumbList(String folderName, String id) {
        breadCrumbItems.add(new BreadCrumbItem(folderName, id));
        return breadCrumbItems;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
            breadCrumbItems.remove(breadCrumbItems.size()-1);
            breadCrumbAdapter.notifyDataSetChanged();
        } else {
            finish();
        }
    }

    private void popUptoPosition(int count) {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < count; i++) {
            fm.popBackStack();
            breadCrumbAdapter.removeItem();
        }

        breadCrumbAdapter.notifyDataSetChanged();
    }

    @Override
    protected void setUpPresenter() {
        homePresenter = new HomePresenter(this,this);
    }

    @Override
    protected void isNetworkAvailable(boolean isConnected) {

    }

    @OnClick({R.id.backBtn, R.id.homeTV})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.homeTV:
                finish();
                break;
        }
    }

    public void addFragments(Fragment fragment,String newId, String newName) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        id = newId;
        folderName = newName;
        headingTV.setText(folderName);

        fragmentManager.beginTransaction().add(R.id.frameLayout, fragment)
                .addToBackStack(null)
                .commit();
        fragmentManager.executePendingTransactions();
    }

    public void replaceFragments(Fragment fragment,String newId, String newName) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        id = newId;
        folderName = newName;
        headingTV.setText(folderName);

        fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment)
                .addToBackStack(null)
                .commit();
        fragmentManager.executePendingTransactions();
    }


    @Override
    public void onGetDownloadUrlSuccess(String url) {

    }

    @Override
    public void onGetDownloadUrlError(APIException e) {

    }

    @Override
    public void onGetParentFolderSuccess(List<DocumentModel> documentModels) {

    }

    @Override
    public void onGetChildFoldersList(List<SearchModel> documentModels) {

    }

    @Override
    public void onGetUserImageSuccess(UserInfoModel userInfoModel) {

    }

    @Override
    public void onGetUserImageError() {

    }

    @Override
    public void onGetCategoryDetailsSuccess(CategoryModel categoryModel) {
        breadCrumbItems.clear();
        if(categoryModel==null){
            for(int i=allParents.size()-1;i>=0;i--){
                breadCrumbItems.add(allParents.get(i));
            }
            initBreadCrumbForDoc();
        }
        else {
            allParents.add(new BreadCrumbItem(categoryModel.getName(), categoryModel.getId()));
            homePresenter.getCategoryDetailsOfSelectedDocument(categoryModel.getParent());
        }


    }

    @Override
    public void onGetCategoryDetailsError() {

    }

    @Override
    public void onInsertCategoryDetailsSuccess() {

    }

    @Override
    public void onInsertCategoryDetailsError() {

    }

    @Override
    public void onGetDocumentInfoSuccess(DocumentModel documentModel) {

    }

    @Override
    public void onGetDocumentInfoError() {

    }

    @Override
    public void onGetNotificationCountSuccess(List<NotificationModel> notificationList) {

    }

    @Override
    public void onGetNotificationCountError() {

    }

    @Override
    public void onInsertFileListSuccess() {

    }

    @Override
    public void onInsertFileListError() {

    }

    @Override
    public void onInsertFormSuccess() {

    }

    @Override
    public void onInsertFormError() {

    }

    @Override
    public void onInsertImageSuccess() {

    }

    @Override
    public void onInsertImageError() {

    }

    @Override
    public void onGetDownloadFilesSuccess(DownloadFilesResponseModel downloadFilesResponseModel) {

    }

    @Override
    public void onGetDownloadFilesError() {

    }


}

