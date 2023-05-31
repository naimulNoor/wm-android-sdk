package com.paymix.opg;



import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class ApiClient {

    private static Retrofit retrofit = null;

    static Retrofit getRetrofitApiClient() {

        if (retrofit == null) {
            //HttpClient Builder....
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            okHttpClientBuilder.connectTimeout(20, TimeUnit.SECONDS);
            okHttpClientBuilder.readTimeout(20, TimeUnit.SECONDS);
            okHttpClientBuilder.writeTimeout(20, TimeUnit.SECONDS);

            // Adding Logging Interceptor in debug mode to see details of request....

                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                okHttpClientBuilder.addInterceptor(logging);


            // Building Retrofit...
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://walletmix.info") // Dummy Base url..because we are using dynamic url...
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClientBuilder.build())
                    .build();
        }
        return retrofit;
    }
}
