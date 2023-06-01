package com.walletmix.paymixbusiness.data.network.api_response.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetOtpResponse(
    @Expose
    @SerializedName("success")
    val success: Boolean,
    @Expose
    @SerializedName("data")
    val data: ArrayList<Any>,
    @Expose
    @SerializedName("message")
    val message: String
)