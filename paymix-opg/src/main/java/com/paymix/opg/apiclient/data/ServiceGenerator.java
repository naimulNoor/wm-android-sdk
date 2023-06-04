package com.paymix.opg.apiclient.data;


import java.util.Objects;

class ServiceGenerator {
    static  <T> T createService(Class<T> service) {
        return Objects.requireNonNull(ApiClient.Companion.getRetrofitApiClient()).create(service);
    }
}
