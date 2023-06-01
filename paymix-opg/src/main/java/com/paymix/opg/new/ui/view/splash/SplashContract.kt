package com.walletmix.paymixbusiness.ui.view.splash

import com.walletmix.paymixbusiness.base.BaseContract
import com.walletmix.paymixbusiness.data.network.api_response.utils.SplashResponseModel

interface SplashContract {
    interface View : BaseContract.View {

        fun appVersionDidReceived(response: SplashResponseModel)
        fun navigateTask()
    }

    interface Presenter : BaseContract.Presenter {
        fun getAppVersion()
    }
}