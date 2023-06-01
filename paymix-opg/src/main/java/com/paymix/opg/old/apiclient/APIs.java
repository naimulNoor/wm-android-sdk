package com.paymix.opg.old.apiclient;
//package walletmix.com.walletmixopglibrary;

interface APIs {

    // Check Server
    String CHECK_SERVER_SANDBOX = "aHR0cHM6Ly9zYW5kYm94LndhbGxldG1peC5jb20vY2hlY2stc2VydmVy";
    String CHECK_SERVER_LIVE = "aHR0cHM6Ly9lcGF5LndhbGxldG1peC5jb20vY2hlY2stc2VydmVy";

    // Check Payment
    String CHECK_PAYMENT_SANDBOX = "aHR0cHM6Ly9zYW5kYm94LndhbGxldG1peC5jb20vY2hlY2stcGF5bWVudA==";
    String CHECK_PAYMENT_LIVE = "aHR0cHM6Ly9lcGF5LndhbGxldG1peC5jb20vY2hlY2stcGF5bWVudA==";

    // Abort Url
    String ABORT_SANDBOX = "aHR0cHM6Ly9zYW5kYm94LndhbGxldG1peC5jb20vYWJvcnQ=";
    String ABORT_LIVE = "aHR0cHM6Ly9lcGF5LndhbGxldG1peC5jb20vYWJvcnQ=";

    //Call_back_url
    String CALL_BACK_URL = "https://epay.walletmix.com/check-payment"; // Any Url can be used as call_back_url

}
