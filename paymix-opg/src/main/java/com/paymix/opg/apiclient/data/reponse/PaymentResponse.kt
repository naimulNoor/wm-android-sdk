package com.paymix.opg.apiclient.data.reponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class PaymentResponse {
    @SerializedName("wmx_id")
    @Expose
    var wmxId: String? = null

    @SerializedName("ref_id")
    @Expose
    var refId: String? = null

    @SerializedName("token")
    @Expose
    var token: String? = null

    @SerializedName("merchant_req_amount")
    @Expose
    var merchantReqAmount: String? = null

    @SerializedName("merchant_ref_id")
    @Expose
    var merchantRefId: String? = null

    @SerializedName("merchant_currency")
    @Expose
    var merchantCurrency: String? = null

    @SerializedName("merchant_amount_bdt")
    @Expose
    var merchantAmountBdt: String? = null

    @SerializedName("conversion_rate")
    @Expose
    var conversionRate: String? = null

    @SerializedName("service_ratio")
    @Expose
    var serviceRatio: String? = null

    @SerializedName("wmx_charge_bdt")
    @Expose
    var wmxChargeBdt: String? = null

    @SerializedName("emi_ratio")
    @Expose
    var emiRatio: String? = null

    @SerializedName("emi_charge")
    @Expose
    var emiCharge: String? = null

    @SerializedName("bank_amount_bdt")
    @Expose
    var bankAmountBdt: String? = null

    @SerializedName("discount_bdt")
    @Expose
    var discountBdt: String? = null

    @SerializedName("merchant_order_id")
    @Expose
    var merchantOrderId: String? = null

    @SerializedName("request_ip")
    @Expose
    var requestIp: String? = null

    @SerializedName("txn_status")
    @Expose
    var txnStatus: String? = null

    @SerializedName("extra_json")
    @Expose
    var extraJson: String? = null

    @SerializedName("txn_details")
    @Expose
    var txnDetails: String? = null

    @SerializedName("card_details")
    @Expose
    var cardDetails: String? = null

    @SerializedName("is_foreign")
    @Expose
    var isForeign = 0

    @SerializedName("payment_card")
    @Expose
    var paymentCard: String? = null

    @SerializedName("card_code")
    @Expose
    var cardCode: String? = null

    @SerializedName("payment_method")
    @Expose
    var paymentMethod: String? = null

    @SerializedName("init_time")
    @Expose
    var initTime: String? = null

    @SerializedName("txn_time")
    @Expose
    var txnTime: String? = null

    @SerializedName("statusCode")
    @Expose
    var statusCode = 0
}
