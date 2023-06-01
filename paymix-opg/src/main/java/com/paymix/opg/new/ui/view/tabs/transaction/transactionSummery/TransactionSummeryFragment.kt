package com.walletmix.paymixbusiness.ui.view.tabs.transaction.transactionSummery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.isVisible
import com.walletmix.paymixbusiness.data.network.api_response.transaction.TransactionSummeryResonseModel
import com.walletmix.paymixbusiness.databinding.FragmentTransactionSummeryBinding
import com.walletmix.paymixbusiness.base.BaseFragment
import kotlin.math.roundToInt


class TransactionSummeryFragment : BaseFragment<TransactionSummeryFrgPresenter>(),
    TransactionSummeryFrgContract.View {


    private var _binding: FragmentTransactionSummeryBinding? = null
    private val binding get() = _binding!!
    override fun getFragmentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        saveInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionSummeryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewReady(getArguments: Bundle?) {

        mPresenter.TransactionSummery()


        binding.totalTransactionAmountLayout.tvTitle.text = "Total Transaction Amount"
        binding.totalTransactionAmountLayout.llTransaction.isVisible = false
        binding.totalForeignTransactionAmountLayout.tvTitle.text = "Total Foreign Transaction Amount"
        binding.totalForeignTransactionAmountLayout.llTransaction.isVisible = false
        binding.totalWalletmixPayableAmountLayout.tvTitle.text = "Total Walletmix Payable Amount"
        binding.totalWalletmixPayableAmountLayout.llTransaction.isVisible = false
        binding.totalTransactionLayout.tvTitle.text = "Total Transaction"
        binding.totalTransactionLayout.llTransaction.isVisible = false
        binding.totalForeignTransactionLayout.tvTitle.text = "Total  Foreign Transaction"
        binding.totalForeignTransactionLayout.llTransaction.isVisible = false

        setMargins(binding.totalTransactionAmountLayout.llShowAmount,0,16,0,13)
        setMargins(binding.totalForeignTransactionAmountLayout.llShowAmount,0,16,0,13)
        setMargins(binding.totalWalletmixPayableAmountLayout.llShowAmount,0,16,0,13)
        setMargins(binding.totalTransactionLayout.llShowAmount,0,16,0,13)
        setMargins(binding.totalForeignTransactionLayout.llShowAmount,0,16,0,13)


    }

    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is MarginLayoutParams) {
            val p = view.layoutParams as MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }

    override fun TransactionSummeryResponse(response: TransactionSummeryResonseModel) {



       // val totalWmxPayable = response.data?.totalWmxPayable?.let { showTwoDigitAfterDecimalPoint(it) }
        var totalWmxPayable = 0.0
        if(response.data?.totalWmxPayable!=null){
             totalWmxPayable = showTwoDigitAfterDecimalPoint(response.data?.totalWmxPayable as Double)
        }


        binding.totalTransactionAmountLayout.tvSuccessAmount.text = response.data?.totalTransactionAmount.toString()
        binding.totalForeignTransactionAmountLayout.tvSuccessAmount.text = response.data?.totalForeignTransactionAmount.toString()
        binding.totalWalletmixPayableAmountLayout.tvSuccessAmount.text = totalWmxPayable.toString()
        binding.totalTransactionLayout.tvSuccessAmount.text = response.data?.transactionCount.toString()
        binding.totalForeignTransactionLayout.tvSuccessAmount.text = response.data?.foreignTransactionCount.toString()

    }

    private fun  showTwoDigitAfterDecimalPoint(number:Double):Double {
        return (number * 100.0).roundToInt() / 100.0
        //return String.format("%.2f", number)
    }

}