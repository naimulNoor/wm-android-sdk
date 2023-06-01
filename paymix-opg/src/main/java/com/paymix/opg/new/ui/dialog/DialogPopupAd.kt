package com.walletmix.paymixbusiness.ui.dialog


import android.annotation.SuppressLint
import android.app.ActionBar.LayoutParams
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.walletmix.paymixbusiness.base.BaseDialogFragment
import com.walletmix.paymixbusiness.data.prefs.PreferenceManager
import com.walletmix.paymixbusiness.R


class DialogPopupAd() : BaseDialogFragment() {

    lateinit var mPrefs: PreferenceManager


    companion object {
        @JvmStatic
        val TAG: String? = DialogPopupAd::class.java.simpleName

        @JvmStatic
        fun newInstance(): DialogPopupAd {

            var args =Bundle()

            val f = DialogPopupAd()
            f.arguments = args
            return f

        }
    }

    override fun getDialogView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        saveInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.popup_dialog, container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("SetTextI18n")
    override fun onDialogViewReady(getArguments: Bundle?) {


    }




    override fun onPause() {
        super.onPause()
        if(dialog!!.isShowing && dialog!=null){
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        val metrics: DisplayMetrics = resources.displayMetrics
        val width: Int = metrics.widthPixels
        val height: Int = metrics.heightPixels


        Log.d("width-px",metrics.widthPixels.toString())
        Log.d("height-px",metrics.heightPixels.toString())


        if(width>=1080){
            //higher devices
            Log.d("devices","higher-resulation")
            //dialog?.window?.setLayout(width-58, height)
            dialog?.window?.setLayout(width-58, LayoutParams.WRAP_CONTENT)
        }else{
            //lower devices
            Log.d("devices","lower-resulation")

//            val height: Int = AppUtils.shared.pxFromDp(requireContext(),350f).toInt()



            dialog?.window?.setLayout(width-50, LayoutParams.WRAP_CONTENT)
        }
        //dialog?.window?.setLayout(width-58, height)
        //dialog?.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = true
    }



    }
