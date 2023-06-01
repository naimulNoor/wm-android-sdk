package com.paymix.opg.di.module

import android.content.Context
import com.paymix.opg.data.network.RetrofitApiClient

import com.paymix.opg.data.prefs.PreferenceManager
import com.paymix.opg.utils.NetworkUtils
import com.walletmix.paymixbusiness.data.network.ApiServiceBuilder
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofitApiClient(
        context: Context,
        networkUtils: NetworkUtils,
        preferenceManager: PreferenceManager
    ): Retrofit {
        return RetrofitApiClient.getRetrofit(context, networkUtils, preferenceManager)
    }

    @Provides
    @Singleton
    fun provideApiServiceBuilder(retrofit: Retrofit): ApiServiceBuilder {
        return ApiServiceBuilder(retrofit)
    }

    //////////////////////////////////////////////////////////////////

//    @Provides
//    @Singleton
//    fun provideUserApiService(apiServiceBuilder: ApiServiceBuilder): OpgApiService {
//        return apiServiceBuilder.buildService(OpgApiService::class.java)
//    }
//




}