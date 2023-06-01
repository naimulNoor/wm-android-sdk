package com.walletmix.paymixbusiness.ui.view.auth.login

import com.walletmix.paymixbusiness.base.BaseContract
import com.walletmix.paymixbusiness.data.network.api_response.auth.LoginResponseModel

interface LoginContract {

    interface View : BaseContract.View {
        fun loginBtnDidTapped()
        fun createAccountBtnDidTapped()
        fun forgetPinDidTapped()

        fun loginDidSucceed(response: LoginResponseModel)
    }

    interface Presenter : BaseContract.Presenter {
        fun doLogin(dataMap: HashMap<String, String>)
    }

}