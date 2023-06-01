package com.walletmix.paymixbusiness.ui.view.marchant.form

import com.walletmix.paymixbusiness.data.network.api_response.merchant.GetMarchentResponse
import com.walletmix.paymixbusiness.data.network.api_response.merchant.MerchantUpdateResponse
import com.walletmix.paymixbusiness.model.MerchantBank
import com.walletmix.paymixbusiness.model.MerchantOrganization
import com.walletmix.paymixbusiness.model.MerchantProfile
import com.walletmix.paymixbusiness.base.BaseContract
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.HashMap

interface MerchantFormContract {
    interface View : BaseContract.View {

        fun updateMerchantDetailsResponse(response: MerchantUpdateResponse)
        fun updateMerchantBankDetailsResponse(response: MerchantUpdateResponse)
        fun updateMerchantProfileResponse(response: MerchantUpdateResponse)
        fun updateMerchantDocumentResponse(response: MerchantUpdateResponse)
        fun getMerchantResponse(response: GetMarchentResponse)



    }

    interface Presenter : BaseContract.Presenter {
        fun getMerchantProfileDetails()
        fun setUpdateMerchantDetails(model: MerchantOrganization)
        fun setUpdateMerchantBankDetails(model: MerchantBank)
        fun setUpdateMerchantProfileDetails(model: MerchantProfile)
        fun setUpdateMerchantDocument(dataMap: HashMap<String, RequestBody>, attachment: MultipartBody.Part?)
    }
}