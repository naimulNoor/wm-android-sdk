package com.walletmix.paymixbusiness.ui.view.auth.verify_otp

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.EditText
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.walletmix.paymixbusiness.ui.view.auth.verify_otp.VerifyOtpContract
import com.walletmix.paymixbusiness.ui.view.auth.verify_otp.VerifyOtpPresenter
import com.walletmix.paymixbusiness.service.sms_retriver.SMSReceiver
import com.walletmix.paymixbusiness.utils.GenericTextWatcher
import com.walletmix.paymixbusiness.utils.KeyboardUtils
import com.walletmix.paymixbusiness.R
import com.walletmix.paymixbusiness.databinding.ActivityVerifyOtpBinding
import com.walletmix.paymixbusiness.base.MvpBaseActivity
import java.util.*

class VerifyOtpActivity : MvpBaseActivity<VerifyOtpPresenter>(), VerifyOtpContract.View{
   // var dialogSuccess: DialogSuccess? = null
    private var countDownTimer: CountDownTimer? = null
    var remainingTimeInSeconds: Int = 60
    private val OTP_TIMER_GAP_IN_MILISECONDS: Long = 1000


    //otp auto read
    private var intentFilter: IntentFilter? = null
    private var smsReceiver: SMSReceiver? = null
    private var isSmsReceiverRegistered: Boolean = false


    private lateinit var binding: ActivityVerifyOtpBinding


    override fun getContentView(): View {
        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        val view = binding.root
        return view
    }

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {

        binding.otp.btnOk.setOnClickListener {  }
        binding.otp.tvResendCode


        binding.otp.otpBoxTwo.setOnClickListener {
            binding.otp.otpBoxTwo.setBackgroundResource(R.drawable.otp_box_background)
        }
        binding.otp.otpBoxThree.setOnClickListener {
            binding.otp.otpBoxThree.setBackgroundResource(R.drawable.otp_box_background)
        }
        binding.otp.otpBoxFour.setOnClickListener {
            binding.otp.otpBoxFour.setBackgroundResource(R.drawable.otp_box_background)
        }



// Starting OTP Timer >>>>>>
        startOtpTimer()



//            if(!isSmsReceiverRegistered){
//                isSmsReceiverRegistered = true
//                registerReceiver(smsReceiver, intentFilter)
//            }


        GenericTextWatcher()
            .registerEditText( binding.otp.otpBoxOne)
            .registerEditText( binding.otp.otpBoxTwo)
            .registerEditText(binding.otp.otpBoxThree)
            .registerEditText(binding.otp.otpBoxFour)

            .setCallback(object : GenericTextWatcher.TextWatcherWithInstance() {

                override fun afterTextChanged(editText: EditText?, editable: Editable?) {
                    val strLength = editable.toString().length
                    when (editText) {
                        binding.otp.otpBoxOne -> if (strLength == 1)  binding.otp.otpBoxTwo.requestFocus()
                        binding.otp.otpBoxTwo -> if (strLength == 1) binding.otp.otpBoxThree.requestFocus() else  binding.otp.otpBoxOne.requestFocus()
                        binding.otp.otpBoxThree -> if (strLength == 1) binding.otp.otpBoxFour.requestFocus() else  binding.otp.otpBoxTwo.requestFocus()
                        binding.otp.otpBoxFour -> if (strLength == 0) binding.otp.otpBoxThree.requestFocus() else {
                            binding.otp.otpBoxFour.clearFocus()
                            KeyboardUtils.sharedInstance.hideSoftInput(this as AppCompatActivity )

                        }
                    }

// Handling Verify Button on Otp Length >>>
                    val otp =
                        binding.otp.otpBoxOne.text.toString() +   binding.otp.otpBoxTwo.text.toString() +  binding.otp.otpBoxThree.text.toString() +  binding.otp.otpBoxFour.text.toString()
                    binding.otp.btnOk.isEnabled = otp.length == 6
                }
            })
    }


    private fun startOtpTimer() {
        binding.otp.tvResendCode.isEnabled = false

        countDownTimer = object :
            CountDownTimer((remainingTimeInSeconds * 1000).toLong(), OTP_TIMER_GAP_IN_MILISECONDS) {
            override fun onTick(secondsRemaining: Long) {
                var seconds = (secondsRemaining / 1000)
                val minutes = seconds / 60
                seconds %= 60
                binding.otp.tvOtpTimer.text =
                    String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds)

            }

            override fun onFinish() {
                binding.otp.tvOtpTimer.text = "0"
                binding.otp.tvResendCode.isEnabled = true

            }
        }

        countDownTimer?.start()
    }
    private fun initSmsListener() {
        Log.d("SMS-get","start listing")
        val client = SmsRetriever.getClient(this)
        client.startSmsRetriever()
    }

    private fun initBroadCast() {
        Log.d("SMS-get","broadcust")
        intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        smsReceiver = SMSReceiver()
        smsReceiver?.setOTPListener(object : SMSReceiver.OTPReceiveListener {
            override fun onOTPReceived(otp: String?) {
                Log.d("SMS-get",otp!!)
                otp.let {
                    binding.otp.otpBoxOne.setText(otp[0].toString())
                    binding.otp.otpBoxTwo.setText(otp[1].toString())
                    binding.otp.otpBoxThree.setText(otp[2].toString())
                    binding.otp.otpBoxFour.setText(otp[3].toString())


                    binding.otp.btnOk.isEnabled = true
                }
                //verifyOtpBtnDidTapped()

            }
        })
    }

}

