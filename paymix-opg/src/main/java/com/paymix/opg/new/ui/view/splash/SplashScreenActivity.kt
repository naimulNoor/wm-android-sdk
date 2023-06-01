package com.walletmix.paymixbusiness.ui.view.splash

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import com.walletmix.paymixbusiness.ui.view.dialog.DialogError
import com.walletmix.paymixbusiness.base.MvpBaseActivity
import com.walletmix.paymixbusiness.data.network.api_response.utils.SplashResponseModel
import com.walletmix.paymixbusiness.data.prefs.PrefKeys
import com.walletmix.paymixbusiness.service.sms_retriver.AppSignatureHashHelper
import com.walletmix.paymixbusiness.ui.view.auth.login.LoginActivity
import com.walletmix.paymixbusiness.ui.view.dashboard.DashBoardActivity
import com.walletmix.paymixbusiness.utils.AppUtils
import com.walletmix.paymixbusiness.utils.Navigator
import com.walletmix.paymixbusiness.utils.showToast
import com.walletmix.paymixbusiness.R
import com.walletmix.paymixbusiness.databinding.ActivitySplashScreenBinding
import java.lang.Exception


class SplashScreenActivity : MvpBaseActivity<SplashPresenter>(), SplashContract.View  {


    val handler = Handler()

    var bundle=Bundle()

    private lateinit var binding: ActivitySplashScreenBinding

    override fun getContentView(): View {


        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root
        return view


    }

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {

//
//        Handler().postDelayed({
//            Navigator.sharedInstance.navigate(this, LoginActivity::class.java)
//        }, 2000)

        genHexStringForSmsRetrieverAPI()

        val info= Build.MANUFACTURER +"-"+Build.MODEL
        val androidversion=Build.VERSION.RELEASE

        Log.d("Device Info : ",info)
        Log.d("androidVersion : ",androidversion)

        mPrefManager.put(PrefKeys.AndroidDeviceInfo, info)
        mPrefManager.put(PrefKeys.UserAndroidVersion,androidversion)


//        Handler().postDelayed({
//            val mIntent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
//            startActivity(mIntent)
//            finish()
//        }, 2000)

        getDeviceHeaderInformation()

    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(Runnable {
            mPresenter.getAppVersion()
        }, 1000)
    }

    private fun genHexStringForSmsRetrieverAPI(){
        val appSignatureHashHelper = AppSignatureHashHelper(this)
        mPrefManager.putString(PrefKeys.APP_SIGNING_KEY, appSignatureHashHelper.appSignatures[0])
        Log.e(TAG, "SMSHashKey: " + appSignatureHashHelper.appSignatures[0])
    }

    override fun appVersionDidReceived(response: SplashResponseModel) {
        mPrefManager.put(PrefKeys.TERMS_AND_CONDITIONS, response.data.termsAndCondition ?: "")
        mPrefManager.put(PrefKeys.PRIVACY_POLICY, response.data.privacyPolicy ?: "")

        mPrefManager.put(PrefKeys.TERMS_AND_CONDITIONS_BN, response.data.termsAndConditionBn ?: "")
        mPrefManager.put(PrefKeys.PRIVACY_POLICY_BN, response.data.privacyPolicyBn ?: "")

        mPrefManager.putStringHashMap(PrefKeys.APP_BANKS,response.data.banks)
        mPrefManager.putStringHashMap(PrefKeys.APP_PAYMENT_MODULE,response.data.cards)




        val playStoreAppVersion = response.data.androidAppVersion
        Log.d("playStoreAppVersion", playStoreAppVersion.toString())
        val installedAppVersion = AppUtils.shared.getAppVersion(getContext())
        if (playStoreAppVersion <= installedAppVersion) {
            navigateTask()
        } else {

            //////show Alert Dialog
            TODO("need to saved a dialog")
            this.showToast("This App Don't Updated")
        }
    }


    override fun navigateTask() {

        if (mPrefManager.getBoolean(PrefKeys.LOGGED_IN, false)) {
            Navigator.sharedInstance.navigate(this, DashBoardActivity::class.java)
        } else {
            Navigator.sharedInstance.navigate(this, LoginActivity::class.java)
        }


        finish()

    }

    override fun onNetworkUnavailable() {

        val animId: Int =R.raw.failed
        try{
            val dialogError: DialogError = DialogError.newInstance(
                getString(R.string.no_internet_connection),
                getString(R.string.no_internet_msg),
                titleFullRed = true,animId
            )
            dialogError.dialogErrorOkCallback = object : DialogError.DialogErrorOkCallback {
                override fun okBtnDidTapped() {
                    mPresenter.getAppVersion()
                }
            }
            dialogError.show(supportFragmentManager, DialogError.TAG)

        }catch (_: Exception){}

    }

    fun getDeviceHeaderInformation() {
        getAndoridCode()
        getDeviceId(getContext())

    }
    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context) {
        try{
            val TelephonyMgr = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            val deviceId: String
            deviceId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            } else {
                val mTelephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        Log.d("deviceId", "need permission")
                    }
                }
                Log.d("sdkcode",Build.VERSION.SDK_INT.toString())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //mTelephony.deviceId
                    Log.d("sdkcode","enterif")
                    Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

                } else {
                    Log.d("sdkcode","enterelse")
                    Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                }
            }
            if(deviceId.isEmpty()){
                mPrefManager.putString(PrefKeys.DEVICEID,"no device id")
            }else{
                mPrefManager.putString(PrefKeys.DEVICEID,deviceId)
            }

            Log.d("deviceId", deviceId)
        }catch (e:Exception){
            Log.d("hedererror", e.toString())
        }

    }
    fun getAndoridCode(){
        val installedAppVersion = AppUtils.shared.getAppVersion(getContext())
        mPrefManager.putString(PrefKeys.APPVERSIONCODE,installedAppVersion.toString())
        Log.d("versioncode", installedAppVersion.toString())
    }




}

