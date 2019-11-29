package com.mariapps.qdmswiki;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * Created by rio.issac on 5/7/2015.
 */
public class SessionManager {

    private static final String PREF_NAME = "QDMSwiki";
    private static final String KEY_IS_LOGGEDIN = "keyisloggedin";
    private static final String KEY_ID = "keyid";
    private static final String KEY_DEVICE_ID = "keydeiceid";
    private static final String KEY_API_TOKEN = "keyapitoken";
    private static final String KEY_FCM_TOKEN_ID = "keyfcmtokenid";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_USER_ID = "keyuserid";
    private static final String KEY_USER_INFO_ID = "keyuserinfoid";
    private static final String KEY_LOGIN_NAME = "keyloginname";
    private static final String KEY_LAST_UPDATED_FILE_NAME = "keylastupdatedfilename";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    // Shared Preferences
    SharedPreferences pref;
    Editor editor;
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public String getId() {
        return pref.getString(KEY_ID, "");
    }

    public void setId(String id) {
        editor.putString(KEY_ID, id);
        editor.commit();
    }

    public String getUserId() {
        return pref.getString(KEY_USER_ID, "");
    }

    public void setUserInfoId(String id) {
        editor.putString(KEY_USER_INFO_ID, id);
        editor.commit();
    }

    public String getUserInfoId() {
        return pref.getString(KEY_USER_INFO_ID, "");
    }

    public void setUserId(String id) {
        editor.putString(KEY_USER_ID, id);
        editor.commit();
    }

    public void setLoggedin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.commit();
    }

    public void setApiToken(String apiToken) {
        editor.putString(KEY_API_TOKEN, apiToken);
        editor.commit();
    }

    public String getApiToken() {
        return pref.getString(KEY_API_TOKEN, "");
    }

    public void setDeviceId(String deviceId) {
        editor.putString(KEY_DEVICE_ID, deviceId);
        editor.commit();
    }

    public void setKeyFcmTokenId(String fcmTokenId) {
        editor.putString(KEY_FCM_TOKEN_ID, fcmTokenId);
        editor.commit();
    }

    public String getKeyFcmTokenId() {
        return pref.getString(KEY_FCM_TOKEN_ID, "");
    }

    public String getDeviceId() {
        return pref.getString(KEY_DEVICE_ID, "");
    }

    public String getUserName() {
        return pref.getString(KEY_USERNAME, "");
    }

    public void setUserName(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    public String getLoginName() {
        return pref.getString(KEY_LOGIN_NAME, "");
    }

    public void setLoginName(String loginName) {
        editor.putString(KEY_LOGIN_NAME, loginName);
        editor.commit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }


    public String getKeyLastUpdatedFileName() {
        return pref.getString(KEY_LAST_UPDATED_FILE_NAME, "");
    }

    public void setKeyLastUpdatedFileName(String lastUpdatedFileName) {
        editor.putString(KEY_LAST_UPDATED_FILE_NAME, lastUpdatedFileName);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setLoginParams(boolean isLoggedIn, String id, String fcmtoken, String apitoken, String name, String loginName) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_API_TOKEN, apitoken);
        editor.putString(KEY_FCM_TOKEN_ID, fcmtoken);
        editor.putString(KEY_USERNAME, name);
        editor.putString(KEY_LOGIN_NAME, loginName);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public void removeSession() {
        setId("");
        setApiToken("");
        setLoggedin(false);
        setDeviceId("");
        setUserName("");
        setLoginName("");
        setUserId("");
        setUserInfoId("");
    }

}
