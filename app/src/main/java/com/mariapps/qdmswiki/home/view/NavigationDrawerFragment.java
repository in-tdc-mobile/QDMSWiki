package com.mariapps.qdmswiki.home.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.SessionManager;
import com.mariapps.qdmswiki.baseclasses.BaseFragment;
import com.mariapps.qdmswiki.custom.CustomTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NavigationDrawerFragment extends BaseFragment {

    @BindView(R.id.headingTV)
    CustomTextView headingTV;
    @BindView(R.id.linearProfile)
    LinearLayout linearProfile;
    @BindView(R.id.navigationRV)
    RecyclerView navigationRV;
    @BindView(R.id.navRL)
    RelativeLayout navRL;

    private SessionManager sessionManager;

    @Override
    protected void setUpPresenter() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nav_drawer, container, false);
        ButterKnife.bind(this, view);
        sessionManager = new SessionManager(getActivity());
        return view;
    }

}
