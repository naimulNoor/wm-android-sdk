package com.walletmix.paymixbusiness.data.network.api_service

import com.walletmix.paymixbusiness.data.network.api_response.HomePageResponseModel
import com.walletmix.paymixbusiness.data.network.api_response.merchant.MerchantUpdateResponse
import com.walletmix.paymixbusiness.data.network.api_response.transaction.TransactionDetailsResponseModel
import com.walletmix.paymixbusiness.data.network.api_response.transaction.TransactionResponseModel
import com.walletmix.paymixbusiness.data.network.api_response.transaction.TransactionSummeryResonseModel
import com.walletmix.paymixbusiness.data.network.APIs
import com.walletmix.paymixbusiness.data.network.api_response.utils.SplashResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface UtilsApiService {


    ///////for Splash Screen
    @GET(APIs.SPLASH)
    fun getSplashResources(): Single<SplashResponseModel?>

    ///////for Banner
    @GET(APIs.HOMEPAGE)
    fun getHomePage(): Single<HomePageResponseModel?>

    /////Transaction
    @POST(APIs.TRANSACTION_LIST)
    fun  getTransactionList(
        @Query("page") page: String?,
        @Query("wmx_trxn_id") wmxTrnxId: String?,
        @Query("merchant_order_id") orderId: String?,
        @Query("card_number") cardNumber: String?,
        @Query("min_amount") minAmmount: String?,
        @Query("max_amount") maxAmmount: String?,
        @Query("date_range",encoded = true) dateRange: String?,
        @Query("bank") bank: String?,
        @Query("payment_module") paymentModule: String?,
        @Query("transaction_id") trnxId: String?,
    ): Single<TransactionResponseModel?>

    @GET(APIs.TRANSACTION_SUMMERY)
    fun getTransactionSummery(): Single<TransactionSummeryResonseModel?>

    @FormUrlEncoded
    @POST(APIs.TRANSACTION_DETAILS)
    fun getTransactionDetailsData(@Field("id") id: String): Single<TransactionDetailsResponseModel>

    @FormUrlEncoded
    @POST(APIs.TRANSACTION_LIST)
    fun doFilter(@FieldMap filterData : HashMap<String, String>): Single<TransactionResponseModel>



//    @Multipart
//    @POST(APIs.UPLOAD_INVOICE)
//    fun uploadInvoice(
//        @Path(value = "id") txnId: String?,
//        @Part("invoice") fileName: MultipartBody.Part?,
//    ): Single<MerchantUpdateResponse>
//

    @Multipart
    @POST(APIs.UPLOAD_INVOICE)
    fun uploadInvoice(
        @Path("id") id: String?,
        @Part attachment: MultipartBody.Part?): Single<MerchantUpdateResponse>

}