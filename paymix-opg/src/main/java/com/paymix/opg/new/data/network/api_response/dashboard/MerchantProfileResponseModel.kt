package com.walletmix.paymixbusiness.data.network.api_response.dashboard


data class MerchantProfileResponseModel (
    val success: Boolean,
    val data: Data,
    val message: String
)

data class Data (
    val details: Details
)

data class Details (
    val username: String,
    val email: String,
    val contactNo: String,
    val websiteURL: String,
    val merchantName: Any? = null,
    val orgContactNo: Any? = null,
    val orgName: Any? = null,
    val orgAddress: Any? = null,
    val orgPhone: Any? = null,
    val orgProduct: Any? = null,
    val orgMobileNo: Any? = null,
    val nidNo: Any? = null,
    val businessType: Any? = null,
    val passportNo: Any? = null,
    val bankName: Any? = null,
    val bankBranch: Any? = null,
    val bankAccountName: Any? = null,
    val bankAccountNo: Any? = null,
    val logo: String,
    val profilePhoto: String,
    val nid: String,
    val passport: String,
    val tradeLicence: Any? = null,
    val agreement: Any? = null,
    val suspensionLetter: Any? = null,
    val tinBin: Any? = null,
    val brandColor: Any? = null
)
