package com.walletmix.paymixbusiness.data.network.api_response.transaction

import com.google.gson.JsonElement
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.walletmix.paymixbusiness.data.network.api_response.*




data class TransactionResponseModel (
    @Expose
    @SerializedName("success")
    val success: Boolean,
    @Expose
    @SerializedName("data")
    val data: Transactions,
    @Expose
    @SerializedName("message")
    val message: String
)





data class Transactions (
    @SerializedName("current_page")
    val currentPage: Long,

    @SerializedName("data")
    val data: ArrayList<TransactionData>,

    @SerializedName("first_page_url")
    val firstPageURL: String,

    @SerializedName("form")
    val from: Long,

    @SerializedName("next_page_url")
    val nextPageURL: String?,

    @SerializedName("path")
    val path: String,

    @SerializedName("per_page")
    val perPage: String,

    @SerializedName("prev_page_url")
    val prevPageURL: String?,

    @SerializedName("to")
    val to: Long
)


data class TransactionData (
    @SerializedName("merchant_order_id")
    val merchantOrderID: String,

    @SerializedName("bpp_wmx_txn_id")
    val bppWmxTxnID: String,

    @SerializedName("bpp_bank")
    val bppBank: String,

    @SerializedName("bpp_bank_amount")
    val bppBankAmount: String,

    @SerializedName("bpp_txn_f_status")
    val bppTransactionStatus: String?,

    @SerializedName("bpp_created_at")
    val bppCreatedAt: String,

)



