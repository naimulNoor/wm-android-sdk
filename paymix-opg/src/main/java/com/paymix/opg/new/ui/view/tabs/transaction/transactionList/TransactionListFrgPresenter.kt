package com.walletmix.paymixbusiness.ui.view.tabs.transaction.transactionList

import android.util.Log
import com.walletmix.paymixbusiness.data.network.api_response.merchant.MerchantUpdateResponse
import com.walletmix.paymixbusiness.data.network.api_response.transaction.TransactionDetailsResponseModel
import com.walletmix.paymixbusiness.data.network.api_response.transaction.TransactionResponseModel
import com.walletmix.paymixbusiness.data.network.api_service.TransectionApiService
import com.walletmix.paymixbusiness.R
import com.walletmix.paymixbusiness.base.BasePresenter
import com.walletmix.paymixbusiness.data.network.SSDisposableSingleObserver
import com.walletmix.paymixbusiness.data.network.api_service.UtilsApiService
import okhttp3.MultipartBody
import javax.inject.Inject

class TransactionListFrgPresenter @Inject constructor(view: TransactionListFrgContract.View) :
    BasePresenter<TransactionListFrgContract.View>(view),
    TransactionListFrgContract.Presenter {


    @Inject
    lateinit var utilsApiService: UtilsApiService

    @Inject
    lateinit var transApiService: TransectionApiService


    override fun TransactionList(
        page:Int,
        WMtrnxId:String,
        orderId:String,
        cardNumber:String,
        minAmount:String,
        maxAmount:String,
        dateRange:String?,
        bank:String?,
        paymentModule:String?,
        trnxID:String,
    ) {
        Log.d("page::",page.toString())
        if (this::utilsApiService.isInitialized) {

            mView?.onNetworkCallStarted(context.getString(R.string.please_wait))
            compositeDisposable?.add(
                utilsApiService.getTransactionList(page.toString(),WMtrnxId,orderId,cardNumber,minAmount,maxAmount,dateRange,bank,paymentModule,trnxID)
                    .subscribeOn(appSchedulerProvider.io())
                    .unsubscribeOn(appSchedulerProvider.computation())
                    .observeOn(appSchedulerProvider.ui())
                    .subscribeWith(object :
                        SSDisposableSingleObserver<TransactionResponseModel, TransactionListFrgContract.View>(mView) {
                        override fun onRequestSuccess(response: TransactionResponseModel) {
                            mView?.TransactionListResponse(response)
                        }

                    })
            )
        }
    }


    override fun TransactionDetails(id:String) {
         if (this::utilsApiService.isInitialized) {
             mView?.onNetworkCallStarted(context.getString(R.string.please_wait))
             compositeDisposable?.add(
                 utilsApiService.getTransactionDetailsData(id)
                     .subscribeOn(appSchedulerProvider.io())
                     .unsubscribeOn(appSchedulerProvider.computation())
                     .observeOn(appSchedulerProvider.ui())
                     .subscribeWith(object :
                         SSDisposableSingleObserver<TransactionDetailsResponseModel, TransactionListFrgContract.View>(mView) {
                         override fun onRequestSuccess(response: TransactionDetailsResponseModel) {
                             mView?.TransactionDetailsResponse(response)
                         }

                     })
             )
         }
     }

    override fun UploadInvoice(
        id:String,
        attachment: MultipartBody.Part?
    ) {

        Log.d("Upload Invoice ","Out of if ")

        if (this::utilsApiService.isInitialized) {
            Log.d("Upload Invoice ","Inner if ")
            mView?.onNetworkCallStarted(context.getString(R.string.please_wait))
            compositeDisposable?.add(
                utilsApiService.uploadInvoice(id,attachment)
                    .subscribeOn(appSchedulerProvider.io())
                    .unsubscribeOn(appSchedulerProvider.computation())
                    .observeOn(appSchedulerProvider.ui())
                    .subscribeWith(object :
                        SSDisposableSingleObserver<MerchantUpdateResponse, TransactionListFrgContract.View>(mView) {
                        override fun onRequestSuccess(response: MerchantUpdateResponse) {
                            mView?.UploadInvoiceResponse(response)
                        }

                    })
            )
        }
    }


}