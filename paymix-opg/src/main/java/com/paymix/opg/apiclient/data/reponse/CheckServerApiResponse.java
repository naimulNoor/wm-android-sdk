package com.paymix.opg.apiclient.data.reponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckServerApiResponse {

    @SerializedName("selectedServer")
    @Expose
    public
    boolean isServerSelected;

    @SerializedName("statusMsg")
    @Expose
    public
    String statusMsg;

    @SerializedName("url")
    @Expose
    public
    String initPaymentUrl;

    @SerializedName("bank_payment_url")
    @Expose
    public
    String bankPaymentUrl;

}
