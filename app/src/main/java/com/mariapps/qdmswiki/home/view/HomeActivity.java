package com.mariapps.qdmswiki.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mariapps.qdmswiki.AppConfig;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.baseclasses.BaseActivity;
import com.mariapps.qdmswiki.custom.CustomViewPager;
import com.mariapps.qdmswiki.home.model.NavDrawerObj;
import com.mariapps.qdmswiki.notification.view.NotificationActivity;
import com.mariapps.qdmswiki.search.view.FolderStructureActivity;
import com.mariapps.qdmswiki.settings.view.SettingsActivity;
import com.mariapps.qdmswiki.utils.ScreenUtils;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.frameLayoutOne)
    FrameLayout frameLayoutOne;
    @BindView(R.id.frameLayoutTwo)
    FrameLayout frameLayoutTwo;
    @BindView(R.id.navFL)
    FrameLayout navFL;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    //    @BindView(R.id.loading_spinner)
//    ProgressBar loadingSpinner;
    @BindView(R.id.mainVP)
    CustomViewPager mainVP;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottom_navigation;
    @BindView(R.id.userImageIV)
    AppCompatImageView userImageIV;
    @BindView(R.id.notificationIV)
    AppCompatImageView notificationIV;
    @BindView(R.id.appBarMain)
    AppBarLayout appBarMain;


    private ActionBarDrawerToggle mDrawerToggle;
    private MainViewPager mainViewPager;
    private NavigationDrawerFragment navigationDrawerFragment;
    private ArrayList<NavDrawerObj.MenuItemsEntity> menuItemsEntities;
    private NavDrawerObj.MenuItemsEntity menuItemsEntity;
    private NavDrawerObj navDrawerObj;
    private int currentPosition = 0;
    private int newPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setSupportActionBar(toolbar);
        initNavigationDrawerItems();
        initViewpager();
        initBottomNavigation();
    }

    private void initNavigationDrawerItems() {
        navDrawerObj = new NavDrawerObj();
        menuItemsEntities = new ArrayList<>();
        menuItemsEntities.add(new NavDrawerObj.MenuItemsEntity(1, -1, "ISO AND ISM Manual", "ISO AND ISM Manual", "ISO AND ISM Manual", false, 1, "Folder"));
        menuItemsEntities.add(new NavDrawerObj.MenuItemsEntity(2, -1, "Data Protection", "Data Protection", "Data Protection", false, 2, "Folder"));
        menuItemsEntities.add(new NavDrawerObj.MenuItemsEntity(3, -1, "Shore Procedures", "Shore Procedures", "Shore Procedures", false, 3, "Folder"));
        menuItemsEntities.add(new NavDrawerObj.MenuItemsEntity(4, -1, "Ship Procedures", "Ship Procedures", "Ship Procedures", false, 4, "Folder"));
        menuItemsEntities.add(new NavDrawerObj.MenuItemsEntity(5, -1, "BSM Circulars", "BSM Circulars", "BSM Circulars", false, 5, "Folder"));

        menuItemsEntities.add(new NavDrawerObj.MenuItemsEntity(6, 1, "Ship Manual test", "Ship Manual test", "Ship Manual test", false, 1, "Document"));
        menuItemsEntities.add(new NavDrawerObj.MenuItemsEntity(7, 1, "Draft DOC_QA", "Draft DOC_QA", "Draft DOC_QA", false, 2, "Document"));
        menuItemsEntities.add(new NavDrawerObj.MenuItemsEntity(8, 1, "Normative References", "Normative References", "Normative References", false, 3, "Folder"));
        menuItemsEntities.add(new NavDrawerObj.MenuItemsEntity(9, 1, "Terms and definitions", "Terms and definitions", "Terms and definitions", false, 4, "Folder"));
        menuItemsEntities.add(new NavDrawerObj.MenuItemsEntity(10, 2, "Management System", "Management System", "Management System", false, 5, "Folder"));

        menuItemsEntities.add(new NavDrawerObj.MenuItemsEntity(11, 8, "Draft DOC_QA", "Draft DOC_QA", "Draft DOC_QA", false, 1, "Document"));
        menuItemsEntities.add(new NavDrawerObj.MenuItemsEntity(12, 3, "Data Protection", "Data Protection", "Data Protection", false, 2, "Folder"));
        menuItemsEntities.add(new NavDrawerObj.MenuItemsEntity(13, 3, "Shore Procedures", "Shore Procedures", "Shore Procedures", false, 3, "Folder"));
        menuItemsEntities.add(new NavDrawerObj.MenuItemsEntity(14, 4, "Ship Procedures", "Ship Procedures", "Ship Procedures", false, 4, "Folder"));
        menuItemsEntities.add(new NavDrawerObj.MenuItemsEntity(15, 13, "BSM Circulars", "BSM Circulars", "BSM Circulars", false, 5, "Folder"));

        navDrawerObj.setMenuItemsEntities(menuItemsEntities);

        initNavDrawer();
    }

    @OnClick({R.id.userImageIV, R.id.notificationIV})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userImageIV:
                Intent settingsIntent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.notificationIV:
                Intent notificationsIntent = new Intent(HomeActivity.this, NotificationActivity.class);
                startActivity(notificationsIntent);
                break;
        }
    }

    public void setupFragments(Fragment fragment, boolean addTOstack, boolean doAnimation) {
        try {
            if (fragment != null) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                if (doAnimation)
                    fragmentTransaction.setCustomAnimations(R.anim.enter_right, R.anim.exit_left);
                fragmentTransaction.replace(R.id.navFL, fragment);
                if (addTOstack) fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        } catch (IllegalStateException e) {
            Log.d("IllegalStateException", "Exception", e);
        }

    }

    private void initNavDrawer() {
        ViewGroup.LayoutParams layoutParams = navFL.getLayoutParams();
        layoutParams.width = (ScreenUtils.getScreenWidth(this) * 3) / 4;
        navFL.setLayoutParams(layoutParams);


        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset == 0) {
                    // drawer closed
                    setupFragments(findFragmentById(AppConfig.FRAG_NAV_DRAWER), false, false);
                }
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else
                    drawerLayout.openDrawer((int) Gravity.START);
            }

        });

        drawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_menu);
        mDrawerToggle.syncState();
        setupFragments(findFragmentById(AppConfig.FRAG_NAV_DRAWER), false, false);


    }

    private void initBottomNavigation() {
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case 0:
                        mainVP.setCurrentItem(0);
                        return true;
                    case 1:
                        mainVP.setCurrentItem(1);
                        return true;
                    case 2:
                        mainVP.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });

        bottom_navigation.setItemIconTintList(null);
        bottom_navigation.getMenu().clear();
        Menu menu = bottom_navigation.getMenu();
        menu.add(Menu.NONE, 0, Menu.NONE, "HOME")
                .setIcon(R.drawable.drawable_home_selecter);

        menu.add(Menu.NONE, 1, Menu.NONE, "DOCUMENTS")
                .setIcon(R.drawable.drawable_document_selector);

        menu.add(Menu.NONE, 2, Menu.NONE, "ARTICLES")
                .setIcon(R.drawable.drawable_article_selector);
    }

    @Override
    protected void setUpPresenter() {

    }

    @Override
    protected void isNetworkAvailable(boolean isConnected) {

    }

    private void initViewpager() {
        mainViewPager = new MainViewPager(getSupportFragmentManager());
        mainViewPager.setCount(3);


        mainVP.setOffscreenPageLimit(3);
        mainVP.setAdapter(mainViewPager);
        mainVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int newPos) {
                newPosition = newPos;
                bottom_navigation.setSelectedItemId(newPosition);
                currentPosition = newPosition;

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public Fragment findFragmentById(String key) {

        switch (key) {
            case AppConfig.FRAG_NAV_DRAWER:
                navigationDrawerFragment = new NavigationDrawerFragment();
                Bundle bundlenavDrawer = new Bundle();
                bundlenavDrawer.putSerializable(AppConfig.BUNDLE_NAV_DRAWER, (Serializable) navDrawerObj);
                navigationDrawerFragment.setNavigationListener(new NavigationDrawerFragment.NavigationListener() {
                    @Override
                    public void onItemClicked(NavDrawerObj.MenuItemsEntity menuEntity) {
                        menuItemsEntity = menuEntity;
                        setupFragments(findFragmentById(AppConfig.FRAG_NAV_DETAILS_DRAWER), true, true);
                    }
                });

                navigationDrawerFragment.setArguments(bundlenavDrawer);
                return navigationDrawerFragment;
            case AppConfig.FRAG_NAV_DETAILS_DRAWER:
                NavigationDetailFragment navigationDetailFragment = new NavigationDetailFragment();
                navigationDetailFragment.setNavigationDetailListener(new NavigationDetailFragment.NavigationDetailListener() {
                    @Override
                    public void onItemClicked(NavDrawerObj.MenuItemsEntity menuEntity) {
                        menuItemsEntity = menuEntity;
                        if(menuItemsEntity.getType().equals("Folder"))
                            setupFragments(findFragmentById(AppConfig.FRAG_NAV_DETAILS_DRAWER), true, true);
                        else
                        {
                            Intent intent = new Intent(HomeActivity.this, FolderStructureActivity.class);
                            intent.putExtra(AppConfig.BUNDLE_TYPE,menuItemsEntity.getType());
                            intent.putExtra(AppConfig.BUNDLE_FOLDER_NAME,"");
                            intent.putExtra(AppConfig.BUNDLE_FOLDER_ID,1);
                            startActivity(intent);
                        }
                    }

                });
                Bundle bundleNavDetail = new Bundle();
                bundleNavDetail.putSerializable(AppConfig.BUNDLE_NAV_DETAILS_OBJECT, (Serializable) menuItemsEntity);
                bundleNavDetail.putSerializable(AppConfig.BUNDLE_NAV_DETAILS_LIST, (Serializable) navDrawerObj);
                navigationDetailFragment.setArguments(bundleNavDetail);
                return navigationDetailFragment;
            default:

                return null;
        }
    }

}

