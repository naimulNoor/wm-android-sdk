package com.walletmix.paymixbusiness.data.network.api_response.transaction

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.Date

data class TransactionDetailsResponseModel (

//    @Expose
//    @SerializedName("success")
//    var success : Boolean,
//    @Expose
//    @SerializedName("data")
//    var data : Data,
//    @Expose
//    @SerializedName("message")
//    var message : String

    @SerializedName("success" ) var success : Boolean? = null,
    @SerializedName("data"    ) var data    : Data?    = Data(),
    @SerializedName("message" ) var message : String?  = null
)

data class Data (

    @SerializedName("bpp_wmx_txn_id"       ) var bppWmxTxnId        : Int?                = null,
    @SerializedName("bpp_bank"             ) var bppBank            : String?             = null,
    @SerializedName("bpp_ref_id"           ) var bppRefId           : String?             = null,
    @SerializedName("bpp_is_checked"       ) var bppIsChecked       : String?             = null,
    @SerializedName("ipp_attempt_time"     ) var ippAttemptTime     : String?             = null,
    @SerializedName("ipp_customer_details" ) var ippCustomerDetails : String?             = null,
    @SerializedName("bpp_merchant_amount"  ) var bppMerchantAmount  : Int?                = null,
    @SerializedName("bpp_bank_amount"      ) var bppBankAmount      : Double?             = null,
    @SerializedName("bpp_extra_charge"     ) var bppExtraCharge     : String?             = null,
    @SerializedName("bpp_request_ip"       ) var bppRequestIp       : String?             = null,
    @SerializedName("bpp_is_paid"          ) var bppIsPaid          : String?             = null,
    @SerializedName("bpp_service_ratio"    ) var bppServiceRatio    : Int?                = null,
    @SerializedName("bpp_wmx_charge"       ) var bppWmxCharge       : Double?             = null,
    @SerializedName("bpp_txn_time"         ) var bppTxnTime         : String?             = null,
    @SerializedName("m_url"                ) var mUrl               : String?             = null,
    @SerializedName("ipp_csutomer_details" ) var ippCsutomerDetails : IppCsutomerDetails? = IppCsutomerDetails()

)
//data class Data (
//
//    @Expose
//    @SerializedName("bpp_wmx_txn_id")
//    var bppWmxTxnId: Int,
//    @Expose
//    @SerializedName("bpp_bank")
//    var bppBank: String?,
//    @Expose
//    @SerializedName("bpp_ref_id")
//    var bppRefId: String?,
//    @Expose
//    @SerializedName("bpp_is_checked")
//    var bppIsChecked: String?,
//    @Expose
//    @SerializedName("ipp_attempt_time")
//    var ippAttemptTime: String?,
//    @Expose
//    @SerializedName("ipp_customer_details")
//    var ippCustomerDetails : String?,
//    @Expose
//    @SerializedName("bpp_merchant_amount")
//    var bppMerchantAmount: Int,
//    @Expose
//    @SerializedName("bpp_bank_amount")
//    var bppBankAmount: Double,
//    @Expose
//    @SerializedName("bpp_extra_charge")
//    var bppExtraCharge: String?,
//    @Expose
//    @SerializedName("bpp_request_ip")
//    var bppRequestIp: String,
//    @Expose
//    @SerializedName("bpp_is_paid")
//    var bppIsPaid: String?,
//    @Expose
//    @SerializedName("bpp_service_ratio")
//    var bppServiceRatio: Int,
//    @Expose
//    @SerializedName("bpp_wmx_charge")
//    var bppWmxCharge: Int,
//    @Expose
//    @SerializedName("bpp_txn_time")
//    var bppTxnTime : String,
//    @Expose
//    @SerializedName("m_url")
//    var mUrl: String,
//    @Expose
//    @SerializedName("ipp_csutomer_details")
//    var ippCsutomerDetails : IppCustomerDetails
//
//)

data class IppCsutomerDetails (

    @SerializedName("customer_name"     ) var customerName     : String? = null,
    @SerializedName("customer_email"    ) var customerEmail    : String? = null,
    @SerializedName("customer_add"      ) var customerAdd      : String? = null,
    @SerializedName("customer_city"     ) var customerCity     : String? = null,
    @SerializedName("customer_state"    ) var customerState    : String? = null,
    @SerializedName("customer_country"  ) var customerCountry  : String? = null,
    @SerializedName("customer_postcode" ) var customerPostcode : String? = null,
    @SerializedName("customer_phone"    ) var customerPhone    : String? = null

)

//data class IppCustomerDetails (
//
//    @Expose
//    @SerializedName("customer_name")
//    var customerName: String,
//    @Expose
//    @SerializedName("customer_email")
//    var customerEmail: String,
//    @Expose
//    @SerializedName("customer_add")
//    var customerAdd : String,
//    @Expose
//    @SerializedName("customer_city")
//    var customerCity: String,
//    @Expose
//    @SerializedName("customer_state")
//    var customerState: String,
//    @Expose
//    @SerializedName("customer_country")
//    var customerCountry: String,
//    @Expose
//    @SerializedName("customer_postcode")
//    var customerPostcode: String,
//    @Expose
//    @SerializedName("customer_phone")
//    var customerPhone: String
//
//)

