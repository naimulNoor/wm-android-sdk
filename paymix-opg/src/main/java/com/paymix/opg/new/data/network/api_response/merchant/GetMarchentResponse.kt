package com.walletmix.paymixbusiness.data.network.api_response.merchant

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class GetMarchentResponse (
    @Expose
    @SerializedName("success")
    val success: Boolean,
    @Expose
    @SerializedName("data")
    val data: MyData,
    @Expose
    @SerializedName("message")
    val message: String
)

data class MyData (
    @Expose
    @SerializedName("details")
    val merchantDetails: MerchantDetails,

)

data class MerchantDetails (
    @Expose
    @SerializedName("username")
    val username: String?,
    @Expose
    @SerializedName("email")
    val email: String?,
    @Expose
    @SerializedName("contact_no")
    val contact_no: String?,
    @Expose
    @SerializedName("website_url")
    val website_url: String?,
    @Expose
    @SerializedName("merchant_name")
    val merchant_name: String?,
    @Expose
    @SerializedName("org_contact_no")
    val org_contact_no: String?,
    @Expose
    @SerializedName("org_name")
    val org_name: String?,
    @Expose
    @SerializedName("org_address")
    val org_address: String?,
    @Expose
    @SerializedName("org_phone")
    val org_phone: String?,
    @Expose
    @SerializedName("org_product")
    val org_product: String?,
    @Expose
    @SerializedName("org_mobile_no")
    val org_mobile_no: String?,
    @Expose
    @SerializedName("nid_no")
    val nid_no: String?,
    @Expose
    @SerializedName("business_type")
    val business_type: String?,
    @Expose
    @SerializedName("passport_no")
    val passport_no: String?,
    @Expose
    @SerializedName("bank_name")
    val bank_name: String?,
    @Expose
    @SerializedName("bank_branch")
    val bank_branch: String?,
    @Expose
    @SerializedName("bank_account_name")
    val bank_account_name: String?,
    @Expose
    @SerializedName("bank_account_no")
    val bank_account_no: String?,
    @Expose
    @SerializedName("logo")
    val logo: String?,
    @Expose
    @SerializedName("profile_photo")
    val profile_photo: String?,
    @Expose
    @SerializedName("nid")
    val nid: String?,
    @Expose
    @SerializedName("passport")
    val passport: String?,
    @Expose
    @SerializedName("trade_licence")
    val trade_licence: String?,
    @Expose
    @SerializedName("agreement")
    val agreement: String?,
    @Expose
    @SerializedName("suspension_letter")
    val suspencionLetter: String?,
    @Expose
    @SerializedName("tin_bin")
    val tinBin: String? ,
    @Expose
    @SerializedName("brand_color")
    val brandColor: String?
    )