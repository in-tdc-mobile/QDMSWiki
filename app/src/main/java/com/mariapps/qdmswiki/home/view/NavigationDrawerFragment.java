package com.mariapps.qdmswiki.home.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.mariapps.qdmswiki.SessionManager;
import com.mariapps.qdmswiki.baseclasses.BaseFragment;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.home.adapter.CustomNavigationAdapter;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.model.NavDrawerObj;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavigationDrawerFragment extends BaseFragment {

    @BindView(R.id.headingTV)
    CustomTextView headingTV;
    @BindView(R.id.linearProfile)
    LinearLayout linearProfile;
    @BindView(R.id.navigationRV)
    CustomRecyclerView navigationRV;
    @BindView(R.id.navRL)
    RelativeLayout navRL;

    private SessionManager sessionManager;
    private CustomNavigationAdapter customNavigationAdapter;
    private NavDrawerObj navDrawerObj;
    private NavigationListener navigationListener;
    private List<DocumentModel> folderList = new ArrayList<>();

    @Override
    protected void setUpPresenter() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nav_drawer, container, false);
        ButterKnife.bind(this, view);
        sessionManager = new SessionManager(getActivity());

        if (getArguments() != null) {
            folderList = (List<DocumentModel>) getArguments().getSerializable(AppConfig.BUNDLE_NAV_DRAWER);
            if (folderList != null) {
                initRecycler(folderList);
            }
        }

        return view;
    }

    private void initRecycler(List<DocumentModel> folderList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        navigationRV.setLayoutManager(mLayoutManager);
        navigationRV.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        customNavigationAdapter = new CustomNavigationAdapter(getActivity(), folderList);
        customNavigationAdapter.setNavAdapterListener(new CustomNavigationAdapter.NavAdapterListener() {
            @Override
            public void onItemClick(DocumentModel documentModel) {
                navigationListener.onItemClicked(documentModel);
            }
        });
        navigationRV.setAdapter(customNavigationAdapter);
    }

    public interface NavigationListener {
        void onItemClicked(DocumentModel documentModel);
    }

    public void setNavigationListener(NavigationListener navigationListener) {
        this.navigationListener = navigationListener;
    }

}
