package com.walletmix.paymixbusiness.data.network.api_response.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SignUpResponseModel(
    @Expose
    @SerializedName("success")
    val success: Boolean,
    @Expose
    @SerializedName("data")
    val data: Data,
    @Expose
    @SerializedName("message")
    val message: String
) {
    data class Data(
        @Expose
        @SerializedName("token")
        val token: String,
        @Expose
        @SerializedName("username")
        val username: String,
        @Expose
        @SerializedName("email")
        val email: String,
        @Expose
        @SerializedName("contact")
        val contact: String,

        @Expose
        @SerializedName("password")
        val password: String,
        @Expose
        @SerializedName("confirm_password")
        val confirm_password: String,
        @Expose
        @SerializedName("terms_and_condition")
        val terms_and_condition: String,




    )
}