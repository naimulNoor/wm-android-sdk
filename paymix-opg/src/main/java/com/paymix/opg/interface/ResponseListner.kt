package com.paymix.opg.`interface`



interface OPGResponseListner {
    fun onSuccessfullySelectedServer(initPaymentUrl: String?, bankPaymentUrl: String?)
    fun onFailedToSelectServer(failedMessage: String?)
}