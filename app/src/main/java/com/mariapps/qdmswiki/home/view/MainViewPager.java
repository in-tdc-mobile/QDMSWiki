package com.mariapps.qdmswiki.home.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.mariapps.qdmswiki.articles.view.ArticlesFragment;
import com.mariapps.qdmswiki.documents.view.DocumentsFragment;

import java.util.ArrayList;

public class MainViewPager extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private Fragment currentFragment;
    public int fragCount=0;

    private MainVPListener mainVPListener;
    HomeFragment homeFragment;
    DocumentsFragment documentsFragment;
    ArticlesFragment articlesFragment;


    public MainViewPager(FragmentManager fm) {
        super(fm);

        fragments.clear();
        //Home Fragment

        homeFragment=new HomeFragment();

        fragments.add(homeFragment);

        //Documents Fragment
        documentsFragment = new DocumentsFragment();
        fragments.add(documentsFragment);

        //Articles Fragment
        articlesFragment = new ArticlesFragment();
        fragments.add(articlesFragment);

    }
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        if(fragCount==0){
            return fragments.size();
        }else {
            return fragCount;
        }

    }

    public void setCount(int count){
        fragCount=count;
        notifyDataSetChanged();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            currentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);

    }

    public int getItemPosition(Object object){
        return POSITION_NONE;
    }

    /**
     * Get the current fragment
     */
    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    public void setMainVPListener(MainVPListener mainVPListener){
        this.mainVPListener = mainVPListener;
    }

    public interface MainVPListener {

    }

}



