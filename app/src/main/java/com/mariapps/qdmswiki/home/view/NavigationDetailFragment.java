package com.mariapps.qdmswiki.home.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseFragment;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.home.adapter.CustomNavigationDetailAdapter;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.model.NavDrawerObj;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NavigationDetailFragment extends BaseFragment {

    @BindView(R.id.backBtn)
    AppCompatImageView backBtn;
    @BindView(R.id.headingTV)
    CustomTextView headingTV;
    @BindView(R.id.headingLL)
    LinearLayout headingLL;
    @BindView(R.id.navigationRV)
    CustomRecyclerView navigationRV;
    @BindView(R.id.navRL)
    RelativeLayout navRL;

    private CustomNavigationDetailAdapter customNavigationDetailAdapter;
    private List<DocumentModel> documentList;
    private String folderName;
    private NavigationDetailListener navigationDetailListener;

    @Override
    protected void setUpPresenter() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nav_details, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            documentList = (List<DocumentModel>) getArguments().getSerializable(AppConfig.BUNDLE_NAV_DETAILS_LIST);
            folderName =  getArguments().getString(AppConfig.BUNDLE_FOLDER_NAME);
            if (documentList != null) {
                initRecycler(documentList);
            }
            if (folderName != null) {
                headingTV.setText(folderName);
            }
        }

        return view;
    }

    private void initRecycler(List<DocumentModel> documentList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        navigationRV.setLayoutManager(mLayoutManager);
        navigationRV.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        customNavigationDetailAdapter = new CustomNavigationDetailAdapter(getActivity(), documentList);

        customNavigationDetailAdapter.setNavAdapterListener(new CustomNavigationDetailAdapter.NavAdapterListener() {
            @Override
            public void onItemClick(DocumentModel documentModel) {
                navigationDetailListener.onItemClicked(documentModel);
            }

        });

        navigationRV.setAdapter(customNavigationDetailAdapter);
    }

    @OnClick(R.id.headingLL)
    public void onBackPress() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0) {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        }
        else{
            fm.popBackStack();
        }

    }


    public interface NavigationDetailListener {
        void onItemClicked(DocumentModel documentModel);
    }

    public void setNavigationDetailListener(NavigationDetailListener navigationDetailListener) {
        this.navigationDetailListener = navigationDetailListener;
    }


}
