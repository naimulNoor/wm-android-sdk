package com.paymix.opg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class InitTokenResponse {


    @SerializedName("statusCode")
    @Expose
    public int statusCode;

    @SerializedName("data")
    @Expose
    public Data data;

    @SerializedName("invalid_fields")
    @Expose
    public HashMap<String, String> invalidMap;


    public class Data {

        @SerializedName("status")
        @Expose
        public String status;

        @SerializedName("msg")
        @Expose
        public String message;
    }
}
