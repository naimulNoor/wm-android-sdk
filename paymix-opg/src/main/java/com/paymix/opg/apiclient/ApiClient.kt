package com.paymix.opg.apiclient

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    private var retrofit: Retrofit? = null

    fun getRetrofitApiClient(): Retrofit? {
        if (retrofit == null) {
            //HttpClient Builder....
            val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            okHttpClientBuilder.connectTimeout(20, TimeUnit.SECONDS)
            okHttpClientBuilder.readTimeout(20, TimeUnit.SECONDS)
            okHttpClientBuilder.writeTimeout(20, TimeUnit.SECONDS)

            // Adding Logging Interceptor in debug mode to see details of request....
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                okHttpClientBuilder.addInterceptor(logging)


            // Building Retrofit...
            retrofit = Retrofit.Builder()
                .baseUrl("https://walletmix.info") // Dummy Base url..because we are using dynamic url...
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClientBuilder.build())
                .build()
        }
        return retrofit
    }
}