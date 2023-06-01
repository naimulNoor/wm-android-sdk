package com.walletmix.paymixbusiness.utils

import android.util.Log
import io.sentry.android.timber.BuildConfig
import timber.log.Timber

class TimberReleaseTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.ERROR && t != null) {
            //Crashlytics reporting here
        }
//        val tree = CrashReportingTree().Builder()
//            .system("Android")
//            .program("Papertrail")
//            .logger("My-App")
//            .host(BuildConfig.PAPERTRAIL_HOST)
//            .port(BuildConfig.PAPERTRAIL_PORT)
//            // send logs to papertrail with priority Log.INFO and above
//            .priority(Log.INFO)
//            .build()
//
//        Timber.plant(tree)
//
    }

}