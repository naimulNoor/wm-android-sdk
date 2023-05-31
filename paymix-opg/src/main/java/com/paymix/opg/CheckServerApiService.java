package com.paymix.opg;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

interface CheckServerApiService {

    @GET
    Call<CheckServerApiResponse> checkServer(@Url String url);

}
