package com.paymix.opg;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class CheckServerApiResponse {

    @SerializedName("selectedServer")
    @Expose
    boolean isServerSelected;

    @SerializedName("statusMsg")
    @Expose
    String statusMsg;

    @SerializedName("url")
    @Expose
    String initPaymentUrl;

    @SerializedName("bank_payment_url")
    @Expose
    String bankPaymentUrl;

}
