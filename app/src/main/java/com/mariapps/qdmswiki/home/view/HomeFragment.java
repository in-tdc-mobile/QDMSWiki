package com.mariapps.qdmswiki.home.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseFragment;
import com.mariapps.qdmswiki.custom.CustomEditText;
import com.mariapps.qdmswiki.custom.CustomProgressBar;
import com.mariapps.qdmswiki.custom.CustomViewPager;
import com.mariapps.qdmswiki.home.adapter.HomeFragmentAdapter;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.search.view.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment {

    @BindView(R.id.searchET)
    CustomEditText searchET;
    @BindView(R.id.slidingTabs)
    TabLayout slidingTabs;
    @BindView(R.id.relMainLL)
    RelativeLayout relMainLL;
    @BindView(R.id.relLL)
    RelativeLayout relLL;
    @BindView(R.id.noDataLL)
    LinearLayout noDataLL;
    @BindView(R.id.customProgressBar)
    CustomProgressBar customProgressBar;
    @BindView(R.id.overViewLL)
    LinearLayout overViewLL;
//    @BindView(R.id.pullToRefresh)
//    SwipeRefreshLayout pullToRefresh;
    @BindView(R.id.viewPager)
    CustomViewPager viewPager;

    private HomeFragmentAdapter homeFragmentAdapter;
    private FragmentManager fragmentManager;
    private RecommendedFragment recommendedFragment;
    private RecentlyFragment recentlyFragment;

    @Override
    protected void setUpPresenter() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ButterKnife.bind(this, view);
        fragmentManager = getFragmentManager();
        customProgressBar.setVisibility(View.GONE);

        searchET.setFocusable(false);
        searchET.clearFocus();

        setupViewPager();
        slidingTabs.setupWithViewPager(viewPager);
        return view;
    }

    @OnClick({R.id.searchET})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchET:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                this.startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up,R.anim.slide_down);

                break;
        }
    }

    private void setupViewPager() {

        if (getActivity() != null) {
            homeFragmentAdapter = new HomeFragmentAdapter(getChildFragmentManager());
        }
        recommendedFragment = new RecommendedFragment();
//        recommendedFragment.setCommentsListener(new CommentsFragment.CommentsListener() {
//            @Override
//            public void onItemClicked(CommentsAndPublishedRespObj.AlertInfoEntity alertInfoEntity) {
//                notificationListener.onCommentClicked(alertInfoEntity);
//            }
//        });
        recentlyFragment = new RecentlyFragment();
//        publishedFragment.setPublishedListener(new PublishedFragment.PublishedListener() {
//            @Override
//            public void onItemClicked(CommentsAndPublishedRespObj.AlertInfoEntity alertInfoEntity) {
//                notificationListener.onPublishedClicked(alertInfoEntity);
//            }
//        });

        homeFragmentAdapter.addFrag(recommendedFragment, "Recommended");
        homeFragmentAdapter.addFrag(recentlyFragment, "Recently Added");
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(homeFragmentAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int newPos) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    public void updateRecommendedList(List<DocumentModel> documentList) {
        recommendedFragment.updateDocumentList(documentList);
    }

    public void updateRecentlyList(List<DocumentModel> documentList) {
        recentlyFragment.updateRecentlyList(documentList);
    }
}
