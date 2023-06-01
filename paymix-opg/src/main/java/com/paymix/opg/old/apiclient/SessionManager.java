package com.paymix.opg.old.apiclient;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class SessionManager {

    // General PREF
    private static final String PREFERENCE_NAME = "my_preference.";
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    // Notification Seen PREF
    private static final String PREFERENCE_NAME_NOTIFICATION = "my_preference.notification";
    private SharedPreferences notPref;
    private SharedPreferences.Editor notEditor;



    public SessionManager(Context context) {
        mPref = context.getSharedPreferences(PREFERENCE_NAME + context.getApplicationContext().getPackageName(),
                Context.MODE_PRIVATE);

        notPref = context.getSharedPreferences(PREFERENCE_NAME_NOTIFICATION + context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
    }

    //String-------------------------------------------------------------------------------------------------------
    public void putString(String key, String val) {
        doEdit();
        mEditor.putString(key, val);
        doCommit();
    }

    //Get String
    public String getString(String key) {
        return mPref.getString(key, "");
    }


    // Get String with a default value...
    public String getString(String key, String defaultValue) {
        return mPref.getString(key, defaultValue);
    }


    //int--------------------------------------------------------------------------------------------------------
    public void putInt(String key, int val) {
        doEdit();
        mEditor.putInt(key, val);
        doCommit();
    }

    //Get int
    public int getInt(String key) {
        return mPref.getInt(key, 0);
    }

    //Get int with a default value
    public int getInt(String key, int defaultValue) {
        return mPref.getInt(key, defaultValue);
    }


    //Put double as String.------------------------------------------------------------------------------------------
    public void putDouble(String key, double val) {
        doEdit();
        mEditor.putString(key, String.valueOf(val));
        doCommit();
    }

    //Get double
    public double getDouble(String key, double defaultValue) {
        String result = mPref.getString(key, "");
        return Double.parseDouble(result);
    }

    //put bolean-------------------------------------------------------------------------------------------------------
    public void putBoolean(String key, boolean val) {
        doEdit();
        mEditor.putBoolean(key, val);
        doCommit();
    }


    // Get bolean with a default  value
    public boolean getBoolean(String key, boolean defaultValue) {
        return mPref.getBoolean(key, defaultValue);
    }

    //put ArrayList-----------------------------------------------------------------------------------------
    public void putStringArrayList(String key, ArrayList<String> arrayList) {
        doEdit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        mEditor.putString(key, json);
        doCommit();
    }

    // Get string type arraylist
    public ArrayList<String> getStringArrayList(String key) {
        Gson gson = new Gson();
        String json = mPref.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    //put ArrayList-----------------------------------------------------------------------------------------
    public void putIntegerArrayList(String key, ArrayList<Integer> arrayList) {
        doEdit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        mEditor.putString(key, json);
        doCommit();
    }

    // Get string type arraylist
    public ArrayList<Integer> getIntegerArrayList(String key) {
        Gson gson = new Gson();
        String json = mPref.getString(key, null);
        Type type = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    //put HashMap-----------------------------------------------------------------------------------------
    public void putStringHashMap(String key, HashMap<String, String> map) {
        doEdit();
        Gson gson = new Gson();
        String json = gson.toJson(map);
        mEditor.putString(key, json);
        doCommit();
    }

    public void putLinkedHashMap(String key, LinkedHashMap<String, String> map){
        doEdit();
        String json = new Gson().toJson(map);
        mEditor.putString(key, json);
        doCommit();
    }

    // Get string type arraylist
    public HashMap<String, String> getStringHashMap(String key) {
        Gson gson = new Gson();
        String json = mPref.getString(key, null);
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    // Get string type arraylist
    public LinkedHashMap<String, String> getLinkedHashMap(String key) {
        Gson gson = new Gson();
        String json = mPref.getString(key, null);
        Type type = new TypeToken<LinkedHashMap<String, String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void firstTimeAskingPermission(String permission, boolean isFirstTime) {
        doEdit();
        mEditor.putBoolean(permission, isFirstTime);
        doCommit();
    }

    public boolean isFirstTimeAskingPermission(String permission) {
        return mPref.getBoolean(permission, true);
    }


    //--------------------------------------------------------------------------------------------------------------------
    private void doEdit() {
        if (mEditor == null) {
            mEditor = mPref.edit();
        }
    }

    private void doCommit() {
        if (mEditor != null) {
            mEditor.apply();
            mEditor = null;
        }
    }

    public void clearPreference() {
        doEdit();
        mEditor.clear();
        notEditor = notPref.edit();
        notEditor.clear();
        notEditor.apply();
        doCommit();
    }

    public void addNotificationToSeenList(String notificationId) {
        ArrayList<String> seenNotificationList = getSeenNotificationList();
        if (!seenNotificationList.contains(notificationId)) {
            seenNotificationList.add(notificationId);
            putUnseenNotificationCount(getUnSeenNotificationsCount() - 1);
            notEditor = notPref.edit();
            notEditor.putString(Key.seen_notification_list.name(), new Gson().toJson(seenNotificationList));
            if (notEditor.commit()) {
                notEditor = null;
            }
        }

    }

    public ArrayList<String> getSeenNotificationList() {
        Gson gson = new Gson();
        String json = notPref.getString(Key.seen_notification_list.name(), null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> seenNotList = gson.fromJson(json, type);
        return seenNotList == null ? new ArrayList<String>() : seenNotList;
    }

    public void putUnseenNotificationCount(int unSeenNotCount) {
        notEditor = notPref.edit();
        notEditor.putInt(Key.un_seen_notification_count.name(), unSeenNotCount);
        if (notEditor.commit()) {
            notEditor = null;
        }
    }

    public int getUnSeenNotificationsCount() {
        return notPref.getInt(Key.un_seen_notification_count.name(), 0);
    }
}
