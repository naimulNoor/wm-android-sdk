package com.walletmix.paymixbusiness.ui.view.auth.login

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import com.walletmix.paymixbusiness.base.MvpBaseActivity
import com.walletmix.paymixbusiness.data.network.api_response.auth.LoginResponseModel
import com.walletmix.paymixbusiness.data.prefs.PrefKeys
import com.walletmix.paymixbusiness.ui.view.WebViewActivity
import com.walletmix.paymixbusiness.ui.view.auth.forgotPassword.ForgotPasswordActivity
import com.walletmix.paymixbusiness.ui.view.auth.signup.SignUpActivity
import com.walletmix.paymixbusiness.ui.view.dashboard.DashBoardActivity
import com.walletmix.paymixbusiness.utils.Keys.IntentKeys
import com.walletmix.paymixbusiness.utils.Navigator
import com.walletmix.paymixbusiness.utils.PermissionUtils
import com.walletmix.paymixbusiness.utils.showToast
import com.walletmix.paymixbusiness.R
import com.walletmix.paymixbusiness.databinding.ActivityLoginBinding
import javax.inject.Inject


class LoginActivity : MvpBaseActivity<LoginPresenter>(), LoginContract.View{


    private val VISIBLE: HideReturnsTransformationMethod = HideReturnsTransformationMethod.getInstance()
    private val HIDDEN: PasswordTransformationMethod = PasswordTransformationMethod.getInstance()
    private var isPinVisible = false

    private lateinit var login:Login

    private var termsAndConditionsUrl = ""
    private var privacyPolicyUrl = ""

    var bundle=Bundle()

    @Inject
    lateinit var permissionUtils: PermissionUtils

    private lateinit var binding: ActivityLoginBinding


    override fun getContentView(): View {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        return view
    }

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {

        login=Login(getContext())


        termsAndConditionsUrl = mPrefManager.getString(PrefKeys.TERMS_AND_CONDITIONS)
        privacyPolicyUrl = mPrefManager.getString(PrefKeys.PRIVACY_POLICY)

        Log.d("termsAndConditionsUrl",termsAndConditionsUrl)
        Log.d("privacyPolicyUrl",privacyPolicyUrl)

        //  mPrefManager.clearPreference()
        mPrefManager.put(PrefKeys.TERMS_AND_CONDITIONS, termsAndConditionsUrl)
        mPrefManager.put(PrefKeys.PRIVACY_POLICY, privacyPolicyUrl)


        binding.btnLogin.setOnClickListener(onBtnLoginTapped)
        binding.llSignupBtn.setOnClickListener(onBtnSignupTapped)
        binding.tvForgotPassword.setOnClickListener(onBtnForgotTapped)
        binding.ivPinVisibility.setOnClickListener(passwordVisibilityTapped)

        binding.tvTermsAndConditions.setOnClickListener(termAndCondition)
        binding.tvPrivacyPolicy.setOnClickListener(privacyPolicy)

    }

    //on login tapped
    private  var onBtnLoginTapped=View.OnClickListener {
        loginBtnDidTapped()
    }
    //on create account tapped
    private  var onBtnSignupTapped=View.OnClickListener {
        createAccountBtnDidTapped()
    }
    //on forgot Password tapped
    private  var onBtnForgotTapped=View.OnClickListener {
        forgetPinDidTapped()
    }

    //on visible password tapped
    private  var passwordVisibilityTapped=View.OnClickListener {
        binding.ivPinVisibility.setOnClickListener {

            isPinVisible = !isPinVisible
            binding.ivPinVisibility.isSelected = isPinVisible
            binding.etPin.transformationMethod = if (isPinVisible) VISIBLE else HIDDEN
            binding.etPin.setSelection( binding.etPin.text!!.length)
        }
    }

    private var termAndCondition=View.OnClickListener {
        termAndCondition()
    }
    private var privacyPolicy=View.OnClickListener {
        privacyPolicy()
    }

    override fun loginBtnDidTapped() {

        val username = binding.etUserName.text.toString().trim()
        val password = binding.etPin.text.toString().trim()

        var data=login.checkValidation(username,password)
        if(data!=null){
            mPresenter.doLogin(data)

            //btn_login.disableFor1Sec()
        }

    }

    override fun createAccountBtnDidTapped() {
        Navigator.sharedInstance.navigate(this, SignUpActivity::class.java)
    }

    override fun forgetPinDidTapped() {
        Navigator.sharedInstance.navigate(this, ForgotPasswordActivity::class.java)
    }

    override fun loginDidSucceed(response: LoginResponseModel) {
        //AppAnalytics.sharedInstance.addCustomEvent(applicationContext, "Merchant Login")
        showToast("Login Successfully")

        //store data
        mPrefManager.put(PrefKeys.LOGGED_IN,true)
        mPrefManager.put(PrefKeys.TOKEN,response.data.token)

        Log.d("Token",response.data.token)

        //navigate
        Navigator.sharedInstance.navigate(getContext(),DashBoardActivity::class.java)
        finish()


    }

    private fun termAndCondition(){

        if (mNetworkUtils.isConnectedToNetwork(getContext())) {

                val bundle = Bundle()
                bundle.putString(IntentKeys.WEB_URL, termsAndConditionsUrl)
                bundle.putString(IntentKeys.TITLE, getString(R.string.terms_and_conditions))
                Navigator.sharedInstance.navigateWithBundle(
                    getContext(),
                    WebViewActivity::class.java,
                    IntentKeys.DATA_BUNDLE,
                    bundle
                )

        } else {
            showErrorDialog(
                getString(R.string.no_internet_connection),
                getString(R.string.no_internet_msg),
                titleFullRed = true
            )
        }
    }
    private fun privacyPolicy(){
        if (mNetworkUtils.isConnectedToNetwork(getContext())) {

                val bundle = Bundle()
                bundle.putString(IntentKeys.WEB_URL, privacyPolicyUrl)
                bundle.putString(IntentKeys.TITLE,getString(R.string.Privacy_Policy) )
                Navigator.sharedInstance.navigateWithBundle(
                    getContext(),
                    WebViewActivity::class.java,
                    IntentKeys.DATA_BUNDLE,
                    bundle
                )


        } else {
            showErrorDialog(
                getString(R.string.no_internet_connection),
                getString(R.string.no_internet_msg),
                titleFullRed = true
            )
        }
    }


    fun forgetPintask() {


    }




    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity();
    }


}


