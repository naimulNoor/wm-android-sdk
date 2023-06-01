package com.paymix.myapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.paymix.opg.old.apiclient.WalletmixOnlinePaymentGateway
import com.walletmix.myapp.R
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    private var walletmixOnlinePGateway: WalletmixOnlinePaymentGateway? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        walletmixOnlinePGateway = WalletmixOnlinePaymentGateway(this)

        var button:Button=findViewById(R.id.init_payment)

        val wmx_id = "WMX60ac7f66a4f2c"
        val access_app_key = "f2af089d817955e2f02c277bb502e5a4b522df31"
        val merchant_user_name = "bogubd_568249845"
        val merchant_pass = "bogubd_1528854484"

        //live for cholbe robi
//        val wmx_id = "WMX5a2d2d56aab99"
//        val access_app_key = "507c619c7ccd9c96b4765d9a3ff7739494424348"
//        val merchant_user_name = "robi_cholbe_966010813"
//        val merchant_pass = "robi_cholbe_128505461"


        button.setOnClickListener{
            walletmixOnlinePGateway!!.setTransactionInformation(
                wmx_id,
                merchant_user_name,
                merchant_pass,
                access_app_key,
                "wl"+(0..10000).random(),
                "wl"+(0..10000).random(),
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
                "",
            )
            walletmixOnlinePGateway!!.startTransactions(true,MainActivity::class.java)
        }


        try {
            val response = intent.getStringExtra("response")
            val jsonObject: JSONObject
            val responseTxnStatus: String
            if (response != null) {
                if (response == "false") {
                    Log.d("payment-response", "Transaction was incomplete. Please try again to complete your transaction.")

                } else {
                    try {
                        jsonObject = JSONObject(response)
                        responseTxnStatus = jsonObject.getString("txn_status")
                        when (responseTxnStatus) {
                            "1000" -> Log.d("payment-response", "Your transaction was Success")

                            "1001" -> Log.d("payment-response", "Your transaction was REJECTED")

                            "1009" -> Log.d("payment-response", "Your Transaction was  CANCELLED.")
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }



    }


}