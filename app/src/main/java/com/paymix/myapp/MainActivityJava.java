package com.paymix.myapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.paymix.opg.WalletmixOnlinePaymentGateway;
import com.paymix.opg.apiclient.data.reponse.PaymentResponse;
import com.paymix.opg.appInterface.OPGResponseListener;
import com.walletmix.myapp.R;

import java.util.Map;

public class MainActivityJava extends AppCompatActivity {
    private WalletmixOnlinePaymentGateway walletmixOnlinePaymentGateway;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        walletmixOnlinePaymentGateway = new WalletmixOnlinePaymentGateway(this);
        Button button=findViewById(R.id.init_payment);

        String wmx_id = "WMX60ac7f66a4f2c";
        String access_app_key = "f2af089d817955e2f02c277bb502e5a4b522df31";
        String merchant_user_name = "bogubd_568249845";
        String merchant_pass = "bogubd_1528854484";



        button.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {
               walletmixOnlinePaymentGateway.setTransactionInformation(
                       wmx_id,
                       merchant_user_name,
                       merchant_pass,
                       access_app_key,
                       "wl45d6",
                       "wl456d",
                       "Naimul Hassan Noor",
                       "01733433672",
                       "a@b.com",
                       "dhaka",
                       "dhaka",
                       "BD",
                       "1236",
                       "test",
                       "10",
                       "BDT",
                       "Naimul Hasssan Noor",
                       "dhaka",
                       "dhaka",
                       "BD",
                       "1236",
                       "www.naimulnoor.com",
                       ""
               );
               walletmixOnlinePaymentGateway.startTransactions(false, MainActivityJava.class, new OPGResponseListener() {
                   @Override
                   public void intRequest(boolean sandBox, @Nullable String initPaymentUrl) {
                       Toast.makeText(getApplicationContext(), "intRequest", Toast.LENGTH_SHORT).show();
                   }

                   @Override
                   public void onProcessPaymentRequest(@Nullable String initPaymentUrl, @NonNull Map<String, String> parameter) {
                       Toast.makeText(getApplicationContext(), "onProcessPaymentRequest", Toast.LENGTH_SHORT).show();
                   }

                   @Override
                   public void onSuccessPaymentRequest(int statusCode, @Nullable PaymentResponse response) {
                       Toast.makeText(getApplicationContext(), "onSuccessPaymentRequest", Toast.LENGTH_SHORT).show();
                   }

                   @Override
                   public void onFailedPaymentRequest(int statusCode, @Nullable PaymentResponse response) {
                       Toast.makeText(getApplicationContext(), "onFailedPaymentRequest", Toast.LENGTH_SHORT).show();
                   }

                   @Override
                   public void onDeclinedPaymentRequest(int statusCode, @Nullable PaymentResponse response) {

                   }

                   @Override
                   public void onFailed(@Nullable String message) {

                   }
               });
           }
       });


    }
}
