package com.paymix.opg;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.google.gson.Gson;
import com.paymix.opg.apiclient.data.APIs;
import com.paymix.opg.apiclient.data.NetworkUtils;
import com.paymix.opg.apiclient.data.RetrofitHelperService;
import com.paymix.opg.apiclient.pref.Keys;
import com.paymix.opg.apiclient.pref.SessionManager;
import com.paymix.opg.appInterface.OPGResponseListener;
import com.paymix.opg.utils.AlertServices;
import com.wallemix.paymix.opg.R;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class WalletmixOnlinePaymentGateway  {

    private Context context;
    private ProgressDialog PD;
    private NetworkUtils networkUtils;
    private RetrofitHelperService retrofitHelperService;

    private String init_Payment_Url;
    private String bank_payment_url;
    private boolean isLive = false;
    private String callBackActivityClassName = "";
    private ProgressDialog progressDialog;

    //Merchant Information
    private String access_username;
    private String access_password;
    private String wmx_id;
    private String access_app_key;
    private String merchantRefId;
    private String merchant_order_id;

    //Customer Information
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String customerAddress;
    private String customerCity;
    private String customerCountry;
    private String customerPostcode;

    //Product Information
    private String productDesc;
    private String amount;
    private String currency;

    // Shipping Information
    private String shippingName;
    private String shippingAddress;
    private String shippingCity;
    private String shippingCountry;
    private String shippingPostCode;

    // Others
    private String web_address;
    private String extra_json;

    private String cart_info;
    private String options;
    private String authorization;
    OPGResponseListener listener;

    public WalletmixOnlinePaymentGateway(Context context) {
        this.context = context;
        networkUtils = new NetworkUtils(context);
        retrofitHelperService = new RetrofitHelperService();
    }

    @SuppressWarnings("unused")
    public void setTransactionInformation(String wmx_id,
                                          String access_username,
                                          String access_password,
                                          String access_app_key,
                                          String merchant_order_id,
                                          String merchantRefId,
                                          String customerName,
                                          String customerPhone,
                                          String customerEmail,
                                          String customerAddress,
                                          String customerCity,
                                          String customerCountry,
                                          String customerPostcode,
                                          String productDesc,
                                          String amount,
                                          String currency,
                                          String shippingName,
                                          String shippingAddress,
                                          String shippingCity,
                                          String shippingCountry,
                                          String shippingPostCode,
                                          String web_address,
                                          String extra_json) {
        //Merchant
        this.wmx_id = wmx_id;
        this.access_username = access_username;
        this.access_password = access_password;
        this.access_app_key = access_app_key;
        this.merchant_order_id = merchant_order_id;
        this.merchantRefId = merchantRefId;
        //Customer
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.customerAddress = customerAddress;
        this.customerCity = customerCity;
        this.customerCountry = customerCountry;
        this.customerPostcode = customerPostcode;
        this.productDesc = productDesc;
        this.amount = amount;
        this.currency = currency;
        //Shipping
        this.shippingName = shippingName;
        this.shippingAddress = shippingAddress;
        this.shippingCity = shippingCity;
        this.shippingCountry = shippingCountry;
        this.shippingPostCode = shippingPostCode;
        this.web_address = web_address;
        this.extra_json = extra_json;
    }


    public <T> void startTransactions(boolean isLive, @NotNull Class<T> callBackActivityClass, final OPGResponseListener oPGResponseListener) {



        this.isLive = isLive;
        this.callBackActivityClassName = callBackActivityClass.getCanonicalName();

        if (networkUtils.isNetworkAvailable()) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            retrofitHelperService.checkServer(isLive, new RetrofitHelperService.CheckServerApiCallListener() {
                @Override
                public void onSuccessfullySelectedServer(String initPaymentUrl, String bankPaymentUrl) {
                    init_Payment_Url = initPaymentUrl;
                    bank_payment_url = bankPaymentUrl;
                    oPGResponseListener.intRequest(isLive,initPaymentUrl);
                    initPayment(init_Payment_Url,oPGResponseListener);
                }

                @Override
                public void onFailedToSelectServer(String failedMessage) {
                    oPGResponseListener.onFailed(failedMessage);
                    if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
                    AlertServices.showAlertDialog(context, null, failedMessage, "Okay", null, null);
                }
            });

        } else {
            AlertServices.showAlertDialog(context, context.getString(R.string.connection_error_title), context.getString(R.string.connection_error_message), "Okay", null, null);
            oPGResponseListener.onFailed(context.getString(R.string.connection_error_title));
        }
    }


    private void initPayment(String initPaymentUrl, OPGResponseListener oPGResponseListener) {
        Map<String, String> initPaymentParams = getParamsMap();
        retrofitHelperService.initPayment(initPaymentUrl, initPaymentParams, new RetrofitHelperService.InitPaymentApiCallListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onSuccessfullyInitPayment(String token) {

                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                        oPGResponseListener.onProcessPaymentRequest(initPaymentUrl,initPaymentParams);
                        String cardSelectionPageUrl = bank_payment_url + "/" + token;
                        CardSelectionActivity activity=new CardSelectionActivity();
                        activity.callback=oPGResponseListener;
                        Intent cardSelectionIntent = new Intent(context,CardSelectionActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Bundle dataBundle = new Bundle();
                        dataBundle.putBoolean(Keys.is_live.name(), isLive);
                        dataBundle.putString(Keys.token.name(), token);
                        dataBundle.putString(Keys.card_selection_url.name(), cardSelectionPageUrl);
                        dataBundle.putString(Keys.call_back_url.name(), APIs.CALL_BACK_URL);
                        dataBundle.putString(Keys.wmx_id.name(), wmx_id);
                        dataBundle.putString(Keys.access_api_key.name(), access_app_key);
                        dataBundle.putString(Keys.authorization.name(), authorization);
                        dataBundle.putString(Keys.call_back_activity_class_name.name(), callBackActivityClassName);
                        //dataBundle.putSerializable("OPG-LISTENER", oPGResponseListener);
                        cardSelectionIntent.putExtra(Keys.data_bundle.name(), dataBundle);
                        //cardSelectionIntent.putExtra("OPG-LISTENER",oPGResponseListener);
                        CardSelectionActivity.setUpListener(oPGResponseListener);
                        context.startActivity(cardSelectionIntent);
//                        try{
//
//                        }catch (Exception exception){
//                            Log.d("init-payment-token",token);
//                        oPGResponseListener.onFailed(exception.getMessage());
//                        }




//
//                retrofitHelperService.initToken(context, merchant_order_id, token, new RetrofitHelperService.InitTokenCallListener() {
//                    @Override
//                    public void onSuccessfullyInitToken() {
//                        if (progressDialog != null && progressDialog.isShowing())
//                            progressDialog.dismiss();
//                        String cardSelectionPageUrl = bank_payment_url + "/" + token;
//                        Intent cardSelectionIntent = new Intent(context, CardSelectionActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        Bundle dataBundle = new Bundle();
//                        dataBundle.putBoolean(Keys.is_live.name(), isLive);
//                        dataBundle.putString(Keys.token.name(), token);
//                        dataBundle.putString(Keys.card_selection_url.name(), cardSelectionPageUrl);
//                        dataBundle.putString(Keys.call_back_url.name(), APIs.CALL_BACK_URL);
//                        dataBundle.putString(Keys.wmx_id.name(), wmx_id);
//                        dataBundle.putString(Keys.access_api_key.name(), access_app_key);
//                        dataBundle.putString(Keys.authorization.name(), authorization);
//                        dataBundle.putString(Keys.call_back_activity_class_name.name(), callBackActivityClassName);
//                        cardSelectionIntent.putExtra(Keys.data_bundle.name(), dataBundle);
//                        context.startActivity(cardSelectionIntent);
//                    }
//
//                    @Override
//                    public void onFailedToInitToken(String failedMessage) {
//                        if (progressDialog != null && progressDialog.isShowing())
//                            progressDialog.dismiss();
//                        AlertServices.showAlertDialog(context, null, failedMessage, "Okay", null, null);
//                    }
//                });
            }

            @Override
            public void onFailedToInitPayment(String failedMessage) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                oPGResponseListener.onFailed(failedMessage);
                AlertServices.showAlertDialog(context, null, failedMessage, "Okay", null, null);
            }
        });
    }


    private Map<String, String> getParamsMap() {

        Map<String, String> initPaymentParams = new HashMap<String, String>();

        String myApp_Name = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        cart_info = wmx_id + "," + web_address + "," + myApp_Name;

        String ipAddress = networkUtils.getLocalIpAddress();
        String credential = "s=" + web_address + "," + "i=" + ipAddress;
        options = Base64.encodeToString(credential.getBytes(), Base64.DEFAULT).replace("\n", "");

        String credentials = access_username + ":" + access_password;
        authorization = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT).replace("\n", "");

        initPaymentParams.put("wmx_id", wmx_id != null ? wmx_id : "");
        initPaymentParams.put("access_app_key", access_app_key != null ? access_app_key : "");
        initPaymentParams.put("merchant_order_id", merchant_order_id != null ? merchant_order_id : "");
        initPaymentParams.put("merchant_ref_id", merchantRefId != null ? merchantRefId : "");
        initPaymentParams.put("customer_name", customerName != null ? customerName : "");
        initPaymentParams.put("customer_phone", customerPhone != null ? customerPhone : "");
        initPaymentParams.put("customer_email", customerEmail != null ? customerEmail : "");
        initPaymentParams.put("customer_add", customerAddress != null ? customerAddress : "");
        initPaymentParams.put("customer_city", customerCity != null ? customerCity : "");
        initPaymentParams.put("customer_country", customerCountry != null ? customerCountry : "");
        initPaymentParams.put("customer_postcode", customerPostcode != null ? customerPostcode : "");
        initPaymentParams.put("product_desc", productDesc != null ? productDesc : "");
        initPaymentParams.put("amount", amount != null ? amount : "");
        initPaymentParams.put("currency", currency != null ? currency : "");
        initPaymentParams.put("shipping_name", shippingName != null ? shippingName : "");
        initPaymentParams.put("shipping_add", shippingAddress != null ? shippingAddress : "");
        initPaymentParams.put("shipping_city", shippingCity != null ? shippingCity : "");
        initPaymentParams.put("shipping_country", shippingCountry != null ? shippingCountry : "");
        initPaymentParams.put("shipping_postCode", shippingPostCode != null ? shippingPostCode : "");
        initPaymentParams.put("app_name", web_address != null ? web_address : "");
        initPaymentParams.put("cart_info", cart_info != null ? cart_info : "");
        initPaymentParams.put("options", options != null ? options : "");
        initPaymentParams.put("callback_url", APIs.CALL_BACK_URL);
        initPaymentParams.put("authorization", authorization != null ? authorization : "");
        initPaymentParams.put("extra_json", extra_json != null ? extra_json : "");

        return initPaymentParams;
    }





}
