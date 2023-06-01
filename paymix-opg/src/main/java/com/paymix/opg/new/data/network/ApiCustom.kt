package com.walletmix.paymixbusiness.data.network

import android.util.Log
import okhttp3.Dispatcher
import okhttp3.Interceptor

class ApiCustom {

      fun  dispatchApi(code: Int, dispatcher: Dispatcher, chain: Interceptor.Chain){
          Log.e("function","Dispatch Condition")
        if(code==423){
                Log.e("To-Many-Request-{423}",chain.request().toString())
                dispatcher.maxRequestsPerHost.toString()
                dispatcher.cancelAll()
                dispatcher.executorService.shutdown()
        }else{
                dispatcher.executorService.shutdown()
            }
    }
}