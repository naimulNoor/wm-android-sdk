package com.paymix.opg.apiclient


internal interface APIs {
    companion object {
        // Check Server
        const val CHECK_SERVER_SANDBOX = "aHR0cHM6Ly9zYW5kYm94LndhbGxldG1peC5jb20vY2hlY2stc2VydmVy"
        const val CHECK_SERVER_LIVE = "aHR0cHM6Ly9lcGF5LndhbGxldG1peC5jb20vY2hlY2stc2VydmVy"

        // Check Payment
        const val CHECK_PAYMENT_SANDBOX =
            "aHR0cHM6Ly9zYW5kYm94LndhbGxldG1peC5jb20vY2hlY2stcGF5bWVudA=="
        const val CHECK_PAYMENT_LIVE = "aHR0cHM6Ly9lcGF5LndhbGxldG1peC5jb20vY2hlY2stcGF5bWVudA=="

        // Abort Url
        const val ABORT_SANDBOX = "aHR0cHM6Ly9zYW5kYm94LndhbGxldG1peC5jb20vYWJvcnQ="
        const val ABORT_LIVE = "aHR0cHM6Ly9lcGF5LndhbGxldG1peC5jb20vYWJvcnQ="

        //Call_back_url
        const val CALL_BACK_URL =
            "https://epay.walletmix.com/check-payment" // Any Url can be used as call_back_url
    }
}