package com.paymix.myapp

import android.util.Base64
import android.util.Log
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.Socket


class TimberLogstashTreeConf: Timber.DebugTree() {
    private val LOGSTASH_SERVER_ADDRESS = "154.26.128.15"
    private val LOGSTASH_SERVER_PORT = 9200
    private val LOGSTASH_SERVER_USER = "elastic"
    private val LOGSTASH_SERVER_PASSWORD = "Pencil@123"
    fun sendLog(log: String) {
        val thread = Thread {
            try {
                val socket = Socket(LOGSTASH_SERVER_ADDRESS, LOGSTASH_SERVER_PORT)
                // Send the authentication credentials

                val outputStream = socket.getOutputStream()
                val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                // Send the authentication credentials
                val auth: String = "$LOGSTASH_SERVER_USER:$LOGSTASH_SERVER_PASSWORD"
                val encodedAuth: String = Base64.encodeToString(auth.toByteArray(), Base64.NO_WRAP)
                val authHeader = "Authorization: Basic $encodedAuth"
                val logWithIndex: String = "android-log $log"

                outputStream.write(authHeader.toByteArray())
                outputStream.write(logWithIndex.toByteArray())
                val response = reader.readLine()
                Log.d("timber-log",response)
                outputStream.flush()
                outputStream.close()
                socket.close()

            } catch (e: IOException) {
                e.printStackTrace()
                Log.d("timber-log", e.message!!)
            }
        }.start()
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        // Customize the log format as per your requirements
        val builder = java.lang.StringBuilder()
        builder.append("[").append(priority).append("] ").append(tag).append(": ").append(message)
        if (t != null) {
            builder.append(Log.getStackTraceString(t))
        }
        sendLog(builder.toString())

    }



}