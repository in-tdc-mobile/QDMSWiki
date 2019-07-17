package com.mariapps.qdmswiki.home.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mariapps.qdmswiki.home.view.HomeDetailFragment;

import java.util.ArrayList;

public class HomeFragmentAdapter extends FragmentStatePagerAdapter {

    Context mContext;
    FragmentManager mFragman;
    ArrayList<String> documentTypes;

    public HomeFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public HomeFragmentAdapter(FragmentManager fragman, Context context, ArrayList<String> types) {
        super(fragman);
        mContext =  context;
        documentTypes = types;
        mFragman = fragman;
    }

    @Override
    public Fragment getItem(int position) {
        return  HomeDetailFragment.newInstance();
    }

    @Override
    public int getCount() {
        return documentTypes.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return documentTypes.get(position);

    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}

