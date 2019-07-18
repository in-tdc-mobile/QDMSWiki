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

import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.applicationinfo.view.ApplicationInfoActivity;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.documents.adapter.TagsAdapter;
import com.mariapps.qdmswiki.documents.adapter.UserAdapter;
import com.mariapps.qdmswiki.documents.model.DocumentInfoModel;
import com.mariapps.qdmswiki.documents.model.TagModel;
import com.mariapps.qdmswiki.documents.model.UserModel;
import com.mariapps.qdmswiki.home.view.HomeActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class DocumentInfoViewActivity extends BaseActivity {

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

    private DocumentInfoModel documentInfoModel;
    private ArrayList<TagModel> tagList = new ArrayList<>();
    private ArrayList<UserModel> usersList = new ArrayList<>();

    @Override
    protected void setUpPresenter() {

    }

    @Override
    protected void isNetworkAvailable(boolean isConnected) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_info);

        tagsRV.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        tagsRV.setHasFixedSize(true);

        usersRV.setHasFixedSize(true);
        usersRV.setLayoutManager(new LinearLayoutManager(this));
        usersRV.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        headingTV.setText(getResources().getString(R.string.string_document_details));

        setTagList();
        setUserList();
        setDocumentDetails();
    }

    private void setTagList() {
        tagList.add(new TagModel(1,2,"GDPR","Y"));
        tagList.add(new TagModel(2,2,"LPSQ Department","Y"));
        tagList.add(new TagModel(3,2,"LPSQ Departmentsss","Y"));
    }

    private void setUserList() {
        usersList.add(new UserModel(1,"Administrator"));
        usersList.add(new UserModel(2,"Aruna Ramakrishnan"));
        usersList.add(new UserModel(3,"Subin Kuriakose"));
        usersList.add(new UserModel(4,"Rio Issac"));
        usersList.add(new UserModel(5,"Arun Aravindan"));
        usersList.add(new UserModel(6,"Thomas Paul"));
        usersList.add(new UserModel(7,"Elby Samson"));

        usersList.add(new UserModel(8,"Administrator"));
        usersList.add(new UserModel(9,"Aruna Ramakrishnan"));
        usersList.add(new UserModel(10,"Subin Kuriakose"));
        usersList.add(new UserModel(11,"Rio Issac"));
        usersList.add(new UserModel(12,"Arun Aravindan"));
        usersList.add(new UserModel(13,"Thomas Paul"));
        usersList.add(new UserModel(14,"Elby Samson"));
    }

    private void setDocumentDetails() {
        titleTV.setText("Document Details");
        documentInfoModel = new DocumentInfoModel(1, "40","20",
                "/DataProtection/GDPR Manual","25/02/2019 11:08:35 AM",
                "Director LPSQ", "All vessels",tagList,"25",usersList,false);

        documentNumberTV.setText(documentInfoModel.getDocumentNumber());
        documentVersionTV.setText(documentInfoModel.getDocumentVersion());
        locationTV.setText(documentInfoModel.getLocation());
        publishedOnTV.setText(documentInfoModel.getPublishedOn());
        approvedByTV.setText(documentInfoModel.getApprovedBy());
        sitesTV.setText(documentInfoModel.getSites());
        documentNumberTV.setText(documentInfoModel.getDocumentNumber());
        documentNumberTV.setText(documentInfoModel.getDocumentNumber());
        documentNumberTV.setText(documentInfoModel.getDocumentNumber());

        TagsAdapter tagsAdapter = new TagsAdapter(this,tagList);
        tagsRV.setAdapter(tagsAdapter);

        UserAdapter userAdapter = new UserAdapter(this,usersList);
        usersRV.setAdapter(userAdapter);

    }

    @OnClick({R.id.backBtn, R.id.homeTV, R.id.linNum})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.homeTV:
                Intent homeIntent = new Intent(DocumentInfoViewActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                break;
            case R.id.linNum:
                if(documentInfoModel.isUserShown){
                    linUsers.setVisibility(View.GONE);
                    documentInfoModel.setUserShown(false);
                    arrowIV.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.down_arrow,null));
                }
                else{
                    linUsers.setVisibility(View.VISIBLE);
                    documentInfoModel.setUserShown(true);
                    arrowIV.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.up_arrow,null));
                }

                break;
        }
    }
}
