package com.paymix.opg.base

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.wallemix.paymix.opg.R

import com.paymix.opg.data.prefs.PrefKeys
import com.paymix.opg.data.prefs.PreferenceManager


abstract class BaseDialogFragment : DialogFragment() {


    open fun BaseDialogFragment() {}



    lateinit var rootView: View
    // Dialog >>>>
    var loader: AlertDialog? = null

    var deviceId:String=""
    var appVersionCode:String=""
    var imei:String=""
    var macAddress:String=""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = getDialogView(inflater, container, savedInstanceState)

        return rootView
    }



    protected abstract fun getDialogView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        saveInstanceState: Bundle?
    ): View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE) //TODO :: chane on 15sept(release day)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setGravity(Gravity.CENTER)
        isCancelable = false
        onDialogViewReady(arguments)


    }

    protected abstract fun onDialogViewReady(getArguments: Bundle?)

    override fun onDestroyView() {
        super.onDestroyView()

    }

    public fun showProgressDialog() {
        if (loader == null) {
            val loaderBuilder = AlertDialog.Builder(context)
            loaderBuilder.setCancelable(false)
            val loadingView = LayoutInflater.from(context).inflate(R.layout.app_loading_view, null)
            val gifImgView = loadingView.findViewById<ImageView>(R.id.img_loader)
            Glide.with(requireContext()).load(R.raw.lottie_loader).into(gifImgView)
            loaderBuilder.setView(loadingView)
            loader = loaderBuilder.create()
            loader?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        loader?.show()
    }

    public fun hideProgressDialog() {
        loader?.cancel()
    }

    fun showErrorDialog(title: String? = null, message: String?, titleFullRed: Boolean = false) {
        try{
            val dialogError: DialogError = DialogError.newInstance(title, message, titleFullRed, animId = R.raw.failed)
            fragmentManager?.let { fm ->
                dialogError.show(fm, DialogError.TAG)
            }
        }catch (e:Exception){

        }

    }

    override fun onPause() {
        super.onPause()
        //TODO::need to test
        //lower device cannot see the dialog onResume app but upper version can check
//        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
//        {
//            dismiss()
//        }


    }

     fun _getHeaderData(mPrefs: PreferenceManager) {

         deviceId= mPrefs.getString(PrefKeys.DEVICEID,"NO DEVICE ID")!!
         appVersionCode=mPrefs.getString(PrefKeys.APPVERSIONCODE,"NO Version Code")!!
         macAddress= mPrefs.getString(PrefKeys.MACADDRESS,"NO MACADDRESS")!!
         imei= mPrefs.getString(PrefKeys.IMEI,"NO IMEI")!!
     }

    open fun runAnimatedIcon(){}






}