package com.walletmix.paymixbusiness.data.network.api_response.dashboard

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BannerResponseModel (
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
        @SerializedName("banners")
        val banners: ArrayList<Banners>,

        ){
        data class Banners(
            @Expose
            @SerializedName("title")
            val title: String,
            @Expose
            @SerializedName("description")
            val description: String,
            @Expose
            @SerializedName("image")
            val image: String
        )

    }
}