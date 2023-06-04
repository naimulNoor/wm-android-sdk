package com.paymix.opg.apiclient.data.request;

import com.google.gson.JsonElement;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface CheckPaymentService {

    @FormUrlEncoded
    @POST
    Call<JsonElement> checkPayment(@Url String url, @FieldMap Map<String, String> params);

}