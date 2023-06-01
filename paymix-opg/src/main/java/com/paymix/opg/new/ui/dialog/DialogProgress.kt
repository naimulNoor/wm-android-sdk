package com.walletmix.paymixbusiness.ui.dialog

import android.annotation.SuppressLint
import android.app.ActionBar
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.walletmix.paymixbusiness.databinding.DialogUpdateProfileBinding
import com.walletmix.paymixbusiness.base.BaseDialogFragment



class DialogProgress: BaseDialogFragment() {
    private var userName: String? = ""

    private lateinit var _binding: DialogUpdateProfileBinding
    private val binding get() = _binding

    var dialogUpdateButtonTapped:DialogProgressUpdateCallback? = null

    companion object {

        @JvmStatic
        val TAG: String? = DialogProgress::class.java.simpleName
        @JvmStatic
        fun newInstance(
            title: String,
        ): DialogProgress {
            Log.d("username",title)
            var args =Bundle()
            args.putString("userName", title)
            val f = DialogProgress()
            f.arguments = args
            return f

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userName=arguments?.getString("userName")
    }

    override fun getDialogView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        saveInstanceState: Bundle?
    ): View {
        _binding = DialogUpdateProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userName.text=userName
        binding.btnUpdateProfile.setOnClickListener(btnUpdateClickLister)
    }
    @SuppressLint("SetTextI18n")
    override fun onDialogViewReady(getArguments: Bundle?) {


    }
    override fun onStart() {
        super.onStart()
        val metrics: DisplayMetrics = resources.displayMetrics
        val width: Int = metrics.widthPixels
        val height: Int = metrics.heightPixels
        dialog?.window?.setLayout(width-58, ActionBar.LayoutParams.WRAP_CONTENT)



    }
    private fun updateButtonTappd() {
        Log.d("okDidtapped","yes")
        dismiss()
        dialogUpdateButtonTapped?.updateButtonTappd()
    }

    private  var btnUpdateClickLister=View.OnClickListener {
        updateButtonTappd()
    }


    interface DialogProgressUpdateCallback {
        fun updateButtonTappd()
    }





}
