package com.walletmix.paymixbusiness.ui.view.tabs.transaction.transactionList


import com.walletmix.paymixbusiness.data.network.api_response.merchant.MerchantUpdateResponse
import com.walletmix.paymixbusiness.data.network.api_response.transaction.TransactionDetailsResponseModel
import com.walletmix.paymixbusiness.data.network.api_response.transaction.TransactionResponseModel
import com.walletmix.paymixbusiness.base.BaseContract
import okhttp3.MultipartBody

interface TransactionListFrgContract {


    interface View : BaseContract.View {

        fun TransactionListResponse(response: TransactionResponseModel)
        fun TransactionDetailsResponse(response: TransactionDetailsResponseModel)

        fun UploadInvoiceResponse(response: MerchantUpdateResponse)

    }

    interface Presenter : BaseContract.Presenter {

        fun TransactionList(page:Int,
                            WMtrnxId:String,
                            orderId:String,
                            cardNumber:String,
                            minAmount:String,
                            maxAmount:String,
                            dateRange:String?,
                            bank:String?,
                            paymentModule:String?,
                            trnxID:String,)
        fun TransactionDetails(id:String)

        fun UploadInvoice(id: String, attachment: MultipartBody.Part?)
    }
}