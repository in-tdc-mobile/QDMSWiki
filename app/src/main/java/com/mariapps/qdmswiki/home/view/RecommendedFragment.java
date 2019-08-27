package com.mariapps.qdmswiki.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.SessionManager;
import com.mariapps.qdmswiki.baseclasses.BaseFragment;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.home.adapter.RecommendedRecentlyAdapter;
import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.model.CategoryModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.model.TagModel;
import com.mariapps.qdmswiki.search.view.FolderStructureActivity;
import com.mariapps.qdmswiki.usersettings.UserInfoModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsCategoryModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsModel;
import com.mariapps.qdmswiki.usersettings.UserSettingsTagModel;
import com.mariapps.qdmswiki.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class RecommendedFragment extends BaseFragment {

    @BindView(R.id.rvDocuments)
    CustomRecyclerView rvDocuments;
    @BindView(R.id.noFilesIV)
    AppCompatImageView noFilesIV;
    @BindView(R.id.noDataRL)
    RelativeLayout noDataRL;

    private RecommendedRecentlyAdapter recommendedRecentlyAdapter;
    private HomeDatabase homeDatabase;
    private List<DocumentModel> recommendedRecentlyModels = new ArrayList<>();

    @Override
    protected void setUpPresenter() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommended, container, false);
        ButterKnife.bind(this, view);
        rvDocuments.setHasFixedSize(true);
        rvDocuments.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDocuments.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        homeDatabase = HomeDatabase.getInstance(getActivity());
        getRecommendedList();

        return view;
    }

    private void setData(){

        if (recommendedRecentlyModels.size() > 0) {
            noDataRL.setVisibility(View.GONE);
            recommendedRecentlyAdapter = new RecommendedRecentlyAdapter(getActivity(), recommendedRecentlyModels);
            rvDocuments.setAdapter(recommendedRecentlyAdapter);

            recommendedRecentlyAdapter.setRowClickListener(new RecommendedRecentlyAdapter.RowClickListener() {
                @Override
                public void onItemClicked(DocumentModel documentModel) {
                    Intent intent = new Intent(getActivity(), FolderStructureActivity.class);
                    intent.putExtra(AppConfig.BUNDLE_TYPE,"Document");
                    intent.putExtra(AppConfig.BUNDLE_FOLDER_NAME,documentModel.getDocumentName());
                    intent.putExtra(AppConfig.BUNDLE_FOLDER_ID,documentModel.getId());
                    startActivity(intent);

                }
            });

        } else {
            noDataRL.setVisibility(View.VISIBLE);
        }



    }

    public void getRecommendedList() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                recommendedRecentlyModels = homeDatabase.homeDao().getRecommendedDocuments();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                setData();
            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void updateDocumentList(List<DocumentModel> documentsList) {
        this.recommendedRecentlyModels = documentsList;
        setData();
    }
}

