package com.walletmix.paymixbusiness.ui.view.dashboard

import com.walletmix.paymixbusiness.data.network.api_response.HomePageResponseModel
import com.walletmix.paymixbusiness.data.network.api_response.merchant.GetMarchentResponse
import com.walletmix.paymixbusiness.base.BaseContract


interface DashBoardContract {

    interface View : BaseContract.View {
        fun logOutDidSucceed()
        fun merchantProfileSuccess(response: GetMarchentResponse)
        fun homePageResponse(response: HomePageResponseModel)
    }

    interface Presenter : BaseContract.Presenter {

        fun getmerchantProfile()
        fun homePage()

        fun logout()
    }
}