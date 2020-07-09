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
    private static final String KEY_IS_SEAFARER_LOGIN = "isSeafarerLogin";
    private static final String KEY_LAST_UPDATED_FILE_NAME = "keylastupdatedfilename";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    private static final String IS_DASHBOARD_SEARCH_SHOWN = "IsDashboardSearchShown";
    private static final String IS_DASHBOARD_MENU_SHOWN = "IsDashboardMenuShown";
    private static final String IS_DASHBOARD_TAB_SHOWN = "IsDashboardTabShown";
    private static final String IS_DOCUMENT_VIEW_SHOWN = "IsDocumentViewShown";
    private static final String IS_DOCUMENT_SEARCH_SHOWN = "IsDocumentSearchShown";
    private static final String IS_DOCUMENT_INFO_SHOWN = "IsDocumentInfoShown";

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

    public void seturlno(String urlno){
        editor.putString("urlno", urlno);
        editor.commit();
    }

    public void addurlno(){
        int num=Integer.parseInt(pref.getString("urlno", "0"));
        num++;
        seturlno(num+"");
    }

    public Integer geturlno(){
        return  Integer.parseInt(pref.getString("urlno", "0"));
    }


    public String getUserId() {
        return pref.getString(KEY_USER_ID, "");
    }

    public String getJsonError(){
        return pref.getString("jsonerror","");
    }

    public void putJsonError(String msg){
        editor.putString("jsonerror", msg);
        editor.commit();
    }


    public String getisFirst(){
        return pref.getString("isfirst","");
    }

    public void putisFirst(String msg){
        editor.putString("isfirst", msg);
        editor.commit();
    }




    public String getisSend(){
        return pref.getString("issend","");
    }

    public void putisSend(String msg){
        editor.putString("issend", msg);
        editor.commit();
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

    public void setUserEmail(String username) {
        editor.putString("UserEmail", username);
        editor.commit();
    }

    public String getLoginName() {
        return pref.getString(KEY_LOGIN_NAME, "");
    }

    public String getLoginUserEmail() {
        return pref.getString("UserEmail", "");
    }

    public void setLoginName(String loginName) {
        editor.putString(KEY_LOGIN_NAME, loginName);
        editor.commit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public void setDashboardSearchShown(boolean isDashboardSearchShown) {
        editor.putBoolean(IS_DASHBOARD_SEARCH_SHOWN, isDashboardSearchShown);
        editor.commit();
    }

    public void setDashboardMenuShown(boolean isDashboardMenuShown) {
        editor.putBoolean(IS_DASHBOARD_MENU_SHOWN, isDashboardMenuShown);
        editor.commit();
    }

    public void setDashboardTabShown(boolean isDashboardTabShown) {
        editor.putBoolean(IS_DASHBOARD_TAB_SHOWN, isDashboardTabShown);
        editor.commit();
    }

    public void setDocumentViewShown(boolean isDocumentViewShown) {
        editor.putBoolean(IS_DOCUMENT_VIEW_SHOWN, isDocumentViewShown);
        editor.commit();
    }

    public void setDocumentSearchShown(boolean isDocumentSearchShown) {
        editor.putBoolean(IS_DOCUMENT_SEARCH_SHOWN, isDocumentSearchShown);
        editor.commit();
    }

    public void setDocumentInfoShown(boolean isDocumentInfoShown) {
        editor.putBoolean(IS_DOCUMENT_INFO_SHOWN, isDocumentInfoShown);
        editor.commit();
    }


    public String getKeyLastUpdatedFileName() {
        return pref.getString(KEY_LAST_UPDATED_FILE_NAME, "");
    }

    public void setKeyLastUpdatedFileName(String lastUpdatedFileName) {
        editor.putString(KEY_LAST_UPDATED_FILE_NAME, lastUpdatedFileName);
        editor.commit();
    }

    public String getKeyIsSeafarerLogin() {
        return pref.getString(KEY_IS_SEAFARER_LOGIN, "");
    }

    public void setKeyIsSeafarerLogin(String isSeafarerLogin) {
        editor.putString(KEY_IS_SEAFARER_LOGIN, isSeafarerLogin);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public boolean getIsDashboardSearchShown() {
        return pref.getBoolean(IS_DOCUMENT_SEARCH_SHOWN, false);
    }

    public boolean getIsDashboardMenuShown() {
        return pref.getBoolean(IS_DASHBOARD_MENU_SHOWN, false);
    }

    public boolean getIsDashboardTabShown() {
        return pref.getBoolean(IS_DASHBOARD_TAB_SHOWN, false);
    }

    public boolean getIsDocumentViewShown() {
        return pref.getBoolean(IS_DOCUMENT_VIEW_SHOWN, false);
    }

    public boolean getIsDocumentSearchShown() {
        return pref.getBoolean(IS_DOCUMENT_SEARCH_SHOWN, false);
    }

    public boolean getIsDocumentInfoShown() {
        return pref.getBoolean(IS_DOCUMENT_INFO_SHOWN, false);
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

    public void removeSessionAll() {
      editor.clear().commit();
    }

}
