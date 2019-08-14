package com.mariapps.qdmswiki.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import com.mariapps.qdmswiki.QDMSWikiApplication;


/**
 * Created by aruna.ramakrishnan on 14-08-2019
 */

public class QDMSWikiNetworkReceiver extends BroadcastReceiver {
    private final String TAG = "MyNetworkReciver";
    public static boolean isActive=false;
    @Override
    public void onReceive(Context context, Intent intent) {
        Activity activity = QDMSWikiApplication.mActivity ; // Getting Current Activity
        isActive = isOnline(activity);
        if (!isActive) {
            //if internet connection disconnected, then this block exceutes
            //activity is currently running activity
           /* Toast.makeText(activity, "asasa", Toast.LENGTH_SHORT).show();*/
           Intent intents = new Intent("YourAction");
            Bundle bundle = new Bundle();
            bundle.putBoolean("valueName", false);
            intents.putExtras(bundle);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intents);

        }else {
            Intent intents = new Intent("YourAction");
            Bundle bundle = new Bundle();
            bundle.putBoolean("valueName", true);
            intents.putExtras(bundle);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intents);
        }
    }
    //returns internet connection
    public boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            if(netInfo != null && netInfo.isConnected()){
                return true;
            }else {
                return false;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}
