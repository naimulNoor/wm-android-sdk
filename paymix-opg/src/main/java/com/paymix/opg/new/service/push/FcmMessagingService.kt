package com.walletmix.paymixbusiness.push

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.walletmix.paymixbusiness.data.prefs.Keys
import com.walletmix.paymixbusiness.data.prefs.PrefKeys
import com.walletmix.paymixbusiness.data.prefs.PreferenceManager
import com.walletmix.paymixbusiness.utils.Keys.AppLanguage
import com.walletmix.paymixbusiness.utils.NotificationUtils
import com.walletmix.paymixbusiness.R

import org.json.JSONObject


class FcmMessagingService : FirebaseMessagingService() {

    private var mNotificationUtils: NotificationUtils? = null
    private var mPrefs: PreferenceManager? = null


    override fun onCreate() {
        super.onCreate()
        mNotificationUtils = NotificationUtils(this)
        mPrefs = PreferenceManager(this)
        //roni's code
        getCurrentToken()
    }

    private fun getCurrentToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            // Log and toast
            Log.d("FCM", token!!)

        })
    }

    override fun onNewToken(newToken: String) {

        mPrefs?.put(PrefKeys.DEVICE_TOKEN, newToken)
    }

    //your app is in background or foreground all time calling
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.d("notificaiton-robi-alpha",remoteMessage.toString())
        Log.d("notificaiton-robi-alpha",remoteMessage.data.toString())


//        remoteMessage.notification?.let { notification ->
//            handleNotificationMessage(notification.title, notification.body)
//        }
//
//        /*...................................................
//            Handle Data Payload
//         ..................................................*/

        if (remoteMessage.data.isNotEmpty()) {
            try {
                val json = JSONObject(remoteMessage.data as Map<*, *>)
                handleDataMessage(json)
            } catch (e: Exception) {
                // Log.d(TAG, e.message)
            }
        }
    }

    private fun handleNotificationMessage(title: String?, message: String?,time: String?) {
        /* If app is in background, notification will be handled by fireBase itself.
        We have to show notification manually if app is in foreground. */
        mNotificationUtils?.let { notificationUtils ->
            if (notificationUtils.isAppIsInBackground(applicationContext)) {
                notificationUtils.showNotification(
                    R.mipmap.ic_launcher,
                    title,
                    message,
                    time,
                    "",
                1,
                )
            }
        }
    }

    private fun handleDataMessage(notificationJson: JSONObject) {
        Log.d("notification", notificationJson.toString())
        val imageUrl = notificationJson.optString("notification_image")
        var title=""
        var message=""
        var time=""
        if(mPrefs!!.getString(Keys.APP_LANGUAGE.name, AppLanguage.ENGLISH.name)==AppLanguage.ENGLISH.name){
             title = notificationJson.optString("title")
             message = notificationJson.optString("message")
             time=notificationJson.optString("created_at")
        }else{
            title = notificationJson.optString("title_bn")
            message = notificationJson.optString("message_bn")
            time=notificationJson.optString("created_at")
        }

        val id=(0..10).random()
        mNotificationUtils?.showNotification(R.mipmap.ic_launcher, title, message,time, imageUrl,id)
    }
}
