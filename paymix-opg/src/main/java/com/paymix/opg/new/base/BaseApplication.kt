package com.walletmix.paymixbusiness.base


import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import com.squareup.okhttp.Credentials
import com.walletmix.paymixbusiness.BuildConfig
import com.walletmix.paymixbusiness.data.prefs.PrefKeys
import com.walletmix.paymixbusiness.data.prefs.PreferenceManager
import com.walletmix.paymixbusiness.di.component.DaggerAppComponent
import com.walletmix.paymixbusiness.service.sms_retriver.AppSignatureHashHelper
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import javax.inject.Inject


class BaseApplication : Application(), HasActivityInjector, LifecycleObserver {


    companion object {
        var islogindta = false
    }

    @Inject
    lateinit var mActivityInjector: DispatchingAndroidInjector<Activity>
    private val TAG = BaseApplication::class.java.simpleName

    @Inject
    lateinit var mPrefManager: PreferenceManager
    var sInstance: BaseApplication=this



    override fun onCreate() {
        super.onCreate()
        Log.d("lifecycle",islogindta.toString())
        Log.d("MyApp", "App in first time")
        initDagger()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        genHexStringForSmsRetrieverAPI()
        loadAppLanguage()

        timberLoggerInit()

        Timber.d("this is timber")
    }

    private fun timberLoggerInit() {
        if (!BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(object : Timber.Tree() {
                private val client = createAuthenticatedClient()
                private val mediaType = "text/plain; charset=utf-8".toMediaType()

                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    // Format the log message
                    val logMessage = String.format("[%s] %s: %s", priorityToString(priority), tag, message)

                    // Send the log message to Logstash server
                    sendLogToLogstash(logMessage)
                }

                private fun sendLogToLogstash(logMessage: String) {
                    Thread {
                        val requestBody = RequestBody.create(mediaType, logMessage)
                        val request = Request.Builder()
                            .url("https://154.26.128.15:9200")
                            .post(requestBody)
                            .build()

                        try {
                            val call = client.newCall(request)
                            val response = call.execute()
                            response.close()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }.start()

                }

            })
        }
    }
        private fun createAuthenticatedClient(): okhttp3.OkHttpClient {
            val credentials = Credentials.basic("elastic", "Pencil@123")
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            return okhttp3.OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor{ chain ->
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                        .header("Authorization", credentials)
                    val request = requestBuilder.build()
                    chain.proceed(request)
                }.build()


        }

        private fun priorityToString(priority: Int): String {
            return when (priority) {
                android.util.Log.DEBUG -> "DEBUG"
                android.util.Log.INFO -> "INFO"
                android.util.Log.WARN -> "WARN"
                android.util.Log.ERROR -> "ERROR"
                else -> "UNKNOWN"
            }
        }


    private fun genHexStringForSmsRetrieverAPI(){
        val appSignatureHashHelper = AppSignatureHashHelper(this)
        mPrefManager.putString(PrefKeys.APP_SIGNING_KEY, appSignatureHashHelper.appSignatures[0])
        Log.e(TAG, "SMSHashKey: " + appSignatureHashHelper.appSignatures[0])

    }

    private fun initDagger() {
        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.d("lifecycle","onTermination")

    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)

    }




    override fun activityInjector(): AndroidInjector<Activity> {
        return mActivityInjector
    }

    private fun loadAppLanguage() {
//        val appLang = mPrefManager.getString(Keys.APP_LANGUAGE.name, AppLanguage.ENGLISH.name)
//
//        when (appLang) {
//            AppLanguage.BENGALI.name -> {
//                AppUtils.shared.setLocale(this, AppLanguage.BENGALI)
//                this.getTheme().applyStyle(R.style.AppThemeBangla, true);
//            }
//            AppLanguage.ENGLISH.name -> {
//                AppUtils.shared.setLocale(this, AppLanguage.ENGLISH)
//                this.getTheme().applyStyle(R.style.AppThemeEnglish, true);
//            }
//
//        }
    }
}