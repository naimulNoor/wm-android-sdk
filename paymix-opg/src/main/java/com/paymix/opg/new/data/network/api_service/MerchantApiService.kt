package com.walletmix.paymixbusiness.data.network.api_service

import com.walletmix.paymixbusiness.data.network.api_response.merchant.GetMarchentResponse
import com.walletmix.paymixbusiness.data.network.APIs
import io.reactivex.Single
import retrofit2.http.GET

interface MerchantApiService {


    ///////for Splash Screen
    @GET(APIs.GET_merchant_PROFILE)
    fun getmerchantProfileDetails(): Single<GetMarchentResponse?>


}