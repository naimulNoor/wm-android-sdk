package com.walletmix.paymixbusiness.ui.view.auth.resetPassword

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import com.walletmix.paymixbusiness.data.network.api_response.merchant.MerchantUpdateResponse
import com.walletmix.paymixbusiness.ui.view.auth.resetPassword.ResetPassword
import com.walletmix.paymixbusiness.ui.view.auth.resetPassword.ResetPasswordContract
import com.walletmix.paymixbusiness.base.MvpBaseActivity
import com.walletmix.paymixbusiness.utils.Navigator
import com.walletmix.paymixbusiness.databinding.ActivityResetPasswordBinding
import com.walletmix.paymixbusiness.ui.view.auth.login.LoginActivity
import com.walletmix.paymixbusiness.utils.showToast

class ResetPasswordActivity :  MvpBaseActivity<ResetPasswordPresenter>(), ResetPasswordContract.View  {


     private val VISIBLE: HideReturnsTransformationMethod =
         HideReturnsTransformationMethod.getInstance()
     private val HIDDEN: PasswordTransformationMethod = PasswordTransformationMethod.getInstance()
     private var isPinVisible = false
    private lateinit var binding: ActivityResetPasswordBinding


    private lateinit var resetPassword: ResetPassword

     override fun getContentView(): View {
         binding = ActivityResetPasswordBinding.inflate(layoutInflater)
         val view = binding.root
         return view
     }

     override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {

         resetPassword= ResetPassword(getContext())


         var info= Build.MANUFACTURER +"-"+ Build.MODEL
         var androidversion= Build.VERSION.RELEASE
         Log.d("roni",androidversion)



         binding.btnReset.setOnClickListener{

//             Navigator.sharedInstance.navigate(this, VerifyOtpActivity::class.java)

             resetPasswordBtnDidTapped()

         }



         binding.ivPinVisibilityOld.setOnClickListener {

             isPinVisible = !isPinVisible
             binding.ivPinVisibilityOld.isSelected = isPinVisible
             binding.etOldPin.transformationMethod = if (isPinVisible) VISIBLE else HIDDEN
             binding.etOldPin.setSelection( binding.etOldPin.text!!.length)
         }
         binding.ivPinVisibilityNew.setOnClickListener {

             isPinVisible = !isPinVisible
             binding.ivPinVisibilityNew.isSelected = isPinVisible
             binding.etNewPin.transformationMethod = if (isPinVisible) VISIBLE else HIDDEN
             binding.etNewPin.setSelection(  binding.etNewPin.text!!.length)
         }
         binding.ivPinVisibilityConfirm.setOnClickListener {
             isPinVisible = !isPinVisible
             binding.ivPinVisibilityConfirm.isSelected = isPinVisible
             binding.etPinConfirm.transformationMethod = if (isPinVisible) VISIBLE else HIDDEN
             binding.etPinConfirm.setSelection( binding.etPinConfirm.text!!.length)
         }

     }

    override fun resetPasswordBtnDidTapped() {
        val oldPassword = binding.etOldPin.text.toString().trim()
        val password = binding.etNewPin.text.toString().trim()
        val confirmPassword = binding.etPinConfirm.text.toString().trim()



        val data=resetPassword.checkResetPasswordValidation(oldPassword,password,confirmPassword)
        if(data!=null){
            mPresenter.initChangePin(data)

        }else{
            this.showToast("Updated Password Failed")
        }
    }

    override fun initChangePinDidSucceed(response: MerchantUpdateResponse) {

        Navigator.sharedInstance.navigate(
            this,
            LoginActivity::class.java)

        finish()

//        Log.d("reset Pass Response",response.data.toString())

    }
    }



