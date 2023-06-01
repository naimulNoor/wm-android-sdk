package com.walletmix.paymixbusiness.di.module

import android.content.Context
import com.walletmix.paymixbusiness.data.network.api_service.TransectionApiService
import com.walletmix.paymixbusiness.data.network.ApiServiceBuilder
import com.walletmix.paymixbusiness.data.network.RetrofitApiClient
import com.walletmix.paymixbusiness.data.network.api_service.MerchantApiService

import com.walletmix.paymixbusiness.data.network.api_service.UserApiService
import com.walletmix.paymixbusiness.data.network.api_service.UserAuthApiService
import com.walletmix.paymixbusiness.data.network.api_service.UtilsApiService
import com.walletmix.paymixbusiness.data.prefs.PreferenceManager
import com.walletmix.paymixbusiness.utils.NetworkUtils
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

    @Provides
    @Singleton
    fun provideUserApiService(apiServiceBuilder: ApiServiceBuilder): UserApiService {
        return apiServiceBuilder.buildService(UserApiService::class.java)
    }


    @Provides
    @Singleton
    fun provideUserAuthApiService(apiServiceBuilder: ApiServiceBuilder): UserAuthApiService {
        return apiServiceBuilder.buildService(UserAuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUtilsApiService(apiServiceBuilder: ApiServiceBuilder): UtilsApiService {
        return apiServiceBuilder.buildService(UtilsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMerchantApiService(apiServiceBuilder: ApiServiceBuilder): MerchantApiService {
        return apiServiceBuilder.buildService(MerchantApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTransecionApiService(apiServiceBuilder: ApiServiceBuilder): TransectionApiService {
        return apiServiceBuilder.buildService(TransectionApiService::class.java)
    }



}