package com.walletmix.paymixbusiness.ui.view.auth.resetPassword

import com.walletmix.paymixbusiness.data.network.api_response.merchant.MerchantUpdateResponse
import com.walletmix.paymixbusiness.base.BaseContract

interface ResetPasswordContract {

    interface View : BaseContract.View {

        fun resetPasswordBtnDidTapped()
        fun initChangePinDidSucceed(response: MerchantUpdateResponse)

    }

    interface Presenter : BaseContract.Presenter {
        fun initChangePin(dataMap: HashMap<String, String>)
    }
}