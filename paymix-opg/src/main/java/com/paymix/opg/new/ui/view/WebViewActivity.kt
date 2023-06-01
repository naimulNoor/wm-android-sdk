package com.walletmix.paymixbusiness.ui.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.http.SslError
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.walletmix.paymixbusiness.base.BaseActivity
import com.walletmix.paymixbusiness.utils.Keys.IntentKeys
import com.walletmix.paymixbusiness.R
import com.walletmix.paymixbusiness.data.prefs.PrefKeys
import com.walletmix.paymixbusiness.data.prefs.PreferenceManager

class WebViewActivity : BaseActivity() {

    private var url: String = ""
    private var title : String = ""
    private var isAuthNeeded : Boolean = false


    private lateinit var web_view:WebView
    private lateinit var tv_toolbar_title:TextView
    private lateinit var progress_bar:ImageView
    val headerMap = HashMap<String, String>()
    var loader: AlertDialog? = null
    private var doubleBackToExitPressedOnce = false

    private lateinit var binding: WebViewActivity

    override fun getContentView(): Int {
        return R.layout.activity_web_view
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {

        web_view = findViewById(R.id.web_view)
        tv_toolbar_title = findViewById(R.id.tv_toolbar_title)
        progress_bar = findViewById(R.id.progress_bar)


        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val bundle = intent.getBundleExtra(IntentKeys.DATA_BUNDLE)
        if (bundle != null) {
            url = bundle.getString(IntentKeys.WEB_URL) ?: ""
            title = bundle.getString(IntentKeys.TITLE) ?: ""
            isAuthNeeded= bundle.getBoolean(IntentKeys.IS_AUTH_NEEDED) ?:false
        }

        tv_toolbar_title.text = title

        if(isAuthNeeded){
            val token = PreferenceManager(this).getString(PrefKeys.TOKEN)
            headerMap["Authorization"] = "Bearer $token"
        }



        /**
         *  Web View necessary settings
         * */
        web_view.settings.javaScriptEnabled = true
        web_view.isClickable = true
        web_view.settings.domStorageEnabled = true
        //web_view.settings.setAppCacheEnabled(false)
        web_view.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        web_view.clearCache(true)
        web_view.settings.allowFileAccessFromFileURLs = true
        web_view.settings.allowUniversalAccessFromFileURLs = true
        web_view.settings.loadWithOverviewMode = true
        web_view.settings.useWideViewPort = true
        web_view.webViewClient = CheckoutWebViewClient()

        web_view.loadUrl(url,headerMap)


        startAnimation()
    }

    private inner class CheckoutWebViewClient : WebViewClient() {
        @SuppressLint("WebViewClientOnReceivedSslError")
        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(getContext())
            alertBuilder.setCancelable(true)
            alertBuilder.setTitle("SSL Certificate error")
            alertBuilder.setMessage("SSL certificate error has been occurred. Do you want to continue anyway?")
            alertBuilder.setNegativeButton("No", null)
            alertBuilder.setPositiveButton("Continue") { _, _ -> handler?.proceed() }
            alertBuilder.create().show()
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            progress_bar.visibility = View.VISIBLE

        }

        override fun onPageFinished(view: WebView?, url: String?) {
            try{
                if (loader != null && loader!!.isShowing()) {
                    loader?.dismiss()
                }
            }catch (_:Exception){

            }



            progress_bar.visibility = View.GONE
            web_view.visibility = View.VISIBLE
        }
    }



    private fun startAnimation(){
        try{
            val loaderBuilder = AlertDialog.Builder(this)
            loaderBuilder.setCancelable(false)
            val loadingView = LayoutInflater.from(this).inflate(R.layout.app_loading_view, null)
            val gifImgView = loadingView.findViewById<ImageView>(R.id.img_loader)
            Glide.with(this).asGif().load(R.raw.lottie_loader).into(gifImgView)
            loaderBuilder.setView(loadingView)
            loader = loaderBuilder.create()
            loader?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            loader?.show()
        }catch (e:Exception){

        }
    }

    override fun onResume() {
        super.onResume()
        if(loader!=null){
            loader!!.setOnKeyListener(object : DialogInterface.OnKeyListener {
                override fun onKey(
                    dialog: DialogInterface?,
                    keyCode: Int, event: KeyEvent?
                ): Boolean {
                    return if (keyCode == KeyEvent.KEYCODE_BACK) {
                        // To dismiss the fragment when the back-button is pressed.
                        loader!!.dismiss()
                        finish()

                        true
                    } else false
                }
            })
        }

    }
}