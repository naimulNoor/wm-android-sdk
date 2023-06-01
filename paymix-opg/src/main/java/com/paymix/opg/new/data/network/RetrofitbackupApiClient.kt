package com.walletmix.paymixbusiness.data.network

import android.content.Context
import android.util.Log
import com.walletmix.paymixbusiness.service.sms_retriver.AppSignatureHashHelper
import com.walletmix.paymixbusiness.data.prefs.PrefKeys
import com.walletmix.paymixbusiness.data.prefs.PreferenceManager
import com.walletmix.paymixbusiness.utils.NetworkUtils
import com.walletmix.paymixbusiness.BuildConfig
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.security.SecureRandom
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


object RetrofitbackupApiClient {

    private const val HEADER_CACHE_CONTROL = "Cache-Control"
    private const val HEADER_PRAGMA = "Pragma"
    private const val CACHE_SIZE = 10 * 1024 * 1024 // 10 MB
    private const val MAX_AGE = 10 // in Minutes
    private const val MAX_STALE = 1 // in Days
    var i=0
    fun getRetrofit(
        context: Context,
        networkUtils: NetworkUtils,
        pref: PreferenceManager
    ): Retrofit {

        /** Cache  Object*/
        val httpCacheDirectory = File(context.applicationContext.cacheDir, "http-cache")
        val cache = Cache(httpCacheDirectory, CACHE_SIZE.toLong())

        val dispatcher = Dispatcher()
        /**
         * This interceptor will be called ONLY if the network is available
         */
        val networkInterceptor = Interceptor { chain ->
            if (BuildConfig.DEBUG) Log.e("NETWORK INTERCEPTOR", " >>>>>>>>>>>> START")

            val response = chain.proceed(chain.request())
            var request =chain.request()
            val cacheControl = CacheControl.Builder()
                .maxAge(MAX_AGE, TimeUnit.MINUTES)
                .build()




            if (BuildConfig.DEBUG) Log.e("NETWORK INTERCEPTOR", " >>>>>>>>>>>> END")

            val responseBuilder = response.newBuilder()
            if (APIs.CACHING_URLs.isNotEmpty() && APIs.CACHING_URLs.contains(chain.request().url.toString())) {
                responseBuilder.removeHeader(HEADER_PRAGMA)
                responseBuilder.removeHeader(HEADER_CACHE_CONTROL)
                responseBuilder.header(HEADER_CACHE_CONTROL, cacheControl.toString())
            }


  //           var customConditon=ApiCustom()
//            customConditon.dispatchApi(response.code, dispatcher,chain)

               responseBuilder.build()
        }

        /**
         * This interceptor will be called both if the network is available and if the network is not available
         */
        val offlineInterceptor = Interceptor { chain ->
            if (BuildConfig.DEBUG) Log.e("OFFLINE INTERCEPTOR", ">>>>> START.")

            if (!networkUtils.isConnectedToNetwork(context)) {
                throw NoConnectivityException()
            } else {
                val request = chain.request()
                val requestBuilder = request.newBuilder()
                requestBuilder.header("Accept", Headers.ACCEPT)
                requestBuilder.header("content-type", Headers.MULTIPART_FORM_DATA)
                requestBuilder.header("App-Env", "stage")

                if(pref.getString(PrefKeys.APP_SIGNING_KEY,"0x00").equals("0x00")){
                    val appSignatureHashHelper = AppSignatureHashHelper(context)
                    pref.putString(PrefKeys.APP_SIGNING_KEY, appSignatureHashHelper.appSignatures[0])
                    Log.d("fall","0x00")
                }else{
                    requestBuilder.header("Sms-Hash-Key", pref.getString(PrefKeys.APP_SIGNING_KEY,"0x00")!!)
                    Log.d("pass","0x00")
                }

                requestBuilder.header("deviceId", pref.getString(PrefKeys.DEVICEID,"NO DEVICE ID").toString()
                )
                requestBuilder.header("device", "1") //1 for android
                requestBuilder.header("appVersionCode",
                    pref.getString(PrefKeys.APPVERSIONCODE,"NO Version Code")!!
                )
                requestBuilder.header("Accept-Language", "en")
                requestBuilder.header("OS-Version", pref.getString(PrefKeys.UserAndroidVersion,"0")!!)
                requestBuilder.header("Device-Info", pref.getString(PrefKeys.AndroidDeviceInfo,"0")!!
                )



                /** AUTHORIZATION TOKEN  */

                val mainAccountToken = "${Headers.BEARER}${pref.getString(PrefKeys.TOKEN)}"
                requestBuilder.header("Authorization", mainAccountToken)

                val requestUrl = request.url.toString()

                /** CACHED CHECKED >>>> */
                if (APIs.CACHING_URLs.isNotEmpty() && APIs.CACHING_URLs.contains(requestUrl)) {
                    val cacheControl = CacheControl.Builder()
                        .maxStale(MAX_STALE, TimeUnit.DAYS)
                        .build()
                    requestBuilder.removeHeader(HEADER_PRAGMA)
                    requestBuilder.removeHeader(HEADER_CACHE_CONTROL)
                    requestBuilder.cacheControl(cacheControl)
                }

                if (BuildConfig.DEBUG) Log.e("OFFLINE INTERCEPTOR", ">>>>> END.")
                chain.proceed(requestBuilder.build())
            }
        }


        /** http Client Builder, configure it with cache, network cache interceptor and logging interceptor*/
        val httpClientBuilder = OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(60, TimeUnit.SECONDS) //TODO connection time out
            .readTimeout(60, TimeUnit.SECONDS)//TODO connection time out
            .writeTimeout(60, TimeUnit.SECONDS)//TODO connection time out
            .addNetworkInterceptor(networkInterceptor)
            .addInterceptor(offlineInterceptor)
            .dispatcher(dispatcher)




        /** Adding Logging Interceptor only for DEBUG mode >>>>>>>>>>*/
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            httpClientBuilder.addInterceptor(loggingInterceptor)

        }


        return Retrofit.Builder()
            .baseUrl(APIs.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            //.client(unSafeOkHttpClient().build())
            .client(httpClientBuilder.build())
            .build()
    }

    //Okhttp 4.9.0
    //for ssl issue
    fun unSafeOkHttpClient() :OkHttpClient.Builder {
        val okHttpClient = OkHttpClient.Builder()
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts:  Array<TrustManager> = arrayOf(object : X509TrustManager {
                override fun checkClientTrusted(
                    p0: Array<out java.security.cert.X509Certificate>?,
                    authType: String?
                ) {}
                override fun checkServerTrusted(
                    p0: Array<out java.security.cert.X509Certificate>?,
                    authType: String?
                ) {}
                override fun getAcceptedIssuers(): Array<out java.security.cert.X509Certificate>? = arrayOf()
            })

            // Install the all-trusting trust manager
            val  sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            if (trustAllCerts.isNotEmpty() &&  trustAllCerts.first() is X509TrustManager) {
                okHttpClient.sslSocketFactory(sslSocketFactory, trustAllCerts.first() as X509TrustManager)
               // okHttpClient.hostnameVerifier { HostnameVerifier { _, _ -> true } }
            }

            //for looging
//            val httpClientBuilder = OkHttpClient.Builder()
//                .cache(cache)
//                .connectTimeout(60, TimeUnit.SECONDS) //TODO connection time out
//                .readTimeout(60, TimeUnit.SECONDS)//TODO connection time out
//                .writeTimeout(60, TimeUnit.SECONDS)//TODO connection time out
//                .addNetworkInterceptor(networkInterceptor)
//                .addInterceptor(offlineInterceptor)
//                .dispatcher(dispatcher)




            /** Adding Logging Interceptor only for DEBUG mode >>>>>>>>>>*/
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

                okHttpClient.addInterceptor(loggingInterceptor)

            }


            return okHttpClient
        } catch (e: Exception) {
            return okHttpClient
        }
    }

}