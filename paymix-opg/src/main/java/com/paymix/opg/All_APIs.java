package com.paymix.opg;


public interface All_APIs {

    String SUB_DOMAIN = "/apps";
    String API_VERSION = "/v4";
    String BASE_URL = "https://cholbedigital.com";

//     String SUB_DOMAIN = "projects/clone-walletmix-recharge-web/public";
//     String API_VERSION = "/v4";
//     String BASE_URL = "http://192.168.0.105";

    String API_REGISTRATION = SUB_DOMAIN + "/api" + API_VERSION + "/register-user";

    // Token
    String ACCESS_TOKEN = SUB_DOMAIN + "/api" + API_VERSION + "/access-token";

    // Profile
    String USER_PROFILE = SUB_DOMAIN + "/api" + API_VERSION + "/profile";
    String UPDATE_PROFILE = SUB_DOMAIN + "/api" + API_VERSION + "/user-profile-update";

    // Menu
    String ABOUT_US = SUB_DOMAIN + "/api" + API_VERSION + "/about-us";

    // Password and Pin
    String CHANGE_PASSWORD = SUB_DOMAIN + "/api" + API_VERSION + "/change-password";
    String CHANGE_PIN = SUB_DOMAIN + "/api" + API_VERSION + "/change-pin";
    String FORGOT_PASSWORD = SUB_DOMAIN + "/api" + API_VERSION + "/password-reset";
    String FORGOT_PIN = SUB_DOMAIN + "/api" + API_VERSION + "/forgot-pin";

    //Support
    String ADD_SUPPORT = SUB_DOMAIN + "/api" + API_VERSION + "/add-support";
    String SUPPORT_LIST = SUB_DOMAIN + "/api" + API_VERSION + "/support-list";
    String INDIVIDUAL_SUPPORT_LIST = SUB_DOMAIN + "/api" + API_VERSION + "/individual-support-list";
    String SEND_SUPPORT_REPLY = SUB_DOMAIN + "/api" + API_VERSION + "/support-reply";

    //Banner
    String BANNER = SUB_DOMAIN + "/api" + API_VERSION + "/banner-list";
    String PROMOTIONAL_BANNER = SUB_DOMAIN + "/api" + API_VERSION + "/get-promotional-banner";

    // Topup
    String TOPUP = SUB_DOMAIN + "/api" + API_VERSION + "/topup";
    String TOPUP_HISTORY = SUB_DOMAIN + "/api" + API_VERSION + "/topup-history";
    String QUICK_AMOUNT = SUB_DOMAIN + "/api" + API_VERSION + "/quick-amount";

    // Balance
    String USER_BALANCE = SUB_DOMAIN + "/api" + API_VERSION + "/user-balance";

    // Push Notification
    String STORE_DEVICE_TOKEN = SUB_DOMAIN + "/api" + API_VERSION + "/store-device-token";

    // Requisition
    String SERVICE_CHECK = SUB_DOMAIN + "/api" + API_VERSION + "/version-check";

    // History
    String COMMISSION_HISTORY = SUB_DOMAIN + "/api" + API_VERSION + "/commission-history";
    String DISTRIBUTO_DASHBOARD_SUMMERY = SUB_DOMAIN + "/api" + API_VERSION + "/dashboard-summary";
    String FUND_HISTORY = SUB_DOMAIN + "/api" + API_VERSION + "/transaction-list";

    // List...
    String OFFER_LIST = SUB_DOMAIN + "/api" + API_VERSION + "/offer-list";

    // Fund...
    String INIT_FUND = SUB_DOMAIN + "/api" + API_VERSION + "/credit-initialization";
    String CONFIRMED_FUND = SUB_DOMAIN + "/api" + API_VERSION + "/payment-confirmed";

    // Email Verification...
    String VERIFY_EMAIL = SUB_DOMAIN + "/api" + API_VERSION + "/verify-email";
    String REQUEST_VERIFY_EMAIL = SUB_DOMAIN + "/api" + API_VERSION + "/request-verify-email";

    // Notification
    String NOTIFICATION_LIST = SUB_DOMAIN + "/api" + API_VERSION + "/get-notification-list";

    String USER_CHECK = SUB_DOMAIN + "/api" + API_VERSION + "/user-check";

    String PROFESSION = SUB_DOMAIN + "/api" + API_VERSION + "/get-profession";
}
