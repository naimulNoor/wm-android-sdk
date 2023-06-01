package com.walletmix.paymixbusiness.base


import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

import com.walletmix.paymixbusiness.data.prefs.PreferenceManager


abstract class BaseActivity : AppCompatActivity() {

    protected val TAG: String  by lazy {
        this.javaClass.simpleName
    }

    private var progressDialog: ProgressDialog? = null
    lateinit var mPrefs: PreferenceManager
    var mAppLanguage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPrefs = PreferenceManager(getContext())
        loadAppLanguage()
        setContentView(getContentView())
        loadAppLanguage()
        supportActionBar?.let {
            supportActionBar!!.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }
        }

        onViewReady(savedInstanceState, intent)
    }



    abstract fun getContentView(): Int
    abstract fun onViewReady(savedInstanceState: Bundle?, intent: Intent)
    protected fun getContext(): Context {
        return this
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.let {
            if (item.itemId == android.R.id.home) {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        progressDialog?.let {
            if (progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
            }
        }
    }




    private fun loadAppLanguage() {
//         mAppLanguage = mPrefs.getString(Keys.APP_LANGUAGE.name, AppLanguage.ENGLISH.name)
//        when (mAppLanguage) {
//            AppLanguage.BENGALI.name -> {
//                AppUtils.shared.setLocale(getContext(), AppLanguage.BENGALI)
//                this.getTheme().applyStyle(R.style.AppThemeBangla, true);
//            }
//            AppLanguage.ENGLISH.name -> {
//                AppUtils.shared.setLocale(getContext(), AppLanguage.ENGLISH)
//                this.getTheme().applyStyle(R.style.AppThemeEnglish, true);
//            }
//        }
    }




}