package com.walletmix.paymixbusiness.ui.view.dialog

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.walletmix.paymixbusiness.base.BaseDialogFragment
import com.walletmix.paymixbusiness.databinding.DialogErrorBinding


class DialogError : BaseDialogFragment() {

    private var title: String? = null
    private var message: String?=null
    private var titleFullRed: Boolean? = null
    private var isDialogCancellable: Boolean? = null
    private var negativeBtnText: String? = null
    private var positiveBtnText: String? = null
    private var animId: Int? = null

    private lateinit var _binding: DialogErrorBinding
    private val binding get() = _binding
    lateinit var anim: LottieAnimationView

    var dialogErrorOkCallback: DialogError.DialogErrorOkCallback? = null




    companion object {

        @JvmStatic
        val TAG: String? = DialogError::class.java.simpleName

        @JvmStatic
        fun newInstance(
            title: String?,
            message: String?,
            titleFullRed: Boolean,
            animId:Int,
            isDialogCancellable: Boolean = false,
            negativeBtnText: String? = null,
            positiveBtnText: String? = null,
        ): DialogError {
            var args =Bundle()
            Log.d("dialog-error-2",titleFullRed.toString())
            args.putString("title", title)
            args.putString("message",  message)
            args.putBoolean("titleFullRed", titleFullRed)
            args.putBoolean("isDialogCancellable", isDialogCancellable)
            args.putString("negativeBtnText", negativeBtnText)
            args.putString("positiveBtnText", positiveBtnText)
            args.putInt("animId", animId)
            val f = DialogError()
            f.arguments = args
            return f

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        message = if( arguments?.getString("message")==null){
            null
        }else{ arguments?.getString("message") }
        if(arguments?.getString("negativeBtnText")==""){
            negativeBtnText=null
        }
        if(arguments?.getString("positiveBtnText")==""){
            positiveBtnText=null
        }


        title= arguments?.getString("title")
        titleFullRed= arguments?.getBoolean("titleFullRed")
        animId=arguments?.getInt("animId")
        isDialogCancellable= arguments?.getBoolean("isDialogCancellable")
        negativeBtnText= arguments?.getString("negativeBtnText")
        positiveBtnText= arguments?.getString("positiveBtnText")




    }
    override fun getDialogView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        saveInstanceState: Bundle?
    ): View {
        _binding = DialogErrorBinding.inflate(inflater, container, false)
        val view = binding.getRoot()
        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //click listner
        binding.btnPositive.setOnClickListener(btnPositiveClickLister)
        binding.btnNegative.setOnClickListener(btnNegativeClickLister)

        isCancelable = isDialogCancellable!!

    }

    @SuppressLint("SetTextI18n")
    override fun onDialogViewReady(getArguments: Bundle?) {
        //run animation
        runAnimatedIcon()

        if(title!=""){
            if (message == null) {
               binding.tvMessage.visibility = View.GONE


            } else
                binding.tvMessage.text = message
            title?.let {
                if (titleFullRed == true) {
                    Log.d("dialog-error-5", titleFullRed.toString())
                    binding.tvTitle.text = title
                    binding.tvTitle.setTextColor(Color.RED)
                } else {
                    val titleSplit = it.split(" ")
                    if (titleSplit.size > 1) {
                        val lastWord = titleSplit.last()
                        val start = it.indexOf(lastWord)
                        val end = start + lastWord.length
                        val builder = SpannableStringBuilder()
                        builder.append(it)
                        builder.setSpan(
                            ForegroundColorSpan(Color.RED), start, end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        binding.tvTitle.text = builder
                    } else {
                        binding.tvTitle.text = it
                    }
                }
            } ?: run {
                binding.tvTitle.visibility = View.GONE
            }

            if (negativeBtnText != null) {
                binding.btnNegative.text = negativeBtnText
                binding.btnNegative.visibility = View.VISIBLE
            }
            if (positiveBtnText != null) {
                binding.btnPositive.text = positiveBtnText
            }

        }
        else{
            dismiss()
        }





    }


   override fun runAnimatedIcon(){
       anim = binding.animationView
       Log.d("animId", animId!!.toString())
       anim.setAnimation(animId!!)
       anim.playAnimation()
       anim.repeatCount=100
   }



    private fun okDidTapped() {
        Log.d("okDidtapped","yes")
        dismiss()
        dialogErrorOkCallback?.okBtnDidTapped()
    }



    private fun negativeBtnDidTapped() {
        dismiss()
    }

    interface DialogErrorOkCallback {
        fun okBtnDidTapped()
    }


    private  var btnNegativeClickLister=View.OnClickListener {
        negativeBtnDidTapped()
    }
    private  var btnPositiveClickLister=View.OnClickListener {
        okDidTapped()
    }







}




