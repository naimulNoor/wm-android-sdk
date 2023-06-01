package com.walletmix.paymixbusiness.data.network.api_response.transaction

import com.google.gson.annotations.SerializedName

data class TransactionSummeryResonseModel (

    @SerializedName("success" ) var success : Boolean? = null,
    @SerializedName("data"    ) var data    : TransactionSummeryData?    = TransactionSummeryData(),
    @SerializedName("message" ) var message : String?  = null

)

data class TransactionSummeryData (

    @SerializedName("total_wmx_payable"                ) var totalWmxPayable               : Double? = null,
    @SerializedName("total_transaction_amount"         ) var totalTransactionAmount        : Double?    = null,
    @SerializedName("transaction_count"                ) var transactionCount              : Int?    = null,
    @SerializedName("total_foreign_transaction_amount" ) var totalForeignTransactionAmount : Double?    = null,
    @SerializedName("foreign_transaction_count"        ) var foreignTransactionCount       : Int?    = null

)