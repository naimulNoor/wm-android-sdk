package com.walletmix.paymixbusiness.data.network.api_service

import com.walletmix.paymixbusiness.data.network.api_response.merchant.GetMarchentResponse
import com.walletmix.paymixbusiness.data.network.api_response.merchant.MerchantUpdateResponse
import com.walletmix.paymixbusiness.model.MerchantBank
import com.walletmix.paymixbusiness.model.MerchantOrganization
import com.walletmix.paymixbusiness.model.MerchantProfile
import com.walletmix.paymixbusiness.data.network.APIs
import com.walletmix.paymixbusiness.data.network.api_response.auth.LoginResponseModel
import com.walletmix.paymixbusiness.data.network.api_response.auth.LogoutResponseModel
import com.walletmix.paymixbusiness.data.network.api_response.auth.SignUpResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


interface UserAuthApiService {

    //LOGIN
    @FormUrlEncoded
    @POST(APIs.LOGIN)
    fun doLogin(@FieldMap loginData: HashMap<String, String>): Single<LoginResponseModel>

    //SIGNUP
    @FormUrlEncoded
    @POST(APIs.REGISTRATION)
    fun doSignUp(@FieldMap signupData : HashMap<String, String>): Single<SignUpResponseModel>


    //RESET PASSWORD
    @FormUrlEncoded
    @POST(APIs.UPDATE_MERCHANT_PASSWORD)
    fun doResetPassword(@FieldMap resetPasswordData : HashMap<String, String>): Single<MerchantUpdateResponse>

    //LOGOUT
    @GET(APIs.LOGOUT)
    fun doLogout(): Single<LogoutResponseModel?>

    //PROFILE

    @GET(APIs.GET_MERCHANT_PROFILE)
    fun getMerchantProfile(): Single<GetMarchentResponse>


    @POST(APIs.UPDATE_MERCHANT_DETAILS)
    fun updateMerchantProfile(@Body model: MerchantProfile): Single<MerchantUpdateResponse>


    @POST(APIs.UPDATE_BANK_DETAILS)
    fun updateMerchantBank(@Body model: MerchantBank): Single<MerchantUpdateResponse>


    @POST(APIs.UPDATE_MERCHANT_PROFILE)
    fun updateMerchantOrganization(@Body model: MerchantOrganization): Single<MerchantUpdateResponse>




    @Multipart
    @POST(APIs.UPDATE_ATTACHMENT)
    fun updateMerchantAttatchment(
        @PartMap partMap: HashMap<String, RequestBody>,
        @Part attachment: MultipartBody.Part?): Single<MerchantUpdateResponse>



}