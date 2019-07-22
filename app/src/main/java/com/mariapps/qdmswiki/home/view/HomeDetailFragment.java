package com.mariapps.qdmswiki.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseFragment;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.documents.view.DocumentViewActivity;
import com.mariapps.qdmswiki.home.adapter.RecommendedRecentlyAdapter;
import com.mariapps.qdmswiki.home.model.RecommendedRecentlyModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeDetailFragment extends BaseFragment {

    @BindView(R.id.rvDocuments)
    CustomRecyclerView rvDocuments;

    private RecommendedRecentlyAdapter recommendedRecentlyAdapter;
    private ArrayList<RecommendedRecentlyModel> documentsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_details, container, false);
        ButterKnife.bind(this, view);

        rvDocuments.setHasFixedSize(true);
        rvDocuments.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDocuments.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        setData();
        return view;
    }

    @Override
    protected void setUpPresenter() {

    }

    public static HomeDetailFragment newInstance() {
        Bundle args = new Bundle();
        HomeDetailFragment fragment = new HomeDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void setData(){
        documentsList.add(new RecommendedRecentlyModel(1, "General Data Protection Manual","GDPR Manual","V1"));
        documentsList.add(new RecommendedRecentlyModel(2, "ECDIS Manual","Information Technology","V2"));
        documentsList.add(new RecommendedRecentlyModel(3, "ISO Ethical Ship Operations Policy","Ethical Ship Operations Policy","V1"));
        documentsList.add(new RecommendedRecentlyModel(4, "Safety Management Manual Appendix","Safety Management Manual","V1"));
        documentsList.add(new RecommendedRecentlyModel(5, "LPG Carrier Manual","LPG Carrier Manual","V2"));
        documentsList.add(new RecommendedRecentlyModel(6, "General Data Protection Manual","GDPR Manual","V1"));
        documentsList.add(new RecommendedRecentlyModel(7, "ECDIS Manual","Information Technology","V2"));

        recommendedRecentlyAdapter = new RecommendedRecentlyAdapter(getActivity(), documentsList);
        rvDocuments.setAdapter(recommendedRecentlyAdapter);

        recommendedRecentlyAdapter.setRowClickListener(new RecommendedRecentlyAdapter.RowClickListener() {
            @Override
            public void onItemClicked(RecommendedRecentlyModel recommendedRecentlyModel) {
                Intent intent = new Intent(getActivity(), DocumentViewActivity.class);
                startActivity(intent);

            }
        });

    }
}
