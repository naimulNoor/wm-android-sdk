package com.walletmix.paymixbusiness.service.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.liveData
//import com.walletmix.paymixbusiness.data.network.api_response.transaction.DataL
import com.walletmix.paymixbusiness.data.network.api_service.TransectionApiService
import java.lang.Exception
import javax.inject.Inject


class  TransectionRepository @Inject constructor(private val tranxApi: TransectionApiService) {

//    fun getTransectionList() = Pager(
//        config = PagingConfig(pageSize = 20, maxSize = 100),
//        //pagingSourceFactory = { TransactionsPagingSource(tranxApi) }
//    ).liveData
}
