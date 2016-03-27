package com.example.kim6515516.utils;

/**
 * Created by kim6515516 on 2016-03-27.
 * 싱글톤 패턴 적용
 */
public class ApplicationConstants {
    public static ApplicationConstants instance =  new ApplicationConstants() ;
    public final static String TAG = "KAKATEST";
    public static boolean DEBUG;
    public final static String DOMAIN_URL ="http://www.gettyimagesgallery.com";

    private ApplicationConstants(){
        DEBUG = true;
    }
    public static ApplicationConstants getInstance(){
        if (instance == null)
            instance = new ApplicationConstants();
        return instance;
    }

    public static final int NOTI_CHANGE_DATA = 1;
    public static final int NOTI_PROGRESS_VISIBLE = 2;
    public static final int NOTI_PROGRESS_INVISIBLE = 3;
}
