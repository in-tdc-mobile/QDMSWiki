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
import com.mariapps.qdmswiki.baseclasses.BaseFragment;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.home.adapter.RecentlyViewedAdapter;
import com.mariapps.qdmswiki.home.adapter.RecommendedAdapter;
import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.model.RecentlyViewedModel;
import com.mariapps.qdmswiki.search.view.FolderStructureActivity;
import com.mariapps.qdmswiki.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class RecentlyFragment extends BaseFragment {

    @BindView(R.id.rvDocuments)
    CustomRecyclerView rvDocuments;
    @BindView(R.id.noFilesIV)
    AppCompatImageView noFilesIV;
    @BindView(R.id.noDataRL)
    RelativeLayout noDataRL;

    private RecentlyViewedAdapter recentlyViewedAdapter;
    private HomeDatabase homeDatabase;
    private List<RecentlyViewedModel> recentlyViewedList = new ArrayList<>();
    private String documentType;

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
        getRecentlyViewedList();

        return view;
    }

    private void setData(){
        if (recentlyViewedList.size() > 0) {
            noDataRL.setVisibility(View.GONE);
            recentlyViewedAdapter = new RecentlyViewedAdapter(getActivity(), recentlyViewedList);
            rvDocuments.setAdapter(recentlyViewedAdapter);

            recentlyViewedAdapter.setRowClickListener(new RecentlyViewedAdapter.RowClickListener() {
                @Override
                public void onItemClicked(RecentlyViewedModel recentlyViewedModel) {
                    Intent intent = new Intent(getActivity(), FolderStructureActivity.class);
                    intent.putExtra(AppConfig.BUNDLE_PAGE,"Recently");
                    intent.putExtra(AppConfig.BUNDLE_TYPE,"Document");
                    intent.putExtra(AppConfig.BUNDLE_NAME,recentlyViewedModel.getDocumentName());
                    intent.putExtra(AppConfig.BUNDLE_FOLDER_NAME,recentlyViewedModel.getCategoryName());
                    intent.putExtra(AppConfig.BUNDLE_ID,recentlyViewedModel.getDocumentId());
                    intent.putExtra(AppConfig.BUNDLE_FOLDER_ID,recentlyViewedModel.getCategoryId());
                    intent.putExtra(AppConfig.BUNDLE_VERSION,recentlyViewedModel.getVersion());
                    startActivity(intent);

                }
            });

        } else {
            noDataRL.setVisibility(View.VISIBLE);
        }
    }

    public void getRecentlyViewedList() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                recentlyViewedList = homeDatabase.homeDao().getRecentlyViewedDocuments();
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

    public void updateRecentlyList(List<RecentlyViewedModel> documentsList) {
        this.recentlyViewedList = documentsList;
        setData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getRecentlyViewedList();
        }
    }
}
