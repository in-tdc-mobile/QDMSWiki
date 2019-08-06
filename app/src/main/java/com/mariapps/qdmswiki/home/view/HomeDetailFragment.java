package com.mariapps.qdmswiki.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseFragment;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.home.adapter.RecommendedRecentlyAdapter;
import com.mariapps.qdmswiki.home.model.RecommendedRecentlyModel;
import com.mariapps.qdmswiki.search.view.FolderStructureActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class HomeDetailFragment extends BaseFragment {

    @BindView(R.id.rvDocuments)
    CustomRecyclerView rvDocuments;
    @BindView(R.id.noFilesIV)
    AppCompatImageView noFilesIV;
    @BindView(R.id.noDataRL)
    RelativeLayout noDataRL;

    private RecommendedRecentlyAdapter recommendedRecentlyAdapter;
    private ArrayList<RecommendedRecentlyModel> documentsList = new ArrayList<>();
    private String documentType;

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

        if(documentType.equals("Recently Viewed"))
            documentsList = new ArrayList<>();
        else {
            documentsList.add(new RecommendedRecentlyModel(1, "General Data Protection Manual", "GDPR Manual", "V1"));
            documentsList.add(new RecommendedRecentlyModel(2, "ECDIS Manual", "Information Technology", "V2"));
            documentsList.add(new RecommendedRecentlyModel(3, "ISO Ethical Ship Operations Policy", "Ethical Ship Operations Policy", "V1"));
            documentsList.add(new RecommendedRecentlyModel(4, "Safety Management Manual Appendix", "Safety Management Manual", "V1"));
            documentsList.add(new RecommendedRecentlyModel(5, "LPG Carrier Manual", "LPG Carrier Manual", "V2"));
            documentsList.add(new RecommendedRecentlyModel(6, "General Data Protection Manual", "GDPR Manual", "V1"));
            documentsList.add(new RecommendedRecentlyModel(7, "ECDIS Manual", "Information Technology", "V2"));
        }

        recommendedRecentlyAdapter = new RecommendedRecentlyAdapter(getActivity(), documentsList);
        rvDocuments.setAdapter(recommendedRecentlyAdapter);

        if (documentsList.size() > 0) {
            noDataRL.setVisibility(View.GONE);
        } else {
            noDataRL.setVisibility(View.VISIBLE);
        }

        recommendedRecentlyAdapter.setRowClickListener(new RecommendedRecentlyAdapter.RowClickListener() {
            @Override
            public void onItemClicked(RecommendedRecentlyModel recommendedRecentlyModel) {
                Intent intent = new Intent(getActivity(), FolderStructureActivity.class);
                intent.putExtra(AppConfig.BUNDLE_TYPE,"Document");
                intent.putExtra(AppConfig.BUNDLE_FOLDER_NAME,recommendedRecentlyModel.getName());
                intent.putExtra(AppConfig.BUNDLE_FOLDER_ID,recommendedRecentlyModel.getId());
                startActivity(intent);

            }
        });

    }


}
