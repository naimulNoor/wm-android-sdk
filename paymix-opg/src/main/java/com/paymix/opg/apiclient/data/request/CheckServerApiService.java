package com.paymix.opg.apiclient.data.request;

import com.paymix.opg.apiclient.data.reponse.CheckServerApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface CheckServerApiService {

    @GET
    Call<CheckServerApiResponse> checkServer(@Url String url);

}
