package com.walletmix.paymixbusiness.service.push

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.walletmix.paymixbusiness.base.BaseActivity
import com.walletmix.paymixbusiness.data.prefs.PreferenceManager
import com.walletmix.paymixbusiness.R

import javax.inject.Inject

class PushMessageHandler : BaseActivity() {

    @Inject
    lateinit var mPrefManager: PreferenceManager
    var bundle=Bundle()
    override fun getContentView(): Int {
        return R.layout.activity_push_handler
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPrefs = PreferenceManager(getContext())
        Log.d("loging-status","notification-false")
    }

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {

    }

}

