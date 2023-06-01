package com.walletmix.paymixbusiness.base

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.Unbinder
import com.bumptech.glide.Glide
import com.walletmix.paymixbusiness.ui.view.dialog.DialogError
import com.walletmix.paymixbusiness.data.prefs.PreferenceManager
import com.walletmix.paymixbusiness.ui.dialog.DialogSuccess
import com.walletmix.paymixbusiness.ui.view.auth.login.LoginActivity

import com.walletmix.paymixbusiness.utils.*
import com.walletmix.paymixbusiness.utils.Keys.IntentKeys
import com.walletmix.paymixbusiness.R
import com.walletmix.paymixbusiness.databinding.AppBarLayoutBinding
import com.walletmix.paymixbusiness.data.prefs.PrefKeys
import dagger.android.support.AndroidSupportInjection
import java.lang.Exception
import javax.inject.Inject
import kotlin.system.exitProcess

abstract class BaseFragment<P : BaseContract.Presenter> : Fragment(), BaseContract.View {

    protected val TAG: String by lazy {
        this.javaClass.simpleName
    }

    private var progressDialog: ProgressDialog? = null
    private var viewUnbinder: Unbinder? = null

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

    lateinit var rootView: View

    var mAppLanguage: String? = null

    // Dialog >>>>
    var loader: AlertDialog? = null
    var dialogError: DialogError? = null
    var dialogSuccess: DialogSuccess? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadAppLanguage()
    }

    override fun onCreateView(
         inflater: LayoutInflater,
         container: ViewGroup?,
         savedInstanceState: Bundle?
    ): View? {
        rootView = getFragmentView(inflater, container, savedInstanceState)
        viewUnbinder = ButterKnife.bind(this, rootView)
        onViewReady(arguments)
        return rootView
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
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



    protected abstract fun getFragmentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        saveInstanceState: Bundle?
    ): View

    protected abstract fun onViewReady(getArguments: Bundle?)


    fun showSuccessDialog(title: String? = null, message: String?,animId:Int=R.raw.success) {
        try {
            dialogSuccess?.dismiss()
            dialogSuccess = DialogSuccess.newInstance(title, message, animId)
            fragmentManager?.let { fm ->
                dialogSuccess?.show(fm, DialogSuccess.TAG)
            }
        } catch (e: Exception) {
        }

    }

    fun showErrorDialog(title: String? = null, message: String?, titleFullRed: Boolean = false,animId:Int=R.raw.failed) {
        try {
            dialogError?.dismiss()  //dialog bug
            dialogError = DialogError.newInstance(title, message, titleFullRed,animId)
            activity?.supportFragmentManager?.let { fm -> dialogError?.show(fm, DialogError.TAG) }

        } catch (e: Exception) {

        }
    }


    private fun showProgressDialog(message: String) {
        try {
            if (loader == null) {
                val loaderBuilder = AlertDialog.Builder(context)
                loaderBuilder.setCancelable(false)
                val loadingView = LayoutInflater.from(context).inflate(R.layout.app_loading_view, null)
                val gifImgView = loadingView.findViewById<ImageView>(R.id.img_loader)
                Glide.with(context).load(R.raw.lottie_loader).into(gifImgView)
                loaderBuilder.setView(loadingView)
                loader = loaderBuilder.create()
                loader?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            loader?.show()
        } catch (e: Exception) {

        }

    }

    private fun hideProgressDialog() {
        loader?.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideProgressDialog()
        mPresenter.clearDisposable()
        mPresenter.detachView()
    }

    override fun onDestroy() {
        viewUnbinder?.let {
            viewUnbinder!!.unbind()
            viewUnbinder = null
        }
        super.onDestroy()
    }


    override fun getContext(): Context {
        return requireActivity()
    }

    override fun onNetworkCallStarted(loadingMessage: String) {
        if (activity != null)
            showProgressDialog(loadingMessage)
    }

    override fun onNetworkCallEnded() {
        if (activity != null)
            hideProgressDialog()
    }

    override fun onNetworkUnavailable() {
        if (activity != null)
            showErrorDialog(
                title = getString(R.string.no_internet_connection),
                message = getString(R.string.no_internet_msg),
                titleFullRed = true
            )
    }

    override fun onTestError(error: String) {
        showErrorDialog(
            //title = getString(R.string.error),
            title = error,
            message = getString(R.string.server_error),
            titleFullRed = true
        )
    }

    override fun onServerError() {
        if (activity != null)
            showErrorDialog(
                title = getString(R.string.error),
                message = getString(R.string.server_error),
                titleFullRed = true
            )
    }

    override fun onNetworkError() { //TODO::need to update
        if (activity != null)
            showErrorDialog(
                title = getString(R.string.error),
                message = getString(R.string.server_error),
                titleFullRed = true
            )
    }

    override fun onTimeOutError() {
        if (activity != null)
            showErrorDialog(
                title = getString(R.string.error),
                message = getString(R.string.server_error),
                titleFullRed = true
            )
    }

    override fun onUserDidTooManyAttempts(errorMsg: String) {
        mAlertService.showToast(context, errorMsg)
    }

    override fun onUserUnauthorized(errorMessage: String) {
        if (activity != null) {
            mPrefManager.clearPreference()
            val bundle = Bundle()
            bundle.putSerializable(IntentKeys.IS_COME_FROM_NOTIFICATION, false)
            val loginIntent = Intent(context, LoginActivity::class.java)
            loginIntent.putExtra(IntentKeys.DATA_BUNDLE, bundle)
            loginIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(loginIntent)
        }
    }

    override fun onUserDisabled(errorMsg: String) {
        showErrorDialog(title = null, message = errorMsg)
    }

    override fun applyForcePinReset() {
       //Navigator.sharedInstance.navigate(context, ChangePinActivity::class.java)
    }

    override fun onExpectationFailed(message: String) {
        showErrorDialog(title = null, message = message)
    }

    override fun onValidationFailed(messages: LinkedHashMap<String, String>) {
        showErrorDialog(title = null, message = messages.values.first())
    }

    fun showAlert(title: String? = null, message: String) {
        mAlertService.showAlert(getContext(), title, message)
    }


    override fun onSystemUpgrading() {
        if (activity != null)
            mAlertService.showConfirmationAlert(
                context,
                getString(R.string.maintenance_title),
                getString(R.string.maintenance_message),
                null,
                "Okey",
                object : MyAlertService.AlertListener {
                    override fun negativeBtnDidTapped() {

                    }

                    override fun positiveBtnDidTapped() {
                        exitProcess(0)
                    }
                })
    }


    fun loadAppLanguage() {
        if (activity != null) {
//            mAppLanguage = mPrefManager.getString(Keys.APP_LANGUAGE.name, AppLanguage.ENGLISH.name)
//                when (mAppLanguage) {
//                    AppLanguage.BENGALI.name -> {
//                        AppUtils.shared.setLocale(getContext(), AppLanguage.BENGALI)
//                        context.getTheme().applyStyle(R.style.AppThemeBangla, true);
//                    }
//                    AppLanguage.ENGLISH.name -> {
//                        AppUtils.shared.setLocale(getContext(), AppLanguage.ENGLISH)
//                        context.getTheme().applyStyle(R.style.AppThemeEnglish, true);
//                    }
//                }
//            }

        }


    }
}