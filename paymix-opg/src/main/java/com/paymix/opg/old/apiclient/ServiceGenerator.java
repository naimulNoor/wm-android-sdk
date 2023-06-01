package com.paymix.opg.old.apiclient;

class ServiceGenerator {
    static  <T> T createService(Class<T> service) {
        return ApiClient.getRetrofitApiClient().create(service);
    }
}
