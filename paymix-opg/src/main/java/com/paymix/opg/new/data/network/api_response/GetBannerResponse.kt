package com.walletmix.paymixbusiness.data.network.api_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetBannerResponse(

    @Expose
    @SerializedName("success")
    val success: Boolean,
    @Expose
    @SerializedName("data")
    val data: ArrayList<SingleBanner?>,
    @Expose
    @SerializedName("message")
    val message: String
) {
    data class SingleBanner(
        @Expose
        @SerializedName("title")
        val title: String?,
        @Expose
        @SerializedName("details")
        val details: String?,
        @Expose
        @SerializedName("image")
        val image: String,
        @Expose
        @SerializedName("banner_type")
        val bannerType: String,
        @Expose
        @SerializedName("external_link")
        val externalLink: String?
    ): Serializable
}
