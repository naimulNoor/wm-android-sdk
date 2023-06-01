package com.walletmix.paymixbusiness.data.network.api_response
import com.google.gson.annotations.SerializedName


data class HomePageResponseModel (
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: MyData,
    @SerializedName("message")
    val message: String
){

}


data class MyData (
    @SerializedName("merchant_details")
    val merchantDetails: MerchantDetails,
    @SerializedName("banners"             )
    var banners            : ArrayList<Banners> = arrayListOf(),
    @SerializedName("transaction_summery" )
    var transactionSummery : TransactionSummery?= TransactionSummery(),
    @SerializedName("recent_transactions" )
    var recentTransactions : ArrayList<RecentTransactions> = arrayListOf(),
    @SerializedName("recent_comments"     )
    var recentComments     : ArrayList<RecentComments>     = arrayListOf()
)


data class Banners (
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("image")
    val image: String
)


data class MerchantDetails (
    @SerializedName("username")
    val username: String,

    @SerializedName("is_verified")
    val isVerified: Int,

    @SerializedName("merchant_name")
    val merchantName: String,

    @SerializedName("org_name")
    val orgName: String,

    @SerializedName("profile_photo")
    val profilePhoto: String
)


data class RecentComments (
    @SerializedName("wmx_id"     ) var wmxId     : Int?    = null,
    @SerializedName("title"      ) var title     : String? = null,
    @SerializedName("comment"    ) var comment   : String? = null,
    @SerializedName("created_at" ) var createdAt : String? = null
)


data class RecentTransactions (
    @SerializedName("wmx_trxn_id"      ) var wmxTrxnId     : Int?    = null,
    @SerializedName("bpp_txn_f_status" ) var bppTxnFStatus : String? = null,
    @SerializedName("bpp_bank_amount"  ) var bppBankAmount : Int?    = null
)


data class TransactionSummery (
    @SerializedName("total_success_amount" ) var totalSuccessAmount : String? = null,
    @SerializedName("total_success_count"  ) var totalSuccessCount  : Int?    = null,
    @SerializedName("payable"              ) var payable            : String? = null

)
