package com.mariapps.qdmswiki.home.view;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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
import com.mariapps.qdmswiki.custom.taptargetview.TapTarget;
import com.mariapps.qdmswiki.custom.taptargetview.TapTargetView;
import com.mariapps.qdmswiki.home.adapter.HomeFragmentAdapter;
import com.mariapps.qdmswiki.home.database.HomeDatabase;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.model.RecentlyViewedModel;
import com.mariapps.qdmswiki.search.view.SearchActivity;
import com.mariapps.qdmswiki.utils.ShowCasePreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends BaseFragment {

    @BindView(R.id.searchET)
    CustomEditText searchET;
    @BindView(R.id.slidingTabs)
    TabLayout slidingTabs;
    @BindView(R.id.relMainLL)
    RelativeLayout relMainLL;
    @BindView(R.id.relLL)
    RelativeLayout relLL;
    //    @BindView(R.id.customProgressBar)
//    CustomProgressBar customProgressBar;
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
    private List<RecentlyViewedModel> recentlyViewedList;
    private List<DocumentModel> recommendedList = new ArrayList<>();
    private HomeDatabase homeDatabase;
    private ShowCasePreferenceUtil util;
    private ClickListener clickListener;

    @Override
    protected void setUpPresenter() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ButterKnife.bind(this, view);
        util = new ShowCasePreferenceUtil(getActivity());
        fragmentManager = getFragmentManager();
        homeDatabase = HomeDatabase.getInstance(getActivity());

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
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.slide_down);

                break;
        }
    }

    private void setupViewPager() {

        if (getActivity() != null) {
            homeFragmentAdapter = new HomeFragmentAdapter(getChildFragmentManager());
        }
        recommendedFragment = new RecommendedFragment();
//        recommendedFragment.setR(new CommentsFragment.CommentsListener() {
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
        homeFragmentAdapter.addFrag(recentlyFragment, "Recently Viewed");
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

    public void updateRecentlyList(List<RecentlyViewedModel> recentlyViewedList) {
        recentlyFragment.updateRecentlyList(recentlyViewedList);
    }

    @SuppressLint("ResourceType")
    public void initShowCase(ShowCasePreferenceUtil util) {
        util.setShowCaseName(ShowCasePreferenceUtil.SEARCH);
        TapTargetView.showFor(getActivity(),
                TapTarget.forView(searchET, "Search here for documents/articles/folders", "")
                        // All options below are optional
                        .outerCircleColor(R.color.searchHint)
                        .outerCircleAlpha(0.90f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(18)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(10)
                        .descriptionTextColor(R.color.white)
                        .textColor(R.color.white)
                        .textTypeface(Typeface.SANS_SERIF)
                        .drawShadow(true)
                        .cancelable(true)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(40),
                new TapTargetView.Listener() {
                    @Override
                    public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                        super.onTargetDismissed(view, userInitiated);
                    }
                });
    }

    public interface ClickListener {
        void onInitDashBoard();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

}
