package com.example.note.seoulddok;

import com.example.note.seoulddok.DB.DBManager;

/**
 * Created by gyun_home on 2018-03-18.
 */

public class Contact {

    public static String MAPMOVE = "MAPMOVE";
    public static String MOVE_EMER = "MOVE_EMER";
    public static String STOPCLIENT = "STOPCLIENT";
    public static String STARTCLIENT = "STARTCLIENT";

    //public static String connectIp = "223.194.134.162";
    //public static String connectIp = "192.168.0.2";
    //public static String connectIp = "192.168.0.18";
    //public static String connectIp = "192.168.0.7";
    public static String connectIp = "192.168.0.9";
    //public static String connectIp = "192.168.0.18";
    public static DBManager dbManager;

    public static String loca_gu="";
    public static String loca_dong="";

    public static String ClientId="paho1526978748858000000";

    public static boolean isSensor = true;

    public static final String[] PERMISSIONS = {
            "android.permission.MODIFY_AUDIO_SETTINGS",
            "android.permission.ACCESS_NETWORK_STATE",
            "android.permission.CHANGE_WIFI_STATE",
            "android.permission.ACCESS_WIFI_STATE",
            "android.permission.READ_PHONE_STATE",
            "android.permission.INTERNET",
            "android.permission.ACCESS_NETWORK_STATE",
            "android.permission.CHANGE_WIFI_STATE",
            "android.permission.ACCESS_WIFI_STATE",
            "android.permission.READ_PHONE_STATE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION",
            "com.google.android.providers.gsf.permission.READ_GSERVICES"
    };
}
