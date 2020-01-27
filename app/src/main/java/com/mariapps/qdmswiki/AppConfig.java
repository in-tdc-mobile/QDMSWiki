package com.mariapps.qdmswiki;


import android.arch.lifecycle.MutableLiveData;



public class AppConfig {
    //public static final String BASE_URL = "http://10.201.1.19:8899/QDMSMobileService/api/";//DEV
   public static final String BASE_URL = "http://10.201.1.164/QDMSMobileService/api/";//QA
   // public static final String BASE_URL = "https://pal4uat.bs-shipmanagement.com/QDMSMobileService/api/";//UAT
    public static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";

    //Bundles
    public static final String BUNDLE_NAV_DRAWER = "bundlenavdrawer";
    public static final String BUNDLE_PAGE = "bundlepage";
    public static final String BUNDLE_TYPE = "type";
    public static final String BUNDLE_ID= "id";
    public static final String BUNDLE_NAME= "name";
    public static final String BUNDLE_FOLDER_NAME= "foldername";
    public static final String BUNDLE_FOLDER_ID= "folderid";
    public static final String BUNDLE_VERSION = "version";
    public static final String BOOKMARK_ALL = "bookmark";
    public static final String BOOKMARK_ID = "";
    public static final String BUNDLE_NAV_DETAILS_OBJECT = "bundlenavdetailsobject";
    public static final String BUNDLE_NAV_DETAILS_LIST = "bundlenavdetailslist";

    //Fragments
    public static final String FRAG_HOME = "fragmenthome";
    public static final String FRAG_NAV_DRAWER = "fragmentnavdrawer";
    public static final String FRAG_NAV_DETAILS_DRAWER = "fragmentnavdetailsdrawer";
    public static MutableLiveData<String> dwnldstarted=null;
    public static  MutableLiveData<Integer> dwnldprgress=null;
    public static  MutableLiveData<String> dwnldcmplted=null;
    public static  MutableLiveData<String> dwnlderror=null;




    public static MutableLiveData<String> getDwnldstarted() {
        if(dwnldstarted==null){
            dwnldstarted = new MutableLiveData<>();
        }
        return dwnldstarted;
    }

    public static MutableLiveData<Integer> getDwnldprgress() {
        if(dwnldprgress==null){
            dwnldprgress = new MutableLiveData<>();
        }
        return dwnldprgress;
    }

    public static MutableLiveData<String> getDwnldcmplted() {
        if(dwnldcmplted==null){
            dwnldcmplted = new MutableLiveData<>();
        }
        return dwnldcmplted;
    }

    public static MutableLiveData<String> getDwnlderror() {
        if(dwnlderror==null){
            dwnlderror = new MutableLiveData<>();
        }
        return dwnlderror;
    }














}

