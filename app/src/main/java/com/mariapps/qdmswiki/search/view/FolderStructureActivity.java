package com.mariapps.qdmswiki.search.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
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
    private String id;
    List<BreadCrumbItem> breadCrumbItems = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_structure);

        if (getIntent() != null && getIntent().getExtras() != null) {
            type = getIntent().getExtras().getString(AppConfig.BUNDLE_TYPE);
            folderName = getIntent().getExtras().getString(AppConfig.BUNDLE_FOLDER_NAME);
            id = getIntent().getExtras().getString(AppConfig.BUNDLE_FOLDER_ID);
            headingTV.setText(folderName);
        }

        if (type.equals("Document")) {
            // Begin the transaction
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            DocumentViewFragment documentViewFragment = new DocumentViewFragment();
            Bundle args = new Bundle();
            args.putString(AppConfig.BUNDLE_FOLDER_ID, id);
            args.putString(AppConfig.BUNDLE_FOLDER_NAME, folderName);
            documentViewFragment.setArguments(args);
            ft.replace(R.id.frameLayout, documentViewFragment);
            ft.commit();
        } else {
            // Begin the transaction
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FolderFragment folderFragment = new FolderFragment();
            Bundle args = new Bundle();
            args.putString(AppConfig.BUNDLE_FOLDER_ID, id);
            folderFragment.setArguments(args);
            ft.addToBackStack(null);
            ft.add(R.id.frameLayout, folderFragment);
            ft.commit();

        }

        initBreadCrumb(folderName, id);
    }

    public void initBreadCrumb(String folderName, String id) {
        breadCrumbRV.setHasFixedSize(true);
        breadCrumbRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        breadCrumbAdapter = new BreadCrumbAdapter(initBreadCrumbList(folderName, id), this);
        breadCrumbAdapter.setBreadCrumbListener(new BreadCrumbAdapter.BreadCrumbListener() {

            @Override
            public void onClick(int count) {
                popUptoPosition(count);
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

    public void replaceFragments(Fragment fragment,String newId, String newName) {
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


}
