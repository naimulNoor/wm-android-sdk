package com.walletmix.paymixbusiness.utils

import android.content.Intent




interface OnIntentReceived {
    fun onIntent(i: Intent?, resultCode: Int)
}