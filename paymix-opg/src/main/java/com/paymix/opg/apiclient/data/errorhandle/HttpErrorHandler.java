package com.paymix.opg.apiclient.data.errorhandle;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.HttpException;

public class HttpErrorHandler {

    private static final String SERVER_ERROR_MESSAGE = "We are having some trouble completing your request at this moment. Please try again shortly.";
    private static final String TIME_OUT_MESSAGE = "Could not connect with the server. Please try again later.";

    public static void handleError(Throwable throwable, final ErrorListener errorListener){
        if(throwable instanceof HttpException){
            int errorCode = ((HttpException) throwable).code();
            if(errorCode == 500)
                errorListener.onError(SERVER_ERROR_MESSAGE);
        } else if ((throwable instanceof SocketTimeoutException) || (throwable instanceof SSLHandshakeException)) {
            errorListener.onError(TIME_OUT_MESSAGE);
        }else if (throwable instanceof ConnectException){
            errorListener.onError(SERVER_ERROR_MESSAGE);
        }
    }

    public interface ErrorListener{

        void onError(String errorMessage);
    }
}
