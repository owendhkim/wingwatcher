package com.example.wingwatcher;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class userInfo {
    private static String email;
    private static String username;
    private static int privilege;
    private static JSONArray birdTrackingInfo;
    private static JSONObject analytics;

    public static void setUserDetails(Context context, String email, String username, int privilege, JSONArray birdTrackingInfo, JSONObject analytics) {
        userInfo.email = email;
        userInfo.username = username;
        userInfo.privilege = privilege;
        userInfo.birdTrackingInfo = birdTrackingInfo;
        userInfo.analytics = analytics;
    }

    public static String getEmail() {
        return email;
    }
    public static String getUsername() {
        return username;
    }

    public static int getPrivilege() {
        return privilege;
    }

    public static JSONArray getBirdTrackingInfo() {
        return birdTrackingInfo;
    }

    public static JSONObject getAnalytics() {
        return analytics;
    }

    public void setPrivilege(int n){
        privilege = n;
    }
}

