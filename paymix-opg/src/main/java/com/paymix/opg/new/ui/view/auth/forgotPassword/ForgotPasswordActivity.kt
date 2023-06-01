package com.walletmix.paymixbusiness.ui.view.auth.forgotPassword

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.walletmix.paymixbusiness.base.MvpBaseActivity
import com.walletmix.paymixbusiness.ui.view.auth.resetPassword.ResetPasswordActivity
import com.walletmix.paymixbusiness.utils.Navigator
import com.walletmix.paymixbusiness.R
import com.walletmix.paymixbusiness.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : MvpBaseActivity<ForgotPasswordPresenter>(), ForgotPasswordContract.View  {

    private lateinit var binding: ActivityForgotPasswordBinding
    override fun getContentView(): View {

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        return view
    }

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {

        var info= Build.MANUFACTURER +"-"+ Build.MODEL
        var androidversion= Build.VERSION.RELEASE
        Log.d("roni",androidversion)


        val otpBtn = findViewById<Button>(R.id.btn_send_otp);
        otpBtn.setOnClickListener{

            Navigator.sharedInstance.navigate(this, ResetPasswordActivity::class.java)

        }

    }

    override fun navigateToLogin() {
        TODO("Not yet implemented")
    }

}