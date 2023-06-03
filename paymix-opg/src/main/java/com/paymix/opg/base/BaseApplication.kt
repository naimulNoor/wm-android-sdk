package com.paymix.opg.base


import android.app.Activity
import android.app.Application
//import android.arch.lifecycle.ProcessLifecycleOwner
import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex


import com.paymix.opg.data.prefs.PrefKeys
import com.paymix.opg.data.prefs.PreferenceManager
//package com.paymix.opg.di.component.DaggerAppComponent
import com.walletmix.paymixbusiness.service.sms_retriver.AppSignatureHashHelper

import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector

import timber.log.Timber
import javax.inject.Inject


class BaseApplication : Application(), HasAndroidInjector, LifecycleObserver {


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
        initDagger()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        loadAppLanguage()

        Timber.d("this is timber")
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


    private fun genHexStringForSmsRetrieverAPI() {
        val appSignatureHashHelper = AppSignatureHashHelper(this)
        mPrefManager.putString(PrefKeys.APP_SIGNING_KEY, appSignatureHashHelper.appSignatures[0])
        Timber.tag(TAG).e("SMSHashKey: %s", appSignatureHashHelper.appSignatures[0])

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

    override fun androidInjector(): AndroidInjector<Any> {
       return mActivityInjector
    }
}