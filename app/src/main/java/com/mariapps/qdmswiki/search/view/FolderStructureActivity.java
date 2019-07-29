package com.mariapps.qdmswiki.search.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.search.adapter.BreadCrumbAdapter;
import com.mariapps.qdmswiki.search.model.BreadCrumbItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FolderStructureActivity extends BaseActivity {

    @BindView(R.id.backBtn)
    AppCompatImageView backBtn;
    @BindView(R.id.headingTV)
    CustomTextView headingTV;
    @BindView(R.id.homeTV)
    CustomTextView homeTV;
    @BindView(R.id.breadCrumbRV)
    CustomRecyclerView breadCrumbRV;


    private BreadCrumbAdapter breadCrumbAdapter;
    private String type = "Document";
    private String folderName = "";
    private Integer id;
    List<BreadCrumbItem> breadCrumbItems = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_structure);

        if (getIntent() != null && getIntent().getExtras() != null) {
            type = getIntent().getExtras().getString(AppConfig.BUNDLE_TYPE);
            folderName = getIntent().getExtras().getString(AppConfig.BUNDLE_FOLDER_NAME);
            id = getIntent().getExtras().getInt(AppConfig.BUNDLE_FOLDER_ID);
        }

        if(type.equals("Document")){
            // Begin the transaction
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.frameLayout, new DocumentViewFragment());
            ft.commit();
        }
        else{
            // Begin the transaction
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FolderFragment folderFragment = new FolderFragment();
            Bundle args = new Bundle();
            args.putInt(AppConfig.BUNDLE_FOLDER_ID, id);
            folderFragment.setArguments(args);
            ft.replace(R.id.frameLayout, folderFragment);
            ft.commit();
        }

        initBreadCrumb(folderName,id);
    }

    public void initBreadCrumb(String folderName, Integer id) {
        breadCrumbRV.setHasFixedSize(true);
        breadCrumbRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        breadCrumbAdapter = new BreadCrumbAdapter(initBreadCrumbList(folderName,id), this);
        breadCrumbAdapter.setBreadCrumbListener(new BreadCrumbAdapter.BreadCrumbListener() {

            @Override
            public void onClick(int count, Integer id) {
                    popUptoPosition(count,id);
            }
        });

        breadCrumbRV.setAdapter(breadCrumbAdapter);
    }

    private List<BreadCrumbItem> initBreadCrumbList (String folderName,Integer id){
        breadCrumbItems.add(new BreadCrumbItem(folderName, id));
        return breadCrumbItems;
    }

    private void popUptoPosition(int count, Integer id) {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < count; i++) {
            fm.popBackStack();
            breadCrumbAdapter.removeItem();
        }

        FolderFragment folderFragment = new FolderFragment();
        Bundle args = new Bundle();
        args.putInt(AppConfig.BUNDLE_FOLDER_ID, id);
        folderFragment.setArguments(args);
        replaceFragments(folderFragment);
    }

    @Override
    protected void setUpPresenter() {

    }

    @Override
    protected void isNetworkAvailable(boolean isConnected) {

    }


    @OnClick({R.id.backBtn,R.id.homeTV})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.homeTV:
                finish();
                break;
        }

        onBackPressed();
    }

    public void replaceFragments(Fragment fragment) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment)
                .commit();
    }


}
