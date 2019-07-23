package com.mariapps.qdmswiki.home.view;

import android.app.ActivityOptions;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseFragment;
import com.mariapps.qdmswiki.custom.CustomEditText;
import com.mariapps.qdmswiki.custom.CustomProgressBar;
import com.mariapps.qdmswiki.custom.CustomViewPager;
import com.mariapps.qdmswiki.home.adapter.HomeFragmentAdapter;
import com.mariapps.qdmswiki.search.view.SearchActivity;

import java.util.ArrayList;

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
    ViewPager viewPager;

    private FragmentManager fragmentManager;
    private HomeFragmentAdapter homeFragmentAdapter;

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

        setData();
        return view;
    }

    @OnClick({R.id.searchET})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchET:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_up, R.anim.slide_down).toBundle();
                this.startActivity(intent,bundle);
                break;
        }
    }

    private void setData() {
        ArrayList<String> headingList = new ArrayList<>();
        headingList.add("Recommended");
        headingList.add("Recently Viewed");
        homeFragmentAdapter = new HomeFragmentAdapter(fragmentManager, getActivity(), headingList);
        viewPager.setAdapter(homeFragmentAdapter);
        slidingTabs.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
        viewPager.getAdapter().notifyDataSetChanged();
        viewPager.invalidate();
    }

}
