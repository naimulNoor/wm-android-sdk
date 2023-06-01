package com.walletmix.paymixbusiness.ui.view.tabs.transaction.transactionSummery

import com.walletmix.paymixbusiness.data.network.api_response.transaction.TransactionSummeryResonseModel
import com.walletmix.paymixbusiness.base.BaseContract

interface TransactionSummeryFrgContract {

    interface View : BaseContract.View {

        fun TransactionSummeryResponse(response: TransactionSummeryResonseModel)

    }

    interface Presenter : BaseContract.Presenter {

        fun TransactionSummery()
    }
}