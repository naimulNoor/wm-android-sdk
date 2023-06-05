package com.paymix.opg.appInterface

import com.paymix.opg.apiclient.data.reponse.PaymentResponse


interface OPGResponseListener {
    fun intRequest(sandBox: Boolean,initPaymentUrl: String?)
    fun onProcessPaymentRequest(initPaymentUrl: String?)
    fun onSuccessPaymentRequest(statusCode:Int,response: PaymentResponse?)
    fun onFailedPaymentRequest(statusCode:Int,response: PaymentResponse?)
    fun onDeclinedPaymentRequest(statusCode:Int,response: PaymentResponse?)
    fun onFailed(message: String?)
}