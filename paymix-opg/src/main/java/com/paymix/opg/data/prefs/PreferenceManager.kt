package com.paymix.opg.data.prefs

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList
import java.util.HashMap


class PreferenceManager(context: Context) {

    private val mPref: SharedPreferences = context.getSharedPreferences(
        context.applicationContext.packageName,
        Context.MODE_PRIVATE
    )

    private var mEditor: SharedPreferences.Editor? = null

    fun put(key: String, value: Any) {
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Double -> putDouble(key, value)
            is Boolean -> putBoolean(key, value)
        }
    }

    fun get(key: String): Any {
        return mPref.all[key] ?: ""
    }

    fun <T> putObject(key: String, value: T) {
        putString(key, Gson().toJson(value))
    }

    fun <T> getObject(key: String, theClass: Class<T>): T? {
        return Gson().fromJson(getString(key, ""), theClass)
    }

    fun putString(key: String, value: String) {
        doEdit()
        mEditor!!.putString(key, value)
        doCommit()
    }

    fun getString(key: String): String {
        return mPref.getString(key, "") ?: ""
    }

    fun getString(key: String, defaultValue: String): String? {
        return mPref.getString(key, defaultValue)
    }

    fun putInt(key: String, value: Int) {
        doEdit()
        mEditor!!.putInt(key, value)
        doCommit()
    }

    fun getInt(key: String): Int {
        return mPref.getInt(key, 0)
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return mPref.getInt(key, defaultValue)
    }

    fun putDouble(key: String, value: Double) {
        doEdit()
        mEditor!!.putString(key, value.toString())
        doCommit()
    }


    fun putBoolean(key: String, value: Boolean) {
        doEdit()
        mEditor!!.putBoolean(key, value)
        doCommit()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return mPref.getBoolean(key, defaultValue)
    }

    fun putStringArrayList(key: String, arrayList: ArrayList<String>) {
        doEdit()
        val gson = Gson()
        val json = gson.toJson(arrayList)
        mEditor!!.putString(key, json)
        doCommit()
    }

    fun getStringArrayList(key: String): ArrayList<String>? {
        val gson = Gson()
        val json = mPref.getString(key, null)
        val type = object : TypeToken<ArrayList<String>>() {

        }.type
        return gson.fromJson<ArrayList<String>>(json, type)
    }

    fun putIntegerArrayList(key: String, arrayList: ArrayList<Int>) {
        doEdit()
        val gson = Gson()
        val json = gson.toJson(arrayList)
        mEditor!!.putString(key, json)
        doCommit()
    }

    fun getIntegerArrayList(key: String): ArrayList<Int>? {
        val gson = Gson()
        val json = mPref.getString(key, null)
        val type = object : TypeToken<ArrayList<Int>>() {

        }.type
        return gson.fromJson<ArrayList<Int>>(json, type)
    }

    fun putStringHashMap(key: String, map: HashMap<String, String>) {
        doEdit()
        val gson = Gson()
        val json = gson.toJson(map)
        mEditor!!.putString(key, json)
        doCommit()
    }

    fun getStringHashMap(key: String): HashMap<String, String>? {
        val gson = Gson()
        val json = mPref.getString(key, null)
        val type = object : TypeToken<HashMap<String, String>>() {

        }.type
        return gson.fromJson<HashMap<String, String>>(json, type)
    }


    fun firstTimeAskingPermission(permission: String, isFirstTime: Boolean) {
        doEdit()
        mEditor!!.putBoolean(permission, isFirstTime)
        doCommit()
    }

    fun isFirstTimeAskingPermission(permission: String): Boolean {
        return mPref.getBoolean(permission, true)
    }

    private fun doEdit() {
        if (mEditor == null) {
            mEditor = mPref.edit()
        }
    }

    private fun doCommit() {
        mEditor?.let {
            mEditor!!.apply()
            mEditor = null
        }
    }

    fun remove(key: String) {
        doEdit()
        mEditor?.remove(key)
        doCommit()
    }

    fun clearPreference() {
        doEdit()
        mEditor?.clear()
        doCommit()
    }


}

/**
 * .....................................................................
 *  Difference between apply() and commit() in SharedPreference
 * .....................................................................
 * 1. apply() commits without returning a boolean indicating success or failure. commit() returns true if the save works, false otherwise.
 * 2. apply() is faster. commit() is slower.
 * 3. apply(): Asynchronous commit(): Synchronous
 * 4. apply() was added as the Android dev team noticed that almost no one took notice of the return value, so apply is faster as it is asynchronous.
 * 5. apply() writes to a temporary Map that is later written asynchronously to disk. If you immediately use methods like getBoolean() in your case,
 *    it will first lookup if there is a value for this key in the temporary Map and returns it.
 * 6. Even if you call a commit() after apply(), its safe because all the commit calls will be blocked until the apply()’s commit and the commit()’s commit
 *    (you called after apply()) is done.
 * */