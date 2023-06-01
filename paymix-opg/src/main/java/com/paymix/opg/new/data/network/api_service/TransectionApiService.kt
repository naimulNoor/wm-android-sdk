package com.walletmix.paymixbusiness.data.network.api_service

import com.walletmix.paymixbusiness.data.network.api_response.comment.CommentResponseModel
import com.walletmix.paymixbusiness.data.network.api_response.transaction.TransactionResponseModel
import com.walletmix.paymixbusiness.data.network.APIs
import io.reactivex.Single
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface TransectionApiService {

    ///Transaction
//    @POST(APIs.TRANSACTION_LIST)
//    fun getTransactionList(
//        @Path("page") id: String?,
//    ): TransactionResponseModel?

    @POST(APIs.TRANSACTION_LIST)
    fun getTransactionList(@Query("page") page: String): Single<TransactionResponseModel>

    ///transaction comment
    @FormUrlEncoded
    @POST(APIs.COMMENT_LIST)
    fun getCommentList(@FieldMap commentData: HashMap<String, String>): Single<CommentResponseModel>
}
