package com.paymix.opg;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class InitPaymentApiResponse {

   @SerializedName("statusCode")
   @Expose
   String statusCode;


   @SerializedName("statusMsg")
   @Expose
   String statusMsg;


   @SerializedName("token")
   @Expose
   String token;

}
