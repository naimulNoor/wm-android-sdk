package com.walletmix.paymixbusiness.service.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.walletmix.paymixbusiness.data.network.api_service.TransectionApiService
import dagger.Module
import java.lang.Exception
import javax.inject.Inject


@Module
class TransactionViewModel constructor(repository: TransectionRepository) : ViewModel() {
        //val list = repository.getTransectionList().cachedIn(viewModelScope)
}

