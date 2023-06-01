package com.walletmix.paymixbusiness.service.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
//import com.walletmix.paymixbusiness.data.network.api_response.transaction.DataL
import com.walletmix.paymixbusiness.data.network.api_service.TransectionApiService
import java.lang.Exception



class TransactionsPagingSource(private val transectionApi: TransectionApiService)  {

//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TransactionData> {
//        return try {
//            val page = params.key ?: 1
//            val response = transectionApi.getTransactionList(page.toString())
//
//            return LoadResult.Page(
//
//                data = response!!.data.transactions.data,
//                prevKey = if (page == 1) null else page - 1,
//                nextKey = if (response!!.data.transactions.nextPageURL==null) null else page + 1
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }

//    override fun getRefreshKey(state: PagingState<Int, DataL>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataL> {
//        TODO("Not yet implemented")
//    }

}