package com.mariapps.qdmswiki.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by elby.samson on 24,June,2019
 */
public class ShowCasePreferenceUtil {

    private static final String PREF_NAME = "qdmswikishowcaseview";
    public static final String SEARCH = "search";
    public static final String INLINE_SEARCH = "inlinesearch";
    public static final String MORE = "more";
    public static final String MENU = "menu";
    public static final String RECOMMENDED = "recommended";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;


    public ShowCasePreferenceUtil(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public String getShowCasePref(String showCasePrefName) {
        return pref.getString(showCasePrefName, "");
    }

    public void setShowCaseName(String showCasePrefName) {
        editor.putString(showCasePrefName, showCasePrefName);
        editor.commit();
    }
}
