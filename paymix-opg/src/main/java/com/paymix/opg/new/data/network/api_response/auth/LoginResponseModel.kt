package com.walletmix.paymixbusiness.data.network.api_response.auth


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponseModel(
    @Expose
    @SerializedName("success")
    val success: Boolean,
    @Expose
    @SerializedName("data")
    val data: Data,
    @Expose
    @SerializedName("message")
    val message: String) {
    data class Data(
        @Expose
        @SerializedName("token")
        val token: String)
}
