package com.walletmix.paymixbusiness.ui.view.auth.signup

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.fragment.app.Fragment
import com.walletmix.paymixbusiness.base.MvpBaseActivity
import com.walletmix.paymixbusiness.data.network.api_response.auth.SignUpResponseModel
import com.walletmix.paymixbusiness.ui.view.dashboard.DashBoardActivity
import com.walletmix.paymixbusiness.utils.Navigator
import com.walletmix.paymixbusiness.utils.PermissionUtils
import com.walletmix.paymixbusiness.utils.showToast
import com.walletmix.paymixbusiness.R
import com.walletmix.paymixbusiness.databinding.ActivitySignUpBinding
import javax.inject.Inject

class SignUpActivity : MvpBaseActivity<SignUpPresenter>(), SignUpContract.View {

    private val VISIBLE: HideReturnsTransformationMethod =
        HideReturnsTransformationMethod.getInstance()
    private val HIDDEN: PasswordTransformationMethod = PasswordTransformationMethod.getInstance()
    private var isPinVisible = false

    private lateinit var signup: SignUp

    var bundle=Bundle()

    @Inject
    lateinit var permissionUtils: PermissionUtils

    private lateinit var binding: ActivitySignUpBinding

    override fun getContentView(): View {
        binding = ActivitySignUpBinding .inflate(layoutInflater)
        val view = binding.root
        return view
    }

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {

        signup= SignUp(getContext())



        binding.btnCreateAccount.setOnClickListener(onBtnCreateAccountTapped)
        binding.ivPinVisibility.setOnClickListener(passwordVisibilityTapped)
        binding.ivPinVisibilityConfirm.setOnClickListener(confirmPasswordVisibilityTapped)



    }

   //on create account tapped
    private  var onBtnCreateAccountTapped=View.OnClickListener {
       signupBtnDidTapped()
    }

    //on visible password tapped
    private  var passwordVisibilityTapped=View.OnClickListener {
        passwordVisibility()
    }

    //on visible confirm password tapped
    private  var confirmPasswordVisibilityTapped=View.OnClickListener {
        confirmPasswordVisibility()
    }



    override fun signupBtnDidTapped() {

        val username = binding.etUserName.text.toString().trim()
        val merchentName = binding.etMerchentName.text.toString().trim()
        val email = binding.etUserEmail.text.toString().trim()
        val contact = binding.etUserContract.text.toString().trim()
        val website = binding.etUserWebsite.text.toString().trim()
        val confirmPassword = binding.etPinConfirm.text.toString().trim()
        val password = binding.etPin.text.toString().trim()
        val didAcceptedTermsAndConditions = binding.checkboxTermsAndConditions.isChecked

        val data=signup.checkSignupValidation(username,merchentName, email,contact,website,password,confirmPassword,didAcceptedTermsAndConditions)
        if(data!=null){
            mPresenter.doSignUp(data)

        }else{

        }



    }

    override fun signupDidSucceed(response: SignUpResponseModel) {
        //AppAnalytics.sharedInstance.addCustomEvent(applicationContext, "Merchant Signup")
        showToast("Create Account Successfully")

//        loadFragment(ProfileFragment.newInstance())

        Navigator.sharedInstance.navigate(getContext(),DashBoardActivity::class.java)
        finish()

    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_frame, fragment)
            .commitAllowingStateLoss()
    }

    private fun passwordVisibility(){
        isPinVisible = !isPinVisible
        binding.ivPinVisibility.isSelected = isPinVisible
        binding.etPin.transformationMethod = if (isPinVisible) VISIBLE else HIDDEN
        binding.etPin.setSelection(binding.etPin.text!!.length)
    }

    private fun confirmPasswordVisibility(){
        isPinVisible = !isPinVisible
        binding.ivPinVisibilityConfirm.isSelected = isPinVisible
        binding.etPinConfirm.transformationMethod = if (isPinVisible) VISIBLE else HIDDEN
        binding.etPinConfirm.setSelection(binding.etPinConfirm.text!!.length)
    }


}