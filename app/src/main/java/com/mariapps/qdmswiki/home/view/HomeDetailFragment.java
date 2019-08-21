package com.mariapps.qdmswiki.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.mariapps.qdmswiki.home.adapter.RecommendedRecentlyAdapter;
import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.model.CategoryModel;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.search.view.FolderStructureActivity;

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

public class HomeDetailFragment extends BaseFragment {

    @BindView(R.id.rvDocuments)
    CustomRecyclerView rvDocuments;
    @BindView(R.id.noFilesIV)
    AppCompatImageView noFilesIV;
    @BindView(R.id.noDataRL)
    RelativeLayout noDataRL;

    private RecommendedRecentlyAdapter recommendedRecentlyAdapter;
    private ArrayList<DocumentModel> documentsList = new ArrayList<>();
    private HomeDatabase homeDatabase;
    private List<DocumentModel> recommendedRecentlyModels = new ArrayList<>();
    private String documentType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_details, container, false);
        ButterKnife.bind(this, view);

        rvDocuments.setHasFixedSize(true);
        rvDocuments.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDocuments.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        homeDatabase = HomeDatabase.getInstance(getActivity());
        getRecommendedList();

        return view;
    }

    @Override
    protected void setUpPresenter() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        documentType = getArguments().getString("DOC_TYPE");
    }

    public static HomeDetailFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("DOC_TYPE", type);
        HomeDetailFragment fragment = new HomeDetailFragment();
        fragment.setArguments(args);
        return fragment;

    }

    private void setData(){
        recommendedRecentlyAdapter = new RecommendedRecentlyAdapter(getActivity(), recommendedRecentlyModels);
        rvDocuments.setAdapter(recommendedRecentlyAdapter);

        if (recommendedRecentlyModels.size() > 0) {
            noDataRL.setVisibility(View.GONE);
        } else {
            noDataRL.setVisibility(View.VISIBLE);
        }

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

    }

    public void getRecommendedList() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                recommendedRecentlyModels = homeDatabase.homeDao().getDocuments();
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


}
