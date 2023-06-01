package com.walletmix.paymixbusiness.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.walletmix.paymixbusiness.ui.view.dialog.DialogError


import com.walletmix.paymixbusiness.R
import java.util.*


private const val NOTIF_CHANNEL_ID = "primary_notification_channel"
private const val NOTIF_CHANNEL_NAME = "Job Service notification"
val TAG: String? = DialogError::class.java.simpleName
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    class NotificationJobService : JobService() {
        var i:Int=1
        override fun onStartJob(params: JobParameters?): Boolean {

            notificationJob(params);
            Log.d(TAG,"Running service now..");
            return true

        }



        override fun onStopJob(params: JobParameters?): Boolean {
            Log.d(TAG,"Running service now..");
            // True because if the job fails, you want the job to be rescheduled instead of dropped.
            return true
        }


        private fun notificationJob(params: JobParameters?) {
            // Get Notification Manager
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Create Notification Channel if device OS >= Android O
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel(NOTIF_CHANNEL_ID, NOTIF_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).let {
                    notificationManager.createNotificationChannel(it)
                }
            }

            // Create PendingIntent with empty Intent
            // So this pending intent does nothing
            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                Intent(),
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )


            // Configure NotificationBuilder
            val builder = NotificationCompat.Builder(this, NOTIF_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("notificationcount-${i++}")
                .setContentText("Message-${Calendar.getInstance().getTime()}")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

            // Make the Notification
            notificationManager.notify(0, builder.build())

            // False to let system know that the job is completed by the end of onStartJob(),
            // and the system calls jobFinished() to end the job.
        }

    }
