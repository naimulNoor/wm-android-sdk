package com.walletmix.paymixbusiness.data.network.api_response.utils


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BaseResponseModel(
    @Expose
    @SerializedName("success")
    val success: Boolean,
    @Expose
    @SerializedName("data")
    val data: Any,
    @Expose
    @SerializedName("message")
    val message: String
    )

