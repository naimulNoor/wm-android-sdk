package com.paymix.opg.apiclient.data.request;



import com.google.gson.JsonObject;
import com.paymix.opg.apiclient.data.reponse.InitTokenResponse;
import com.paymix.opg.apiclient.data.reponse.InitPaymentApiResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface InitPaymentApiService {

    @FormUrlEncoded
    @POST
    Call<JsonObject> initPayment(@Url String url, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST
    Call<InitTokenResponse> initToken(@Url String url,
                                      @Field("reference_id") String referenceId,
                                      @Field("payment_token") String params,
                                      @Header("Authorization") String authorization,
                                      @Header("Accept") String accept);

    @GET
    Call<InitTokenResponse> bankPaymentProcess(@Url String url);

}
