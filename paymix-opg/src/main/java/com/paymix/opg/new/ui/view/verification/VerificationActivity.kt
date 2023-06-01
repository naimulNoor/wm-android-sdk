package com.walletmix.paymixbusiness.ui.view.verification

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.walletmix.paymixbusiness.databinding.ActivityVerificationBinding
import com.walletmix.paymixbusiness.base.MvpBaseActivity

class VerificationActivity  : MvpBaseActivity<VerificationPresenter>(), VerificationContract.View{


    private lateinit var binding: ActivityVerificationBinding


    override fun getContentView(): View {
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        val view = binding.root
        return view
    }

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {


    }
}