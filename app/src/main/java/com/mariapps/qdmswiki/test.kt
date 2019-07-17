package com.mariapps.qdmswiki

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ProgressBar
import butterknife.BindView
import com.mariapps.qdmswiki.custom.CustomViewPager
import com.mariapps.qdmswiki.home.view.MainViewPager

class test : AppCompatActivity(),View.OnClickListener {

    override fun onClick(v: View?) {

    }

    @BindView(R.id.toolbar)
    internal var toolbar: Toolbar? = null
    @BindView(R.id.navigationView)
    internal var navigationView: NavigationView? = null
    @BindView(R.id.drawer_layout)
    internal var drawerLayout: DrawerLayout? = null
    @BindView(R.id.loading_spinner)
    internal var loadingSpinner: ProgressBar? = null
    @BindView(R.id.mainVP)
    internal var mainVP: CustomViewPager? = null

    private var mDrawerToggle: ActionBarDrawerToggle? = null
    private var mainViewPager: MainViewPager? = null
    private var actionBar: ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun initViewpager() {
        mainViewPager = MainViewPager(supportFragmentManager)
        mainViewPager!!.setCount(3)

        mainVP?.setOffscreenPageLimit(3)
        mainVP?.setAdapter(mainViewPager)
        mainVP?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(i: Int, v: Float, i1: Int) {}

            override fun onPageSelected(newPos: Int) {}

            override fun onPageScrollStateChanged(i: Int) {

            }
        })

    }

}