package com.walletmix.paymixbusiness.ui.view.auth.forgotPassword

import com.walletmix.paymixbusiness.base.BaseContract

interface ForgotPasswordContract {

    interface View : BaseContract.View {

        fun navigateToLogin()
    }

    interface Presenter : BaseContract.Presenter {

    }
}