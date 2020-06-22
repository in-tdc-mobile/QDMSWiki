package com.mariapps.qdmswiki;


import android.arch.lifecycle.MutableLiveData;



public class AppConfig {
    //public static final String BASE_URL = "http://10.201.1.19:8899/QDMSMobileService/api/";//DEV
   //public static final String BASE_URL = "http://10.201.1.164/QDMSMobileService/api/";//QA
    public static final String BASE_URL = "https://mobapp-seafarer.bs-shipmanagement.com/QDMSMobileService/api/";  //PROD
 //   public static final String BASE_URL =   "http://mapps-qa-iis-v4/QDMSMobileService/api/";
    //public static final String BASE_URL = "https://pal4uat.bs-shipmanagement.com/QDMSMobileService/api/";//UAT
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
    public static SingleLiveEvent<String> dwnldstarted=null;
    public static  SingleLiveEvent<Integer> dwnldprgress=null;
    public static  SingleLiveEvent<String> dwnldcmplted=null;
    public static  SingleLiveEvent<String> dwnlderror=null;

    public static  SingleLiveEvent<String> insertstarted=null;
    public static  SingleLiveEvent<String> insertcompletedall=null;
    public static  SingleLiveEvent<String> insertprogress=null;
    public static  SingleLiveEvent<String> insertcompletedonce=null;


    public static SingleLiveEvent<String> getInsertcompletedonce() {
        if(insertcompletedonce==null){
            insertcompletedonce=new SingleLiveEvent<>();
        }
        return insertcompletedonce;
    }

    public static SingleLiveEvent<String> getInsertstarted() {
        if(insertstarted==null){
            insertstarted=new SingleLiveEvent<>();
        }

        return insertstarted;
    }

    public static SingleLiveEvent<String> getInsertcompletedall() {
        if(insertcompletedall==null){
            insertcompletedall=new SingleLiveEvent<>();
        }
        return insertcompletedall;
    }

    public static SingleLiveEvent<String> getInsertprogress() {
        if(insertprogress==null){
            insertprogress=new SingleLiveEvent<>();
        }
        return insertprogress;
    }

    public static SingleLiveEvent<String> getDwnldstarted() {
        if(dwnldstarted==null){
            dwnldstarted = new SingleLiveEvent<>();
        }
        return dwnldstarted;
    }

    public static SingleLiveEvent<Integer> getDwnldprgress() {
        if(dwnldprgress==null){
            dwnldprgress = new SingleLiveEvent<>();
        }
        return dwnldprgress;
    }

    public static SingleLiveEvent<String> getDwnldcmplted() {
        if(dwnldcmplted==null){
            dwnldcmplted = new SingleLiveEvent<>();
        }
        return dwnldcmplted;
    }

    public static SingleLiveEvent<String> getDwnlderror() {
        if(dwnlderror==null){
            dwnlderror = new SingleLiveEvent<>();
        }
        return dwnlderror;
    }














}

