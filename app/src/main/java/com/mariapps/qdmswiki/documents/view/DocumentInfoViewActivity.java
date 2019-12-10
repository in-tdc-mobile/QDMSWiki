package com.mariapps.qdmswiki.documents.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.documents.adapter.TagsAdapter;
import com.mariapps.qdmswiki.documents.adapter.UserAdapter;
import com.mariapps.qdmswiki.documents.model.DocumentInfoModel;
import com.mariapps.qdmswiki.documents.model.UserModel;
import com.mariapps.qdmswiki.home.model.CategoryModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.model.DownloadFilesResponseModel;
import com.mariapps.qdmswiki.home.model.TagModel;
import com.mariapps.qdmswiki.home.presenter.HomePresenter;
import com.mariapps.qdmswiki.home.view.HomeActivity;
import com.mariapps.qdmswiki.home.view.HomeView;
import com.mariapps.qdmswiki.notification.model.NotificationModel;
import com.mariapps.qdmswiki.search.model.BreadCrumbItem;
import com.mariapps.qdmswiki.search.model.SearchModel;
import com.mariapps.qdmswiki.serviceclasses.APIException;
import com.mariapps.qdmswiki.usersettings.UserInfoModel;
import com.mariapps.qdmswiki.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DocumentInfoViewActivity extends BaseActivity implements HomeView {

    @BindView(R.id.headingTV)
    CustomTextView headingTV;
    @BindView(R.id.homeTV)
    CustomTextView homeTV;
    @BindView(R.id.nameTV)
    CustomTextView nameTV;
    @BindView(R.id.titleTV)
    CustomTextView titleTV;
    @BindView(R.id.documentNumberTV)
    CustomTextView documentNumberTV;
    @BindView(R.id.documentVersionTV)
    CustomTextView documentVersionTV;
    @BindView(R.id.locationTV)
    CustomTextView locationTV;
    @BindView(R.id.publishedOnTV)
    CustomTextView publishedOnTV;
    @BindView(R.id.approvedByTV)
    CustomTextView approvedByTV;
    @BindView(R.id.sitesTV)
    CustomTextView sitesTV;
    @BindView(R.id.tagsRV)
    CustomRecyclerView tagsRV;
    @BindView(R.id.numberTV)
    CustomTextView numberTV;
    @BindView(R.id.linNum)
    LinearLayout linNum;
    @BindView(R.id.linUsers)
    LinearLayout linUsers;
    @BindView(R.id.usersRV)
    CustomRecyclerView usersRV;
    @BindView(R.id.arrowIV)
    AppCompatImageView arrowIV;

    private HomePresenter homePresenter;
    private List<TagModel> tagList = new ArrayList<>();
    private ArrayList<UserModel> usersList = new ArrayList<>();
    private String folderName;
    private String id;
    private String folderId;
    private String location;
    List<String> allParents = new ArrayList<>();

    @Override
    protected void setUpPresenter() {
        homePresenter = new HomePresenter(this, this);
    }

    @Override
    protected void isNetworkAvailable(boolean isConnected) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_info);
        ButterKnife.bind(this);

        try{
            Bundle bundle = getIntent().getExtras();
            id = bundle.getString(AppConfig.BUNDLE_ID);
            folderId = bundle.getString(AppConfig.BUNDLE_FOLDER_ID);
            folderName = bundle.getString(AppConfig.BUNDLE_FOLDER_NAME);
        }
        catch (Exception e){}

        tagsRV.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        tagsRV.setHasFixedSize(true);

        usersRV.setHasFixedSize(true);
        usersRV.setLayoutManager(new LinearLayoutManager(this));
        usersRV.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        headingTV.setText(getResources().getString(R.string.string_document_details));
        nameTV.setText(folderName);

        getLocationDetails(folderId);
        setUserList();

    }

    private void setUserList() {
    }

    private void setDocumentDetails() {
        homePresenter.getDocumentInfo(id);

        UserAdapter userAdapter = new UserAdapter(this,usersList);
        usersRV.setAdapter(userAdapter);
    }

    @OnClick({R.id.backBtn, R.id.homeTV,R.id.nameTV, R.id.linNum})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.nameTV:
                onBackPressed();
                break;
            case R.id.homeTV:
                Intent homeIntent = new Intent(DocumentInfoViewActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                break;
            case R.id.linNum:
//                if(documentInfoModel.isUserShown){
//                    linUsers.setVisibility(View.GONE);
//                    documentInfoModel.setUserShown(false);
//                    arrowIV.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_expand_arrow,null));
//                }
//                else{
//                    linUsers.setVisibility(View.VISIBLE);
//                    documentInfoModel.setUserShown(true);
//                    arrowIV.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_collapse_arrow,null));
//                }

                break;
        }
    }

    public void getLocationDetails(String folderId) {
        homePresenter.getCategoryDetailsOfSelectedDocument(folderId);
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
        if(categoryModel==null){
            for(int i=allParents.size()-1;i>=0;i--){
                if(location != null)
                    location = location +"/"+ allParents.get(i);
                else
                    location = "/"+allParents.get(i);
            }
            setDocumentDetails();
        }
        else {
            allParents.add(categoryModel.getName());
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
        titleTV.setText("Document Details");
        locationTV.setText(location);
        documentNumberTV.setText(documentModel.getDocumentNumber());
        documentVersionTV.setText("V "+documentModel.getVersion());
        publishedOnTV.setText(DateUtils.getFormattedDateinDateTime(documentModel.getDate()));
        approvedByTV.setText(documentModel.getApprovedName());

        tagList = documentModel.getTags();
        TagsAdapter tagsAdapter = new TagsAdapter(this,tagList);
        tagsRV.setAdapter(tagsAdapter);
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
