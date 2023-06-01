package com.paymix.opg.base

import android.content.Context

interface BaseContract {

    interface View {

        fun onNetworkCallStarted(loadingMessage: String)

        fun onNetworkCallEnded()

        fun onServerError()

        fun onNetworkError()

        fun onTestError(error: String)

        fun onTimeOutError()

        fun onNetworkUnavailable()

        fun onUserUnauthorized(errorMessage: String)

        fun onUserDisabled(errorMsg: String)

        fun applyForcePinReset()

        fun onSystemUpgrading()

        fun onUserDidTooManyAttempts(errorMsg: String)

        fun onValidationFailed(messages: LinkedHashMap<String, String>)

        fun onExpectationFailed(message: String)


        fun getContext(): Context

    }

    interface Presenter {

        fun detachView()

        fun clearDisposable()

    }
}