package com.paymix.opg;

class ServiceGenerator {
    static  <T> T createService(Class<T> service) {
        return ApiClient.getRetrofitApiClient().create(service);
    }
}
