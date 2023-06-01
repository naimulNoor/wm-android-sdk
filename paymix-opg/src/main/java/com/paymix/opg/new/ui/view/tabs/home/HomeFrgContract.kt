package com.walletmix.paymixbusiness.ui.view.tabs.home

import com.walletmix.paymixbusiness.data.network.api_response.HomePageResponseModel
import com.walletmix.paymixbusiness.base.BaseContract


interface HomeFrgContract {


    interface View : BaseContract.View {
        fun homePageResponse(response: HomePageResponseModel)
    }

    interface Presenter : BaseContract.Presenter {
        fun homePage()
    }
}