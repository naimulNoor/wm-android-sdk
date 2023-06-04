package com.paymix.opg.apiclient.data.reponse;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class BaseResponse {


    @SerializedName("success")
    @Expose
    public boolean success;

    @SerializedName("data")
    @Expose
    public JsonObject data;

    @SerializedName("message")
    @Expose
    public String message;


}
