package com.mcbeengs.imagerdon;

import android.app.Application;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by McBeengs on 01/10/2016.
 */

public class ImagerdonApplication extends Application {

    private static String TAG = "com.mcbeengs.imagerdon";
    private static ImagerdonApplication instance;
    private static Map<String, Boolean> mapUpdate = new HashMap<>();

    @Override
    public void onCreate() {
        Log.d(TAG, "com.mcbeengs.imagerdon.ImagerdonApplication.onCreate();");
        instance = this;
    }

    public static ImagerdonApplication getInstance() {
        return instance;
    }

    public void setNewUpdate(String s, boolean b) {
        mapUpdate.put(s, b);
    }

    public boolean isUpdateNeeded(String s) {
        if (mapUpdate.containsKey(s)) {
            return mapUpdate.remove(s);
        }
        return false;
    }
}
