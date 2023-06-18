package com.paymix.opg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import com.google.gson.Gson;
import com.paymix.opg.apiclient.data.APIs;
import com.paymix.opg.apiclient.data.RetrofitHelperService;
import com.paymix.opg.apiclient.data.reponse.PaymentResponse;
import com.paymix.opg.apiclient.pref.Keys;
import com.paymix.opg.appInterface.OPGResponseListener;
import com.paymix.opg.utils.AlertServices;
import com.wallemix.paymix.opg.R;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

public class CardSelectionActivity extends AppCompatActivity {

    String cardSelectionPageUrl, callBackUrl, wmxId, accessApiKey, authorization, token, callBackActivityClassName;
    boolean isLive;
    String abort_sandbox, abort_live;
    final int WEB_VIEW_ID = 123;
    RetrofitHelperService retrofitHelperService;

    static OPGResponseListener callback;


    public static void setUpListener(OPGResponseListener Listener) {
        callback = Listener;
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            Bundle dataBundle = getIntent().getBundleExtra(Keys.data_bundle.name());
            token = dataBundle.getString(Keys.token.name());
            cardSelectionPageUrl = dataBundle.getString(Keys.card_selection_url.name());
            callBackUrl = dataBundle.getString(Keys.call_back_url.name());
            wmxId = dataBundle.getString(Keys.wmx_id.name());
            accessApiKey = dataBundle.getString(Keys.access_api_key.name());
            authorization = dataBundle.getString(Keys.authorization.name());
            callBackActivityClassName = dataBundle.getString(Keys.call_back_activity_class_name.name());
            isLive = dataBundle.getBoolean(Keys.is_live.name());

            //callback= (OPGResponseListener) dataBundle.getParcelable("OPG-LISTENER");

        }

        // Making View Programmatically
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        WebView cardSelectionWebView = new WebView(this);
        cardSelectionWebView.setWebViewClient(new MyCardSelectionWebViewClient());
        cardSelectionWebView.setLayoutParams(layoutParams);
        cardSelectionWebView.setId(WEB_VIEW_ID);
        cardSelectionWebView.getSettings().setLoadsImagesAutomatically(true);
        cardSelectionWebView.getSettings().setJavaScriptEnabled(true);
        cardSelectionWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        cardSelectionWebView.loadUrl(cardSelectionPageUrl);
        cardSelectionWebView.setVerticalScrollBarEnabled(false);
        cardSelectionWebView.setHorizontalScrollBarEnabled(false);
        cardSelectionWebView.clearHistory();

        rootLayout.addView(cardSelectionWebView);
        setContentView(rootLayout, layoutParams);

        //init
        retrofitHelperService = new RetrofitHelperService();
        abort_sandbox = new String(Base64.decode(APIs.ABORT_SANDBOX, Base64.DEFAULT));
        abort_live = new String(Base64.decode(APIs.ABORT_LIVE, Base64.DEFAULT));

    }



    private class MyCardSelectionWebViewClient extends WebViewClient  {
        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {

            AlertServices.showAlertDialog(CardSelectionActivity.this, null, getString(R.string.ssl_error_message),getString(R.string.button_continue), getString(android.R.string.cancel), new AlertServices.AlertDialogListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            handler.proceed();
                        }

                        @Override
                        public void onNegativeButtonClicked() {
                            handler.cancel();
                        }
                    });
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (url.equals(callBackUrl)) {
                view.loadUrl("about:blank");
                Map<String, String> params = new HashMap<>();
                params.put("wmx_id", wmxId);
                params.put("access_app_key", accessApiKey);
                params.put("authorization", authorization);
                params.put("token", token);
                retrofitHelperService.checkPayment(isLive, params, new RetrofitHelperService.CheckPaymentApiCallListener() {
                    @Override
                    public void onSuccessfullyCheckedPayment(String checkPaymentResponse) {
                        try {

                            JSONObject jsonObject = new JSONObject(checkPaymentResponse);
                            String status = jsonObject.getString("txn_status");
                            PaymentResponse paymentResponse=new Gson().fromJson(checkPaymentResponse, PaymentResponse.class);
                            switch (Integer.parseInt(status)){
                                case 1000:callback.onSuccessPaymentRequest(Integer.parseInt(status),paymentResponse);break;
                                case 1001:callback.onDeclinedPaymentRequest(Integer.parseInt(status),paymentResponse);break;
                                case 1009:callback.onFailedPaymentRequest(Integer.parseInt(status),paymentResponse);break;
                                default: callback.onFailed("Payment Request Failed");
                            }

                            finish();
                        }  catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailedToCheckedPayment(String failedMessage) {
                        AlertServices.showAlertDialog(CardSelectionActivity.this, null, failedMessage, "Okay", null, null);
                    }
                });
            } else if (url.equals(abort_sandbox) || url.equals(abort_live)) {
                view.loadUrl("about:blank");
                callback.onFailed("Payment Request Failed");
                finish();

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        callback.onFailed("Payment Request Abort");
    }
}
