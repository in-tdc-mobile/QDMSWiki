package com.mariapps.qdmswiki.serviceclasses;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mariapps.qdmswiki.SessionManager;

/**
 * Created by aruna.ramakrishnan 08/26/2019.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    SessionManager sessionManager;

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sessionManager = new SessionManager(getApplicationContext());
        System.out.println("refreshed token" + refreshedToken);
        sessionManager.setKeyFcmTokenId(refreshedToken);
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/allUsers");
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/allAndroid");
        // TODO: Implement this method to send any registration to your app's servers.

    }
}
