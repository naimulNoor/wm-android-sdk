package com.walletmix.paymixbusiness.data.network.api_response.transaction

import com.google.gson.annotations.SerializedName


data class UploadInvoiceResponceModel (

    @SerializedName("code"    ) var code    : Int?    = null,
    @SerializedName("msg"     ) var msg     : String? = null,
    @SerializedName("result"  ) var result  : Result? = Result(),
    @SerializedName("curTime" ) var curTime : Int?    = null

)

data class Result (

    @SerializedName("key" ) var key : String? = null,
    @SerializedName("sid" ) var sid : String? = null

)