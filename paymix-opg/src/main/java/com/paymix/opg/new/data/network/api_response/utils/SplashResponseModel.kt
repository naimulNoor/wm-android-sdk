package com.walletmix.paymixbusiness.data.network.api_response.utils

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SplashResponseModel(
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
        @SerializedName("androidAppVersion")
        val androidAppVersion: Int,

        @Expose
        @SerializedName("iosAppVersion")
        val iosAppVersion: Int,

        @Expose
        @SerializedName("termsAndCondition")
        val termsAndCondition: String,

        @Expose
        @SerializedName("privacyPolicy")
        val privacyPolicy: String,
        @Expose
        @SerializedName("termsAndConditionBn")
        val termsAndConditionBn: String,

        @Expose
        @SerializedName("privacyPolicyBn")
        val privacyPolicyBn: String,
        @Expose
        @SerializedName("banks")
        val banks: HashMap<String,String>,
        @Expose
        @SerializedName("cards")
        val cards: HashMap<String,String>,
    )
}