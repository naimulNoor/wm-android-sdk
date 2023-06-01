package com.walletmix.paymixbusiness.ui.view.tabs.profile

import com.walletmix.paymixbusiness.data.network.api_response.merchant.GetMarchentResponse
import com.walletmix.paymixbusiness.data.network.api_response.merchant.MerchantUpdateResponse
import com.walletmix.paymixbusiness.base.BaseContract
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.HashMap

interface ProfileFrgContract {

    interface View : BaseContract.View {
        fun profilePageResponse(response: GetMarchentResponse)
        fun updateMerchantDocumentResponse(response: MerchantUpdateResponse)
    }

    interface Presenter : BaseContract.Presenter {
        fun profile()
        fun setUpdateProfileDocument(dataMap: HashMap<String, RequestBody>, attachment: MultipartBody.Part?)
    }
}