package com.walletmix.paymixbusiness.base

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.Unbinder
import com.bumptech.glide.Glide
import com.walletmix.paymixbusiness.ui.view.dialog.DialogError
import com.walletmix.paymixbusiness.R
import com.walletmix.paymixbusiness.databinding.AppBarLayoutBinding
import com.walletmix.paymixbusiness.data.prefs.PrefKeys
import com.walletmix.paymixbusiness.data.prefs.PreferenceManager
import com.walletmix.paymixbusiness.ui.dialog.DialogSuccess
import com.walletmix.paymixbusiness.ui.view.auth.login.LoginActivity
import com.walletmix.paymixbusiness.utils.*
import com.walletmix.paymixbusiness.utils.Keys.IntentKeys
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject
import kotlin.system.exitProcess


abstract class MvpBaseActivity<P : BaseContract.Presenter> : AppCompatActivity(), BaseContract.View,
    HasSupportFragmentInjector {

    protected val TAG: String by lazy {
        this.javaClass.simpleName
    }

    private var progressDialog: ProgressDialog? = null
    private var unBinder: Unbinder? = null

    var flag:Int=0

    @Inject
    lateinit var mFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var mPresenter: P

    @Inject
    lateinit var mAlertService: MyAlertService

    @Inject
    lateinit var mNetworkUtils: NetworkUtils

    @Inject
    lateinit var mAppLogger: AppLogger

    @Inject
    lateinit var mPrefManager: PreferenceManager

    lateinit var appBarLayoutBinding:AppBarLayoutBinding


    var mAppLanguage: String? = null

    // Dialog >>>>
    var loader: AlertDialog? = null
    var dialogError: DialogError? = null
    var dialogSuccess: DialogSuccess? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        loadAppLanguage()

        setContentView(getContentView())
        unBinder = ButterKnife.bind(this)

        supportActionBar?.let {
            supportActionBar!!.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }
        }

        mPrefManager.putBoolean("running", true)

        onViewReady(savedInstanceState, intent)
    }

     open fun setAppBar(binding: AppBarLayoutBinding) {
         mAppLogger.logD("setup-app-bar")
         var data=mPrefManager.getObject(PrefKeys.APP_BAR_DATA,Map::class.java)
         if (data != null) {
             if(data.isNotEmpty()){
                 try {
                     mAppLogger.logD("app-bar",data.toString())
                     binding.merchantName.text= data["app_bar_name"].toString()
                     AppUtils.shared.showImage(
                         getContext(),
                         data["app_bar_image"].toString(),
                         binding.merchantImg
                     )
                 }catch (e:Exception){
                     mAppLogger.logD("app-bar-error",e.toString())
                 }


             }
         }

     }

    abstract fun getContentView(): View
    abstract fun onViewReady(savedInstanceState: Bundle?, intent: Intent)

    protected fun showProgressDialog(message: String) {

        try{
            val loaderBuilder = AlertDialog.Builder(this)
            loaderBuilder.setCancelable(false)
            val loadingView = LayoutInflater.from(this).inflate(R.layout.app_loading_view, null)
            val gifImgView = loadingView.findViewById<ImageView>(R.id.img_loader)
            Glide.with(this).asGif().load(R.raw.lottie_loader).into(gifImgView)
            loaderBuilder.setView(loadingView)
            loader = loaderBuilder.create()
            loader?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            loader?.show()
        }catch (e:Exception){

        }

    }

    protected fun showSuccessDialog(
        title: String? = null,
        message: String?,
        animId:Int=R.raw.success,

    ) {
        try{
            dialogSuccess?.dismiss()
            dialogSuccess = DialogSuccess.newInstance(title, message,animId)
            dialogSuccess?.show(supportFragmentManager, DialogSuccess.TAG)
        }catch (e:Exception){}

    }





    protected fun showSuccessWithNavigateDialog(
        title: String? = null,
        message: String?,
        destination_activity: Class<AppCompatActivity>
    ) {
        try{
            dialogSuccess?.dismiss()
            dialogSuccess = DialogSuccess.newInstance(title, message, R.raw.success)
//            dialogSuccess!!.dialogSuccessOkCallback=object : DialogSuccess.DialogSuccessOkCallback {
//                override fun okBtnDidTapped() {
//                    Navigator.sharedInstance.navigate(getContext(), destination_activity)
//                    finish()
//                }
//            }
            dialogSuccess?.show(supportFragmentManager, DialogSuccess.TAG)
        }catch (e:Exception){}

    }

    protected fun showSuccessWithNavigateDialogWithBundle(
        title: String? = null,
        message: String?,
        destination_activity: Class<*>,
        bundle_key:String?,
        bundle: Bundle?
    ) {
        try{
            dialogSuccess?.dismiss()
            dialogSuccess = DialogSuccess.newInstance(title, message, R.raw.success)
//            dialogSuccess!!.dialogSuccessOkCallback=object : DialogSuccess.DialogSuccessOkCallback {
//                override fun okBtnDidTapped() {
//                    //Navigator.sharedInstance.navigate(getContext(), destination_activity)
//
//                    Navigator.sharedInstance.navigateWithBundle(
//                        getContext(),
//                        destination_activity,
//                        bundle_key!!,
//                        bundle!!
//                    )
//                    finish()
//                }
//            }
            dialogSuccess?.show(supportFragmentManager, DialogSuccess.TAG)
        }catch (e:Exception){}

    }


    protected fun showErrorDialog(title: String? = null,
        message: String?,
        titleFullRed: Boolean = false,
        animId: Int =R.raw.failed
    ) {
        try{
            dialogError?.dismiss()
            Log.d("dialog-error",titleFullRed.toString())
            dialogError = DialogError.newInstance(title, message, titleFullRed,animId)
            dialogError?.show(supportFragmentManager, DialogError.TAG)

        }catch (e:Exception){}

    }

    protected fun hideProgressDialog() {
        loader?.cancel()
    }

    protected fun showAlert(title: String? = null, message: String) {
        mAlertService.showAlert(getContext(), title, message)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item?.let {
            if (item.itemId == android.R.id.home) {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        mPresenter.detachView()
        //mAlertService.onDestroy()
        hideProgressDialog()
        unBinder?.let {
            unBinder!!.unbind()
            unBinder = null
        }
        mPresenter.clearDisposable()

        super.onDestroy()
    }

    override fun getContext(): Context {
        return this
    }

    override fun onNetworkCallStarted(loadingMessage: String) {
        showProgressDialog(getString(R.string.please_wait))
    }

    override fun onNetworkCallEnded() {
        hideProgressDialog()
    }



    override fun onNetworkUnavailable() {
        showErrorDialog(
            title = getString(R.string.no_internet_connection),
            message = getString(R.string.no_internet_msg),
            titleFullRed = true
        )
    }

    override fun onTestError(error: String) {
        showErrorDialog(
            title = getString(R.string.error),
            message = error,
            titleFullRed = true
        )
    }

    override fun onServerError() {

        showErrorDialog(
            title = getString(R.string.error),
            message = getString(R.string.server_error),
            titleFullRed = true
        )
    }

    override fun onNetworkError() { //TODO:: need to impliment

        showErrorDialog(
            title = getString(R.string.error),
            message = getString(R.string.server_error),
            titleFullRed = true
        )
    }

    override fun onTimeOutError() {
        showErrorDialog(
            title = getString(R.string.error),
            message = getString(R.string.server_error),
            titleFullRed = true
        )
    }

    override fun onUserDidTooManyAttempts(errorMsg: String) {
        showToast(errorMsg)
    }

    override fun onUserUnauthorized(errorMessage: String) {
        mPrefManager.clearPreference()
        var bundle=Bundle()
        bundle.putSerializable(IntentKeys.IS_COME_FROM_NOTIFICATION,false)
        val loginIntent = Intent(getContext(), LoginActivity::class.java)
        loginIntent.putExtra(IntentKeys.DATA_BUNDLE, bundle)
        loginIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(loginIntent)
    }

    override fun onUserDisabled(errorMsg: String) {
        showErrorDialog(title = null, message = errorMsg)
    }



    override fun onSystemUpgrading() {
        mAlertService.showConfirmationAlert(
            getContext(),
            getString(R.string.maintenance_title),
            getString(R.string.maintenance_message),
            null,
            getString(R.string.okay),
            object : MyAlertService.AlertListener {
                override fun negativeBtnDidTapped() {

                }

                override fun positiveBtnDidTapped() {
                    exitProcess(0)
                }
            })
    }

    override fun applyForcePinReset() {

    }

    override fun onExpectationFailed(message: String) {
        showErrorDialog(title = getString(R.string.Attention), message = message, titleFullRed = true)
    }

    override fun onValidationFailed(messages: LinkedHashMap<String, String>) {
        showErrorDialog(title="Error",message = messages.values.first(),animId=R.raw.warning)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return mFragmentInjector
    }


    public fun loadAppLanguage() {
       /* mAppLanguage = mPrefManager.getString(Keys.APP_LANGUAGE.name, AppLanguage.ENGLISH.name)
        when (mAppLanguage) {
            AppLanguage.BENGALI.name -> {
                AppUtils.shared.setLocale(getContext(), AppLanguage.BENGALI)
                this.getTheme().applyStyle(R.style.AppThemeBangla, true);
            }
            AppLanguage.ENGLISH.name -> {
                AppUtils.shared.setLocale(getContext(), AppLanguage.ENGLISH)
                this.getTheme().applyStyle(R.style.AppThemeEnglish, true);
            }
        }*/
    }



    }


