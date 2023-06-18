package com.paymix.myapp

import android.os.Bundle
import android.os.Parcel
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.paymix.opg.WalletmixOnlinePaymentGateway
import com.paymix.opg.apiclient.data.reponse.PaymentResponse
import com.paymix.opg.appInterface.OPGResponseListener
import com.walletmix.myapp.R
import timber.log.Timber
import java.io.Serializable

class SecondActivity : AppCompatActivity() {
    private var walletmixOnlinePGateway: WalletmixOnlinePaymentGateway? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)



        walletmixOnlinePGateway = WalletmixOnlinePaymentGateway(this)

        val button: Button = findViewById(R.id.init_payment)










//        try {
//            val response = intent.getStringExtra("response")
//            val jsonObject: JSONObject
//            val responseTxnStatus: String
//            if (response != null) {
//                if (response == "false") {
//                    Log.d(
//                        "payment-response",
//                        "Transaction was incomplete. Please try again to complete your transaction."
//                    )
//
//                } else {
//                    try {
//                        jsonObject = JSONObject(response)
//                        responseTxnStatus = jsonObject.getString("txn_status")
//                        when (responseTxnStatus) {
//                            "1000" -> Log.d("payment-response", "Your transaction was Success")
//
//                            "1001" -> Log.d("payment-response", "Your transaction was REJECTED")
//
//                            "1009" -> Log.d("payment-response", "Your Transaction was  CANCELLED.")
//                        }
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }


    }

    private fun successCall() {
        //Toast.makeText(applicationContext, "onSuccessPaymentRequest", Toast.LENGTH_SHORT).show()
    }


}