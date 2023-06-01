package com.walletmix.paymixbusiness.utils

import android.app.*
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.walletmix.paymixbusiness.service.push.PushMessageHandler

import com.walletmix.paymixbusiness.base.BaseApplication
import com.walletmix.paymixbusiness.utils.Keys.IntentKeys
import com.walletmix.paymixbusiness.R
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*


class NotificationUtils(private val context: Context) {

    private var mNotificationManager: NotificationManager? = null
    private var mBuilder: NotificationCompat.Builder? = null
    private val notificationChannelID: String = context.applicationContext.packageName
    private val notificationChannelName: String = context.applicationContext.packageName

    private var notiId:Int=-1
    private var notititle:String=""
    private var notiMessage:String=""

    var notibundle:Bundle=Bundle()

    private val notificationManager: NotificationManager?
        get() {
            if (mNotificationManager == null) {
                mNotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val mChannel = NotificationChannel(
                        notificationChannelID,
                        notificationChannelName,
                        NotificationManager.IMPORTANCE_HIGH
                    )
                    mChannel.enableLights(true)
                    mChannel.setShowBadge(false)
                    mChannel.enableVibration(true)
                    mNotificationManager!!.createNotificationChannel(mChannel)
                }
            }
            return mNotificationManager
        }

    private val notificationId: Int
        get() = (Date().time / 1000L % Integer.MAX_VALUE).toInt()


    private fun buildNotification(NOTIFICATION_ID: Int) {
        notificationManager?.let {
            notificationManager!!.notify(NOTIFICATION_ID, mBuilder!!.build())
        }
    }

    private fun getLaunchIntent(id: Int?): PendingIntent {
        var intent: Intent? = null

        Log.d("lifecycle-notiutils", BaseApplication.islogindta.toString())

        intent = Intent(context, PushMessageHandler::class.java)


        intent.clearStack()
        intent.putExtra(IntentKeys.DATA_BUNDLE, notibundle)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return PendingIntent.getActivity(context, id!!, intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE)

        } else {
            return PendingIntent.getActivity(
                context,
                id!!,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

    }



    /*--------------------------------------------------
        BIG PICTURE STYLE NOTIFICATION
      -------------------------------------------------------*/

    private fun bigPictureStyleNotification(
        notificationId: Int,
        title: String?,
        message:String?,
        bitmap: Bitmap?
    ) {
        mBuilder?.let {
            mBuilder!!.setContentTitle(title)
            //mBuilder!!.setStyle(NotificationCompat.BigTextStyle().bigText(message))
            mBuilder!!.setContentText(message)
            mBuilder!!.setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            buildNotification(notificationId)
        }
    }


    /*--------------------------------------------------
        BIG TEXT STYLE NOTIFICATION
      -------------------------------------------------------*/

    private fun bigTextStyleNotification(
        notificationId: Int,
        title: String?,
        message: String?,
        id:Int?
    ) {
        notiId= id!!
        notititle= title!!
        notiMessage=message!!

        mBuilder?.let {
            mBuilder!!.setContentTitle(title)
            mBuilder!!.setContentText(message)
            //mBuilder!!.setStyle(NotificationCompat.BigTextStyle().bigText(message))
            buildNotification(notificationId)
        }
    }

    /*--------------------------------------------------
        INBOX STYLE NOTIFICATION
      -------------------------------------------------------*/

    private fun inboxStyleNotification(
        notificationId: Int,
        title: String,
        summeryText: String,
        messageList: ArrayList<String>?
    ) {

        if (messageList == null || messageList.isEmpty())
            return


        val inboxStyle = NotificationCompat.InboxStyle()
        inboxStyle.setSummaryText(summeryText)
        for (singleMsg in messageList) {
            inboxStyle.addLine(singleMsg)
        }
        mBuilder!!.setContentTitle(title)
        mBuilder!!.setStyle(inboxStyle)
        buildNotification(notificationId)
    }

    /*--------------------------------------------------
        SHOW NOTIFICATION PUBLIC
     -------------------------------------------------------*/

    fun showNotification(
        smallIcon: Int,
        title: String?,
        message: String?,
        time:String?,
        imageUrl: String?,
        id :Int?
    ) {
        goNotificationDetails(title,message,time,imageUrl)
        if (message == null || message.isEmpty())
            return
        val NOTIFICATION_ID = notificationId
        mBuilder = NotificationCompat.Builder(context.applicationContext, notificationChannelID)
        //mBuilder!!.setSmallIcon(smallIcon)
        mBuilder!!.setSmallIcon(smallIcon)
        mBuilder!!.setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mBuilder!!.color = ContextCompat.getColor(context, R.color.white)
        }else{
            mBuilder!!.color = ContextCompat.getColor(context, R.color.colorPrimary)
        }


        mBuilder!!.setContentIntent(getLaunchIntent(NOTIFICATION_ID))
        if (imageUrl != null && Patterns.WEB_URL.matcher(imageUrl).matches()) {
            try{
                val futureTarget = Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .submit()
                val bitmap = futureTarget.get()

                if(bitmap!=null){
                    Log.d("bitmap-image",bitmap.toString());
                    mBuilder!!.setLargeIcon(bitmap)
                    bigPictureStyleNotification(NOTIFICATION_ID, title, message,bitmap)
                }else{
                    mBuilder!!.setLargeIcon(
                        BitmapFactory.decodeResource(
                            context.resources,
                            R.mipmap.ic_launcher
                        )
                    )
                    bigTextStyleNotification(NOTIFICATION_ID, title, message,id)
                }

            }catch (e:Exception){
                mBuilder!!.setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.mipmap.ic_launcher
                    )
                )
                bigTextStyleNotification(NOTIFICATION_ID, title, message,id)
            }





//            val imageDownloader = ImageDownloader(imageUrl, object : ImageDownloadListener {
//                override fun onDownloadedImage(bitmap: Bitmap?) {
//                    if (bitmap != null) {
//                        Log.d("bitmap-image",bitmap.toString());
//                        mBuilder!!.setLargeIcon(bitmap)
//                        bigPictureStyleNotification(NOTIFICATION_ID, title, message,bitmap)
//
//
//                    } else {
//                        mBuilder!!.setLargeIcon(
//                            BitmapFactory.decodeResource(
//                                context.resources,
//                                R.mipmap.ic_launcher
//                            )
//                        )
//                        bigTextStyleNotification(NOTIFICATION_ID, title, message,id)
//
//                    }
//                }
//
//                override fun onFailedToDownloadImage() {
//                    mBuilder!!.setLargeIcon(
//                        BitmapFactory.decodeResource(
//                            context.resources,
//                            R.mipmap.ic_launcher
//                        )
//                    )
//                    bigTextStyleNotification(NOTIFICATION_ID, title, message,id)
//                }
//            },)
//            imageDownloader.execute()
        } else {
            mBuilder!!.setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.mipmap.ic_launcher
                )
            )
            bigTextStyleNotification(NOTIFICATION_ID, title, message,id)
        }
    }


    private fun isAppOnBackGroundOrForeground(context: Context): Boolean{
        if(isAppOnForeground(context)) return true
        return false
    }


    private fun isAppOnForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false
        val packageName = context.packageName
        for (appProcess in appProcesses) {
            if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName == packageName) {
                return true
            }
        }
        return false
    }

    internal fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            val runningProcesses = am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess == context.packageName) {
                            isInBackground = false
                        }
                    }
                }
            }
        } else {
            val taskInfo = am.getRunningTasks(1)
            val componentInfo = taskInfo[0].topActivity
            if (componentInfo?.packageName == context.packageName) {
                isInBackground = false
            }
        }

        return isInBackground
    }


    internal class ImageDownloader(
        private val imageUrl: String,
        private val downloadListener: ImageDownloadListener,
    ) : AsyncTask<String, Void, Bitmap>() {

        override fun doInBackground(vararg params: String): Bitmap? {
            try {
                val url = URL(imageUrl)
                val inputStream: InputStream
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                if(connection.inputStream!=null){
                    inputStream = connection.inputStream
                    return BitmapFactory.decodeStream(inputStream)
                }


            } catch (e: MalformedURLException) {
                downloadListener.onFailedToDownloadImage()
            } catch (e: IOException) {
                downloadListener.onFailedToDownloadImage()
            }

            return null
        }

        override fun onPostExecute(bitmap: Bitmap) {
            if(bitmap!=null){
                downloadListener.onDownloadedImage(bitmap)
            }

        }
    }

    interface ImageDownloadListener {

        fun onDownloadedImage(bitmap: Bitmap?)
        fun onFailedToDownloadImage()
    }


    private fun goNotificationDetails(
        title: String?,
        message: String?,
        time: String?,
        imageUrl: String?
    ) {
//        notifacation= NotificationResponse.SingleNotification(title!!, message!!, time!!,imageUrl!!)
//        notibundle.putSerializable(IntentKeys.TAPPED_NOTIFICATION, notifacation)
//        notibundle.putBoolean(IntentKeys.IS_COME_FROM_NOTIFICATION, true)
//        Log.d("noti-utils-getNoti",notibundle.toString())
    }

}
