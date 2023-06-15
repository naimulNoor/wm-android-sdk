package com.paymix.opg.apiclient.data;

import android.content.Context;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.paymix.opg.apiclient.data.reponse.BaseResponse;
import com.paymix.opg.apiclient.pref.Keys;
import com.paymix.opg.apiclient.pref.SessionManager;
import com.paymix.opg.apiclient.data.errorhandle.HttpErrorHandler;
import com.paymix.opg.apiclient.data.reponse.CheckServerApiResponse;
import com.paymix.opg.apiclient.data.reponse.InitPaymentApiResponse;
import com.paymix.opg.apiclient.data.reponse.InitTokenResponse;
import com.paymix.opg.apiclient.data.request.CheckPaymentService;
import com.paymix.opg.apiclient.data.request.CheckServerApiService;
import com.paymix.opg.apiclient.data.request.Headers;
import com.paymix.opg.apiclient.data.request.InitPaymentApiService;


import java.util.Arrays;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RetrofitHelperService {

    public void checkServer(final boolean isLive, final CheckServerApiCallListener checkServerApiCallListener) {

        String checkServerUrl = isLive ? APIs.CHECK_SERVER_LIVE : APIs.CHECK_SERVER_SANDBOX;
        checkServerUrl = new String(Base64.decode(checkServerUrl, Base64.DEFAULT));

        CheckServerApiService checkServerApiService = ServiceGenerator.createService(CheckServerApiService.class);
        Call<CheckServerApiResponse> checkServerCall = checkServerApiService.checkServer(checkServerUrl);
        checkServerCall.enqueue(new Callback<CheckServerApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<CheckServerApiResponse> call, @NonNull Response<CheckServerApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CheckServerApiResponse result = response.body();
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
            public void onFailure(@NonNull Call<CheckServerApiResponse> call, @NonNull Throwable t) {
                HttpErrorHandler.handleError(t, new HttpErrorHandler.ErrorListener() {
                    @Override
                    public void onError(String errorMessage) {
                        checkServerApiCallListener.onFailedToSelectServer(errorMessage);
                    }
                });
            }
        });

    }


    public void initPayment(final String initPaymentUrl, final Map<String, String> params, final InitPaymentApiCallListener initPaymentApiCallListener) {
        InitPaymentApiService initPaymentApiService = ServiceGenerator.createService(InitPaymentApiService.class);
        Call<JsonObject> initPaymentApiCall = initPaymentApiService.initPayment(initPaymentUrl, params);
        initPaymentApiCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    InitPaymentApiResponse result  = new Gson().fromJson(response.body(),InitPaymentApiResponse.class);
                    if (result.statusCode.equals("1000")) {
                        initPaymentApiCallListener.onSuccessfullyInitPayment(result.token);
                    } else {
                        initPaymentApiCallListener.onFailedToInitPayment(result.statusMsg);
                    }
                }else{
                    BaseResponse data  = new Gson().fromJson(response.errorBody().charStream(),BaseResponse.class);
                    List<String> errorlistkey = Arrays.asList(data.data.keySet().toString().replace("[","").replace("]","").replace(" ","").split(","));
                    JsonElement errorMessage=data.data.get(errorlistkey.get(0));
                    initPaymentApiCallListener.onFailedToInitPayment(errorMessage.toString().replace("\"",""));

                }
            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                HttpErrorHandler.handleError(t, new HttpErrorHandler.ErrorListener() {
                    @Override
                    public void onError(String errorMessage) {
                        initPaymentApiCallListener.onFailedToInitPayment(errorMessage);
                    }
                });
            }
        });
    }


    public void checkPayment(final boolean isLive, final Map<String, String> params, final CheckPaymentApiCallListener checkPaymentApiCallListener) {

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



    public interface CheckServerApiCallListener {

        void onSuccessfullySelectedServer(String initPaymentUrl, String bankPaymentUrl);

        void onFailedToSelectServer(String failedMessage);
    }

    public interface InitPaymentApiCallListener {

        void onSuccessfullyInitPayment(String token);

        void onFailedToInitPayment(String failedMessage);
    }

    interface InitTokenCallListener {

        void onSuccessfullyInitToken();

        void onFailedToInitToken(String failedMessage);
    }

    public interface CheckPaymentApiCallListener {

        void onSuccessfullyCheckedPayment(String checkPaymentResponse);

        void onFailedToCheckedPayment(String failedMessage);
    }

}
