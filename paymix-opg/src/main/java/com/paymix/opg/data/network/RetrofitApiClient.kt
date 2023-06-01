package com.paymix.opg.data.network

import android.content.Context
import com.paymix.opg.data.prefs.PrefKeys
import com.paymix.opg.data.prefs.PreferenceManager
import com.paymix.opg.utils.NetworkUtils
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.security.SecureRandom
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException


object RetrofitApiClient {

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


            val response = chain.proceed(chain.request())
            var request =chain.request()
            val cacheControl = CacheControl.Builder()
                .maxAge(MAX_AGE, TimeUnit.MINUTES)
                .build()






            val responseBuilder = response.newBuilder()


            //           var customConditon=ApiCustom()
//            customConditon.dispatchApi(response.code, dispatcher,chain)

            responseBuilder.build()
        }

        /**
         * This interceptor will be called both if the network is available and if the network is not available
         */
        val offlineInterceptor = Interceptor { chain ->


            if (!networkUtils.isConnectedToNetwork(context)) {
                throw NoConnectivityException()
            } else {
                val request = chain.request()
                val requestBuilder = request.newBuilder()

                requestBuilder.header("App-Env", "stage")


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



                val requestUrl = request.url.toString()


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

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        httpClientBuilder.addInterceptor(loggingInterceptor)


        return Retrofit.Builder()
            .baseUrl(APIs.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClientBuilder.build())
            //.client(getUnsafeOkHttpClient().build())
            .build()
    }

    //Okhttp 4.9.0
    //for ssl issue
    private fun getUnsafeOkHttpClient(): OkHttpClient.Builder =
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts: Array<TrustManager> = arrayOf(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        p0: Array<out java.security.cert.X509Certificate>?,
                        authType: String?
                    ) = Unit

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        p0: Array<out java.security.cert.X509Certificate>?,
                        authType: String?
                    ) = Unit

                    override fun getAcceptedIssuers(): Array<out java.security.cert.X509Certificate>? = arrayOf()
                }
            )
            // Install the all-trusting trust manager
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory,
                trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { _, _ -> true }
            builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

}