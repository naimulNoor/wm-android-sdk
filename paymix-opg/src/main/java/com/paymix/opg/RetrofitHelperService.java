package com.paymix.opg;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;


import java.util.Map;

import androidx.annotation.NonNull;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class RetrofitHelperService {

    void checkServer(final boolean isLive, final CheckServerApiCallListener checkServerApiCallListener) {

        String checkServerUrl = isLive ? APIs.CHECK_SERVER_LIVE : APIs.CHECK_SERVER_SANDBOX;
        checkServerUrl = new String(Base64.decode(checkServerUrl, Base64.DEFAULT));

        CheckServerApiService checkServerApiService = ServiceGenerator.createService(CheckServerApiService.class);
        Call<CheckServerApiResponse> checkServerCall = checkServerApiService.checkServer(checkServerUrl);
        checkServerCall.enqueue(new Callback<CheckServerApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<CheckServerApiResponse> call, @NonNull Response<CheckServerApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CheckServerApiResponse result = response.body();
                    assert result != null;
                    if (result.isServerSelected) {
                        String initPaymentUrl = result.initPaymentUrl;
                        String bank_payment_url = result.bankPaymentUrl;
                        checkServerApiCallListener.onSuccessfullySelectedServer(initPaymentUrl, bank_payment_url);
                    } else {
                        checkServerApiCallListener.onFailedToSelectServer(result.statusMsg);
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckServerApiResponse> call, Throwable t) {
                HttpErrorHandler.handleError(t, new HttpErrorHandler.ErrorListener() {
                    @Override
                    public void onError(String errorMessage) {
                        checkServerApiCallListener.onFailedToSelectServer(errorMessage);
                    }
                });
            }
        });

    }


    void initPayment(final String initPaymentUrl, final Map<String, String> params, final InitPaymentApiCallListener initPaymentApiCallListener) {
        InitPaymentApiService initPaymentApiService = ServiceGenerator.createService(InitPaymentApiService.class);
        Call<InitPaymentApiResponse> initPaymentApiCall = initPaymentApiService.initPayment(initPaymentUrl, params);
        initPaymentApiCall.enqueue(new Callback<InitPaymentApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<InitPaymentApiResponse> call, Response<InitPaymentApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    InitPaymentApiResponse result = response.body();
                    assert result != null;
                    if (result.statusCode.equals("1000")) {
                        initPaymentApiCallListener.onSuccessfullyInitPayment(result.token);
                    } else {
                        initPaymentApiCallListener.onFailedToInitPayment(result.statusMsg);
                    }
                }
            }

            @Override
            public void onFailure(Call<InitPaymentApiResponse> call, Throwable t) {
                HttpErrorHandler.handleError(t, new HttpErrorHandler.ErrorListener() {
                    @Override
                    public void onError(String errorMessage) {
                        initPaymentApiCallListener.onFailedToInitPayment(errorMessage);
                    }
                });
            }
        });
    }


    void checkPayment(final boolean isLive, final Map<String, String> params, final CheckPaymentApiCallListener checkPaymentApiCallListener) {

        String checkPaymentUrl = isLive ? APIs.CHECK_PAYMENT_LIVE : APIs.CHECK_PAYMENT_SANDBOX;
        checkPaymentUrl = new String(Base64.decode(checkPaymentUrl, Base64.DEFAULT));

        CheckPaymentService checkPaymentService = ServiceGenerator.createService(CheckPaymentService.class);
        Call<JsonElement> checkPaymentApiCall = checkPaymentService.checkPayment(checkPaymentUrl, params);
        checkPaymentApiCall.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String checkPaymentResponse = new Gson().toJson(response.body());
                    checkPaymentApiCallListener.onSuccessfullyCheckedPayment(checkPaymentResponse);
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                HttpErrorHandler.handleError(t, new HttpErrorHandler.ErrorListener() {
                    @Override
                    public void onError(String errorMessage) {
                        checkPaymentApiCallListener.onFailedToCheckedPayment(errorMessage);
                    }
                });
            }
        });
    }

    void initToken(Context context, String refId, String paymentToken, final InitTokenCallListener initTokenCallListener) {



        //String url = All_APIs.BASE_URL + All_APIs.SUB_DOMAIN + "/api" + All_APIs.API_VERSION + "/token-initialization";



        SessionManager sessionManager = new SessionManager(context);
        String authorization = Headers.BEARER + sessionManager.getString(Key.access_token.name());
        //String authorization =sessionManager.getString(Key.access_token.name());

        //add bank process url
        String url ="https://sandbox.walletmix.com/bank-payment-process/"+paymentToken;
        InitPaymentApiService initPaymentApiService = ServiceGenerator.createService(InitPaymentApiService.class);
        Log.d("authtoken",authorization);
        Log.d("authtoken",paymentToken);

        //oke
        Call<InitTokenResponse> initPaymentApiCall = initPaymentApiService.bankPaymentProcess(url);
        initPaymentApiCall.enqueue(new Callback<InitTokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<InitTokenResponse> call, Response<InitTokenResponse> response) {
                Log.d("","");
                if (response.isSuccessful() && response.body() != null) {
                    InitTokenResponse result = response.body();
                    if (result.statusCode == 9090) {
                        for (Map.Entry entry : result.invalidMap.entrySet()) {
                            if (entry.getKey().equals("reference_id"))
                                initTokenCallListener.onFailedToInitToken(entry.getValue().toString());
                            else if (entry.getKey().equals("payment_token")) {
                                initTokenCallListener.onFailedToInitToken(entry.getValue().toString());
                            }
                        }
                    } else if (result.statusCode == 200) {
                        initTokenCallListener.onSuccessfullyInitToken();
                    }
                }
            }

            @Override
            public void onFailure(Call<InitTokenResponse> call, Throwable t) {
                HttpErrorHandler.handleError(t, new HttpErrorHandler.ErrorListener() {
                    @Override
                    public void onError(String errorMessage) {
                        initTokenCallListener.onFailedToInitToken(errorMessage);
                    }
                });
            }
        });
    }


    interface CheckServerApiCallListener {

        void onSuccessfullySelectedServer(String initPaymentUrl, String bankPaymentUrl);

        void onFailedToSelectServer(String failedMessage);
    }

    interface InitPaymentApiCallListener {

        void onSuccessfullyInitPayment(String token);

        void onFailedToInitPayment(String failedMessage);
    }

    interface InitTokenCallListener {

        void onSuccessfullyInitToken();

        void onFailedToInitToken(String failedMessage);
    }

    interface CheckPaymentApiCallListener {

        void onSuccessfullyCheckedPayment(String checkPaymentResponse);

        void onFailedToCheckedPayment(String failedMessage);
    }

}
