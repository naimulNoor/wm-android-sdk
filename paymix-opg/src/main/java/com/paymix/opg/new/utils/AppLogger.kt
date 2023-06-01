package com.walletmix.paymixbusiness.utils

import android.util.Log

class AppLogger {

    private val TAG = "mr.root"

    fun logD(message: String) {
        logD(TAG, message)
    }

    fun logD(TAG: String, message: String) {
        if (AppUtils.shared.isDebug) {
            Log.d(TAG, message)
        }
    }

    fun <T> logD(className: Class<T>, message: String) {
        logD(className.simpleName, message)

    }

    fun logV(message: String) {
        logV(TAG, message)
    }

    fun logE(message: String) {
        if(AppUtils.shared.isDebug){
            Log.e(">>>", message)
        }
    }


    fun logV(TAG: String, message: String) {
        if (AppUtils.shared.isDebug) {
            Log.v(TAG, message)
        }
    }

    fun <T> logV(className: Class<T>, message: String) {
        logV(className.simpleName, message)
    }
}
