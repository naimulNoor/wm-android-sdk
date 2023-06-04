package com.paymix.opg.apiclient.data.reponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InitPaymentApiResponse {

   @SerializedName("statusCode")
   @Expose
   public
   String statusCode;


   @SerializedName("statusMsg")
   @Expose
   public
   String statusMsg;


   @SerializedName("token")
   @Expose
   public
   String token;

}
