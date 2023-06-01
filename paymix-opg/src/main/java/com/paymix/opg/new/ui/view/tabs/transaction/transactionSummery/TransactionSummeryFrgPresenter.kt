package com.walletmix.paymixbusiness.ui.view.tabs.transaction.transactionSummery

import com.walletmix.paymixbusiness.data.network.api_response.transaction.TransactionSummeryResonseModel
import com.walletmix.paymixbusiness.data.network.api_service.UtilsApiService
import com.walletmix.paymixbusiness.R
import com.walletmix.paymixbusiness.base.BasePresenter
import com.walletmix.paymixbusiness.data.network.SSDisposableSingleObserver
import javax.inject.Inject

class TransactionSummeryFrgPresenter @Inject constructor(view: TransactionSummeryFrgContract.View) :
    BasePresenter<TransactionSummeryFrgContract.View>(view),
    TransactionSummeryFrgContract.Presenter {

    @Inject
    lateinit var utilsApiService: UtilsApiService

    override fun TransactionSummery() {
        if (this::utilsApiService.isInitialized) {
            mView?.onNetworkCallStarted(context.getString(R.string.please_wait))
            compositeDisposable?.add(
                utilsApiService.getTransactionSummery()
                    .subscribeOn(appSchedulerProvider.io())
                    .unsubscribeOn(appSchedulerProvider.computation())
                    .observeOn(appSchedulerProvider.ui())
                    .subscribeWith(object :
                        SSDisposableSingleObserver<TransactionSummeryResonseModel, TransactionSummeryFrgContract.View>(mView) {
                        override fun onRequestSuccess(response: TransactionSummeryResonseModel) {
                            mView?.TransactionSummeryResponse(response)
                        }

                    })
            )
        }
    }
}