package com.mariapps.qdmswiki.home.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseFragment;
import com.mariapps.qdmswiki.custom.CustomTextView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class NavigationDetailFragment extends BaseFragment {

    @BindView(R.id.backBtn)
    AppCompatImageView backBtn;
    @BindView(R.id.headingTV)
    CustomTextView headingTV;
    @BindView(R.id.headingLL)
    LinearLayout headingLL;
    @BindView(R.id.navigationRV)
    RecyclerView navigationRV;
    @BindView(R.id.navRL)
    RelativeLayout navRL;

    @Override
    protected void setUpPresenter() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nav_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

}
