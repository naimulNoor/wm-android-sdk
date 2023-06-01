package com.walletmix.paymixbusiness.ui.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.walletmix.paymixbusiness.base.BaseDialogFragment
import com.walletmix.paymixbusiness.databinding.DialogLayoutBinding


class DialogSuccess: BaseDialogFragment() {
    private var title: String? = ""
    private var message: String? = ""

    private var animId: Int? = null

    private lateinit var _binding: DialogLayoutBinding
    private val binding get() = _binding
    lateinit var anim: LottieAnimationView

    companion object {

        @JvmStatic
        val TAG: String? = DialogSuccess::class.java.simpleName
        @JvmStatic
        fun newInstance(
            title: String?,
            message: String?,
            animId: Int
        ): DialogSuccess {

            var args =Bundle()
            args.putString("title", title)
            args.putString("message", message)
            args.putInt("animId", animId)
            val f = DialogSuccess()
            f.arguments = args
            return f

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        message = if( arguments?.getString("message")==null){
            null
        }else{ arguments?.getString("message") }


        title= arguments?.getString("title")
        animId=arguments?.getInt("animId")
    }

    override fun getDialogView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        saveInstanceState: Bundle?
    ): View {
        _binding = DialogLayoutBinding.inflate(inflater, container, false)
        val view = binding.getRoot()
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onDialogViewReady(getArguments: Bundle?) {
        runAnimatedIcon()
        binding.tvTitle.text=title
        binding.tvMessage.text=message

        binding.btnPositive.setOnClickListener{
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

}
