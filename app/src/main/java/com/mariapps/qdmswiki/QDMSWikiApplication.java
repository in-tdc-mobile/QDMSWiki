package com.mariapps.qdmswiki;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import com.mariapps.qdmswiki.serviceclasses.QDMSWikiNetworkService;
import com.mariapps.qdmswiki.utils.QDMSWikiNetworkReceiver;

public class QDMSWikiApplication extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {

    public static Activity mActivity;
    private static QDMSWikiApplication mInstance;
    private QDMSWikiNetworkService networkService;
    private QDMSWikiNetworkReceiver qdmsWikiNetworkReceiver;
    boolean isRegistered = false;
    private int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;
    private SessionManager sessionManager;
    private boolean isConnectionLost=false;


    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        mInstance = this;
        networkService = new QDMSWikiNetworkService();
    }

    public static QDMSWikiApplication getApplicationInstance() {
        return mInstance;
    }

    public static QDMSWikiNetworkService getInstance() {
        return QDMSWikiApplication.Loader.FACTORY_SINGLETON;
    }

    public static Context getContext() {
        return mInstance;
    }

    public static Activity getCurrentActivity() {
        return mActivity;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }



    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mActivity=activity;
       /* Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));*/
        qdmsWikiNetworkReceiver = new QDMSWikiNetworkReceiver();
        final IntentFilter intentFilter = new IntentFilter("YourAction");
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, intentFilter);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        mActivity = activity;
        if (++activityReferences == 1 && !isActivityChangingConfigurations) {
            // App enters foreground
            sessionManager=new SessionManager(mActivity);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        mActivity = activity;
        registerNetworkBroadcastForNougat();
        isRegistered = true;

    }

    @Override
    public void onActivityPaused(Activity activity) {
        mActivity = null;
        if (isRegistered) {
        unregisterReceiver(qdmsWikiNetworkReceiver);
            isRegistered = false;
        }
    }



    @Override
    public void onActivityStopped(Activity activity) {
        isActivityChangingConfigurations = activity.isChangingConfigurations();
        if (--activityReferences == 0 && !isActivityChangingConfigurations) {
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    private static class Loader {
        static final QDMSWikiNetworkService FACTORY_SINGLETON = new QDMSWikiNetworkService();
    }

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(qdmsWikiNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }else {
            registerReceiver(qdmsWikiNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean isConnected = intent.getExtras().getBoolean("valueName");

        }
    };


    public static Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(c);
        return bitmap;
    }

}

