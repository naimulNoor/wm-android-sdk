package com.walletmix.paymixbusiness.service.sms_retriver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.util.regex.Pattern


class SMSReceiver : BroadcastReceiver() {

    private var otpListener: OTPReceiveListener? = null
    val OTP_REGEX = "(?<!\\d)\\d{6}(?!\\d)"

    fun setOTPListener(otpListener: OTPReceiveListener?) {
        this.otpListener = otpListener
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("SMS","onRecive")
        if (intent.action == SmsRetriever.SMS_RETRIEVED_ACTION) {
            val extras = intent.extras

            val status = extras!![SmsRetriever.EXTRA_STATUS] as Status?
            when (status!!.statusCode) {
                CommonStatusCodes.SUCCESS -> {

                    val sms = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String?
                    sms?.let {
                        val p = Pattern.compile(OTP_REGEX)
                        //val p = Pattern.compile("\\d+")

                        //val p = Pattern.compile(OTP_REGEX)
                        val m = p.matcher(it)
                        if (m.find()) {
                            val otp = m.group()
                            if (otpListener != null) {
                                Log.d("SMS",otp.toString())
                                otpListener!!.onOTPReceived(otp)
                            }
                        }
                    }
                }
            }
        }
    }


    interface OTPReceiveListener {
        fun onOTPReceived(otp: String?)
    }
}