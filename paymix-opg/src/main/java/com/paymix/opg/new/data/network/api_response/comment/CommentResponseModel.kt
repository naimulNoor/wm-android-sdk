package com.walletmix.paymixbusiness.data.network.api_response.comment


import com.google.gson.annotations.SerializedName

data class CommentResponseModel(
//    @Expose
//    @SerializedName("success")
//    val success: Boolean,
//    @Expose
//    @SerializedName("data")
//    val data: ArrayList<DataComment>,
//    @Expose
//    @SerializedName("message")
//    val message: String

    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("data") var data: ArrayList<DataComment> = arrayListOf(),
    @SerializedName("message") var message: String? = null
)

data class DataComment(
//    @Expose
//    @SerializedName("title")
//    val title: String,
//    @Expose
//    @SerializedName("comment")
//    val comment: String
    @SerializedName("title") var title: String? = null,
    @SerializedName("comment") var comment: String? = null
)
